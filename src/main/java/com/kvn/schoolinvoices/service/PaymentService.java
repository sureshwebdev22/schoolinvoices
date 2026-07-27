package com.kvn.schoolinvoices.service;


import com.kvn.schoolinvoices.dto.PaymentRequest;
import com.kvn.schoolinvoices.entity.Invoice;
import com.kvn.schoolinvoices.entity.InvoiceStatus;
import com.kvn.schoolinvoices.entity.Payment;
import com.kvn.schoolinvoices.service.repository.InvoiceRepository;
import com.kvn.schoolinvoices.service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public Payment savePayment(PaymentRequest request) {

        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        BigDecimal balance = invoice.getBalanceAmount();

        if (request.getAmount().compareTo(balance) > 0) {
            throw new RuntimeException("Payment amount exceeds balance amount");
        }

        Payment payment = new Payment();

        payment.setInvoice(invoice);
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setPaymentMode(request.getPaymentMode());
        payment.setTransactionReference(request.getTransactionReference());
        payment.setRemarks(request.getRemarks());

        paymentRepository.save(payment);

        BigDecimal paid = invoice.getPaidAmount().add(request.getAmount());

        invoice.setPaidAmount(paid);

        invoice.setBalanceAmount(
                invoice.getTotalAmount().subtract(paid));

        if (invoice.getBalanceAmount().compareTo(BigDecimal.ZERO) == 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }

        invoiceRepository.save(invoice);

        return payment;
    }

    public List<Payment> getPaymentHistory(Long invoiceId) {
        return paymentRepository.findByInvoiceInvoiceIdOrderByPaymentDateDesc(invoiceId);

    }

}