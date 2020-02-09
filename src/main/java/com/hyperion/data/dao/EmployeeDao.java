package com.hyperion.data.dao;

import com.hyperion.data.entity.Employee;

import java.util.List;

public interface EmployeeDao {
    Employee getEmployeeByName(String name);
    void saveEmployee(Employee employee);
    List<Employee> findAll();
    void updateEmployee(Employee employee);
}
