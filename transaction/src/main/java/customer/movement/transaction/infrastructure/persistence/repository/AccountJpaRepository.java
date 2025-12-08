package customer.movement.transaction.infrastructure.persistence.repository;

import customer.movement.transaction.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity, Integer> {
    List<AccountEntity> findByClientIdentification(int clientIdentification);
}
