package customer.movement.transaction.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Account {
    @Id
    private int number;
    private String type;
    private double initialBalance;
    private boolean status;
    private int clientIdentification;
}
