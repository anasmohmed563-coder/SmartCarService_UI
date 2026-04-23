package ui.main;

import services.AuthenticationService;
import services.LanguageManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class NavigationBar extends JPanel {

    private JButton logoutButton;
    private JComboBox<String> languageCombo;
    private JLabel welcomeLabel, langLabel;

    private Runnable onLogout;
    private Consumer<String> onLanguageChange;

    public NavigationBar(Runnable onLogout, Consumer<String> onLanguageChange) {
        this.onLogout = onLogout;
        this.onLanguageChange = onLanguageChange;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(welcomeLabel);

        add(Box.createHorizontalStrut(20));

        langLabel = new JLabel();
        add(langLabel);

        languageCombo = new JComboBox<>(new String[]{"Türkçe (TR)", "English (EN)"});
        languageCombo.setSelectedIndex(0);
        languageCombo.addActionListener(e -> changeLanguage());
        add(languageCombo);

        add(Box.createHorizontalStrut(10));

        logoutButton = new JButton();
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> onLogout.run());
        add(logoutButton);

        updateTexts();
    }

    private void changeLanguage() {
        String selected = (String) languageCombo.getSelectedItem();
        if (selected.contains("Türkçe")) {
            onLanguageChange.accept("tr");
        } else {
            onLanguageChange.accept("en");
        }
    }

    public void updateTexts() {
        if (AuthenticationService.getCurrentRole() == AuthenticationService.UserRole.ADMIN) {
            welcomeLabel.setText(LanguageManager.getString("customer.welcome") + " " + AuthenticationService.getCurrentUsername());
        } else {
            welcomeLabel.setText(LanguageManager.getString("customer.welcome"));
        }
        langLabel.setText(LanguageManager.getString("menu.language") + ":");
        logoutButton.setText(LanguageManager.getString("menu.logout"));
    }
}