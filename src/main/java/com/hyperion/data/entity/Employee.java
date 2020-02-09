package com.hyperion.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    public Employee(String name, String position, String department) {
        this.name = name;
        this.position = position;
        this.department = department;
    }

    @Id
    @GeneratedValue
    private long employeeId;
    private String name;
    private String position;
    private String department;
    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Employee_Extra_Work",
            joinColumns = { @JoinColumn(name = "EMPLOYEEID") },
            inverseJoinColumns = { @JoinColumn(name = "EXTRAWORKID") }
    )
    private List<ExtraWork> employeeExtraWork;

}
