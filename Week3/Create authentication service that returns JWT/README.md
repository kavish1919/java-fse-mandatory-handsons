# Create Authentication Service That Returns JWT (`jwt-learn`)

## Project Overview
This project implements the first major step of the **JSON Web Token (JWT) Authentication workflow** using **Spring Boot 2.7.x**, **Spring Security**, and **JJWT (`io.jsonwebtoken`)**. When a client transmits valid credentials via HTTP Basic authentication (`curl -u user:pwd`), the `/authenticate` endpoint validates the request, decodes the `Authorization` header, and responds with a freshly signed JWT token.

---

## 1. Execution & Curl Verification Guide

### Compilation & Server Startup
Open terminal, navigate to the project directory (`server.port=8090`), and run via Maven:
```bash
cd "C:\Users\Pyramid\Desktop\CognizantWork\Week3\Create authentication service that returns JWT\jwt-learn"

mvn clean package
java -jar target/jwt-learn-0.0.1-SNAPSHOT.jar
```

### Testing via cURL (`-u user:pwd`)
Once the application starts on port `8090`, open another terminal and execute the exact curl request specified:
```bash
curl -s -u user:pwd http://localhost:8090/authenticate
```
**Sample JSON Response:**
```json
{"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzIwNjI4MzgxLCJleHAiOjE3MjA2Mjk1ODF9.<signature>"}
```

---

## 2. Implementation of Three Major Steps

### Step 1: Create Authentication Controller & Configure `SecurityConfig`
In `SecurityConfig.java`, we configure Spring Security to enforce HTTP Basic authentication (`http.httpBasic()`) for `/authenticate` and register an `InMemoryUserDetailsManager` with credentials `user:pwd`:
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("pwd")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/authenticate").hasRole("USER")
            .anyRequest().authenticated()
            .and()
            .httpBasic();
        return http.build();
    }
}
```

### Step 2: Read `Authorization` Header and Decode Username & Password
In `AuthenticationController.java`, we intercept the HTTP header `Authorization: Basic dXNlcjpwd2Q=` (`dXNlcjpwd2Q=` is Base64 for `user:pwd`), decode it into plain text (`user:pwd`), and extract the username:
```java
private String getUser(String authHeader) {
    if (authHeader != null && authHeader.startsWith("Basic ")) {
        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
        return credentials.split(":", 2)[0]; // Returns "user"
    }
    return "unknown";
}
```

### Step 3: Generate Token Based on Retrieved User
Once `"user"` is extracted, `generateJwt(user)` uses the `JJWT` builder API (`io.jsonwebtoken.Jwts`) to construct a token signed with `HS256` and valid for 20 minutes:
```java
private String generateJwt(String user) {
    JwtBuilder builder = Jwts.builder();
    builder.setSubject(user);
    builder.setIssuedAt(new Date());
    builder.setExpiration(new Date((new Date()).getTime() + 1200000)); // 20 minutes validity
    builder.signWith(SignatureAlgorithm.HS256, "secretkey");
    return builder.compact();
}
```
When returned inside a `Map<String, String>` (`response.put("token", token)`), Spring MVC's Jackson converter automatically serializes it into `{"token":"eyJhbG..."}`.
