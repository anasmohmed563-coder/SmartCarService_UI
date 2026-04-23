package services;

import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    // Tüm randevuları/servisleri burada tutacağız
    private static List<Object[]> allAppointments = new ArrayList<>();

    public static void addAppointment(Object[] appointment) {
        allAppointments.add(appointment);
    }

    public static List<Object[]> getAllAppointments() {
        return allAppointments;
    }
}