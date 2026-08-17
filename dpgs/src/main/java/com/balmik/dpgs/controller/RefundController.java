package com.balmik.dpgs.controller;

import com.balmik.dpgs.dto.request.CreateRefundRequest;
import com.balmik.dpgs.dto.response.RefundResponse;
import com.balmik.dpgs.service.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;

    @PostMapping("/createRefund")
    public RefundResponse createRefund(@RequestBody @Valid CreateRefundRequest request, Authentication authentication){

        return refundService.createRefund(request, authentication.getName());
    }

    @GetMapping("/getRefund/{refundId}")
    public RefundResponse getRefund(@PathVariable String refundId, Authentication authentication){
        return refundService.getRefund(refundId, authentication.getName());
    }

    @GetMapping("/my-refunds")
    public List<RefundResponse> getMyRefunds(Authentication authentication){
        return refundService.getMyRefunds(authentication.getName());
    }
}
