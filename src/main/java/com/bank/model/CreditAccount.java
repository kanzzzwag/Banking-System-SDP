package com.bank.model;

// Credit account can go below zero up to the credit limit
public class CreditAccount extends Account {
    private final double creditLimit;

    public CreditAccount(String id, Customer owner, double creditLimit) {
        super(id, owner);
        this.creditLimit = creditLimit;
    }

    @Override
    protected boolean canWithdraw(double amount) {
        return balance - amount >= -creditLimit;
    }

    // Interest is charged only when the client owes money
    @Override
    public double applyMonthlyInterest() {
        if (balance >= 0) {
            return 0;
        }
        double interest = getInterestStrategy().calculateMonthlyInterest(-balance);
        balance -= interest;
        return -interest;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    @Override
    public String getType() {
        return "Credit";
    }
}
