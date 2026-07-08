import java.util.*;
import java.util.Collections;

class Edge {
    String target;
    double distance;

    public Edge(String target, double distance) {
        this.target = target;
        this.distance = distance;
    }
}

public class CityMap {
    // Adjacency List: Key is the Location Name, Value is the list of Roads
    private Map<String, List<Edge>> adjacencyList = new HashMap<>();

    public void addLocation(String name) {
        adjacencyList.putIfAbsent(name, new ArrayList<>());
    }

    public void addRoad(String source, String destination, double dist, boolean isOneWay) {
        adjacencyList.get(source).add(new Edge(destination, dist));
        if (!isOneWay) {
            adjacencyList.get(destination).add(new Edge(source, dist));
        }
    }

    public Map<String, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public List<String> getLocations() {
        List<String> locs = new ArrayList<>(adjacencyList.keySet());
        Collections.sort(locs);
        return locs;
    }

    public List<Edge> getRoadsFrom(String location) {
        return adjacencyList.get(location) == null ? new ArrayList<>() : adjacencyList.get(location); // return the list
                                                                                                      // of roads
                                                                                                      // (edges) from a
                                                                                                      // given location
                                                                                                      // [destination,distance]
    }

}