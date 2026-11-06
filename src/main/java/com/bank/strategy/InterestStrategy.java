package com.bank.strategy;

// Strategy interface. Every way of calculating interest implements it.
public interface InterestStrategy {
    double calculateMonthlyInterest(double amount);

    double getAnnualRate();

    String getName();
}
