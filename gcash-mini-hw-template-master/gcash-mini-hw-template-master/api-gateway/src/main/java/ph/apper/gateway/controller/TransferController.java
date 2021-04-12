package ph.apper.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ph.apper.gateway.payload.TransferRequest;
import ph.apper.gateway.App;
import ph.apper.gateway.payload.response.TransferResponse;


@RestController
@RequestMapping("transfer")
public class TransferController {

    private final RestTemplate restTemplate;
    private final App.GCashMiniProperties gCashMiniProperties;

    public TransferController(RestTemplate restTemplate, App.GCashMiniProperties gCashMiniProperties) {
        this.restTemplate = restTemplate;
        this.gCashMiniProperties = gCashMiniProperties;
    }

    @PostMapping
    public ResponseEntity<Object> transfer(@RequestBody TransferRequest request) {
        ResponseEntity<TransferResponse> response = restTemplate.postForEntity(
                gCashMiniProperties.getTransferUrl(), request, TransferResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }
        return ResponseEntity.status(response.getStatusCode()).build();
    }
}
