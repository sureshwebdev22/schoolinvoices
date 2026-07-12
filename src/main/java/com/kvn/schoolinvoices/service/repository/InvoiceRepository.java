package com.kvn.schoolinvoices.service.repository;

import com.kvn.schoolinvoices.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    @Query("SELECT COALESCE(MAX(i.invoiceId), 0) + 1 FROM Invoice i")
    Long getNextId();
}