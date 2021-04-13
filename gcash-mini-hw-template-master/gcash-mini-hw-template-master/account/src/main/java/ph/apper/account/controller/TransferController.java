package ph.apper.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ph.apper.account.domain.Activity;
import ph.apper.account.exceptions.InsufficientBalanceException;
import ph.apper.account.exceptions.InvalidAccountRequestException;
import ph.apper.account.payload.TransferRequest;
import ph.apper.account.payload.response.TransferResponse;
import ph.apper.account.service.AccountService;
import ph.apper.account.service.TransferService;
import ph.apper.account.util.ActivityService;


@RestController
@RequestMapping("transfer")
public class TransferController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    private final TransferService transferService;
    private final ActivityService activityService;
    private final AccountService accountService;

    public TransferController(TransferService transferService, ActivityService activityService, AccountService accountService) {
        this.transferService = transferService;
        this.activityService = activityService;
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Object> transfer(@RequestBody TransferRequest request) throws InvalidAccountRequestException, InsufficientBalanceException, JsonProcessingException {
        LOGGER.info("Money Transfer request received");

        double senderBalance = accountService.getAccountDetails(request.getFromAccountId()).getBalance();
        double recipientBalance = accountService.getAccountDetails(request.getToAccountId()).getBalance();

        if (senderBalance >= request.getAmount()) {
            TransferResponse response = transferService.transfer(request);

            Activity activity = new Activity();
            activity.setAction("TRANSFER MONEY");
            activity.setIdentifier(response.getTransferId());
            activity.setDetails("NEW MONEY TRANSFER: " + request.getAmount() +
                                " FROM " + request.getFromAccountId() +
                                " TO " + request.getToAccountId());
            activityService.submitActivity(activity);

            Double newSenderBalance = senderBalance - request.getAmount();
            accountService.updateBalance(request.getFromAccountId(), newSenderBalance);
            Activity updateSenderBalance = new Activity();
            updateSenderBalance.setAction("UPDATE BALANCE");
            updateSenderBalance.setIdentifier(request.getFromAccountId());
            updateSenderBalance.setDetails("NEW ACCOUNT BALANCE: " + newSenderBalance);
            activityService.submitActivity(updateSenderBalance);

            Double newRecipientBalance = recipientBalance + request.getAmount();
            accountService.updateBalance(request.getToAccountId(), newRecipientBalance);
            Activity updateRecipientBalance = new Activity();
            updateRecipientBalance.setAction("UPDATE BALANCE");
            updateRecipientBalance.setIdentifier(request.getToAccountId());
            updateRecipientBalance.setDetails("NEW ACCOUNT BALANCE: " + newRecipientBalance);
            activityService.submitActivity(updateRecipientBalance);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new InsufficientBalanceException("Insufficient Balance in Account.");
    }

}