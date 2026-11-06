package com.bank.strategy;

// Checking accounts are for daily payments, so the rate is very low
public class CheckingInterestStrategy implements InterestStrategy {
    private static final double ANNUAL_RATE = 0.5;

    @Override
    public double calculateMonthlyInterest(double amount) {
        if (amount <= 0) {
            return 0;
        }
        return amount * ANNUAL_RATE / 100 / 12;
    }

    @Override
    public double getAnnualRate() {
        return ANNUAL_RATE;
    }

    @Override
    public String getName() {
        return "Checking " + ANNUAL_RATE + "%";
    }
}
