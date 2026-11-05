package com.bank.model;

// Thrown for business errors like insufficient funds or unknown account
public class BankException extends RuntimeException {
    public BankException(String message) {
        super(message);
    }
}
