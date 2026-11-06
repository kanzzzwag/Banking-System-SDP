package com.bank.observer;

import com.bank.model.Transaction;

import java.util.ArrayList;
import java.util.List;

// Subject of the Observer pattern
public class TransactionPublisher {
    private final List<TransactionListener> listeners = new ArrayList<>();

    public void subscribe(TransactionListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(TransactionListener listener) {
        listeners.remove(listener);
    }

    public void publish(Transaction transaction) {
        for (TransactionListener listener : listeners) {
            listener.onTransaction(transaction);
        }
    }
}
