import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RestaurantUI extends JFrame {
    private static Reservation pendingReservation;
    private static CustomerUI customerUI;
    private static JLabel requestLabel;
    private static JLabel responseLabel;
    private static JButton acceptBtn;
    private static JButton declineBtn;

    public static void setCustomerUI(CustomerUI ui) {
        customerUI = ui;
    }

    public RestaurantUI() {
        setTitle("Restaurant - Reservation Manager");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        JLabel title = new JLabel("Incoming Reservation Request");
        requestLabel = new JLabel("No request received.");
        acceptBtn = new JButton("Accept");
        declineBtn = new JButton("Decline");
        responseLabel = new JLabel("Status: Waiting...");

        // Disable buttons initially
        acceptBtn.setEnabled(false);
        declineBtn.setEnabled(false);

        acceptBtn.addActionListener((ActionEvent e) -> {
            if (pendingReservation != null) {
                pendingReservation.setStatus("Accepted");
                responseLabel.setText("Reservation Accepted");

                if (customerUI != null) {
                    customerUI.receiveNotification(
                        "Your reservation at " + pendingReservation.getRestaurant().getName() + " was accepted."
                    );
                }

                // Disable buttons after decision
                acceptBtn.setEnabled(false);
                declineBtn.setEnabled(false);
            }
        });

        declineBtn.addActionListener((ActionEvent e) -> {
            if (pendingReservation != null) {
                pendingReservation.setStatus("Declined");
                responseLabel.setText("Reservation Declined");

                if (customerUI != null) {
                    customerUI.receiveNotification(
                        "Your reservation at " + pendingReservation.getRestaurant().getName() + " was declined."
                    );
                }

                // Disable buttons after decision
                acceptBtn.setEnabled(false);
                declineBtn.setEnabled(false);
            }
        });

        add(title);
        add(requestLabel);
        add(acceptBtn);
        add(declineBtn);
        add(responseLabel);

        setVisible(true);
    }

    public static void receiveReservation(Reservation r) {
        pendingReservation = r;
        if (requestLabel != null) {
            requestLabel.setText("Reservation from: " + r.getCustomer().getName() +
                    " at " + r.getTime());

            // Enable buttons only when new request comes in
            acceptBtn.setEnabled(true);
            declineBtn.setEnabled(true);
            responseLabel.setText("Status: Action required...");
        }
    }
}
