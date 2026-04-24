package models;

import services.ServiceCostCalculator; // HESAPLAYICIYI İÇERİ ALDIK

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Invoice implements Serializable {
    public enum PaymentStatus { UNPAID, PARTIAL, PAID }

    private String invoiceId;
    private String customerId;
    private String vehicleId;
    private String serviceId;
    private String invoiceDate;
    private String dueDate;
    private List<InvoiceLineItem> lineItems;
    private double subtotal;
    private double taxAmount;
    private double totalAmount;
    private PaymentStatus paymentStatus;
    private String notes;

    public static class InvoiceLineItem implements Serializable {
        private String description;
        private double unitPrice;
        private int quantity;
        private double totalPrice;

        public InvoiceLineItem(String description, double unitPrice, int quantity) {
            // Fatura kalemi doğrulamaları
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("Fatura kalemi açıklaması boş bırakılamaz!");
            }
            if (unitPrice < 0) {
                throw new IllegalArgumentException("Birim fiyat sıfırdan küçük olamaz!");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("Miktar sıfırdan büyük olmalıdır!");
            }

            this.description = description;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.totalPrice = unitPrice * quantity;
        }

        public String getDescription() { return description; }
        public double getUnitPrice() { return unitPrice; }
        public int getQuantity() { return quantity; }
        public double getTotalPrice() { return totalPrice; }
    }

    public Invoice(String invoiceId, String customerId, String vehicleId,
                   String serviceId, int dueDays) {

        // Kimlik (ID) Doğrulamaları
        if (invoiceId == null || invoiceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Fatura ID boş olamaz!");
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Müşteri ID boş olamaz!");
        }
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Araç ID boş olamaz!");
        }
        if (serviceId == null || serviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Servis ID boş olamaz!");
        }
        if (dueDays < 0) {
            throw new IllegalArgumentException("Vade günü negatif bir değer olamaz!");
        }

        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.serviceId = serviceId;
        this.invoiceDate = new java.text.SimpleDateFormat("yyyy-MM-dd")
                .format(new java.util.Date());
        this.lineItems = new ArrayList<>();
        this.paymentStatus = PaymentStatus.UNPAID;

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_MONTH, dueDays);
        this.dueDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    // YENİ EKLENEN KÖPRÜ METOD: Hesaplayıcıyı çalıştırıp faturaya satır ekler
    public void addCalculatedServiceItem(String serviceType, int customerServiceCount) {
        if (serviceType == null || serviceType.trim().isEmpty()) {
            throw new IllegalArgumentException("Servis tipi boş olamaz!");
        }

        // 1. Yazdığımız kurtarma bloklu hesaplayıcıdan fiyatı çekiyoruz
        double calculatedCost = ServiceCostCalculator.calculateStandardServiceCost(serviceType, customerServiceCount);

        // 2. Bunu faturaya yeni bir kalem (işlem satırı) olarak ekliyoruz
        String description = serviceType + " (İşçilik ve Parça Dahil)";
        InvoiceLineItem item = new InvoiceLineItem(description, calculatedCost, 1);

        this.addLineItem(item);
    }

    public String getInvoiceId() { return invoiceId; }
    public String getCustomerId() { return customerId; }
    public String getVehicleId() { return vehicleId; }
    public String getServiceId() { return serviceId; }
    public String getInvoiceDate() { return invoiceDate; }
    public String getDueDate() { return dueDate; }
    public List<InvoiceLineItem> getLineItems() { return lineItems; }

    public void addLineItem(InvoiceLineItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Eklenecek fatura kalemi boş (null) olamaz!");
        }
        this.lineItems.add(item);
        updateTotals();
    }

    public double getSubtotal() { return subtotal; }
    public double getTaxAmount() { return taxAmount; }
    public double getTotalAmount() { return totalAmount; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        if (paymentStatus == null) {
            throw new IllegalArgumentException("Ödeme durumu boş bırakılamaz!");
        }
        this.paymentStatus = paymentStatus;
    }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    private void updateTotals() {
        this.subtotal = lineItems.stream().mapToDouble(InvoiceLineItem::getTotalPrice).sum();

        // HOCA BURAYI GÖRECEK: N-Version Programming ile zırhlı hesaplama
        this.taxAmount = services.TaxCalculatorNVP.getVerifiedTax(this.subtotal);

        this.totalAmount = this.subtotal + this.taxAmount;
    }

    @Override
    public String toString() {
        return "Fatura ID: " + invoiceId + " | Toplam: " + String.format("%.2f", totalAmount) +
                " TL | Durum: " + paymentStatus;
    }
}