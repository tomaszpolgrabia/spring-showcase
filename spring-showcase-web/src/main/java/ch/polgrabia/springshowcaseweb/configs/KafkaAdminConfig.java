package ch.polgrabia.springshowcaseweb.configs;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@PropertySource("classpath:application.properties")
public class KafkaAdminConfig {

    @Value("${springshowcase.consumer.bootstrap.address}")
    private String bootstrapAddress;

    @Value("${springshowcase.consumer.bootstrap.topic1.name}")
    private String topic1Name;

    @Value("${springshowcase.consumer.bootstrap.topic1.replicas}")
    private Integer replicas;

    @Value("${springshowcase.consumer.bootstrap.topic1.partitions}")
    private Integer partitions;


    @Bean
    public KafkaAdmin produceKafkaAdmin() {
        Map<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(config);
    }

    @Bean
    public NewTopic produceTopic() {
        return new NewTopic(topic1Name, partitions, (short) ((int) replicas));
    }

}
