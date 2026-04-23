package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private List<String> vehicleIds;
    private String registrationDate;

    public Customer(String customerId, String firstName, String lastName,
                    String email, String phoneNumber, String address) {
        this.customerId = customerId;

        // Constructor içinde direkt atama yapmak yerine setter'ları çağırıyoruz ki
        // nesne oluşturulurken de kurallarımız (validation) çalışsın.
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPhoneNumber(phoneNumber); // Telefon numarası kontrolü eklendi

        this.address = address;
        this.vehicleIds = new ArrayList<>();
        this.registrationDate = new java.text.SimpleDateFormat("yyyy-MM-dd")
                .format(new java.util.Date());
    }

    public String getCustomerId() { return customerId; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) {
        // Sadece harfler (Türkçe karakterler dahil) ve boşluk, 2 ile 50 karakter arası sınır.
        if (firstName == null || !firstName.matches("^[a-zA-ZğüşıöçĞÜŞİÖÇ ]{2,50}$")) {
            throw new IllegalArgumentException("Ad sadece harflerden oluşmalı ve 2-50 karakter arasında olmalıdır!");
        }
        this.firstName = firstName;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) {
        // Sadece harfler (Türkçe karakterler dahil) ve boşluk, 2 ile 50 karakter arası sınır.
        if (lastName == null || !lastName.matches("^[a-zA-ZğüşıöçĞÜŞİÖÇ ]{2,50}$")) {
            throw new IllegalArgumentException("Soyad sadece harflerden oluşmalı ve 2-50 karakter arasında olmalıdır!");
        }
        this.lastName = lastName;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        // @ işareti kontrolü
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Lütfen geçerli bir email adresi giriniz (@ işareti zorunludur)!");
        }
        this.email = email;
    }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) {
        // Tam olarak 10 karakter ve sadece rakam kontrolü
        if (phoneNumber == null || !phoneNumber.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Telefon numarası tam olarak 10 haneli olmalı ve sadece rakamlardan oluşmalıdır!");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<String> getVehicleIds() { return vehicleIds; }
    public void addVehicleId(String vehicleId) { this.vehicleIds.add(vehicleId); }

    public String getRegistrationDate() { return registrationDate; }

    @Override
    public String toString() {
        return "Customer{" + "customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' + ", vehicleCount=" + vehicleIds.size() + '}';
    }
}