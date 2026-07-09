package com.cognizant.ormlearn.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cognizant.ormlearn.model.Employee;

/**
 * Demonstration of NATIVE HIBERNATE approach for persisting an Employee.
 * Notice the significant amount of boilerplate required for session handling,
 * explicit transaction boundaries, and exception rollback logic.
 */
@Repository
public class HibernateEmployeeDao {

    @Autowired
    private SessionFactory factory;

    /**
     * Method to CREATE an employee in the database using native Hibernate API.
     * 
     * @param employee The employee entity to persist.
     * @return The generated primary key (employeeID).
     */
    public Integer addEmployee(Employee employee) {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer employeeID = null;

        try {
            tx = session.beginTransaction();
            employeeID = (Integer) session.save(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeID;
    }
}
