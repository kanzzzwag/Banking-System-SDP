package com.bank;

import com.bank.facade.BankFacade;
import com.bank.model.BankException;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BankFacade bank = new BankFacade();

    public static void main(String[] args) {
        System.out.println("=== Banking System (Software Design Patterns project) ===");
        try {
            while (true) {
                printMenu();
                int choice = readInt("Choose an option: ");
                if (choice == 0) {
                    System.out.println("Goodbye!");
                    return;
                }
                try {
                    handle(choice);
                } catch (BankException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            // input was closed (for example Ctrl+D)
            System.out.println("Input closed. Goodbye!");
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("----------- MENU -----------");
        System.out.println(" 1. Register customer");
        System.out.println(" 2. Open account");
        System.out.println(" 3. Deposit");
        System.out.println(" 4. Withdraw");
        System.out.println(" 5. Internal transfer");
        System.out.println(" 6. External transfer");
        System.out.println(" 7. Apply for loan");
        System.out.println(" 8. Apply monthly interest");
        System.out.println(" 9. Show accounts");
        System.out.println("10. Show transaction history");
        System.out.println("11. Change interest strategy");
        System.out.println("12. Run demo");
        System.out.println(" 0. Exit");
    }

    private static void handle(int choice) {
        switch (choice) {
            case 1:
                bank.registerCustomer(readLine("Full name: "), readLine("Phone (+77011234567): "));
                break;
            case 2:
                bank.showCustomers();
                String customerId = readLine("Customer id (C1, C2, ...): ");
                String type = readLine("Account type (SAVINGS / CHECKING / CREDIT): ");
                bank.openAccount(customerId, type);
                break;
            case 3:
                bank.deposit(readLine("Account id: "), readPositive("Amount KZT: "));
                break;
            case 4:
                bank.withdraw(readLine("Account id: "), readPositive("Amount KZT: "));
                break;
            case 5:
                bank.transferInternal(readLine("From account id: "), readLine("To account id: "),
                        readPositive("Amount KZT: "));
                break;
            case 6:
                String from = readLine("From account id: ");
                String system = readLine("Payment system (SWIFT / CARD): ");
                String receiver = readLine("Receiver (SWIFT: BIC/IBAN, CARD: 16 digits): ");
                bank.transferExternal(from, system, receiver, readPositive("Amount KZT: "));
                break;
            case 7:
                String loanCustomer = readLine("Customer id: ");
                String loanAccount = readLine("Account id for the money: ");
                double amount = readPositive("Loan amount KZT: ");
                int term = readInt("Term in months: ");
                String collateral = readLine("Collateral (empty if none): ");
                String guarantor = readLine("Guarantor (empty if none): ");
                bank.applyForLoan(loanCustomer, loanAccount, amount, term, collateral, guarantor);
                break;
            case 8:
                bank.applyMonthlyInterest();
                break;
            case 9:
                bank.showAccounts();
                break;
            case 10:
                bank.showTransactionHistory(readLine("Account id (empty for all): "));
                break;
            case 11:
                String accountId = readLine("Account id: ");
                String strategy = readLine("New strategy (SAVINGS / CHECKING / LOAN): ");
                double rate = strategy.equalsIgnoreCase("CHECKING") ? 0 : readPositive("Annual rate %: ");
                bank.changeInterestStrategy(accountId, strategy, rate);
                break;
            case 12:
                new Demo(bank, () -> readLine("\nPress Enter to continue...")).run();
                break;
            default:
                System.out.println("Unknown option, try again.");
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            String text = readLine(prompt);
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readPositive(String prompt) {
        while (true) {
            String text = readLine(prompt).replace(",", "").replace(" ", "");
            try {
                double value = Double.parseDouble(text);
                if (value > 0) {
                    return value;
                }
                System.out.println("The number must be greater than zero.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number, for example 15000 or 2500.50");
            }
        }
    }
}
