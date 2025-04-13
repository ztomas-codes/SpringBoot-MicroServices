- CQRS
- Event Sorcing
- Kafka
- LoadBalancer ( **Round Robin**)
- Queues ( Dead Queues)


# MicroService Architektura ve Spring Bootu

#### Diagram
![Diagram](https://raw.githubusercontent.com/ztomas-codes/SpringBoot-MicroServices/refs/heads/main/diagram.svg)


Projekt, na kterém jsem se učil mikro servisní architekturu a zkoušel více psát škálovatelný software a pár dalších Patternů a vychytávek
- K8S (Kubernetees)
- Docker
- Spring Boot
- Java

## Credit Service - CQRS Pattern ( Command Query Separation )
Jelikož dělám aplikaci, kterou chci hodně škálovat, tak v něčem jako je Credit Systém nesmí chybět CQRS.
Funguje to tak, že je oddělený styl jakým se data zapisují a jakým se data čtou. Protože klasický CRUD není uplně optimální na zápis

### Zápis
Zapisují se události (eventy), přes tzv. Producer:
```java
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
     
```
do EventStore tabulky, a dále pošle event na **Kafku**. Mám potom listener který poslouchá od kafky eventy a dále s němi manipuluje..
```java
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
}```

Zde můžete vidět že odchytává eventy a zakládá potom ReadModely na čtení v tabulce credit_account (Nachovaný aktualní kreditový stav = Rychlé čtení).
A zápis je řešený takto, protože pro zápis je mnohem rychlejší přidávat, než dělat update nebo delete, jelikož by musel locknout řádek přepsat atd..

## Employee Service - Normalní CRUD

```txt
└── employee_service
     ├── Configs
     │   └── ModelMapperConfig.java
     ├── Controllers
     │   ├── EmployeeController.java
     │   └── EmployeeRestControllerAdvice.java
     ├── EmployeeServiceApplication.java
     ├── Entities
     │   ├── Employee.java
     │   └── Gender.java
     ├── Models
     │   ├── EmployeeCreateRequest.java
     │   └── EmployeeResponse.java
     ├── Repositories
     │   └── EmployeeRepository.java
     ├── Services
     │   └── EmployeeService.java
```

### Co jsem se naučil:
- Lazy v Java Stream API ( Vše se ve streamu zavolá až po .toList() ) např.:
```java
    public List<EmployeeResponse> listAll()
    {
        return employeeRepository
            .findAll()
            .stream()
            .map(e-> modelMapper.map(e, EmployeeResponse.class))
            .toList(); //Vše se krásně sputí až zde.. Narozdíl od Javascriptu kde to tak není
    }
```
- Ve Springbootu existuje tzv **RestControllerAdvice**, který je vlastně takový middleware pattern na error handling, kde se dá krásně error změnit na **Json Response**
```java
@RestControllerAdvice
public class EmployeeRestControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleRuntimeException(RuntimeException e)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("message", e.getMessage());
        return map;
    }
}
```
- Monad Pattern v RestControllerAdvice:
```java
    public EmployeeResponse getById(Integer id)
    {
        return employeeRepository.findById(id)
            .map(e-> modelMapper.map(e, EmployeeResponse.class))
            .orElseThrow(()-> new RuntimeException("Employee not found")); //<- Monad Pattern
    }
```
