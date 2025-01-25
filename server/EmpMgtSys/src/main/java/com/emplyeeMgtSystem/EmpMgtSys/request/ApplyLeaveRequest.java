package com.emplyeeMgtSystem.EmpMgtSys.request;

import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class ApplyLeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String reason; 
    private LocalDate from;
    private LocalDate endDate;
}
