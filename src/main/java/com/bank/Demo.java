package com.bank;

import com.bank.facade.BankFacade;
import com.bank.model.Account;
import com.bank.model.BankException;
import com.bank.model.Customer;

// Scenario that shows all six patterns. It uses only the facade, like the menu.
public class Demo {
    private final BankFacade bank;
    private final Runnable pause; // waits for Enter between the steps
    private boolean firstStep = true;

    public Demo(BankFacade bank, Runnable pause) {
        this.bank = bank;
        this.pause = pause;
    }

    public void run() {
        title("1. Facade + Factory Method: register customers and open accounts");
        Customer aruzhan = bank.registerCustomer("Aruzhan Sadykova", "+77011112233");
        Customer daniyar = bank.registerCustomer("Daniyar Bekov", "+77024445566");
        Account savings = bank.openAccount(aruzhan.getId(), "SAVINGS");
        Account checking = bank.openAccount(aruzhan.getId(), "CHECKING");
        Account credit = bank.openAccount(daniyar.getId(), "CREDIT");
        Account daniyarChecking = bank.openAccount(daniyar.getId(), "CHECKING");

        title("2. Observer: deposits and withdrawals notify SMS, audit and fraud services");
        bank.deposit(savings.getId(), 800_000);
        bank.deposit(checking.getId(), 250_000);
        bank.deposit(daniyarChecking.getId(), 1_500_000);
        bank.withdraw(checking.getId(), 40_000);
        tryStep(() -> bank.withdraw(savings.getId(), 5_000_000));

        title("3. Observer: internal transfer and fraud alert for too many operations");
        bank.transferInternal(checking.getId(), savings.getId(), 30_000);
        bank.withdraw(checking.getId(), 5_000);

        title("4. Adapter: external transfers through SWIFT and local card processor");
        bank.transferExternal(savings.getId(), "SWIFT", "DEUTDEFFXXX/DE89370400440532013000", 250_000);
        bank.transferExternal(daniyarChecking.getId(), "CARD", "4400 4301 2345 6789", 75_500.50);
        tryStep(() -> bank.transferExternal(daniyarChecking.getId(), "CARD", "1234", 10_000));

        title("5. Builder: loan agreements with optional collateral and guarantor");
        bank.applyForLoan(daniyar.getId(), credit.getId(), 2_000_000, 24, null, null);
        bank.applyForLoan(aruzhan.getId(), savings.getId(), 8_000_000, 60, "Apartment in Astana", "Sadykov Nurlan");
        tryStep(() -> bank.applyForLoan(daniyar.getId(), credit.getId(), 9_000_000, 36, null, null));

        title("6. Strategy: monthly interest, then change a strategy at runtime");
        bank.withdraw(credit.getId(), 2_300_000); // Daniyar now owes money on the credit account
        bank.applyMonthlyInterest();
        bank.changeInterestStrategy(checking.getId(), "SAVINGS", 9.0);
        bank.applyMonthlyInterest();

        title("7. Result");
        bank.showAccounts();
        System.out.println();
        bank.showTransactionHistory("");
        System.out.println();
        System.out.println("=== Demo finished ===");
    }

    // runs a step that is expected to fail and prints the error
    private void tryStep(Runnable step) {
        try {
            step.run();
        } catch (BankException e) {
            System.out.println("Error (expected): " + e.getMessage());
        }
    }

    private void title(String text) {
        if (!firstStep) {
            pause.run();
        }
        firstStep = false;
        System.out.println();
        System.out.println("=== " + text + " ===");
    }
}
