package ph.apper.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ph.apper.account.domain.Activity;
import ph.apper.account.exceptions.InsufficientBalanceException;
import ph.apper.account.exceptions.InvalidAccountRequestException;
import ph.apper.account.payload.TransferMoneyRequest;
import ph.apper.account.payload.response.TransferMoneyResponse;
import ph.apper.account.service.TransferService;
import ph.apper.account.util.ActivityService;


@RestController
@RequestMapping("transfer")
public class TransferController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    private final TransferService transferService;
    private final ActivityService activityService;

    public TransferController(TransferService transferService, ActivityService activityService) {
        this.transferService = transferService;
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<Object> transfer(@RequestBody TransferMoneyRequest request) throws InvalidAccountRequestException, InsufficientBalanceException {
        LOGGER.info("Money Transfer request received");
        TransferMoneyResponse transfer = transferService.transfer(request);

        if (transfer != null) {
            Activity activity = new Activity();
            activity.setAction("TRANSFER MONEY");
            activity.setIdentifier(transfer.getTransferId());
            activity.setDetails("NEW MONEY TRANSFER: " + request.getAmount() +
                                " FROM " + request.getFromAccountId() +
                                " TO " + request.getToAccountId());
            activityService.postActivity(activity);
            return new ResponseEntity<>("Money transferred successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Insufficient balance.", HttpStatus.FORBIDDEN);
    }

}