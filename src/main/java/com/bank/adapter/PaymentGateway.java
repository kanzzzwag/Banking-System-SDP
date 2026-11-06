package com.bank.adapter;

// Target interface. Our bank sends all external payments through it.
public interface PaymentGateway {
    // returns true if the payment was accepted
    boolean sendPayment(String fromAccountId, String receiver, double amountKzt);

    String getName();
}
