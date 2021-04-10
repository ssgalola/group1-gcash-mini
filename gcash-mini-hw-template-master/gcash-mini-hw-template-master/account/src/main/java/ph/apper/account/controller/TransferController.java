package ph.apper.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ph.apper.account.exception.BalanceInsufficientException;
import ph.apper.account.payload.TransferMoneyRequest;
import ph.apper.account.payload.response.AuthenticateResponse;
import ph.apper.account.payload.response.TransferMoneyResponse;
import ph.apper.account.service.AccountService;
import ph.apper.account.service.TransferService;
//import ph.apper.activity.payload.Activity;

import static java.lang.Integer.parseInt;

@RestController
@RequestMapping("transfer")
public class TransferController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    private final RestTemplate restTemplate;
    private final TransferService transferService;

    public TransferController(RestTemplate restTemplate, TransferService transferService, AccountService accountService) {
        this.restTemplate = restTemplate;
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Object> transfer(@RequestBody TransferMoneyRequest request) throws BalanceInsufficientException {
        TransferMoneyResponse transfer = transferService.transfer(request);
        if (transfer != null) {
            LOGGER.info("NEW MONEY TRANSFER: " + request.getAmount()
                    + " FROM " + request.getFromAccountId()
                    + " TO " + request.getToAccountId());

            //        Activity activity = new Activity();
            //        activity.setAction("TRANSFER MONEY");
            //        activity.setIdentifier(response.getTransferId());
            //        activity.setDetails("NEW MONEY TRANSFER: " + request.getAmount()
            //                            + " FROM " + request.getFromAccountId()
            //                            + " TO " + request.getToAccountId());
            //
            //        ResponseEntity<Activity[]> activityResponse = restTemplate.postForEntity("http://localhost:8082", activity, Activity[].class);
            //        if (activityResponse.getStatusCode().is2xxSuccessful()) {
            //            LOGGER.info("Transfer money activity recorded.");
            //        }
            //        else {
            //            LOGGER.error("Err: " + activityResponse.getStatusCode());
            //        }
            return new ResponseEntity<>("Money transferred successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Insufficient balance.", HttpStatus.FORBIDDEN);
    }
}