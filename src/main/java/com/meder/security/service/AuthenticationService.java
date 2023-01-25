package com.meder.security.service;

import com.meder.security.config.JwtService;
import com.meder.security.model.Account;
import com.meder.security.payload.AuthenticationRequest;
import com.meder.security.payload.AuthenticationResponse;
import com.meder.security.payload.RegisterRequest;
import com.meder.security.model.Role;
import com.meder.security.model.User;
import com.meder.security.repository.AccountRepository;
import com.meder.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public AuthenticationResponse register(RegisterRequest request) {
        var account = Account.builder()
                .accountBalance(8.0)
                .build();
        Account savedAccount = accountRepository.save(account);
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .account(savedAccount)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
