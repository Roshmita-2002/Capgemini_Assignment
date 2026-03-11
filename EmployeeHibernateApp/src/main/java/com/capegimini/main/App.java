package com.capegimini.main;

import java.time.LocalDate;
import java.util.List;

import com.capegimini.dao.AddressDAO;
import com.capegimini.dao.EmployeeDAO;
import com.capegimini.entity.Address;
import com.capegimini.entity.Employee;

public class App {
    public static void main(String[] args) {

        EmployeeDAO employeeDAO = new EmployeeDAO();
        AddressDAO addressDAO = new AddressDAO();

        // Unique email for every run
        String uniqueEmail = "user" + System.currentTimeMillis() + "@example.com";

        // 1. Insert employee with address
        Employee employee = new Employee();
        employee.setEmployeeName("Riya");
        employee.setEmail(uniqueEmail);
        employee.setGender("Female");
        employee.setPassword("riya123");
        employee.setPhone("9123456789");
        employee.setSalary(28000);
        employee.setDateOfBirth(LocalDate.of(2001, 8, 15));

        Address address = new Address();
        address.setStreet("Salt Lake");
        address.setCity("Kolkata");
        address.setState("West Bengal");
        address.setCountry("India");
        address.setPincode("700091");

        employeeDAO.insertEmployee(employee, address);

        // 2. Fetch all employees first
        List<Employee> employees = employeeDAO.getAllEmployees();
        System.out.println("\nAll Employees:");
        for (Employee e : employees) {
            System.out.println(e.getId() + " - " + e.getEmployeeName() + " - " + e.getSalary());
        }

        // 3. Get the last inserted employee id
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        Employee latestEmployee = employees.get(employees.size() - 1);
        int employeeId = latestEmployee.getId();

        // 4. Fetch employee by id
        Employee fetchedEmployee = employeeDAO.getEmployeeById(employeeId);
        if (fetchedEmployee != null) {
            System.out.println("\nEmployee Found:");
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

        // 5. Update employee
        employeeDAO.updateEmployee(employeeId, "Riya Updated", 30000, "9876543210");

        // 6. Update employee salary and address city
        employeeDAO.updateEmployeeSalaryAndCity(employeeId, 35000, "Delhi");

        // 7. Fetch employee again after update
        Employee updatedEmployee = employeeDAO.getEmployeeById(employeeId);
        if (updatedEmployee != null) {
            System.out.println("\nUpdated Employee Details:");
            System.out.println("ID: " + updatedEmployee.getId());
            System.out.println("Name: " + updatedEmployee.getEmployeeName());
            System.out.println("Salary: " + updatedEmployee.getSalary());

            if (updatedEmployee.getAddress() != null) {
                System.out.println("Updated City: " + updatedEmployee.getAddress().getCity());
            }
        }

        // 8. JPQL: salary greater than 15000
        List<Employee> highSalaryEmployees = employeeDAO.getEmployeesWithSalaryGreaterThan(15000);
        System.out.println("\nEmployees with salary > 15000:");
        for (Employee e : highSalaryEmployees) {
            System.out.println(e.getEmployeeName() + " - " + e.getSalary());
        }

        // 9. JPQL: employees by city
        List<Employee> cityEmployees = employeeDAO.getEmployeesByCity("Delhi");
        System.out.println("\nEmployees from Delhi:");
        for (Employee e : cityEmployees) {
            System.out.println(e.getEmployeeName());
        }

        // 10. JPQL: employees born after date
        List<Employee> bornAfterEmployees = employeeDAO.getEmployeesBornAfter(LocalDate.of(1999, 1, 1));
        System.out.println("\nEmployees born after 1999-01-01:");
        for (Employee e : bornAfterEmployees) {
            System.out.println(e.getEmployeeName() + " - " + e.getDateOfBirth());
        }

        // 11. JPQL: count employees by city
        List<Object[]> cityCount = employeeDAO.countEmployeesByCity();
        System.out.println("\nEmployee count by city:");
        for (Object[] row : cityCount) {
            System.out.println(row[0] + " : " + row[1]);
        }

        // 12. Fetch address by id
        if (updatedEmployee != null && updatedEmployee.getAddress() != null) {
            int addressId = updatedEmployee.getAddress().getId();

            Address fetchedAddress = addressDAO.getAddressById(addressId);
            if (fetchedAddress != null) {
                System.out.println("\nAddress Found:");
                System.out.println(fetchedAddress.getStreet() + ", " + fetchedAddress.getCity());
            }

            // 13. Update address
            addressDAO.updateAddress(addressId, "MG Road", "Mumbai", "Maharashtra", "India", "400001");

            // 14. Fetch updated address
            Address updatedAddress = addressDAO.getAddressById(addressId);
            if (updatedAddress != null) {
                System.out.println("\nUpdated Address:");
                System.out.println(updatedAddress.getStreet() + ", " + updatedAddress.getCity()
                        + ", " + updatedAddress.getState() + ", " + updatedAddress.getPincode());
            }

            // Uncomment when you want to test delete address
            // addressDAO.deleteAddress(addressId);
        }

        // Uncomment when you want to test delete employee
        // employeeDAO.deleteEmployee(employeeId);
    }
}