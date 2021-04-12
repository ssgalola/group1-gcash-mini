package ph.apper.account.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ph.apper.account.domain.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByAccountId(String accountId);

}
