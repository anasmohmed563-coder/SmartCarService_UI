package services;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {

    public enum UserRole { ADMIN, CUSTOMER }

    private static class User {
        private String username;
        private String password;
        private UserRole role;

        public User(String username, String password, UserRole role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }

    private static Map<String, User> users = new HashMap<>();
    private static String currentUsername;
    private static UserRole currentRole;

    static {
        // Sistem kullanıcıları (Customer hesaplarını test amaçlı tutabilirsin, ama artık arayüzden direkt girilmeyecek)
        users.put("admin", new User("admin", "admin123", UserRole.ADMIN));
        users.put("customer1", new User("customer1", "pass123", UserRole.CUSTOMER));
        users.put("customer2", new User("customer2", "pass456", UserRole.CUSTOMER));
    }

    /**
     * Yönetici girişini doğrula
     */
    public static boolean authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.password.equals(password)) {
            currentUsername = username;
            currentRole = user.role;
            return true;
        }
        return false;
    }

    /**
     * Şifresiz Müşteri (Misafir) girişi
     * Randevu al butonuna tıklandığında bu metot çağrılacak.
     */
    public static void loginAsGuest() {
        currentUsername = "Değerli Müşterimiz"; // Navigasyon çubuğunda görünecek isim (İstersen "Misafir" de yapabilirsin)
        currentRole = UserRole.CUSTOMER; // Müşteri paneline yönlendirmesi için rolü ayarlıyoruz
    }

    /**
     * Güncel kullanıcı rolünü döndür
     */
    public static UserRole getCurrentRole() {
        return currentRole;
    }

    /**
     * Güncel kullanıcı adını döndür
     */
    public static String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Çıkış yap
     */
    public static void logout() {
        currentUsername = null;
        currentRole = null;
    }

    /**
     * Giriş yapıldı mı kontrol et
     */
    public static boolean isLoggedIn() {
        return currentUsername != null;
    }
}