package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Service implements Serializable {
    public enum ServiceStatus { PENDING, IN_PROGRESS, COMPLETED, ON_HOLD }

    private String serviceId;
    private String vehicleId;
    private String customerId;
    private String serviceType;
    private String description;
    private String startDate;
    private String completionDate;
    private ServiceStatus status;
    private double estimatedCost;
    private double actualCost;
    private List<String> partsUsed;
    private String technician;
    private int maintenanceReminderDays;

    public Service(String serviceId, String vehicleId, String customerId,
                   String serviceType, String description, int maintenanceReminderDays) {
        this.serviceId = serviceId;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.serviceType = serviceType;
        this.description = description;
        this.startDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date());
        this.status = ServiceStatus.PENDING;
        this.partsUsed = new ArrayList<>();
        this.maintenanceReminderDays = maintenanceReminderDays;
    }

    public String getServiceId() { return serviceId; }
    public String getVehicleId() { return vehicleId; }
    public String getCustomerId() { return customerId; }
    public String getServiceType() { return serviceType; }
    public String getDescription() { return description; }
    public String getStartDate() { return startDate; }
    public String getCompletionDate() { return completionDate; }
    public void setCompletionDate(String completionDate) { this.completionDate = completionDate; }
    public ServiceStatus getStatus() { return status; }
    public void setStatus(ServiceStatus status) { this.status = status; }
    public double getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(double estimatedCost) { this.estimatedCost = estimatedCost; }
    public double getActualCost() { return actualCost; }
    public void setActualCost(double actualCost) { this.actualCost = actualCost; }
    public List<String> getPartsUsed() { return partsUsed; }
    public void addPartUsed(String partId) { this.partsUsed.add(partId); }
    public String getTechnician() { return technician; }
    public void setTechnician(String technician) { this.technician = technician; }
    public int getMaintenanceReminderDays() { return maintenanceReminderDays; }

    @Override
    public String toString() {
        return "Service ID: " + serviceId + " | Type: " + serviceType +
                " | Status: " + status + " | Cost: $" + actualCost;
    }
}