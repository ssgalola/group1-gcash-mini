package ph.apper.account.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ph.apper.account.domain.Activity;

import java.util.Objects;

@Service
public class ActivityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);
    @Autowired
    private final RestTemplate restTemplate;

    private Environment env;

    public ActivityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void postActivity(Activity activity){
        ResponseEntity<Activity[]> activityResponse = restTemplate.postForEntity(Objects.requireNonNull(env.getProperty("activity.host")),activity, Activity[].class);
        if (activityResponse.getStatusCode().is2xxSuccessful()) {
            LOGGER.info(activity.getAction() + " activity with" + activity.getIdentifier() + " identifier has been"  + " recorded.");
        }
        else {
            LOGGER.error("Err: " + activityResponse.getStatusCode());
        }
    }
}
