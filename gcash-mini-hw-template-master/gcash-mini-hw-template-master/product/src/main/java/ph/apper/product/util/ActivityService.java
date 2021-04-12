package ph.apper.product.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ph.apper.product.App;
import ph.apper.product.payload.Activity;

import java.util.Objects;

@Service
public class ActivityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);

    private final RestTemplate restTemplate;
    private final App.GCashMiniProperties gCashMiniProperties;

    public ActivityService(RestTemplate restTemplate, App.GCashMiniProperties gCashMiniProperties) {
        this.restTemplate = restTemplate;
        this.gCashMiniProperties = gCashMiniProperties;
    }

    public void postActivity(Activity activity) {
        ResponseEntity<Activity[]> activityResponse = restTemplate.postForEntity(gCashMiniProperties.getActivityUrl(), activity, Activity[].class);

        if (activityResponse.getStatusCode().is2xxSuccessful()) {
            LOGGER.info("Add product activity recorded.");
        }
        else {
            LOGGER.error("Err: " + activityResponse.getStatusCode());
        }
    }
}
