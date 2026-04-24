package ui.admin;

import models.Customer;
import models.Invoice;
import services.LanguageManager;
import services.AppointmentService;
import services.CustomerService;
import services.InvoiceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AdminPanelUI extends JPanel {

    private JTabbedPane tabbedPane;
    private DefaultTableModel customersTableModel, vehiclesTableModel, servicesTableModel, invoicesTableModel;
    private JTable customersTable, vehiclesTable, servicesTable, invoicesTable;
    private JTextArea reportArea;
    private Map<String, Customer> customers;

    private JLabel titleLabel;
    private JButton deleteButtonCustomers, refreshButtonCustomers;
    private JButton deleteButtonVehicles, refreshButtonVehicles;
    private JButton deleteButtonServices, refreshButtonServices, completeAndInvoiceButton;

    private JPanel customersButtonPanel, vehiclesButtonPanel, servicesButtonPanel, invoicesButtonPanel;

    public AdminPanelUI() {
        customers = new HashMap<>();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(titleLabel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));

        tabbedPane.addTab("", createCustomersPanel());
        tabbedPane.addTab("", createVehiclesPanel());
        tabbedPane.addTab("", createServicesPanel());
        tabbedPane.addTab("", createInvoicesPanel());
        tabbedPane.addTab("", createReportsPanel());

        tabbedPane.addChangeListener(e -> refreshAllTabs());

        add(tabbedPane, BorderLayout.CENTER);

        updateTexts();
    }

    public void refreshAllTabs() {
        refreshCustomersTable();
        refreshVehiclesTable();
        refreshServicesTable();
        refreshInvoicesTable();
        refreshReports();
    }

    public void updateTexts() {
        titleLabel.setText(LanguageManager.getString("admin.title"));
        tabbedPane.setTitleAt(0, "  " + LanguageManager.getString("admin.customers") + "  ");
        tabbedPane.setTitleAt(1, "  " + LanguageManager.getString("admin.vehicles") + "  ");
        tabbedPane.setTitleAt(2, "  " + LanguageManager.getString("admin.services") + "  ");
        tabbedPane.setTitleAt(3, "  Faturalar  ");
        tabbedPane.setTitleAt(4, "  " + LanguageManager.getString("admin.reports") + "  ");

        if (deleteButtonCustomers != null) deleteButtonCustomers.setText(LanguageManager.getString("admin.delete"));
        if (refreshButtonCustomers != null) refreshButtonCustomers.setText(LanguageManager.getString("button.refresh"));
        if (deleteButtonVehicles != null) deleteButtonVehicles.setText(LanguageManager.getString("admin.delete"));
        if (refreshButtonVehicles != null) refreshButtonVehicles.setText(LanguageManager.getString("button.refresh"));
        if (deleteButtonServices != null) deleteButtonServices.setText(LanguageManager.getString("admin.delete"));
        if (refreshButtonServices != null) refreshButtonServices.setText(LanguageManager.getString("button.refresh"));

        if (customersTableModel != null) {
            customersTableModel.setColumnIdentifiers(new String[]{
                    LanguageManager.getString("table.customerId"), LanguageManager.getString("table.name"),
                    LanguageManager.getString("table.surname"), LanguageManager.getString("table.email"),
                    LanguageManager.getString("table.phone"), LanguageManager.getString("table.address")
            });
        }

        if (vehiclesTableModel != null) {
            vehiclesTableModel.setColumnIdentifiers(new String[]{
                    LanguageManager.getString("table.plate"), LanguageManager.getString("table.make"),
                    LanguageManager.getString("table.model"), LanguageManager.getString("table.year"),
                    LanguageManager.getString("table.owner")
            });
        }

        if (servicesTableModel != null) {
            servicesTableModel.setColumnIdentifiers(new String[]{
                    LanguageManager.getString("table.appId"), LanguageManager.getString("table.name"),
                    LanguageManager.getString("table.plate"), LanguageManager.getString("table.date"),
                    LanguageManager.getString("table.serviceType"), LanguageManager.getString("table.status")
            });
        }

        if (invoicesTableModel != null) {
            invoicesTableModel.setColumnIdentifiers(new String[]{
                    "Fatura ID", "Randevu ID", "Tarih", "Son Ödeme", "Toplam (TL)", "Durum"
            });
        }
    }

    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        customersButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        deleteButtonCustomers = new JButton();
        deleteButtonCustomers.addActionListener(e -> deleteCustomer()); // Buton bağlandı
        customersButtonPanel.add(deleteButtonCustomers);

        refreshButtonCustomers = new JButton();
        refreshButtonCustomers.addActionListener(e -> refreshCustomersTable());
        customersButtonPanel.add(refreshButtonCustomers);

        panel.add(customersButtonPanel, BorderLayout.NORTH);

        String[] columns = {"Müşteri ID", "Ad", "Soyad", "Email", "Telefon", "Adres"};
        customersTableModel = new DefaultTableModel(columns, 0);
        customersTable = new JTable(customersTableModel);
        customersTable.setRowHeight(25);
        customersTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(new JScrollPane(customersTable), BorderLayout.CENTER);

        refreshCustomersTable();
        return panel;
    }

    private JPanel createVehiclesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        vehiclesButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        deleteButtonVehicles = new JButton();
        deleteButtonVehicles.addActionListener(e -> deleteVehicle()); // Buton bağlandı
        vehiclesButtonPanel.add(deleteButtonVehicles);

        refreshButtonVehicles = new JButton();
        refreshButtonVehicles.addActionListener(e -> refreshVehiclesTable());
        vehiclesButtonPanel.add(refreshButtonVehicles);

        panel.add(vehiclesButtonPanel, BorderLayout.NORTH);

        String[] columns = {"Plaka", "Marka", "Model", "Yıl", "Sahibi"};
        vehiclesTableModel = new DefaultTableModel(columns, 0);
        vehiclesTable = new JTable(vehiclesTableModel);
        vehiclesTable.setRowHeight(25);
        vehiclesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(new JScrollPane(vehiclesTable), BorderLayout.CENTER);

        refreshVehiclesTable();
        return panel;
    }

    private JPanel createServicesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        servicesButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        deleteButtonServices = new JButton();
        deleteButtonServices.addActionListener(e -> deleteService()); // Buton bağlandı
        servicesButtonPanel.add(deleteButtonServices);

        refreshButtonServices = new JButton();
        refreshButtonServices.addActionListener(e -> refreshServicesTable());
        servicesButtonPanel.add(refreshButtonServices);

        completeAndInvoiceButton = new JButton("✓ İşlemi Tamamla ve Fatura Kes");
        completeAndInvoiceButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        completeAndInvoiceButton.setBackground(new Color(0, 153, 0));
        completeAndInvoiceButton.setForeground(Color.WHITE);
        completeAndInvoiceButton.addActionListener(e -> processSelectedAppointment());
        servicesButtonPanel.add(completeAndInvoiceButton);

        panel.add(servicesButtonPanel, BorderLayout.NORTH);

        String[] columns = {"Randevu ID", "Müşteri Adı", "Plaka", "Tarih", "Servis Tipi", "Durum"};
        servicesTableModel = new DefaultTableModel(columns, 0);
        servicesTable = new JTable(servicesTableModel);
        servicesTable.setRowHeight(25);
        servicesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(new JScrollPane(servicesTable), BorderLayout.CENTER);

        refreshServicesTable();
        return panel;
    }

    private JPanel createInvoicesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        invoicesButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshInvoicesBtn = new JButton(LanguageManager.getString("button.refresh") != null ? LanguageManager.getString("button.refresh") : "Yenile");
        refreshInvoicesBtn.addActionListener(e -> refreshInvoicesTable());
        invoicesButtonPanel.add(refreshInvoicesBtn);

        panel.add(invoicesButtonPanel, BorderLayout.NORTH);

        String[] columns = {"Fatura ID", "Randevu ID", "Tarih", "Son Ödeme", "Toplam (TL)", "Durum"};
        invoicesTableModel = new DefaultTableModel(columns, 0);
        invoicesTable = new JTable(invoicesTableModel);
        invoicesTable.setRowHeight(25);
        invoicesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(new JScrollPane(invoicesTable), BorderLayout.CENTER);

        refreshInvoicesTable();
        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        reportArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        refreshReports();
        return panel;
    }

    private void refreshCustomersTable() {
        customersTableModel.setRowCount(0);
        for (models.Customer customer : CustomerService.getAllCustomers().values()) {
            customersTableModel.addRow(new Object[]{
                    customer.getCustomerId(), customer.getFirstName(), customer.getLastName(),
                    customer.getEmail(), customer.getPhoneNumber(), customer.getAddress()
            });
        }
    }

    public void refreshVehiclesTable() {
        vehiclesTableModel.setRowCount(0);
        Set<String> addedPlates = new HashSet<>();
        for (Object[] row : AppointmentService.getAllAppointments()) {
            String plate = (String) row[8];
            if (!addedPlates.contains(plate)) {
                vehiclesTableModel.addRow(new Object[]{ plate, row[5], row[6], row[7], row[1] + " " + row[2] });
                addedPlates.add(plate);
            }
        }
    }

    public void refreshServicesTable() {
        servicesTableModel.setRowCount(0);
        for (Object[] row : AppointmentService.getAllAppointments()) {
            servicesTableModel.addRow(new Object[]{ row[0], row[1] + " " + row[2], row[8], row[9], row[10], row[11] });
        }
    }

    public void refreshInvoicesTable() {
        if (invoicesTableModel == null) return;
        invoicesTableModel.setRowCount(0);
        for (Invoice inv : InvoiceService.getAllInvoices()) {
            invoicesTableModel.addRow(new Object[]{
                    inv.getInvoiceId(), inv.getServiceId(), inv.getInvoiceDate(),
                    inv.getDueDate(), String.format("%.2f", inv.getTotalAmount()), inv.getPaymentStatus()
            });
        }
    }

    private void refreshReports() {
        if (reportArea != null) {
            int totalAppointments = AppointmentService.getAllAppointments().size();
            int totalCustomers = CustomerService.getAllCustomers().size();
            int totalInvoices = InvoiceService.getAllInvoices().size();

            double totalRevenue = 0;
            for(Invoice inv : InvoiceService.getAllInvoices()) {
                totalRevenue += inv.getTotalAmount();
            }

            String reportText = "=== SİSTEM İSTATİSTİKLERİ ===\n\n" +
                    "Toplanan Veriler:\n" +
                    "-----------------------------------\n" +
                    "> Kayıtlı Toplam Müşteri : " + totalCustomers + "\n" +
                    "> Açılan Toplam Randevu  : " + totalAppointments + "\n" +
                    "> Kesilen Fatura Sayısı  : " + totalInvoices + "\n" +
                    "> Beklenen Toplam Ciro   : " + String.format("%.2f TL", totalRevenue) + "\n" +
                    "-----------------------------------\n" +
                    "\nSistem Durumu: Aktif ve Senkronize\n" +
                    "Son Güncelleme: " + new java.util.Date().toString();

            reportArea.setText(reportText);
        }
    }

    // YENİ: MÜŞTERİ SİLME MANTIĞI
    private void deleteCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen silinecek müşteriyi seçin!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bu müşteriyi sistemden tamamen silmek istediğinize emin misiniz?", "Silme Onayı", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String customerId = (String) customersTableModel.getValueAt(selectedRow, 0);

            // Backend'den (CustomerService) Sil
            CustomerService.deleteCustomer(customerId);

            // Tüm sekmeleri güncelle
            refreshAllTabs();
            JOptionPane.showMessageDialog(this, "Müşteri başarıyla silindi.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // YENİ: RANDEVU SİLME VE KAPASİTE BOŞALTMA MANTIĞI
    private void deleteService() {
        int selectedRow = servicesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen silinecek randevuyu seçin!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bu randevuyu iptal etmek/silmek istediğinize emin misiniz?\n(İlgili günün kapasitesi açılacaktır)", "Silme Onayı", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String appId = (String) servicesTableModel.getValueAt(selectedRow, 0);

            // Backend'den (AppointmentService) Sil ve o günkü kontenjanı 1 azalt
            AppointmentService.deleteAppointment(appId);

            // Tüm sekmeleri güncelle
            refreshAllTabs();
            JOptionPane.showMessageDialog(this, "Randevu başarıyla silindi ve kapasite güncellendi.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // YENİ: ARAÇ SİLME MANTIĞI (Araçlar randevuya bağlı olduğu için uyarı verir)
    private void deleteVehicle() {
        JOptionPane.showMessageDialog(this, "Araç kayıtları doğrudan randevulara bağlıdır.\nBir aracı silmek için lütfen 'Servisler' sekmesinden ilgili randevuyu siliniz.", "Erişim Reddedildi", JOptionPane.INFORMATION_MESSAGE);
    }

    private void processSelectedAppointment() {
        int selectedRow = servicesTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen işlem yapılacak randevuyu seçin!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String currentStatus = (String) servicesTableModel.getValueAt(selectedRow, 5);
        if ("Tamamlandı".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this, "Bu işlem zaten tamamlanmış ve faturası kesilmiş!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String appointmentId = (String) servicesTableModel.getValueAt(selectedRow, 0);
            String customerName = (String) servicesTableModel.getValueAt(selectedRow, 1);
            String plate = (String) servicesTableModel.getValueAt(selectedRow, 2);
            String serviceType = (String) servicesTableModel.getValueAt(selectedRow, 4);

            String newInvoiceId = "INV-" + System.currentTimeMillis();
            String customerId = "CUST-" + customerName.replace(" ", "");
            String vehicleId = "VEH-" + plate;

            Invoice invoice = new Invoice(newInvoiceId, customerId, vehicleId, appointmentId, 15);
            invoice.addCalculatedServiceItem(serviceType, 3);
            InvoiceService.addInvoice(invoice);

            List<Object[]> allApps = AppointmentService.getAllAppointments();
            for (Object[] app : allApps) {
                if (app[0].equals(appointmentId)) {
                    app[11] = "Tamamlandı";
                    break;
                }
            }

            refreshAllTabs();
            JOptionPane.showMessageDialog(this,
                    "İşlem başarıyla tamamlandı!\n\n" +
                            "Oluşturulan Fatura ID: " + newInvoiceId + "\n" +
                            "Hesaplanan Tutar: " + String.format("%.2f", invoice.getTotalAmount()) + " TL\n" +
                            "(%20 KDV Dahildir)",
                    "Fatura Kesildi",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fatura oluşturulurken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}