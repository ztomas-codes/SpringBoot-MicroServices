package ztomas.me.employee_service.Services;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ztomas.me.employee_service.Entities.Employee;
import ztomas.me.employee_service.Entities.SessionToken;
import ztomas.me.employee_service.Models.ClientInfo;
import ztomas.me.employee_service.Models.EmployeeResponse;
import ztomas.me.employee_service.Models.LoginRequest;
import ztomas.me.employee_service.Models.SessionTokenRequest;
import ztomas.me.employee_service.Models.SessionTokenResponse;
import ztomas.me.employee_service.Repositories.EmployeeRepository;
import ztomas.me.employee_service.Repositories.SessionTokenRepository;
import ztomas.me.employee_service.Utils.PasswordUtils;

@Service
@AllArgsConstructor
public class SessionTokenService {
    private final SessionTokenRepository sessionTokenRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public SessionTokenResponse login(@Valid LoginRequest req, HttpServletRequest httpReq)
    {
        Employee e = employeeRepository
            .findByEmail(req.getEmail())
            .orElseThrow(()-> new RuntimeException("Wrong credentials"));

        if(!PasswordUtils.checkPassword(req.getPassword(), e.getPassword()))
        {
            throw new RuntimeException("Wrong credentials");
        }

        ObjectMapper mapper = new ObjectMapper();

        String data;
        try {
            data = mapper.writeValueAsString(getClientInfo(httpReq));
        } catch(JsonProcessingException er) 
        {
            data = "{}";
        }

        SessionToken sessionToken= SessionToken.builder()
            .employee(e)
            .sessionData(data)
            .build();
        
        return SessionTokenResponse.builder().token(
            sessionTokenRepository.save(sessionToken).getToken()
        ).build();
    }

    public List<ClientInfo> getActiveSessionsByEmployee(Integer id)
    {
        ObjectMapper mapper = new ObjectMapper();
        return sessionTokenRepository
            .findAllByEmployeeId(id)
            .stream()
            .map(x -> {
                try{
                    return mapper.readValue(x.getSessionData(), ClientInfo.class);
                } catch (JsonProcessingException e) {
                    return null;
                }
            })
        .toList();
    }

    public Integer getEmployeeIdFromToken(SessionTokenRequest token)
    {
        SessionToken sessionToken = sessionTokenRepository
            .findByToken(token.getToken())
            .orElseThrow(()-> new RuntimeException("Session not found"));
        return sessionToken.getEmployee().getId();
    }

    public EmployeeResponse checkSession(SessionTokenRequest token)
    {
        SessionToken sessionToken = sessionTokenRepository
            .findByToken(token.getToken())
            .orElseThrow(()-> new RuntimeException("Session not found"));
        return modelMapper.map(sessionToken.getEmployee(), EmployeeResponse.class);
    }

    private ClientInfo getClientInfo(HttpServletRequest httpReq) {
        String userAgent = httpReq.getHeader("User-Agent");
        System.out.println(userAgent);
        String browser = detectBrowser(userAgent);
        String os = detectOS(userAgent);

        String ip = httpReq.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = httpReq.getRemoteAddr();
        }

        String location = "Unknown";
        String country = "Unknown";

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://ipapi.co/" + ip + "/json/";
            Map<String, String> response = restTemplate.getForObject(url, Map.class);
            if (response != null) {
                location = response.get("city") + ", " + response.get("region");
                country = response.get("country_name");
            }
        } catch (Exception e) {
        }

        System.out.println(browser);
        System.out.println(os);

        return new ClientInfo(browser, os, location, country);
    }

    private String detectBrowser(String userAgent) {
        if (userAgent == null) return "Unknown";
        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("edg")) return "Edge";
        else if (userAgent.contains("chrome")) return "Chrome";
        else if (userAgent.contains("safari") && !userAgent.contains("chrome")) return "Safari";
        else if (userAgent.contains("firefox")) return "Firefox";
        else if (userAgent.contains("msie") || userAgent.contains("trident")) return "Internet Explorer";
        else return "Unknown";
    }

    private String detectOS(String userAgent) {
        if (userAgent == null) return "Unknown";
        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("windows")) return "Windows";
        else if (userAgent.contains("mac")) return "macOS";
        else if (userAgent.contains("x11") || userAgent.contains("linux")) return "Linux";
        else if (userAgent.contains("android")) return "Android";
        else if (userAgent.contains("iphone") || userAgent.contains("ipad")) return "iOS";
        else return "Unknown";
    }

}
