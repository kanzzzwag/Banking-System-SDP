# Banking System

This is the final project for the course Software Design Patterns. It is a simple console banking system written in plain Java 17. It uses six design patterns: Builder, Factory Method, Adapter, Facade, Strategy and Observer. The project does not use a database or external libraries. All data is kept in memory, so it is lost when the program is closed. All amounts are in KZT.

## How to run

The easiest way is IntelliJ IDEA. Open the project folder, wait until Maven loads the project, then open the class com.bank.Main and press the green Run button next to the main method. The menu will appear in the Run window.

You can also run it from the command line with JDK 17 or newer. Run these commands in the project folder:

```
mkdir out
javac -d out src/main/java/com/bank/*.java src/main/java/com/bank/*/*.java
java -cp out com.bank.Main
```

If Maven is installed, you can run mvn compile and then java -cp target/classes com.bank.Main.

After the start the program shows a numbered menu. Type the number of an option and press Enter. Option 12 runs a demo scenario. It creates two customers and four accounts and shows every pattern in action. The demo stops after each step, so press Enter to go to the next one. Each pattern prints its own tag, for example [Factory], [Builder], [Adapter], [Strategy], [Facade] and [Observer]. Option 0 closes the program.

## Packages and patterns

The package com.bank contains Main with the console menu and Demo with the demo scenario.

The package com.bank.model contains the main classes of the bank: Customer, Account, SavingsAccount, CheckingAccount, CreditAccount, Transaction, TransactionType, Money and BankException.

The package com.bank.builder contains the Builder pattern. LoanAgreement is created only through LoanAgreement.Builder, and the build() method checks all rules of the loan.

The package com.bank.factory contains the Factory Method pattern. AccountCreator has the abstract method createAccount(), and SavingsAccountCreator, CheckingAccountCreator and CreditAccountCreator create their own account type with the right interest strategy.

The package com.bank.adapter contains the Adapter pattern. PaymentGateway is the interface our bank uses for external payments. InternationalSwiftService and LocalCardProcessor are the third party classes with different methods, and SwiftPaymentAdapter and CardPaymentAdapter make them fit PaymentGateway.

The package com.bank.facade contains the Facade pattern. BankFacade is the only class that the menu talks to, and it uses all other parts inside.

The package com.bank.strategy contains the Strategy pattern. InterestStrategy is implemented by SavingsInterestStrategy, CheckingInterestStrategy and LoanInterestStrategy. The strategy of an account can be changed while the program runs with menu option 11.

The package com.bank.observer contains the Observer pattern. TransactionPublisher sends every deposit, withdrawal and transfer to FraudDetectionService, SmsNotificationService and AuditLogger.

## Other folders

The folder uml has the PlantUML diagrams. There is one class diagram for the whole system, one class diagram for each pattern and one sequence diagram for an external transfer. The folder docs has the project report, and docs/screenshots has the screenshots used in it.

## Team

Student 1 made Builder and Factory Method. Student 2 made Adapter and Facade. Student 3 made Strategy and Observer.
