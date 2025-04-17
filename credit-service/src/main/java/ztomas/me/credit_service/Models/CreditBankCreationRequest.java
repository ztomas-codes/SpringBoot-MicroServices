package ztomas.me.credit_service.Models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditBankCreationRequest {

    @NotNull
    @PositiveOrZero
    private Integer employeeId; 
}

