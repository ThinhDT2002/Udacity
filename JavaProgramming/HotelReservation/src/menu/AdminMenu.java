package menu;

import api.AdminResource;
import api.HotelResource;
import model.Room;
import model.RoomType;

import java.util.Collections;

import static common.Utils.*;

public class AdminMenu {
    private static final AdminMenu ADMIN_MENU = new AdminMenu();
    private static final AdminResource ADMIN_RESOURCE = AdminResource.getAdminResource();
    private static final HotelResource HOTEL_RESOURCE = HotelResource.getHotelResource();
    private static final MainMenu MAIN_MENU = MainMenu.getMainMenu();

    private AdminMenu() {
    }

    public static AdminMenu getAdminMenu() {
        return ADMIN_MENU;
    }

    public void showAdminMenu() {
        System.out.println("1. See all customers");
        System.out.println("2. See all rooms");
        System.out.println("3. See all reservations");
        System.out.println("4. Add a room");
        System.out.println("5. Back to main menu");
        int choice = getCustomerChoice("Choose 1 options", 5);
        switch (choice) {
            case 1:
                seeAllCustomers();
                break;
            case 2:
                seeAllRooms();
                break;
            case 3:
                seeAllReservations();
                break;
            case 4:
                addARoom();
                break;
            case 5:
                backToMainMenu();
                break;
            default:
                System.out.println("Select 1 options");
        }
    }

    private void seeAllCustomers() {
        System.out.println(ADMIN_RESOURCE.getAllCustomers());
        showAdminMenu();
    }

    private void seeAllRooms() {
        System.out.println(ADMIN_RESOURCE.getAllRooms());
        showAdminMenu();
    }

    private void seeAllReservations() {
        ADMIN_RESOURCE.displayAllReservations();
        showAdminMenu();
    }

    private void addARoom() {
        String roomNumber = getRoomNumber("Room number: ");
        if (HOTEL_RESOURCE.getRoom(roomNumber) != null) {
            System.out.println("Room number is already existed! Please input another room number!");
            addARoom();
        }
        double roomPrice = promptDouble("Price per night: ");
        int type = getCustomerChoiceForYesNoOption("Room type: \n1. Single\n2. Double");
        RoomType roomType = type == 1 ? RoomType.SINGLE : RoomType.DOUBLE;
        ADMIN_RESOURCE.addRoom(Collections.singletonList(new Room(roomNumber, roomPrice, roomType)));
        System.out.println("Add room successfully!");
        showAdminMenu();
    }

    private String getRoomNumber(String message) {
        try {
            String roomNumber = promtpString(message);
            Integer.parseInt(roomNumber);
            return roomNumber;
        } catch (Exception ex) {
            System.out.println("Room number must be a number!");
            return getRoomNumber(message);
        }
    }

    private void backToMainMenu() {
        MAIN_MENU.showMainMenu();
    }
}
