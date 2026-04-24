package services;

import models.Customer;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    // Tüm panellerin erişebileceği ortak müşteri listesi
    private static Map<String, Customer> allCustomers = new HashMap<>();

    public static void addCustomer(Customer customer) {
        allCustomers.put(customer.getCustomerId(), customer);
    }

    public static Map<String, Customer> getAllCustomers() {
        return allCustomers;
    }

    // İlk açılışta boş kalmasın diye örnek veri
    static {
        addCustomer(new Customer("CUST-001", "Ahmet", "Yılmaz", "ahmet@email.com", "5414114141", "İstanbul"));
    }
    // YENİ: Müşteriyi ID'sine göre bulup siler
    public static boolean deleteCustomer(String customerId) {
        if (getAllCustomers().containsKey(customerId)) {
            getAllCustomers().remove(customerId);
            return true;
        }
        return false;
    }
}