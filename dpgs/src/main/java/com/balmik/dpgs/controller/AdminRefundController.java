package com.balmik.dpgs.controller;

import com.balmik.dpgs.dto.response.RefundResponse;
import com.balmik.dpgs.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/refunds")
public class AdminRefundController {

    private final RefundService refundService;

    @PostMapping("/{refundId}/approve")
    public RefundResponse approveRefund(@PathVariable String refundId){
        return refundService.approveRefund(refundId);
    }

    @PostMapping("/{refundId}/reject")
    public RefundResponse rejectRefund(@PathVariable String refundId){
        return refundService.rejectRefund(refundId);
    }
}
