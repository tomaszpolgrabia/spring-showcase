package ch.polgrabia.springshowcaseweb.services;

import org.apache.kafka.streams.KafkaStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class KafkaService {
    private final KafkaStreams kafkaStreams;

    public KafkaService(
            @Autowired KafkaStreams kafkaStreams) {
        this.kafkaStreams = kafkaStreams;
    }

    @PostConstruct
    public void init() {
        kafkaStreams.start();
    }

    @PreDestroy
    public void stop() {
        kafkaStreams.close();
    }
}
