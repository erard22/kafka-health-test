package ch.erard22.quarkus;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DemoConsumerIT {

    private static final String TOPIC = "demo";

    private KafkaProducer<String, String> kafkaProducer;


    @BeforeAll
    void beforeAll() {
        Properties config = new Properties();
        config.put("bootstrap.servers", System.getProperty("kafka.bootstrap.servers", "localhost:9092"));
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "all");
        kafkaProducer = new KafkaProducer<>(config);
    }

    @AfterAll
    void afterAll() {
        kafkaProducer.close();
    }

    @Test
    void send_ok()  {
        kafkaProducer.send(new ProducerRecord<>(TOPIC, "Testmessage"));
    }
}
