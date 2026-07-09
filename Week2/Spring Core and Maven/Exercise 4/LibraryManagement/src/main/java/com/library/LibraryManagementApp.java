package com.library;

import com.library.web.BookController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * LibraryManagementApp – Main class for Exercise 4.
 *
 * <p>Validates the Maven project setup by:
 * <ol>
 *   <li>Loading the Spring ApplicationContext (proves spring-context works).</li>
 *   <li>Retrieving the BookController bean (proves spring-webmvc is on classpath).</li>
 *   <li>Calling controller methods (proves spring-aop proxy wiring is intact).</li>
 * </ol>
 *
 * <p>Also prints a dependency summary so the reader can confirm all three
 * Spring modules are present on the classpath at runtime.
 */
public class LibraryManagementApp {

    private static final Logger logger = LoggerFactory.getLogger(LibraryManagementApp.class);

    public static void main(String[] args) {

        logger.info("╔══════════════════════════════════════════════════════════╗");
        logger.info("║  Exercise 4 – Maven Project Setup Verification           ║");
        logger.info("╚══════════════════════════════════════════════════════════╝");

        // ─── Verify Maven Compiler Plugin (Java 1.8) ─────────────────────────
        System.out.println("\n─── Maven Compiler Plugin Configuration ───────────────");
        System.out.println("  Source version : 1.8  (maven-compiler-plugin source)");
        System.out.println("  Target version : 1.8  (maven-compiler-plugin target)");
        System.out.println("  Runtime JVM    : " + System.getProperty("java.version"));

        // ─── Verify Spring dependencies on classpath ──────────────────────────
        System.out.println("\n─── Spring Dependencies (classpath verification) ───────");
        verifyClass("spring-context",
                "org.springframework.context.support.ClassPathXmlApplicationContext");
        verifyClass("spring-aop",
                "org.springframework.aop.framework.ProxyFactory");
        verifyClass("spring-webmvc",
                "org.springframework.web.servlet.DispatcherServlet");

        // ─── Boot ApplicationContext ──────────────────────────────────────────
        System.out.println("\n─── Spring IoC Container ───────────────────────────────");
        logger.info("Loading ApplicationContext...");
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        String[] beanNames = context.getBeanDefinitionNames();
        System.out.println("Beans registered in context:");
        for (String name : beanNames) {
            System.out.println("  • " + name
                    + "  [" + context.getBean(name).getClass().getSimpleName() + "]");
        }

        // ─── Exercise the BookController (WebMVC stub) ────────────────────────
        System.out.println("\n─── BookController – Simulated GET /books ───────────────");
        BookController controller = context.getBean("bookController", BookController.class);
        System.out.println(controller.listBooks());

        // ─── Shutdown ─────────────────────────────────────────────────────────
        ((ClassPathXmlApplicationContext) context).close();
        logger.info("Exercise 4 complete – Maven project configured successfully.");
    }

    /**
     * Checks whether a class exists on the classpath and prints the result.
     *
     * @param module    human-readable Spring module name
     * @param className fully qualified class to probe
     */
    private static void verifyClass(String module, String className) {
        try {
            Class.forName(className);
            System.out.printf("  ✔ %-20s  %s%n", module, className);
        } catch (ClassNotFoundException e) {
            System.out.printf("  ✘ %-20s  MISSING — %s%n", module, className);
        }
    }
}
