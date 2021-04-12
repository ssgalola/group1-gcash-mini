package ph.apper.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ph.apper.gateway.App;
import ph.apper.gateway.payload.AccountRequest;
import ph.apper.gateway.payload.AddFundsRequest;
import ph.apper.gateway.payload.response.GetAccountResponse;
import ph.apper.gateway.payload.response.NewAccountResponse;
import ph.apper.gateway.payload.response.UpdateBalanceResponse;

@RestController
@RequestMapping("account")
public class AccountManagementControlller {
    private final RestTemplate restTemplate;
    private final App.GCashMiniProperties gCashMiniProperties;

    public AccountManagementControlller(RestTemplate restTemplate, App.GCashMiniProperties gCashMiniProperties) {
        this.restTemplate = restTemplate;
        this.gCashMiniProperties = gCashMiniProperties;
    }

    @PostMapping
    public ResponseEntity<Object> createAccount(@RequestBody AccountRequest request){
        ResponseEntity<NewAccountResponse> response = restTemplate.postForEntity(
                gCashMiniProperties.getAccountUrl(),
                request,
                NewAccountResponse.class);
        if(response.getStatusCode().is2xxSuccessful())
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        return ResponseEntity.status(response.getStatusCode()).build();

    }

    @GetMapping("{accountId}")
    public ResponseEntity<Object> getAccount(@PathVariable String accountId) throws HttpClientErrorException {
        ResponseEntity<GetAccountResponse> response = restTemplate.getForEntity(
                gCashMiniProperties.getAccountUrl()+accountId,
                GetAccountResponse.class);
        if (response.getStatusCode().is2xxSuccessful()){
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    @PostMapping("addFunds")
    public ResponseEntity<Object> addFunds(@RequestBody AddFundsRequest request) throws HttpClientErrorException{
        ResponseEntity<UpdateBalanceResponse> response = restTemplate.postForEntity(
                gCashMiniProperties.getAccountUrl()+"addFunds/",
                request,
                UpdateBalanceResponse.class);
        if (response.getStatusCode().is2xxSuccessful()){
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }

        return ResponseEntity.status(response.getStatusCode()).build();
        
    }
}
