package com.kvn.schoolinvoices.controller;


import com.kvn.schoolinvoices.dto.PaymentRequest;
import com.kvn.schoolinvoices.entity.Payment;
import com.kvn.schoolinvoices.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public Payment savePayment(@RequestBody PaymentRequest request) {

        return paymentService.savePayment(request);

    }

    @GetMapping("/invoice/{invoiceId}")
    public List<Payment> getPaymentHistory(@PathVariable Long invoiceId) {

        return paymentService.getPaymentHistory(invoiceId);

    }

}