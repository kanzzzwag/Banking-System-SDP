package com.bank.model;

public class CheckingAccount extends Account {
    public CheckingAccount(String id, Customer owner) {
        super(id, owner);
    }

    @Override
    public String getType() {
        return "Checking";
    }
}
