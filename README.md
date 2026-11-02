# SmartStock – Inventory Management (Spring Boot + MySQL)

Products, categories, suppliers, stock in/out tracking and low-stock alerts.

- **Backend:** Spring Boot 3.3, Spring Data JPA, Bean Validation, REST API (`/api/...`)
- **Database:** MySQL 8 (tables are created automatically)
- **Frontend:** HTML + CSS + JavaScript, served by Spring Boot from `src/main/resources/static`

## Requirements
- JDK 17 or newer
- Maven 3.9+ (or open the folder in IntelliJ IDEA / Eclipse / STS and run `SmartStockApplication`)
- MySQL 8 running on `localhost:3306`

## Run
1. Open `src/main/resources/application.properties` and set your MySQL username and password
   (defaults: `root` / `root`), or set the environment variables `DB_USERNAME` and `DB_PASSWORD`.
2. From this folder run:
   ```
   mvn spring-boot:run
   ```
3. Open http://localhost:8080

The database `smartstock` and all tables are created on first start, and a few sample
products are added so the dashboard is not empty.

## REST API
| Method | URL | What it does |
|---|---|---|
| GET | `/api/dashboard` | Totals, low-stock list, recent movements |
| GET / POST | `/api/products` (`?search=`) | List / create products |
| GET / PUT / DELETE | `/api/products/{id}` | One product |
| GET | `/api/products/low-stock` | Products at or below reorder level |
| GET / POST | `/api/categories` | List / create categories |
| PUT / DELETE | `/api/categories/{id}` | Update / delete |
| GET / POST | `/api/suppliers` | List / create suppliers |
| PUT / DELETE | `/api/suppliers/{id}` | Update / delete |
| GET / POST | `/api/stock` | Movement history / record stock IN or OUT |

## Folder structure
```
smartstock/
├─ pom.xml
├─ database/schema.sql
└─ src/main
   ├─ java/com/smartstock
   │  ├─ model  repository  service  controller  exception  config
   └─ resources
      ├─ application.properties
      └─ static  (index.html, css/style.css, js/app.js)
```
