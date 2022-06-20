import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Bank {

    private static final Scanner in = new Scanner(System.in);
    MenuData menu = new MenuData();
    CSVReader csvReader = new CSVReader();

    public static void main(String[] args) {
        MenuData menu = new MenuData();
        System.out.println("Welcome in banking system!");
        String choice = "";
        while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")) {
            menu.displayMainMenu();
            choice = in.next();
            menu.chooseMainMenuOption(choice);
        }
    }

    public void logIntoCustomer() throws NoSuchAlgorithmException {
        menu.displayLogIntoCustomerMenu();
    }

    public void createCustomer() throws IOException, NoSuchAlgorithmException {
        csvReader.newCustomer();
    }

    public void displayUsers() {
        for (Customer customer : csvReader.getCustomers()) {
            System.out.println(customer);
        }
    }

    public void displayAccounts() {
        for (Account account : csvReader.getAccounts()) {
            System.out.println(account);
        }
    }

    public void logIntoATM() throws NoSuchAlgorithmException, IOException {
        menu.displayLogIntoATMMenu();
    }

}
