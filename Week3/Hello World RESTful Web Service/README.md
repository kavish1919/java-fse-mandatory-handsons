# Hello World RESTful Web Service (`spring-learn`)

## Project Overview
This exercise implements a RESTful Web Service using **Spring Boot 2.7.x** (`@RestController` / `@GetMapping`) that handles HTTP `GET` requests to the `/hello` endpoint and returns `"Hello World!!"`.

---

## 1. Execution Guide & Log Verification

### Compilation & Execution via Maven
Navigate to the project directory (`server.port=8083`) and run:
```bash
cd "C:\Users\Pyramid\Desktop\CognizantWork\Week3\Hello World RESTful Web Service\spring-learn"

mvn clean package
java -jar target/spring-learn-0.0.1-SNAPSHOT.jar
```

### Testing the REST Endpoint
Open your browser or Postman and navigate to:
```url
GET http://localhost:8083/hello
```
**Sample Response Body:**
```text
Hello World!!
```

### Expected Log Output (`sayHello` Start / End Verification)
When `http://localhost:8083/hello` is accessed, the console displays the start and end trace logs inside `HelloController.sayHello()`:
```text
10-07-26 02:18:15.112 main  INFO  com.cognizant.springlearn.SpringLearnApplication - Started SpringLearnApplication in 2.34 seconds
10-07-26 02:18:22.450 http-nio-8083-exec-1 INFO  com.cognizant.springlearn.controller.HelloController - Start
10-07-26 02:18:22.453 http-nio-8083-exec-1 INFO  com.cognizant.springlearn.controller.HelloController - End
```

---

## 2. SME Technical Walkthrough: HTTP Headers & Developer Tools

### 1. HTTP Response Header Details Received
When the `GET /hello` endpoint returns `"Hello World!!"`, Spring Boot's embedded Tomcat server and `StringHttpMessageConverter` generate the following standard HTTP response headers:

| Header Name | Sample Value | Explanation |
| :--- | :--- | :--- |
| **`HTTP Status`** | `HTTP/1.1 200 OK` | Confirms that the request succeeded without errors. |
| **`Content-Type`** | `text/plain;charset=UTF-8` | Indicates that the response body contains plain text (since `sayHello()` returns a Java `String` directly rather than JSON). |
| **`Content-Length`** | `13` | The exact size of the payload in bytes (`"Hello World!!"` is exactly 13 characters/bytes). |
| **`Date`** | `Fri, 10 Jul 2026 20:48:22 GMT` | The server timestamp when the HTTP response was generated. |
| **`Connection`** | `keep-alive` | Instructs the client browser/tool to keep the underlying TCP connection open for subsequent requests. |

---

### 2. Inspecting Headers in Chrome Developer Tools (Network Tab)
1. Open Google Chrome and press **`F12`** (or right-click > **Inspect**) to open **Developer Tools**.
2. Click on the **Network** tab at the top of the panel.
3. In the address bar, type `http://localhost:8083/hello` and press **Enter**.
4. In the Network requests table, click on the **`hello`** request.
5. In the right-hand inspection pane, ensure the **Headers** tab is selected.
6. Scroll down to **Response Headers** to inspect `Content-Type: text/plain;charset=UTF-8`, `Content-Length: 13`, and status code `200 OK`.

---

### 3. Inspecting Headers in Postman ("Headers" Tab)
1. Launch **Postman** and click `+` to open a new request tab.
2. Select HTTP method **`GET`** from the dropdown.
3. Enter request URL: **`http://localhost:8083/hello`** and click **Send**.
4. In the lower response section (below the request bar), you will see the **Body** tab displaying `Hello World!!`.
5. Click on the **Headers** tab right next to **Body**.
6. You will see the complete table of received HTTP response headers (`Content-Type`, `Content-Length`, `Date`, `Connection`).
