package com.kvn.schoolinvoices.controller;

import com.kvn.schoolinvoices.dto.InvoiceDTO;
import com.kvn.schoolinvoices.entity.Invoice;
import com.kvn.schoolinvoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/schooladmin")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/invoices")
    public ResponseEntity<Invoice> createInvoice(
            @RequestBody InvoiceDTO dto){

        return ResponseEntity.ok(invoiceService.save(dto));

    }

    @GetMapping("/invoices/next-number")
    public ResponseEntity<Map<String,String>> getNextInvoiceNumber() {
        return ResponseEntity.ok(Map.of("nextInvoiceNumber",invoiceService.getNextInvoiceNumber()));
    }

}