package ph.apper.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ph.apper.account.domain.Account;
import ph.apper.account.domain.Activity;
import ph.apper.account.exceptions.InvalidAccountRequestException;
import ph.apper.account.exceptions.InvalidLoginException;
import ph.apper.account.exceptions.InvalidVerificationCodeException;
import ph.apper.account.payload.*;
import ph.apper.account.payload.response.AuthenticateResponse;
import ph.apper.account.payload.response.NewAccountResponse;
import ph.apper.account.payload.response.UpdateBalanceResponse;
import ph.apper.account.service.AccountService;
import ph.apper.account.util.ActivityService;

import javax.validation.Valid;

@RestController
@RequestMapping("account")
public class AccountManagementController {
    private static  final Logger LOGGER = LoggerFactory.getLogger(AccountManagementController.class);
    private final AccountService accountService;
    private final ActivityService activityService;

    public AccountManagementController(AccountService accountService, ActivityService activityService){
        this.accountService = accountService;
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<Object> createAccount(@Valid @RequestBody AccountRequest request) throws JsonProcessingException {
        LOGGER.info("Create account request received.");
        NewAccountResponse response = accountService.addAccount(request);
        LOGGER.info("Account Created");

        Activity activity = new Activity();
        activity.setAction("REGISTRATION");
        activity.setIdentifier(request.getEmail());
        activity.setDetails("NEW ACCOUNT CREATED: " + request.getEmail());
        activityService.submitActivity(activity);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{accountId}")
    public ResponseEntity<Object> getAccount(@PathVariable("accountId") String accountId) throws InvalidAccountRequestException {
        return new ResponseEntity<>(accountService.getAccountDetails(accountId), HttpStatus.OK);
    }

    @PostMapping("verify")
    public ResponseEntity<Object> verifyAccount(@RequestBody VerifyAccountRequest request) throws InvalidAccountRequestException, InvalidVerificationCodeException, JsonProcessingException {
        Activity activity = new Activity();
        activity.setAction("VERIFICATION");
        activity.setIdentifier(request.getEmail());

        if(accountService.verifyAccount(request.getEmail(), request.getVerificationCode())) {
            activity.setDetails("ACCOUNT VERIFIED:"+request.getEmail());
            activityService.submitActivity(activity);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid email or verification code.", HttpStatus.NOT_FOUND);
    }

    @PostMapping("authenticate")
    public ResponseEntity<Object> authenticateAccount(@RequestBody AuthenticateAccountRequest request) throws InvalidLoginException, InvalidAccountRequestException, JsonProcessingException {
        Account account = accountService.authenticateAccount(request.getEmail(), request.getPassword());
        Activity activity = new Activity();
        activity.setAction("AUTHENTICATION");
        activity.setIdentifier(account.getEmail());

        activity.setDetails("ACCOUNT AUTHENTICATED:"+account.getEmail());
        activityService.submitActivity(activity);
        return new ResponseEntity<>(new AuthenticateResponse(account), HttpStatus.OK);
    }

    @PostMapping("{accountId}")
    public ResponseEntity<Object> updateBalance(
            @PathVariable("accountId") String accountId,
            @RequestBody UpdateBalanceRequest request) throws InvalidAccountRequestException, JsonProcessingException {
        LOGGER.info("Update account balance request received.");
        UpdateBalanceResponse response = accountService.updateBalance(accountId, request.getNewBalance());

        Activity activity = new Activity();
        activity.setAction("UPDATE BALANCE");
        activity.setIdentifier(accountId);
        activity.setDetails("NEW ACCOUNT BALANCE: " + request.getNewBalance());

        activityService.submitActivity(activity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("addFunds")
    public ResponseEntity<Object> addFunds(@RequestBody AddFundsRequest request) throws InvalidAccountRequestException, JsonProcessingException {
        LOGGER.info("Add funds request received");
        Account account = accountService.getAccountById(request.getAccountId());
        Double updatedBalance = account.getBalance() + request.getAmount();
        UpdateBalanceResponse response = accountService.updateBalance(request.getAccountId(), updatedBalance);

        Activity activity = new Activity();
        activity.setAction("ADD FUND");
        activity.setIdentifier(request.getAccountId());
        activity.setDetails("NEW BALANCE: " + updatedBalance);
        activityService.submitActivity(activity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

