package ph.apper.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ph.apper.account.domain.Account;
import ph.apper.account.domain.Activity;
import ph.apper.account.exceptions.InvalidAccountRequestException;
import ph.apper.account.payload.*;
import ph.apper.account.payload.response.AuthenticateResponse;
import ph.apper.account.payload.response.NewAccountResponse;
import ph.apper.account.payload.response.UpdateBalanceResponse;
import ph.apper.account.service.AccountService;
import ph.apper.account.util.ActivityService;

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
    public ResponseEntity<Object> createAccount(@RequestBody AccountRequest request){
        LOGGER.info("Create account request received.");
        NewAccountResponse response = accountService.addAccount(request);
        LOGGER.info("Account Created");

        Activity activity = new Activity();
        activity.setAction("REGISTRATION");
        activity.setIdentifier(request.getEmail());
        activity.setDetails("NEW ACCOUNT CREATED: " + request.getEmail());
        activityService.postActivity(activity);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{accountId}")
    public ResponseEntity<Object> getAccount(@PathVariable("accountId") String accountId) throws InvalidAccountRequestException {
        return new ResponseEntity<>(accountService.getAccountDetails(accountId), HttpStatus.OK);
    }

    @PostMapping("verify")
    public ResponseEntity<Object> verifyAccount(@RequestBody VerifyAccountRequest request) throws InvalidAccountRequestException{
        Activity activity = new Activity();
        activity.setAction("Verify Account");
        activity.setIdentifier(request.getEmail());

        if(accountService.verifyAccount(request.getEmail(), request.getVerificationCode())) {
            activity.setDetails("Account verified");
            activityService.postActivity(activity);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        activity.setDetails("Invalid email or verification code.");
        activityService.postActivity(activity);
        return new ResponseEntity<>("Invalid email or verification code.", HttpStatus.NOT_FOUND);
    }

    @PostMapping("authenticate")
    public ResponseEntity<Object> authenticateAccount(@RequestBody AuthenticateAccountRequest request){
        Account account = accountService.authenticateAccount(request.getEmail(), request.getPassword());

        Activity activity = new Activity();
        activity.setAction("Authenticate Account");
        activity.setIdentifier(account.getEmail());

        if(account != null){
            activity.setDetails("Correct email and password combination.");
            activityService.postActivity(activity);
            return new ResponseEntity<>(new AuthenticateResponse(account), HttpStatus.OK);
        }
        activity.setDetails("Invalid email and password combination.");
        activityService.postActivity(activity);
        return new ResponseEntity<>("Invalid email or password.", HttpStatus.FORBIDDEN);
    }

    @PostMapping("{accountId}")
    public ResponseEntity<Object> updateBalance(
            @PathVariable("accountId") String accountId,
            @RequestBody UpdateBalanceRequest request) throws InvalidAccountRequestException {
        LOGGER.info("Update account balance request received.");
        UpdateBalanceResponse response = accountService.updateBalance(accountId, request.getNewBalance());

        Activity activity = new Activity();
        activity.setAction("UPDATE BALANCE");
        activity.setIdentifier(accountId);
        activity.setDetails("NEW ACCOUNT BALANCE: " + request.getNewBalance());

        activityService.postActivity(activity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("addFunds")
    public ResponseEntity<Object> addFunds(@RequestBody AddFundsRequest request) throws InvalidAccountRequestException{
        LOGGER.info("Add funds request received");
        Account account = accountService.getAccountById(request.getAccountId());
        Double updatedBalance = account.getBalance() + request.getAmount();
        UpdateBalanceResponse response = accountService.updateBalance(request.getAccountId(), updatedBalance);

        Activity activity = new Activity();
        activity.setAction("ADD FUND");
        activity.setIdentifier(request.getAccountId());
        activity.setDetails("NEW BALANCE: " + updatedBalance);
        activityService.postActivity(activity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

