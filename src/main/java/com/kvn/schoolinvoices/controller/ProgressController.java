package com.kvn.schoolinvoices.controller;

import com.kvn.schoolinvoices.dto.ImportProgress;
import com.kvn.schoolinvoices.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schooladmin")
public class ProgressController {

    @Autowired
    private  ProgressService progressService;

    @GetMapping("/progress/{jobId}")
    public ImportProgress progress(
            @PathVariable String jobId){

        return progressService.get(jobId);

    }

}