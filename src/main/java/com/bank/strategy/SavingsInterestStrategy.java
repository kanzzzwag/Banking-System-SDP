package com.bank.strategy;

// Savings accounts get a good rate, 12% per year by default
public class SavingsInterestStrategy implements InterestStrategy {
    private final double annualRate;

    public SavingsInterestStrategy() {
        this(12.0);
    }

    public SavingsInterestStrategy(double annualRate) {
        this.annualRate = annualRate;
    }

    @Override
    public double calculateMonthlyInterest(double amount) {
        if (amount <= 0) {
            return 0;
        }
        return amount * annualRate / 100 / 12;
    }

    @Override
    public double getAnnualRate() {
        return annualRate;
    }

    @Override
    public String getName() {
        return "Savings " + annualRate + "%";
    }
}
