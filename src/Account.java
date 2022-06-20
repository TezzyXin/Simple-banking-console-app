import java.math.BigDecimal;

public class Account {

    private int customerID;
    private int accountID;
    private BigDecimal balance;
    private String PIN;

    @Override
    public String toString() {
        return "Account{" +
                "customerID=" + customerID +
                ", accountID=" + accountID +
                ", balance=" + balance +
                ", PIN=" + PIN +
                '}';
    }

    public String toString(boolean nothing) {
        return "--- " +
                "CustomerID = " + customerID +
                ", accountID = " + accountID +
                ", balance = " + balance;
    }

    public Account(int customerID, int accountID, BigDecimal balance, String PIN) {
        this.customerID = customerID;
        this.accountID = accountID;
        this.balance = balance;
        this.PIN = PIN;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getAccountID() {
        return accountID;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }
}
