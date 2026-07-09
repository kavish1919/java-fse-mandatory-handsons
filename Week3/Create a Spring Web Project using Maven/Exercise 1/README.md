# Hands-on 1: Create a Spring Web Project using Maven (`spring-learn`)

## Project Overview
This project demonstrates how to initialize, configure, and execute a **Spring Web Project** using **Maven** and **Spring Boot 2.7.x**. It configures embedded Tomcat Server, Spring Web MVC (`DispatcherServlet`), and SLF4J / Logback console logging.

---

## 1. Step-by-Step Execution Guide

### Step 1 to 6: Project Creation via Spring Initializr
1. Go to **https://start.spring.io/**
2. Set **Project**: Maven Project, **Language**: Java, **Spring Boot**: `2.7.18` (or latest stable 2.7.x).
3. Set **Group**: `com.cognizant`
4. Set **Artifact**: `spring-learn`
5. Click **Dependencies** and select:
   * **Spring Web** (Includes Spring MVC, REST, Jackson, and embedded Tomcat Server).
   * **Spring Boot DevTools** (Provides hot reloading during development).
6. Click **Generate** to download `spring-learn.zip` and extract it into your workspace.

### Step 7: Build Project via Maven Command Line
Open terminal/command prompt inside the `spring-learn` directory and build the project using Maven (including corporate proxy settings if inside corporate network):
```bash
cd "C:\Users\Pyramid\Desktop\CognizantWork\Week3\Create a Spring Web Project using Maven\Exercise 1\spring-learn"

mvn clean package -Dhttp.proxyHost=proxy.cognizant.com -Dhttp.proxyPort=6050 -Dhttps.proxyHost=proxy.cognizant.com -Dhttps.proxyPort=6050 -Dhttp.proxyUser=123456
```

### Step 8: Import into Eclipse IDE
1. Open Eclipse IDE (`2019-03 R` or newer).
2. Navigate to **File > Import > Maven > Existing Maven Projects**.
3. Click **Browse...**, select the extracted `spring-learn` folder, and click **Finish**.

### Step 9 & 10: Verify and Execute `SpringLearnApplication`
Run `SpringLearnApplication.java` as a **Java Application** or via `java -jar target/spring-learn-0.0.1-SNAPSHOT.jar`. Observe the console logs verifying that `main()` executed cleanly and Tomcat started on port 8080:

```text
10-07-26 02:14:12.100 main  INFO  com.cognizant.springlearn.SpringLearnApplication - Start
10-07-26 02:14:12.215 main  INFO  com.cognizant.springlearn.SpringLearnApplication - Starting SpringLearnApplication using Java 17
10-07-26 02:14:13.501 main  INFO  org.springframework.boot.web.embedded.tomcat.TomcatWebServer - Tomcat initialized with port(s): 8080 (http)
10-07-26 02:14:13.512 main  INFO  org.apache.catalina.core.StandardService         - Starting service [Tomcat]
10-07-26 02:14:13.513 main  INFO  org.apache.catalina.core.StandardEngine          - Starting Servlet engine: [Apache Tomcat/9.0.83]
10-07-26 02:14:14.200 main  INFO  org.springframework.web.servlet.DispatcherServlet - Initializing Servlet 'dispatcherServlet'
10-07-26 02:14:14.202 main  INFO  org.springframework.web.servlet.DispatcherServlet - Completed initialization in 2 ms
10-07-26 02:14:14.850 main  INFO  org.springframework.boot.web.embedded.tomcat.TomcatWebServer - Tomcat started on port(s): 8080 (http) with context path ''
10-07-26 02:14:14.855 main  INFO  com.cognizant.springlearn.SpringLearnApplication - Started SpringLearnApplication in 2.75 seconds
10-07-26 02:14:14.856 main  INFO  com.cognizant.springlearn.SpringLearnApplication - Inside main
10-07-26 02:14:14.857 main  INFO  com.cognizant.springlearn.SpringLearnApplication - End
```

---

## 2. SME Technical Walkthrough Checklist

