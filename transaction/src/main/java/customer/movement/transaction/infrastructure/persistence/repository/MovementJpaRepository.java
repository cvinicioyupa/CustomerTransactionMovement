package customer.movement.transaction.infrastructure.persistence.repository;

import customer.movement.transaction.infrastructure.persistence.entity.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MovementJpaRepository extends JpaRepository<MovementEntity, Integer> {
    List<MovementEntity> findByAccountNumberAndDateBetween(int accountNumber, Timestamp startDate, Timestamp endDate);
}
