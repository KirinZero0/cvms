# CVMS - Candidate Vacancy Management System

A Spring Boot REST API application for managing candidates and vacancies with intelligent candidate ranking based on customizable criteria.

## Features

- **Candidate Management**: Create, read, update, and delete candidates
- **Vacancy Management**: Manage job vacancies with flexible criteria
- **Intelligent Ranking**: Automatically rank candidates for vacancies based on matching criteria
- **Pagination Support**: All listing endpoints support pagination
- **Validation**: Comprehensive input validation and error handling
- **MongoDB Integration**: NoSQL database for flexible data storage

## Technology Stack

- **Backend**: Spring Boot 3.x
- **Database**: MongoDB
- **Validation**: Jakarta Bean Validation
- **Build Tool**: Maven
- **Java Version**: 21+

## Getting Started

### Prerequisites

- Java 21 or higher
- MongoDB (local or cloud instance)
- Maven 3.6+

### Installation

1. Clone the repository
2. Configure MongoDB connection in `src/main/resources/application.properties`
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Running Tests

The project includes comprehensive unit and integration tests using JUnit 5 and Mockito.

**Run all tests:**
```bash
./mvnw test
```

**Run tests with detailed output:**
```bash
./mvnw test -Dtest.verbose=true
```

**Run specific test class:**
```bash
./mvnw test -Dtest=CandidateServiceTest
```

**Run specific test method:**
```bash
./mvnw test -Dtest=CandidateServiceTest#createCandidate_Success
```

**Run tests for specific package:**
```bash
./mvnw test -Dtest=com.jobseeker.cvms.demo.service.*
```

**Test reports** are generated in `target/surefire-reports/` after running tests.

### API Documentation

Complete API documentation with examples and testing interface is available at:

**📚 [API Documentation - Postman](https://documenter.getpostman.com/view/38965260/2sB3BLjnZV)**

## API Overview

### Candidate Endpoints
- `POST /api/candidates` - Create new candidate
- `GET /api/candidates` - List all candidates (paginated)
- `GET /api/candidates/{id}` - Get candidate by ID
- `PUT /api/candidates/{id}` - Update candidate
- `DELETE /api/candidates/{id}` - Delete candidate

### Vacancy Endpoints
- `POST /api/vacancies` - Create new vacancy
- `GET /api/vacancies` - List all vacancies (paginated)
- `GET /api/vacancies/{id}` - Get vacancy by ID
- `PUT /api/vacancies/{id}` - Update vacancy
- `DELETE /api/vacancies/{id}` - Delete vacancy
- `GET /api/vacancies/{id}/ranking` - Get ranked candidates for vacancy

## Key Features

### Candidate Ranking System
The system automatically scores and ranks candidates for vacancies based on configurable criteria:
- **Age Criteria**: Match candidates within age ranges
- **Gender Criteria**: Filter by gender preferences
- **Salary Range Criteria**: Match salary expectations
- **Weighted Scoring**: Each criterion has customizable weights

### Database Optimization
- **MongoDB Indexing**: Strategic indexing for improved query performance
  - Email uniqueness index on candidates collection
  - Compound indexes on frequently queried fields (age, gender, salary)
  - ID indexes for fast document retrieval
  - Performance optimized for pagination and filtering operations

### Extensibility for Future Criteria
The system is designed with extensibility in mind, allowing easy addition of new criteria types:

- **Strategy Pattern Implementation**: Uses abstract `Criteria` base class with concrete implementations (`AgeCriteria`, `GenderCriteria`, `SalaryRangeCriteria`)
- **Polymorphic Design**: New criteria types can be added by simply extending the `Criteria` class and implementing the `matches()` method
- **JSON Serialization Support**: MongoDB's document structure naturally accommodates new criteria fields
- **Weight-Based Scoring**: New criteria automatically inherit the weighted scoring system

**Example**: To add `ExperienceCriteria`:
1. Add `EXPERIENCE` to the `CriteriaType` enum
2. Add `@JsonSubTypes.Type(value = ExperienceCriteria.class, name = "EXPERIENCE")` to the `Criteria` class
3. Create the new `ExperienceCriteria` class extending `Criteria` and implementing the `matches()` method

### Scalability for Ranking Process
The system addresses performance challenges for large-scale candidate ranking:

#### Current Optimizations:
- **Pagination**: Built-in pagination prevents loading entire datasets into memory
- **Database Indexing**: Strategic indexes on commonly filtered fields (age, gender, salary)
- **Stream Processing**: Uses Java Streams for memory-efficient candidate processing

#### Future Scalability Approaches:

1. **Database-Level Filtering**
   - Pre-filter candidates at database level using MongoDB aggregation pipelines
   - Reduce data transfer between database and application layers
   - Implement criteria-specific MongoDB queries

2. **Caching Strategies**
   - Redis caching for frequently accessed vacancy criteria
   - Cache ranking results for popular vacancies
   - Implement cache invalidation on candidate updates

3. **Asynchronous Processing**
   - Background job processing for large ranking operations
   - Message queues (RabbitMQ/Apache Kafka) for handling ranking requests
   - Real-time notifications when rankings are complete

4. **Horizontal Scaling**
   - Microservice architecture to separate ranking service
   - Load balancing across multiple ranking service instances
   - Database read replicas for ranking queries

5. **Advanced Optimizations**
   - Elasticsearch integration for complex search and ranking
   - Machine learning models for predictive candidate scoring
   - Batch processing during off-peak hours

## Project Structure

```
src/main/java/com/jobseeker/cvms/demo/
├── controller/          # REST API controllers
├── service/            # Business logic layer
├── repository/         # Data access layer
├── model/              # Entity models
├── dto/                # Data transfer objects
├── exception/          # Exception handling
└── config/             # Configuration classes
```

## Contributing

This project was developed by:
- **Author**: Arya Permana - Kirin
- **GitHub**: https://github.com/KirinZero0
- **Copyright**: 2025
