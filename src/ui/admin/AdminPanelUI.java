package ui.admin;

import models.Customer;
import services.LanguageManager;
import services.AppointmentService;
import services.CustomerService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class AdminPanelUI extends JPanel {

    private JTabbedPane tabbedPane;
    private DefaultTableModel customersTableModel, vehiclesTableModel, servicesTableModel;
    private JTable customersTable, vehiclesTable, servicesTable;
    private JTextArea reportArea;
    private Map<String, Customer> customers;

    private JLabel titleLabel;
    private JButton deleteButtonCustomers, refreshButtonCustomers;
    private JButton deleteButtonVehicles, refreshButtonVehicles;
    private JButton deleteButtonServices, refreshButtonServices;

    private JPanel customersButtonPanel, vehiclesButtonPanel, servicesButtonPanel;

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
        tabbedPane.addTab("", createReportsPanel());

        tabbedPane.addChangeListener(e -> refreshAllTabs());

        add(tabbedPane, BorderLayout.CENTER);

        updateTexts();
    }

    public void refreshAllTabs() {
        refreshCustomersTable();
        refreshVehiclesTable();
        refreshServicesTable();
        refreshReports();
    }

    public void updateTexts() {
        titleLabel.setText(LanguageManager.getString("admin.title"));
        tabbedPane.setTitleAt(0, "  " + LanguageManager.getString("admin.customers") + "  ");
        tabbedPane.setTitleAt(1, "  " + LanguageManager.getString("admin.vehicles") + "  ");
        tabbedPane.setTitleAt(2, "  " + LanguageManager.getString("admin.services") + "  ");
        tabbedPane.setTitleAt(3, "  " + LanguageManager.getString("admin.reports") + "  ");

        if (deleteButtonCustomers != null) deleteButtonCustomers.setText(LanguageManager.getString("admin.delete"));
        if (refreshButtonCustomers != null) refreshButtonCustomers.setText(LanguageManager.getString("button.refresh"));
        if (deleteButtonVehicles != null) deleteButtonVehicles.setText(LanguageManager.getString("admin.delete"));
        if (refreshButtonVehicles != null) refreshButtonVehicles.setText(LanguageManager.getString("button.refresh"));
        if (deleteButtonServices != null) deleteButtonServices.setText(LanguageManager.getString("admin.delete"));
        if (refreshButtonServices != null) refreshButtonServices.setText(LanguageManager.getString("button.refresh"));

        // TABLO BAŞLIKLARINI GÜNCELLEME KISMI
        if (customersTableModel != null) {
            customersTableModel.setColumnIdentifiers(new String[]{
                    LanguageManager.getString("table.customerId"),
                    LanguageManager.getString("table.name"),
                    LanguageManager.getString("table.surname"),
                    LanguageManager.getString("table.email"),
                    LanguageManager.getString("table.phone"),
                    LanguageManager.getString("table.address")
            });
        }

        if (vehiclesTableModel != null) {
            vehiclesTableModel.setColumnIdentifiers(new String[]{
                    LanguageManager.getString("table.plate"),
                    LanguageManager.getString("table.make"),
                    LanguageManager.getString("table.model"),
                    LanguageManager.getString("table.year"),
                    LanguageManager.getString("table.owner")
            });
        }

        if (servicesTableModel != null) {
            servicesTableModel.setColumnIdentifiers(new String[]{
                    LanguageManager.getString("table.appId"),
                    LanguageManager.getString("table.name"),
                    LanguageManager.getString("table.plate"),
                    LanguageManager.getString("table.date"),
                    LanguageManager.getString("table.serviceType"),
                    LanguageManager.getString("table.status")
            });
        }
    }

    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        customersButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        deleteButtonCustomers = new JButton();
        deleteButtonCustomers.addActionListener(e -> deleteCustomer());
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
        servicesButtonPanel.add(deleteButtonServices);

        refreshButtonServices = new JButton();
        refreshButtonServices.addActionListener(e -> refreshServicesTable());
        servicesButtonPanel.add(refreshButtonServices);

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
                vehiclesTableModel.addRow(new Object[]{
                        plate,
                        row[5],
                        row[6],
                        row[7],
                        row[1] + " " + row[2]
                });
                addedPlates.add(plate);
            }
        }
    }

    public void refreshServicesTable() {
        servicesTableModel.setRowCount(0);
        for (Object[] row : AppointmentService.getAllAppointments()) {
            servicesTableModel.addRow(new Object[]{
                    row[0],
                    row[1] + " " + row[2],
                    row[8],
                    row[9],
                    row[10],
                    row[11]
            });
        }
    }

    private void refreshReports() {
        if (reportArea != null) {
            int totalAppointments = AppointmentService.getAllAppointments().size();
            int totalCustomers = CustomerService.getAllCustomers().size();

            String reportText = "=== SİSTEM İSTATİSTİKLERİ ===\n\n" +
                    "Toplanan Veriler:\n" +
                    "-----------------------------------\n" +
                    "> Kayıtlı Toplam Müşteri : " + totalCustomers + "\n" +
                    "> Açılan Toplam Randevu  : " + totalAppointments + "\n" +
                    "-----------------------------------\n" +
                    "\nSistem Durumu: Aktif ve Senkronize\n" +
                    "Son Güncelleme: " + new java.util.Date().toString();

            reportArea.setText(reportText);
        }
    }

    private void deleteCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow != -1) customersTableModel.removeRow(selectedRow);
    }
}