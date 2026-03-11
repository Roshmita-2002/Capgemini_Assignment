package com.capegimini.dao;

import com.capegimini.entity.Address;
import com.capegimini.entity.Employee;
import com.capegimini.util.JpaUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class AddressDAO {

    private boolean isValidAddress(Address address) {
        if (address.getPincode() == null || !address.getPincode().matches("\\d{6}")) {
            System.out.println("Pincode must contain exactly 6 digits.");
            return false;
        }
        return true;
    }

    // Insert address for an employee
    public void insertAddressForEmployee(int employeeId, Address address) {
        if (!isValidAddress(address)) {
            return;
        }

        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Employee employee = em.find(Employee.class, employeeId);

            if (employee != null) {
                address.setEmployee(employee);
                employee.setAddress(address);

                em.persist(address);

                tx.commit();
                System.out.println("Address inserted successfully for employee.");
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

    // Fetch address by ID
    public Address getAddressById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        Address address = null;

        try {
            address = em.find(Address.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return address;
    }

    // Update address details
    public void updateAddress(int id, String newStreet, String newCity, String newState,
                              String newCountry, String newPincode) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Address address = em.find(Address.class, id);

            if (address != null) {
                if (newPincode == null || !newPincode.matches("\\d{6}")) {
                    System.out.println("Pincode must contain exactly 6 digits.");
                    return;
                }

                address.setStreet(newStreet);
                address.setCity(newCity);
                address.setState(newState);
                address.setCountry(newCountry);
                address.setPincode(newPincode);

                tx.commit();
                System.out.println("Address updated successfully.");
            } else {
                System.out.println("Address not found.");
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

    // Delete address by ID
    public void deleteAddress(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Address address = em.find(Address.class, id);

            if (address != null) {
                em.remove(address);
                tx.commit();
                System.out.println("Address deleted successfully.");
            } else {
                System.out.println("Address not found.");
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
}