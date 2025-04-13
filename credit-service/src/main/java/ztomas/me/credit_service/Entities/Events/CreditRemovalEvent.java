package ztomas.me.credit_service.Entities.Events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditRemovalEvent implements CreditEvent {
    private Integer amount;
}
