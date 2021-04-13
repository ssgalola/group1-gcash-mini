package ph.apper.purchase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ph.apper.purchase.App;
import ph.apper.purchase.domain.Account;
import ph.apper.purchase.domain.Activity;
import ph.apper.purchase.domain.Product;
import ph.apper.purchase.exception.InsufficientBalanceException;
import ph.apper.purchase.exception.ProductNotFoundException;
import ph.apper.purchase.payload.PurchaseRequest;
import ph.apper.purchase.payload.UpdateBalanceRequest;
import ph.apper.purchase.payload.response.GetAccountResponse;
import ph.apper.purchase.payload.response.ProductData;
import ph.apper.purchase.payload.response.UpdateBalanceResponse;
import ph.apper.purchase.util.ActivityService;

@RestController
@RequestMapping("purchase")
public class PurchaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseController.class);
    private final RestTemplate restTemplate;
    private final App.GCashMiniProperties gCashMiniProperties;
    private final ActivityService activityService;

    public PurchaseController(RestTemplate restTemplate, App.GCashMiniProperties gCashMiniProperties, ActivityService activityService) {
        this.restTemplate = restTemplate;
        this.gCashMiniProperties = gCashMiniProperties;
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity purchase(@RequestBody PurchaseRequest request) throws ProductNotFoundException, HttpClientErrorException, InsufficientBalanceException, HttpMessageNotReadableException, JsonProcessingException {
        LOGGER.info(String.valueOf(request));

        ResponseEntity<ProductData> productResponse = restTemplate.getForEntity(gCashMiniProperties.getProductUrl() + request.getProductId(), ProductData.class);

        ProductData productData = productResponse.getBody();
        if (productResponse.getStatusCode().is2xxSuccessful()) {
            LOGGER.info("Success");
            Product product = new Product(request.getProductId());
            product.setProductId(request.getProductId());
            product.setName(productData.getName());
            product.setPrice(productData.getPrice());
        } else {
            LOGGER.error("Err: " + productResponse.getStatusCode());
        }

        ResponseEntity<GetAccountResponse> accountResponse = restTemplate.getForEntity(gCashMiniProperties.getAccountUrl() + request.getAccountId(), GetAccountResponse.class);

        GetAccountResponse accountData = accountResponse.getBody();
        if (accountResponse.getStatusCode().is2xxSuccessful()) {
            LOGGER.info("Success");
            Account account = new Account(request.getAccountId());
            account.setAccountId(request.getAccountId());
            account.setBalance(accountData.getBalance());
            account.setFirstName(accountData.getFirstName());
            account.setLastName(accountData.getLastName());
            account.setEmail(accountData.getEmail());
        } else {
            LOGGER.error("Err: " + accountResponse.getStatusCode()); // throw new InvalidAccountRequestException();
        }

        Activity activity = new Activity();
        activity.setAction("PURCHASE");
        activity.setIdentifier(String.valueOf(request.getAccountId()));
        activity.setDetails("PURCHASED: " + request.getProductId());
        activityService.submitActivity(activity);

        Double newBalance = accountData.getBalance() - productData.getPrice();
        UpdateBalanceRequest updateBalanceRequest = new UpdateBalanceRequest();
        updateBalanceRequest.setNewBalance(newBalance);

        if (newBalance < 0) {
            throw new InsufficientBalanceException("Insufficient Balance.");
        } else {
            ResponseEntity<UpdateBalanceResponse> updateResponse = restTemplate.postForEntity(gCashMiniProperties.getAccountUrl() + request.getAccountId(), updateBalanceRequest, UpdateBalanceResponse.class);
            if (updateResponse.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("New balance: " + newBalance);
            } else {
                LOGGER.error("Err: " + updateResponse.getStatusCode());
            }
        }

        return ResponseEntity.ok().build();
    }
}
