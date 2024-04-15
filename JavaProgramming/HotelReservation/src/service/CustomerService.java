package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static common.Utils.isStringEmpty;

public class CustomerService {
    private static final CustomerService CUSTOMER_SERVICE = new CustomerService();
    private static final Map<String, Customer> CUSTOMERS = new HashMap<>();

    private CustomerService() {
    }

    public static CustomerService getCustomerService() {
        return CUSTOMER_SERVICE;
    }

    public void addCustomer(String email, String firstName, String lastName) throws IllegalArgumentException {
        if (isStringEmpty(firstName) || isStringEmpty(lastName)) {
            throw new IllegalArgumentException("Customer name is required!");
        }
        if (isUserExist(email)) {
            throw new IllegalArgumentException("Email is exist!");
        }
        CUSTOMERS.put(email, new Customer(firstName, lastName, email));
    }

    public Customer getCustomer(String email) {
        return CUSTOMERS.get(email);
    }

    public Collection<Customer> getAllCustomers() {
        return CUSTOMERS.values();
    }

    private boolean isUserExist(String email) {
        return CUSTOMERS.containsKey(email);
    }

}
