package com.bank.facade;

import com.bank.adapter.CardPaymentAdapter;
import com.bank.adapter.InternationalSwiftService;
import com.bank.adapter.LocalCardProcessor;
import com.bank.adapter.PaymentGateway;
import com.bank.adapter.SwiftPaymentAdapter;
import com.bank.builder.LoanAgreement;
import com.bank.factory.AccountCreator;
import com.bank.factory.CheckingAccountCreator;
import com.bank.factory.CreditAccountCreator;
import com.bank.factory.SavingsAccountCreator;
import com.bank.model.Account;
import com.bank.model.BankException;
import com.bank.model.Customer;
import com.bank.model.Money;
import com.bank.model.Transaction;
import com.bank.model.TransactionType;
import com.bank.observer.AuditLogger;
import com.bank.observer.FraudDetectionService;
import com.bank.observer.SmsNotificationService;
import com.bank.observer.TransactionPublisher;
import com.bank.strategy.CheckingInterestStrategy;
import com.bank.strategy.InterestStrategy;
import com.bank.strategy.LoanInterestStrategy;
import com.bank.strategy.SavingsInterestStrategy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Facade: the only class the console menu works with.
// It hides the factories, builder, strategies, observers and adapters.
public class BankFacade {
    private final Map<String, Customer> customers = new LinkedHashMap<>();
    private final Map<String, Account> accounts = new LinkedHashMap<>();
    private final List<LoanAgreement> loans = new ArrayList<>();

    private final Map<String, AccountCreator> creators = new LinkedHashMap<>();
    private final Map<String, PaymentGateway> gateways = new LinkedHashMap<>();
    private final TransactionPublisher publisher = new TransactionPublisher();
    private final AuditLogger auditLogger = new AuditLogger();

    private int nextCustomerNumber = 1;

    public BankFacade() {
        creators.put("SAVINGS", new SavingsAccountCreator());
        creators.put("CHECKING", new CheckingAccountCreator());
        creators.put("CREDIT", new CreditAccountCreator());

        gateways.put("SWIFT", new SwiftPaymentAdapter(new InternationalSwiftService()));
        gateways.put("CARD", new CardPaymentAdapter(new LocalCardProcessor()));

        // alert above 1 000 000 KZT or more than 3 operations in 60 seconds
        publisher.subscribe(new FraudDetectionService(1_000_000, 3, 60));
        publisher.subscribe(new SmsNotificationService());
        publisher.subscribe(auditLogger);
    }

    public Customer registerCustomer(String fullName, String phone) {
        if (fullName == null || fullName.isBlank()) {
            throw new BankException("Name cannot be empty");
        }
        if (phone == null || !phone.matches("\\+?\\d{10,12}")) {
            throw new BankException("Phone must have 10 to 12 digits, for example +77011234567");
        }
        Customer customer = new Customer("C" + nextCustomerNumber++, fullName.trim(), phone);
        customers.put(customer.getId(), customer);
        System.out.println("[Facade] Customer registered: " + customer);
        return customer;
    }

    public Account openAccount(String customerId, String type) {
        Customer customer = findCustomer(customerId);
        AccountCreator creator = creators.get(type.toUpperCase());
        if (creator == null) {
            throw new BankException("Unknown account type: " + type);
        }
        Account account = creator.openAccount(customer);
        accounts.put(account.getId(), account);
        return account;
    }

    public void deposit(String accountId, double amount) {
        Account account = findAccount(accountId);
        account.deposit(amount);
        publisher.publish(new Transaction(TransactionType.DEPOSIT, account, null, amount));
    }

    public void withdraw(String accountId, double amount) {
        Account account = findAccount(accountId);
        account.withdraw(amount);
        publisher.publish(new Transaction(TransactionType.WITHDRAWAL, account, null, amount));
    }

    public void transferInternal(String fromId, String toId, double amount) {
        Account from = findAccount(fromId);
        Account to = findAccount(toId);
        if (from == to) {
            throw new BankException("Cannot transfer to the same account");
        }
        from.withdraw(amount);
        to.deposit(amount);
        publisher.publish(new Transaction(TransactionType.TRANSFER, from, to.getId(), amount));
    }

