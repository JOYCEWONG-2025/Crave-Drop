import java.util.ArrayList;
import java.util.List;

public class MenuTree {

    private class AVLNode {
        FoodItem item;
        int height;
        AVLNode left;
        AVLNode right;

        AVLNode(FoodItem item) {
            this.item = item;
            this.height = 1;
            this.left = null;
            this.right = null;
        }
    }

    private AVLNode root;
    private int size;

    public MenuTree() {
        this.root = null;
        this.size = 0;
    }

    // Insert food item — O(log n)
    public void insertFood(FoodItem item) {
        root = insert(root, item);
        size++;
    }

    // Search food by name — O(log n)
    public FoodItem searchByName(String name) {
        return search(root, name);
    }

    // Search food by keyword — O(n)
    public List<FoodItem> searchByKeyword(String keyword) {
        List<FoodItem> matches = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return matches;
        }
        // Run a recursive traversal to collect partial matching items
        keywordSearchHelper(root, keyword.toLowerCase().trim(), matches);
        return matches;
    }

    // Background helper for partial keyword search
    private void keywordSearchHelper(AVLNode node, String keyword, List<FoodItem> matches) {
        if (node == null) return;

        // A. Scan the left branch
        keywordSearchHelper(node.left, keyword, matches);

        // B. Process current node using case-insensitive partial substring matching
        String currentFoodName = node.item.getFoodName().toLowerCase();
        if (currentFoodName.contains(keyword)) {
            matches.add(node.item);
        }

        // C. Scan the right branch
        keywordSearchHelper(node.right, keyword, matches);
    }

    // Display all food items sorted A-Z — in-order traversal O(n)
    public void displaySortedMenu() {
        if (root == null) {
            System.out.println("  [Menu is empty]");
            return;
        }
        System.out.println("\n  " + String.format("%-25s | %-12s | %s", "Food Name", "Category", "Price"));
        System.out.println("  " + "-".repeat(58));
        inOrder(root);
        System.out.println("  " + "-".repeat(58));
        System.out.println("  Total items: " + size);
    }

    // Load all menu items from a restaurant into the AVL Tree
    public void loadFromRestaurant(Restaurant restaurant) {
        root = null;
        size = 0;
        for (FoodItem item : restaurant.getMenuItems()) {
            insertFood(item);
        }
    }

     public int getSize() {
         return size;
     }

     public boolean isEmpty() {
         return size == 0;
      }

    // Recursive insert — returns updated subtree root after rebalancing
    private AVLNode insert(AVLNode node, FoodItem item) {
        if (node == null)
            return new AVLNode(item);

        int cmp = item.getFoodName().compareToIgnoreCase(node.item.getFoodName());

        if (cmp < 0) {
            node.left = insert(node.left, item);
        } else if (cmp > 0) {
            node.right = insert(node.right, item);
        } else {
            size--;
            return node;
        }

        updateHeight(node);
        return rebalance(node);
    }

    // Recursive search — eliminates half the tree at each step
    private FoodItem search(AVLNode node, String name) {
        if (node == null)
            return null;

        int cmp = name.compareToIgnoreCase(node.item.getFoodName());

        if (cmp == 0)
            return node.item;
        else if (cmp < 0)
            return search(node.left, name);
        else
            return search(node.right, name);
    }

    // In-order traversal: left -> root -> right (produces A-Z output)
    private void inOrder(AVLNode node) {
        if (node == null)
            return;
        inOrder(node.left);
        System.out.println("  " + node.item.toString());
        inOrder(node.right);
    }

    private int height(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    private void updateHeight(AVLNode node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    // Balance factor = height(left) - height(right), AVL allows only {-1, 0, +1}
    private int getBalance(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    // Right child climbs up, current node drops to the left
    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode B = y.left;

        y.left = x;
        x.right = B;

        updateHeight(x);
        updateHeight(y);
        return y;
    }

    // Left child climbs up, current node drops to the right
    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode B = x.right;

        x.right = y;
        y.left = B;

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    // Check balance factor and apply the correct rotation
    private AVLNode rebalance(AVLNode node) {
        int balance = getBalance(node);

        // Left-Left: single right rotation
        if (balance > 1 && getBalance(node.left) >= 0)
            return rotateRight(node);

        // Left-Right: left rotate child first, then right rotate root
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right-Right: single left rotation
        if (balance < -1 && getBalance(node.right) <= 0)
            return rotateLeft(node);

        // Right-Left: right rotate child first, then left rotate root
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }
}