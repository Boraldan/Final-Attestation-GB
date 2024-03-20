package boraldan.bank.repository;

import boraldan.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
   // Метод для поиска счета по номеру кредитной карты
   Account findByCard(Long card);
}
