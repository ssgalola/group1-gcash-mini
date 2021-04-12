package ph.apper.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ph.apper.account.domain.Account;
import ph.apper.account.domain.VerificationCode;
import ph.apper.account.exceptions.InvalidAccountRequestException;
import ph.apper.account.exceptions.InvalidLoginException;
import ph.apper.account.exceptions.InvalidUserRegistrationException;
import ph.apper.account.exceptions.InvalidVerificationRequestException;
import ph.apper.account.payload.AccountRequest;
import ph.apper.account.payload.response.GetAccountResponse;
import ph.apper.account.payload.response.NewAccountResponse;
import ph.apper.account.payload.response.UpdateBalanceResponse;
import ph.apper.account.repository.AccountRepository;
import ph.apper.account.repository.VerificationCodeRepository;
import ph.apper.account.util.IdService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Profile({"dev", "prod"})
public class AccountServiceImpl implements AccountServiceInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    public AccountServiceImpl(AccountRepository accountRepository, VerificationCodeRepository verificationCodeRepository) {
        this.accountRepository = accountRepository;
        this.verificationCodeRepository = verificationCodeRepository;
    }
    @Override
    public NewAccountResponse addAccount(AccountRequest request) throws InvalidUserRegistrationException {
        Optional<Account> accountQry = accountRepository
                .findByEmail(request.getEmail());

        if(accountQry.isPresent()){
            throw new InvalidUserRegistrationException("Account exists.");
        }

        UUID accountId = IdService.getNextUserId();
        LOGGER.info("New account ID: " + accountId);

        Account account = new Account(accountId.toString());
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setEmail(request.getEmail());
        account.setPassword(request.getPassword());
        account.setBalance(25000.00);
        account.setVerified(false);

        String verificationCode = IdService.generateCode(6);
        VerificationCode mappedCode = new VerificationCode(request.getEmail(), verificationCode);
        accountRepository.save(account);
        verificationCodeRepository.save(mappedCode);

        return new NewAccountResponse(verificationCode);
    }

    @Override
    public boolean verifyAccount(String email, String verificationCode) throws InvalidVerificationRequestException {
        Optional<Account> accountQry = accountRepository.findByEmail(email);
        if(accountQry.isPresent() && accountQry.get().isVerified())
            throw new InvalidVerificationRequestException("Account already verified");

        Optional<VerificationCode> verificationQry = verificationCodeRepository.findByEmailAndCode(email,verificationCode);
        if(verificationQry.isPresent()){
//            Account account = accountRepository.findByEmail(email).get();
            accountQry.get().setVerified(true);
            accountQry.get().setDateVerified(LocalDateTime.now());
            accountRepository.save(accountQry.get());
            verificationCodeRepository.delete(verificationQry.get());
            return true;
        }
        return false;
    }
    @Override
    public Account getAccount(String email) throws InvalidAccountRequestException {
        Optional<Account> accountQry = accountRepository.findByEmail(email);
        if(accountQry.isPresent())
            return accountQry.get();

        throw new InvalidAccountRequestException("Account not found");
    }

    @Override
    public Account getAccountById(String accountId) throws InvalidAccountRequestException {
        Optional<Account> accountQry = accountRepository.findByAccountId(accountId);
        if(accountQry.isPresent())
            return accountQry.get();

        throw new InvalidAccountRequestException("Account not found");
    }

    @Override
    public GetAccountResponse getAccountDetails(String accountId) throws InvalidAccountRequestException{
        Optional<Account> accountQry = accountRepository.findById(accountId);
        if(accountQry.isPresent())
            return new GetAccountResponse(accountQry.get());

        throw new InvalidAccountRequestException("Account not found");
    }



    @Override
    public Account authenticateAccount(String email, String password) throws InvalidLoginException {
        Optional<Account> accountQry = accountRepository.findByEmail(email);
        if(accountQry.isPresent()){
            if(password.equals(accountQry.get().getPassword())){
                if(accountQry.get().isVerified()){
                    accountQry.get().setLastLogin(LocalDateTime.now());
                    accountRepository.save(accountQry.get());
                    return accountQry.get();
                }else{
                    throw new InvalidLoginException("Account is not verified.");
                }
            }
        }
        throw new InvalidLoginException("Invalid Login credentials.");
    }

    @Override
    public UpdateBalanceResponse updateBalance(String accountId, Double newBalance) throws InvalidAccountRequestException{
        Optional<Account> accountQry = accountRepository.findById(accountId);
        accountQry.get().setBalance(newBalance);
        return new UpdateBalanceResponse(newBalance);
    }

}
