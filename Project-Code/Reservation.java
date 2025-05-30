import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private int reservationId;
    private LocalDate date;
    private LocalTime time;
    private String status;
    private Customer customer;
    private Restaurant restaurant;

    public Reservation(int reservationId, LocalDate date, LocalTime time, Customer customer, Restaurant restaurant) {
        this.reservationId = reservationId;
        this.date = date;
        this.time = time;
        this.status = "Pending";
        this.customer = customer;
        this.restaurant = restaurant;
    }

    public boolean reservationExist() {
        return date != null && time != null && restaurant != null;
    }

    public void CR() {
        if (reservationExist() && valid(date) && validNumOfPeople()) {
            System.out.println("Reservation created successfully.");
        } else {
            System.out.println("Failed to create reservation.");
        }
    }

    public boolean valid(LocalDate date) {
        return !date.isBefore(LocalDate.now());
    }

    public boolean validNumOfPeople() {
        return true;
    }

    public int getReservationId() { return reservationId; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getStatus() { return status; }
    public Customer getCustomer() { return customer; }
    public Restaurant getRestaurant() { return restaurant; }

    public void setStatus(String status) { this.status = status; }
}
