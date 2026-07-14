package com.kvn.schoolinvoices.service;

import com.kvn.schoolinvoices.dto.InvoiceDTO;
import com.kvn.schoolinvoices.dto.InvoiceItemDTO;
import com.kvn.schoolinvoices.dto.StudentDTO;
import com.kvn.schoolinvoices.entity.Invoice;
import com.kvn.schoolinvoices.entity.InvoiceItem;
import com.kvn.schoolinvoices.entity.InvoiceStatus;
import com.kvn.schoolinvoices.entity.Student;
import com.kvn.schoolinvoices.service.repository.InvoiceRepository;
import com.kvn.schoolinvoices.service.repository.StudentRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Invoice save(InvoiceDTO dto) {

        Student student =
                studentRepository.findById(dto.getStudentId())
                        .orElseThrow();

        Invoice invoice = new Invoice();

        invoice.setStudent(student);
        invoice.setInvoiceDate(dto.getInvoiceDate());
        invoice.setDueDate(dto.getDueDate());
        invoice.setStatus(InvoiceStatus.UNPAID);

        List<InvoiceItem> items = new ArrayList<>();

        BigDecimal grandTotal = BigDecimal.ZERO;

        for (InvoiceItemDTO itemDto : dto.getInvoiceItems()) {

            InvoiceItem item = new InvoiceItem();

            item.setInvoice(invoice);
            item.setFeeType(itemDto.getFeeType());

            item.setAmount(itemDto.getAmount());
            item.setDiscount(itemDto.getDiscount());

            BigDecimal total =
                    itemDto.getAmount().subtract(itemDto.getDiscount());

            item.setTotal(total);

            grandTotal = grandTotal.add(total);

            items.add(item);

        }

        invoice.setInvoiceItems(items);

        invoice.setTotalAmount(grandTotal);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        savedInvoice.setInvoiceNumber(
                "INV-" + LocalDate.now().getYear() + "-" +
                        String.format("%06d", savedInvoice.getInvoiceId())
        );

        return invoiceRepository.save(savedInvoice);

    }

    public String getNextInvoiceNumber() {

        Long nextId = invoiceRepository.getNextId();

        return String.format("INV-%d-%06d",
                Year.now().getValue(),
                nextId);
    }


    public Page<InvoiceDTO> searchInvoices(String search, Pageable pageable) {
        return invoiceRepository.searchInvoices(search, pageable).map(this::convertToDto);
    }

    private InvoiceDTO convertToDto(Invoice invoice) {

        InvoiceDTO build = InvoiceDTO.builder().invoiceID(invoice.getInvoiceId())
                .invoiceNumber(invoice.getInvoiceNumber())

                .invoiceDate(invoice.getInvoiceDate())
                .dueDate(invoice.getDueDate())
                .studentId(invoice.getStudent().getStudentId())
                .invoiceItems(convertInvoiceItemsToDto(invoice.getInvoiceItems()))
                .build();
        return build;

    }

    private List<InvoiceItemDTO> convertInvoiceItemsToDto(List<InvoiceItem> invoiceItems) {
       return invoiceItems.stream().map(this::convertInvoiceItemsToDto1).toList();
    }

    private InvoiceItemDTO convertInvoiceItemsToDto1(InvoiceItem invoiceItem) {
        return InvoiceItemDTO.builder()
                .feeType(invoiceItem.getFeeType())
                .amount(invoiceItem.getAmount())
                .discount(invoiceItem.getDiscount())
                .build();
    }

}