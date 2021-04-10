package ph.apper.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ph.apper.account.domain.Account;
import ph.apper.account.exceptions.InvalidAccountRequestException;
import ph.apper.account.payload.*;
import ph.apper.account.payload.response.AuthenticateResponse;
import ph.apper.account.payload.response.NewAccountResponse;
import ph.apper.account.payload.response.UpdateBalanceResponse;
import ph.apper.account.service.AccountService;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("account")
public class AccountManagementController {
    private static  final Logger LOGGER = LoggerFactory.getLogger(AccountManagementController.class);
    private final AccountService accountService;

    public AccountManagementController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Object> createAccount(@RequestBody AccountRequest request){
        LOGGER.info("Create account request received.");
        NewAccountResponse response = accountService.addAccount(request);
        LOGGER.info("Account Created");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{accountId}")
    public ResponseEntity<Object> getAccount(@PathVariable("accountId") String accountId) throws InvalidAccountRequestException {
        return new ResponseEntity<>(accountService.getAccountDetails(accountId), HttpStatus.OK);
    }

    @PostMapping("verify")
    public ResponseEntity<Object> verifyAccount(@RequestBody VerifyAccountRequest request) throws InvalidAccountRequestException{
        if(accountService.verifyAccount(request.getEmail(), request.getVerificationCode()))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>("Invalid email or verification code.", HttpStatus.NOT_FOUND);
    }

    @PostMapping("authenticate")
    public ResponseEntity<Object> authenticateAccount(@RequestBody AuthenticateAccountRequest request){
        Account account = accountService.authenticateAccount(request.getEmail(), request.getPassword());
        if(account != null)
            return new ResponseEntity<>(new AuthenticateResponse(account), HttpStatus.OK);
        return new ResponseEntity<>("Invalid email or password.", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("{accountId}")
    public ResponseEntity<Object> updateBalance(
            @PathVariable("accountId") String accountId,
            @RequestBody UpdateBalanceRequest request) throws InvalidAccountRequestException {
        LOGGER.info("Update account balance request received.");
        UpdateBalanceResponse response = accountService.updateBalance(accountId, request.getNewBalance());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("addFunds")
    public ResponseEntity<Object> addFunds(@RequestBody AddFundsRequest request) throws InvalidAccountRequestException{
        LOGGER.info("Add funds request received");
        Account account = accountService.getAccountById(request.getAccountId());
        Double updatedBalance = account.getBalance() + request.getAmount();
        UpdateBalanceResponse response = accountService.updateBalance(request.getAccountId(), updatedBalance);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

