package com.emplyeeMgtSystem.EmpMgtSys.request;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeRequest {
    private String name;
    private String email;
    private String mobile; 
    private String designation;
    private List<Long> trainees = new ArrayList<>();
}
