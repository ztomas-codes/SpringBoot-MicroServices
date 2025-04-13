
package ztomas.me.credit_service.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ztomas.me.credit_service.Entities.EventStore;
import ztomas.me.credit_service.Entities.ReadModels.CreditAccount;

@Repository
public interface CreditAccountRepository extends JpaRepository<CreditAccount, Integer> {
        Optional<CreditAccount> findByEmployeeId(Integer employeeId);
}

    
