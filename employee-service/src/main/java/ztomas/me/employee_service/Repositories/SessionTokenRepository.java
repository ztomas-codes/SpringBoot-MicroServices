package ztomas.me.employee_service.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ztomas.me.employee_service.Entities.SessionToken;

@Repository
public interface SessionTokenRepository extends JpaRepository<SessionToken, Integer>{
    Optional<SessionToken> findByToken(String token);
    List<SessionToken> findAllByEmployeeId(Integer employeeId);
}
