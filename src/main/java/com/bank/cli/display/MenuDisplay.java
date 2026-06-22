package com.bank.cli.display;

import com.bank.customer.AccountsService;
import com.bank.customer.AuthService;
import com.bank.dto.AccountDTO;
import com.bank.dto.TransactionDTO;
import com.bank.enums.Role;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.NegativeAmountException;
import com.bank.exception.SameAccountTransferException;
import com.bank.exception.UserAlreadyExistsException;
import com.bank.exception.UserCreationFailedException;


import java.sql.SQLException;
import java.util.List;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.bank.orchestrator.SignupOrchestrator;
import com.bank.orchestrator.TransferOrchestrator;
import javax.security.auth.login.AccountLockedException;

import com.bank.exception.InsufficientFundsException;
import com.bank.exception.NegativeAmountException;
import com.bank.exception.SameAccountTransferException;
import com.bank.exception.UserAlreadyExistsException;
import com.bank.exception.UserCreationFailedException;
import com.bank.customer.ProductService;
import com.bank.dto.ProductDTO;
import com.bank.exception.*;
import com.bank.orchestrator.AccountOpeningOrchestrator;
import com.bank.orchestrator.SignupOrchestrator;
import com.bank.orchestrator.TransferOrchestrator;
import com.bank.service.TransactionService;
import com.bank.session.Session;
import com.bank.utils.UuidGeneratorUtil;
import com.bank.orchestrator.SignupOrchestrator;
import com.bank.orchestrator.TransferOrchestrator;

import javax.security.auth.login.AccountLockedException;

/**
 * Handles all CLI menu display and user input.
 * This class is responsible for showing menus and collecting user choices.
 */
public class MenuDisplay {
    private final Scanner scanner;
    private final ProductService productService;
    private final Session session;
    private final SignupOrchestrator signupOrchestrator;
    private final TransferOrchestrator transferOrchestrator;
    private final AccountsService accountsService;
    private final AuthService authService;
    private final TransactionService transactionService;

    public MenuDisplay() {
        this.scanner = new Scanner(System.in);
        this.productService = new ProductService();
        this.session = Session.getInstance();
        this.signupOrchestrator = new SignupOrchestrator();
        this.accountsService = new AccountsService();
        this.transferOrchestrator = new TransferOrchestrator();
        this.authService = new AuthService();
        this.transactionService = new TransactionService();
    }

