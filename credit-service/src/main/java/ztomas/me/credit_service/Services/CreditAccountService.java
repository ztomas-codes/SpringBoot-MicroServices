package ztomas.me.credit_service.Services;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import ztomas.me.credit_service.Entities.Events.AccountCreationEvent;
import ztomas.me.credit_service.Entities.Events.CreditAdditionEvent;
import ztomas.me.credit_service.Entities.Events.CreditRemovalEvent;
import ztomas.me.credit_service.Entities.ReadModels.CreditAccount;
import ztomas.me.credit_service.Repositories.CreditAccountRepository;

@Service
@AllArgsConstructor
public class CreditAccountService{
    private final CreditAccountRepository creditAccountRepository;
    private final CreditAccountEventProducer creditAccountEventProducer;

    public CreditAccount getAccount(Integer employeeId)
    {
        return creditAccountRepository
            .findByEmployeeId(employeeId)
            .orElseThrow(()-> new RuntimeException("Employee credit account not found"));
    }

    public void createAccount(Integer employeeId)
    {
        boolean exists = creditAccountRepository.findByEmployeeId(employeeId).isPresent();
        if (exists) throw new RuntimeException("Account already exists");
        creditAccountEventProducer.createAndPushCreditEvent(
                AccountCreationEvent
                    .builder()
                    .build()
                , employeeId);
    }

    public void addCreditsToAccount(Integer employeeId, Integer amount)
    {
        getAccount(employeeId);
        creditAccountEventProducer.createAndPushCreditEvent(
                CreditAdditionEvent
                    .builder()
                    .amount(amount)
                    .build()
                , employeeId);
    }

    public void removeCreditsFromAccount(Integer employeeId, Integer amount)
    {
        getAccount(employeeId);
        creditAccountEventProducer.createAndPushCreditEvent(
                CreditRemovalEvent
                    .builder()
                    .amount(amount)
                    .build()
                , employeeId);
    }
}
