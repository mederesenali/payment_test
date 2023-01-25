package com.meder.security.controller;

import com.meder.security.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("")
    public ResponseEntity<?> test(HttpServletRequest httpServletRequest){
       return paymentService.doPayment(httpServletRequest);
    }
}
