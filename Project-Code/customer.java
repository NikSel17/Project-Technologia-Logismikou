import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int customerId;
    private String name;
    private String nickname;
    private String phone;
    private String email;
    private List<Reservation> reservations;

    public Customer(int customerId, String name, String nickname, String phone, String email) {
        this.customerId = customerId;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.reservations = new ArrayList<>();
    }

    public void makeReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getNickname() { return nickname; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public List<Reservation> getReservations() { return reservations; }
}
