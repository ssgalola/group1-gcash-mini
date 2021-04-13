package ph.apper.account.util;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ph.apper.account.App;
import ph.apper.account.domain.Activity;

@Service
public class ActivityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);
    private final RestTemplate restTemplate;
    private final App.GCashMiniProperties gCashMiniProperties;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final AmazonSQS amazonSQS;

    public ActivityService(RestTemplate restTemplate, App.GCashMiniProperties gCashMiniProperties, AmazonSQS amazonSQS) {
        this.restTemplate = restTemplate;
        this.gCashMiniProperties = gCashMiniProperties;
        this.amazonSQS = amazonSQS;

    }

    @Deprecated
    public void postActivity(Activity activity){
        ResponseEntity<Activity[]> activityResponse = restTemplate.postForEntity(gCashMiniProperties.getActivityUrl(),activity, Activity[].class);
        if (activityResponse.getStatusCode().is2xxSuccessful()) {
            LOGGER.info(activity.getAction() + " activity with " + activity.getIdentifier() + " identifier has been"  + " recorded.");
        }
        else {
            LOGGER.error("Err: " + activityResponse.getStatusCode());
        }
    }


    public void submitActivity(Activity activity) throws JsonProcessingException {
        String message = OBJECT_MAPPER.writeValueAsString(activity);
        SendMessageRequest sendMessageRequest = new SendMessageRequest(gCashMiniProperties.getQueueUrl(), message);
        amazonSQS.sendMessage(sendMessageRequest);
    }


}
