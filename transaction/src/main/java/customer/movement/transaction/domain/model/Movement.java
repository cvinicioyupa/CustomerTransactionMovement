package customer.movement.transaction.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movement {
    private int id;
    private Date date;
    private String type;
    private double amount;
    private double balance;
    private boolean status;
    private int accountNumber;
}
