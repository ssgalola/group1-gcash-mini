package ph.apper.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ph.apper.account.domain.Account;
import ph.apper.account.exceptions.InvalidAccountRequestException;
import ph.apper.account.payload.response.GetAccountResponse;
import ph.apper.account.payload.response.NewAccountResponse;
import ph.apper.account.payload.response.UpdateBalanceResponse;
import ph.apper.account.util.IdService;
import ph.apper.account.util.VerificationService;
import ph.apper.account.payload.AccountRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        UUID accountId = idService.getNextUserId();
        LOGGER.info("New account ID: " + accountId);

        Account account = new Account(accountId.toString());
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setEmail(request.getEmail());
        account.setPassword(request.getPassword());
        account.setBalance(25000.00); // default value of accounts
        account.setVerified(false);

        String verificationCode = idService.generateCode(6);
        verificationService.addVerificationCode(request.getEmail(), verificationCode);
        accounts.add(account);
        return new NewAccountResponse(verificationCode);
    }

    public boolean verifyAccount(String email, String verificationCode) throws InvalidAccountRequestException{
        if(Objects.nonNull(verificationService.getVerificationCode(email))){
            if(verificationService.getVerificationCode(email).equals(verificationCode)){
                getAccount(email).setVerified(true);
                verificationService.invalidateVerificationCode(email);
                return true;
            }
        }
        return false;
    }

    public Account getAccount(String email) throws InvalidAccountRequestException {
        return accounts.stream()
                .filter(account -> email.equals(account.getEmail()))
                .findFirst()
                .orElseThrow(() -> new InvalidAccountRequestException("Account not found"));
    }

    public GetAccountResponse getAccountDetails(String accountId) throws InvalidAccountRequestException{
        return new GetAccountResponse(
                accounts.stream()
                        .filter(account -> accountId.equals(account.getAccountId().toString()))
                        .findFirst()
                        .orElseThrow(() -> new InvalidAccountRequestException("Account not found"))
        );
    }

    public Account getAccountById(String accountId) throws InvalidAccountRequestException{
        return accounts.stream()
                .filter(account -> accountId.equals(account.getAccountId().toString()))
                .findFirst()
                .orElseThrow(() -> new InvalidAccountRequestException("Account not found"));

    }

    public Account authenticateAccount(String email, String password){
        return accounts.stream().filter(
                account -> account.getEmail().equals(email) && account.getPassword().equals(password)
        ).findFirst().get();
    }

    public UpdateBalanceResponse updateBalance(String accountId, Double newBalance) throws InvalidAccountRequestException{
        Account account = getAccountById(accountId);
        account.setBalance(newBalance);
        return new UpdateBalanceResponse(newBalance);
    }

}
