package com.capegimini.main;

import java.time.LocalDate;

import com.capegimini.dao.EmployeeDAO;
import com.capegimini.entity.Address;
import com.capegimini.entity.Employee;

public class App {
    public static void main(String[] args) {

        EmployeeDAO employeeDAO = new EmployeeDAO();

        Employee employee = new Employee();
        employee.setEmployeeName("John");
        employee.setEmail("jebraA@example.com");
        employee.setGender("Male");
        employee.setPassword("john123");
        employee.setPhone("9876543210");
        employee.setSalary(25000);
        employee.setDateOfBirth(LocalDate.of(2000, 5, 10));

        Address address = new Address();
        address.setStreet("Park Street");
        address.setCity("Kolkata");
        address.setState("West Bengal");
        address.setCountry("India");
        address.setPincode("700016");

        employeeDAO.insertEmployee(employee, address);

        Employee fetchedEmployee = employeeDAO.getEmployeeById(1);

        if (fetchedEmployee != null) {
            System.out.println("Employee Found:");
            System.out.println("ID: " + fetchedEmployee.getId());
            System.out.println("Name: " + fetchedEmployee.getEmployeeName());
            System.out.println("Email: " + fetchedEmployee.getEmail());
            System.out.println("Salary: " + fetchedEmployee.getSalary());

            if (fetchedEmployee.getAddress() != null) {
                System.out.println("City: " + fetchedEmployee.getAddress().getCity());
                System.out.println("Pincode: " + fetchedEmployee.getAddress().getPincode());
            }
        } else {
            System.out.println("Employee not found.");
        }
    }
}