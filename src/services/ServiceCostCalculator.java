package services;

public class ServiceCostCalculator {

    // ARAYÜZ (UI) İÇİN KÖPRÜ METOD
    // Sadece servis tipini ve müşteri sadakatini ver, gerisini o halletsin!
    public static double calculateStandardServiceCost(String serviceType, int customerServiceCount) {
        int laborHours;
        double partsCost;
        double standardLaborRate = 500.0; // Saatlik standart işçilik ücreti (TL)

        // Servis tipine göre tahmini süre ve yedek parça maliyeti (Sabit değerler)
        switch (serviceType.toLowerCase()) {
            case "oil change" -> { laborHours = 1; partsCost = 1500; }
            case "engine repair" -> { laborHours = 10; partsCost = 15000; }
            case "transmission repair" -> { laborHours = 8; partsCost = 12000; }
            case "brake service" -> { laborHours = 3; partsCost = 3000; }
            case "tire replacement" -> { laborHours = 2; partsCost = 4000; }
            case "battery replacement" -> { laborHours = 1; partsCost = 2500; }
            case "general maintenance" -> { laborHours = 4; partsCost = 5000; }
            case "inspection" -> { laborHours = 1; partsCost = 0; }
            default -> { laborHours = 2; partsCost = 1000; }
        }

        // Recovery Block algoritmasını çağırıyoruz
        RecoveryBlockCalculator calculator = new RecoveryBlockCalculator();
        return calculator.calculateServiceCost(serviceType, laborHours, standardLaborRate, partsCost, customerServiceCount);
    }

    private static class CalculationVersion1 {
        public double calculate(String serviceType, int laborHours, double laborRate) {
            double baseCost = laborHours * laborRate;
            double markup = baseCost * 0.15;
            return baseCost + markup;
        }
    }

    private static class CalculationVersion2 {
        public double calculate(String serviceType, int laborHours, double laborRate, double partsCost) {
            double laborCost = laborHours * laborRate;
            double serviceMultiplier = getServiceMultiplier(serviceType);
            return (laborCost + partsCost) * serviceMultiplier;
        }

        private double getServiceMultiplier(String serviceType) {
            return switch(serviceType.toLowerCase()) {
                case "oil change" -> 1.10;
                case "engine repair" -> 1.35;
                case "transmission repair" -> 1.40;
                case "brake service" -> 1.25;
                case "tire replacement" -> 1.20;
                case "battery replacement" -> 1.15;
                default -> 1.20;
            };
        }
    }

    private static class CalculationVersion3 {
        public double calculate(String serviceType, int laborHours, double laborRate,
                                double partsCost, int customerServiceCount) {
            double laborCost = laborHours * laborRate;
            double totalBeforeDiscount = laborCost + partsCost;
            double discount = calculateDiscount(customerServiceCount);
            double finalCost = totalBeforeDiscount * (1 - discount);

            // Eğer iş 8 saatten uzun sürüyorsa %10 zorluk/mesai ücreti ekle
            double expediteFee = laborHours > 8 ? finalCost * 0.10 : 0;
            return finalCost + expediteFee;
        }

        private double calculateDiscount(int serviceCount) {
            if (serviceCount > 10) return 0.15;
            if (serviceCount > 5) return 0.10;
            if (serviceCount > 2) return 0.05;
            return 0.0;
        }
    }

    public static class RecoveryBlockCalculator {

        public double calculateServiceCost(String serviceType, int laborHours, double laborRate,
                                           double partsCost, int customerServiceCount) {
            double result = 0;

            // ÖNCE EN KAPSAMLI VE DOĞRU HESAPLAMAYI DENİYORUZ (Primary)
            try {
                result = executeVersion3(serviceType, laborHours, laborRate, partsCost, customerServiceCount);
                if (validateResult(result)) {
                    System.out.println("✓ Version 3 başarılı: " + String.format("%.2f", result) + " TL");
                    return result;
                }
            } catch (Exception e) {
                System.out.println("✗ Version 3 başarısız, Version 2'ye geçiliyor...");
            }

            // V3 ÇÖKERSE V2'Yİ DENE (Alternate 1)
            try {
                result = executeVersion2(serviceType, laborHours, laborRate, partsCost);
                if (validateResult(result)) {
                    System.out.println("✓ Version 2 başarılı: " + String.format("%.2f", result) + " TL");
                    return result;
                }
            } catch (Exception e) {
                System.out.println("✗ Version 2 başarısız, Version 1'e geçiliyor...");
            }

            // V2 DE ÇÖKERSE SADECE İŞÇİLİĞİ HESAPLAYAN V1'İ DENE (Alternate 2)
            try {
                result = executeVersion1(serviceType, laborHours, laborRate);
                if (validateResult(result)) {
                    System.out.println("⚠ Sadece İşçilik (Version 1) hesaplandı: " + String.format("%.2f", result) + " TL");
                    return result;
                }
            } catch (Exception e) {
                System.out.println("✗ Version 1 başarısız!");
            }

            // HİÇBİRİ ÇALIŞMAZSA (Sistemin çökmesini engelleyen Fallback)
            System.out.println("⚠ Tüm hesaplamalar çöktü, Fallback kullanılıyor...");
            return laborHours * laborRate * 1.25;
        }

        private double executeVersion1(String serviceType, int laborHours, double laborRate) {
            if (laborHours <= 0 || laborRate <= 0) {
                throw new IllegalArgumentException("İşçilik saati veya ücreti geçersiz.");
            }
            return new CalculationVersion1().calculate(serviceType, laborHours, laborRate);
        }

        private double executeVersion2(String serviceType, int laborHours, double laborRate, double partsCost) {
            if (laborHours <= 0 || laborRate <= 0 || partsCost < 0) {
                throw new IllegalArgumentException("Geçersiz parametreler.");
            }
            return new CalculationVersion2().calculate(serviceType, laborHours, laborRate, partsCost);
        }

        private double executeVersion3(String serviceType, int laborHours, double laborRate,
                                       double partsCost, int customerServiceCount) {
            if (laborHours <= 0 || laborRate <= 0 || partsCost < 0 || customerServiceCount < 0) {
                throw new IllegalArgumentException("Negatif veri girilemez.");
            }
            return new CalculationVersion3().calculate(serviceType, laborHours, laborRate,
                    partsCost, customerServiceCount);
        }

        // Fatura tutarı 0'dan büyük olmalı ve 1 Milyon TL'yi (absürt bir rakam) geçmemeli
        private boolean validateResult(double result) {
            return result > 0 && result < 1000000;
        }
    }
}