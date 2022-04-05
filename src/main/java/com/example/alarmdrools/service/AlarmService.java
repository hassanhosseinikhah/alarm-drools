package com.example.alarmdrools.service;

import com.clarity.alarmaction.action.ActionResult;
import com.example.alarmdrools.config.QueryPropertyConfig;
import com.example.alarmdrools.enums.Network;
import com.example.alarmdrools.feign.FeignAlarmAction;
import com.example.alarmdrools.feign.FeignService;
import com.example.alarmdrools.feign.FeignServiceManager;
import com.example.alarmdrools.inteface.AttributeHelper;
import com.example.alarmdrools.map.ConvertToDTO;
import com.example.alarmdrools.model.ApplicationVariables;
import com.example.alarmdrools.model.TripleString;
import com.example.alarmdrools.model.dto.*;
import com.example.alarmdrools.model.entity.AlarmComments;
import com.example.alarmdrools.model.entity.AlarmStates;
import com.example.alarmdrools.model.entity.RuleAttribute;
import com.example.alarmdrools.repository.AlarmCoomentsRepository;
import com.example.alarmdrools.repository.AlarmStatesRepository;
import com.example.alarmdrools.repository.RuleAttributeRepository;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
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

    @Value("${query.FSM_GROUP_QUERY}")
    private String FSM_GROUP_QUERY;

    @Value("${query.CONTRACTOR_QUERY}")
    private String CONTRACTOR_QUERY;

    @Value("${query.AREA_QUERY}")
    private String AREA_QUERY;

    @Value("${query.GET_ALARMS}")
    private String GET_ALARMS;


    private ConvertToDTO convertToDTO;
    private ProblemService problemService;
    private RedisProblemService redisProblemService;
    private EntityManager entityManager;
    private Map<String, String> siteIdToFSMGroup;
    private AttributeHelper attributeHelper;
    private RuleAttributeRepository ruleAttributeRepository;
    private CreateFaultDTO createFaultDTO = new CreateFaultDTO();
    private KieSession kieSession;
    private FeignService feignService;
    private FeignServiceManager feignServiceManager;
    private FeignAlarmAction feignAlarmAction;
    private ApplicationVariables appVars;
    private Map<String, String> cityMap;
    private Map<String, String> siteIdToArea;
    private Map<String, String> map;
    private AlarmStatesRepository alarmStatesRepository;


    @PostConstruct
    public void doIt() {
        refreshData();
    }

    @Autowired
    public AlarmService(ConvertToDTO convertToDTO, ProblemService problemService, RedisProblemService redisProblemService,
                        KieSession kieSession, AttributeHelper attributeHelper,
                        EntityManager entityManager, RuleAttributeRepository ruleAttributeRepository,
                        AlarmStatesRepository alarmStatesRepository, ApplicationVariables appVars,  FeignService feignService,
                        FeignServiceManager feignServiceManager, FeignAlarmAction feignAlarmAction) {


        initCityMap();
        initWorkGroup();
        this.problemService = problemService;
        this.redisProblemService = redisProblemService;
        this.kieSession = kieSession;

        this.feignService = feignService;
        this.feignServiceManager = feignServiceManager;
        this.feignAlarmAction = feignAlarmAction;
        this.appVars = appVars;
        this.entityManager = entityManager;
        this.ruleAttributeRepository = ruleAttributeRepository;
        this.attributeHelper = attributeHelper;
        this.alarmStatesRepository = alarmStatesRepository;
        this.convertToDTO = convertToDTO;


    }



    public List<CreateFaultDTO> createFJW(String token, String userId, String severity, String comment) throws IOException {

        List<Alarm> relatedAlarms = entityManager.createNativeQuery(GET_ALARMS, "AlarmMapping").getResultList();
        CreateFaultDTO createFaultDTO = new CreateFaultDTO();
        List<CreateFaultDTO> createFaultDTOS = new ArrayList<>();

        int counter = 0;
        for (Alarm alarm : relatedAlarms) {

            kieSession.insert(alarm);
            kieSession.fireAllRules();

            if (alarm.getAction() != null) {
                log.info("Alarm OccurrncedId  :  " + alarm.getAlasOccurrenceId());
                counter++;
                doAlarmAction(token, "ACK", Stream.of(alarm.getAlasOccurrenceId()).collect(Collectors.toList()), severity, comment);
                AlarmStates alarmStates = alarmStatesRepository.getByAlasOccurrenceId(alarm.getAlasOccurrenceId());

                if (alarmStates.getAlasAcknowledged() != null) {
                    if (linkedProblem(alarm, token, userId).size() == 0) {
                        if (getLocation(alarm) != null || getTechnologies(alarm) != null) {
                            for (char c : alarm.getAction().toCharArray()) {
                                if (c == 'F') {
                                    createFaultDTO = createFault(alarm, token);
                                    createFaultDTOS.add(createFaultDTO);


                                }
                                if (c == 'J') {
                                    FaultJobTicketDTO faultJobTicketDTO = new FaultJobTicketDTO(getFsmWorkGroup(alarm.getAlamLocnTtname()), "1", new Date(), getAreaCode(alarm.getAlamLocnTtname()), "MCI", "");
                                    if (faultJobTicketDTO.getFsmWorkGroup() != null) {
                                        createFaultJobTicket(createFaultDTO.getNumber().toString(), faultJobTicketDTO, token, userId);
                                    } else
                                        log.info("no job ticket workgroup!!");

                                }
                                if (c == 'W') {
                                    WorkOrderDetailDTO workOrderDetailDTO = new WorkOrderDetailDTO();
                                    workOrderDetailDTO.setPromNumber(createFaultDTO.getNumber());
                                    workOrderDetailDTO.setOrderType("CREATE");
                                    if (alarm.getWorkOrder().equals("Province/FLM")) {
                                        workOrderDetailDTO.setWorkGroup(map.get(getGroupLocation(alarm)) + "_FO_RAN");
                                    } else {
                                        workOrderDetailDTO.setWorkGroup(alarm.getWorkOrder());
                                    }
                                    workOrderDetailDTO.setDescription("avasvsadvsdvsd");

                                    insertWorkOrder(token, workOrderDetailDTO, true, userId);
                                }
                            }
                        } else {
                            log.info(" create fault NI Ticket");
                            createFaultNI(alarm, token);

                        }
                    }
                } else {
                    log.info(alarm.getAlasOccurrenceId() + "  not ack");

                }

            }
        }
        log.info("  END, total processed alarms = " + counter);

        return createFaultDTOS;


    }


    public CreateFaultDTO createFaultWOA(String token, CreateFaultDTO createFaultDTO) throws IOException {
        return feignService.createFaultWOA(token, createFaultDTO);
    }

    public CreatWorkOrderResponseDTO insertWorkOrder(String token, WorkOrderDetailDTO workOrderDTO, boolean forceCreate, String userId) {
        return feignServiceManager.insert(token, workOrderDTO, forceCreate, userId);
    }


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

        List<TripleString> data = entityManager.createNativeQuery(query, "DualStringMapping").getResultList();
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


    public EnumSet<Network> getNetwork(Alarm alarm) {
        String technology = getTechnologies(alarm);
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

    public CreateFaultDTO createFault(Alarm alarm, String token) throws IOException {
        CreateFaultDTO createFaultDTO2 = new CreateFaultDTO();
        createFaultDTO.setCause(alarm.getCause());
        createFaultDTO.setImpact(alarm.getImpact());
        createFaultDTO.setWorgName(alarm.getWorkGroup());
        createFaultDTO.setReportedTime(alarm.getAlamReported());
        createFaultDTO.setReportedby("COMPLEX_ADMIN");
        createFaultDTO.setPriority(2L);
        createFaultDTO.setDescription("COMPLEX_ADMIN");
        createFaultDTO.setType(alarm.getType());
        createFaultDTO.setRegnCode(alarm.getAlamAreaCode());
        createFaultDTO.setEntity(entity(alarm.getCause()));

        createFaultDTO.setLinks(createLink(alarm));
        createFaultDTO.setAttributes(extractAttributes(createTicketParams(alarm), alarm));
        createFaultDTO2 = createFaultWOA(token, createFaultDTO);
        log.info("Create Ticket  " + createFaultDTO2.getNumber());
        log.info("Alarm OccurrenceId" + alarm.getAlasOccurrenceId());
        ProblemDTO problemDTO = convertToDTO.MapToProlemDto(createFaultDTO2, alarm);
        redisProblemService.redisMyProblemDTO(problemDTO);
        return createFaultDTO2;


    }

    private List<LinksDTO> createLink(Alarm alarm) {
        LinksDTO linksDTO = new LinksDTO();
        List<LinksDTO> linksDTOS = new ArrayList<>();
        linksDTO.setForeignId(alarm.getAlasOccurrenceId());
        linksDTO.setForeignParent(alarm.getAlamId().toString());
        linksDTO.setForeignType("ALARMS");
        linksDTOS.add(linksDTO);
        return linksDTOS;

    }

    public TicketParams createTicketParams(Alarm alarm) {
        TicketParams ticketParams = new TicketParams();
        ticketParams.setSiteId(alarm.getAlamLocnTtname());
        ticketParams.setAttributeCategory(getSubCategory(alarm.getAlamMansName()));
        ticketParams.setEventClearDate(alarm.getAlasCleared());
        ticketParams.setEventStartDate(alarm.getAlamReported());
        ticketParams.setEventType(alarm.getCause());
        Set<String> set = new HashSet<String>();
        set.add(getTechnologies(alarm));
        ticketParams.setTechnologies(set);
        ticketParams.setNetworks(getNetwork(alarm));
        return ticketParams;

    }

    public CreateFaultDTO createFaultNI(Alarm alarm, String token) throws IOException {
        CreateFaultDTO createFaultDTO2 = new CreateFaultDTO();
        createFaultDTO.setImpact(alarm.getImpact());
        createFaultDTO.setWorgName(alarm.getWorkGroup());
        createFaultDTO.setReportedTime(alarm.getAlamReported());
        createFaultDTO.setReportedby("COMPLEX_ADMIN");
        createFaultDTO.setPriority(2L);
        createFaultDTO.setDescription("this is a test. ");

        createFaultDTO.setRegnCode(alarm.getAlamAreaCode());
        createFaultDTO.setEntity(entity(alarm.getCause()));
        createFaultDTO.setType("NETWORK_FAULT");
        createFaultDTO.setCause("NI:NI SITE");
        createFaultDTO.setLinks(createLink(alarm));
        createFaultDTO.setAttributes(extractAttributes(createTicketParamsNI(alarm), alarm));
        createFaultDTO2 = createFaultWOA(token, createFaultDTO);
        log.info("Create Fault NI  " + createFaultDTO2.getNumber());
        return createFaultDTO2;

    }

    public String getLocation(Alarm alarm) {

        Pattern LocnPattern = Pattern.compile(".*([A-Z]{2})(\\d{4}).*");
        if (alarm.getAlamLocnTtname() != null) {
            Matcher matcherLocnTtname = LocnPattern.matcher(alarm.getAlamLocnTtname());
            if (matcherLocnTtname.matches()) {
                return matcherLocnTtname.group(1) + matcherLocnTtname.group(2);
//
            }
        }
        if (alarm.getAlamEqupIndex() != null) {
            Matcher matcherEqupIndex = LocnPattern.matcher(alarm.getAlamEqupIndex());
            if (matcherEqupIndex.matches()) {
                return matcherEqupIndex.group(1) + matcherEqupIndex.group(2);
            }
        }
        if (alarm.getAlamEventType() != null) {
            Matcher matcherEventType = LocnPattern.matcher(alarm.getAlamEventType());
            if (matcherEventType.matches()) {
                return matcherEventType.group(1) + matcherEventType.group(2);
            }

        }
        if (alarm.getAlasMessage() != null) {
            Matcher matcherAlasMessage = LocnPattern.matcher(alarm.getAlasMessage());
            if (matcherAlasMessage.matches()) {
                return matcherAlasMessage.group(1) + matcherAlasMessage.group(2);
            }
        }
        if (alarm.getAlamPort() != null) {
            Matcher matcherAlamPost = LocnPattern.matcher(alarm.getAlasMessage());
            if (matcherAlamPost.matches()) {
                return matcherAlamPost.group(1) + matcherAlamPost.group(2);
            }

        }

        return null;

    }

    public String getTechnologies(Alarm alarm) {


        Pattern LocnPattern = Pattern.compile(".*([A-Z]{2})([A-Z0-9]{1}[GDUL]{1,3})(\\d{4}).*");

        if (alarm.getAlamEqupIndex() != null) {
            Matcher matcherEqupIndex = LocnPattern.matcher(alarm.getAlamEqupIndex());
            if (matcherEqupIndex.matches()) {
                return matcherEqupIndex.group(2);
            }
        }
        if (alarm.getAlamEventType() != null) {
            Matcher matcherEventType = LocnPattern.matcher(alarm.getAlamEventType());
            if (matcherEventType.matches()) {
                return (matcherEventType.group(2));

            }
        }
        if (alarm.getAlasMessage() != null) {
            Matcher matcherAlasMessage = LocnPattern.matcher(alarm.getAlasMessage());
            if (matcherAlasMessage.matches()) {
                return matcherAlasMessage.group(2);
            }
        }
        if (alarm.getAlamProbableCause() != null) {
            Matcher matcherAlamProbableCause = LocnPattern.matcher(alarm.getAlamProbableCause());
            if (matcherAlamProbableCause.matches()) {
                return matcherAlamProbableCause.group(2);
            }
        }

        return null;
    }

    public TicketParams createTicketParamsNI(Alarm alarm) {
        TicketParams ticketParams = new TicketParams();
        ticketParams.setSiteId(alarm.getAlamLocnTtname());
        ticketParams.setAttributeCategory(getSubCategory(alarm.getAlamMansName()));
        ticketParams.setEventClearDate(alarm.getAlasCleared());
        ticketParams.setEventStartDate(alarm.getAlamReported());
        ticketParams.setEventType(alarm.getCause());
        Set<String> set = new HashSet<String>();
        set.add(alarm.getAlamLocnTtname());
        ticketParams.setTechnologies(set);
        ticketParams.setNetworks(getNetwork(alarm));
        return ticketParams;

    }

    private List<ProblemLinks> linkedProblem(Alarm alarm, String token, String userId) {

        List<ProblemLinks> problemLinks = new ArrayList<>();
        List<ProblemDTO> myProblemDTOS = filterMYProblemDTO(alarm);
        if (myProblemDTOS.size() != 0) {

            for (ProblemDTO problemDTO : myProblemDTOS) {
                if (problemService.checkStatus(problemDTO.getPROM_NUMBER()) != null) {
                    log.info("***********************************************************************************");
                    log.info("START linked  problem My Redis");
                    log.info("Alarm occur" + alarm.getAlasOccurrenceId());
                    log.info("Number Problem " + problemDTO.getPROM_NUMBER());
                    createLink(alarm);
                    problemLinks = createFaultLinks(problemDTO.getPROM_NUMBER(), createLink(alarm), token, userId);
                    log.info("END linked  problem My Redis");
                    log.info("***********************************************************************************");
                    break;
                }
            }
            return problemLinks;
        } else {
            List<ProblemDTO> problemDTOS = filterProblemDTO(alarm);

            for (ProblemDTO problemDTO : problemDTOS) {
                if (problemService.checkStatus(problemDTO.getPROM_NUMBER()) != null) {
                    log.info("START linked  problem Query Redis");
                    log.info("Alarm occur" + alarm.getAlasOccurrenceId());
                    log.info("Number Problem " + problemDTO.getPROM_NUMBER());
                    createLink(alarm);
                    problemLinks = createFaultLinks(problemDTO.getPROM_NUMBER(), createLink(alarm), token, userId);
                    log.info("END linked  problem Query Redis");

                    break;

                }
            }
            return problemLinks;
        }

    }


    private List<ProblemDTO> filterProblemDTO(Alarm alarm) {

        List<ProblemDTO> problemDTOS = redisProblemService.getProblemDTO();

        List<ProblemDTO> filterProblem = new ArrayList<>();

        for (ProblemDTO problem : problemDTOS) {
            if (problem.getALAM_ALARMTYPE().equals(alarm.getAlamAlarmType())) {
                if (problem.getPRAT_VALUE().equals(getLocation(alarm)) || problem.getPRAT_VALUE().equals(getTechnologies(alarm))) {
                    filterProblem.add(problem);
                }
            }
        }
        return filterProblem;

    }

    private List<ProblemDTO> filterMYProblemDTO(Alarm alarm) {

        List<ProblemDTO> problemDTOS = redisProblemService.getMyProblemDTO();

        List<ProblemDTO> filterProblem = new ArrayList<>();
        if (problemDTOS != null) {
            for (ProblemDTO problem : problemDTOS) {
                if (problem.getALAM_ALARMTYPE().equals(alarm.getAlamAlarmType())) {
                    if (problem.getPRAT_VALUE().equals(getLocation(alarm)) || problem.getPRAT_VALUE().equals(getTechnologiesRegex(alarm))) {
                        filterProblem.add(problem);
                    }
                }
            }
        }
        return filterProblem;

    }

    public String getTechnologiesRegex(Alarm alarm) {


        Pattern LocnPattern = Pattern.compile(".*([A-Z]{2})([A-Z0-9]{1}[GDUL]{1,3})(\\d{4}).*");

        if (alarm.getAlamEqupIndex() != null) {
            Matcher matcherEqupIndex = LocnPattern.matcher(alarm.getAlamEqupIndex());
            if (matcherEqupIndex.matches()) {
                return matcherEqupIndex.group(1) + matcherEqupIndex.group(3);
            }
        }
        if (alarm.getAlamEventType() != null) {
            Matcher matcherEventType = LocnPattern.matcher(alarm.getAlamEventType());
            if (matcherEventType.matches()) {
                return (matcherEventType.group(1) + matcherEventType.group(3));

            }
        }
        if (alarm.getAlasMessage() != null) {
            Matcher matcherAlasMessage = LocnPattern.matcher(alarm.getAlasMessage());
            if (matcherAlasMessage.matches()) {
                return matcherAlasMessage.group(1) + matcherAlasMessage.group(3);
            }
        }
        if (alarm.getAlamProbableCause() != null) {
            Matcher matcherAlamProbableCause = LocnPattern.matcher(alarm.getAlamProbableCause());
            if (matcherAlamProbableCause.matches()) {
                return matcherAlamProbableCause.group(1) + matcherAlamProbableCause.group(3);
            }
        }

        return null;
    }

    private void initWorkGroup() {
        map=new HashMap<>();
        map.put("KJ", "ALB");
        map.put("AR", "ARD");
        map.put("AG", "AZW");
        map.put("BU", "BSH");
        map.put("CH", "CHR");
        map.put("FS", "FRS");
        map.put("QM", "QOM");
        map.put("GL", "GLN");
        map.put("GN", "GOL");
        map.put("HN", "HMD");
        map.put("HZ", "HRM");
        map.put("IL", "ILM");
        map.put("ES", "ESF");
        map.put("KS", "KRS");
        map.put("KM", "KRM");
        map.put("KH", "KHR");
        map.put("KZ", "KHZ");
        map.put("KB", "KBA");
        map.put("LN", "LOR");
        map.put("KD", "KRD");
        map.put("MK", "MRK");
        map.put("MA", "MZD");
        map.put("NK", "KHN");
        map.put("QN", "GZV");
        map.put("SK", "KHS");
        map.put("SM", "SEM");
        map.put("SB", "SYS");
        map.put("TH", "TEH");
        map.put("YZ", "YZD");
        map.put("ZN", "YZJ");
    }

    public String getGroupLocation(Alarm alarm) {

        Pattern LocnPattern = Pattern.compile(".*([A-Z]{2})(\\d{4}).*");
        if (alarm.getAlamLocnTtname() != null) {
            Matcher matcherLocnTtname = LocnPattern.matcher(alarm.getAlamLocnTtname());
            if (matcherLocnTtname.matches()) {
                return matcherLocnTtname.group(1);
//
            }
        }
        if (alarm.getAlamEqupIndex() != null) {
            Matcher matcherEqupIndex = LocnPattern.matcher(alarm.getAlamEqupIndex());
            if (matcherEqupIndex.matches()) {
                return matcherEqupIndex.group(1);
            }
        }
        if (alarm.getAlamEventType() != null) {
            Matcher matcherEventType = LocnPattern.matcher(alarm.getAlamEventType());
            if (matcherEventType.matches()) {
                return matcherEventType.group(1);
            }

        }
        if (alarm.getAlasMessage() != null) {
            Matcher matcherAlasMessage = LocnPattern.matcher(alarm.getAlasMessage());
            if (matcherAlasMessage.matches()) {
                return matcherAlasMessage.group(1);
            }
        }
        if (alarm.getAlamPort() != null) {
            Matcher matcherAlamPost = LocnPattern.matcher(alarm.getAlasMessage());
            if (matcherAlamPost.matches()) {
                return matcherAlamPost.group(1);
            }

        }

        return null;

    }


}