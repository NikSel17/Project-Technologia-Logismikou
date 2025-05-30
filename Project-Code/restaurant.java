public class Restaurant {
    private int restaurantId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String openingHours;
    private String priceRange;
    private int capacity;
    private boolean status; // true = open, false = closed

    public Restaurant(int restaurantId, String name, String address, String phone, String email,
                      String openingHours, String priceRange, int capacity, boolean status) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.openingHours = openingHours;
        this.priceRange = priceRange;
        this.capacity = capacity;
        this.status = status;
    }

    public void restSearch() {
        // Search logic can be implemented here
        System.out.println("Searching restaurant: " + name);
    }

    // Getters and Setters
    public int getRestaurantId() { return restaurantId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getOpeningHours() { return openingHours; }
    public String getPriceRange() { return priceRange; }
    public int getCapacity() { return capacity; }
    public boolean isOpen() { return status; }
}