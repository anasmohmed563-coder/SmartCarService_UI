package ui.login;

import services.AuthenticationService;
import services.LanguageManager;
import javax.swing.*;
import java.awt.*;

public class LoginUI extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, exitButton, adminLoginBtn, guestLoginBtn, backButton;
    private JComboBox<String> languageCombo;
    private Runnable onLoginSuccess;

    private JLabel mainTitleLabel, adminTitleLabel, usernameLabel, passwordLabel, langLabel;

    private JPanel cardPanel;
    private CardLayout cardLayout;
    private static final String MAIN_VIEW = "MAIN_VIEW";
    private static final String ADMIN_VIEW = "ADMIN_VIEW";

    public LoginUI(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(createMainSelectionPanel(), MAIN_VIEW);
        cardPanel.add(createAdminLoginPanel(), ADMIN_VIEW);

        add(cardPanel, BorderLayout.CENTER);
        updateTexts();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        langLabel = new JLabel();
        panel.add(langLabel);

        languageCombo = new JComboBox<>(new String[]{"Türkçe (TR)", "English (EN)"});
        languageCombo.setSelectedIndex(0);
        languageCombo.addActionListener(e -> changeLanguage());
        panel.add(languageCombo);

        return panel;
    }

    private JPanel createMainSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 25, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        mainTitleLabel = new JLabel();
        mainTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        mainTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(mainTitleLabel, gbc);

        guestLoginBtn = new JButton();
        guestLoginBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        guestLoginBtn.setPreferredSize(new Dimension(300, 60));
        guestLoginBtn.setFocusPainted(false);
        guestLoginBtn.addActionListener(e -> {
            AuthenticationService.loginAsGuest();
            onLoginSuccess.run();
        });
        gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 20, 30, 20);
        panel.add(guestLoginBtn, gbc);

        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));

        adminLoginBtn = new JButton();
        adminLoginBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        adminLoginBtn.setPreferredSize(new Dimension(150, 40));
        adminLoginBtn.addActionListener(e -> cardLayout.show(cardPanel, ADMIN_VIEW));

        exitButton = new JButton();
        exitButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        exitButton.setPreferredSize(new Dimension(150, 40));
        exitButton.addActionListener(e -> System.exit(0));

        bottomButtons.add(adminLoginBtn);
        bottomButtons.add(exitButton);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 20, 20, 20);
        panel.add(bottomButtons, gbc);

        return panel;
    }

    private JPanel createAdminLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        adminTitleLabel = new JLabel();
        adminTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        adminTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 15, 30, 15);
        panel.add(adminTitleLabel, gbc);

        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.gridwidth = 1; gbc.gridy = 1;
        usernameLabel = new JLabel();
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(250, 40));
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        passwordLabel = new JLabel();
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(250, 40));
        panel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        loginButton = new JButton();
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(140, 45));
        loginButton.addActionListener(e -> performLogin());

        backButton = new JButton();
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setPreferredSize(new Dimension(140, 45));
        backButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            cardLayout.show(cardPanel, MAIN_VIEW);
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, LanguageManager.getString("login.error.empty"), LanguageManager.getString("status.warning"), JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (AuthenticationService.authenticate(username, password)) {
            if(AuthenticationService.getCurrentRole() == AuthenticationService.UserRole.ADMIN) {
                usernameField.setText("");
                passwordField.setText("");
                onLoginSuccess.run();
            } else {
                JOptionPane.showMessageDialog(this, "Bu alandan sadece yöneticiler giriş yapabilir.", "Yetki Hatası", JOptionPane.ERROR_MESSAGE);
                AuthenticationService.logout();
            }
        } else {
            JOptionPane.showMessageDialog(this, LanguageManager.getString("login.error.invalid"), LanguageManager.getString("status.error"), JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void changeLanguage() {
        String selected = (String) languageCombo.getSelectedItem();
        if (selected.contains("Türkçe")) {
            LanguageManager.setLanguage("tr");
        } else {
            LanguageManager.setLanguage("en");
        }
        updateTexts();
    }

    public void updateTexts() {
        langLabel.setText(LanguageManager.getString("menu.language") + ":");
        mainTitleLabel.setText(LanguageManager.getString("login.title"));
        guestLoginBtn.setText(LanguageManager.getString("login.button.guest"));
        adminLoginBtn.setText(LanguageManager.getString("login.button.admin"));
        exitButton.setText(LanguageManager.getString("login.button.exit"));

        adminTitleLabel.setText(LanguageManager.getString("login.admin.title"));
        usernameLabel.setText(LanguageManager.getString("login.username"));
        passwordLabel.setText(LanguageManager.getString("login.password"));
        loginButton.setText(LanguageManager.getString("login.button.login"));
        backButton.setText(LanguageManager.getString("login.button.back"));
    }
}