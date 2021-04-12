package ph.apper.gateway.controller;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ph.apper.gateway.App;
import ph.apper.gateway.payload.PurchaseRequest;

@RestController
@RequestMapping("purchase")
public class PurchaseController {

    private final RestTemplate restTemplate;
    private final App.GCashMiniProperties gCashMiniProperties;

    public PurchaseController(RestTemplate restTemplate, App.GCashMiniProperties gCashMiniProperties, Environment env) {
        this.restTemplate = restTemplate;
        this.gCashMiniProperties = gCashMiniProperties;
    }

    @PostMapping
    public ResponseEntity<Object> purchase(@RequestBody PurchaseRequest request) {
        ResponseEntity response = restTemplate.postForEntity(gCashMiniProperties.getPurchaseUrl(), request, ResponseEntity.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        if (response.getStatusCode().is4xxClientError()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.status(response.getStatusCode()).build();
    }

}
