public class Customer extends PersonalData {

    private int customerID;
    private String password;
    private String role;

    @Override
    public String toString() {
        return "Customer{" +
                "customerID=" + customerID +
                ", name=" + super.getName() +
                ", surname=" + super.getSurname() +
                ", sex=" + super.getSex() +
                ", phoneNumber=" + super.getPhoneNumber() +
                ", password=" + password +
                ", role=" + role +
                '}';
    }

    public Customer(int customerID, String name, String surname, String sex, long phoneNumber, String password, String role) {
        super(name, surname, sex, phoneNumber);
        this.customerID = customerID;
        this.password = password;
        this.role = role;
    }

    public Customer() {
        super();
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

}
