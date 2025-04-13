package ztomas.me.credit_service.Entities.Events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCreationEvent implements CreditEvent {
}
