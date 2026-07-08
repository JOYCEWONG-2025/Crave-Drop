public class Node<T> {
    private String destination;
    Node<T> next;
    double distance;

    public Node(String destination) {
        this.destination = destination;
        this.next = null;
    }

    public String getDestination() { 
        return destination; 
    }

    public double getDistance() { 
        return distance; 
    }
    
}