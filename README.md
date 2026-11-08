# SmartStock – Inventory Management System

A web-based inventory management system built with **Spring Boot and MySQL**. SmartStock helps users manage products, categories, suppliers, and stock movements from a single dashboard. It also provides low-stock alerts to help identify products that may need restocking.

## Features

- **Dashboard:** View inventory totals, low-stock products, and recent stock movements.
- **Product Management:** Create, view, update, delete, and search products.
- **Category Management:** Organize products into categories.
- **Supplier Management:** Maintain supplier information.
- **Stock In/Out Tracking:** Record stock received and stock issued, with movement history.
- **Low-Stock Alerts:** Identify products at or below their reorder level.
- **REST API:** Access inventory features through `/api/...` endpoints.

## Technology Stack

| Component | Technology |
|---|---|
| Backend | Java, Spring Boot 3.3 |
| Data Access | Spring Data JPA |
| Validation | Bean Validation |
| Database | MySQL 8 |
| Frontend | HTML, CSS, JavaScript |
| API Style | REST API |
| Build Tool | Maven |

The frontend is served by Spring Boot from `src/main/resources/static`.

## Requirements

- JDK 17 or newer
- Maven 3.9+ (or IntelliJ IDEA, Eclipse, or STS)
- MySQL 8 running on `localhost:3306`

## Getting Started

### 1. Configure the database

Open `src/main/resources/application.properties` and set your MySQL username and password.

Default credentials:

```properties
DB_USERNAME=root
DB_PASSWORD=root
```

You can also configure the credentials using the `DB_USERNAME` and `DB_PASSWORD` environment variables.

> Replace the default credentials with your own MySQL credentials. Do not commit real passwords or secrets to GitHub.

### 2. Run the application

Open a terminal in the project folder and run:

```bash
mvn spring-boot:run
```

You can also open the project in IntelliJ IDEA, Eclipse, or STS and run `SmartStockApplication`.

### 3. Open SmartStock

Visit:

```text
http://localhost:8080
```

On first startup, the application creates the `smartstock` database and its tables. A few sample products are also added so the dashboard is not empty.

## REST API

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/dashboard` | Get dashboard totals, low-stock products, and recent movements |
| GET | `/api/products` | List products |
| GET | `/api/products?search=keyword` | Search products |
| POST | `/api/products` | Create a product |
| GET | `/api/products/{id}` | Get a product by ID |
| PUT | `/api/products/{id}` | Update a product |
| DELETE | `/api/products/{id}` | Delete a product |
| GET | `/api/products/low-stock` | List products at or below their reorder level |
| GET | `/api/categories` | List categories |
| POST | `/api/categories` | Create a category |
| PUT | `/api/categories/{id}` | Update a category |
| DELETE | `/api/categories/{id}` | Delete a category |
| GET | `/api/suppliers` | List suppliers |
| POST | `/api/suppliers` | Create a supplier |
| PUT | `/api/suppliers/{id}` | Update a supplier |
| DELETE | `/api/suppliers/{id}` | Delete a supplier |
| GET | `/api/stock` | View stock movement history |
| POST | `/api/stock` | Record a stock IN or OUT movement |

## Project Structure

```text
smartstock/
├── pom.xml
├── database/
│   └── schema.sql
└── src/
    └── main/
        ├── java/com/smartstock/
        │   ├── model/
        │   ├── repository/
        │   ├── service/
        │   ├── controller/
        │   ├── exception/
        │   └── config/
        └── resources/
            ├── application.properties
            └── static/
                ├── index.html
                ├── css/
                │   └── style.css
                └── js/
                    └── app.js
```
## API Request Examples

Base URL:

```text
http://localhost:8080
```

### 1. Get Dashboard

```http
GET /api/dashboard
```

```bash
curl http://localhost:8080/api/dashboard
```

### 2. List Products

```http
GET /api/products
```

```bash
curl http://localhost:8080/api/products
```

Search products by keyword:

```bash
curl "http://localhost:8080/api/products?search=keyboard"
```

### 3. Create a Product

```http
POST /api/products
Content-Type: application/json
```

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

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Wireless Keyboard\",\"description\":\"Wireless keyboard with USB receiver\",\"quantity\":25,\"reorderLevel\":5,\"categoryId\":1,\"supplierId\":1}"
```

> This is an example request body. Your actual field names and required fields must match the DTO or request model used by your application.

### 4. Get Low-Stock Products

```http
GET /api/products/low-stock
```

```bash
curl http://localhost:8080/api/products/low-stock
```
---

### 5. Create a Category

```http
POST /api/categories
Content-Type: application/json
```

```json
{
  "name": "Electronics",
  "description": "Electronic devices and accessories"
}
```

```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Electronics\",\"description\":\"Electronic devices and accessories\"}"
```

### 6. Create a Supplier

```http
POST /api/suppliers
Content-Type: application/json
```

```json
{
  "name": "ABC Electronics",
  "email": "supplier@example.com",
  "phone": "9876543210",
  "address": "New Delhi"
}
```

```bash
curl -X POST http://localhost:8080/api/suppliers \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"ABC Electronics\",\"email\":\"supplier@example.com\",\"phone\":\"9876543210\",\"address\":\"New Delhi\"}"
```

### 7. Record a Stock Movement

```http
POST /api/stock
Content-Type: application/json
```

Example request for stock received (IN):

```json
{
  "productId": 1,
  "type": "IN",
  "quantity": 10,
  "note": "New stock received"
}
```

Example request for stock issued (OUT):

```json
{
  "productId": 1,
  "type": "OUT",
  "quantity": 2,
  "note": "Stock issued"
}
```

```bash
curl -X POST http://localhost:8080/api/stock \
  -H "Content-Type: application/json" \
  -d "{\"productId\":1,\"type\":\"IN\",\"quantity\":10,\"note\":\"New stock received\"}"
```

> The product, category, and supplier IDs in these examples are illustrative. Use IDs that exist in your database. Request field names and allowed stock movement values must match your application's DTOs and validation rules.

---
## How It Works

1. Users manage products, categories, and supplier details through the web interface.
2. Product and inventory requests are sent to the Spring Boot REST API.
3. Spring Data JPA interacts with the MySQL database.
4. Stock IN and OUT movements update inventory quantities and are recorded in the movement history.
5. The dashboard displays inventory summaries and products that need restocking.

## Future Enhancements

- Barcode or QR-code scanning
- Inventory reports and data export
- User roles and access control
- Email or in-app low-stock notifications
- Advanced sales and stock analytics

## Author
Developed using Spring Boot and REST API architecture for efficient SmartStock management.

# Mayank Kapoor

📧 Email: kapoormayank645@gmail.com

🌐 GitHub: https://github.com/kapoormayank9696

Live Host : https://smart-stock-management-system.onrender.com/

**Project:** SmartStock – Inventory Management System  
**Developed using:** Spring Boot, MySQL, HTML, CSS, and JavaScript

## License

Add a license to this repository if you intend to distribute or reuse the project. Otherwise, specify the usage terms you prefer.

---

# ⭐ Support

If you find this project helpful:

⭐ Star the repository

🍴 Fork it

🧠 Use it for learning and practice

---

# Happy Coding! 🚀

---
