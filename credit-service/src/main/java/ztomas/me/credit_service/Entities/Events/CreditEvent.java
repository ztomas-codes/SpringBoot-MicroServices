package ztomas.me.credit_service.Entities.Events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreditAdditionEvent.class, name = "CreditAdditionEvent"),
        @JsonSubTypes.Type(value = AccountCreationEvent.class, name = "AccountCreationEvent") ,
        @JsonSubTypes.Type(value = CreditRemovalEvent.class, name = "CreditRemovalEvent") 
})
public interface CreditEvent {
}
