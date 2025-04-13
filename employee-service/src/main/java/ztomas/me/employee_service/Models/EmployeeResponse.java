package ztomas.me.employee_service.Models;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ztomas.me.employee_service.Entities.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponse {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private Gender gender;


    @Past(message = "birth date needs to past")
    private LocalDate birthDate;

    @PastOrPresent(message = "hire date needs to past")
    private LocalDate hireDate;

    @NotEmpty
    private String cuRefNo;
}
