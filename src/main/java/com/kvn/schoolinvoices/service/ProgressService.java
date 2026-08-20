package com.kvn.schoolinvoices.service;

import com.kvn.schoolinvoices.dto.ImportProgress;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProgressService {

    private final Map<String, ImportProgress> map =
            new ConcurrentHashMap<>();

    public void create(String jobId,int total){

        ImportProgress p=new ImportProgress();

        p.setTotalRecords(total);

        p.setProcessedRecords(0);

        p.setPercentage(0);

        p.setStatus("PROCESSING");

        map.put(jobId,p);

    }

    public void update(String jobId,int processed){

        ImportProgress p=map.get(jobId);

        p.setProcessedRecords(processed);

        p.setPercentage(
                (processed*100)/p.getTotalRecords()
        );

    }

    public void complete(String jobId){

        ImportProgress p=map.get(jobId);

        p.setPercentage(100);

        p.setStatus("COMPLETED");

    }

    public ImportProgress get(String jobId){

        return map.get(jobId);

    }

}