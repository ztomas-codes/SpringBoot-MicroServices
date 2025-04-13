package ztomas.me.employee_service.Entities;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String cuRefNo;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Past(message = "birth date cannot be not set")
    private LocalDate birthDate;

    @PastOrPresent(message = "hire date cannot be not set")
    private LocalDate hireDate;

    @NotEmpty
    private String password;

    @NotEmpty
    @Email
    private String email;

    @OneToMany(mappedBy = "employee")
    private List<SessionToken> sessionTokens;

    @PrePersist
    public void generateCuRefNo() {
        if (this.cuRefNo == null || this.cuRefNo.isEmpty()) {
            this.cuRefNo = UUID.randomUUID().toString(); 
        }
    }

}
