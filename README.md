Smart Grocery Management System (Java Swing)

A full-featured desktop-based Grocery Store Management System built using Java Swing and Object-Oriented Programming principles.
The system simulates real-world retail operations including inventory control, billing (POS), customer management, reporting, and notifications.

✨ Features
📦 Inventory Management
Add, update, and delete products
Support for perishable and non-perishable items
Real-time stock tracking
Low-stock and expiry alerts
Barcode-based product identification
Automatic data persistence using file handling
💳 Point of Sale (POS)
Barcode scanning checkout system
Automatic price calculation
Sales transaction recording
Inventory update after each sale
Multi-cashier support
👥 Customer Management
Customer registration and login
Role-based access control (Admin / Cashier / Customer)
Order history tracking
Loyalty points system
📊 Reports & Analytics
Top-selling products report
Daily / weekly / monthly sales reports
Inventory statistics overview
Business performance tracking
🔔 Notification System
Low stock alerts
Expiry warnings
Real-time GUI notifications
🧠 System Architecture

The project follows clean Object-Oriented Design principles:

MVC (Model–View–Controller) architecture
Separation of business logic and UI
Modular package structure
Reusable components and managers
File-based data persistence
🛠️ Tech Stack
Java (JDK 11+)
Java Swing (GUI Development)
Object-Oriented Programming (OOP)
File I/O (Data Storage)
Serialization
LocalDate API
📂 Project Structure
src/
├── model/        # Entity classes (Product, Customer, Order, etc.)
├── manager/      # Business logic layer
├── view/         # GUI (Swing UI screens)
├── utils/        # Helper classes (Barcode, File handling)
└── Main.java     # Application entry point
🚀 How to Run
1. Clone Repository
git clone https://github.com/Sidra-Hayat/smart-grocery-management-system-java.git
cd smart-grocery-management-system-java
2. Open in IDE
IntelliJ IDEA / Eclipse / NetBeans
Configure JDK 11+
3. Run Application

Run the Main.java file from your IDE.

👤 Default Login Credentials
Role	Username	Password
Admin	lara	lara123
Customer	rija	rija123
Cashier	ali	ali123
📸 Screenshots

Add screenshots in /screenshots folder:

Login Screen
Admin Dashboard
Inventory Management
POS System

Example:

screenshots/login.png
screenshots/dashboard.png
screenshots/inventory.png
screenshots/pos.png
📌 Limitations
File-based storage (no database integration yet)
GUI design can be further improved
No automated unit testing implemented
Hardcoded demo configuration data
🔮 Future Improvements
Database integration (MySQL / PostgreSQL)
Web-based version using Spring Boot
Barcode scanner hardware integration
AI-based product recommendations
PDF invoice generation
Multi-store support system
👨‍💻 Author

Sidra Hayat
GitHub: @Sidra-Hayat

⭐ Project Highlights
Real-world grocery store simulation
Complete POS (billing) system
Role-based authentication system
Inventory + reporting system
Clean OOP + MVC architecture
