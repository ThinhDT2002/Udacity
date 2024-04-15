package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {
    private static final AdminResource ADMIN_RESOURCE = new AdminResource();
    private static final CustomerService CUSTOMER_SERVICE = CustomerService.getCustomerService();
    private static final ReservationService RESERVATION_SERVICE = ReservationService.getReservationService();

    private AdminResource() {
    }

    public static AdminResource getAdminResource() {
        return ADMIN_RESOURCE;
    }

    public Customer getCustomer(String customerEmail) {
        return CUSTOMER_SERVICE.getCustomer(customerEmail);
    }

    public void addRoom(List<IRoom> rooms) {
        for (IRoom room : rooms) {
            RESERVATION_SERVICE.addRoom(room);
        }
    }

    public Collection<IRoom> getAllRooms() {
        return RESERVATION_SERVICE.getAllRooms();
    }

    public Collection<Customer> getAllCustomers() {
        return CUSTOMER_SERVICE.getAllCustomers();
    }

    public void displayAllReservations() {
        RESERVATION_SERVICE.printAllReservation();
    }
}
