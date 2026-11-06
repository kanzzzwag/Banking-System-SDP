package com.bank.observer;

import com.bank.model.Account;
import com.bank.model.Money;
import com.bank.model.Transaction;

// Fake SMS sender, it only prints the message to the console
public class SmsNotificationService implements TransactionListener {

    @Override
    public void onTransaction(Transaction t) {
        Account account = t.getAccount();
        String text;
        switch (t.getType()) {
            case DEPOSIT:
                text = "Account " + account.getId() + " credited " + Money.kzt(t.getAmount());
                break;
            case WITHDRAWAL:
                text = "Account " + account.getId() + " debited " + Money.kzt(t.getAmount());
                break;
            case TRANSFER:
                text = "Transfer " + Money.kzt(t.getAmount()) + " from " + account.getId() + " to " + t.getTarget();
                break;
            default:
                text = "External payment " + Money.kzt(t.getAmount()) + " to " + t.getTarget();
        }
        System.out.println("[Observer] SMS to " + account.getOwner().getPhone() + ": " + text
                + ". Balance: " + Money.kzt(account.getBalance()));
    }
}
