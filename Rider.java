public class Rider {

    private String riderID;
    private String riderName;
    private String currentLocation;
    private double rating;

    // Constructor
    public Rider(String riderID, String riderName, String currentLocation, double rating) {
        this.riderID = riderID;
        this.riderName = riderName;
        this.currentLocation = currentLocation;
        this.rating = rating;
    }

    // Getters
    public String getRiderID() {
        return riderID;
    }

    public String getRiderName() {
        return riderName;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public double getRating() {
        return rating;
    }

    public void setRiderID(String riderID) {
        this.riderID = riderID;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void updateLocation(String newLocation) {
        this.currentLocation = newLocation;
    }

    public void updateRating(double newRating) {
        this.rating = newRating;
    }

    public double getCurrentDistance(String destination, NavigationSystem nav) {
        return nav.calculateShortestPath(currentLocation, destination);
    }

    // Display Rider Information
    @Override
    public String toString() {
        
        return "\n[Rider Profile]" +
                "\nID      : " + riderID +
                "\nName    : " + riderName +
                "\nLocation: " + currentLocation +
                "\nRating  : " + rating;
    }
}