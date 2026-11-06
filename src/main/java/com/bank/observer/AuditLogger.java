package com.bank.observer;

import com.bank.model.Transaction;

import java.util.ArrayList;
import java.util.List;

// Keeps the full history of transactions in memory
public class AuditLogger implements TransactionListener {
    private final List<Transaction> history = new ArrayList<>();

    @Override
    public void onTransaction(Transaction t) {
        history.add(t);
        System.out.println("[Observer] Audit log: saved transaction #" + t.getId());
    }

    public List<Transaction> getHistory() {
        return history;
    }

    // Transactions where the account is the source or the target
    public List<Transaction> getHistory(String accountId) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : history) {
            if (t.getAccount().getId().equals(accountId) || accountId.equals(t.getTarget())) {
                result.add(t);
            }
        }
        return result;
    }
}
