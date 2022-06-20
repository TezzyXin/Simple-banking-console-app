import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ATM {

    private static Scanner in = new Scanner(System.in);
    private CSVReader csvReader = new CSVReader();

    /**
     * Withdraw is the method used to withdraw money from a given account,
     * this method can be accessed through the ATM option in the main menu
     * @param accountID     ID of the account we want to withdraw money from
     * @param amount        amount of money we want to withdraw from our account
     * @throws IOException
     */


    public void withdraw(int accountID, BigDecimal amount) throws IOException {
        Account myAccount = csvReader.getAccountFromGivenAccountID(accountID);
        if (amount.compareTo(myAccount.getBalance()) > 0) {
            System.out.println("You cannot withdraw more money than you have!");
        } else {
            if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                System.out.println("You cannot withdraw negative or zero amount of money!");
            } else {
                myAccount.setBalance(myAccount.getBalance().subtract(amount));
                System.out.println("Amount of money you have withdrawn: " + amount + ". New balance: " + myAccount.getBalance());
                csvReader.writeAccountsToCSV(csvReader.getAccounts());
            }
        }
    }
    /**
     * Deposit is the method used to deposit money to a given account,
     * this method can be accessed through the ATM option in the main menu
     * @param accountID     ID of the account we want to deposit money to
     * @param amount        amount of money we want to deposit from our account
     * @throws IOException
     */
    public void deposit(int accountID, BigDecimal amount) throws IOException {
        Account myAccount = csvReader.getAccountFromGivenAccountID(accountID);
        if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
            System.out.println("You cannot deposit negative or zero amount of money!");
        } else {

            myAccount.setBalance(myAccount.getBalance().add(amount));
            System.out.println("Amount of money you have deposited: " + amount + ". New balance: " + myAccount.getBalance());
            csvReader.writeAccountsToCSV(csvReader.getAccounts());
        }
    }

    /**
     * Transfer is the method used for transferring money to and from given accounts,
     * this method can be accessed from the account menu, to access account menu you have to first login into customer,
     * then you with an ID and a PIN you can access your account to use the transfer method
     * @param myAccountID       ID of the account we are sending money from
     * @param otherAccountID    ID of the account we are sending money to
     * @throws IOException
     */
    public void transfer(int myAccountID, int otherAccountID) throws IOException {
        outerloop:
        while (true) {
            if (csvReader.getAccountIDs().contains(otherAccountID) && otherAccountID != myAccountID) {
                Account myAccount = csvReader.getAccountFromGivenAccountID(myAccountID);
                Account otherAccount = csvReader.getAccountFromGivenAccountID(otherAccountID);
                while (true) {
                    try {
                        System.out.println("Enter amount of money you want to transfer (use comma instead of dot):");
                        BigDecimal transferMoney = in.nextBigDecimal();
                        transferMoney = transferMoney.setScale(2, RoundingMode.FLOOR);
                        if (transferMoney.compareTo(myAccount.getBalance()) > 0) {
                            System.out.println("You cannot transfer more money than you have!");
                        } else {
                            if (transferMoney.compareTo(BigDecimal.valueOf(0)) <= 0) {
                                System.out.println("You cannot transfer negative or zero amount of money!");
                            } else {
                                csvReader.getAccountFromGivenAccountID(myAccountID).setBalance(myAccount.getBalance().subtract(transferMoney));
                                csvReader.getAccountFromGivenAccountID(otherAccountID).setBalance(otherAccount.getBalance().add(transferMoney));
                                System.out.println("You have transferred " + transferMoney + " to account number " + otherAccountID);
                                csvReader.writeAccountsToCSV(csvReader.getAccounts());
                                break outerloop;
                            }
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input!");
                        break outerloop;
                    }
                }
            } else {
                System.out.println("Invalid account ID!\n");
                break;
            }
        }
    }
}
