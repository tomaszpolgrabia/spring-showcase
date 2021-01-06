package ch.polgrabia.springshowcaseweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messaging")
@PropertySource("classpath:application.properties")
public class KafkaController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${springshowcase.consumer.bootstrap.topic1.name}")
    private String topic1;

    public KafkaController(
            @Autowired KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @RequestMapping(path = "/send/{message}")
    public void sendMessage(@PathVariable("message") String message) {
        kafkaTemplate.send(topic1, message);
    }

    @KafkaListener(topics = "topic1", groupId = "group1")
    public void handleMessage(String message) {
        System.out.printf("Got message: %s", message);
    }
}
