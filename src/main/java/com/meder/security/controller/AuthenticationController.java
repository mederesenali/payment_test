package com.meder.security.controller;

import com.meder.security.config.JwtService;
import com.meder.security.error.AccountBlockedException;
import com.meder.security.payload.AuthenticationRequest;
import com.meder.security.payload.RegisterRequest;
import com.meder.security.service.AuthenticationService;
import com.meder.security.service.BlackListingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService service;
    private final JwtService jwtService;
    private final BlackListingService blackListingService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        try {
            return ResponseEntity.ok(service.register(request));
        } catch (Exception e) {
            log.info("/register", e);
            return new ResponseEntity<>("invalid credentials" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) throws Exception {
        try {
            return ResponseEntity.ok(service.authenticate(request));
        }catch (AccountBlockedException ae){
            log.info("/authenticate", ae);
            return new ResponseEntity<>(ae.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.info("/authenticate", e);
            return new ResponseEntity<>("inavalid username/password", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;
            jwt = authHeader.substring(7);
            blackListingService.blackListJwt(jwt);
            return  ResponseEntity.ok(null);
        } catch (Exception e) {
            log.info("/logut", e);
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
