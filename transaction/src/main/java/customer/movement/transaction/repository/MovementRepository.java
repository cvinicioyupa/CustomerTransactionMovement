package customer.movement.transaction.repository;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import customer.movement.transaction.model.Movement;

public interface MovementRepository  extends JpaRepository<Movement, Integer> {

    List<Movement> findByAccountNumberAndDateBetween(int accountNumber, Timestamp startDate, Timestamp endDate);
}
