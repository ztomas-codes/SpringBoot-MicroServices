package ztomas.me.credit_service.Services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import ztomas.me.credit_service.Entities.EventStore;
import ztomas.me.credit_service.Entities.Events.CreditAdditionEvent;
import ztomas.me.credit_service.Entities.Events.CreditRemovalEvent;
import ztomas.me.credit_service.Entities.ReadModels.CreditAccount;
import ztomas.me.credit_service.Repositories.CreditAccountRepository;

@Service
@AllArgsConstructor
public class CreditAccountEventConsumer {
    private final CreditAccountRepository creditAccountRepository;

    @KafkaListener(topics = "credit-accounts", groupId = "credit-account-group")
    public void handleEvent(EventStore event) {
        switch(event.getEventType())
        {
            case AccountCreation:
                handleAccountCreation(event);
                break;
            case CreditAddition:
                handleCreditAddition(event);
                break;
            case CreditRemoval:
                handleCreditRemoval(event);
                break;
        }
    }

    private void handleAccountCreation(EventStore event)
    {
        CreditAccount acc = CreditAccount
            .builder()
            .employeeId(event.getEmployeeId())
            .balance(0)
            .build();
        creditAccountRepository.save(acc);
    }

    private void handleCreditAddition(EventStore event)
    {
        ObjectMapper mapper = new ObjectMapper();
        CreditAdditionEvent e;
        try { 
            e = mapper.readValue(event.getPayload(), CreditAdditionEvent.class );
        } catch (Exception exception)
        {
            //TODO: DEAD QUEUE
            throw new RuntimeException("Error while reading payload");
        }

        CreditAccount acc = creditAccountRepository.
            findByEmployeeId(event.getEmployeeId())
            .orElseThrow(()-> new RuntimeException("User not found")); //TODO: DEAD QUEUE
        acc.setBalance(acc.getBalance() + e.getAmount());
        creditAccountRepository.save(acc); 
    }

    private void handleCreditRemoval(EventStore event)
    {
        ObjectMapper mapper = new ObjectMapper();
        CreditRemovalEvent e;
        try { 
            e = mapper.readValue(event.getPayload(), CreditRemovalEvent.class );
        } catch (Exception exception)
        {
            //TODO: DEAD QUEUE
            throw new RuntimeException("Error while reading payload");
        }

        CreditAccount acc = creditAccountRepository.
            findByEmployeeId(event.getEmployeeId())
            .orElseThrow(()-> new RuntimeException("User not found")); //TODO: DEAD QUEUE
        acc.setBalance(acc.getBalance() - e.getAmount());
        creditAccountRepository.save(acc); 
    }
}
