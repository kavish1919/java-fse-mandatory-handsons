# Creating Microservices for Account and Loan (`account` & `loan`)

## Exercise Overview
In this hands-on exercise, we decompose a monolithic banking concept into two independent, decoupled **Spring RESTful Webservice** microservices managed as separate Maven projects:
1. **Account Microservice (`account`)**: Handles account inquiries on default port **`8080`**.
2. **Loan Microservice (`loan`)**: Handles loan account inquiries on configured port **`8081`** (avoiding port binding conflicts with `account`).

Both microservices are lightweight, independent applications with their own `pom.xml`, embedded Tomcat instances, and REST controllers without any backend database dependencies.

---

## 1. Architecture & Port Conflict Resolution Walkthrough

### Why did the initial launch of Loan Microservice fail?
When `account` (`AccountApplication.java`) is launched first, embedded Tomcat binds to the default HTTP port **`8080`**. 
If `loan` (`LoanApplication.java`) is then launched without custom port configuration while `account` is still running, Tomcat throws a **`java.net.BindException: Address already in use: bind`** error. 

### How did we resolve it?
We added the following property inside `loan/src/main/resources/application.properties`:
```properties
server.port=8081
```
Now, both microservices run simultaneously on distinct ports without interference.

---

## 2. Execution Guide & Verification

### A. Launching Account Microservice (`port 8080`)
Open terminal or Eclipse console and run:
```bash
cd "C:\Users\Pyramid\Desktop\CognizantWork\Week4\Microservices with Spring Boot 3 and Spring Cloud\account"
mvn clean package
java -jar target/account-0.0.1-SNAPSHOT.jar
```

**Verify in Browser or Postman (`GET /accounts/{number}`)**:
```url
http://localhost:8080/accounts/00987987973432
```
**JSON Response:**
```json
{
  "number": "00987987973432",
  "type": "savings",
  "balance": 234343.0
}
```

---

### B. Launching Loan Microservice (`port 8081`)
Open a second terminal window or use the Eclipse monitor icon in the console view and run:
```bash
cd "C:\Users\Pyramid\Desktop\CognizantWork\Week4\Microservices with Spring Boot 3 and Spring Cloud\loan"
mvn clean package
java -jar target/loan-0.0.1-SNAPSHOT.jar
```

**Verify in Browser or Postman (`GET /loans/{number}`)**:
```url
http://localhost:8081/loans/H00987987972342
```
**JSON Response:**
```json
{
  "number": "H00987987972342",
  "type": "car",
  "loan": 400000.0,
  "emi": 3258.0,
  "tenure": 18
}
```

---

## 3. Eclipse Multi-Console Management Note
When running both microservices inside Eclipse IDE:
* The **Console View** displays output from the currently active application.
* To switch between `AccountApplication` (`8080`) and `LoanApplication` (`8081`), click the **Monitor / Display Selected Console icon (computer monitor icon with a drop-down arrow)** located in the upper-right toolbar of the Eclipse Console View.
