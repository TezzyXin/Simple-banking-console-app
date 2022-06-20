public class PersonalData {

    private String name;
    private String surname;
    private String sex;
    private long phoneNumber;

    public boolean isValidPhoneNumber(long phoneNumber) {
        return phoneNumber > 100000000 && phoneNumber < 999999999;
    }

    public PersonalData(String name, String surname, String sex, long phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
    }

    public PersonalData() {
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getSex() {
        return sex;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

}
