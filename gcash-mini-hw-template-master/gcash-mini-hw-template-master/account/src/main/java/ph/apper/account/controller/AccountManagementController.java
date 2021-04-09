package ph.apper.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ph.apper.account.domain.Account;
import ph.apper.account.payload.AccountRequest;
import ph.apper.account.payload.AuthenticateAccountRequest;
import ph.apper.account.payload.VerifyAccountRequest;
import ph.apper.account.payload.response.AuthenticateResponse;
import ph.apper.account.payload.response.NewAccountResponse;
import ph.apper.account.service.AccountService;


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
    public ResponseEntity<Object> getAccount(@PathVariable("accountId") String accountId){
        return new ResponseEntity<>(accountService.getAccountDetails(accountId), HttpStatus.OK);
    }

    @PostMapping("verify")
    public ResponseEntity<Object> verifyAccount(@RequestBody VerifyAccountRequest request){
        if(accountService.verifyAccount(request.getVerificationCode(), request.getEmail()))
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
}

