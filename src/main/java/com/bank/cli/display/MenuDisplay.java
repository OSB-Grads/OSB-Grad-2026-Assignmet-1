package com.bank.cli.display;

import com.bank.enums.Role;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.bank.customer.ProductService;
import com.bank.dto.AccountDTO;
import com.bank.dto.ProductDTO;
import com.bank.orchestrator.AccountOpeningOrchestrator;

/**
 * Handles all CLI menu display and user input.
 * This class is responsible for showing menus and collecting user choices.
 */
public class MenuDisplay {
    private Scanner scanner;
    // private Principle ctx = Principle.getInstance();
    private final ProductService productService;
    
    public MenuDisplay() {
        this.scanner = new Scanner(System.in);
        this.productService = new ProductService();
    }
    
    /**
     * Display the main menu and handle user navigation.
     */
    public void showMainMenu() {
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
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    /**
     * Display the customer menu after a CUSTOMER logs in.
     */
    public void showCustomerMenu() {
        boolean loggedIn = true;

        while (loggedIn) {
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
                        System.out.println("Logging out...");
                        loggedIn = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please select 1-9.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Display the admin menu after an ADMIN logs in.
     */
    public void showAdminMenu() {
        boolean loggedIn = true;

        while (loggedIn) {
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
                        System.out.println("Logging out...");
                        loggedIn = false;
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
    
    private void handleLogin() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        // TODO: Call AuthService to validate credentials

        if (user.getRole() == Role.ADMIN) {
            showAdminMenu();
        } else {
            showCustomerMenu();
        }
        System.out.println("TODO: Implement login logic using AuthService");

    }
    
    private void handleCreateProfile() {

        System.out.println("\n=== CREATE CUSTOMER PROFILE ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        String password;
        String confirmPassword;

        do{
            System.out.print("Password: ");
            password = scanner.nextLine().trim();

            System.out.print("Retype Password : ");
            confirmPassword = scanner.nextLine().trim();

            if(!password.equals(confirmPassword))
            {
                System.out.println("Passwords do not match");
            }
        }while(!password.equals(confirmPassword));

        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Date of Birth in YYYY-MM-DD");
        String dateOfBirth = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Address: ");
        String address = scanner.nextLine().trim();
        System.out.print("National ID: ");
        String nationalId = scanner.nextLine().trim();

        // orchestrator.SignupOrchestrar(pass all input values)
        //System.out.println(message);
    }
    
    private void handleOpenAccount() {
        System.out.println("\n=== OPEN BANK ACCOUNT ===");
        
           System.out.println("1. Savings");
           System.out.println("2. Fixed Deposit");
           System.out.println("3. Limited Access");
           
           int ch=scanner.nextInt();
          
            List<ProductDTO> productList;
            int i;
            int productChoice;
            Long Acc;
            String category;
    try{
           switch(ch){
            case 1:
               productList = productService.listProductsByCategory("Savings");
            break;
            case 2:
               productList = productService.listProductsByCategory("Fixed Deposit");
            break;
            case 3:
               productList = productService.listProductsByCategory("Limited Access");
            break;
            default:
              System.out.println("Invalid choice");
            break;
           }
           
             i=0;
             for(ProductDTO PrintProducts: productList){
                System.out.println(i +"."+ PrintProducts.getProductName());
                i++;
             }
              System.out.println("select a product");
              productChoice=scanner.nextInt();
               Acc= AccountOpeningOrchestrator.openAccount(session.getCustomerId,productList.get(productChoice-1).getId());
              System.out.println("Account Number is: " + Acc);
        }
        
        catch(SQLException e){
               System.out.println("unable to fetch the products");
        }
    
        // System.out.println("TODO: Implement account opening using AccountOpeningOrchestrator");
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
    
    private void handleTransfer() {
        System.out.println("\n=== TRANSFER MONEY ===");
        // TODO: Show transfer options (Savings to Savings, Savings to FD)
        System.out.println("TODO: Implement transfer logic using appropriate Orchestrator");
    }
    
    private void handleViewAccounts() {
        System.out.println("\n=== YOUR ACCOUNTS ===");
        // TODO: Call AccountService to get user's accounts and display them
        
        System.out.println("TODO: Implement account viewing using AccountService");
    }
    
    private void handleViewTransactionHistory() {
        System.out.println("\n=== TRANSACTION HISTORY ===");
        // TODO: Show user's accounts, let them select one, then show transaction history
        System.out.println("TODO: Implement transaction history using TransactionService");
    }
    
    private void handleRequestLoan() {
        System.out.println("\n=== REQUEST A LOAN ===");
        // TODO: pick loan category + amount -> LoanOrchestrator assesses, offers or rejects
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
        //   java -jar real-world-bank/target/real-world-bank-1.0.0.jar seed
        System.out.println("TODO: Implement inbox viewing");
    }

    private void handleRunPaymentProcessor() {
        System.out.println("\n=== RUN PAYMENT PROCESSOR ===");
        // TODO: process pending deposit/withdraw queue entries via PaymentProcessorOrchestrator
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
}
