package com.example.alarmdrools.service;

import com.clarity.alarmaction.action.ActionResult;
import com.example.alarmdrools.config.QueryPropertyConfig;
import com.example.alarmdrools.enums.Network;
import com.example.alarmdrools.feign.FeignAlarmAction;
import com.example.alarmdrools.feign.FeignService;
import com.example.alarmdrools.feign.FeignServiceManager;
import com.example.alarmdrools.inteface.AttributeHelper;
import com.example.alarmdrools.model.ApplicationVariables;
import com.example.alarmdrools.model.TripleString;
import com.example.alarmdrools.model.dto.*;
import com.example.alarmdrools.model.entity.AlarmComments;
import com.example.alarmdrools.model.entity.RuleAttribute;
import com.example.alarmdrools.repository.AlarmRepository;
import com.example.alarmdrools.repository.RuleAttributeRepository;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class AlarmService {


    private Map<String, String> siteIdToFSMGroup;
    private AttributeHelper attributeHelper;
    private RuleAttributeRepository ruleAttributeRepository;
    private CreateFaultDTO createFaultDTO = new CreateFaultDTO();
    private KieSession kieSession;
    private AlarmRepository alarmRepository;
    private FeignService feignService;
    private FeignServiceManager feignServiceManager;
    private FeignAlarmAction feignAlarmAction;
    private ApplicationVariables appVars;
    private Map<String, String> cityMap;
    private Map<String, String> siteIdToArea;


    private static final String FSM_GROUP_QUERY = "SELECT mfwo_province c1 ,mfwo_workgroup c2 FROM mci_clarity.mci_fsm_workgroups";
    private static final String CONTRACTOR_QUERY = "SELECT ma_location c1, upper(ma_flm_subcontractor) c2\n" +
            "FROM mci_clarity.mci_aras\n" +
            "WHERE REGEXP_LIKE ( ma_location, '^TH\\d{4}(-\\w)?$') and ma_flm_subcontractor is not null";
    private static final String AREA_QUERY = "SELECT locn_ttname c1, area_code c2, area_area_code c3\n" +
            "FROM locations l, areas a\n" +
            "WHERE l.locn_area_code = a.area_code AND REGEXP_LIKE ( locn_ttname, '^[A-Z]{2}\\d{4}$' )";

    private EntityManager entityManager;

    @PostConstruct
    public void doIt() {
        refreshData();
    }

    @Autowired
    public AlarmService(KieSession kieSession, AttributeHelper attributeHelper, EntityManager entityManager, RuleAttributeRepository ruleAttributeRepository, ApplicationVariables appVars, AlarmRepository alarmRepository, FeignService feignService, FeignServiceManager feignServiceManager, FeignAlarmAction feignAlarmAction) {
        initCityMap();
        this.kieSession = kieSession;
        this.alarmRepository = alarmRepository;
        this.feignService = feignService;
        this.feignServiceManager = feignServiceManager;
        this.feignAlarmAction = feignAlarmAction;
        this.appVars = appVars;
        this.entityManager = entityManager;
        this.ruleAttributeRepository = ruleAttributeRepository;
        this.attributeHelper = attributeHelper;
    }

    public List<AlarmComments> getAlarms() {
        return alarmRepository.findAll();
    }

    public List<CreateFaultDTO> getRelatedAlarms(String token, String userId, String severity, String comment) throws IOException {
        String query = QueryPropertyConfig.getProperties().getProperty("get.alarms");
        List<Alarm> relatedAlarms = entityManager.createNativeQuery(query, "AlarmMapping").getResultList();
        List<CreateFaultDTO> createFaultDTOS = new ArrayList<>();
        CreateFaultDTO createFaultDTO2 = new CreateFaultDTO();
        int counter = 0;
        for (Alarm alarm : relatedAlarms) {

            kieSession.insert(alarm);
            kieSession.fireAllRules();

            if (alarm.getAction() != null) {
                counter++;
                doAlarmAck(token, "ACK", Stream.of(alarm.getAlasOccurrenceId()).collect(Collectors.toList()), severity, comment);

                if (getTechnologies(alarm.getAlamEqupIndex(), alarm.getAlamEventType(), alarm.getAlamProbableCause(), alarm.getAlasMessage()) != null) {
                    for (char c : alarm.getAction().toCharArray()) {
                        if (c == 'F') {
                            createFaultDTO.setCause(alarm.getCause());
                            createFaultDTO.setImpact(alarm.getImpact());
                            createFaultDTO.setWorgName(alarm.getWorkGroup());
                            createFaultDTO.setReportedTime(alarm.getAlamReported());
                            createFaultDTO.setReportedby("COMPLEX_ADMIN");
                            createFaultDTO.setPriority(2L);
                            createFaultDTO.setDescription("this is a test. ");
                            createFaultDTO.setType(alarm.getType());
                            createFaultDTO.setRegnCode(alarm.getAlamAreaCode());
                            createFaultDTO.setEntity(entity(alarm.getCause()));
                            LinksDTO linksDTO = new LinksDTO();
                            List<LinksDTO> linksDTOS = new ArrayList<>();
                            linksDTO.setForeignId(alarm.getAlasOccurrenceId());
                            linksDTO.setForeignParent(alarm.getAlamId().toString());
                            linksDTO.setForeignType("ALARMS");
                            linksDTOS.add(linksDTO);

                            TicketParams ticketParams = new TicketParams();
                            ticketParams.setSiteId(alarm.getAlamLocnTtname());
                            ticketParams.setAttributeCategory(getSubCategory(alarm.getAlamMansName()));
                            ticketParams.setEventClearDate(alarm.getAlasCleared());
                            ticketParams.setEventStartDate(alarm.getAlamReported());
                            ticketParams.setEventType(alarm.getCause());
                            Set<String> set = new HashSet<String>();
                            set.add(getTechnologies(alarm.getAlamEqupIndex(), alarm.getAlamEventType(), alarm.getAlamProbableCause(), alarm.getAlasMessage()));
                            ticketParams.setTechnologies(set);
                            ticketParams.setNetworks(getNetwork(alarm.getAlamEqupIndex(), alarm.getAlamEventType(), alarm.getAlamProbableCause(), alarm.getAlasMessage()));

                            createFaultDTO.setLinks(linksDTOS);
                            createFaultDTO.setAttributes(extractAttributes(ticketParams, alarm));
                            createFaultDTO2 = createFaultWOA(token, createFaultDTO);
                            createFaultDTOS.add(createFaultDTO2);
                        }
                        if (c == 'J') {
                            FaultJobTicketDTO faultJobTicketDTO = new FaultJobTicketDTO(getFsmWorkGroup(alarm.getAlamLocnTtname()), "1", new Date(), getAreaCode(alarm.getAlamLocnTtname()), "MCI", "");
                            if (faultJobTicketDTO.getFsmWorkGroup() != null) {
                                createFaultJobTicket(createFaultDTO2.getNumber().toString(), faultJobTicketDTO, token, userId);
                            } else
                                System.out.println("no job ticket workgroup!!");
                        }
                        if (c == 'W') {
                            WorkOrderDetailDTO workOrderDetailDTO = new WorkOrderDetailDTO();
                            workOrderDetailDTO.setPromNumber(createFaultDTO2.getNumber());
                            workOrderDetailDTO.setOrderType("CREATE");
                            workOrderDetailDTO.setWorkGroup(alarm.getWorkGroup());
                            workOrderDetailDTO.setDescription("avasvsadvsdvsd");

                            insertWorkOrder(token, workOrderDetailDTO, true, userId);
                        }
                    }
                }
            }
        }
            System.out.println("total processed alarms = " + counter);
            return createFaultDTOS;
        }


    public CreateFaultDTO createFaultWOA(String token, CreateFaultDTO createFaultDTO) throws IOException {
        return feignService.createFaultWOA(token, createFaultDTO);
    }

    public CreatWorkOrderResponseDTO insertWorkOrder(String token, WorkOrderDetailDTO workOrderDTO, boolean forceCreate, String userId) {
        return feignServiceManager.insert(token, workOrderDTO, forceCreate, userId);
    }

    //    public ActivityJobTicketDTO createJobTicket( String parentId,ActivityJobTicketDTO activityJobTicketDTO,String token){
//        return feignService.createJrobTicket(parentId,activityJobTicketDTO,token);
//
//    }
    public JobTicketDetailDTO createFaultJobTicket(String parentId, FaultJobTicketDTO faultJobTicketDTO, String token, String userId) {
        return feignService.createFaultJobTicket(parentId, faultJobTicketDTO, token, userId);
    }


    public ActionResult doAlarmAction(String token, String type, List<String> occurrenceIds, String severity, String comment) {
        try {
            String message = feignAlarmAction.doAlarmAction(token, type, occurrenceIds, severity, comment).getMessage();
            ActionResult actionResult = new ActionResult(message);
            return actionResult;
        } catch (Exception ex) {
            System.out.println(ex);
        }

        ActionResult actionResult = new ActionResult("completed but there is an unhandled exception");
        return actionResult;
    }

    public ActionResult doAlarmAck(String token, String type, List<String> occurrenceIds, String severity, String comment) {
        try {
            String message = feignAlarmAction.doAlarmAck(token, type, occurrenceIds, severity, comment).getMessage();
            return new ActionResult(message);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        ActionResult actionResult = new ActionResult("completed but there is an unhandled exception");
        return actionResult;
    }

    public List<ProblemLinks> createFaultLinks(Long problemNumber, List<LinksDTO> links, String token, String userId) {
        return feignService.createFaultLinks(problemNumber, links, token, userId);
    }

    public List<ProblemAttributeDTO> updateAttributes(long promNumber, List<ProblemAttributeDTO> attributes, String token, String userId) {
        return feignService.updateAttributes(promNumber, attributes, token, userId);
    }

    public List<ProblemAttributeDTO> getProblemAttributes(@PathVariable("id") long promNumber) {
        return feignService.getProblemAttributes(promNumber);
    }

    public Boolean addAttributes(
            @PathVariable("id") Long promNumber,
            @RequestBody @Valid List<ProblemAttributeDTO> attributes,
            @RequestHeader(value = "Authorization") String token) {
        return feignService.addAttributes(promNumber, attributes, token);
    }

    public List<ProblemAttributeTemplateItemDTO> getAttributesByFaultType(@PathVariable("type") String type) {
        return feignService.getAttributesByFaultType(type);
    }


    public CreateFaultDTO createFault(String token, String problem, String userId, List<MultipartFile> files) throws IOException {
        return feignService.createFault(token, problem, userId, files);
    }

    private String city(String province) {
        return cityMap.get(province);
    }

    private void initCityMap() {
        cityMap = new HashMap<>();
        cityMap.put("ALBORZ_P", "ALB");
        cityMap.put("ARDABIL_P", "ARD");
        cityMap.put("AZARGHARBI_P", "AZW");
        cityMap.put("BUSHEHR_P", "BSH");
        cityMap.put("CHAHARMAHAL_P", "CHR");
        cityMap.put("FARS_P", "FRS");
        cityMap.put("GHOM_P", "QOM");
        cityMap.put("GILAN_P", "GLN");
        cityMap.put("GOLESTAN_P", "GOL");
        cityMap.put("HAMEDAN_P", "HMD");
        cityMap.put("HORMOZGAN_P", "HRM");
        cityMap.put("ILAAM_P", "ILM");
        cityMap.put("ISFAHAN_P", "ESF");
        cityMap.put("KERMANSHAH_P", "KRS");
        cityMap.put("KERMAN_P", "KRM");
        cityMap.put("KHORASANRAZAVI_P", "KHR");
        cityMap.put("KHUZESTAN_P", "KHZ");
        cityMap.put("KOHKILOOYE_P", "KBA");
        cityMap.put("LORESTAN_P", "LOR");
        cityMap.put("KURDESTAN_P", "KRD");
        cityMap.put("MARKAZI_P", "MRK");
        cityMap.put("MAZANDARAN_P", "MZD");
        cityMap.put("N KHORASAN_P", "KHN");
        cityMap.put("QAZVIN_P", "GZV");
        cityMap.put("S KHORASAN_P", "KHS");
        cityMap.put("SEMNAN_P", "SEM");
        cityMap.put("SISTAN_P", "SYS");
        cityMap.put("TEHRAN_P", "TEH");
        cityMap.put("YAZD_P", "YZD");
        cityMap.put("ZANJAN_P", "YZD");
    }

    private String entity(String cause) {
        return cause.substring(0, cause.indexOf(":"));

    }

    private String getFsmWorkGroup(String workGroup) {
        return siteIdToFSMGroup.get(workGroup);
    }

    private String getAreaCode(String siteId) {
        return siteIdToArea.get(siteId);
    }

    private Map<String, String> getMappedData(String query) {

        List<TripleString> data = entityManager.createNativeQuery(query,
                "DualStringMapping").getResultList();
        return data.stream()
                .filter(m -> !StringUtils.isEmpty(m.getFirstString()) && !StringUtils.isEmpty(m.getSecondString()))
                .collect(toMap(TripleString::getFirstString, TripleString::getSecondString,
                        (oldValue, newValue) -> oldValue));
    }

    public void refreshData() {
        List<TripleString> areaData = entityManager.createNativeQuery(AREA_QUERY, "TripleStringMapping").getResultList();
        siteIdToArea = areaData.stream()
                .filter(m -> !StringUtils.isEmpty(m.getFirstString()) && !StringUtils.isEmpty(m.getSecondString()))
                .collect(toMap(TripleString::getFirstString, TripleString::getSecondString,
                        (oldValue, newValue) -> oldValue));

        refreshSiteIdToFSMGroup(areaData);

    }

    private void refreshSiteIdToFSMGroup(List<TripleString> areaData) {
        Map<String, String> provinceToGroup = getMappedData(FSM_GROUP_QUERY);
        Map<String, String> siteToContractor = getMappedData(CONTRACTOR_QUERY);
        siteIdToFSMGroup = new HashMap<>();
        for (TripleString area : areaData) {
            if (area.getThirdString().equals("TEHRAN_P")) {
                String contractor = siteToContractor.get(area.getFirstString());
                if (contractor != null && appVars.getContractorToFSMGroup().containsKey(contractor))
                    siteIdToFSMGroup.put(area.getFirstString(), appVars.getContractorToFSMGroup().get(contractor));
            } else if (!StringUtils.isEmpty(area.getThirdString())) {
                String group = provinceToGroup.get(area.getThirdString());
                if (group != null)
                    siteIdToFSMGroup.put(area.getFirstString(), group);
            }
        }
    }

    public List<ProblemAttributeDTO> extractAttributes(TicketParams ticketParams, Alarm alarm) {
        List<RuleAttribute> ruleAttributes =
                ruleAttributeRepository.findAllByCategory(
                        ticketParams.getAttributeCategory());
        List<ProblemAttributeDTO> result = new ArrayList<>();
        ruleAttributes.forEach(ruleAttribute ->
                result.addAll(attributeHelper.getTicketAttributes(ruleAttribute, ticketParams, alarm)));
        return result;
    }

    public String getSubCategory(String alamMansName) {
        if (alamMansName.contains("ERICSSON"))
            return "ERICSSON_2G";
        if (alamMansName.contains("NSN"))
            return "NOKIA_2G";
        if (alamMansName.contains("HUAWEI"))
            return "HUAWEI_2G";
        return null;
    }

    public String getTechnologies(String alamEqupIndex, String eventType,String alamProbableCause,String alasMessage ) {
        Pattern LocnPattern = Pattern.compile(".*([A-Z]{2})([A-Z0-9]{1}[GDUL]{1,3})(\\d{4}).*");
//        Pattern LocnPattern = Pattern.compile(".*([A-Z]{2})(\\d{4}).*");
        String result = null;
        if (alamEqupIndex != null && eventType != null && alamProbableCause != null && alasMessage != null) {
            Matcher matcherEqupIndex = LocnPattern.matcher(alamEqupIndex);
            Matcher matcherEventType = LocnPattern.matcher(eventType);
//            Matcher matcherLocnTTname = LocnPattern.matcher(alamLocnTTname);
            Matcher matcherAlasMessage=LocnPattern.matcher(alasMessage);
            Matcher matcherAlamProbableCause=LocnPattern.matcher(alamProbableCause);

            if (matcherEqupIndex.matches()) {
                result = matcherEqupIndex.group(2);
            } else if (matcherEventType.matches()) {
                result = (matcherEventType.group(2));

            }
          else if (matcherAlamProbableCause.matches()) {
                result = matcherAlamProbableCause.group(2);
            } else if (matcherAlasMessage.matches()) {
                result = matcherAlasMessage.group(2);
            }else {
                return null;
            }
        }
        return result;
    }

    public EnumSet<Network> getNetwork(String alamEqupIndex, String eventType, String alamProbableCause,String alasMessage) {
        String technology = getTechnologies(alamEqupIndex, eventType, alamProbableCause,alasMessage);
        List<Network> networks = new ArrayList<>();
        if (technology == null)
            return EnumSet.noneOf(Network.class);
        if (technology.contains("G"))
            networks.add(Network.G2G);
        if (technology.contains("U"))
            networks.add(Network.G3G);
        if (technology.contains("L"))
            networks.add(Network.G4G);
        return EnumSet.copyOf(networks);

    }


}