package ztomas.me.employee_service.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ztomas.me.employee_service.Models.ClientInfo;
import ztomas.me.employee_service.Models.EmployeeCreateRequest;
import ztomas.me.employee_service.Models.EmployeeResponse;
import ztomas.me.employee_service.Models.LoginRequest;
import ztomas.me.employee_service.Models.SessionTokenRequest;
import ztomas.me.employee_service.Models.SessionTokenResponse;
import ztomas.me.employee_service.Models.SuccessResponse;
import ztomas.me.employee_service.Services.EmployeeService;
import ztomas.me.employee_service.Services.SessionTokenService;

@RestController
@RequestMapping("/api/employee")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;
    private SessionTokenService sessionTokenService;

    @PostMapping
    public EmployeeResponse create(@RequestBody @Valid EmployeeCreateRequest empReq)
    {
        return employeeService.createEmployee(empReq);
    }

    @GetMapping("{id}")
    public EmployeeResponse getEmployee(@PathVariable(value = "id") Integer id)
    {
        return employeeService.getById(id);
    }

    @DeleteMapping("{id}")
    public SuccessResponse deleteEmployee(@PathVariable (value= "id") Integer id)
    {
        return employeeService.deleteById(id);
    }

    @GetMapping()
    public List<EmployeeResponse> list()
    {
        return employeeService.listAll();
    }

    @PostMapping("/login")
    public SessionTokenResponse login(@RequestBody @Valid LoginRequest req, HttpServletRequest httpReq )
    {
        return sessionTokenService.login(req, httpReq);
    }

    @PostMapping("/info")
    public EmployeeResponse checkSession(@RequestBody @Valid SessionTokenRequest req)
    {
        return sessionTokenService.checkSession(req);
    }

    @PostMapping("/sessions")
    public List<ClientInfo> activeSessions(@RequestBody @Valid SessionTokenRequest req)
    {
        Integer id = sessionTokenService.getEmployeeIdFromToken(req);
        return sessionTokenService.getActiveSessionsByEmployee(id);
    }
}
