package ztomas.me.employee_service.Models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ztomas.me.employee_service.Entities.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class EmployeeCreateRequest {

    @NotEmpty
    @NotBlank
    private String firstName;

    @NotEmpty
    @NotBlank
    private String lastName;


    @NotNull
    private Gender gender;

    @Past(message = "birth date needs to past")
    private LocalDate birthDate;

    @PastOrPresent(message = "hire date needs to past")
    private LocalDate hireDate;

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 20)
    private String password;

    @NotEmpty
    @NotBlank
    @Email
    @Size(min = 3)
    private String email;
}
