# SmartStock – Inventory Management System

A full-stack **Inventory Management System** developed using **Java, Spring Boot, MySQL, HTML, CSS, and JavaScript**. SmartStock provides a centralized platform to manage products, categories, suppliers, and stock movements through an interactive dashboard.

The application helps businesses monitor inventory levels, track stock IN/OUT transactions, and identify products that require restocking through automated low-stock alerts.

## 🚀 Live Demo

**Live Application:**  
https://smart-stock-management-system.onrender.com/

## ✨ Features

- **Interactive Dashboard:** View total products, categories, suppliers, low-stock products, and recent stock movements.
- **Product Management:** Add, view, update, delete, and search inventory products.
- **Category Management:** Organize products into different categories.
- **Supplier Management:** Maintain supplier details, including name, email, phone, and address.
- **Stock IN/OUT Tracking:** Record incoming and outgoing stock transactions.
- **Low-Stock Alerts:** Identify products whose quantities are at or below their reorder levels.
- **Stock Movement History:** Maintain records of inventory transactions.
- **REST API Integration:** Perform inventory operations through RESTful API endpoints.
- **Database Integration:** Store and manage inventory information using MySQL.
- **Responsive Frontend:** Access the inventory dashboard through a web-based interface.

## 🛠️ Technology Stack

| Component | Technology |
|---|---|
| Programming Language | Java |
| Backend Framework | Spring Boot 3.3 |
| Data Access | Spring Data JPA |
| ORM | Hibernate |
| Database | MySQL 8 |
| Frontend | HTML5, CSS3, JavaScript |
| API Architecture | RESTful APIs |
| Validation | Bean Validation |
| Build Tool | Maven |
| Development IDE | IntelliJ IDEA |
| Containerization | Docker (if configured) |
| Version Control | Git & GitHub |
| Deployment | Render |

## 📂 Project Structure

```text
SmartStock/
│
├── pom.xml
├── database/
│   └── schema.sql
│
└── src/
    └── main/
        ├── java/
        │   └── com/smartstock/
        │       ├── SmartStockApplication.java
        │       ├── model/
        │       ├── repository/
        │       ├── service/
        │       ├── controller/
        │       ├── exception/
        │       └── config/
        │
        └── resources/
            ├── application.properties
            └── static/
                ├── index.html
                ├── css/
                │   └── style.css
                └── js/
                    └── app.js
```

## ⚙️ Prerequisites

Make sure the following software is installed:

- JDK 17 or newer
- Maven 3.9+
- MySQL Server 8
- IntelliJ IDEA, Eclipse, or Spring Tool Suite
- Git (optional, for cloning the repository)

## 🗄️ Database Configuration

SmartStock uses MySQL as its primary database.

### Step 1: Start MySQL Server

Make sure your MySQL server is running on port `3306`.

### Step 2: Configure Database Credentials

Open:

`src/main/resources/application.properties`

Configure your MySQL username and password.

Example:

```properties
DB_USERNAME=root
DB_PASSWORD=root
```

You can also configure these values using environment variables:

```text
DB_USERNAME
DB_PASSWORD
```

### Step 3: Database Initialization

The application is configured to use the `smartstock` database.

On first startup, database and table creation depends on the database configuration and initialization scripts.

If required, execute the SQL commands from:

```text
database/schema.sql
```

**Important:** Ensure the database exists and the configured MySQL credentials have the required permissions. Never commit real database passwords or sensitive credentials to GitHub.

## ▶️ Getting Started

Follow these steps to run SmartStock locally.

### Step 1: Clone the Repository

```bash
git clone https://github.com/kapoormayank9696/SmartStock.git
```

```bash
cd SmartStock
```

Replace the repository URL if your GitHub repository uses a different name or URL.

### Step 2: Configure MySQL

- Start MySQL Server.
- Configure database credentials in `application.properties`.
- Ensure the `smartstock` database is available or configured for automatic creation.

### Step 3: Build the Project

```bash
mvn clean install
```

### Step 4: Run the Application

```bash
mvn spring-boot:run
```

Alternatively, open the project in IntelliJ IDEA and run:

```text
SmartStockApplication.java
```

### Step 5: Open SmartStock

Once the application starts successfully, open your browser:

```text
http://localhost:8080
```

The frontend is served directly by Spring Boot from:

```text
src/main/resources/static/
```

## 🌐 REST API Documentation

SmartStock provides RESTful APIs for managing inventory operations.

**Base URL:**

```text
http://localhost:8080
```

### API Endpoints

#### Dashboard APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/dashboard` | Retrieve dashboard statistics |

#### Product APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/products` | Get all products |
| GET | `/api/products?search=keyword` | Search products |
| POST | `/api/products` | Create a product |
| GET | `/api/products/{id}` | Get product by ID |
| PUT | `/api/products/{id}` | Update a product |
| DELETE | `/api/products/{id}` | Delete a product |
| GET | `/api/products/low-stock` | Get low-stock products |

#### Category APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/categories` | Get all categories |
| POST | `/api/categories` | Create a category |
| PUT | `/api/categories/{id}` | Update a category |
| DELETE | `/api/categories/{id}` | Delete a category |

#### Supplier APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/suppliers` | Get all suppliers |
| POST | `/api/suppliers` | Create a supplier |
| PUT | `/api/suppliers/{id}` | Update a supplier |
| DELETE | `/api/suppliers/{id}` | Delete a supplier |

#### Stock Management APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/stock` | Get stock movement history |
| POST | `/api/stock` | Record stock IN/OUT movement |

## 📡 API Request Examples

