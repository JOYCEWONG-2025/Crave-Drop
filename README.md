# 🛵 GoodTech — Smart Food Delivery System

A campus logistics engine for food ordering, restaurant management, and rider assignment — built entirely on foundational data structures rather than frameworks or libraries.

| | |
|---|---|
| **Course** | WIA1002 Data Structures |
| **Runtime** | Java SE 17+ |
| **Interface** | Command-Line Interface (CLI) |

---

## 📖 Overview

GoodTech simulates a real delivery platform end-to-end — browsing restaurants, building a cart, checking out, and tracking a rider — using nothing but core data structures implemented from scratch:

| Data Structure | Where it's used |
|---|---|
| **Graph (Adjacency List) + Dijkstra's Algorithm** | Campus route tracing between locations |
| **AVL Tree** | Self-sorting, O(log n) restaurant menus |
| **Min-Heap Priority Queue** | Multi-criteria rider assignment |
| **Singly Linked List** | Live route tracking |
| **Stack (LIFO)** | Undo-last-item in the shopping cart |
| **Queue (FIFO)** | Order processing pipeline |

---

## 📁 Project Structure

All `.java` and `.txt` files must sit in the same directory — the project uses flat relative-path file I/O, so nothing will resolve correctly if files are split across folders.

```
Smart-Delivery-Food-System/
├── MainApplication.java     # Entry point & CLI menu loop
├── ManagementSystem.java    # Core controller (HashMap-based indexing)
├── CityMap.java             # Graph structure for campus locations
├── NavigationSystem.java    # Dijkstra shortest-path engine
├── DeliveryScheduler.java   # Rider allocation (Min-Heap wrapper)
├── MenuTree.java            # AVL Tree menu index
├── NodeLinkedList.java      # Custom singly linked list
├── Node.java                # Linked list element
├── FoodItem.java            # Menu item model
├── Restaurant.java          # Restaurant profile model
├── Rider.java               # Courier profile model
├── Order.java               # Finalized order/invoice model
├── OrderCart.java           # Stack-based cart handler
├── OrderItem.java           # Individual cart line item
├── User.java                # Customer profile model
├── Admin.java               # Admin profile model
├── users.txt                # Customer records
├── admin.txt                # Admin records
├── restaurants.txt          # Restaurant records
├── menus.txt                # Menu master list
└── rider.txt                # Courier records
```

---

## ▶️ Running the Project

### Option A — Terminal / Command Line
```bash
cd path/to/Smart-Delivery-Food-System
javac *.java
java MainApplication
```

### Option B — IDE (IntelliJ / Eclipse / NetBeans)
1. Open the IDE and choose **Open Project** / **Import Project**.
2. Select the `Smart-Delivery-Food-System` root folder.
3. Confirm the project SDK is set to **Java 17+**.
4. Locate `MainApplication.java` in the project tree.
5. Right-click → **Run 'MainApplication.main()'**.

---

## 🔑 Test Accounts

| Portal | ID | Password | Notes |
|---|---|---|---|
| Customer | `U101` | `123@123` | Maps to "Alice" at the Main Library node |
| Admin | `A001` | `A123@123` | Restaurant manager / admin access |

---

## 🧪 Sample Workflow (End-to-End Trace)

Use this sequence to exercise every data structure in one pass:

1. Open the **Customer Portal**, log in with `U102` / `y4512`.
2. Choose **Search Restaurants**, search `pizza`.
3. Select **Pizza Place** (Mid Valley Megamall Food Court).
4. View the menu — items are alphabetically sorted via the **AVL Tree**.
5. Add `Margherita Pizza` and `Garlic Bread` to your cart.
6. Trigger **Undo Last Item** — watch the **LIFO Stack** pop the last add.
7. **Checkout** — riders load from `rider.txt` into a **Min-Heap**, and the optimal candidate is auto-paired.
8. Select **Track Active Order** — **Dijkstra's algorithm** computes the path to your delivery node, and the rider's progress streams down a custom **NodeLinkedList**.

---

## 🌐 Bonus: Browser Demo

There's also a standalone, single-file **web demo** (`CraveDrop-web-demo.html`) that reimplements the same routing graph, Dijkstra pathing, and min-heap rider ranking logic in JavaScript, with a live-animated tracking map — useful for showing the project off without requiring Java installed. It's a client-side simulation only (no backend, no real GPS), meant as a portfolio companion piece to the CLI system above.

To view it, open the file directly in any browser, or deploy it as a static site (e.g. drag-and-drop onto [Netlify Drop](https://app.netlify.com/drop) or push to GitHub Pages).

---

## ⚠️ Notes

- Ensure `.txt` database files are writable — the system persists changes (new users, updated orders) back to these files.
- Requires Java 17+; earlier versions may fail on newer language syntax used across the codebase.
