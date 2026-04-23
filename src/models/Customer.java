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
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.vehicleIds = new ArrayList<>();
        this.registrationDate = new java.text.SimpleDateFormat("yyyy-MM-dd")
                .format(new java.util.Date());
    }

    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
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