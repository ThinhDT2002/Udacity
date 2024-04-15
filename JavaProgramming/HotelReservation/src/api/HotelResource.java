package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
    private static final HotelResource HOTEL_RESOURCE = new HotelResource();
    private static final CustomerService CUSTOMER_SERVICE = CustomerService.getCustomerService();
    private static final ReservationService RESERVATION_SERVICE = ReservationService.getReservationService();

    private HotelResource() {
    }

    public static HotelResource getHotelResource() {
        return HOTEL_RESOURCE;
    }

    public Customer getCustomer(String email) {
        return CUSTOMER_SERVICE.getCustomer(email);
    }

    public void createCustomer(String email, String firstName, String lastName) {
        CUSTOMER_SERVICE.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return RESERVATION_SERVICE.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        Customer customer = CUSTOMER_SERVICE.getCustomer(customerEmail);
        return RESERVATION_SERVICE.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomerReservations(String customerEmail) {
        return RESERVATION_SERVICE.getCustomerReservations(CUSTOMER_SERVICE.getCustomer(customerEmail));
    }

    public Collection<IRoom> getAllRooms() {
        return RESERVATION_SERVICE.getAllRooms();
    }

    public Collection<IRoom> findARoom(Date checkInDate, Date checkOutDate) {
        Collection<IRoom> rooms = RESERVATION_SERVICE.findRooms(checkInDate, checkOutDate);
        if(rooms.isEmpty()) {
            System.out.println("There are no rooms available from " + checkInDate.toString() + " to " + checkOutDate.toString());
            System.out.println("Recommend rooms for next 7 days");
        }
        return rooms;
    }
}
