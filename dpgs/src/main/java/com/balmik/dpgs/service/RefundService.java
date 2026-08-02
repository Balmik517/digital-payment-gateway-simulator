package com.balmik.dpgs.service;

import com.balmik.dpgs.dto.request.CreateRefundRequest;
import com.balmik.dpgs.dto.response.RefundResponse;

import java.util.List;

public interface RefundService {

    RefundResponse createRefund(CreateRefundRequest request, String email);

    RefundResponse getRefund(String refundId, String email);

    List<RefundResponse> getMyRefunds(String email);
}
