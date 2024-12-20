package org.example.accountservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.accountservice.entity.Account;
import org.example.accountservice.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AccountKafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(AccountKafkaConsumer.class);

    @Autowired
    private AccountRepository accountRepository;

    @KafkaListener(topics = "card-events", groupId = "account-group")
    public void consumeCardEvent(String message) {
        processEvent(message, "CARD");
    }

    @KafkaListener(topics = "loan-events", groupId = "account-group")
    public void consumeLoanEvent(String message) {
        processEvent(message, "LOAN");
    }

    private void processEvent(String message, String eventType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode event = objectMapper.readTree(message);

            Long accountId = Long.valueOf(event.get("accountId").asText());
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            if ("CARD".equals(eventType)) {
                if ("CARD_CREATED".equals(event.get("event").asText())) {
                    account.setTotalCards(account.getTotalCards() + 1);
                } else if ("CARD_DELETED".equals(event.get("event").asText()) && account.getTotalCards() > 0) {
                    account.setTotalCards(account.getTotalCards() - 1);
                }
            } else if ("LOAN".equals(eventType)) {
                if ("LOAN_CREATED".equals(event.get("event").asText())) {
                    account.setTotalLoans(account.getTotalLoans() + 1);
                } else if ("LOAN_DELETED".equals(event.get("event").asText()) && account.getTotalLoans() > 0) {
                    account.setTotalLoans(account.getTotalLoans() - 1);
                }
            }

            accountRepository.save(account);
            logger.info("Updated account: {}", account);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}