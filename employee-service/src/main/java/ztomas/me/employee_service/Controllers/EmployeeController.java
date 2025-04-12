package ztomas.me.employee_service.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ztomas.me.employee_service.Models.EmployeeCreateRequest;
import ztomas.me.employee_service.Models.EmployeeResponse;
import ztomas.me.employee_service.Services.EmployeeService;

@RestController
@RequestMapping("/api/employee")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;

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
    public String deleteEmployee(@PathVariable (value= "id") Integer id)
    {
        return employeeService.deleteById(id);
    }

    @GetMapping()
    public List<EmployeeResponse> list()
    {
        return employeeService.listAll();
    }
}
