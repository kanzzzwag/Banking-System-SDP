package com.bank.adapter;

import com.bank.model.Money;

import java.util.Locale;

// Adapter: makes InternationalSwiftService look like a PaymentGateway
public class SwiftPaymentAdapter implements PaymentGateway {
    private static final double KZT_PER_USD = 500.0;
    private final InternationalSwiftService swiftService;

    public SwiftPaymentAdapter(InternationalSwiftService swiftService) {
        this.swiftService = swiftService;
    }

    // receiver format for SWIFT is "BIC/IBAN"
    @Override
    public boolean sendPayment(String fromAccountId, String receiver, double amountKzt) {
        String[] parts = receiver.split("/");
        if (parts.length != 2) {
            System.out.println("[Adapter] SWIFT receiver must be in format BIC/IBAN");
            return false;
        }
        double amountUsd = amountKzt / KZT_PER_USD;
        System.out.printf(Locale.US, "[Adapter] Sending via SWIFT: %s converted to %.2f USD (rate %.0f)%n",
                Money.kzt(amountKzt), amountUsd, KZT_PER_USD);
        try {
            String reference = swiftService.executeSwiftTransfer(fromAccountId, parts[0], parts[1], amountUsd);
            System.out.println("[Adapter] SWIFT accepted the payment, reference " + reference);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("[Adapter] SWIFT rejected the payment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getName() {
        return "SWIFT";
    }
}
