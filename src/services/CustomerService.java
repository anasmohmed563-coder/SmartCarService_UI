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
        addCustomer(new Customer("CUST-001", "Ahmet", "Yılmaz", "ahmet@email.com", "555-1234", "İstanbul"));
    }
}