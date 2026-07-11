package com.kvn.schoolinvoices.controller;

import com.kvn.schoolinvoices.dto.InvoiceDTO;
import com.kvn.schoolinvoices.entity.Invoice;
import com.kvn.schoolinvoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}