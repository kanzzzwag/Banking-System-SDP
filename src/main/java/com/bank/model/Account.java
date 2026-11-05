package com.bank.model;

import com.bank.strategy.InterestStrategy;

// Base class for all account types. Interest is calculated by a strategy.
public abstract class Account {
    private final String id;
    private final Customer owner;
    protected double balance;
    private InterestStrategy interestStrategy;

    protected Account(String id, Customer owner) {
        this.id = id;
        this.owner = owner;
    }

    public abstract String getType();

    // Each account type decides if it has enough money
    protected boolean canWithdraw(double amount) {
        return balance >= amount;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new BankException("Amount must be positive");
        }
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new BankException("Amount must be positive");
        }
        if (!canWithdraw(amount)) {
            throw new BankException("Insufficient funds on account " + id + ". Balance: " + Money.kzt(balance));
        }
        balance -= amount;
    }

    // Returns the interest that was added (or charged) this month
    public double applyMonthlyInterest() {
        double interest = interestStrategy.calculateMonthlyInterest(balance);
        balance += interest;
        return interest;
    }

    public void setInterestStrategy(InterestStrategy interestStrategy) {
        this.interestStrategy = interestStrategy;
    }

    public InterestStrategy getInterestStrategy() {
        return interestStrategy;
    }

    public String getId() {
        return id;
    }

    public Customer getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return String.format("%-8s %-9s %-18s %20s  [%s]", id, getType(), owner.getFullName(),
                Money.kzt(balance), interestStrategy.getName());
    }
}
