package org.example.accountservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountKafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(AccountKafkaProducer.class);
    private final KafkaTemplate<String,String > kafkaTemplate;

    @Value("${spring.kafka.topic.account:account-events}")
    private String topic;

    public AccountKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAccountDeleteEvent(Long accountId){
        String event = String.format("{\"event\":\"ACCOUNT_DELETED\",\"accountId\":\"%s\"}", accountId);
        logger.info("Producing account deleted event: {} ",event);
        kafkaTemplate.send(topic,event);
    }




}
