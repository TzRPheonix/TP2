package org.example.cardservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CardKafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(CardKafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.card:card-events}")
    private String topic;

    public CardKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCardEvent(String event) {
        logger.info("Producing card event: {}", event);
        kafkaTemplate.send(topic, event);
    }
}