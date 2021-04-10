package ph.apper.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ph.apper.gateway.payload.AccountRequest;
import ph.apper.gateway.payload.Activity;
import ph.apper.gateway.payload.response.NewAccountResponse;

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
        ResponseEntity<NewAccountResponse> response = restTemplate.postForEntity(gCashMiniProperties.getAccountsUrl(), request, NewAccountResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return ResponseEntity.status(response.getStatusCode()).build();
    }

}
