package com.acc.ACC_AYCE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;

import com.acc.ACC_AYCE.Entity.Billing;
import com.acc.ACC_AYCE.dto.BillingResponse;
import com.acc.ACC_AYCE.repository.BillingRepository;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private BillingRepository billingRepository;

    
    @GetMapping("")
    public List<BillingResponse> getAllBillings() {
        return billingRepository.findAll().stream()
                .map(this::convertToBillingResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("")
    public BillingResponse createBilling(@RequestBody @NonNull Billing billing) {
        Billing savedBilling = billingRepository.save(billing);
        return convertToBillingResponse(savedBilling);
    }

    @GetMapping("/{id}")
    public BillingResponse getBillingById(@PathVariable @NonNull Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Billing not found with id: " + id));
        return convertToBillingResponse(billing);
    }

    private BillingResponse convertToBillingResponse(Billing billing) {
        if (billing == null) {
            throw new IllegalArgumentException("Billing cannot be null");
        }
        BillingResponse response = new BillingResponse();
        response.setBillId(billing.getBillId());
        response.setAmount(billing.getAmount());
        response.setStatus(billing.getStatus());
        response.setDueDate(billing.getDueDate());
        response.setDescription(billing.getDescription());
        
        // Handle student information properly
        if (billing.getStudent() != null) {
            BillingResponse.StudentInfo studentInfo = new BillingResponse.StudentInfo();
            studentInfo.setStudentId(billing.getStudent().getStudentId());
            studentInfo.setName(billing.getStudent().getName());
            studentInfo.setEmail(billing.getStudent().getEmail());
            studentInfo.setStudentCode(billing.getStudent().getStudentCode());
            response.setStudent(studentInfo);
        }
        
        return response;
    }
}
