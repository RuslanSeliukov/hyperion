package com.hyperion.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
public class ExtraWork {

    public ExtraWork() {}

    public ExtraWork(String workClass, String workName, String description, String dateStart, String dateEnd, List<Employee> employees, String amountOfTime) {
        this.workClass = workClass;
        this.workName = workName;
        this.description = description;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.employees = employees;
        this.amountOfTime = amountOfTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long extraWorkId;
    private String workClass;
    private String workName;
    private String description;
    private String dateStart;
    private String dateEnd;
    @ManyToMany(cascade = { CascadeType.ALL }, mappedBy = "employeeExtraWork", fetch = FetchType.EAGER)
    private List<Employee> employees;
    private String amountOfTime;

}
