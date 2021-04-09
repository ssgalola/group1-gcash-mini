package ph.apper.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ph.apper.account.domain.Account;
import ph.apper.account.payload.response.GetAccountResponse;
import ph.apper.account.payload.response.NewAccountResponse;
import ph.apper.account.util.IdService;
import ph.apper.account.util.VerificationService;
import ph.apper.account.payload.AccountRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final List<Account> accounts = new ArrayList<>();
    private final IdService idService;
    private final VerificationService verificationService;

    public AccountService(IdService idService, VerificationService verificationService){
        this.idService = idService;
        this.verificationService = verificationService;
    }

    public NewAccountResponse addAccount(AccountRequest request){
        UUID accountId = IdService.getNextUserId();
        LOGGER.info("New account ID: " + accountId);

        Account account = new Account(accountId);
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setEmail(request.getEmail());
        account.setPassword(request.getPassword());
        account.setBalance(new BigDecimal("25000.00"));
        account.setVerified(false);

        String verificationCode = IdService.generateCode(6);
        verificationService.addVerificationCode(request.getEmail(), verificationCode);
        accounts.add(account);
        return new NewAccountResponse(verificationCode);
    }

    public void verify(String email){
        verificationService.verifyAccount(email);
        getAccount(email).setVerified(true);
    }

    public Account getAccount(String email){
        return accounts.stream().filter(account -> email.equals(account.getEmail())).findFirst().get();
    }

    public GetAccountResponse getAccountDetails(String accountId){
        return new GetAccountResponse(
                accounts.stream().filter(
                        account -> accountId.equals(account.getAccountId().toString())
                ).findFirst().get()
        );
    }

    public boolean verifyAccount(String verificationCode, String email){
        return accounts.stream().anyMatch(
                account -> account.getVerificationCode().equals(verificationCode) && account.getEmail().equals(email)
        );
    }

    public Account authenticateAccount(String email, String password){
        return accounts.stream().filter(
                account -> account.getEmail().equals(email) && account.getPassword().equals(password)
        ).findFirst().get();
    }

}
