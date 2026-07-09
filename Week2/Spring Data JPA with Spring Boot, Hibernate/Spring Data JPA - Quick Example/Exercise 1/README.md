# Hands-on 1: Spring Data JPA - Quick Example (`orm-learn`)

## Project Overview
This project demonstrates **Spring Data JPA** and **Hibernate ORM** integrated with **Spring Boot 2.7.x** (`javax.persistence.*`). It covers configuring data sources, entity mapping, repository abstraction (`JpaRepository`), service layer transactions (`@Transactional`), and console logging with Logback/SLF4J.

---

## 1. Directory Structure & SME Walkthrough

### `src/main/java` - Folder with Application Code
Contains the core business logic, entity definitions, data access layer, and Spring Boot entry point:
* **`com.cognizant.ormlearn.model.Country`**: Persistence Entity class mapping the database table `country`.
* **`com.cognizant.ormlearn.repository.CountryRepository`**: Spring Data JPA repository extending `JpaRepository<Country, String>`.
* **`com.cognizant.ormlearn.service.CountryService`**: Service layer encapsulating data access calls and marked with `@Transactional`.
* **`com.cognizant.ormlearn.OrmLearnApplication`**: Main bootstrap class.

### `src/main/resources` - Folder for Application Configuration
Contains externalized configuration and database init scripts:
* **`application.properties`**: Defines MySQL JDBC connection details, Hibernate DDL settings (`validate`), dialect (`MySQL5Dialect`), and logging levels (`trace` for SQL queries/parameters).
* **`schema.sql` & `data.sql`**: SQL scripts for schema verification and default data pre-population.

### `src/test/java` - Folder with Code for Testing the Application
Contains unit and integration tests:
* **`com.cognizant.ormlearn.OrmLearnApplicationTests`**: Uses `@SpringBootTest` to bootstrap the application context and verify that `CountryService` beans load and retrieve records accurately without manual container startup.

---

## 2. Technical Walkthrough Checklist

### `OrmLearnApplication.java` - `main()` Method Walkthrough
```java
public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(OrmLearnApplication.class, args);
    LOGGER.info("Inside main");

    countryService = context.getBean(CountryService.class);
    testGetAllCountries();
}
```
1. **`SpringApplication.run(OrmLearnApplication.class, args)`**: Bootstraps the application, starts the Spring IoC container (`ApplicationContext`), loads all `@Configuration` definitions, connects to MySQL via `HikariCP` connection pool, and initializes JPA EntityManagerFactory.
2. **`context.getBean(CountryService.class)`**: Retrieves the autowired `CountryService` singleton instance from the container.
3. **`testGetAllCountries()`**: Executes the verification method to call `countryService.getAllCountries()`, triggering a SQL `SELECT` query via Hibernate and logging the returned `List<Country>`.

### Purpose of `@SpringBootApplication` Annotation
`@SpringBootApplication` is a meta-annotation that combines three core Spring Boot capabilities:
1. **`@Configuration`**: Designates the class as a source of Spring bean definitions.
2. **`@EnableAutoConfiguration`**: Instructs Spring Boot to automatically configure `DataSource`, `EntityManagerFactory`, `JpaTransactionManager`, and other necessary infrastructure beans based on classpath dependencies (`spring-boot-starter-data-jpa` + `mysql-connector-java`).
3. **`@ComponentScan`**: Enables component scanning across `com.cognizant.ormlearn` and all sub-packages, discovering `@Service`, `@Repository`, and `@Component` annotations automatically.

### `pom.xml` Walkthrough & Dependency Hierarchy
* **`<parent> spring-boot-starter-parent (2.7.18)`**: Provides centralized dependency management, version moderation, and pre-configured build plugins.
* **`spring-boot-starter-data-jpa`**: Brings in the core data persistence stack:
  * `hibernate-core`: The JPA 2.2 / ORM implementation (`javax.persistence.*`).
  * `spring-data-jpa`: High-level repository abstraction (`JpaRepository`).
  * `spring-jdbc` & `HikariCP`: JDBC connection pooling and data access utilities.
  * `spring-aspects` / `spring-aop`: Declarative transaction management (`@Transactional`).
* **`mysql-connector-java`**: MySQL JDBC 8.0 driver required to communicate with MySQL Server 8.0.
* **`spring-boot-devtools`**: Provides hot swapping and live reloading during development.
* **`slf4j-api` & `logback-classic`**: Included via Spring Boot starter logging for console output formatting.

---

## 3. Database Setup Instructions (MySQL Server / Workbench 8.0)

Execute the provided SQL script `sql/00_schema_setup.sql` or run the following commands in MySQL Client:

```sql
-- Create Schema
CREATE SCHEMA IF NOT EXISTS ormlearn;
USE ormlearn;

-- Create Country Table
CREATE TABLE country (
    co_code VARCHAR(2) PRIMARY KEY,
    co_name VARCHAR(50) NOT NULL
);

-- Insert Sample Records
INSERT INTO country (co_code, co_name) VALUES ('IN', 'India');
INSERT INTO country (co_code, co_name) VALUES ('US', 'United States of America');
INSERT INTO country (co_code, co_name) VALUES ('JP', 'Japan');
INSERT INTO country (co_code, co_name) VALUES ('DE', 'Germany');
```

> [!IMPORTANT]
> **Mapping Note (`co_code` / `co_name`)**: In the table schema, the columns are `co_code` and `co_name`. Therefore, in `Country.java`, the fields are explicitly mapped via `@Column(name = "co_code")` and `@Column(name = "co_name")`. This ensures that when `spring.jpa.hibernate.ddl-auto=validate` is executed, Hibernate accurately validates the table structure without throwing missing column exceptions.

---

## 4. Execution Commands

To compile, package, and run the project via Maven Command Line:
```bash
mvn clean package -Dhttp.proxyHost=proxy.cognizant.com -Dhttp.proxyPort=6050 -Dhttps.proxyHost=proxy.cognizant.com -Dhttps.proxyPort=6050 -Dhttp.proxyUser=123456
java -jar target/orm-learn-0.0.1-SNAPSHOT.jar
```

### Expected Log Output
```text
10-07-26 02:05:12.345 main  INFO  com.cognizant.ormlearn.OrmLearnApplication - Started OrmLearnApplication in 2.15 seconds
10-07-26 02:05:12.347 main  INFO  com.cognizant.ormlearn.OrmLearnApplication - Inside main
10-07-26 02:05:12.350 main  INFO  com.cognizant.ormlearn.OrmLearnApplication - Start
10-07-26 02:05:12.410 main  TRACE org.hibernate.SQL                        - select country0_.co_code as co_code1_0_, country0_.co_name as co_name2_0_ from country country0_
10-07-26 02:05:12.425 main  DEBUG com.cognizant.ormlearn.OrmLearnApplication - countries=[Country [code=IN, name=India], Country [code=US, name=United States of America], Country [code=JP, name=Japan], Country [code=DE, name=Germany]]
10-07-26 02:05:12.426 main  INFO  com.cognizant.ormlearn.OrmLearnApplication - End
```
