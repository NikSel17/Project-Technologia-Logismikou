import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CustomerUI extends JFrame {
    private Customer customer;
    private List<City> cities;
    private List<Restaurant> allRestaurants;

    private JPanel chooseCityPanel;
    private JPanel restaurantsPanel;
    private JPanel reservationPanel;

    private JComboBox<String> cityCombo;
    private JButton searchButton;

    private DefaultListModel<String> restaurantListModel;
    private JList<String> restaurantList;
    private JButton backToCityButton;

    private JLabel restaurantNameLabel;
    private JButton createReservationButton;
    private JButton backToRestaurantsButton;
    private JLabel statusLabel;

    private String selectedCity;
    private Restaurant selectedRestaurant;

    public CustomerUI(Customer customer, List<City> cities, List<Restaurant> allRestaurants) {
        this.customer = customer;
        this.cities = cities;
        this.allRestaurants = allRestaurants;

        setTitle("Restaurant Reservation (Smartphone)");
        setSize(360, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        initChooseCityPanel();
        initRestaurantsPanel();
        initReservationPanel();

        setContentPane(chooseCityPanel);

        setVisible(true);
    }

    private void initChooseCityPanel() {
        chooseCityPanel = new JPanel(new BorderLayout(10, 10));
        chooseCityPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Choose Your City");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        chooseCityPanel.add(title, BorderLayout.NORTH);

        cityCombo = new JComboBox<>();
        for (City c : cities) {
            cityCombo.addItem(c.getName());
        }
        chooseCityPanel.add(cityCombo, BorderLayout.CENTER);

        searchButton = new JButton("Search");
        chooseCityPanel.add(searchButton, BorderLayout.SOUTH);

        searchButton.addActionListener((ActionEvent e) -> {
            selectedCity = (String) cityCombo.getSelectedItem();
            showRestaurantsForCity(selectedCity);
        });
    }

    private void initRestaurantsPanel() {
        restaurantsPanel = new JPanel(new BorderLayout(10, 10));
        restaurantsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Restaurants in City");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        restaurantsPanel.add(title, BorderLayout.NORTH);

        restaurantListModel = new DefaultListModel<>();
        restaurantList = new JList<>(restaurantListModel);
        restaurantList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        restaurantsPanel.add(new JScrollPane(restaurantList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        backToCityButton = new JButton("Back");
        bottomPanel.add(backToCityButton, BorderLayout.WEST);
        restaurantsPanel.add(bottomPanel, BorderLayout.SOUTH);

        backToCityButton.addActionListener(e -> setContentPane(chooseCityPanel));

        restaurantList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && restaurantList.getSelectedIndex() != -1) {
                String selectedName = restaurantList.getSelectedValue();
                selectedRestaurant = allRestaurants.stream()
                        .filter(r -> r.getName().equals(selectedName))
                        .findFirst()
                        .orElse(null);

                if (selectedRestaurant != null) {
                    showReservationPage();
                }
            }
        });
    }

    private void initReservationPanel() {
        reservationPanel = new JPanel(new BorderLayout(10, 10));
        reservationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        restaurantNameLabel = new JLabel("", SwingConstants.CENTER);
        restaurantNameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        reservationPanel.add(restaurantNameLabel, BorderLayout.NORTH);

        createReservationButton = new JButton("Create Reservation");
        reservationPanel.add(createReservationButton, BorderLayout.CENTER);

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        reservationPanel.add(statusLabel, BorderLayout.SOUTH);

        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backToRestaurantsButton = new JButton("Back");
        topButtons.add(backToRestaurantsButton);
        reservationPanel.add(topButtons, BorderLayout.WEST);

        backToRestaurantsButton.addActionListener(e -> {
            statusLabel.setText(" ");
            setContentPane(restaurantsPanel);
        });

        createReservationButton.addActionListener(e -> {
            if (selectedRestaurant != null) {
                Reservation reservation = new Reservation(1, LocalDate.now().plusDays(1), LocalTime.of(19, 0), customer, selectedRestaurant);
                customer.makeReservation(reservation);
                RestaurantUI.receiveReservation(reservation);

                statusLabel.setText("Reservation created for " + selectedRestaurant.getName());
            }
        });
    }

    private void showRestaurantsForCity(String city) {
        restaurantListModel.clear();
        List<Restaurant> filtered = allRestaurants.stream()
                .filter(r -> r.getAddress().contains(city))
                .collect(Collectors.toList());

        for (Restaurant r : filtered) {
            restaurantListModel.addElement(r.getName());
        }

        setContentPane(restaurantsPanel);
        revalidate();
        repaint();
    }

    private void showReservationPage() {
        if (selectedRestaurant != null) {
            restaurantNameLabel.setText(selectedRestaurant.getName());
            statusLabel.setText(" ");
            setContentPane(reservationPanel);
            revalidate();
            repaint();
        }
    }

    public void receiveNotification(String message) {
    JOptionPane.showMessageDialog(this, message, "Reservation Update", JOptionPane.INFORMATION_MESSAGE);

    statusLabel.setText(message);
    }


    private static List<City> loadCitiesFromFile(String filename) {
        List<City> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int id = 1;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    cities.add(new City(id++, parts[0].trim(), parts[1].trim(), Integer.parseInt(parts[2].trim()), parts[3].trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cities;
    }

    private static List<Restaurant> loadRestaurantsFromFile(String filename) {
        List<Restaurant> restaurants = new ArrayList<>();
        Random rand = new Random();
        List<String> greekCities = Arrays.asList("Athens", "Thessaloniki", "Patras", "Heraklion");

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int id = 1;
            while ((line = br.readLine()) != null) {
                String name = line.trim();
                String city = greekCities.get(rand.nextInt(greekCities.size()));
                restaurants.add(new Restaurant(id++, name, city + ", Greece", "123456", name.toLowerCase().replace(" ", "") + "@mail.com",
                        "10:00–22:00", "$$", 50, true));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    public static void main(String[] args) {
        List<City> cities = loadCitiesFromFile("cities.txt");
        List<Restaurant> restaurants = loadRestaurantsFromFile("restaurants.txt");

        Customer customer = new Customer(1, "Alice", "Ali", "123456", "alice@mail.com");

        new CustomerUI(customer, cities, restaurants);
        new RestaurantUI();
    }
}
