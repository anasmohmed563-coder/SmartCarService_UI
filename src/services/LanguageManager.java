package services;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {

    private static ResourceBundle resourceBundle;
    private static Locale currentLocale = new Locale("tr", "TR"); // Varsayılan: Türkçe

    static {
        loadLanguage(currentLocale);
    }

    /**
     * Dili değiştir
     */
    public static void setLanguage(String language) {
        if ("tr".equals(language)) {
            currentLocale = new Locale("tr", "TR");
        } else if ("en".equals(language)) {
            currentLocale = new Locale("en", "US");
        }
        loadLanguage(currentLocale);
    }

    /**
     * ResourceBundle'ı yükle
     */
    private static void loadLanguage(Locale locale) {
        try {
            // "messages" yerine "resources.messages" yazıyoruz
            resourceBundle = ResourceBundle.getBundle("resources.messages", locale);
        } catch (Exception e) {
            System.err.println("ResourceBundle yüklenemedi: " + e.getMessage());
            // Buradaki hatayı önlemek için burayı da "resources.messages" yapmalısın
            resourceBundle = ResourceBundle.getBundle("resources.messages", new Locale("en", "US"));
        }
    }
    /**
     * Belirtilen anahtarın çevirişini döndür
     */
    public static String getString(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (Exception e) {
            return key; // Anahtar bulunamazsa anahtarın kendisini döndür
        }
    }

    /**
     * Güncel dili döndür
     */
    public static String getCurrentLanguage() {
        return currentLocale.getLanguage();
    }
}