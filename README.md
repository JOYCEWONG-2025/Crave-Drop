# Smart-Delivery-Food-System
========================================================================
           GOODTECH SMART FOOD DELIVERY SYSTEM - USER MANUAL
========================================================================
Course Code : WIA1002 Data Structures
Deadline    : 12 June 2026
Target Environment: Java SE 17 or higher
Console Interface: Command Line Interface (CLI)

------------------------------------------------------------------------
1. PROJECT OVERVIEW
------------------------------------------------------------------------
The GOODTECH Smart Food Delivery System is a high-performance campus 
logistics engine built to streamline food ordering, restaurant management, 
and rider assignment across a campus layout grid. 

The system implements advanced, foundational data structures to optimize 
everyday operational layers:
- Graph Adjacency Lists & Dijkstra's Algorithm for campus route tracing.
- Height-Balanced AVL Trees for self-sorting, O(log n) menu systems.
- Min-Heap Priority Queues for multi-criteria rider assignments.
- Custom Singly Linked Lists for dynamic route tracking vectors.
- Stacks (LIFO) and Queues (FIFO) for order tracking and cart workflows.

------------------------------------------------------------------------
2. PACKAGE DIRECTORY STRUCTURE
------------------------------------------------------------------------
Ensure all source files (.java) and text database files (.txt) reside 
in the same directory level so that flat-file streams map smoothly:

WIA1002_GroupProject/
├── MainApplication.java       # Main entry point and CLI menu portal loops
├── ManagementSystem.java     # Core system controller (HashMap indexing)
├── CityMap.java               # Graph database structure for map coordinates
├── NavigationSystem.java     # Shortest path engine (Dijkstra implementation)
├── DeliveryScheduler.java    # Rider allocation pipeline (Min-Heap wrapper)
├── MenuTree.java              # Self-balancing binary menu index (AVL Tree)
├── NodeLinkedList.java        # Custom singly linked list implementation
├── Node.java                  # Singly linked chain element template
├── FoodItem.java              # Restaurant menu item structure
├── Restaurant.java            # Restaurant profile data layout
├── Rider.java                 # Delivery courier profile data layout
├── Order.java                 # Finalized customer invoice transaction wrapper
├── OrderCart.java             # Stack-based customer shopping cart handler
├── OrderItem.java             # Individual transaction item reference
├── User.java                  # Registered customer profile data layout
├── Admin.java                 # Authorized system administrator profile
├── users.txt                  # Persistent customer database records
├── admin.txt                  # Persistent system administrator database records
├── restaurants.txt            # Persistent restaurant database records
├── menus.txt                  # Persistent restaurant menu master list inventory
└── rider.txt                  # Persistent courier database records

------------------------------------------------------------------------
3. COMPILATION AND EXECUTION INSTRUCTIONS
------------------------------------------------------------------------
You can run this program either via a standard terminal window or by 
loading the project directly into any popular Java Integrated Development 
Environment (IDE) such as IntelliJ IDEA, Eclipse, or NetBeans.

Option A: Running via Terminal / Command Line
1. Open your Terminal (macOS/Linux) or Command Prompt (Windows).
2. Navigate into the directory containing your project source code files:
   cd path/to/SMART-DELIVERY-FOOD-SYSTEM
3. Compile all Java source files concurrently:
   javac *.java
4. Launch the executable binary main class run frame:
   java MainApplication

Option B: Running via an IDE (IntelliJ / Eclipse / NetBeans)
1. Open your IDE and select "Open Project" or "Import Project".
2. Select the base "WIA1002_GroupProject" root folder.
3. Verify that your IDE project settings are targeted to Java 17 SDK or higher.
4. Locate 'MainApplication.java' in your project navigation pane.
5. Right-click 'MainApplication.java' and choose "Run 'MainApplication.main()'".

------------------------------------------------------------------------
4. PRE-CONFIGURED TEST PROFILES
------------------------------------------------------------------------
To evaluate and test system features immediately without registering 
new profiles manually, use these default text file entries:

A. Customer Portal Access:
   - User ID  : U101
   - Password : 123@123
   (Profile maps automatically to 'Alice' at the 'Main Library' node)

B. Restaurant Manager & Admin Portal Access:
   - Admin ID : A001
   - Password : A123@123

------------------------------------------------------------------------
5. CORE WORKFLOW EVALUATION TRACE
------------------------------------------------------------------------
To view the full power of the underlying data structures, perform this 
standard tracking sequence:
1. Choose option 1 (Customer Portal) and log in with user ID 'U102' and password 'y4512'.
2. Select option 2 to search for restaurants. Enter the keyword 'pizza'.
3. Select 'Pizza Place' (located at Mid Valley Megamall Food Court).
4. Choose option 1 to view the menu. Notice that the items are sorted 
   alphabetically (A-Z) via the internal AVL Tree.
5. Choose option 3 and add 'Margherita Pizza' and 'Garlic Bread' to your cart.
6. Trigger option 4 (Undo Last Item) to watch the LIFO Stack pop the last item.
7. Confirm your checkout via option 5. The system will load the available couriers 
   from 'rider.txt' into a Min-Heap, instantly pairing the optimal candidate.
8. Select option 6 (Track Active Order). The Navigation System will trigger 
   Dijkstra's algorithm, compute the path to the 'Main Library', and simulate 
   the rider's live tracking progress down your custom NodeLinkedList.