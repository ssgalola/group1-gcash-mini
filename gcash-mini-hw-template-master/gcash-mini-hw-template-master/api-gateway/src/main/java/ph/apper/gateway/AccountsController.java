package ph.apper.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ph.apper.gateway.payload.AccountRequest;
import ph.apper.gateway.payload.Activity;
import ph.apper.gateway.payload.AddFundsRequest;
import ph.apper.gateway.payload.response.GetAccountResponse;
import ph.apper.gateway.payload.response.NewAccountResponse;
import ph.apper.gateway.payload.response.UpdateBalanceResponse;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("account")
public class AccountsController {
    private final RestTemplate restTemplate;
    private final App.GCashMiniProperties gCashMiniProperties;

    public AccountsController(RestTemplate restTemplate, App.GCashMiniProperties gCashMiniProperties){
        this.restTemplate = restTemplate;
        this.gCashMiniProperties = gCashMiniProperties;
    }

    @PostMapping
    public ResponseEntity<Object> createAccount(@RequestBody AccountRequest request) {

        ResponseEntity<NewAccountResponse> response = restTemplate.postForEntity(gCashMiniProperties.getAccountsUrl()+"/account/", request, NewAccountResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }

        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @GetMapping("{accountId}")
    public ResponseEntity<Object> getAccount(@PathVariable String accountId) {
        ResponseEntity<GetAccountResponse> response = restTemplate.getForEntity(gCashMiniProperties.getAccountsUrl()+"/account/"+accountId, GetAccountResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }

        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @PostMapping("addFunds")
    public ResponseEntity<Object> addFunds(@RequestBody AddFundsRequest request) {
        ResponseEntity<UpdateBalanceResponse> response = restTemplate.
                postForEntity(gCashMiniProperties.getAccountsUrl()+"/account/addFunds/",
                                request,
                                UpdateBalanceResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        return ResponseEntity.status(response.getStatusCode()).build();
    }

}
