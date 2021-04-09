package ph.apper.purchase.controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ph.apper.purchase.domain.Account;
import ph.apper.purchase.payload.PurchaseRequest;

@RestController
@RequestMapping("purchase")
public class PurchaseController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping()
    public ResponseEntity<Object> purchaseItem(@RequestBody PurchaseRequest request){
        String url = "http://localhost:8081/account/"+request.getAccountId();
        System.out.println(url);
        Account account = restTemplate.getForObject(url, Account.class);
        return new ResponseEntity<>(account.getFirstName(), HttpStatus.OK);
    }
}
