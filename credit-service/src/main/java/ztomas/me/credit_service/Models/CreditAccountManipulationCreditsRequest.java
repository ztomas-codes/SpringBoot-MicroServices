package ztomas.me.credit_service.Models;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditAccountManipulationCreditsRequest {

    @NotNull
    @PositiveOrZero
    private Integer employeeId; 

    @NotNull
    @Positive
    private Integer amount; 
}

