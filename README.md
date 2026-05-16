# Smart Grocery Management System

![Java](https://img.shields.io/badge/Java-11+-orange?style=flat-square&logo=java)
![Swing GUI](https://img.shields.io/badge/GUI-Swing-blue?style=flat-square)
![Status](https://img.shields.io/badge/Status-Active-green?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-lightgrey?style=flat-square)

> A comprehensive **Java-based inventory management system** for grocery stores with GUI, featuring real-time notifications, cashier operations, customer management, and advanced reporting.

## 🌟 Key Features

### 📦 **Inventory Management**
- ✅ Add, update, and delete products
- ✅ Perishable and non-perishable product categorization
- ✅ Real-time stock tracking and low-stock notifications
- ✅ Dynamic pricing based on expiry dates
- ✅ Barcode scanning support
- ✅ Automated inventory persistence

### 💳 **Cashier Operations**
- ✅ Point of Sale (POS) system
- ✅ Barcode-based checkout
- ✅ Sales transaction recording
- ✅ Multiple cashier support
- ✅ Cashier management and authentication

### 👥 **Customer Management**
- ✅ Customer registration and approval workflow
- ✅ Loyalty points system
- ✅ Order history tracking
- ✅ Customer authentication and profile management
- ✅ Role-based access control (Admin, Cashier, Customer)

### 📊 **Reporting & Analytics**
- ✅ Sales reports
- ✅ Top-selling products analysis
- ✅ Inventory statistics
- ✅ Daily/weekly/monthly reports
- ✅ Performance metrics

### 🔔 **Notification System**
- ✅ Low-stock alerts
- ✅ Expiry date warnings
- ✅ Real-time GUI notifications
- ✅ Urgent notification dialogs
- ✅ Automated notification generation

### 🔐 **Security & Access Control**
- ✅ Role-based authentication (Admin, Cashier, Customer)
- ✅ Secure login system
- ✅ User registration with approval
- ✅ Password protection
- ✅ Admin authorization checks

## 🛠️ Tech Stack

| Technology | Purpose |
|-----------|---------|
| **Java 11+** | Core language |
| **Swing** | GUI framework |
| **File I/O** | Data persistence |
| **LocalDate** | Date/time management |
| **Serialization** | Object persistence |

## 📂 Project Structure

```
smart-grocery-management-system-java/
├── src/
│   ├── Main.java                 # Application entry point
│   ├── main/
│   │   └── App.java             # Alternative entry point
│   ├── model/                    # Domain models
│   │   ├── Product.java
│   │   ├── PerishableProduct.java
│   │   ├── NonPerishableProduct.java
│   │   ├── Customer.java
│   │   ├── Cashier.java
│   │   ├── Admin.java
│   │   ├── Person.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── Notification.java
│   │   ├── TopSellingReport.java
│   │   └── AIRecommender.java
│   ├── manager/                  # Business logic
│   │   ├── InventoryManager.java
│   │   ├── CashierManager.java
│   │   ├── CustomerManager.java
│   │   ├── LoginManager.java
│   │   ├── NotificationManager.java
│   │   ├── OrderHistoryManager.java
│   │   └── ReportManager.java
│   ├── view/                     # GUI components
│   │   ├── LoginUI.java
│   │   ├── CustomerUI.java
│   │   ├── CashierUI.java
│   │   ├── AdminUI.java
│   │   ├── PlaceOrderUI.java
│   │   ├── ProductManagerUI.java
│   │   ├── NotificationsUI.java
│   │   └── [more UI classes...]
│   ├── utils/                    # Utility classes
│   │   ├── FileHandler.java
│   │   └── BarcodeScanner.java
│   └── icons/                    # GUI icons/assets
├── products.txt                  # Product inventory file
├── cashiers.txt                  # Cashier data file
├── users.txt                     # User accounts file
├── sales.txt                     # Sales transaction log
└── README.md                     # This file

```

## 🚀 Getting Started

### Prerequisites
- **Java Development Kit (JDK)** 11 or higher
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code with Java extensions)
- **Git** for version control

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Sidra-Hayat/smart-grocery-management-system-java.git
   cd smart-grocery-management-system-java
   ```

2. **Open in your IDE:**
   - Open the project folder in IntelliJ IDEA, Eclipse, or your preferred IDE
   - Ensure JDK 11+ is configured

3. **Run the application:**
   ```bash
   javac -d . src/**/*.java
   java Main
   ```

   Or simply run `Main.java` from your IDE

### First Run
- The system seeds demo data on first launch:
  - **Admin Account:** Username: `lara Admin` | Password: `lara123`
  - **Customer Account:** Username: `rija` | Password: `rija123`
  - **Cashier Account:** Username: `ali` | Password: `ali123`
  - **20 demo products** (perishable and non-perishable)

## 👤 User Roles

### 🔒 **Admin**
- Manage inventory (add/edit/delete products)
- View complete reports
- Manage cashiers and customers
- System administration

### 💼 **Cashier**
- Process sales transactions
- Scan barcodes
- Record payments
- View daily sales

### 🛍️ **Customer**
- Browse products
- Place orders
- View order history
- Track loyalty points
- Access notifications

## 📖 Usage Examples

### Adding a Product (Admin)
1. Login as Admin
2. Select "Add Product"
3. Enter product details (ID, name, price, quantity)
4. Choose perishable/non-perishable
5. Set expiry date or shelf life
6. Product is automatically saved

### Processing a Sale (Cashier)
1. Login as Cashier
2. Enter/scan product barcode
3. Enter quantity
4. Confirm transaction
5. System records sale and updates inventory

### Placing an Order (Customer)
1. Login as Customer
2. Browse available products
3. Add items to cart
4. View order summary
5. Proceed to checkout
6. Order history is saved

## 🔧 Configuration

### Data Files
- `products.txt` - Inventory storage (pipe-delimited format)
- `cashiers.txt` - Cashier accounts
- `users.txt` - Customer and admin accounts
- `sales.txt` - Transaction log

### Modifying Seed Data
Edit the `seedDataIfEmpty()` method in `Main.java` to customize demo data.

## 📊 Database Format

### Product Format (products.txt)
```
PERISHABLE|P001|Milk|16.0|50|2026-05-23|890123456001
NONPERISHABLE|NP001|Rice|300|100|2028-05-16|890223456001
```

### User Format (users.txt)
```
ADMIN|9293|lara|lara Admin|lara123|50000
CUSTOMER|rija|rija|rija Customer|rija123|0|true
CASHIER|C001|ali|Ali Raza|ali123|30000
```

## 🐛 Known Issues & TODO

### Current Limitations
- [ ] GUI polish needed (as per description "will work on GUI later")
- [ ] Duplicate `loadProducts()` call in Main.java (lines 27, 32)
- [ ] Price calculation methods need consolidation
- [ ] Missing comprehensive error handling
- [ ] No unit tests implemented
- [ ] Hardcoded configuration values

### Planned Improvements
- [ ] Database integration (MySQL/PostgreSQL)
- [ ] RESTful API backend
- [ ] Web-based interface
- [ ] Mobile app support
- [ ] Advanced reporting (PDF export)
- [ ] Multi-store support
- [ ] AI-based recommendations

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Create a feature branch:**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** and commit:
   ```bash
   git add .
   git commit -m "feat: description of changes"
   ```

3. **Push to your branch:**
   ```bash
   git push origin feature/your-feature-name
   ```

4. **Open a Pull Request** to `develop` branch

### Development Branches
- `main` - Production-ready code
- `develop` - Integration branch for features
- `feature/*` - New feature development
- `bugfix/*` - Bug fixes

## 📋 Commit Message Guidelines

```
<type>: <subject>

<body>

<footer>
```

**Types:** feat, fix, docs, style, refactor, perf, test, chore

**Example:**
```
fix: remove duplicate loadProducts() call

- Removed redundant inventory load on line 32
- Improves startup performance
- Closes #123
```

## 📝 License

This project is licensed under the **MIT License** - see the LICENSE file for details.

## 👨‍💻 Author

**Sidra Hayat**
- GitHub: [@Sidra-Hayat](https://github.com/Sidra-Hayat)
- Repository: [smart-grocery-management-system-java](https://github.com/Sidra-Hayat/smart-grocery-management-system-java)

## 🎯 Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2026-05-16 | Initial release with full inventory & GUI |

## 📞 Support & Contact

For issues, bugs, or feature requests, please:
- 📬 Open an [Issue](https://github.com/Sidra-Hayat/smart-grocery-management-system-java/issues)
- 💬 Start a [Discussion](https://github.com/Sidra-Hayat/smart-grocery-management-system-java/discussions)
- 📧 Contact maintainer

## 🎓 Learning Resources

This project demonstrates:
- ✅ Object-oriented programming (OOP) principles
- ✅ Design patterns (MVC, Observer, Singleton)
- ✅ File I/O and data persistence
- ✅ GUI development with Swing
- ✅ Business logic and workflow management
- ✅ Role-based access control
- ✅ Git workflow and version control

## 🏆 Acknowledgments

- Java community for excellent documentation
- Swing framework for robust GUI capabilities
- Contributors and testers

---

<div align="center">

**⭐ If you find this project helpful, please give it a star!**

Made with ❤️ by [Sidra Hayat](https://github.com/Sidra-Hayat)

</div>
