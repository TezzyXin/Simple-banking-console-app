import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuData {

    private static Scanner in = new Scanner(System.in);
    private CSVReader csvReader = new CSVReader();

    private int temporaryUserID;
    private int temporaryAccountID;

    /**
     * Method used to display the Main menu of the program
     * It is used every time when the program is executed
     */
    public void displayMainMenu() {
        while (true) {
            String choice = "";
            System.out.println("\nChoose option:");
            System.out.println(" (1) Sign in\t\n (2) Log into ATM\t\n (3) Exit from the program");
            while (!choice.equals("1") && !choice.equals("2")) {
                choice = in.next();
                chooseMainMenuOption(choice);
            }
        }
    }

    /**
     *  Method used to display the customer menu of the program
     *  This menu is displayed every time when user logs into Customer
     */
    public void displayCustomerMenu() {
        System.out.println("\nHello customer!");
        outerloop:
        while (true) {
            String choice = "";
            System.out.println("\nChoose option:");
            System.out.println(" (1) View my accounts\t\n (2) Log into account\t\n (3) Change password\t\n (4) Log out");
            while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
                if (choice.equals("4")) {
                    break outerloop;
                } else {
                    choice = in.next();
                    chooseCustomerOption(choice);
                }
            }
        }
    }

    /**
     *  Method used to display the account menu of the program
     *  This menu is displayed every time when user logs into an account of a customer
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void displayAccountMenu() throws IOException, NoSuchAlgorithmException {
        System.out.println("Login to the account was successful");
        outerloop:
        while (true) {
            String choice = "";
            System.out.println("\nChoose option:");
            System.out.println(" (1) View balance\t\n (2) Transfer money\t\n (3) Change PIN\t\n (4) Log out");
            while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
                if (choice.equals("4")) {
                    break outerloop;
                } else {
                    choice = in.next();
                    chooseAccountOption(choice);
                }
            }
        }
    }

    /**
     * Method used to display the ATM menu of the program
     * This menu is displayed every time when user logs into ATM with the account credentials
     * @param accountID ID of the account we want to log into
     * @throws IOException
     */
    public void displayATMMenu(int accountID) throws IOException {
        System.out.println("Login to the account was successful");
        outerloop:
        while (true) {
            String choice = "";
            System.out.println("\nChoose option:");
            System.out.println(" (1) View balance\t\n (2) Withdraw money\t\n (3) Deposit money\t\n (4) Log out");
            while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
                if (choice.equals("4")) {
                    break outerloop;
                } else {
                    choice = in.next();
                    chooseATMOption(choice, accountID);
                }
            }
        }
    }

    /**
     * Method that takes in the option entered by the user,
     * so the user can access options given to him by the displayed ATM menu
     * @param option        option that the user picked, that states which method is called upon
     * @param accountID     ID of the  account we want to control by the options chose
     * @throws IOException
     */
    public void chooseATMOption(String option, int accountID) throws IOException {
        ATM atm = new ATM();
        switch (option) {
            case "1":
                csvReader.readAccountIDsFromFile();
                System.out.println("Your balance is: " + csvReader.readAccountsFromFile().stream().filter(account -> account.getAccountID() == temporaryAccountID).findFirst().get().getBalance());
                break;
            case "2":
                System.out.println("Withdrawing money...");
                System.out.println("Enter amount of money you want to withdraw (use comma instead of dot):");
                BigDecimal withdrawMoney = in.nextBigDecimal();
                withdrawMoney = withdrawMoney.setScale(2, RoundingMode.FLOOR);
                atm.withdraw(accountID, withdrawMoney);
                break;
            case "3":
                System.out.println("Depositing money...");
                System.out.println("Enter amount of money you want to deposit (use comma instead of dot):");
                BigDecimal depositMoney = in.nextBigDecimal();
                depositMoney = depositMoney.setScale(2, RoundingMode.FLOOR);
                atm.deposit(accountID, depositMoney);
                break;
            case "4":
                System.out.println("Logging out to main menu...");
                displayMainMenu();
                break;
            default:
                System.out.println("Enter correct option!");
        }
    }

    /**
     * Method used to display the Admin menu of the program
     * this is method is called everytime when the user logs into Admin
     */
    public void displayAdminMenu() {
        System.out.println("\nHello admin!");
        outerloop:
        while (true) {
            String choice = "";
            System.out.println("\nChoose option:");
            System.out.println(" (1) Display all users\t\n (2) Display all accounts\t\n (3) Add new user\t\n (4) Add new account\t\n (5) Delete account\t\n (6) Change password\t\n (7) Change PIN\t\n (8) Log out");
            while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4") && !choice.equals("5") && !choice.equals("6") && !choice.equals("7")) {
                if (choice.equals("8")) {
                    break outerloop;
                } else {
                    choice = in.next();
                    chooseAdminOption(choice);
                }
            }
        }
    }

    /**
     * Method that takes in the option entered by the user,
     * so the user can access options given to him by the displayed Main menu
     * @param option        option that the user picked, that states which method is called upon
     */
    public void chooseMainMenuOption(String option) {
        Bank bank = new Bank();
        try {
            switch (option) {
                case "1":
                    System.out.println("Logging into customer...");
                    bank.logIntoCustomer();
                    break;
                case "2":
                    System.out.println("Logging into ATM...");
                    bank.logIntoATM();
                    break;
                case "3":
                    System.out.println("See you next time!");
                    System.exit(0);
                default:
                    System.out.println("Please re-enter main menu option:");
            }
        } catch (InputMismatchException | NoSuchAlgorithmException | IOException e) {
            System.out.println("Please re-enter main menu option");
        }
    }

    /**
     * Method that takes in the option entered by the user,
     * so the user can access options given to him by the displayed Customer menu
     * @param option        option that the user picked, that states which method is called upon
     */

    public void chooseCustomerOption(String option) {
        try {
            switch (option) {
                case "1":
                    System.out.println("Viewing accounts...");
                    ArrayList<Account> temporaryAccounts = csvReader.getAccountsFromGivenCustomerID(temporaryUserID);
                    for (Account account : temporaryAccounts) {
                        System.out.println(account.toString(true));
                    }
                    break;
                case "2":
                    System.out.println("Logging into account...");
                    displayLogIntoAccountMenu();
                    break;
                case "3":
                    System.out.println("Changing password...");
                    csvReader.changePasswordByCustomer(temporaryUserID);
                    break;
                case "4":
                    System.out.println("Logging out to main menu...");
                    displayMainMenu();
                    String choice = in.next();
                    chooseMainMenuOption(choice);
                    break;
                default:
                    System.out.println("Enter correct option!");
            }
        } catch (InputMismatchException | NoSuchAlgorithmException | IOException e) {
            System.out.println("Enter correct option!");
        }
    }

    /**
     * Method that takes in the option entered by the user,
     * so the user can access options given to him by the displayed Account menu
     * @param option        option that the user picked, that states which method is called upon
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void chooseAccountOption(String option) throws IOException, NoSuchAlgorithmException {
        ATM atm = new ATM();
        switch (option) {
            case "1":
                System.out.println("Your balance is: " + csvReader.readAccountsFromFile().stream().filter(account -> account.getAccountID() == temporaryAccountID).findFirst().get().getBalance());
                break;
            case "2":
                System.out.println("Transferring money");
                System.out.println("Enter ID of account you want to transfer money to:");
                int accountID = in.nextInt();
                atm.transfer(temporaryAccountID, accountID);
                break;
            case "3":
                System.out.println("Changing PIN number...");
                csvReader.changePINByCustomer(temporaryAccountID);
                break;
            case "4":
                System.out.println("Logging out to customer menu...");
                displayCustomerMenu();
                String choice = in.next();
                chooseCustomerOption(choice);
                break;
            default:
                System.out.println("Enter correct option!");
        }
    }

    /**
     * Method that takes in the option entered by the user,
     * so the user can access options given to him by the displayed Admin menu
     * @param option        option that the user picked, that states which method is called upon
     */
    public void chooseAdminOption(String option) {
        Bank bank = new Bank();
        try {
            switch (option) {
                case "1":
                    System.out.println("Displaying data of all users...\n");
                    bank.displayUsers();
                    break;
                case "2":
                    System.out.println("Displaying data of all accounts...\n");
                    bank.displayAccounts();
                    break;
                case "3":
                    System.out.println("Creating new user...");
                    bank.createCustomer();
                    break;
                case "4":
                    System.out.println("Creating new account...");
                    System.out.println("Enter customer ID");
                    int customerID = in.nextInt();
                    csvReader.newAccount(customerID);
                    break;
                case "5":
                    System.out.println("Deleting account...");
                    System.out.println("Enter ID of account you want to delete:");
                    int accountID = in.nextInt();
                    csvReader.removeAccount(accountID);
                    break;
                case "6":
                    System.out.println("Changing password...");
                    System.out.println("Enter the ID of the customer you want to change the password for:");
                    customerID = in.nextInt();
                    csvReader.changePasswordByAdmin(customerID);
                    break;
                case "7":
                    System.out.println("Changing PIN...");
                    System.out.println("Enter the ID of the account you want to change the PIN for:");
                    accountID = in.nextInt();
                    csvReader.changePINByAdmin(accountID);
                    break;
                case "8":
                    System.out.println("Logging out to main menu...");
                    displayMainMenu();
                    String choice = in.next();
                    chooseMainMenuOption(choice);
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        } catch (InputMismatchException | IOException | NoSuchAlgorithmException e) {
            System.out.println("Enter correct option!");
        }
    }

    /**
     * Method that is displayed when the user wants to log into the Customer menu,
     * with the Customer credentials
     * @throws NoSuchAlgorithmException
     */
    public void displayLogIntoCustomerMenu() throws NoSuchAlgorithmException {
        while (true) {
            System.out.println("Enter your customer ID:");
            int logCustomerID = in.nextInt();
            if (csvReader.getCustomerIDs().contains(logCustomerID)) {
                System.out.println("Enter your password:");
                String logPassword = in.next();
                if (csvReader.getPasswordFromGivenCustomerID(logCustomerID).equals(csvReader.hash(logPassword))) {
                    if (csvReader.getRoleFromGivenCustomerID(logCustomerID).equals("customer")) {
                        temporaryUserID = logCustomerID;
                        displayCustomerMenu();
                    } else {
                        displayAdminMenu();
                    }
                } else {
                    System.out.println("Invalid password!\n");
                }
            } else {
                System.out.println("Invalid ID!");
            }
        }
    }

    /**
     * Method that is displayed when the user wants to log into the Account menu,
     * with the Account credentials
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public void displayLogIntoAccountMenu() throws NoSuchAlgorithmException, IOException {
        while (true) {
            System.out.println("Enter your account ID:");
            int logAccountID = in.nextInt();
            temporaryAccountID = logAccountID;
            if (csvReader.getAccountIDsFromGivenCustomerID(temporaryUserID).contains(logAccountID)) {
                System.out.println("Enter PIN:");
                String logPIN = in.next();
                if (csvReader.getPINFromGivenAccountID(logAccountID).equals(csvReader.hash(logPIN))) {
                    displayAccountMenu();
                } else {
                    System.out.println("Wrong PIN");
                    displayCustomerMenu();
                }
            } else {
                System.out.println("Invalid account ID!\n");
            }
        }
    }

    /**
     * Method that is displayed when the user wants to log into the ATM menu,
     * with the Account credentials
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public void displayLogIntoATMMenu() throws NoSuchAlgorithmException, IOException {
        while (true) {
            System.out.println("Enter your account ID:");
            int logAccountID = in.nextInt();
            temporaryAccountID = logAccountID;
            if (csvReader.getAccountIDs().contains(logAccountID)) {
                System.out.println("Enter PIN:");
                String logPIN = in.next();
                if (csvReader.getPINFromGivenAccountID(logAccountID).equals(csvReader.hash(logPIN))) {
                    displayATMMenu(logAccountID);
                } else {
                    System.out.println("Wrong PIN");
                    displayMainMenu();
                }
            } else {
                System.out.println("Invalid account ID!\n");
            }
        }

    }
}
