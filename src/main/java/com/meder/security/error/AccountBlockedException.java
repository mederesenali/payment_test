package com.meder.security.error;

public class AccountBlockedException extends RuntimeException{
    public AccountBlockedException(){
        super("Account blocked exception");
    }
}
