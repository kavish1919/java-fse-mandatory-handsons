# REST - Country Web Service (`spring-learn`)

## Project Overview
This exercise implements a RESTful web service using **Spring Boot 2.7.x** (`@RestController` + `@RequestMapping`) that handles HTTP `GET` requests to `/country` (`getCountryIndia()`). It loads the India bean (`id="in"`) configured inside `country.xml` via `ClassPathXmlApplicationContext` and returns it as a JSON payload (`{"code": "IN", "name": "India"}`).

---

## 1. Execution Guide & Verification

### Compilation & Execution via Maven
Open a terminal inside the project directory (`server.port=8083`) and run via Maven:
```bash
cd "C:\Users\Pyramid\Desktop\CognizantWork\Week3\REST - Country Web Service\spring-learn"

mvn clean package
java -jar target/spring-learn-0.0.1-SNAPSHOT.jar
```

### Testing the REST Endpoint
Open your browser, curl, or Postman and navigate to:
```url
GET http://localhost:8083/country
```
**Sample Response Body (`application/json`):**
```json
{
  "code": "IN",
  "name": "India"
}
```

### Expected Controller Verification Logs
When `http://localhost:8083/country` is hit, the application console displays:
```text
12-07-26 15:00:12.115 http-nio-8083-exec-1 INFO  com.cognizant.springlearn.controller.CountryController - Start getCountryIndia
12-07-26 15:00:12.120 http-nio-8083-exec-1 INFO  org.springframework.context.support.ClassPathXmlApplicationContext - Refreshing ClassPathXmlApplicationContext...
12-07-26 15:00:12.180 http-nio-8083-exec-1 DEBUG com.cognizant.springlearn.model.Country - Inside Country Constructor.
12-07-26 15:00:12.185 http-nio-8083-exec-1 INFO  com.cognizant.springlearn.controller.CountryController - End getCountryIndia
```

---

## 2. SME Technical Walkthrough

### 1. What Happens in the Controller Method (`getCountryIndia()`)?
```java
@RequestMapping(value = "/country", method = RequestMethod.GET)
public Country getCountryIndia() {
    LOGGER.info("Start getCountryIndia");
    ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
    Country country = context.getBean("in", Country.class);
    LOGGER.info("End getCountryIndia");
    return country;
}
```
1. **Request Mapping**: When `DispatcherServlet` receives an HTTP `GET /country` request, it inspects handler mappings and invokes `CountryController.getCountryIndia()` due to the `@RequestMapping` annotation.
2. **Container Initialization**: `new ClassPathXmlApplicationContext("country.xml")` reads `src/main/resources/country.xml` and initializes the Spring IoC container.
3. **Bean Lookup & Injection**: `context.getBean("in", Country.class)` retrieves the `Country` singleton bean identified by `id="in"`. During initialization, Spring calls `Country()` (no-arg constructor) followed by `setCode("IN")` and `setName("India")` (setter injection).
4. **Return**: The method logs `End getCountryIndia` and returns the populated `Country` object (`Country [code=IN, name=India]`) back to Spring MVC's `DispatcherServlet`.

---

### 2. How the Bean is Converted into JSON Response?
1. **`@RestController` Annotation**: Because `CountryController` is annotated with `@RestController` (`@Controller` + `@ResponseBody`), Spring MVC knows that the returned `Country` object should **not** be resolved to an HTML view template (Thymeleaf/JSP). Instead, the object must be serialized directly into the HTTP response body.
2. **`HttpMessageConverter` Selection**: Spring MVC iterates through its registered message converters and selects `MappingJackson2HttpMessageConverter` (provided by `spring-boot-starter-json` / Jackson `ObjectMapper` on the classpath).
3. **Reflection-based Serialization**: Jackson inspects `Country.class` getter methods (`getCode()` and `getName()`) via Java reflection and converts the Java fields into valid JSON key-value pairs (`{"code": "IN", "name": "India"}`).
4. **Response Writing**: The converter sets the HTTP response header `Content-Type: application/json` and writes the JSON string bytes directly into the HTTP output stream.

---

### 3. Inspecting Headers in Chrome Developer Tools (Network Tab)
Unlike `/hello` (`text/plain`), returning a domain object triggers automatic `application/json` content typing:

| Header Name | Sample Value | Explanation |
| :--- | :--- | :--- |
| **`HTTP Status`** | `HTTP/1.1 200 OK` | Confirms the GET request succeeded without errors. |
| **`Content-Type`** | `application/json` | Confirms that Jackson serialized the `Country` POJO into JSON format. |
| **`Transfer-Encoding`** | `chunked` *(or `Content-Length: 29`)* | Indicates how the payload bytes are transmitted across the HTTP connection. |
| **`Date`** | `Sun, 12 Jul 2026 09:30:12 GMT` | Server timestamp when the response was generated. |
| **`Connection`** | `keep-alive` | Keeps the TCP connection open for subsequent browser requests. |

#### Steps in Chrome:
1. Press **`F12`** (or right-click > **Inspect**) and click the **Network** tab.
2. Type `http://localhost:8083/country` into the browser address bar and press **Enter**.
3. In the requests list, click on the **`country`** request row.
4. Select the **Headers** tab in the right-hand inspection panel.
5. Scroll down to **Response Headers** to inspect `Content-Type: application/json` and status `200 OK`.

---

### 4. Inspecting Headers in Postman ("Headers" Tab)
1. Launch **Postman** and open a new request tab (`+`).
2. Set the HTTP method to **`GET`** and enter URL: **`http://localhost:8083/country`**.
3. Click the blue **Send** button.
4. In the lower response panel, verify that the **Body** tab displays:
   ```json
   {
     "code": "IN",
     "name": "India"
   }
   ```
5. Click on the **Headers** tab right next to **Body**.
6. Inspect the table showing `Content-Type: application/json`, `Date`, and `Connection` key-value pairs received from embedded Tomcat.
