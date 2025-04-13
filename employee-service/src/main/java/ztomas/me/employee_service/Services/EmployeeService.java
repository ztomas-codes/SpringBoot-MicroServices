package ztomas.me.employee_service.Services;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ztomas.me.employee_service.Entities.Employee;
import ztomas.me.employee_service.Models.EmployeeCreateRequest;
import ztomas.me.employee_service.Models.EmployeeResponse;
import ztomas.me.employee_service.Models.SuccessResponse;
import ztomas.me.employee_service.Repositories.EmployeeRepository;
import ztomas.me.employee_service.Repositories.SessionTokenRepository;
import ztomas.me.employee_service.Utils.PasswordUtils;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final SessionTokenRepository sessionTokenRepository;
    private final ModelMapper modelMapper;

    public EmployeeResponse createEmployee(@Valid EmployeeCreateRequest empCrReq )
    {
        empCrReq.setPassword(PasswordUtils.hashPassword(empCrReq.getPassword()));
        Employee empReq = modelMapper.map(empCrReq, Employee.class);
        var savedEmp = employeeRepository.save(empReq);
        return modelMapper.map(savedEmp, EmployeeResponse.class);
    }

    public List<EmployeeResponse> listAll()
    {
        return employeeRepository
            .findAll()
            .stream()
            .map(e-> modelMapper.map(e, EmployeeResponse.class))
            .toList();
    }

    public EmployeeResponse getById(Integer id)
    {
        return employeeRepository.findById(id)
            .map(e-> modelMapper.map(e, EmployeeResponse.class))
            .orElseThrow(()-> new RuntimeException("Employee not found"));
    }

    public SuccessResponse deleteById(Integer id)
    {
         employeeRepository.deleteById(id);
         return SuccessResponse
             .builder() 
             .success(true)
             .message("Successfully deleted")
             .build();
    }
}