    /**
     * Display the main menu and handle user navigation.
     */
    public void showMainMenu() throws SQLException {
        boolean running = true;
        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Login");
            System.out.println("2. Create Customer Profile");
            System.out.println("3. Exit");
            System.out.print("Please select an option (1-3): ");
          

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        handleLogin();
                        break;
                    case 2:
                        handleCreateProfile();
                        break;
                    case 3:
                        System.out.println("Thank you for using CLI Banking Application!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please select 1, 2, or 3.");
                }
            } catch (NumberFormatException | SQLException | UserCreationFailedException
                    | UserAlreadyExistsException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Display the customer menu after a CUSTOMER logs in.
     * 
     * @throws SQLException
     */
    public void showCustomerMenu() throws SQLException {
        while (session.getCustomerId() != null) {
            System.out.println("\n=== CUSTOMER MENU ===");
            System.out.println("1. Open Bank Account");
            System.out.println("2. View Accounts & Balances");
            System.out.println("3. Deposit Money (add to queue)");
            System.out.println("4. Withdraw Money (add to queue)");
            System.out.println("5. Transfer Between My Accounts");
            System.out.println("6. Request a Loan");
            System.out.println("7. View Transaction History");
            System.out.println("8. Update Profile Info");
            System.out.println("9. Logout");
            System.out.print("Please select an option (1-9): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        handleOpenAccount();
                        break;
                    case 2:
                        handleViewAccounts();
                        break;
                    case 3:
                        handleDeposit();
                        break;
                    case 4:
                        handleWithdraw();
                        break;
                    case 5:
                        handleTransfer();
                        break;
                    case 6:
                        handleRequestLoan();
                        break;
                    case 7:
                        handleViewTransactionHistory();
                        break;
                    case 8:
                        handleUpdateProfile();
                        break;
                    case 9:
                        handleLogout();
                        break;
                    default:
                        System.out.println("Invalid option. Please select 1-9.");
                }
            } catch (NumberFormatException | NegativeAmountException | AccountLockedException
                    | SameAccountTransferException | InsufficientFundsException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Display the admin menu after an ADMIN logs in.
     */
    public void showAdminMenu() {

        while (session.getCustomerId() != null) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Manage Products");
            System.out.println("2. View Inbox Messages");
            System.out.println("3. Run Payment Processor");
            System.out.println("4. Run Loan Repayments");
            System.out.println("5. View Logs");
            System.out.println("6. Logout");
            System.out.print("Please select an option (1-6): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        handleManageProducts();
                        break;
                    case 2:
                        handleViewInbox();
                        break;
                    case 3:
                        handleRunPaymentProcessor();
                        break;
                    case 4:
                        handleRunLoanRepayments();
                        break;
                    case 5:
                        handleViewLogs();
                        break;
                    case 6:
                        handleLogout();
                        break;
                    default:
                        System.out.println("Invalid option. Please select 1-6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // TODO: Implement these methods by calling appropriate services/orchestrators

    private void handleLogin() throws SQLException {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        Map<String, Object> res = authService.login(username, password);

        session.login((Long.parseLong(res.get("customerId").toString())), (Role) res.get("role"));
        if (session.getRole() == Role.ADMIN) {
            System.out.println("\n\n--------------------------------------------------");
            System.out.println("Welcome " + username);
            System.out.println("--------------------------------------------------");
            showAdminMenu();
        } else {
            System.out.println("\n\n--------------------------------------------------");
            System.out.println("Welcome " + username);
            System.out.println("--------------------------------------------------");
            showCustomerMenu();
        }

    }

    private void handleCreateProfile() throws SQLException, UserCreationFailedException, UserAlreadyExistsException {

        System.out.println("\n=== CREATE CUSTOMER PROFILE ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        String password;
        String confirmPassword;

        do {
            System.out.print("Password: ");
            password = scanner.nextLine().trim();

            System.out.print("Retype Password : ");
            confirmPassword = scanner.nextLine().trim();

            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match");
            }
        } while (!password.equals(confirmPassword));

        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Date of Birth in YYYY-MM-DD : ");
        String dateOfBirth = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Address: ");
        String address = scanner.nextLine().trim();
        System.out.print("National ID: ");
        String nationalId = scanner.nextLine().trim();

        try {
            signupOrchestrator.signup(username, firstName, lastName, dateOfBirth, email, phone,
                    address, nationalId, password);
        } catch (UserCreationFailedException | UserAlreadyExistsException | SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Customer Created Successfully");
    }

    private void handleOpenAccount() {
        System.out.println("\n=== OPEN BANK ACCOUNT ===");

        System.out.println("1. Savings");
        System.out.println("2. Fixed Deposit");
        System.out.println("3. Limited Access");
        System.out.println("Select a Product Category");

        int ch = scanner.nextInt();
        System.out.println();

        List<ProductDTO> productList = null;
        int i;
        int productChoice = 0;
        AccountDTO Acc;
        String category;
        try {
            switch (ch) {
                case 1:
                    productList = productService.listProductsByCategory("Savings");
                    break;
                case 2:
                    productList = productService.listProductsByCategory("Fixed Deposits");
                    break;
                case 3:
                    productList = productService.listProductsByCategory("Limited Access");
                    break;
                default:
                    System.out.println("Invalid choice");
                    return;
            }

            i = 1;
            for (ProductDTO PrintProducts : productList) {
                System.out.println(i + "." + PrintProducts.getProductName());
                i++;
            }
            System.out.println("select a product");
            productChoice = scanner.nextInt();
            scanner.nextLine();

            Long accNo = accountsService.createAccount(session.getCustomerId(),
                    productList.get(productChoice - 1).getId());
            System.out.println("Account Created Suceesfully. Your Account Number is:" + accNo);
        }

        catch (SQLException e) {
            System.out.println("unable to fetch the products");
        }
    }

    private void handleDeposit() {
        System.out.println("\n=== DEPOSIT MONEY ===");
        // TODO: Show user's accounts, get account selection and amount
        System.out.println("TODO: Implement deposit logic using TransactionOrchestrator");
    }

    private void handleWithdraw() {
        System.out.println("\n=== WITHDRAW MONEY ===");
        // TODO: Show user's savings accounts only, get account selection and amount
        System.out.println("TODO: Implement withdrawal logic using TransactionOrchestrator");
    }

    private void handleTransfer() throws NegativeAmountException, AccountLockedException, SameAccountTransferException,
            InsufficientFundsException, SQLException {
        System.out.println("\n=== TRANSFER MONEY ===");
        List<Map<String, Object>> accounts = accountsService.getAllAccountsForCustomer(session.getCustomerId());
        System.out.println("Customer ID: " + session.getCustomerId());
        System.out.println("Accounts returned: " + accounts.size());
        int count = 1;
        System.out.println("Select source Account");
        for (Map<String, Object> account : accounts) {
            System.out.println(
                    count++ + ". Account Number: " + account.get("id") + " | Balance: " + account.get("balance"));
        }
        int option1 = scanner.nextInt();
        scanner.nextLine();
        Long sourceAccountId = ((Number) accounts.get(option1 - 1).get("id")).longValue();

        count = 1;
        System.out.println("Select destination Account");
        for (Map<String, Object> account : accounts) {
            System.out.println(
                    count++ + ". Account Number: " + account.get("id") + " | Balance: " + account.get("balance"));

        }
        int option2 = scanner.nextInt();
        scanner.nextLine();
        Long destinationAccountId = ((Number) accounts.get(option2 - 1).get("id")).longValue();
        System.out.println("Money you want to transfer");
        BigDecimal amountToBeTransferred = scanner.nextBigDecimal();
        scanner.nextLine();
        transferOrchestrator.transfer(session.getCustomerId(), sourceAccountId, destinationAccountId,
                amountToBeTransferred);
        accounts = accountsService.getAllAccountsForCustomer(session.getCustomerId());
        System.out.println("\n Transfer successful! Updated balances:");
        for (Map<String, Object> account : accounts) {
            System.out.println("Account Number: " + account.get("id") +
                    " | Balance: " + account.get("balance"));
        }
    }

    private void handleViewAccounts() {

        System.out.println("\n=== YOUR ACCOUNTS ===");

        List<Map<String, Object>> accounts = accountsService.getAllAccountsForCustomer(session.getCustomerId());

        HashMap<String, BigDecimal> balances = new HashMap<>();

        balances.put("Total Balance", BigDecimal.ZERO);
        balances.put("Savings", BigDecimal.ZERO);
        balances.put("Fixed Deposits", BigDecimal.ZERO);
        balances.put("Limited Access", BigDecimal.ZERO);

        // Calculate balances
        for (Map<String, Object> account : accounts) {

            String category = (String) account.get("category");

            BigDecimal balance = BigDecimal.valueOf(((Number) account.get("balance")).doubleValue());
            balances.put("Total Balance", balances.get("Total Balance").add(balance));
            balances.put(category, balances.getOrDefault(category, BigDecimal.ZERO).add(balance));
        }

        while (true) {

            System.out.println("\n=== YOUR ACCOUNTS ===");
            System.out.println(
                    "Total Balance: $" + balances.get("Total Balance"));

            Map<Integer, Map<String, Object>> optionMap = new HashMap<>();

            String[] categories = {
                    "Savings",
                    "Limited Access",
                    "Fixed Deposits"
            };

            char section = 'A';
            int optionNumber = 1;

            for (String category : categories) {

                System.out.println("\n" + section + ") " + category + " Accounts $"
                        + balances.getOrDefault(category, BigDecimal.ZERO));

                for (Map<String, Object> account : accounts) {

                    if (category.equals(account.get("category"))) {

                        System.out.println(
                                optionNumber + ") Product Name: "
                                        + account.get("product_name"));

                        System.out.println(
                                "   Account Number: "
                                        + account.get("id"));

                        System.out.println(
                                "   Balance: $"
                                        + account.get("balance"));

                        optionMap.put(optionNumber, account);

                        optionNumber++;
                    }
                }

                section++;
            }

            System.out.println("\nSelect Product Number to View Details");
            System.out.println("0. Back to Customer Menu");
            System.out.println("-1. Exit");
            System.out.print("Choice: ");

            int selectedOption = scanner.nextInt();
            scanner.nextLine();

            if (selectedOption == -1) {
                System.out.println("Thank you for using CLI Banking Application!");
                System.exit(0);
            }

            if (selectedOption == 0) {
                return; // back to customer menu
            }

            Map<String, Object> selectedAccount = optionMap.get(selectedOption);

            if (selectedAccount == null) {
                System.out.println("Invalid Product Number");
                continue;
            }

            System.out.println("\n=== ACCOUNT DETAILS ===");

            System.out.println(
                    "Product Name: "
                            + selectedAccount.get("product_name"));

            System.out.println(
                    "Account Number: "
                            + selectedAccount.get("id"));

            System.out.println(
                    "Category: "
                            + selectedAccount.get("category"));

            System.out.println(
                    "Balance: $"
                            + selectedAccount.get("balance"));

            Long accountId = ((Number) selectedAccount.get("id")).longValue();

            List<TransactionDTO> transactions = transactionService.listAccountTransactions(accountId);

            System.out.println("\n=== TRANSACTIONS ===");

            System.out.printf(
                    "%-18s %-15s %-15s %-15s %-12s %-15s %-20s%n",
                    "TRANSACTION ID",
                    "FROM ACCOUNT",
                    "TO ACCOUNT",
                    "TYPE",
                    "AMOUNT",
                    "STATUS",
                    "CREATED AT");

            System.out.println(
                    "-----------------------------------------------------------------------------------------------------------------");

            for (TransactionDTO transaction : transactions) {

                System.out.printf(
                        "%-18s %-15s %-15s %-15s %-12s %-15s %-20s%n",
                        transaction.getId(),
                        transaction.getFromAccountId() == null
                                ? "-"
                                : transaction.getFromAccountId(),
                        transaction.getToAccountId() == null
                                ? "-"
                                : transaction.getToAccountId(),
                        transaction.getTransactionType(),
                        transaction.getAmount(),
                        transaction.getStatus(),
                        transaction.getCreatedAt());
            }

            System.out.println(
                    "-----------------------------------------------------------------------------------------------------------------");

            while (true) {

                System.out.println("\nOptions:");
                System.out.println("1. Back to Accounts List");
                System.out.println("0. Back to Customer Menu");
                System.out.println("-1. Exit");
                System.out.print("Choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    break; // account list is shown again
                }

                if (choice == 0) {
                    return; // customer menu
                }

                if (choice == -1) {
                    System.out.println("Thank you for using CLI Banking Application!");
                    System.exit(0);
                }

                System.out.println("Invalid Option");
            }
        }
    }

    private void handleViewTransactionHistory() {
        System.out.println("\n=== TRANSACTION HISTORY ===\n");
        List<TransactionDTO> transactions = transactionService.listCustomerTransactions(session.getCustomerId());
        System.out.printf(
                "%-18s %-15s %-15s %-15s %-12s %-15s %-20s%n",
                "TRANSACTION ID", "FROM ACCOUNT", "TO ACCOUNT", "TYPE", "AMOUNT", "STATUS", "CREATED AT");
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------");

        for (TransactionDTO transaction : transactions) {
            System.out.printf(
                    "%-18s %-15s %-15s %-15s %-12s %-15s %-20s%n",
                    transaction.getId(),
                    transaction.getFromAccountId() == null ? "-" : transaction.getFromAccountId(),
                    transaction.getToAccountId() == null ? "-" : transaction.getToAccountId(),
                    transaction.getTransactionType(),
                    transaction.getAmount(),
                    transaction.getStatus(),
                    transaction.getCreatedAt());
        }
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------");
    }

    private void handleRequestLoan() {
        System.out.println("\n=== REQUEST A LOAN ===");
        // TODO: pick loan category + amount -> LoanOrchestrator assesses, offers or
        // rejects
        System.out.println("TODO: Implement loan request using LoanOrchestrator");
    }

    private void handleUpdateProfile() {
        System.out.println("\n=== UPDATE PROFILE ===");
        // TODO: Allow customer to update contact information
        System.out.println("TODO: Implement profile update using CustomerService");
    }

    // --- Admin handlers (call services/orchestrators; no logic in the CLI) ---

    private void handleManageProducts() {
        System.out.println("\n=== MANAGE PRODUCTS ===");
        // TODO: create/list product categories and products via ProductService
        System.out.println("TODO: Implement product management using ProductService");
    }

    private void handleViewInbox() {
        System.out.println("\n=== INBOX MESSAGES ===");
        // TODO: list our inbox rows (results from the real-world bank) with
        // status/correlation_id/reason — useful for spotting stuck payments.
        // Seeding real-world accounts now lives in the real-world bank app:
        // java -jar real-world-bank/target/real-world-bank-1.0.0.jar seed
        System.out.println("TODO: Implement inbox viewing");
    }

    private void handleRunPaymentProcessor() {
        System.out.println("\n=== RUN PAYMENT PROCESSOR ===");
        // TODO: process pending deposit/withdraw queue entries via
        // PaymentProcessorOrchestrator
        System.out.println("TODO: Implement processor run using PaymentProcessorOrchestrator");
    }

    private void handleRunLoanRepayments() {
        System.out.println("\n=== RUN LOAN REPAYMENTS ===");
        // TODO: deduct due installments via LoanOrchestrator
        System.out.println("TODO: Implement loan repayments using LoanOrchestrator");
    }

    private void handleViewLogs() {
        System.out.println("\n=== VIEW LOGS ===");
        // TODO: display recent log entries via LogService
        System.out.println("TODO: Implement log viewing using LogService");
    }

    /**
     * Utility method to get user input with prompt.
     */
    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Utility method to display error messages.
     */
    public void showError(String message) {
        System.err.println("ERROR: " + message);
    }

    /**
     * Utility method to display success messages.
     */
    public void showSuccess(String message) {
        System.out.println("SUCCESS: " + message);
    }

    public void handleLogout() {
        session.logout();
        System.out.println("Logout successful.");
    }
}
