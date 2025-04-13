package ztomas.me.employee_service.Models;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionTokenResponse {

    @NotEmpty
    private String token;

    private String sessionData;
}
