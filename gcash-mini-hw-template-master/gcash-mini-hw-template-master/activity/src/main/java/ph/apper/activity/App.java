package ph.apper.activity;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;
import ph.apper.activity.payload.Activity;

import java.util.function.Consumer;

@SpringBootApplication
public class App {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(App.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.run(args);
    }

    @Bean
    public CommandLineRunner pollSqs(@Value("${sqs.queueUrl}") String queueUrl, AmazonSQS amazonSQS) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                while (true) {
                    System.out.println("poll...");
                    ReceiveMessageRequest request = new ReceiveMessageRequest(queueUrl);
                    request.withWaitTimeSeconds(10);

                    ReceiveMessageResult result = amazonSQS.receiveMessage(request);
                    result.getMessages().forEach(new Consumer<Message>() {
                        @SneakyThrows
                        @Override
                        public void accept(Message message) {
                            System.out.println(message.getBody());

                            Activity activity = OBJECT_MAPPER.readValue(message.getBody(), Activity.class);
                            System.out.println(activity);

                            amazonSQS.deleteMessage(queueUrl, message.getReceiptHandle());
                        }
                    });
                }
            }
        };
    }
}
