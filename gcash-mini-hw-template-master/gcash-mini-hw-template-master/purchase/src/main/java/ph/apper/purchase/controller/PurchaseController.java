package ph.apper.purchase.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ph.apper.purchase.App;
import ph.apper.purchase.domain.Account;
import ph.apper.purchase.domain.Activity;
import ph.apper.purchase.domain.Product;
import ph.apper.purchase.exception.InvalidAccountRequestException;
import ph.apper.purchase.exception.ProductNotFoundException;
import ph.apper.purchase.payload.GetProductResponse;
import ph.apper.purchase.payload.PurchaseData;
import ph.apper.purchase.payload.UpdateBalanceRequest;
import ph.apper.purchase.payload.response.GetAccountResponse;
import ph.apper.purchase.payload.response.UpdateBalanceResponse;

@RestController
@RequestMapping("purchase")
public class PurchaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseController.class);
    private final RestTemplate restTemplate;

    @Autowired
    private Environment env;

    public PurchaseController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity purchase(@RequestBody PurchaseData request) throws ProductNotFoundException, HttpClientErrorException {
        LOGGER.info(String.valueOf(request));
        PurchaseData purchase = new PurchaseData();

        // PRODUCT
        purchase.setProductId(request.getProductId());

        ResponseEntity<GetProductResponse> productResponse
                = restTemplate.getForEntity(env.getProperty("gcash.mini.productUrl") + request.getProductId(), GetProductResponse.class);

        GetProductResponse g = productResponse.getBody();
        if (productResponse.getStatusCode().is2xxSuccessful()) {
            LOGGER.info("Success");
            Product product = new Product(request.getProductId());

            product.setProductId(purchase.getProductId());
            product.setName(g.getName());
            product.setPrice(g.getPrice());

        } else {
            LOGGER.error("Err: " + productResponse.getStatusCode());
//            return ResponseEntity.status(productResponse.getStatusCode()).build();
//            throw new ProductNotFoundException();

        }

        // ACCOUNT
        purchase.setAccountId(request.getAccountId());

        ResponseEntity<GetAccountResponse> accountResponse
                = restTemplate.getForEntity(env.getProperty("gcash.mini.accountUrl") + request.getAccountId(), GetAccountResponse.class);
        GetAccountResponse a = accountResponse.getBody();

        if (accountResponse.getStatusCode().is2xxSuccessful()) {
            LOGGER.info("Success");
            Account account = new Account(request.getAccountId());

            account.setAccountId(purchase.getAccountId());
            account.setBalance(a.getBalance());
            account.setFirstName(a.getFirstName());
            account.setLastName(a.getLastName());
            account.setEmail(a.getEmail());

        } else {
            LOGGER.error("Err: " + accountResponse.getStatusCode());
//            throw new InvalidAccountRequestException();
        }

        // Send Purchase Activity
        Activity activity = new Activity();
        activity.setAction("PURCHASE");
        activity.setIdentifier(String.valueOf(request.getAccountId()));
        activity.setDetails("PURCHASED: " + request.getProductId());

        ResponseEntity<Activity[]> activityResponse
                = restTemplate.postForEntity(env.getProperty("gcash.mini.activityUrl"), activity, Activity[].class);
        if (activityResponse.getStatusCode().is2xxSuccessful()) {
            LOGGER.info("Purchase activity recorded");
        }
        else {
            LOGGER.error("Err: " + activityResponse.getStatusCode());
        }

        // Update Balance
        double newBal = a.getBalance() - g.getPrice();
        UpdateBalanceRequest newBalance = new UpdateBalanceRequest();
        newBalance.setNewBalance(newBal);

        UpdateBalanceResponse updateResponse
                = restTemplate.postForObject(env.getProperty("gcash.mini.accountUrl") + request.getAccountId(), newBalance, UpdateBalanceResponse.class);


        if (newBal < 0) {
            LOGGER.info("Insufficient balance");
//            throw new InsufficientBalanceException();

        } else {
            LOGGER.info("New balance: " + newBal);
        }

        return ResponseEntity.ok().build();
//        return new ResponseEntity<>(updateResponse, HttpStatus.OK);

    }

}
