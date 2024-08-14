package fit5171.monash.edu;

import java.util.regex.Pattern;

public class Passenger extends Person {
    private String email;
    private String phoneNumber;
    private String cardNumber;
    private int securityCode;
    private String passport;

    static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+61|0[45])\\d{8}$");
    private static final Pattern CARD_PATTERN = Pattern.compile("^\\d{16}$"); // 16-digit card number
    private static final int PASSPORT_MAX_LENGTH = 9;

    public Passenger(String firstName, String secondName, int age, String gender, String email, String phoneNumber, String passport, String cardNumber, int securityCode) {
        super(firstName, secondName, age, gender); // 调用父类的构造函数
        String validationMessage = validateAndSetFields(email, phoneNumber, passport, cardNumber, securityCode);
        if (!validationMessage.equals("Successfully created a passenger")) {
            throw new IllegalArgumentException(validationMessage);
        }
    }

    private String validateAndSetFields(String email, String phoneNumber, String passport, String cardNumber, int securityCode) {
        // Validate email
        if (email == null || email.isEmpty() || !EMAIL_PATTERN.matcher(email).matches()) {
            return "Invalid email provided";
        }

        // Validate phone number
        if (phoneNumber == null || phoneNumber.isEmpty() || !PHONE_PATTERN.matcher(phoneNumber).matches()) {
            return "Invalid phone number provided";
        }

        // Validate passport
        if (passport == null || passport.length() > PASSPORT_MAX_LENGTH) {
            return "Invalid passport number provided";
        }

        // Validate card number
        if (cardNumber == null || !CARD_PATTERN.matcher(cardNumber).matches()) {
            return "Invalid card number provided";
        }

        // Validate security code
        if (securityCode < 100 || securityCode > 9999) {
            return "Invalid security code provided";
        }

        // Set fields if all validations pass
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passport = passport;
        this.cardNumber = cardNumber;
        this.securityCode = securityCode;
        return "Successfully created a passenger";
    }

    public String getEmail() {
        return email;
    }

    public String setEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            return "Invalid email provided";
        }
        this.email = email;
        return "Successfully Set Email";
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !PHONE_PATTERN.matcher(phoneNumber).matches()) {
            return "Invalid phone number provided";
        }
        this.phoneNumber = phoneNumber;
        return "Successfully Set PhoneNumber";
    }

    public String getPassport() {
        return passport;
    }

    public String setPassport(String passport) {
        if (passport == null || passport.length() > PASSPORT_MAX_LENGTH) {
            return "Invalid passport number provided";
        }
        this.passport = passport;
        return "Successfully Set Passport";
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String setCardNumber(String cardNumber) {
        if (cardNumber == null || !CARD_PATTERN.matcher(cardNumber).matches()) {
            return "Invalid card number provided";
        }
        this.cardNumber = cardNumber;
        return "Successfully Set CardNumber";
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public String setSecurityCode(int securityCode) {
        if (securityCode < 100 || securityCode > 9999) {
            return "Invalid security code provided";
        }
        this.securityCode = securityCode;
        return "Successfully Set SecurityCode";
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "Fullname= " + super.getFirstName() + " " + super.getSecondName() +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", passport='" + passport + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", securityCode=" + securityCode +
                ", age=" + getAge() +
                ", gender='" + getGender() + '\'' +
                '}';
    }
}
