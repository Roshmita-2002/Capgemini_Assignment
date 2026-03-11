package com.capegimini.dao;

import java.time.LocalDate;
import java.util.List;

import com.capegimini.entity.Address;
import com.capegimini.entity.Employee;
import com.capegimini.util.JpaUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class EmployeeDAO {

    // Validation method
    private boolean isValidEmployee(Employee employee) {
        if (employee.getSalary() <= 0) {
            System.out.println("Salary must be greater than 0.");
            return false;
        }

        if (employee.getPhone() == null || !employee.getPhone().matches("\\d{10}")) {
            System.out.println("Phone number must contain exactly 10 digits.");
            return false;
        }

        return true;
    }

    // Insert Employee with Address
    public void insertEmployee(Employee employee, Address address) {
        if (!isValidEmployee(employee)) {
            return;
        }

        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // email unique check
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(e) FROM Employee e WHERE e.email = :email", Long.class);
            query.setParameter("email", employee.getEmail());

            long count = query.getSingleResult();
            if (count > 0) {
                System.out.println("Email already exists.");
                return;
            }

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

    // Fetch All Employees
    public List<Employee> getAllEmployees() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Employee> employees = null;

        try {
            TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e", Employee.class);
            employees = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return employees;
    }

    // Update Employee Details
    public void updateEmployee(int id, String newName, double newSalary, String newPhone) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Employee employee = em.find(Employee.class, id);

            if (employee != null) {
                if (newSalary <= 0) {
                    System.out.println("Salary must be greater than 0.");
                    return;
                }

                if (newPhone == null || !newPhone.matches("\\d{10}")) {
                    System.out.println("Phone number must contain exactly 10 digits.");
                    return;
                }

                employee.setEmployeeName(newName);
                employee.setSalary(newSalary);
                employee.setPhone(newPhone);

                tx.commit();
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("Employee not found.");
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Update Employee Salary and Address City
    public void updateEmployeeSalaryAndCity(int id, double newSalary, String newCity) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Employee employee = em.find(Employee.class, id);

            if (employee != null) {
                if (newSalary <= 0) {
                    System.out.println("Salary must be greater than 0.");
                    return;
                }

                employee.setSalary(newSalary);

                if (employee.getAddress() != null) {
                    employee.getAddress().setCity(newCity);
                }

                tx.commit();
                System.out.println("Employee salary and address city updated successfully.");
            } else {
                System.out.println("Employee not found.");
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Delete Employee by ID
    public void deleteEmployee(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Employee employee = em.find(Employee.class, id);

            if (employee != null) {
                em.remove(employee);
                tx.commit();
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Employee not found.");
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // JPQL: Fetch employees with salary > 15000
    public List<Employee> getEmployeesWithSalaryGreaterThan(double salary) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Employee> employees = null;

        try {
            TypedQuery<Employee> query = em.createQuery(
                    "SELECT e FROM Employee e WHERE e.salary > :salary", Employee.class);
            query.setParameter("salary", salary);
            employees = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return employees;
    }

    // JPQL: Fetch employees from a specific city
    public List<Employee> getEmployeesByCity(String city) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Employee> employees = null;

        try {
            TypedQuery<Employee> query = em.createQuery(
                    "SELECT e FROM Employee e WHERE e.address.city = :city", Employee.class);
            query.setParameter("city", city);
            employees = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return employees;
    }

    // JPQL: Fetch employees born after a given date
    public List<Employee> getEmployeesBornAfter(LocalDate date) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Employee> employees = null;

        try {
            TypedQuery<Employee> query = em.createQuery(
                    "SELECT e FROM Employee e WHERE e.dateOfBirth > :date", Employee.class);
            query.setParameter("date", date);
            employees = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return employees;
    }

    // JPQL: Count employees in each city
    public List<Object[]> countEmployeesByCity() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Object[]> result = null;

        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT e.address.city, COUNT(e) FROM Employee e GROUP BY e.address.city",
                    Object[].class);
            result = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return result;
    }
}