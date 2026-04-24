package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentService {
    private static List<Object[]> allAppointments = new ArrayList<>();

    // YENİ: Artık sadece tarihi değil, "Tarih_ServisTipi" şeklinde birleşik bir anahtar (key) tutuyoruz
    private static Map<String, Integer> serviceCapacityCounts = new HashMap<>();
    private static final int MAX_APPOINTMENTS_PER_SERVICE = 5;

    public static void addAppointment(Object[] appointment) {
        String date = (String) appointment[9];
        String serviceType = (String) appointment[10]; // Arayüzden gelen servis tipi

        // Atölye bazlı kapasite anahtarı (Örn: "2026-04-24_Oil Change")
        String capacityKey = date + "_" + serviceType;

        int currentCount = serviceCapacityCounts.getOrDefault(capacityKey, 0);

        // KAPASİTE KONTROLÜ (Atölye Bazlı)
        if (currentCount >= MAX_APPOINTMENTS_PER_SERVICE) {
            throw new IllegalArgumentException("Seçtiğiniz tarihte '" + serviceType + "' atölyemiz tam kapasiteye (5/5) ulaşmıştır. Lütfen başka bir gün veya servis seçiniz.");
        }

        serviceCapacityCounts.put(capacityKey, currentCount + 1);
        allAppointments.add(appointment);
    }

    public static List<Object[]> getAllAppointments() {
        return allAppointments;
    }

    // YENİ: Arayüzün doluluğu sorgulaması için tarih ve servis tipini beraber alan metod
    public static int getAppointmentCount(String date, String serviceType) {
        if(serviceType == null || serviceType.startsWith("--")) return 0;
        return serviceCapacityCounts.getOrDefault(date + "_" + serviceType, 0);
    }

    public static boolean deleteAppointment(String appointmentId) {
        for (int i = 0; i < allAppointments.size(); i++) {
            if (allAppointments.get(i)[0].equals(appointmentId)) {
                String date = (String) allAppointments.get(i)[9];
                String serviceType = (String) allAppointments.get(i)[10];
                String capacityKey = date + "_" + serviceType;

                int currentCount = serviceCapacityCounts.getOrDefault(capacityKey, 0);
                if (currentCount > 0) {
                    serviceCapacityCounts.put(capacityKey, currentCount - 1);
                }

                allAppointments.remove(i);
                return true;
            }
        }
        return false;
    }
}