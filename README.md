# Smart Grocery Management System (Java Swing)

A full-featured desktop-based Grocery Store Management System built using **Java Swing** and **Object-Oriented Programming** principles. The system simulates real-world retail operations including inventory control, billing (POS), customer management, reporting, and notifications.

---

## ✨ Features

### 📦 Inventory Management
- Add, update, and delete products
- Support for perishable and non-perishable items
- Real-time stock tracking
- Low-stock and expiry alerts
- Automatic data persistence using file handling

### 💳 Point of Sale (POS)
- Cashier checkout system
- Automatic price calculation
- Sales transaction recording
- Inventory update after each sale
- Multi-cashier support

### 👥 Customer Management
- Customer registration and login
- Role-based access control (Admin / Cashier / Customer)
- Order history tracking with timestamps
- Loyalty points system

### 📊 Reports & Analytics
- Top-selling products report
- Sales summary reports
- Inventory statistics overview
- Business performance tracking

### 🔔 Notification System
- Low stock alerts
- Expiry warnings
- Real-time GUI notifications

---

## 🧠 System Architecture

The project follows clean Object-Oriented Design principles:

- MVC (Model–View–Controller) architecture
- Separation of business logic and UI
- Modular package structure
- Reusable components and managers
- File-based data persistence

---

## 🛠️ Tech Stack

- Java (JDK 11+)
- Java Swing (GUI Development)
- Object-Oriented Programming (OOP)
- File I/O (Data Storage)
- LocalDate API

---

## 📂 Project Structure

```
src/
├── main/      # Application entry point (App.java)
├── model/     # Entity classes (Product, Customer, Order, etc.)
├── manager/   # Business logic layer
├── view/      # GUI (Swing UI screens)
└── listener/  # Event listeners
```

---

## 🚀 How to Run

### 1. Clone Repository
```
git clone https://github.com/Sidra-Hayat/smart-grocery-management-system-java.git
cd smart-grocery-management-system-java
```

### 2. Open in IDE
- IntelliJ IDEA / Eclipse / NetBeans
- Configure JDK 11+

### 3. Run Application
Run `src/main/App.java` from your IDE (this is the application entry point).

---

## 👤 Default Login Credentials

| Role     | Username | Password |
|----------|----------|----------|
| Admin    | lara     | lara123  |
| Customer | rija     | rija123  |
| Cashier  | ali      | ali123   |

---

## 📸 Screenshots

### Login
![Login](screenshots/login_ui.png)

### Admin Dashboard
![Admin Dashboard](screenshots/admin_ui.png)

### Customer Dashboard
![Customer Dashboard](screenshots/customer_dashboard.png)

### Customer Profile
![Customer Profile](screenshots/customer_profile.png)

### Customer Registration
![Customer Registration](screenshots/customer_register.png)

### Cashier Dashboard
![Cashier Dashboard](screenshots/cashier_dashboard.png)

### Manage Customers
![Manage Customers](screenshots/manage_customers.png)

### Approve Customers
![Approve Customers](screenshots/approve_customers.png)

### Manage Cashiers
![Manage Cashiers](screenshots/manage_cashiers.png)

### Add Product
![Add Product](screenshots/addproduct_manager.png)

### View Products (Customer View)
![View Products](screenshots/viewproducts_bycustomer.png)

### Top Selling Report
![Top Selling Report](screenshots/topselling_report.png)

### Urgent Notifications
![Urgent Notifications](screenshots/urgent_notification.png)

### place order 
![place order](screenshots/place_order.png)
###  order History
![order history](screenshots/order_history.png)
---

## 📌 Limitations

- File-based storage (no database integration yet)
- GUI design can be further improved
- No automated unit testing implemented
- Hardcoded demo configuration data

---

## 🔮 Future Improvements

- Database integration (MySQL / PostgreSQL)
- Web-based version using Spring Boot
- AI-based product recommendations
- PDF invoice generation
- Multi-store support system

---

## 👨‍💻 Author

**Sidra Hayat**
GitHub: [@Sidra-Hayat](https://github.com/Sidra-Hayat)

---

## ⭐ Project Highlights

- Real-world grocery store simulation
- Complete POS (billing) system
- Role-based authentication system
- Inventory + reporting system
- Clean OOP + MVC architecture