The following examples demonstrate how to interact with SmartStock REST APIs using JSON and cURL.

### 1. Get Dashboard Statistics

```http
GET /api/dashboard
```

```bash
curl http://localhost:8080/api/dashboard
```

### 2. Get All Products

```http
GET /api/products
```

```bash
curl http://localhost:8080/api/products
```

Search products:

```bash
curl "http://localhost:8080/api/products?search=keyboard"
```

### 3. Create a Product

**Request:**

```http
POST /api/products
Content-Type: application/json
```

**JSON Body:**

```json
{
  "name": "Wireless Keyboard",
  "description": "Wireless keyboard with USB receiver",
  "quantity": 25,
  "reorderLevel": 5,
  "categoryId": 1,
  "supplierId": 1
}
```

**cURL:**

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Wireless Keyboard\",\"description\":\"Wireless keyboard with USB receiver\",\"quantity\":25,\"reorderLevel\":5,\"categoryId\":1,\"supplierId\":1}"
```

### 4. Get Low-Stock Products

```http
GET /api/products/low-stock
```

```bash
curl http://localhost:8080/api/products/low-stock
```

### 5. Create a Category

**Request:**

```http
POST /api/categories
Content-Type: application/json
```

**JSON Body:**

```json
{
  "name": "Electronics",
  "description": "Electronic devices and accessories"
}
```

**cURL:**

```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Electronics\",\"description\":\"Electronic devices and accessories\"}"
```

### 6. Create a Supplier

**Request:**

```http
POST /api/suppliers
Content-Type: application/json
```

**JSON Body:**

```json
{
  "name": "ABC Electronics",
  "email": "supplier@example.com",
  "phone": "9876543210",
  "address": "New Delhi"
}
```

**cURL:**

```bash
curl -X POST http://localhost:8080/api/suppliers \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"ABC Electronics\",\"email\":\"supplier@example.com\",\"phone\":\"9876543210\",\"address\":\"New Delhi\"}"
```

### 7. Record Stock IN Movement

**Request:**

```http
POST /api/stock
Content-Type: application/json
```

**JSON Body:**

```json
{
  "productId": 1,
  "type": "IN",
  "quantity": 10,
  "note": "New stock received"
}
```

### 8. Record Stock OUT Movement

**JSON Body:**

```json
{
  "productId": 1,
  "type": "OUT",
  "quantity": 2,
  "note": "Stock issued"
}
```

**cURL Example:**

```bash
curl -X POST http://localhost:8080/api/stock \
  -H "Content-Type: application/json" \
  -d "{\"productId\":1,\"type\":\"IN\",\"quantity\":10,\"note\":\"New stock received\"}"
```

**Note:** The IDs used in these examples are illustrative. Use valid IDs from your database. Request fields and accepted values must match the actual DTOs and validation rules implemented in your application.

## 🔄 How It Works

1. Users interact with the SmartStock dashboard.
2. Product, category, and supplier operations are sent to Spring Boot REST APIs.
3. Controllers receive and process incoming requests.
4. Service classes handle business logic and inventory operations.
5. Spring Data JPA communicates with the MySQL database.
6. Stock IN/OUT transactions update product quantities and maintain movement history.
7. Dashboard statistics and low-stock alerts help users monitor inventory.

## 🔮 Future Enhancements

- Barcode and QR-code scanning.
- Inventory reports and Excel/PDF export.
- Role-based authentication and authorization.
- Email and in-app low-stock notifications.
- Advanced inventory analytics and reporting.
- Product purchase and sales management.
- Improved dashboard visualizations.
- Automated backup and database recovery.

## 🎯 Project Objective

<<<<<<< HEAD
The primary objective of SmartStock is to simplify inventory management by providing an organized and centralized platform for tracking products, suppliers, categories, and stock movements.

The project demonstrates the practical implementation of **Java, Spring Boot, REST APIs, Spring Data JPA, and MySQL** in developing a real-world database-driven application.

## 👨‍💻 Author

**Mayank Kapoor**
=======
# Mayank Kapoor
## 👨‍💻 Author

**Mayank Kapoor**

<p align="left">
  <a href="mailto:kapoormayank645@gmail.com">
    <img src="https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white" alt="Email"/>
  </a>
  <a href="https://github.com/kapoormayank9696">
    <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub"/>
  </a>
  <a href="https://smart-stock-management-system.onrender.com/">
    <img src="https://img.shields.io/badge/Live%20Hosting-Render-46E3B7?style=for-the-badge&logo=render&logoColor=black" alt="Live Hosting"/>
  </a>
</p>
>>>>>>> 2e96421e4d8b9851b1398623ffcdac2ef3288a60

📧 **Email:** kapoormayank645@gmail.com
🌐 **GitHub:** https://github.com/kapoormayank9696
<a href="https://smart-stock-management-system.onrender.com/">
  <img src="https://img.shields.io/badge/Live%20Hosting-Render-46E3B7?style=for-the-badge&logo=render&logoColor=black" alt="Live Hosting"/>
</a> **Live Application:** https://smart-stock-management-system.onrender.com/

**Project:** SmartStock – Inventory Management System

**Developed Using:** Java, Spring Boot, MySQL, HTML, CSS, and JavaScript.

## 📄 License

This project is developed for educational and learning purposes.

Add an appropriate open-source license if you intend to distribute or reuse the project publicly.

## ⭐ Support

If you find this project useful:

- ⭐ Star the repository.
- 🍴 Fork the repository.
- 💻 Explore the source code.
- 🧠 Use it for learning and practice.

---

**Happy Coding! 🚀**