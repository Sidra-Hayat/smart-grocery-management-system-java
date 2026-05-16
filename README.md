🛒 Smart Grocery Management System

A Java-based desktop Grocery Store Management System built using Java Swing and Object-Oriented Programming principles.
The system simulates real-world supermarket operations including inventory control, billing (POS), customer management, and reporting.

✨ Key Features
📦 Inventory Management
Add, update, and delete products
Support for perishable and non-perishable items
Automatic stock tracking
Expiry date-based pricing adjustments
Barcode-based product identification
File-based persistent storage
💳 Point of Sale (POS)
Barcode scanning for quick checkout
Cart-based billing system
Automatic inventory update after sale
Sales transaction recording
Multi-cashier support
👥 Customer Management
Customer registration and authentication
Role-based access (Admin, Cashier, Customer)
Loyalty points system
Order history tracking
📊 Reports & Analytics
Top-selling products report
Daily and monthly sales reports
Inventory statistics
Business performance tracking
🔔 Notification System
Low-stock alerts
Expiry warnings for products
Real-time GUI notifications
🧠 System Architecture

The project is designed using Object-Oriented Programming (OOP) and follows a modular structure:

MVC Pattern (Model – View – Controller separation)
Encapsulation for data protection
Reusable business logic layers
File-based persistence (no database)
🛠️ Tech Stack
Java (JDK 11+)
Java Swing (GUI Development)
Object-Oriented Programming (OOP)
File I/O (Data Storage)
LocalDate API
📂 Project Structure
src/
├── model/        # Core entities (Product, Customer, Order, etc.)
├── manager/      # Business logic (Inventory, Sales, Reports)
├── view/         # GUI screens (Swing UI)
├── utils/        # Helper classes (File handling, Barcode scanner)
└── Main.java     # Application entry point
🚀 How to Run
1. Clone Repository
git clone https://github.com/Sidra-Hayat/smart-grocery-management-system-java.git
cd smart-grocery-management-system-java
2. Open in IDE
IntelliJ IDEA / Eclipse / NetBeans
Ensure JDK 11 or higher is installed
3. Run Project

Run the file:

Main.java
👤 Default Login Credentials
Role	Username	Password
Admin	lara	lara123
Customer	rija	rija123
Cashier	ali	ali123
📸 Screenshots

Add screenshots here after running the project

screenshots/login.png
screenshots/dashboard.png
screenshots/inventory.png
screenshots/pos.png
📌 Current Limitations
File-based storage (no database integration yet)
GUI design needs further UI/UX improvements
No automated testing implemented
Hardcoded demo configuration data
🔮 Future Enhancements
MySQL / PostgreSQL database integration
Web-based version using Spring Boot
Barcode scanner hardware integration
AI-based product recommendations
PDF invoice generation
Multi-store support system
👨‍💻 Author

Sidra Hayat
GitHub: @Sidra-Hayat

⭐ Project Highlights

✔ Real-world grocery store simulation
✔ Fully functional POS system
✔ Role-based authentication system
✔ Modular OOP architecture
✔ Inventory + reporting system

🟢 WHY THIS VERSION IS PERFECT FOR YOUR PORTFOLIO

This version gives you:

✔ Clean recruiter scan (5–10 seconds understanding)
✔ Strong technical signal (OOP + MVC + Swing)
✔ No unnecessary clutter
✔ Professional tone (not “assignment looking”)
✔ Clear structure (used in real company repos)
