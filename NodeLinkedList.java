public class NodeLinkedList<T> {
    private Node<String> head;
    private int size;

    public NodeLinkedList() {
        this.head = null;
        this.size = 0;
    }

    // Add path to the end of the linked list
    public void add(String path) {
        Node<String> newNode = new Node<>(path); // Assuming distance is not needed for the linked list

        if (head == null) {
            head = newNode;
        } else {
            Node<String> current = head;
            // Traverse down the references until we reach the last node
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // Remove an item from the list by matching its data value
    public boolean remove(String path) {
        if (head == null) 
            return false;

        // Special case: If the head node holds the item we want to remove
        if (head.getDestination().equals(path)) {
            head = head.next;
            size--;
            return true;
        }

        Node<String> current = head;
        while (current.next != null) {
            if (current.next.getDestination().equals(path)) {
                // Bypass the target node to unlink it from the chain
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false; // Item not found
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int getSize() {
        return size;
    }

    // Print the entire linked list chain cleanly
    public void printList() {
        if (head == null) {
            System.out.println("The list is empty.");
            return;
        }
        Node<String> current = head;
        while (current != null) {
            System.out.print(current.getDestination());
            current = current.next;
            if (current != null) System.out.print(" -> ");
        }
        System.out.println();
    }

    // Getter for the head node (useful if you need to loop through it manually elsewhere)
    public Node<String> getHead() {
        return head;
    }
}