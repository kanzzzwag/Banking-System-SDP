package com.bank.adapter;

import com.bank.model.Money;

// Adapter: makes LocalCardProcessor look like a PaymentGateway
public class CardPaymentAdapter implements PaymentGateway {
    private final LocalCardProcessor cardProcessor;

    public CardPaymentAdapter(LocalCardProcessor cardProcessor) {
        this.cardProcessor = cardProcessor;
    }

    // receiver for card payments is a 16 digit card number
    @Override
    public boolean sendPayment(String fromAccountId, String receiver, double amountKzt) {
        long amountTiyn = Math.round(amountKzt * 100);
        System.out.println("[Adapter] Sending via local card processor: " + Money.kzt(amountKzt)
                + " converted to " + amountTiyn + " tiyn");
        int code = cardProcessor.pushToCard(receiver.replace(" ", ""), amountTiyn);
        if (code == LocalCardProcessor.OK) {
            System.out.println("[Adapter] Card processor returned code 0 (success)");
            return true;
        }
        String reason = code == LocalCardProcessor.INVALID_CARD ? "invalid card number" : "limit exceeded";
        System.out.println("[Adapter] Card processor returned code " + code + " (" + reason + ")");
        return false;
    }

    @Override
    public String getName() {
        return "CARD";
    }
}
