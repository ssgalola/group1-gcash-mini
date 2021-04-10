package ph.apper.purchase.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ph.apper.account.domain.Account;
import ph.apper.account.payload.UpdateBalanceRequest;
import ph.apper.account.payload.response.GetAccountResponse;
import ph.apper.account.payload.response.UpdateBalanceResponse;
import ph.apper.activity.payload.Activity;
import ph.apper.product.controller.ProductController;
import ph.apper.product.domain.Product;
import ph.apper.product.exception.ProductNotFoundException;
import ph.apper.product.payload.AddProduct;
import ph.apper.product.payload.AddProductResponse;
import ph.apper.product.payload.GetProductResponse;
import ph.apper.purchase.payload.PurchaseData;

import javax.validation.Valid;

@RestController
@RequestMapping("purchase")
public class PurchaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseController.class);

    private final RestTemplate restTemplate;

    public PurchaseController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity purchase(@RequestBody PurchaseData request) throws ProductNotFoundException {
        LOGGER.info(String.valueOf(request));
        PurchaseData purchase = new PurchaseData();

        // PRODUCT
        purchase.setProductId(request.getProductId());
        String url = "http://localhost:8083/product/" + request.getProductId();

        ResponseEntity<GetProductResponse> productResponse = restTemplate.getForEntity(url, GetProductResponse.class);
        GetProductResponse g = productResponse.getBody();

        if (productResponse.getStatusCode().is2xxSuccessful()) {
            LOGGER.info("Success");
            Product product = new Product(request.getProductId());

            product.setProductId(purchase.getProductId());
            product.setName(g.getName());
            product.setPrice(g.getPrice());

        } else {
            LOGGER.error("Err: " + productResponse.getStatusCode());
            throw new ProductNotFoundException();

        }

        // ACCOUNT
        purchase.setAccountId(request.getAccountId());
        String accountUrl = "http://localhost:8081/account/" + request.getAccountId();

        ResponseEntity<GetAccountResponse> accountResponse = restTemplate.getForEntity(accountUrl, GetAccountResponse.class);
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
//            throw new ACCOUNT EXCEPTION HERE
        }

        // Update Balance
        double newBalan = a.getBalance() - g.getPrice();
        UpdateBalanceRequest newBalance = new UpdateBalanceRequest();
        newBalance.setNewBalance(newBalan);

        UpdateBalanceResponse updateResponse = restTemplate.postForObject(accountUrl, newBalance, UpdateBalanceResponse.class);


        if (newBalan < 0) {
            LOGGER.info("Insufficient balance");

        } else {
            LOGGER.info("New balance: " + newBalan);
        }

        // Send Purchase Activity
        Activity activity = new Activity();
        activity.setAction("PURCHASE");
        activity.setIdentifier("Product ID: " + request.getProductId() + ", Account ID: " + request.getAccountId());
        activity.setDetails("NEW BALANCE: " + newBalance);

        ResponseEntity<Activity[]> activityResponse = restTemplate.postForEntity("http://localhost:8082", activity, Activity[].class);
        if (activityResponse.getStatusCode().is2xxSuccessful()) {
            LOGGER.info("Purchase activity recorded");
        }
        else {
            LOGGER.error("Err: " + activityResponse.getStatusCode());
        }

        return new ResponseEntity<>(updateResponse, HttpStatus.OK);

    }

}
