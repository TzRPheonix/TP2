package org.example.loanservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.example.loanservice.service.LoanService;

@Service
public class LoanKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(LoanKafkaConsumer.class);

    @Autowired
    private LoanService loanService;

    @KafkaListener(topics = "account-events", groupId = "loan-group")
    public void consumeAccountDeleteEvent(String message) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode event = objectMapper.readTree(message);

            if ("ACCOUNT_DELETED".equals(event.get("event").asText())) {
                Long id = Long.valueOf(event.get("accountId").asText());
                logger.info("Account Deleted ok + id : " + id);
                loanService.deleteLoanByAccountId(id);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
