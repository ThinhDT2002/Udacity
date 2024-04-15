import api.AdminResource;
import api.HotelResource;
import common.Utils;
import menu.AdminMenu;
import menu.MainMenu;
import model.*;
import service.ReservationService;

import java.text.ParseException;

public class HotelApplication {
    private static final ReservationService RESERVATION_SERVICE = ReservationService.getReservationService();
    private static final HotelResource HOTEL_RESOURCE = HotelResource.getHotelResource();

    public static void main(String[] args) throws ParseException {
        HOTEL_RESOURCE.createCustomer("thinhdt3@fpt.com", "Thinh", "Do Tien");
        IRoom room = new Room("1", 10.0, RoomType.SINGLE);
        IRoom freeRoom = new FreeRoom("2", RoomType.SINGLE);
        RESERVATION_SERVICE.addRoom(room);
        RESERVATION_SERVICE.addRoom(freeRoom);
        MainMenu mainMenu = MainMenu.getMainMenu();
        mainMenu.showMainMenu();
    }
}
