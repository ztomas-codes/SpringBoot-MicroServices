package ztomas.me.employee_service.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientInfo {

    private String browser;
    private String os;
    private String location;
    private String country;
}

