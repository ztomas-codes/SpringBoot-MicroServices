package ztomas.me.employee_service.Services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ztomas.me.employee_service.Entities.Employee;
import ztomas.me.employee_service.Models.EmployeeCreateRequest;
import ztomas.me.employee_service.Models.EmployeeResponse;
import ztomas.me.employee_service.Repositories.EmployeeRepository;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeResponse createEmployee(@Valid EmployeeCreateRequest empCrReq )
    {
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

    public String deleteById(Integer id)
    {
         employeeRepository.deleteById(id);
         return "success";
    }
}
