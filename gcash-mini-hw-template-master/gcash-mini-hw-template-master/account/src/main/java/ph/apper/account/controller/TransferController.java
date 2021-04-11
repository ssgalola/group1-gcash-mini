package ph.apper.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ph.apper.account.domain.Activity;
import ph.apper.account.exceptions.InsufficientBalanceException;
import ph.apper.account.exceptions.InvalidAccountRequestException;
import ph.apper.account.payload.TransferMoneyRequest;
import ph.apper.account.payload.response.TransferMoneyResponse;
import ph.apper.account.service.AccountService;
import ph.apper.account.service.TransferService;
import ph.apper.account.util.ActivityService;


@RestController
@RequestMapping("transfer")
public class TransferController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    private final RestTemplate restTemplate;
    private final TransferService transferService;
    private final ActivityService activityService;

    public TransferController(RestTemplate restTemplate, TransferService transferService, ActivityService activityService) {
        this.restTemplate = restTemplate;
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
            ResponseEntity<Activity[]> activityResponse = postActivity(activity);
            if (activityResponse.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("Money transfer activity recorded.");
            } else {
                LOGGER.error("Err: " + activityResponse.getStatusCode());
            }
            return new ResponseEntity<>("Money transferred successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Insufficient balance.", HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<Activity[]> postActivity(Activity activity) {
        return restTemplate.postForEntity("http://localhost:8082", activity, Activity[].class);
    }
}