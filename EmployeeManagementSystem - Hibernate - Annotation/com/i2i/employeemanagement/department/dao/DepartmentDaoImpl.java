package com.i2i.employeemanagement.department.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.i2i.employeemanagement.exception.EmployeeException;
import com.i2i.employeemanagement.helper.SessionProvider;
import com.i2i.employeemanagement.model.Department;
import com.i2i.employeemanagement.model.Employee;

public class DepartmentDaoImpl implements DepartmentDao {

    @Override
    public void addDepartment(String departmentName) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = SessionProvider.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Department department = new Department(departmentName);
            session.save(department);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error adding department: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Map<Integer, Department> getAllDepartments() {
        Session session = null;
        Map<Integer, Department> departments = new HashMap<>();
        try {
            session = SessionProvider.getSessionFactory().openSession();
            Query<Department> query = session.createQuery("FROM Department WHERE isDeleted = false", Department.class);
            for (Department department : query.list()) {
                departments.put(department.getDepartmentId(), department);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving all departments: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return departments;
    }

    @Override
    public void updateDepartment(int departmentId, Department department) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = SessionProvider.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(department);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error updating department: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean deleteDepartment(int id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = SessionProvider.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Department department = session.get(Department.class, id);
            if (department != null) {
                department.setIsDeleted(true);
                session.update(department);
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error deleting department: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return false;
    }

    @Override
    public Map<Integer, Employee> getEmployeeByDepartment(int departmentId) throws EmployeeException {
        Session session = null;
        Map<Integer, Employee> employeeDetails = new HashMap<>();
        try {
            session = SessionProvider.getSessionFactory().openSession();
            Department department = session.get(Department.class, departmentId);
            if (department != null && department.getEmployees() != null) {
                for (Employee employee : department.getEmployees()) {
                    employeeDetails.put(employee.getId(), employee);
                }
            }
        } catch (Exception e) {
            throw new EmployeeException("Error in retrieving employees by department " + departmentId, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return employeeDetails;
    }
}