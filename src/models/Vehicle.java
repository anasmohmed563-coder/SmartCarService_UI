package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Vehicle implements Serializable {
    private String vehicleId;
    private String customerId;
    private String make;
    private String model;
    private String year;
    private String licensePlate;
    private String vinNumber;
    private int mileage;
    private String registrationDate;
    private List<String> serviceIds;
    private List<String> partIds;

    public Vehicle(String vehicleId, String customerId, String make, String model,
                   String year, String licensePlate, String vinNumber, int mileage) {
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.vinNumber = vinNumber;
        this.mileage = mileage;
        this.serviceIds = new ArrayList<>();
        this.partIds = new ArrayList<>();
        this.registrationDate = new java.text.SimpleDateFormat("yyyy-MM-dd")
                .format(new java.util.Date());
    }

    public String getVehicleId() { return vehicleId; }
    public String getCustomerId() { return customerId; }
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getYear() { return year; }
    public String getLicensePlate() { return licensePlate; }
    public String getVinNumber() { return vinNumber; }
    public int getMileage() { return mileage; }
    public void setMileage(int mileage) { this.mileage = mileage; }
    public List<String> getServiceIds() { return serviceIds; }
    public void addServiceId(String serviceId) { this.serviceIds.add(serviceId); }
    public List<String> getPartIds() { return partIds; }
    public void addPartId(String partId) { this.partIds.add(partId); }

    @Override
    public String toString() {
        return make + " " + model + " (" + year + ") - License: " + licensePlate;
    }
}