package com.meder.security.service;

import com.meder.security.config.JwtService;
import com.meder.security.model.Account;
import com.meder.security.model.AccountAudit;
import com.meder.security.model.User;
import com.meder.security.repository.AccountAuditRepository;
import com.meder.security.repository.AccountRepository;
import com.meder.security.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AccountAuditRepository accountAuditRepository;


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<?> doPayment(HttpServletRequest httpServletRequest) {
        try{
            String email = extractUserName(httpServletRequest);
            User byEmail = userRepository.findByEmail(email).get();
            Account account = byEmail.getAccount();
            if (account.getAccountBalance() > 1.1){
                account.setAccountBalance(account.getAccountBalance() - 1.1);
            }else{
                return new ResponseEntity<>("Not enough balance", HttpStatus.BAD_REQUEST);
            }
            accountRepository.save(account);
            AccountAudit audit = AccountAudit
                    .builder()
                    .account(account)
                    .paymentDate(LocalDateTime.now())
                    .currentBalance(account.getAccountBalance())
                    .build();
            accountAuditRepository.save(audit);
            return new ResponseEntity<>("ok", HttpStatus.OK);
        }catch (Exception e){
            log.info("/payment",e);
            return new ResponseEntity<>("something went wrong"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractUserName(HttpServletRequest httpServletRequest) {
        final String authHeader = httpServletRequest.getHeader("Authorization");
        return jwtService.extractUsername(authHeader.substring(7));
    }

}
