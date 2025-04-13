package ztomas.me.employee_service.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ztomas.me.employee_service.Entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String email);
}
