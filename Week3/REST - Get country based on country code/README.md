# REST - Get Country Based on Country Code (`spring-learn`)

## Project Overview
This project implements a RESTful web service using **Spring Boot 2.7.x** (`@RestController` + `@GetMapping`) that returns specific `Country` details based on a two-character ISO code. It demonstrates how to extract path parameters (`@PathVariable`), load XML bean collections (`country.xml`) via `ClassPathXmlApplicationContext`, and filter records case-insensitively using Java 8 Stream API.

---

## 1. Execution Guide & Verification

### Compilation & Execution via Maven
Navigate to the project folder (`server.port=8083`) and run via Maven:
```bash
cd "C:\Users\Pyramid\Desktop\CognizantWork\Week3\REST - Get country based on country code\spring-learn"

mvn clean package
java -jar target/spring-learn-0.0.1-SNAPSHOT.jar
```

### Testing the Endpoint (Case-Insensitive Verification)
Test the endpoint using Postman, curl, or your browser. Notice that both `in` and `IN` (as well as both `/country/{code}` and `/countries/{code}`) return the exact same JSON response:

#### Request 1: Lowercase Country Code (`in`)
```url
GET http://localhost:8083/country/in
```
**JSON Response:**
```json
{
  "code": "IN",
  "name": "India"
}
```

#### Request 2: Uppercase Country Code (`US`)
```url
GET http://localhost:8083/countries/US
```
**JSON Response:**
```json
{
  "code": "US",
  "name": "United States"
}
```

---

## 2. Technical Implementation Details

### 1. `CountryController.java` (`@RestController` & `@PathVariable`)
```java
@RestController
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping({"/countries/{code}", "/country/{code}"})
    public Country getCountry(@PathVariable String code) {
        LOGGER.info("Start getCountry endpoint in CountryController with code: {}", code);
        Country country = countryService.getCountry(code);
        LOGGER.info("End getCountry endpoint in CountryController");
        return country;
    }
}
```
* **`@RestController`**: Combined `@Controller` + `@ResponseBody`. Tells Spring MVC that returned `Country` objects must be automatically serialized into JSON via Jackson `MappingJackson2HttpMessageConverter`.
* **`@GetMapping({"/countries/{code}", "/country/{code}"})`**: Maps both URL patterns to support exact specification syntax (`/countries/{code}`) alongside sample request syntax (`/country/in`).
* **`@PathVariable String code`**: Extracts the URL path segment (e.g., `"in"`) and binds it to the method argument `code`.

### 2. `CountryService.java` (`@Service` & Stream Filtering)
```java
@Service
public class CountryService {

    public Country getCountry(String code) {
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        @SuppressWarnings("unchecked")
        List<Country> countryList = (List<Country>) context.getBean("countryList");

        return countryList.stream()
                .filter(country -> country.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }
}
```
* **XML Bean Collection (`countryList`)**: `ClassPathXmlApplicationContext` loads `country.xml` and retrieves the `ArrayList` containing the pre-configured `Country` beans (`US`, `DE`, `IN`, `JP`).
* **Case-Insensitive Lambda (`equalsIgnoreCase`)**: The Java 8 Stream API (`stream().filter(...)`) compares each bean's ISO code against the incoming `@PathVariable code` ignoring case differences (`"in".equalsIgnoreCase("IN")` evaluates to `true`).
* **`findFirst().orElse(null)`**: Returns the first matching `Country` entity or `null` if no match is found.
