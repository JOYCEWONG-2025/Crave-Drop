import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class DeliveryScheduler {

    // Priority Queue using Min Heap
    private PriorityQueue<Rider> riderQueue;

    // Persistent master registry list holding riders loaded from your text file database
    private List<Rider> registeredRiders = new ArrayList<>();

    private final String riderFile = "rider.txt"; // File to persist rider data

    // Store destination and nav for use in displayAllRiders()
    private String destination;
    private NavigationSystem nav;

    // Default Constructor
    // Used in MainApplication at system boot to initialize the database without a context
    public DeliveryScheduler() {
        loadRidersFromFile(); // Populate the registeredRiders list from the text file
    }

    // Constructor 2 
    // Used at active order checkout cycles to instantly compute structural proximity priorities
    public DeliveryScheduler(String destination, NavigationSystem nav) {
        this.destination = destination;
        this.nav = nav;
        // Primary Sort: Shortest Distance (Ascending)
        // Secondary Sort (Tie-breaker): Highest Rating (Descending)
        this.riderQueue = new PriorityQueue<>(
            Comparator.comparingDouble((Rider r) -> r.getCurrentDistance(destination, nav))
                    .thenComparing((r1, r2) -> Double.compare(r2.getRating(), r1.getRating())));
    }

    // Added Public Getter for Registered Riders List 
    // Allows MainApplication to grab the master roster and feed it into a checkout queue
    public List<Rider> getRegisteredRiders() {
        return registeredRiders;
    }

    public void loadRidersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(riderFile))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip blank lines
                
                String[] tokens = line.split(",");
                
                String id = tokens[0].trim(); 
                String name = tokens[1].trim();
                String location = tokens[2].trim();
                double rating = Double.parseDouble(tokens[3].trim());
                
                // Add the freshly parsed rider directly to this scheduler's master list
                this.registeredRiders.add(new Rider(id, name, location, rating));
            }
           
        } catch (IOException e) {
            System.out.println("Error loading riders file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Data formatting mismatch inside riders.txt: " + e.getMessage());
        }
    }

    //Saves the current state of registered riders back to the text file, ensuring persistence across sessions
    public void saveRidersToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(riderFile))) {
            for (Rider rider : registeredRiders) {
                String line = String.format("%s,%s,%s,%.1f", 
                                rider.getRiderID(), 
                                rider.getRiderName(), 
                                rider.getCurrentLocation(), 
                                rider.getRating());
                bw.write(line);
                bw.newLine();
            }
            
        } catch (IOException e) {
            System.out.println("Error saving riders to file: " + e.getMessage());
        }
    }
    
    // Register Rider Tool
    public void registerRider(Rider rider) {
        if (riderQueue != null) {
            riderQueue.offer(rider); // Feed the active sorting heap if one exists
        } else {
            registeredRiders.add(rider); // Fallback: add to base master storage database
        }
    }

    // Display All Riders in true Priority Order with distance ranking
    public void displayAllRiders() {
        if (riderQueue == null || riderQueue.isEmpty()) {
            System.out.println("No Riders Available.\n");
            return;
        }

        // Create a temporary copy to drain elements in order
        PriorityQueue<Rider> tempQueue = new PriorityQueue<>(riderQueue.comparator());
        tempQueue.addAll(riderQueue);

        // Show distance ranking table if destination context is available
        if (destination != null && nav != null) {
            System.out.println("=========================================");
            System.out.println("  MIN-HEAP RIDER RANKING");
            System.out.println("  Destination: " + destination);
            System.out.println("=========================================");
            System.out.printf("%-6s %-20s %-10s %-30s %-8s%n",
                    "Rank", "Rider Name", "Rating", "Current Location", "Distance");
            System.out.println("-----------------------------------------" +
                               "-----------------------------------------");

            int rank = 1;
            PriorityQueue<Rider> displayCopy = new PriorityQueue<>(tempQueue.comparator());
            displayCopy.addAll(tempQueue);

            while (!displayCopy.isEmpty()) {
                Rider r = displayCopy.poll();
                double dist = r.getCurrentDistance(destination, nav);
                String distStr = (dist == Double.MAX_VALUE) ? "Unreachable" : String.format("%.2f km", dist);
                System.out.printf("%-6d %-20s %-10.1f %-30s %-8s%n",
                        rank++, r.getRiderName(), r.getRating(), r.getCurrentLocation(), distStr);
            }
            System.out.println("=========================================");
            System.out.println("  #1 = Best Rider (Auto-selected by Heap)");
            System.out.println("=========================================\n");

        } else {
            // Fallback: no destination context, show basic info
            System.out.println("Available Riders (Sorted by Priority) :");
            while (!tempQueue.isEmpty()) {
                System.out.println(tempQueue.poll());
            }
            System.out.println();
        }
    }

    // View Best Rider Without Removing
    public void peekBestRider() {
        if (riderQueue == null || riderQueue.isEmpty()) {
            System.out.println("No Riders Available.\n");
            return;
        }
        System.out.println("Best Rider (Nearest Rider) :");
        System.out.println(riderQueue.peek());
        System.out.println();
    }

    // Assign Best Rider
    public Rider assignBestRider() {
        if (riderQueue == null || riderQueue.isEmpty()) {
            System.out.println("No Riders Available For Assignment.\n");
            return null;
        }

        Rider assignedRider = riderQueue.poll(); // Removes the top of the Min-Heap
        return assignedRider;
    }

    public void populateQueueWithRiders(List<Rider> masterRiders) {
        if (this.riderQueue == null) {
            System.out.println("Error: Queue is not initialized with a location context!");
            return;
        }
        
        for (Rider r : masterRiders) {
            this.riderQueue.offer(r);
        }
    }

    public void setRiderLocation(Rider rider, String newLocation) {
        rider.updateLocation(newLocation);
        saveRidersToFile(); // Persist the location update immediately
    }

    public void rateRider(Rider rider, double newRating) {
        newRating = Math.max(1.0, Math.min(5.0, newRating)); // Ensure rating is between 1 and 5
        double averageRating = (rider.getRating() + newRating) / 2; // Simple average for demo purposes
        rider.updateRating(averageRating);
        saveRidersToFile(); // Persist the rating update immediately
    }

}