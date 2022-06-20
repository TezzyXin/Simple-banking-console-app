import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CSVReader {

    private static final String SEPARATOR = ", ";
    private static final Scanner in = new Scanner(System.in);
    private ArrayList<Customer> customers = readCustomersFromFile();
    private ArrayList<Account> accounts = readAccountsFromFile();
    private ArrayList<Integer> customerIDs = readCustomerIDsFromFile();
    private ArrayList<Integer> accountIDs = readAccountIDsFromFile();

    public ArrayList<Integer> getAccountIDs() {
        return accountIDs;
    }

    public ArrayList<Integer> getCustomerIDs() {
        return customerIDs;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    /**
     * This method returns all accountIDs which are assigned to customerID.
     * It is used when customer wants to log into his account and it needs to be checked
     * if account he wants to log into belongs to him.
     *
     * @param customerID    ID of user we check to get list of accountIDs
     * @return              list of accounts assigned to specific customerID
     */
    public ArrayList<Integer> getAccountIDsFromGivenCustomerID(int customerID) {
        ArrayList<Account> accounts = getAccountsFromGivenCustomerID(customerID);
        ArrayList<Integer> accountIDs = new ArrayList<>();
        for (Account account : accounts) {
            accountIDs.add(account.getAccountID());
        }
        return accountIDs;
    }

    /**
     * This method returns hash of password which is assigned to userID.
     * It is used when logging into user to check if hash of password we entered
     * and password of user are equal.
     *
     * @param customerID    ID of user we check to get his password
     * @return              hash of password of specific user
     */
    public String getPasswordFromGivenCustomerID(int customerID) {
        List<Customer> logCustomer = customers.stream().filter(customer -> customer.getCustomerID() == customerID).collect(Collectors.toList());
        return logCustomer.get(0).getPassword();
    }

    /**
     * This method returns hash of PIN which is assigned to accountID.
     * It is used when logging into account to chech if hash of PIN we entered
     * and PIN of account are equal.
     *
     * @param accountID     ID of account we check to get its PIN
     * @return              hash of PIN of specific account
     */
    public String getPINFromGivenAccountID(int accountID) {
        List<Account> logAccount = accounts.stream().filter(account -> account.getAccountID() == accountID).collect(Collectors.toList());
        return logAccount.get(0).getPIN();
    }

    /**
     * This method returns object of customer with given customerID to get access to its fields.
     * It is used when customer or admin changes password and we need to get
     * object of customer to set a new password.
     *
     * @param customerID    ID of customer we check to get object of customer with this customerID
     * @return              object of customer with given customerID
     */
    public Customer getCustomerFromGivenCustomerID(int customerID) {
        List<Customer> logCustomer = customers.stream().filter(customer -> customer.getCustomerID() == customerID).collect(Collectors.toList());
        return logCustomer.get(0);
    }

    /**
     * This method returns object of account with given accountID to get access to its fields.
     * It is used when customer or admin changes PIN and we need to get
     * object of account to set a new PIN or when money is transferred and we need to set or get balance.
     *
     * @param accountID     ID od account we check to get object of account with this accountID
     * @return              object of account with given accountID
     */
    public Account getAccountFromGivenAccountID(int accountID) {
        List<Account> logAccount = accounts.stream().filter(account -> account.getAccountID() == accountID).collect(Collectors.toList());
        return logAccount.get(0);
    }

    /**
     * This method returns role of user with given userID.
     * It is used when we are logging into user and we need to check role to
     * find out which type of privileges we can get.
     *
     * @param customerID    ID of user we check to get his role
     * @return              role of user with given userID
     */
    public String getRoleFromGivenCustomerID(int customerID) {
        List<Customer> logCustomer = customers.stream().filter(customer -> customer.getCustomerID() == customerID).collect(Collectors.toList());
        return logCustomer.get(0).getRole();
    }

    /**
     * This method gives list of accounts which belongs to specific customer.
     * It is used when customer wants to display all his accounts and it is needed
     * to get all his accounts.
     *
     * @param customerID    customerID from which we want to extract his accounts
     * @return              list of accounts which belongs to customer with given customerID
     */
    public ArrayList<Account> getAccountsFromGivenCustomerID(int customerID) {
        accounts = readAccountsFromFile();
        return accounts.stream().filter(account -> account.getCustomerID() == customerID).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * This method remove account with given accountID. It checks if balance of
     * account equals zero. If this is true program delete object from list of accounts
     * else it sends note to transfer/withdraw money
     *
     * @param accountID         ID of account we want to delete
     * @throws IOException
     */
    public void removeAccount(int accountID) throws IOException {
        Account accountToRemove = getAccountFromGivenAccountID(accountID);
        if (accountToRemove.getBalance().compareTo(BigDecimal.valueOf(0)) > 0) {
            System.out.println("Balance of account you want to delete is " + accountToRemove.getBalance());
            System.out.println("You need to make sure that customer will transfer his money to his another account or deposit money");
        } else {
            System.out.println("Deleted account which belongs to customer no. " + accountToRemove.getCustomerID() + ", accountID: " + accountToRemove.getAccountID());
            accounts.remove(accountToRemove);
            writeAccountsToCSV(accounts);
        }
    }

    /**
     * This method reads all lines from customers.csv using BufferedReader
     * and uses method createCustomer() to make list of customers which we can use in program
     *
     * @return  list of customers read from file
     */
    public ArrayList<Customer> readCustomersFromFile() {
        ArrayList<Customer> customers = new ArrayList<>();
        BufferedReader br;
        String line;
        try {
            br = new BufferedReader(new FileReader("customers.csv"));

            while ((line = br.readLine()) != null) {
                String[] data = line.split(", ");

                Customer customer = createCustomer(data);

                customers.add(customer);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * This method reads all lines from accounts.csv using BufferedReader
     * and uses method createAccount() to make list of accounts which we can use in program
     *
     * @return  list of accounts read from file
     */
    public ArrayList<Account> readAccountsFromFile() {
        ArrayList<Account> accounts = new ArrayList<>();
        BufferedReader br;
        String line;
        try {
            br = new BufferedReader(new FileReader("accounts.csv"));

            while ((line = br.readLine()) != null) {
                String[] data = line.split(", ");

                Account account = createAccount(data);

                accounts.add(account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }


    /**
     * This method reads all lines from customers.csv using BufferedReader
     * and uses method createCustomerID() to make list of customerIDs which we can use in program.
     * It is mainly used when we are generating new user and want to prevent situation
     * when different users have the same userID.
     *
     * @return list of customerIDs read from file
     */
    public ArrayList<Integer> readCustomerIDsFromFile() {
        ArrayList<Integer> customerIDs = new ArrayList<>();
        BufferedReader br;
        String line;
        try {
            br = new BufferedReader(new FileReader("customers.csv"));

            while ((line = br.readLine()) != null) {
                String[] data = line.split(", ");

                int customerID = createCustomerID(data);

                customerIDs.add(customerID);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customerIDs;
    }

    /**
     * This method reads all lines from accounts.csv using BufferedReader
     * and uses method createAccountID() to make list of accountIDs which we can use in program.
     * It is mainly used when we are generating new account and want to prevent situation
     * when different accounts have the same accountID.
     *
     * @return list of accountIDs read from file
     */
    public ArrayList<Integer> readAccountIDsFromFile() {
        ArrayList<Integer> accountIDs = new ArrayList<>();
        BufferedReader br;
        String line;
        try {
            br = new BufferedReader(new FileReader("accounts.csv"));

            while ((line = br.readLine()) != null) {
                String[] data = line.split(", ");

                int accountID = createAccountID(data);

                accountIDs.add(accountID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accountIDs;
    }

    /**
     * This method creates new Integer accountID which we add to list of
     * accountIDs in readAccountIDsFromFile() method.
     *
     * @param data      tab of data which is read from file
     * @return          new accountID which is later added to list of accountIDs
     */
    public int createAccountID(String[] data) {
        return Integer.parseInt(data[1]);
    }


    /**
     * This method creates new Integer customerID which we add to list of
     * customerIDs in readCustomerIDsFromFile() method.
     *
     * @param data      tab of data which is read from file
     * @return          new customerID which is later added to list of customerIDs
     */
    public int createCustomerID(String[] data) {
        return Integer.parseInt(data[0]);
    }

    /**
     * This is key method which saves data to customers.csv file.
     * We are creating a string with all customer fields which is
     * written to the file by bw.write(sbf)
     *
     * @param customers         list of customers which we pass to method after change in data
     * @throws IOException
     */
    public void writeUsersToCSV(ArrayList<Customer> customers) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("customers.csv"), StandardCharsets.UTF_8));
        for (Customer customer : customers) {
            String sbf = customer.getCustomerID() +
                    SEPARATOR +
                    customer.getName() +
                    SEPARATOR +
                    customer.getSurname() +
                    SEPARATOR +
                    customer.getSex() +
                    SEPARATOR +
                    customer.getPhoneNumber() +
                    SEPARATOR +
                    customer.getPassword() +
                    SEPARATOR +
                    customer.getRole();
            bw.write(sbf);
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }

    /**
     * This is key method which saves data to accounts.csv file.
     * We are creating a string with all account fields which is
     * written to the file by bw.write(sbf)
     *
     * @param accounts         list of accounts which we pass to method after change in data
     * @throws IOException
     */
    public void writeAccountsToCSV(ArrayList<Account> accounts) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("accounts.csv"), StandardCharsets.UTF_8));
        for (Account account : accounts) {
            String sbf = account.getCustomerID() +
                    SEPARATOR +
                    account.getAccountID() +
                    SEPARATOR +
                    account.getBalance() +
                    SEPARATOR +
                    account.getPIN();
            bw.write(sbf);
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }


    /**
     * This method is responsible for creating new customer by admin.
     * We enter all fields of customer and check if they are valid.
     * When we finally create new user we can write it to file.
     * Additionally if role of new user is customer we create new account for him.
     *
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void newCustomer() throws IOException, NoSuchAlgorithmException {
        PersonalData pd = new PersonalData();
        int ID = getRandomNumberInRange(100000, 999999, readCustomerIDsFromFile());
        System.out.println("Enter name:");
        String name = in.next();
        System.out.println("Enter surname:");
        String surname = in.next();
        System.out.println("Enter sex:");
        String sex = in.next();
        System.out.println("Enter phone number:");
        long phoneNumber = in.nextLong();
        if (pd.isValidPhoneNumber(phoneNumber)) {
            while (true) {
                System.out.println("Enter password:");
                System.out.println("1) Password must be between 8 and 30 characters.");
                System.out.println("2) Password must contain at least one uppercase, or capital, letter (ex: A, B, etc.)");
                System.out.println("3) Password must contain at least one lowercase letter.");
                System.out.println("4) Password must contain at least one number digit (ex: 0, 1, 2, 3, etc.)");
                String password = in.next();
                if (isValidPassword(password)) {
                    String hashPassword = hash(password);
                    System.out.println("Enter role: (customer/admin)");
                    String role = in.next();
                    customers.add(new Customer(ID, name, surname, sex, phoneNumber, hashPassword, role));
                    System.out.println("ID of " + name + " " + surname + " is: " + ID + "\n");
                    writeUsersToCSV(customers);
                    if (role.equals("customer")) {
                        newAccount(ID);
                    }
                    break;
                } else {
                    System.out.println("This is not a valid password!");
                }
            }
        } else System.out.println("Invalid phone number!");
    }


    /**
     * This method is responsible for creating new account by admin.
     * We enter all fields of account and check if they are valid.
     * When we finally create new account we can write it to file.
     *
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void newAccount(int customerID) throws IOException, NoSuchAlgorithmException {
        customers = readCustomersFromFile();
        customerIDs = readCustomerIDsFromFile();
        accounts = readAccountsFromFile();
        accountIDs = readAccountIDsFromFile();

        if (getCustomerIDs().contains(customerID)) {
            int accountID = getRandomNumberInRange(10000, 99999, readAccountIDsFromFile());
            BigDecimal balance = BigDecimal.valueOf(0);
            String PIN = Short.toString(getRandomPINInRange());
            String hashPIN = hash(PIN);
            accounts.add(new Account(customerID, accountID, balance, hashPIN));
            System.out.println("Created account which belongs to customer no. " + customerID + ". Account ID: " + accountID + ", PIN number: " + PIN);
            writeAccountsToCSV(accounts);
        } else {
            System.out.println("There is no customer with given ID!");
        }

    }

    /**
     * This method is used to change password and it is only available for admin.
     * We check if password is valid and prevent the situation when
     * admin want to change password of another admin.
     * If nothing went wrong, program is overwriting password to the file
     *
     * @param customerID        ID of customer we want change the password for
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public void changePasswordByAdmin(int customerID) throws NoSuchAlgorithmException, IOException {
        while (true) {
            customers = readCustomersFromFile();
            if (getRoleFromGivenCustomerID(customerID).equals("customer")) {
                System.out.println("Enter the new password:");
                System.out.println("1) Password must be between 8 and 30 characters.");
                System.out.println("2) Password must contain at least one uppercase, or capital, letter (ex: A, B, etc.)");
                System.out.println("3) Password must contain at least one lowercase letter.");
                System.out.println("4) Password must contain at least one number digit (ex: 0, 1, 2, 3, etc.)");
                String newPassword = in.next();
                if (isValidPassword(newPassword)) {
                    Customer customer = getCustomerFromGivenCustomerID(customerID);
                    customer.setPassword(hash(newPassword));
                    writeUsersToCSV(customers);
                    break;
                } else {
                    System.out.println("This is not a valid password!");
                }
            } else {
                System.out.println("You cannot change the password of admin!");
                break;
            }
        }
    }

    /**
     * This method is used to change PIN and it is only available for admin.
     * We check if PIN is valid and if it is program overwrite it to the file
     *
     * @param accountID         ID of customer we want change the password for
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public void changePINByAdmin(int accountID) throws NoSuchAlgorithmException, IOException {
        while (true) {
            accounts = readAccountsFromFile();
            System.out.println("Enter the new PIN:");
            System.out.println("1) PIN must be an 4-digit number in range 1000-9999");
            System.out.println("2) PIN cannot contain any letter or special character");
            String PIN = in.next();
            if (isValidPIN(PIN)) {
                Account account = getAccountFromGivenAccountID(accountID);
                account.setPIN(hash(PIN));
                writeAccountsToCSV(accounts);
                break;
            } else {
                System.out.println("This is not a valid PIN!");
            }
        }
    }

    /**
     * This method is used to change PIN and it is only available for customer.
     * Customer have to write his old PIN, then confirm it and if nothing goes wrong
     * he can write the new one with bunch of rules. Finally if PIN is valid
     * program overwrites it to the file.
     *
     * @param accountID         accountID of account we want to change PIN for
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public void changePINByCustomer(int accountID) throws NoSuchAlgorithmException, IOException {
        outerloop:
        while (true) {
            accounts = readAccountsFromFile();
            System.out.println("Enter the old PIN:");
            String oldPIN = in.next();
            if (hash(oldPIN).equals(getPINFromGivenAccountID(accountID))) {
                while (true) {
                    System.out.println("Enter the new PIN:");
                    System.out.println("1) PIN must be an 4-digit number in range 1000-9999");
                    System.out.println("2) PIN cannot contain any letter or special character");
                    String newPIN = in.next();
                    if (isValidPIN(newPIN)) {
                        while (true) {
                            System.out.println("Confirm PIN:");
                            String confirmPIN = in.next();
                            if (newPIN.equals(confirmPIN)) {
                                Account account = getAccountFromGivenAccountID(accountID);
                                account.setPIN(hash(newPIN));
                                writeAccountsToCSV(accounts);
                                System.out.println("\nPIN number changed successfully");
                                break outerloop;
                            } else {
                                System.out.println("PIN numbers does not match!");
                            }
                        }
                    } else {
                        System.out.println("This is not a valid PIN!");
                    }
                }

            } else {
                System.out.println("This is not your old PIN!");
            }
        }
    }

    /**
     * This method is used to change password and it is only available for customer.
     * Customer have to write his old password, then confirm it and if nothing goes wrong
     * he can write the new one with bunch of rules. Finally if password is valid
     * program overwrites it to the file.
     *
     * @param customerID         customerID of account we want to change PIN for
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public void changePasswordByCustomer(int customerID) throws NoSuchAlgorithmException, IOException {
        outerloop:
        while (true) {
            customers = readCustomersFromFile();
            System.out.println("Enter the old password:");
            String oldPassword = in.next();
            if (hash(oldPassword).equals(getPasswordFromGivenCustomerID(customerID))) {
                while (true) {
                    System.out.println("Enter the new password:");
                    System.out.println("1) Password must be between 8 and 30 characters.");
                    System.out.println("2) Password must contain at least one uppercase, or capital, letter (ex: A, B, etc.)");
                    System.out.println("3) Password must contain at least one lowercase letter.");
                    System.out.println("4) Password must contain at least one number digit (ex: 0, 1, 2, 3, etc.)");
                    String newPassword = in.next();
                    if (isValidPassword(newPassword)) {
                        while (true) {
                            System.out.println("Confirm password:");
                            String confirmPassword = in.next();
                            if (newPassword.equals(confirmPassword)) {
                                Customer customer = getCustomerFromGivenCustomerID(customerID);
                                customer.setPassword(hash(newPassword));
                                writeUsersToCSV(customers);
                                System.out.println("\nPassword changed successfully");
                                break outerloop;
                            } else {
                                System.out.println("Passwords does not match!");
                            }
                        }
                    } else {
                        System.out.println("This is not a valid password!");
                    }
                }

            } else {
                System.out.println("This is not your old password!");
            }
        }
    }

    /**
     * This method takes tab of data as an input and create new object of customer.
     *
     * @param data      tab which contains fields of customer
     * @return          new customer we want to create
     */
    public Customer createCustomer(String[] data) {
        int customerID = Integer.parseInt(data[0]);
        String name = data[1];
        String surname = data[2];
        String sex = data[3];
        long phoneNumber = Long.parseLong(data[4]);
        String password = data[5];
        String role = data[6];
        return new Customer(customerID, name, surname, sex, phoneNumber, password, role);
    }

    /**
     * This method takes tab of data as an input and create new object of account.
     *
     * @param data      tab which contains fields of account
     * @return          new account we want to create
     */
    public Account createAccount(String[] data) {
        int customerID = Integer.parseInt(data[0]);
        int accountID = Integer.parseInt(data[1]);
        BigDecimal balance = new BigDecimal(data[2]);
        String PIN = data[3];

        return new Account(customerID, accountID, balance, PIN);
    }

    /**
     * This method create number in range (min, max) and checks if it is an element of
     * given list. If it is we generate the number once again and if it is not we can pass it to the other method.
     *
     * @param min           starting number
     * @param max           ending number
     * @param arrayList     list we check if generated number is in it or not
     * @return
     */
    private int getRandomNumberInRange(int min, int max, ArrayList<Integer> arrayList) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        int randID = r.nextInt((max - min) + 1) + min;
        for (int i = 0; i < arrayList.size(); i++) {
            if (randID == arrayList.get(i)) {
                i = 0;
                randID = r.nextInt((max - min) + 1) + min;
            }
        }
        return randID;
    }

    /**
     * This method generates new PIN in range (1000, 9999) but comparing to the previous method
     * it does not check if there is a PIN like that in file because different account can have
     * the same PIN.
     *
     * @return          generated PIN number
     */
    private short getRandomPINInRange() {
        Random r = new Random();
        return (short) (r.nextInt((9999 - 1000) + 1) + 1000);
    }

    /**
     * This method makes hash of given password/PIN.
     * Used hash function is SHA-256.
     *
     * @param password      password/PIN we want to hash
     * @return              hashed password/PIN
     * @throws NoSuchAlgorithmException
     */
    public String hash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(Integer.toHexString(0xFF & b));
        }
        return hexString.toString();
    }

    /**
     * This method is used for checking if an entered pin by the user id 4 numbers long and if the first number isnt zero
     * If those conditions cannot be met, the function returns false
     * @param PIN       PIN that we are checking for the criteria set above
     * @return
     */
    public boolean isValidPIN(String PIN) {
        if (PIN.length() == 4) {
            if (PIN.charAt(0) != '0') {
                try {
                    int i = Integer.parseInt(PIN);
                } catch (NumberFormatException nfe) {
                    return false;
                }
            } else return false;
        } else return false;
        return true;
    }

    /**
     * This method is used for checking if an entered password is at least 8 to 20 characters long, if it has at least one lower case letter,
     * if it has at least one upper case letter and if it has at least 1 number
     * If those conditions cannot be met, the function returns false
     * @param password      password that we are checking for the criteria set above
     * @return
     */
    public boolean isValidPassword(String password) {
        if (password.length() >= 8 && password.length() <= 20) {
            int upper = 0, lower = 0, number = 0;
            for (int i = 0; i < password.length(); i++) {
                char ch = password.charAt(i);
                if (ch >= 'A' && ch <= 'Z')
                    upper++;
                else if (ch >= 'a' && ch <= 'z')
                    lower++;
                else if (ch >= '0' && ch <= '9')
                    number++;
            }
            return upper != 0 && lower != 0 && number != 0;
        } else return false;
    }

}


