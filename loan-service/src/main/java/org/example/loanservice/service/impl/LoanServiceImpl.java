package org.example.loanservice.service.impl;

import org.example.loanservice.entity.Loan;
import org.example.loanservice.repository.LoanRepository;
import org.example.loanservice.rest.AccountServiceClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.loanservice.service.LoanService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {


    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountServiceClient accountServiceClient;



    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    public List<Loan> getLoansByAccountId(Long accountId) {
        return loanRepository.findByAccountId(accountId);
    }

    public Loan saveLoan(Loan loan) {
        if (accountServiceClient.accountExists(loan.getAccountId())) {
            return loanRepository.save(loan);
        } else {
            throw new RuntimeException("Account does not exist");
        }
    }

    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteLoanByAccountId(Long id) {
        logger.info("Deleting loan in service impl : " + id);
        loanRepository.deleteLoanByAccountId(id);
    }


}

