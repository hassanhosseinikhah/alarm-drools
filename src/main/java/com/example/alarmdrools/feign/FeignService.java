package com.example.alarmdrools.feign;

import com.example.alarmdrools.model.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@FeignClient(value = "fault-manager-service")
public interface FeignService {
    @PostMapping("/fault/create-without-attachments2")
     CreateFaultDTO createFaultWOA(@RequestHeader(value = "Authorization") String token,
                                   @RequestBody @NotNull CreateFaultDTO createFaultDTO) throws IOException;

    @PostMapping("/job-ticket/fault/{parentId}")
    public JobTicketDetailDTO createFaultJobTicket(
            @PathVariable("parentId") String parentId,
            @RequestBody @Valid FaultJobTicketDTO faultJobTicketDTO,
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "userId", required = false) String userId);


    @PutMapping("/link/{id}")
    public List<ProblemLinks> createFaultLinks(@PathVariable("id") Long problemNumber,
                                                                  @RequestBody List<LinksDTO> links,
                                                                  @RequestHeader(value = "Authorization") String token,
                                                                  @RequestParam(value = "userId", required = false) String userId);
    @PutMapping("attributes/edit/{id}")
    public List<ProblemAttributeDTO> updateAttributes(
            @PathVariable("id") long promNumber,
            @RequestBody @Valid List<ProblemAttributeDTO> attributes,
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "userId", required = false) String userId);

    @GetMapping("attributes/fault/{id}")
    public List<ProblemAttributeDTO> getProblemAttributes(@PathVariable("id") long promNumber);


    @PutMapping("attributes/{id}")
    public Boolean addAttributes(
            @PathVariable("id") Long promNumber,
            @RequestBody @Valid List<ProblemAttributeDTO> attributes,
            @RequestHeader(value = "Authorization") String token);

    @GetMapping("attributes/type/{type}")
    public List<ProblemAttributeTemplateItemDTO> getAttributesByFaultType(@PathVariable("type") String type);


    @PostMapping(value = "/fault",consumes = {"multipart/form-data"})
    public CreateFaultDTO createFault(@RequestHeader(value = "Authorization") String token,
                                                         @RequestPart(value = "problem") @NotNull String problem,
                                                         @RequestPart(value = "userId", required = false) String userId,
                                                         @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException;





    @GetMapping("/fault/test")
     String testController();
}
