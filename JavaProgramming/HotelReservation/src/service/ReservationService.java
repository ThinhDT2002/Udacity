package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static common.Utils.isStringEmpty;

public class ReservationService {
    private static final ReservationService RESERVATION_SERVICE = new ReservationService();
    private static final Set<Reservation> RESERVATIONS = new HashSet<>();
    private static final Set<IRoom> ROOMS = new HashSet<>();

    private ReservationService() {
    }

    public static ReservationService getReservationService() {
        return RESERVATION_SERVICE;
    }

    public void addRoom(IRoom room) throws IllegalArgumentException {
        if(isStringEmpty(room.getRoomNumber())) throw new IllegalArgumentException("Room number is required");
        if (isRoomExist(room.getRoomNumber())) {
            throw new IllegalArgumentException("Room number is exist!");
        }
        ROOMS.add(room);
    }

    public IRoom getARoom(String roomId) {
        for(IRoom room : ROOMS) {
            if(room.getRoomNumber().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) throws IllegalArgumentException {
        if (isCheckInDateGreaterThanCheckoutDate(checkInDate, checkOutDate))
            throw new IllegalArgumentException("Booking range is not valid!");
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        if (isARoomBooked(reservation)) throw new IllegalArgumentException("This room is booked!");
        RESERVATIONS.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        if (isCheckInDateGreaterThanCheckoutDate(checkInDate, checkOutDate))
            throw new IllegalArgumentException("Date range is not valid!");
        if (RESERVATIONS.isEmpty()) return ROOMS;
        Collection<IRoom> unavailableRooms = findUnavailableRooms(checkInDate, checkOutDate);
        List<IRoom> availableRooms = new ArrayList<>();
        for (IRoom room : ROOMS) {
            if (!unavailableRooms.contains(room)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public Collection<Reservation> getCustomerReservations(Customer customer) {
        return RESERVATIONS.stream().filter(reservation -> reservation.getCustomer().equals(customer)).collect(Collectors.toList());
    }

    public void printAllReservation() {
        System.out.println(RESERVATIONS);
    }

    public Collection<IRoom> getAllRooms() {
        return ROOMS;
    }

    private Collection<IRoom> findUnavailableRooms(Date checkInDate, Date checkOutDate) {
        List<IRoom> unavailableRooms = new ArrayList<>();
        for (Reservation reservation : RESERVATIONS) {
            if (reservation.getCheckOutDate().after(checkInDate) && reservation.getCheckInDate().before(checkOutDate)) {
                unavailableRooms.add(reservation.getRoom());
            }
        }
        return unavailableRooms;
    }

    private boolean isRoomExist(String roomNumber) {
        for (IRoom room : ROOMS) {
            if(room.getRoomNumber().equals(roomNumber)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCheckInDateGreaterThanCheckoutDate(Date checkInDate, Date checkOutDate) {
        return checkInDate.after(checkOutDate);
    }

    private boolean isARoomBooked(Reservation reservation) {
        return RESERVATIONS.contains(reservation);
    }
}
