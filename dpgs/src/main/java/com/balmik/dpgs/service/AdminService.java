package com.balmik.dpgs.service;

import com.balmik.dpgs.dto.response.PaymentResponse;

import java.util.List;

public interface AdminService {

    List<PaymentResponse> getAllPayments();
}
