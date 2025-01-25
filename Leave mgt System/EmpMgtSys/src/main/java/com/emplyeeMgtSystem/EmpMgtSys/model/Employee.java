package com.emplyeeMgtSystem.EmpMgtSys.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String mobile;
    private String designation;

    @OneToMany(mappedBy = "trainer",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Student> trainees = new ArrayList<>();

    @OneToMany(mappedBy = "trainer",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<LeaveRequest> leavesApproved =  new ArrayList();
}
