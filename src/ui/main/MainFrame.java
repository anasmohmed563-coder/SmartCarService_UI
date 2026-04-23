package ui.main;

import ui.login.LoginUI;
import ui.admin.AdminPanelUI;
import ui.customer.CustomerPanelUI;
import services.AuthenticationService;
import services.LanguageManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private static final String LOGIN_PANEL = "LOGIN";
    private static final String CUSTOMER_PANEL = "CUSTOMER";
    private static final String ADMIN_PANEL = "ADMIN";

    private LoginUI loginUI;
    private CustomerPanelUI customerPanelUI;
    private AdminPanelUI adminPanelUI;
    private NavigationBar navigationBar;

    public MainFrame() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Smart Car Service Tracking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginUI = new LoginUI(this::showAppropriatePanel);
        mainPanel.add(loginUI, LOGIN_PANEL);

        customerPanelUI = new CustomerPanelUI();
        mainPanel.add(customerPanelUI, CUSTOMER_PANEL);

        adminPanelUI = new AdminPanelUI();
        mainPanel.add(adminPanelUI, ADMIN_PANEL);

        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, LOGIN_PANEL);

        setVisible(true);
    }

    private void showAppropriatePanel() {
        AuthenticationService.UserRole role = AuthenticationService.getCurrentRole();
        String username = AuthenticationService.getCurrentUsername();

        if (navigationBar == null) {
            navigationBar = new NavigationBar(this::handleLogout, this::handleLanguageChange);
            add(navigationBar, BorderLayout.NORTH);
            revalidate();
            repaint();
        }

        if (role == AuthenticationService.UserRole.ADMIN) {
            cardLayout.show(mainPanel, ADMIN_PANEL);
            setTitle("Smart Car Service - Yönetici Paneli (" + username + ")");
        } else if (role == AuthenticationService.UserRole.CUSTOMER) {
            cardLayout.show(mainPanel, CUSTOMER_PANEL);
            setTitle("Smart Car Service - Servis Randevu Sistemi");
        }
    }

    private void handleLogout() {
        AuthenticationService.logout();

        if (navigationBar != null) {
            remove(navigationBar);
            navigationBar = null;
            revalidate();
            repaint();
        }

        cardLayout.show(mainPanel, LOGIN_PANEL);
        setTitle("Smart Car Service Tracking System");
    }

    private void handleLanguageChange(String language) {
        LanguageManager.setLanguage(language);

        // SADECE dil değişimi için yazdığımız metodları tetikliyoruz
        loginUI.updateTexts();
        customerPanelUI.updateTexts();
        adminPanelUI.updateTexts();

        if (navigationBar != null) {
            navigationBar.updateTexts();
        }

        SwingUtilities.updateComponentTreeUI(this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}