    // gatewayName is SWIFT or CARD
    public void transferExternal(String fromId, String gatewayName, String receiver, double amount) {
        Account from = findAccount(fromId);
        PaymentGateway gateway = gateways.get(gatewayName.toUpperCase());
        if (gateway == null) {
            throw new BankException("Unknown payment system: " + gatewayName);
        }
        from.withdraw(amount);
        boolean ok = gateway.sendPayment(from.getId(), receiver, amount);
        if (!ok) {
            from.deposit(amount); // give the money back
            throw new BankException("External payment failed, money returned to " + from.getId());
        }
        publisher.publish(new Transaction(TransactionType.EXTERNAL_TRANSFER, from,
                gateway.getName() + " " + receiver, amount));
    }

    public LoanAgreement applyForLoan(String customerId, String accountId, double amount, int termMonths,
                                      String collateral, String guarantor) {
        Customer customer = findCustomer(customerId);
        Account account = findAccount(accountId);
        if (account.getOwner() != customer) {
            throw new BankException("Account " + accountId + " does not belong to " + customer.getFullName());
        }
        LoanAgreement.Builder builder = new LoanAgreement.Builder()
                .customer(customer)
                .amount(amount)
                .termMonths(termMonths);
        if (collateral != null && !collateral.isBlank()) {
            builder.collateral(collateral);
        }
        if (guarantor != null && !guarantor.isBlank()) {
            builder.guarantor(guarantor);
        }
        LoanAgreement agreement;
        try {
            agreement = builder.build();
        } catch (IllegalStateException e) {
            throw new BankException("Loan rejected: " + e.getMessage());
        }
        loans.add(agreement);
        System.out.println("[Facade] Loan money goes to account " + accountId);
        deposit(accountId, amount);
        return agreement;
    }

    public void applyMonthlyInterest() {
        System.out.println("[Facade] Applying monthly interest to " + accounts.size() + " accounts");
        for (Account account : accounts.values()) {
            double interest = account.applyMonthlyInterest();
            System.out.println("[Strategy] " + account.getId() + " uses " + account.getInterestStrategy().getName()
                    + ": " + (interest >= 0 ? "+" : "") + Money.kzt(interest)
                    + ", new balance " + Money.kzt(account.getBalance()));
        }
    }

    // Shows that the strategy of an account can be replaced while the program runs
    public void changeInterestStrategy(String accountId, String strategyType, double annualRate) {
        Account account = findAccount(accountId);
        InterestStrategy strategy;
        switch (strategyType.toUpperCase()) {
            case "SAVINGS":
                strategy = new SavingsInterestStrategy(annualRate);
                break;
            case "CHECKING":
                strategy = new CheckingInterestStrategy();
                break;
            case "LOAN":
                strategy = new LoanInterestStrategy(annualRate);
                break;
            default:
                throw new BankException("Unknown strategy: " + strategyType);
        }
        String old = account.getInterestStrategy().getName();
        account.setInterestStrategy(strategy);
        System.out.println("[Strategy] Account " + accountId + " changed strategy from " + old
                + " to " + strategy.getName());
    }

    public void showAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts yet.");
            return;
        }
        System.out.println("Accounts:");
        for (Account account : accounts.values()) {
            System.out.println("  " + account);
        }
        if (!loans.isEmpty()) {
            System.out.println("Loans:");
            for (LoanAgreement loan : loans) {
                System.out.println("  " + loan);
            }
        }
    }

    public void showCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No customers yet.");
            return;
        }
        for (Customer customer : customers.values()) {
            System.out.println("  " + customer);
        }
    }

    // accountId can be empty, then the whole history is shown
    public void showTransactionHistory(String accountId) {
        List<Transaction> list;
        if (accountId == null || accountId.isBlank()) {
            list = auditLogger.getHistory();
        } else {
            list = auditLogger.getHistory(findAccount(accountId).getId());
        }
        if (list.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        System.out.println("Transaction history (from AuditLogger):");
        for (Transaction t : list) {
            System.out.println("  " + t);
        }
    }

    private Customer findCustomer(String id) {
        Customer customer = customers.get(id.trim().toUpperCase());
        if (customer == null) {
            throw new BankException("Unknown customer: " + id);
        }
        return customer;
    }

    private Account findAccount(String id) {
        Account account = accounts.get(id.trim().toUpperCase());
        if (account == null) {
            throw new BankException("Unknown account: " + id);
        }
        return account;
    }
}
