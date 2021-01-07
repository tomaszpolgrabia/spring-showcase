package ch.polgrabia.springshowcaseweb.configs;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
public class KafkaStreamsConfig {

    private static final String APPLICATION_ID = "app_id";

    @Value("${springshowcase.consumer.bootstrap.address}")
    private String bootstrapAddress;

    @Bean
    @Qualifier("streamsConfig")
    public Properties kStreamsConfigs() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        // props.put(StreamsConfig.STATE_DIR_CONFIG, stateStoreDir.toString());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return props;
    }

    @Bean
    @Qualifier("streamsBuilder")
    public StreamsBuilder streamsBuilder() {
        StreamsBuilder sb = new StreamsBuilder();

        KStream<String, String> textLines = sb.stream("topic1");
        KTable<String, Long> wordCounts = textLines
                .flatMapValues(textLine -> Arrays.asList(textLine.split("\\W+")))
                .groupBy((key, word) -> word)
                .count(Materialized.as("counts-store"));

        wordCounts.toStream()
                .to("counts-store-topic", Produced.with(Serdes.String(), Serdes.Long()));
        return sb;
    }

    @Bean
    public KafkaStreams kafkaStreams(
            @Autowired @Qualifier("streamsBuilder") StreamsBuilder sb,
            @Autowired @Qualifier("streamsConfig") Properties props) {
        return new KafkaStreams(sb.build(), props);
    }
}