### 1. `src/main/java` — Application Code Structure
This directory contains all Java application source code and entry points:
* **`com.cognizant.springlearn.SpringLearnApplication`**: The bootstrap entry class annotated with `@SpringBootApplication` and containing the static `main()` method. Future controllers (`@RestController`), services (`@Service`), and models (`@Component`) are placed in sub-packages under `com.cognizant.springlearn`.

### 2. `src/main/resources` — Application Configuration
Contains externalized configurations and static assets:
* **`application.properties`**: Configures application properties such as the embedded web server port (`server.port=8080`), application name (`spring.application.name=spring-learn`), and custom log patterns (`logging.pattern.console`).
* **`static/` & `templates/`**: Folders for serving static files (CSS, JS, images) and server-side view templates (e.g., Thymeleaf/JSP).

### 3. `src/test/java` — Application Testing Layer
Contains automated unit and integration tests:
* **`com.cognizant.springlearn.SpringLearnApplicationTests`**: Uses `@SpringBootTest` to initialize and verify that the full Spring `WebApplicationContext` and `DispatcherServlet` beans load cleanly without any wiring failures.

### 4. `SpringLearnApplication.java` — Walkthrough of `main()` Method
```java
public static void main(String[] args) {
    LOGGER.info("Start");
    SpringApplication.run(SpringLearnApplication.class, args);
    LOGGER.info("Inside main");
    LOGGER.info("End");
}
```
* **`SpringApplication.run(...)`**:
  1. Bootstraps the application and creates an `AnnotationConfigServletWebServerApplicationContext`.
  2. Starts an embedded **Apache Tomcat HTTP server** listening on port `8080`.
  3. Registers Spring MVC's **`DispatcherServlet`** to handle incoming HTTP requests.
  4. Scans and initializes all Spring beans across `com.cognizant.springlearn`.
* **`LOGGER.info(...)` calls**: Execute sequentially before and after container startup, verifying that the main method runs continuously while Tomcat serves web requests.

### 5. Purpose of `@SpringBootApplication` Annotation
`@SpringBootApplication` is a composite meta-annotation that combines three core annotations:
1. **`@Configuration`**: Designates the class as a primary source of Spring Bean definitions.
2. **`@EnableAutoConfiguration`**: Instructs Spring Boot's autoconfiguration engine to automatically configure web components based on classpath jars (`spring-boot-starter-web` automatically sets up `TomcatServletWebServerFactory`, `DispatcherServletAutoConfiguration`, `WebMvcAutoConfiguration`, and `JacksonAutoConfiguration`).
3. **`@ComponentScan`**: Automatically scans the root package (`com.cognizant.springlearn`) and all sub-packages for Spring components (`@Controller`, `@RestController`, `@Service`, `@Repository`).

### 6. `pom.xml` — Configuration & Dependency Hierarchy
* **`<parent> spring-boot-starter-parent (2.7.18)`**: Provides centralized dependency management, compiler plugin configuration (`UTF-8`, Java 17 target), and default version properties.
* **Dependency Tree / Hierarchy**:
  ```text
  com.cognizant:spring-learn:0.0.1-SNAPSHOT
  ├── org.springframework.boot:spring-boot-starter-web:2.7.18
  │   ├── org.springframework.boot:spring-boot-starter (Spring Core IoC, Logging via Logback/SLF4J, YAML support)
  │   ├── org.springframework.boot:spring-boot-starter-json (Jackson ObjectMapper for JSON serialization/deserialization)
  │   ├── org.springframework.boot:spring-boot-starter-tomcat (Embedded Apache Tomcat server 9.x)
  │   └── org.springframework:spring-webmvc (Spring MVC DispatcherServlet, @RequestMapping annotations)
  │       └── org.springframework:spring-web (Spring Web Context & HTTP utilities)
  ├── org.springframework.boot:spring-boot-devtools (Hot-swapping and live application restart)
  └── org.springframework.boot:spring-boot-starter-test (JUnit 5 Jupiter, Mockito, Spring TestContext)
  ```
