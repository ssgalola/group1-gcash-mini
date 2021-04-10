package ph.apper.purchase.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ph.apper.account.domain.Account;
import ph.apper.account.payload.response.GetAccountResponse;
import ph.apper.product.domain.Product;
import ph.apper.product.payload.GetProductResponse;
import ph.apper.purchase.payload.PurchaseData;

@RestController
@RequestMapping("purchase")
public class PurchaseController {

    private final RestTemplate restTemplate;

    public PurchaseController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity purchase(@RequestBody PurchaseData request) {
        System.out.println(request);
        PurchaseData purchase = new PurchaseData();

        // PRODUCT
        purchase.setProductId(request.getProductId());
        String url = "http://localhost:8083/product/" + request.getProductId();

        ResponseEntity<GetProductResponse> productResponse = restTemplate.getForEntity(url, GetProductResponse.class);
        GetProductResponse g = productResponse.getBody();

        // ACCOUNT
        purchase.setAccountId(request.getAccountId());
        String accountUrl = "http://localhost:8081/account/" + request.getAccountId();

        ResponseEntity<GetAccountResponse> accountResponse = restTemplate.getForEntity(accountUrl, GetAccountResponse.class);
        GetAccountResponse a = accountResponse.getBody();

        double newBalance = a.getBalance() - g.getPrice();
        if (newBalance < 0) {
            System.out.println("Insufficient balance");

        } else {
            System.out.println("New balance: " + newBalance);
        }

        // SEND UPDATED BALANCE TO ACCOUNT..

        return ResponseEntity.ok().build();

    }

}
