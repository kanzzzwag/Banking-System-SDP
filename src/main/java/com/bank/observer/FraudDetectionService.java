package com.bank.observer;

import com.bank.model.Money;
import com.bank.model.Transaction;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Checks every transaction for suspicious activity
public class FraudDetectionService implements TransactionListener {
    private final double amountLimit;
    private final int maxTransactions;
    private final int periodSeconds;
    // account id -> times of its recent transactions
    private final Map<String, List<LocalDateTime>> recentTimes = new HashMap<>();

    public FraudDetectionService(double amountLimit, int maxTransactions, int periodSeconds) {
        this.amountLimit = amountLimit;
        this.maxTransactions = maxTransactions;
        this.periodSeconds = periodSeconds;
    }

    @Override
    public void onTransaction(Transaction t) {
        String accountId = t.getAccount().getId();

        if (t.getAmount() > amountLimit) {
            System.out.println("[Observer] Fraud alert: " + t.getType() + " of " + Money.kzt(t.getAmount())
                    + " on account " + accountId + " is above the limit " + Money.kzt(amountLimit));
        }

        List<LocalDateTime> times = recentTimes.computeIfAbsent(accountId, k -> new ArrayList<>());
        times.add(t.getTime());
        // forget transactions that are older than the period
        times.removeIf(time -> Duration.between(time, t.getTime()).getSeconds() > periodSeconds);
        if (times.size() > maxTransactions) {
            System.out.println("[Observer] Fraud alert: " + times.size() + " transactions on account "
                    + accountId + " in less than " + periodSeconds + " seconds");
        }
    }
}
