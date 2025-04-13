package ztomas.me.credit_service.Services;

import java.time.LocalDateTime;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import ztomas.me.credit_service.Entities.EventStore;
import ztomas.me.credit_service.Entities.EventTypes;
import ztomas.me.credit_service.Entities.Events.CreditEvent;
import ztomas.me.credit_service.Repositories.EventStoreRepository;

@Service
@AllArgsConstructor
public class CreditAccountEventProducer {
    private static final String TOPIC = "credit-accounts";
    private final KafkaTemplate<String, EventStore> kafkaTemplate;
    private final EventStoreRepository eventStoreRepository;

    public void createAndPushCreditEvent(CreditEvent event, Integer employeeId){
        ObjectMapper mapper = new ObjectMapper();
        String payload;
        try {
            payload = mapper.writeValueAsString(event);
        } catch (Exception e)
        {
            throw new RuntimeException("Deserialization failed: " + e.getMessage());
        }

        EventTypes type;
        switch(event.getClass().getSimpleName())
        {
            case "AccountCreationEvent":
                type = EventTypes.AccountCreation;
                break;
            case "CreditAdditionEvent":
                type = EventTypes.CreditAddition;
                break;
            case "CreditRemovalEvent":
                type = EventTypes.CreditRemoval;
                break;
            default:
                throw new RuntimeException("Incorrect type");
        }

        EventStore e = eventStoreRepository.save(EventStore
                .builder()
                .employeeId(employeeId)
                .eventType(type)
                .timestamp(LocalDateTime.now())
                .payload(payload)
                .build()
        );

        kafkaTemplate.send(TOPIC, e);
    }
}
