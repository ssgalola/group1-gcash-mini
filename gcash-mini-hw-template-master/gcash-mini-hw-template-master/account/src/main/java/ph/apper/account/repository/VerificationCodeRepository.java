package ph.apper.account.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ph.apper.account.domain.VerificationCode;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends CrudRepository<VerificationCode, Long>{
    Optional<VerificationCode> findByEmailAndCode(String email, String verificationCode);

}