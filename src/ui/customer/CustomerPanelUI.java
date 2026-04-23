package ui.customer;

import models.Customer;
import services.CustomerService;
import services.LanguageManager;
import services.AppointmentService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CustomerPanelUI extends JPanel {

    private JTextField firstNameField, lastNameField, emailField, phoneField;
    private JComboBox<String> makeCombo, modelCombo, yearCombo;
    private JTextField licensePlateField;

    private JLabel dateDisplayLabel;
    private JButton datePickerButton;
    private Date selectedDate;
    private JComboBox<String> serviceTypeCombo;
    private JButton addButton, clearButton;
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;

    private JLabel titleLabel, nameLabel, surnameLabel, emailLabel, phoneLabel, makeLabel, modelLabel, yearLabel, plateLabel, dateLabel, serviceTypeLabel;
    private JPanel formPanel, bottomPanel;

    private Map<String, String[]> carData;
    private Map<String, String> appointments;

    public CustomerPanelUI() {
        appointments = new HashMap<>();
        selectedDate = new Date();
        initCarData();
        initializeUI();
    }

    private void initCarData() {
        carData = new HashMap<>();
        carData.put("Toyota", new String[]{"Corolla", "Camry", "Yaris"});
        carData.put("Honda", new String[]{"Civic", "Accord", "CR-V"});
        carData.put("Ford", new String[]{"Focus", "Mustang", "Kuga"});
        carData.put("Volkswagen", new String[]{"Golf", "Passat", "Polo"});
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);

        bottomPanel = createAppointmentsTable();
        add(bottomPanel, BorderLayout.SOUTH);

        updateTexts();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));

        nameLabel = new JLabel();
        panel.add(nameLabel);
        firstNameField = new JTextField(15);
        panel.add(firstNameField);

        surnameLabel = new JLabel();
        panel.add(surnameLabel);
        lastNameField = new JTextField(15);
        panel.add(lastNameField);

        emailLabel = new JLabel();
        panel.add(emailLabel);
        emailField = new JTextField(15);
        panel.add(emailField);

        phoneLabel = new JLabel();
        panel.add(phoneLabel);
        phoneField = new JTextField(15);
        panel.add(phoneField);

        makeLabel = new JLabel();
        panel.add(makeLabel);
        makeCombo = new JComboBox<>();
        makeCombo.addItem(LanguageManager.getString("customer.make.select"));
        for (String make : carData.keySet()) {
            makeCombo.addItem(make);
        }
        panel.add(makeCombo);

        modelLabel = new JLabel();
        panel.add(modelLabel);
        modelCombo = new JComboBox<>();
        modelCombo.addItem(LanguageManager.getString("customer.model.select"));
        panel.add(modelCombo);

        makeCombo.addActionListener(e -> {
            String selectedMake = (String) makeCombo.getSelectedItem();
            modelCombo.removeAllItems();

            if (selectedMake != null && carData.containsKey(selectedMake) && makeCombo.getSelectedIndex() != 0) {
                for (String model : carData.get(selectedMake)) {
                    modelCombo.addItem(model);
                }
            } else {
                modelCombo.addItem(LanguageManager.getString("customer.model.select"));
            }
        });

        yearLabel = new JLabel();
        panel.add(yearLabel);
        yearCombo = new JComboBox<>();
        yearCombo.addItem(LanguageManager.getString("customer.year.select"));
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= 1990; i--) {
            yearCombo.addItem(String.valueOf(i));
        }
        panel.add(yearCombo);

        plateLabel = new JLabel();
        panel.add(plateLabel);
        licensePlateField = new JTextField(10);
        panel.add(licensePlateField);

        dateLabel = new JLabel();
        panel.add(dateLabel);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        dateDisplayLabel = new JLabel(formatDate(selectedDate));
        dateDisplayLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dateDisplayLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        dateDisplayLabel.setPreferredSize(new Dimension(120, 25));
        datePanel.add(dateDisplayLabel);

        datePickerButton = new JButton();
        datePickerButton.addActionListener(e -> openDatePicker());
        datePanel.add(datePickerButton);

        panel.add(datePanel);

        serviceTypeLabel = new JLabel();
        panel.add(serviceTypeLabel);
        serviceTypeCombo = new JComboBox<>(new String[]{
                "Oil Change", "Engine Repair", "Transmission Repair",
                "Brake Service", "Tire Replacement", "Battery Replacement",
                "General Maintenance", "Inspection"
        });
        panel.add(serviceTypeCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        addButton = new JButton();
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        addButton.addActionListener(e -> createAppointment());
        buttonPanel.add(addButton);

        clearButton = new JButton();
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearButton.addActionListener(e -> clearFields());
        buttonPanel.add(clearButton);

        panel.add(buttonPanel);

        return panel;
    }

    public void updateTexts() {
        titleLabel.setText(LanguageManager.getString("customer.title"));
        formPanel.setBorder(BorderFactory.createTitledBorder(LanguageManager.getString("customer.form.title")));
        nameLabel.setText(LanguageManager.getString("customer.name"));
        surnameLabel.setText(LanguageManager.getString("customer.surname"));
        emailLabel.setText(LanguageManager.getString("customer.email"));
        phoneLabel.setText(LanguageManager.getString("customer.phone"));
        makeLabel.setText(LanguageManager.getString("customer.make"));
        modelLabel.setText(LanguageManager.getString("customer.model"));
        yearLabel.setText(LanguageManager.getString("customer.year"));
        plateLabel.setText(LanguageManager.getString("customer.plate"));
        dateLabel.setText(LanguageManager.getString("customer.date"));
        serviceTypeLabel.setText(LanguageManager.getString("customer.serviceType"));
        datePickerButton.setText(LanguageManager.getString("customer.date.button"));
        addButton.setText(LanguageManager.getString("customer.button.add"));
        clearButton.setText(LanguageManager.getString("customer.button.clear"));
        bottomPanel.setBorder(BorderFactory.createTitledBorder(LanguageManager.getString("customer.table.title")));

        // TABLO BAŞLIKLARINI GÜNCELLEME KISMI
        if (tableModel != null) {
            tableModel.setColumnIdentifiers(new String[]{
                    LanguageManager.getString("table.appId"),
                    LanguageManager.getString("table.name"),
                    LanguageManager.getString("table.surname"),
                    LanguageManager.getString("table.email"),
                    LanguageManager.getString("table.phone"),
                    LanguageManager.getString("table.make"),
                    LanguageManager.getString("table.model"),
                    LanguageManager.getString("table.year"),
                    LanguageManager.getString("table.plate"),
                    LanguageManager.getString("table.date"),
                    LanguageManager.getString("table.serviceType"),
                    LanguageManager.getString("table.status")
            });
        }

        // COMBOBOX İÇİNDEKİ SEÇİM YAZILARINI GÜNCELLEME
        if (makeCombo.getItemCount() > 0) {
            String currentSelection = (String) makeCombo.getSelectedItem();
            makeCombo.removeItemAt(0);
            makeCombo.insertItemAt(LanguageManager.getString("customer.make.select"), 0);
            if (currentSelection.equals("-- Marka Seç --") || currentSelection.equals("-- Select Make --")) {
                makeCombo.setSelectedIndex(0);
            }
        }

        if (modelCombo.getItemCount() > 0 && modelCombo.getSelectedIndex() == 0) {
            modelCombo.removeItemAt(0);
            modelCombo.insertItemAt(LanguageManager.getString("customer.model.select"), 0);
            modelCombo.setSelectedIndex(0);
        }

        if (yearCombo.getItemCount() > 0) {
            String currentSelection = (String) yearCombo.getSelectedItem();
            yearCombo.removeItemAt(0);
            yearCombo.insertItemAt(LanguageManager.getString("customer.year.select"), 0);
            if (currentSelection.equals("-- Yıl Seç --") || currentSelection.equals("-- Select Year --")) {
                yearCombo.setSelectedIndex(0);
            }
        }
    }

    private void openDatePicker() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Tarih Seçin");
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel calendarPanel = new JPanel();
        calendarPanel.setLayout(new GridLayout(0, 7, 5, 5));
        calendarPanel.setBorder(BorderFactory.createTitledBorder("Takvim Seçin"));

        JPanel monthYearPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        String[] months = {"Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran",
                "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"};
        JComboBox<String> monthCombo = new JComboBox<>(months);
        JComboBox<Integer> yearDialogCombo = new JComboBox<>();

        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH);

        monthCombo.setSelectedIndex(currentMonth);

        for (int i = currentYear - 2; i <= currentYear + 5; i++) {
            yearDialogCombo.addItem(i);
        }
        yearDialogCombo.setSelectedItem(currentYear);

        monthYearPanel.add(new JLabel("Ay:"));
        monthYearPanel.add(monthCombo);
        monthYearPanel.add(new JLabel("Yıl:"));
        monthYearPanel.add(yearDialogCombo);

        content.add(monthYearPanel, BorderLayout.NORTH);

        JPanel daysPanel = new JPanel(new GridLayout(6, 7, 3, 3));
        daysPanel.setBorder(BorderFactory.createTitledBorder("Gün Seçin"));

        String[] dayNames = {"Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz"};
        for (String day : dayNames) {
            JLabel dayLabel = new JLabel(day);
            dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 11));
            daysPanel.add(dayLabel);
        }

        refreshCalendarDays(daysPanel, (Integer) yearDialogCombo.getSelectedItem(),
                monthCombo.getSelectedIndex(), dialog);

        monthCombo.addActionListener(e ->
                refreshCalendarDays(daysPanel, (Integer) yearDialogCombo.getSelectedItem(),
                        monthCombo.getSelectedIndex(), dialog));
        yearDialogCombo.addActionListener(e ->
                refreshCalendarDays(daysPanel, (Integer) yearDialogCombo.getSelectedItem(),
                        monthCombo.getSelectedIndex(), dialog));

        content.add(daysPanel, BorderLayout.CENTER);

        JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton cancelButton = new JButton("İptal");
        cancelButton.addActionListener(e -> dialog.dispose());
        dialogButtonPanel.add(cancelButton);

        content.add(dialogButtonPanel, BorderLayout.SOUTH);

        dialog.add(content);
        dialog.setVisible(true);
    }

    private void refreshCalendarDays(JPanel daysPanel, int year, int month, JDialog dialog) {
        java.util.List<Component> toRemove = new java.util.ArrayList<>();
        if (daysPanel.getComponentCount() > 7) {
            for (int i = 7; i < daysPanel.getComponentCount(); i++) {
                toRemove.add(daysPanel.getComponent(i));
            }
            for (Component c : toRemove) {
                daysPanel.remove(c);
            }
        }

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);

        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDayOfWeek; i++) {
            daysPanel.add(new JLabel());
        }

        for (int day = 1; day <= daysInMonth; day++) {
            final int currentDay = day;
            final int currentYear = year;
            final int currentMonth = month;

            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.addActionListener(e -> {
                Calendar selectedCal = Calendar.getInstance();
                selectedCal.set(currentYear, currentMonth, currentDay);
                selectedDate = selectedCal.getTime();
                dateDisplayLabel.setText(formatDate(selectedDate));
                dialog.dispose();
            });

            daysPanel.add(dayButton);
        }

        daysPanel.revalidate();
        daysPanel.repaint();
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private JPanel createAppointmentsTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 200));

        // Başlıklar updateTexts içinden dinamik çekiliyor artık
        String[] columns = {"ID", "Ad", "Soyad", "Email", "Telefon", "Marka", "Model", "Yıl", "Plaka", "Tarih", "Servis", "Durum"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentsTable = new JTable(tableModel);
        appointmentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void createAppointment() {
        if (!validateFields()) {
            return;
        }

        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            String make = (String) makeCombo.getSelectedItem();
            String model = (String) modelCombo.getSelectedItem();
            String year = (String) yearCombo.getSelectedItem();
            String plate = licensePlateField.getText().trim();

            String date = formatDate(selectedDate);
            String serviceType = (String) serviceTypeCombo.getSelectedItem();

            String appointmentId = "APP-" + System.currentTimeMillis();

            Object[] rowData = new Object[]{
                    appointmentId, firstName, lastName, email, phone,
                    make, model, year, plate, date, serviceType, "Beklemede"
            };

            tableModel.addRow(rowData);
            AppointmentService.addAppointment(rowData);
            appointments.put(appointmentId, firstName + " " + lastName + " - " + make + " " + model + " - " + serviceType);

            String customerId = "CUST-" + System.currentTimeMillis();
            Customer newCustomer = new Customer(customerId, firstName, lastName, email, phone, "Adres belirtilmedi");
            CustomerService.addCustomer(newCustomer);

            JOptionPane.showMessageDialog(this,
                    "Randevu başarıyla oluşturuldu!\n\n" +
                            "Randevu ID: " + appointmentId + "\n" +
                            "Müşteri: " + firstName + " " + lastName + "\n" +
                            "Araç: " + make + " " + model + " (" + year + ")\n" +
                            "Plaka: " + plate + "\n" +
                            "Tarih: " + date + "\n" +
                            "Servis: " + serviceType,
                    LanguageManager.getString("status.success"),
                    JOptionPane.INFORMATION_MESSAGE);

            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    LanguageManager.getString("status.error") + ": " + e.getMessage(),
                    LanguageManager.getString("status.error"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateFields() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String plate = licensePlateField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || plate.isEmpty()) {
            JOptionPane.showMessageDialog(this, LanguageManager.getString("login.error.empty"),
                    LanguageManager.getString("status.warning"), JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (makeCombo.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Lütfen bir Araç Markası seçiniz!",
                    LanguageManager.getString("status.warning"), JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (modelCombo.getSelectedIndex() < 0 || modelCombo.getSelectedItem().toString().startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Lütfen bir Araç Modeli seçiniz!",
                    LanguageManager.getString("status.warning"), JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (yearCombo.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Lütfen bir Üretim Yılı seçiniz!",
                    LanguageManager.getString("status.warning"), JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Geçerli bir email adresi girin!",
                    LanguageManager.getString("status.warning"), JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (phone.length() < 10) {
            JOptionPane.showMessageDialog(this, "Geçerli bir telefon numarası girin!",
                    LanguageManager.getString("status.warning"), JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        licensePlateField.setText("");

        makeCombo.setSelectedIndex(0);
        yearCombo.setSelectedIndex(0);

        selectedDate = new Date();
        dateDisplayLabel.setText(formatDate(selectedDate));
        serviceTypeCombo.setSelectedIndex(0);
    }
}