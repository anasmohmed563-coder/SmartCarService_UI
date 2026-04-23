package services;

public class ServiceCostCalculator {

    private static class CalculationVersion1 {
        public double calculate(String serviceType, int laborHours, double laborRate) {
            double baseCost = laborHours * laborRate;
            double markup = baseCost * 0.15;
            return baseCost + markup;
        }
    }

    private static class CalculationVersion2 {
        public double calculate(String serviceType, int laborHours, double laborRate,
                                double partsCost) {
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

            try {
                result = executeVersion1(serviceType, laborHours, laborRate);
                if (validateResult(result)) {
                    System.out.println("✓ Version 1 başarılı: $" + String.format("%.2f", result));
                    return result;
                }
            } catch (Exception e) {
                System.out.println("✗ Version 1 başarısız");
            }

            try {
                result = executeVersion2(serviceType, laborHours, laborRate, partsCost);
                if (validateResult(result)) {
                    System.out.println("✓ Version 2 başarılı: $" + String.format("%.2f", result));
                    return result;
                }
            } catch (Exception e) {
                System.out.println("✗ Version 2 başarısız");
            }

            try {
                result = executeVersion3(serviceType, laborHours, laborRate,
                        partsCost, customerServiceCount);
                if (validateResult(result)) {
                    System.out.println("✓ Version 3 başarılı: $" + String.format("%.2f", result));
                    return result;
                }
            } catch (Exception e) {
                System.out.println("✗ Version 3 başarısız");
            }

            System.out.println("⚠ Fallback kullanılıyor");
            return laborHours * laborRate * 1.25;
        }

        private double executeVersion1(String serviceType, int laborHours, double laborRate) {
            if (laborHours <= 0 || laborRate <= 0) {
                throw new IllegalArgumentException("Geçersiz parametreler");
            }
            return new CalculationVersion1().calculate(serviceType, laborHours, laborRate);
        }

        private double executeVersion2(String serviceType, int laborHours, double laborRate,
                                       double partsCost) {
            if (laborHours <= 0 || laborRate <= 0 || partsCost < 0) {
                throw new IllegalArgumentException("Geçersiz parametreler");
            }
            return new CalculationVersion2().calculate(serviceType, laborHours, laborRate, partsCost);
        }

        private double executeVersion3(String serviceType, int laborHours, double laborRate,
                                       double partsCost, int customerServiceCount) {
            if (laborHours <= 0 || laborRate <= 0 || partsCost < 0 || customerServiceCount < 0) {
                throw new IllegalArgumentException("Geçersiz parametreler");
            }
            return new CalculationVersion3().calculate(serviceType, laborHours, laborRate,
                    partsCost, customerServiceCount);
        }

        private boolean validateResult(double result) {
            return result > 0 && result < 100000;
        }
    }
}