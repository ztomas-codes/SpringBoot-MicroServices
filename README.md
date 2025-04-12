# MicroService Architektura ve Spring Bootu

Projekt, na kterém jsem se učil mikro servisní architekturu a zkoušel více psát škálovatelný software a pár dalších Patternů a vychytávek
- K8S (Kubernetees)
- Docker
- Spring Boot
- Java

## Employee Service

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
- CQRS
- Event Sorcing
- Kafka
- LoadBalancer ( **Round Robin**)
- Queues ( Dead Queues)
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
