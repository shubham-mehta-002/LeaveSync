package com.emplyeeMgtSystem.EmpMgtSys.request;

import jakarta.annotation.Nullable;
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
public class StudentRequest {
    private String name;
    private String email;
    private String mobile;  
    @Nullable
    private Long trainerId;
}
