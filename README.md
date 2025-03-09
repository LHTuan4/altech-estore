# altech-estore

# Project Architecture

Java 17+, Spring Boot, Maven<br />
RESTful API-<br />
In-memory H2 database<br />
Concurrent-safe<br />
JUnit and integration testing<br />

# Key Components

Domain Entities<br />
Repositories<br />
Services<br />
Controllers<br />
Comprehensive Error Handling<br />
Thread-safe<br />

# Technology Stack

Java 17+<br />
Spring Boot 3.x<br />
Spring Data JPA<br />
H2 Database<br />
JUnit 5<br />
Mockito<br />

# Admin endpoint
pre_path = "/admin/api/v1"<br />
GET {pre_path}/product/get - Get product by Id<br />
POST {pre_path}/product/add - Add new product<br />
POST {pre_path}/product/remove - Remove a product<br />
POST {pre_path}/discount/add - Add new discount<br />
POST {pre_path}/discount/assign - Assign a discount to product discounts<br />

# User endpoint
pre_path = "/user/api/v1"<br />
GET {pre_path}/basket/get - Get basket by Id<br />
POST {pre_path}/basket/add - Add new basket<br />
POST {pre_path}/basket/item/add - Add an item to basket<br />
POST {pre_path}/basket/item/remove - Remove an item from basket<br />
GET {pre_path}/basket/receipt - Get basket receipt<br />

# Contact
Email: lhtuan1095@gmail.com<br />
LinkedIn: https://www.linkedin.com/in/lhtuan1095


