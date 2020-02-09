package com.hyperion;

import com.hyperion.data.dao.EmployeeDao;
import com.hyperion.data.dao.ExtraWorkDao;
import com.hyperion.data.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    ExtraWorkDao extraWorkDao;
    @Autowired
    EmployeeDao employeeDao;

    @Override
    public void run(String... strings) {
        /*employeeDao.saveEmployee(new Employee("Осипов Максим", "Ассистент", "Перевозки"));
        employeeDao.saveEmployee(new Employee("Городецкий Стефан ", "Менеджер по продажам", "Продаж"));
        employeeDao.saveEmployee(new Employee("Ситников Евсей ", "Лоббист", "Финансов"));
        employeeDao.saveEmployee(new Employee("Васильев Зураб", "Менеджер по закупкам", "Финансов"));
        employeeDao.saveEmployee(new Employee("Уваров Святослав", "Бухгалтер", "Бухгалтерия"));*/
    }
}
