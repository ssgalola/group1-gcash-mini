package ph.apper.purchase.util;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ph.apper.purchase.App;
import ph.apper.purchase.domain.Activity;

@Service
public class ActivityService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final AmazonSQS amazonSQS;
    private final App.GCashMiniProperties gCashMiniProperties;

    public ActivityService(AmazonSQS amazonSQS, App.GCashMiniProperties gCashMiniProperties) {
        this.amazonSQS = amazonSQS;
        this.gCashMiniProperties = gCashMiniProperties;
    }

    public void submitActivity(Activity activity) throws JsonProcessingException {
        String message = OBJECT_MAPPER.writeValueAsString(activity);
        SendMessageRequest sendMessageRequest = new SendMessageRequest(gCashMiniProperties.getQueueUrl(), message);
        amazonSQS.sendMessage(sendMessageRequest);
    }
}
