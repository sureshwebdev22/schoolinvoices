package com.kvn.schoolinvoices.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PaymentRequest {

    private Long invoiceId;

    private BigDecimal amount;

    private LocalDate paymentDate;

    private String paymentMode;

    private String transactionReference;

    private String remarks;

}