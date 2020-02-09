package com.hyperion.data.dao;

import com.hyperion.data.entity.Employee;
import com.hyperion.data.entity.ExtraWork;

import java.util.List;

public interface ExtraWorkDao {
    void saveExtraWork(ExtraWork ew);
    List<Employee> getEmployeeByWorkClass(String workClass);
    List<ExtraWork> getExtraWorkByName(String extraWorkName);
    List<ExtraWork> findAll();
}
