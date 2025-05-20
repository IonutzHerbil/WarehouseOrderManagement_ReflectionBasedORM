This application provides a comprehensive solution for managing warehouse orders, implementing a sophisticated layered architecture with a custom reflection-based Object-Relational Mapping (ORM) system.
Key Features

Client Management: Add, edit, view, and delete client records
Product Management: Track product inventory with stock management
Order Processing: Create orders with automatic stock validation and updates
Immutable Billing: Generate immutable bill records for each order

Technical Implementation
Layered Architecture
The application strictly follows a layered architecture pattern:

Presentation Layer: User interface components in the presentation package
Business Logic Layer: Application logic in the businessLayer package
Data Access Layer: Database interactions in the dataAccessLayer package
Model Layer: Data entities in the model package

Advanced Java Techniques
This project demonstrates several advanced Java programming techniques:
1. Custom Reflection-Based ORM
The heart of the data access layer is a custom Object-Relational Mapping system built using Java's reflection API:

Generic AbstractDAO: A powerful generic class for database operations
Dynamic SQL Generation: SQL queries are generated at runtime based on object structure
Automatic Object Mapping: Database records are converted to Java objects via reflection
Naming Convention Handling: Automatic conversion between camelCase (Java) and snake_case (Database)

2. Java Records for Immutable Data
The application uses Java records for immutable bill information:

Immutable Data Storage: Bills are created once and never modified
Component-Based Access: Accessing data through record components
Factory Method Pattern: Static factory methods for bill creation

3. Functional Programming with Streams API
The code leverages modern Java features for cleaner, more concise data processing:

Stream Operations: Filtering, mapping, and collecting data
Lambda Expressions: Concise function implementations
Method References: Elegant syntax for method invocation

4. Design Patterns
Several design patterns are implemented throughout the application:

Singleton Pattern: For database connection management
Factory Method Pattern: For creating database connections and bills
Data Access Object (DAO) Pattern: For database operations
Model-View-Controller (MVC) Pattern: For UI organization

Database Integration
The application connects to a PostgreSQL database, handling:

Connection Pooling: Efficient connection management
Prepared Statements: SQL injection prevention
Transaction Management: Ensuring data integrity
Auto-generated Keys: Retrieving database-generated IDs

Getting Started
Prerequisites

Java 17 or higher
PostgreSQL 13 or higher
Maven

Database Setup

Create a PostgreSQL database named warehousedb
Run the SQL script in database/schema.sql to create the necessary tables

Configuration
Edit ConnectionFactory.java to set your database credentials:
javaprivate static final String DBURL = "jdbc:postgresql://localhost:5432/warehousedb";
private static final String USER = "your_username";
private static final String PASS = "your_password";
Building and Running
bashmvn clean package
java -jar target/warehouse-order-management.jar
Architecture Overview
This application demonstrates a sophisticated implementation of layered architecture:
src/main/java/
├── businessLayer/      # Business logic
├── dataAccessLayer/    # Database access with reflection-based ORM
├── model/              # Data models including Java records
├── presentation/       # User interface components
└── Main.java           # Application entry point
The custom reflection-based ORM is particularly noteworthy, providing generic CRUD operations while maintaining clean separation between database access and business logic. This allows the system to evolve with minimal coupling between layers.
Key Innovations

Dynamic Query Generation: SQL queries are built at runtime based on object structure
Type-Safe Operations: Leveraging Java generics for type safety in database operations
Flexible Data Mapping: Automatic mapping between database and Java object structures
Immutable Audit Records: Using Java records for immutable bill data


This project demonstrates advanced Java techniques including reflection, generics, streams, and records to create a robust, maintainable warehouse management system with clean architectural boundaries.
