package models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Service implements Serializable {
    public enum ServiceStatus { PENDING, IN_PROGRESS, COMPLETED, ON_HOLD }

    private String serviceId;
    private String vehicleId;
    private String customerId;
    private String serviceType;
    private String description;
    private String startDate; // Arayüzdeki "Randevu Tarihi"ni temsil ediyor
    private String completionDate;
    private ServiceStatus status;
    private double estimatedCost;
    private double actualCost;
    private List<String> partsUsed;
    private String technician;
    private int maintenanceReminderDays;

    // Constructor'a 'startDate' eklendi çünkü arayüzden seçilecek
    public Service(String serviceId, String vehicleId, String customerId,
                   String serviceType, String startDate, String description, int maintenanceReminderDays) {
        this.serviceId = serviceId;
        this.vehicleId = vehicleId;
        this.customerId = customerId;

        // Validation kurallarının işlemesi için setter'ları çağırıyoruz
        setServiceType(serviceType);
        setStartDate(startDate);

        this.description = description;
        this.status = ServiceStatus.PENDING;
        this.partsUsed = new ArrayList<>();
        this.maintenanceReminderDays = maintenanceReminderDays;
    }

    public String getServiceId() { return serviceId; }
    public String getVehicleId() { return vehicleId; }
    public String getCustomerId() { return customerId; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) {
        // Servis tipi boş bırakılamaz
        if (serviceType == null || serviceType.trim().isEmpty()) {
            throw new IllegalArgumentException("Servis tipi boş bırakılamaz!");
        }
        this.serviceType = serviceType;
    }

    public String getDescription() { return description; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) {
        // Tarih boş bırakılamaz
        if (startDate == null || startDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Randevu tarihi boş bırakılamaz!");
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date selectedDate = sdf.parse(startDate);

            // Bugünü saat farkını (00:00:00) sıfırlayarak alıyoruz ki adil bir karşılaştırma olsun
            Date today = sdf.parse(sdf.format(new Date()));

            // Seçilen tarih bugünden önceyse hata ver
            if (selectedDate.before(today)) {
                throw new IllegalArgumentException("Geçmiş bir gün için randevu oluşturulamaz!");
            }

            this.startDate = startDate;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Geçersiz tarih formatı! Beklenen format: YYYY-AA-GG");
        }
    }

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
                " | Status: " + status + " | Date: " + startDate;
    }
}