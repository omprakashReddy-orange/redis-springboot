# Product Management Application

A complete Spring Boot REST API for product management with full CRUD operations. This application is ready for Redis caching integration in your learning journey.

## Architecture Overview

The application follows a layered architecture pattern:

```
Controller Layer (REST endpoints)
    ↓
Service Layer (Business logic)
    ↓
Repository Layer (Data access)
    ↓
Entity Layer (Database models)
```

## Project Structure

```
src/main/java/com/app/product/
├── entity/
│   └── Product.java              # JPA Entity with auto timestamps
├── repository/
│   └── ProductRepository.java    # Spring Data JPA repository
├── service/
│   └── ProductService.java       # Business logic and validations
├── controller/
│   └── ProductController.java    # REST API endpoints
├── dto/
│   ├── CreateProductDTO.java     # Request DTO for creation
│   ├── UpdateProductDTO.java     # Request DTO for updates
│   └── ProductResponseDTO.java   # Response DTO
├── exception/
│   ├── ProductNotFoundException.java
│   ├── ProductAlreadyExistsException.java
│   ├── GlobalExceptionHandler.java
│   └── ErrorResponse.java
└── ProductApplication.java       # Spring Boot main class
```

## Key Features

### 1. Product Entity
- **Fields:** id, name, description, price, quantity, category, createdAt, updatedAt
- **Auto-Timestamping:** Automatic creation and update timestamps
- **Unique Constraint:** Product names must be unique

### 2. REST API Endpoints

#### Create Product
```
POST /api/products
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High performance laptop",
  "price": 1299.99,
  "quantity": 10,
  "category": "Electronics"
}

Response: 201 Created with ProductResponseDTO
```

#### Get All Products
```
GET /api/products

Response: 200 OK with List<ProductResponseDTO>
```

#### Get Single Product
```
GET /api/products/{id}

Response: 200 OK with ProductResponseDTO
         404 Not Found if product doesn't exist
```

#### Update Product
```
PUT /api/products/{id}
Content-Type: application/json

{
  "price": 1199.99,
  "quantity": 8
}

Response: 200 OK with updated ProductResponseDTO
         404 Not Found if product doesn't exist
         409 Conflict if new name already exists
```

#### Delete Product
```
DELETE /api/products/{id}

Response: 204 No Content
         404 Not Found if product doesn't exist
```

## Validation Rules

### CreateProductDTO
- **name**: Required, 1-255 characters, must be unique
- **price**: Required, must be > 0
- **quantity**: Required, must be >= 0
- **category**: Required, 1-100 characters
- **description**: Optional, max 1000 characters

### UpdateProductDTO
- All fields are optional (partial updates supported)
- Same validation rules apply if fields are provided

## Error Handling

All errors return standardized error responses:

```json
{
  "status": 404,
  "message": "Product with id 999 not found",
  "timestamp": "2026-07-03T14:30:00"
}
```

### Exception Types
- **ProductNotFoundException** → 404 Not Found
- **ProductAlreadyExistsException** → 409 Conflict
- **Validation Errors** → 400 Bad Request with field errors
- **General Errors** → 500 Internal Server Error

## Database Configuration

Edit `application.properties` to set your MySQL connection:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/product_db
spring.datasource.username=root
spring.datasource.password=root
```

Database will be auto-created with `spring.jpa.hibernate.ddl-auto=update`

## Automatic Data Seeding

The application includes a **DataSeeder** component that automatically populates the database with 10 sample products on first startup.

### Sample Data Included

1. **Laptop** - $1,299.99 (15 in stock)
2. **Wireless Mouse** - $29.99 (50 in stock)
3. **USB-C Cable** - $12.99 (100 in stock)
4. **Monitor 27 inch** - $499.99 (20 in stock)
5. **Mechanical Keyboard** - $149.99 (30 in stock)
6. **Webcam HD** - $59.99 (25 in stock)
7. **Phone Stand** - $14.99 (60 in stock)
8. **HDMI Cable** - $19.99 (80 in stock)
9. **Power Bank** - $39.99 (40 in stock)
10. **Desk Lamp** - $44.99 (35 in stock)

### How It Works

- On application startup, the DataSeeder checks if the database is empty
- If empty, it automatically seeds 10 sample products
- If products already exist, seeding is skipped (idempotent)
- Console output: `✅ Database seeded with 10 sample products!`

### Start Testing Immediately

After running the application, you can immediately:
- Get all products: `GET /api/products`
- Test with real data
- No need to manually create products first

Location: `src/main/java/com/app/product/config/DataSeeder.java`

## Building and Running

### Build
```bash
mvn clean compile
```

### Run
```bash
mvn spring-boot:run
```

Application will start on `http://localhost:8080` with sample data ready to use!

## Testing with cURL

```bash
# Create product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Keyboard",
    "description": "Mechanical keyboard",
    "price": 149.99,
    "quantity": 25,
    "category": "Accessories"
  }'

# Get all products
curl http://localhost:8080/api/products

# Get specific product
curl http://localhost:8080/api/products/1

# Update product
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"price": 129.99}'

# Delete product
curl -X DELETE http://localhost:8080/api/products/1
```

## Why No Redis Yet?

This application is intentionally built **without Redis** to provide you with:

1. **Clean baseline** - Focus on core application logic first
2. **Proper service abstraction** - Service layer is perfectly positioned for caching
3. **Easy integration point** - Add Redis to ProductService.getAllProducts() or getProductById() in your next phase
4. **Learning foundation** - Understand the non-cached version before optimization
5. **Visible latency** - 500ms artificial delay on read operations demonstrates why caching is needed

### Simulated Slow Database Call

The service includes a `simulateSlowDbCall()` method that adds a **500ms delay** to all read operations:
- **Purpose:** Shows the response time WITHOUT caching
- **Applied to:** `getProductById()` and `getAllProducts()`
- **Benefit:** You'll immediately see the dramatic improvement when Redis caching is added
- **Will be removed:** Once you implement Redis caching in your next learning phase

This is a common pattern in demo applications to clearly demonstrate caching benefits!

## Next Steps: Redis Integration

When you're ready to learn Redis, you'll add:
1. Spring Data Redis dependency
2. Redis connection configuration
3. @Cacheable annotations on read methods
4. Cache eviction strategies on write methods
5. **You'll then REMOVE the simulateSlowDbCall()** - caching will make it unnecessary!

## Dependencies

- Spring Boot 4.1.0
- Spring Data JPA
- Spring WebMVC
- Spring Validation
- MySQL Connector/J
- Lombok
- Java 21

## Notes

- Uses constructor injection (best practice)
- Transactional services with @Transactional annotation
- Lombok reduces boilerplate code
- Proper separation of concerns
- DTOs for API contracts
- Global exception handling
