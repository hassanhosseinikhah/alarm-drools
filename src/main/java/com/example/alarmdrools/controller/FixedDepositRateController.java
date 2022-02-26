package com.example.alarmdrools.controller;

import com.clarity.alarmaction.action.ActionResult;
import com.example.alarmdrools.feign.FeignService;
import com.example.alarmdrools.model.dto.*;
import com.example.alarmdrools.model.entity.AlarmComments;
import com.example.alarmdrools.service.AlarmService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class FixedDepositRateController {

    private final FeignService feignService;
    private final AlarmService alarmService;

    public FixedDepositRateController(FeignService feignService, AlarmService alarmService) {
        this.feignService = feignService;
        this.alarmService = alarmService;
    }

    @GetMapping("/alarms")
    public List<AlarmComments> getActiveAlarms(){
        return alarmService.getAlarms();
    }

    @PostMapping("/related-alarms")
    public List<CreateFaultDTO> getRelatedAlarms(@RequestHeader(value = "Authorization") String token,
                                                 @RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "severity", required = false) String severity,
                                                 @RequestParam(value = "comment", required = false) String comment) throws IOException {
        return alarmService.getRelatedAlarms(token,userId,severity,comment);
    }


    @PostMapping("/create")
    public CreateFaultDTO create(@RequestHeader(value = "Authorization") String token,
                                 @RequestBody @NotNull CreateFaultDTO createFaultDTO)throws IOException {
        System.out.println("here");
        return alarmService.createFaultWOA(token,createFaultDTO);
    }
//    @GetMapping("/create2")
//    public void create (){
//        System.out.println("111111111111");
//        alarmService.create();
//
//    }
    @PostMapping("/create/workorder")
    public CreatWorkOrderResponseDTO insert(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody WorkOrderDetailDTO workOrderDTO,
            @RequestParam(defaultValue = "false") boolean forceCreate,
            @RequestParam(value = "userId", required = false) String userId){
        return alarmService.insertWorkOrder(token,workOrderDTO,forceCreate,userId);

    }
//    @PostMapping("/create/jobTicket/{parentId}")
//    public ActivityJobTicketDTO createJobTicket(@PathVariable("parentId") String parentId,
//                                                @RequestBody @Valid ActivityJobTicketDTO activityJobTicketDTO,
//                                                @RequestHeader(value = "Authorization") String token){
//        return alarmService.createJobTicket(parentId,activityJobTicketDTO,token);
//    }
    @PostMapping("/createjobticket/{parentId}")
    public JobTicketDetailDTO createFaultJobTicket(
            @PathVariable("parentId") String parentId,
            @RequestBody @Valid FaultJobTicketDTO faultJobTicketDTO,
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "userId", required = false) String userId){
        return  alarmService.createFaultJobTicket(parentId,faultJobTicketDTO,token,userId);


    }


    @GetMapping("test")
    public String testFaultFeign(@RequestHeader(value = "Authorization") String token){
        return feignService.testController();
    }

    @PostMapping("/acknowleged/{type}")
    ActionResult doAlarmAction(@RequestHeader(value = "Authorization") String token,
                               @PathVariable("type") String type,
                               @RequestBody String occurrenceIds,
                               @RequestParam(value = "severity", required = false) String severity,
                               @RequestParam(value = "comment", required = false) String comment) {
        return alarmService.doAlarmAction(token,type, Stream.of(occurrenceIds).collect(Collectors.toList()), severity,comment);
    }
    @PutMapping("/createlink/{id}")
    public void createFaultLinks(@PathVariable("id") Long problemNumber,
                                 @RequestBody List<LinksDTO> links,
                                 @RequestHeader(value = "Authorization") String token,
                                 @RequestParam(value = "userId", required = false) String userId){

        alarmService.createFaultLinks(problemNumber,links,token,userId);
    }
    @PutMapping("/updateAttribute/{id}")
    public List<ProblemAttributeDTO> updateAttributes(
            @PathVariable("id") long promNumber,
            @RequestBody @Valid List<ProblemAttributeDTO> attributes,
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "userId", required = false) String userId){
        return alarmService.updateAttributes(promNumber,attributes,token,userId);
    }
    @GetMapping("/getProblemAttributes/{id}")
    public List<ProblemAttributeDTO> getProblemAttributes(@PathVariable("id") long promNumber) {
        return alarmService.getProblemAttributes(promNumber);
    }
    @PutMapping("addAttributes/{id}")
    public Boolean addAttributes(
            @PathVariable("id") Long promNumber,
            @RequestBody @Valid List<ProblemAttributeDTO> attributes,
            @RequestHeader(value = "Authorization") String token){
        return alarmService.addAttributes(promNumber,attributes,token);
    }
    @GetMapping("/type/{type}")
    public List<ProblemAttributeTemplateItemDTO> getAttributesByFaultType(@PathVariable("type") String type){
        return alarmService.getAttributesByFaultType(type);
    }
    @PostMapping("/createfault")
    public CreateFaultDTO createFault(@RequestHeader(value = "Authorization") String token,
                                      @RequestPart(value = "problem") @NotNull String problem,
                                      @RequestPart(value = "userId", required = false) String userId,
                                      @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException{
        return alarmService.createFault(token,problem,userId,files);
    }
    @PostMapping("/createfaultwoa")
    CreateFaultDTO createFaultWOA(@RequestHeader(value = "Authorization") String token,
                                  @RequestBody @NotNull CreateFaultDTO createFaultDTO) throws IOException{
        return feignService.createFaultWOA(token,createFaultDTO);
    }




    /*
    @GetMapping("/getFDInterestRate")
    public FDRequest getQuestions(@RequestParam("bankName") String bankName, @RequestParam("durationInYear") Integer durationInYear){


        //KieSession kieSession = kieContainer.newKieSession();
        FDRequest fdRequest = new FDRequest(bankName, durationInYear);


        kieSession.insert(fdRequest);
        kieSession.fireAllRules();
        //kieSession.dispose();


        return  fdRequest;
    }*/
//    @RequestMapping(value = "/create" ,method = RequestMethod.POST)
//    public CompletableFuture<CreateFaultDTO> createFaultWOA(){
//
//        return alarmService.createFaultWOA();
//    }
}
