package com.bank.observer;

import com.bank.model.Transaction;

// Observer interface. Every service that wants to know about transactions implements it.
public interface TransactionListener {
    void onTransaction(Transaction transaction);
}
