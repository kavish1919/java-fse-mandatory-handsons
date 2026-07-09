# Hands-on 4: Spring Core – Load Country from Spring Configuration XML (`spring-learn`)

## Project Overview
This project demonstrates how to configure and instantiate Java POJOs using **Spring Core IoC Container** and **XML-based Bean Definitions (`country.xml`)**. It illustrates bean lifecycle initialization, dependency injection (`<property>`), and retrieving managed beans via `ClassPathXmlApplicationContext`.

---

## 1. Execution Guide & Expected Output

### Compilation & Execution via Maven
Navigate to the project directory and run via Maven:
```bash
cd "C:\Users\Pyramid\Desktop\CognizantWork\Week3\Spring Core – Load Country from Spring Configuration XML\Exercise 4\spring-learn"

mvn clean package
java -cp target/spring-learn-0.0.1-SNAPSHOT.jar com.cognizant.springlearn.SpringLearnApplication
```

### Expected Log Traces & Method Invocation Order
When `SpringLearnApplication.main()` is executed, observe the exact order of logs confirming how the Spring IoC container initializes the bean:

```text
10-07-26 02:16:01.100 main  INFO  com.cognizant.springlearn.SpringLearnApplication - Inside main - Start
10-07-26 02:16:01.210 main  INFO  org.springframework.context.support.ClassPathXmlApplicationContext - Refreshing org.springframework.context.support.ClassPathXmlApplicationContext@...
10-07-26 02:16:01.305 main  DEBUG com.cognizant.springlearn.Country - Inside Country Constructor.
10-07-26 02:16:01.310 main  DEBUG com.cognizant.springlearn.Country - Inside setCode.
10-07-26 02:16:01.312 main  DEBUG com.cognizant.springlearn.Country - Inside setName.
10-07-26 02:16:01.320 main  DEBUG com.cognizant.springlearn.SpringLearnApplication - Country : Country [code=IN, name=India]
10-07-26 02:16:01.325 main  INFO  com.cognizant.springlearn.SpringLearnApplication - Inside main - End
```

#### Lifecycle Observations:
1. **No-arg Constructor First**: The container instantiates `Country` by calling `new Country()` first (`Inside Country Constructor.`).
2. **Setters Second**: Next, setter injection occurs when the container processes `<property>` tags, calling `setCode("IN")` (`Inside setCode.`) and `setName("India")` (`Inside setName.`).

---

## 2. SME Technical Walkthrough

### 1. XML Configuration Tags & Attributes (`country.xml`)
```xml
<bean id="country" class="com.cognizant.springlearn.Country">
    <property name="code" value="IN" />
    <property name="name" value="India" />
</bean>
```
* **`<bean>` tag**: The fundamental building block of Spring XML configuration. It instructs the Spring IoC container to manage an instance of the specified class.
* **`id` attribute**: A unique identifier (`"country"`) assigned to the bean within the container. Used when calling `context.getBean("country", Country.class)`.
* **`class` attribute**: The fully qualified Java class name (`"com.cognizant.springlearn.Country"`) that the container must instantiate using reflection.
* **`<property>` tag**: Specifies setter-based Dependency Injection (`DI`). Instructs Spring to call the corresponding mutator (`setter`) method on the bean.
* **`name` attribute**: Identifies the Java bean property name (e.g., `code` maps directly to `setCode(...)`, and `name` maps to `setName(...)`).
* **`value` attribute**: Assigns a literal string value (`"IN"` or `"India"`) to the property. Spring automatically converts string literals into target primitive/wrapper types.

### 2. `ApplicationContext` vs `ClassPathXmlApplicationContext`
* **`ApplicationContext`**: The core Spring container interface (`org.springframework.context.ApplicationContext`) that extends `BeanFactory`. It provides advanced enterprise capabilities including bean lifecycle management, internationalization (`MessageSource`), event publishing (`ApplicationEventPublisher`), and automatic bean post-processing.
* **`ClassPathXmlApplicationContext`**: A concrete implementation of `ApplicationContext` (`org.springframework.context.support.ClassPathXmlApplicationContext`). It loads and parses bean definitions from one or more XML files located on the application classpath (e.g., inside `src/main/resources/country.xml`).

### 3. What Exactly Happens When `context.getBean()` is Invoked?
```java
ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
Country country = (Country) context.getBean("country", Country.class);
```
When `context.getBean("country", Country.class)` is called, the container executes the following lookup and initialization sequence:
1. **Container Lookup**: The container searches its internal bean registry (`ConcurrentHashMap` inside `DefaultListableBeanFactory`) for a bean registered with the ID `"country"`.
2. **Scope Verification**: By default, beans are defined with `singleton` scope. If the `Country` singleton instance has already been created during container startup (`ClassPathXmlApplicationContext` instantiation), the cached instance is returned immediately.
3. **Instantiation via Reflection (if not cached)**:
   * The container calls `Country.class.getDeclaredConstructor().newInstance()` to invoke the empty-parameter constructor (`Inside Country Constructor.`).
4. **Dependency Injection (`PopulateBean`)**:
   * The container reads the `<property>` definitions and invokes `setCode("IN")` and `setName("India")` via Java reflection (`Inside setCode.` -> `Inside setName.`).
5. **Bean Post-Processing & Initialization**: Any registered `BeanPostProcessor` hooks or `InitializingBean.afterPropertiesSet()` callbacks are executed.
6. **Type Casting & Return**: The fully initialized and populated `Country` object is cast to `Country.class` and returned to the caller (`displayCountry()`), where its details are printed (`Country : Country [code=IN, name=India]`).
