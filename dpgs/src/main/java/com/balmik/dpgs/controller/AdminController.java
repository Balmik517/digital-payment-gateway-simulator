package com.balmik.dpgs.controller;

import com.balmik.dpgs.dto.response.PaymentResponse;
import com.balmik.dpgs.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/payments")
    public List<PaymentResponse> getAllPayments() {
        return adminService.getAllPayments();
    }
}
