package com.emplyeeMgtSystem.EmpMgtSys.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "leave_request") 
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String remark; // by the trainer
    private String reason; // by the student
    private String status; // ["pending","approved","rejected"]
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "student_id") 
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "accepted_by")
    private Employee trainer;    

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "id=" + id +
                ", remark='" + remark + '\'' +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
