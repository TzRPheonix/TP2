package org.example.loanservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoanKafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(LoanKafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.loan:loan-events}")
    private String topic;

    public LoanKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLoanEvent(String event) {
        logger.info("Producing loan event: {}", event);
        kafkaTemplate.send(topic, event);
    }
}