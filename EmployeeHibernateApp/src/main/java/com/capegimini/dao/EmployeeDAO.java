package com.capegimini.dao;

import com.capegimini.entity.Address;
import com.capegimini.entity.Employee;
import com.capegimini.util.JpaUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class EmployeeDAO {

    // Insert Employee with Address
    public void insertEmployee(Employee employee, Address address) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            employee.setAddress(address);
            address.setEmployee(employee);

            em.persist(employee);

            tx.commit();
            System.out.println("Employee with address inserted successfully.");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Fetch Employee by ID
    public Employee getEmployeeById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        Employee employee = null;

        try {
            employee = em.find(Employee.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return employee;
    }
}