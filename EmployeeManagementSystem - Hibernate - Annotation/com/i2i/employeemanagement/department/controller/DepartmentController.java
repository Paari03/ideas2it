package com.i2i.employeemanagement.department.controller;

import java.util.Map;
import java.util.Scanner;

import com.i2i.employeemanagement.department.service.DepartmentService;
import com.i2i.employeemanagement.department.service.DepartmentServiceImpl;
import com.i2i.employeemanagement.exception.EmployeeException;
import com.i2i.employeemanagement.model.Department;
import com.i2i.employeemanagement.model.Employee;
import com.i2i.employeemanagement.util.EmployeeValidator;

/**
 * This class  manage all the input and output function with CRUD operations. 
 * @author paari
 */
public class DepartmentController {
    private DepartmentService departmentService = new DepartmentServiceImpl();
    private EmployeeValidator validation = new EmployeeValidator();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Display the operation in the department.
     */
    public void departmentFunction() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("Choose an option: "
                    + "1 - Create a Department\t"
                    + "2 - View All Departments\t"
                    + "3 - Update a Department\t"
                    + "4 - Delete a Department\t"
                    + "5 - Display Employees by Department\t"
                    + "6 - Exit");
            int option = scanner.nextInt();
            scanner.nextLine();
         
            try {
                switch (option) {
                    case 1:
                        createDepartment();
                        break;
                    case 2:
                        displayDepartments();
                        break;
                    case 3:
                        updateDepartment();
                        break;
                    case 4:
                        deleteDepartment();
                        break;
                    case 5:
                        viewEmployeesByDepartment();
                        break;
                    case 6:
                        isRunning = false;
                        break;
                    default:
                        System.out.println("Invalid Choice. Please try again.");
                        break;
                }
            } catch (EmployeeException e){
                System.out.println(e.getMessage());
            } 
        }
    }

    /**
     * Method to add a department to the department storage.
     */
    public void createDepartment() throws EmployeeException  {
        String departmentName;
        do {
            System.out.println("Enter Department Name: ");
            departmentName = scanner.nextLine();
        } while (validation.stringValidator( departmentName ));
        departmentService.createDepartment(departmentName);
        System.out.println("Department created successfully!");
    }

    /**
     * Displays all the departments .
     */
    public void displayDepartments() throws EmployeeException {
        Map<Integer, Department> departmentMap = departmentService.getAllDepartments();
        String format = "| %-15s | %-15s |\n";
        System.out.format(format, "Department ID", "Department Name");
        
        if (!departmentMap.isEmpty()) {
            for (Department department : departmentMap.values()) {
                if (!department.getIsDeleted()) {
                    System.out.format(format, department.getDepartmentId(),
                    department.getDepartmentName());
                }
            }
        } else {
            System.out.println("No departments available.");
        }
    }

    /**
     * This Method will update the department based on the user input.
     */
    public void updateDepartment() throws EmployeeException {
        displayDepartments();
        System.out.println("Enter the Department ID you want to update:");
        int departmentId = scanner.nextInt();
        scanner.nextLine();
        
        Department department = departmentService.getDepartmentById(departmentId);
        if (department != null) {
            System.out.println("Enter the new Department Name:");
            String departmentName = scanner.nextLine();
            department.setDepartmentName(departmentName);
            departmentService.updateDepartment(departmentId, department);
            System.out.println("Department updated successfully.");
        } else {
            System.out.println("Department ID not found. Please try again.");
        }
    }

    /**
     * This method will delete the department according to the user input.
     */
    public void deleteDepartment() throws EmployeeException {
        if (departmentService.checkDepartment()) {
            System.out.println("No departments available. Add department .");
        } else {
            displayDepartments();
            System.out.println("Enter the Department ID you want to delete:");
            int departmentId = scanner.nextInt();
            scanner.nextLine();
            if(departmentService.deleteDepartment(departmentId)) {
                System.out.println("Department deleted successfully.");
            }
        }
    }
    
    /*
     * This method is to check the department empty.
     * @return boolean
     *     True if the department is empty else false.
     */
    public boolean isDepartmentEmpty() throws EmployeeException {
        return departmentService.checkDepartment();      
    }

    /**
     * Displays employees by department.
     */
    public void viewEmployeesByDepartment() throws EmployeeException {
        displayDepartments();
        System.out.println("Enter the Department ID you want to view:");
        int departmentId = scanner.nextInt();
        scanner.nextLine();
        
        Map<Integer, Employee> employees = departmentService.getEmployeeByDepartment(departmentId);
        
        String format = "| %-15s | %-20s | %-15s | %-15s|\n";
        System.out.format(format, "Employee ID", "Employee Name",
                              "Place","Department");
            
        if (!employees.isEmpty()) {
            for (Employee employee : employees.values()) {
                System.out.format(format, employee.getId(),employee.getName(), 
                                     employee.getPlace(),departmentService.getDepartmentById(departmentId).getDepartmentName());
            }
        } else {
                System.out.println("No employees found in this department.");
        }
    }

}
