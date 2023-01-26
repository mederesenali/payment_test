package com.meder.security.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountBlockedException extends RuntimeException{
    public AccountBlockedException(){
        super("Account blocked exception");
    }
}
