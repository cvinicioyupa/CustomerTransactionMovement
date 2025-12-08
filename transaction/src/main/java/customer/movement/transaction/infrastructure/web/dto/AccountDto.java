package customer.movement.transaction.infrastructure.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private int number;
    private String type;
    private double initialBalance;
    private boolean status;
    private int clientIdentification;
}
