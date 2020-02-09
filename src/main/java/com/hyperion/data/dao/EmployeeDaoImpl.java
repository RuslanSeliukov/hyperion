package com.hyperion.data.dao;

import com.hyperion.data.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class EmployeeDaoImpl implements EmployeeDao {

    @Autowired
    EntityManager em;

    @Override
    public Employee getEmployeeByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);

        Root<Employee> employee = cq.from(Employee.class);
        Predicate employeeLastNamePredicate = cb.equal(employee.get("name"), name);
        cq.where(employeeLastNamePredicate);

        TypedQuery<Employee> query = em.createQuery(cq);
        List<Employee> result = query.getResultList();
        if (result != null || result.size() != 0)
            return result.get(0);
        return new Employee();
    }

    @Override
    public void saveEmployee(Employee employee) {
        em.persist(employee);
    }

    @Override
    public List<Employee> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> rootEntry = cq.from(Employee.class);
        CriteriaQuery<Employee> all = cq.select(rootEntry);
        TypedQuery<Employee> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    public void updateEmployee(Employee employee) {
        em.merge(employee);
    }
}
