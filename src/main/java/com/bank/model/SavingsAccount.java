package com.bank.model;

public class SavingsAccount extends Account {
    public SavingsAccount(String id, Customer owner) {
        super(id, owner);
    }

    @Override
    public String getType() {
        return "Savings";
    }
}
