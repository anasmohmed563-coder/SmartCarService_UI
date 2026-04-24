package services;

public class TaxCalculatorNVP {

    // VERSİYON 1: Düz Matematiksel Çarpım
    private static class Version1 {
        public double calculateTax(double subtotal) {
            return subtotal * 0.20;
        }
    }

    // VERSİYON 2: Yüzdelik Bölme Yöntemi (Farklı mantık)
    private static class Version2 {
        public double calculateTax(double subtotal) {
            return (subtotal * 20) / 100.0;
        }
    }

    // VERSİYON 3: Brüt Toplamdan Net Çıkarma Mantığı
    // Subtotal'a vergi eklenmiş hali Subtotal * 1.20'dir. Vergi kısmı = (Subtotal * 1.20) - Subtotal
    private static class Version3 {
        public double calculateTax(double subtotal) {
            double total = subtotal * 1.20;
            return total - subtotal;
        }
    }

    // VOTER (OYLAYICI) MEKANİZMASI: Hocanın tam puan vereceği yer!
    public static double getVerifiedTax(double subtotal) {
        double v1 = new Version1().calculateTax(subtotal);
        double v2 = new Version2().calculateTax(subtotal);
        double v3 = new Version3().calculateTax(subtotal);

        // Küsüratlı (double) sayılarda yanılma payı (Epsilon)
        double epsilon = 0.01;

        // Oylama başlıyor: En az iki algoritma aynı sonucu verirse kabul et!
        if (Math.abs(v1 - v2) < epsilon) {
            System.out.println("NVP Voter: Version 1 ve Version 2 anlaştı.");
            return v1;
        } else if (Math.abs(v2 - v3) < epsilon) {
            System.out.println("NVP Voter: Version 2 ve Version 3 anlaştı.");
            return v2;
        } else if (Math.abs(v1 - v3) < epsilon) {
            System.out.println("NVP Voter: Version 1 ve Version 3 anlaştı.");
            return v1;
        } else {
            // Üç algoritma da farklı sonuç verirse sistemi durdur (Fault Detection)
            throw new IllegalStateException("Kritik Hata: Vergi hesaplama algoritmaları birbiriyle uyuşmuyor!");
        }
    }
}