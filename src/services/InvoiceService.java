package services;

import models.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoiceService {

    // Kesilen tüm faturaları hafızada tutacağımız liste
    private static List<Invoice> allInvoices = new ArrayList<>();

    // 1. CREATE: Yeni fatura ekleme
    public static void addInvoice(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Boş fatura sisteme eklenemez!");
        }
        allInvoices.add(invoice);
        System.out.println("Sisteme yeni fatura eklendi: " + invoice.getInvoiceId());
    }

    // 2. READ: Tüm faturaları listeleme (Admin paneli tablosu için gerekecek)
    public static List<Invoice> getAllInvoices() {
        return allInvoices;
    }

    // 3. READ: Spesifik bir faturayı ID'sine göre bulma
    public static Invoice getInvoiceById(String invoiceId) {
        for (Invoice inv : allInvoices) {
            if (inv.getInvoiceId().equals(invoiceId)) {
                return inv;
            }
        }
        return null; // Bulunamazsa null döner
    }

    // 4. READ: Belirli bir müşteriye ait faturaları getirme
    public static List<Invoice> getInvoicesByCustomerId(String customerId) {
        List<Invoice> customerInvoices = new ArrayList<>();
        for (Invoice inv : allInvoices) {
            if (inv.getCustomerId().equals(customerId)) {
                customerInvoices.add(inv);
            }
        }
        return customerInvoices;
    }

    // 5. UPDATE: Faturayı "Ödendi" olarak işaretleme
    public static boolean markAsPaid(String invoiceId) {
        Invoice invoice = getInvoiceById(invoiceId);
        if (invoice != null) {
            invoice.setPaymentStatus(Invoice.PaymentStatus.PAID);
            System.out.println("Fatura ödendi olarak güncellendi: " + invoiceId);
            return true;
        }
        return false;
    }
}