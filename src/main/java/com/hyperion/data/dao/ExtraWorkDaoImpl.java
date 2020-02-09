package com.hyperion.data.dao;

import com.hyperion.data.entity.Employee;
import com.hyperion.data.entity.ExtraWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class ExtraWorkDaoImpl implements ExtraWorkDao {

    @Autowired
    EntityManager em;

    @Override
    public void saveExtraWork(ExtraWork ew) {
        em.persist(ew);
    }

    @Override
    public List<Employee> getEmployeeByWorkClass(String workClass) {

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);

            Root<Employee> employee = cq.from(Employee.class);
        /*ListJoin<Employee, ExtraWork>*/



            /*Predicate employeeLastNamePredicate = cb.equal(employee.get("name"), name);
            cq.where(employeeLastNamePredicate);

            TypedQuery<Employee> query = em.createQuery(cq);
            List<Employee> result = query.getResultList();
            if (result != null || result.size() != 0)
                return result.get(0);
            return new Employee();*/
return new ArrayList<>();
    }

    @Override
    public List<ExtraWork> getExtraWorkByName(String extraWorkName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ExtraWork> cq = cb.createQuery(ExtraWork.class);

        Root<ExtraWork> extraWork = cq.from(ExtraWork.class);
        Predicate extraWorkClassPredicate = cb.equal(extraWork.get("workClass"), extraWorkName);
        cq.where(extraWorkClassPredicate);

        TypedQuery<ExtraWork> query = em.createQuery(cq);
        List<ExtraWork> result = query.getResultList();
        if (result != null || result.size() != 0)
            return result;
        return new ArrayList<ExtraWork>();
    }

    @Override
    public List<ExtraWork> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ExtraWork> cq = cb.createQuery(ExtraWork.class);
        Root<ExtraWork> rootEntry = cq.from(ExtraWork.class);
        CriteriaQuery<ExtraWork> all = cq.select(rootEntry);
        TypedQuery<ExtraWork> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }
}
