package menu;

import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

import static common.Utils.*;

public class MainMenu {
    private static final MainMenu MAIN_MENU = new MainMenu();
    private static final HotelResource HOTEL_RESOURCE = HotelResource.getHotelResource();
    private static final AdminResource ADMIN_RESOURCE = AdminResource.getAdminResource();
    private static final AdminMenu ADMIN_MENU = AdminMenu.getAdminMenu();

    private MainMenu() {
    }

    public static MainMenu getMainMenu() {
        return MAIN_MENU;
    }

    public void showMainMenu() {
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        int choice = getCustomerChoice("Choose 1 option", 5);
        switch (choice) {
            case 1:
                findAndReserveARoom();
                break;
            case 2:
                getCustomerReservations();
                break;
            case 3:
                createAnAccount();
                break;
            case 4:
                ADMIN_MENU.showAdminMenu();
                break;
            case  5:
                exit();
                break;
            default:
                System.out.println("Select 1 option");
        }
    }

    private void findAndReserveARoom() {
        try {
            Date checkInDate = promptDate("Check in date: (MM-dd-yyyy, Ex: 11-02-2023)");
            Date checkOutDate = promptDate("Check out date: (MM-dd-yyyy, Ex: 11-02-2023)");
            Collection<IRoom> availableRooms = findRooms(checkInDate, checkOutDate);
            if(availableRooms != null && availableRooms.isEmpty()) {
                checkInDate = Date.from(checkInDate.toInstant().plus(7, ChronoUnit.DAYS));
                checkOutDate = Date.from(checkOutDate.toInstant().plus(7, ChronoUnit.DAYS));
                availableRooms = findRooms(checkInDate, checkOutDate);
            }
            System.out.println(availableRooms);
            reserveARoom(availableRooms, checkInDate, checkOutDate);
            showMainMenu();
        } catch (Exception ex) {
            System.out.println("Please input valid date range!");
            findAndReserveARoom();
        }
    }

    private void getCustomerReservations() {
        String customerEmail = promtpString("Enter your email: ");
        Collection<Reservation> customerReservations = HOTEL_RESOURCE.getCustomerReservations(customerEmail);
        if(customerReservations != null && !customerReservations.isEmpty()) {
            System.out.println(customerReservations);
        } else {
            System.out.println("You don't have any reservation");
        }
        showMainMenu();
    }

    private void createAnAccount() {
        try {
            String email = promtpString("Email address: ");
            if(HOTEL_RESOURCE.getCustomer(email) != null) {
                System.out.println("Email address is already existed! Please select other email");
                createAnAccount();
            }
            String firstName = promtpString("First name: ");
            String lastName = promtpString("Last name: ");
            HOTEL_RESOURCE.createCustomer(email, firstName, lastName);
            System.out.println("Create customer successfully!");
            showMainMenu();
        } catch (Exception ex) {
            System.out.println("Email address not valid");
            createAnAccount();
        }
    }

    private void exit() {
        int exitChoice = getCustomerChoiceForYesNoOption("Close application ?\n1. Yes\n2. No");
        if(exitChoice == 1) {
            System.out.println("Exited");
            System.exit(0);
        }
    }

    private Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        return HOTEL_RESOURCE.findARoom(checkInDate, checkOutDate);
    }

    private void reserveARoom(Collection<IRoom> availableRooms, Date checkInDate, Date checkOutDate) throws NullPointerException {
        if (!availableRooms.isEmpty()) {
            int bookARoomChoice = getCustomerChoiceForYesNoOption("Do you want to book a room ?\n1. Yes\n2. No");
            if (bookARoomChoice == 1) {
                int createAccountChoice = getCustomerChoiceForYesNoOption("Do you have an account ?\n1. Yes\n2. No");
                if (createAccountChoice == 1) {
                    String customerEmail = getCustomerEmail();
                    IRoom room = getRoom(availableRooms);
                    Reservation reservation = HOTEL_RESOURCE.bookARoom(customerEmail, room, checkInDate, checkOutDate);
                    System.out.println(reservation);
                }
            }
        } else {
            showMainMenu();
        }
    }

    private String getCustomerEmail() {
        String customerEmail;
        do {
            customerEmail = promtpString("Enter your email: ");
        } while (ADMIN_RESOURCE.getCustomer(customerEmail) == null);
        return customerEmail;
    }

    private IRoom getRoom(Collection<IRoom> availableRooms) {
        String roomNumber;
        IRoom room;
        do {
            roomNumber = promtpString("Enter room number you want to book: ");
            room = HOTEL_RESOURCE.getRoom(roomNumber);
        } while (room == null || !availableRooms.contains(room));
        return room;
    }
}
