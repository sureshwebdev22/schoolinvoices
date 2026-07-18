package com.kvn.schoolinvoices.service.repository;

import com.kvn.schoolinvoices.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByInvoiceInvoiceIdOrderByPaymentDateDesc(Long invoiceId);

}