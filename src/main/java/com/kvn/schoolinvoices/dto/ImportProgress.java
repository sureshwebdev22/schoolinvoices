package com.kvn.schoolinvoices.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportProgress {

    private int totalRecords;

    private int processedRecords;

    private int percentage;

    private String status;

    // getters setters
}