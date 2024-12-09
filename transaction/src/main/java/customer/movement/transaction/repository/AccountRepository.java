package customer.movement.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import customer.movement.transaction.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findByClientIdentification(int clientIdentification); 
   

}
