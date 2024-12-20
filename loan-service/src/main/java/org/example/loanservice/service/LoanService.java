package org.example.loanservice.service;



import org.example.loanservice.entity.Loan;

import java.util.List;

public interface LoanService {

    public List<Loan> getAllLoans();

    public Loan getLoanById(Long id);

    public List<Loan> getLoansByAccountId(Long accountId);
    public Loan saveLoan(Loan loan);

    public void deleteLoan(Long id);

    public void deleteLoanByAccountId(Long id);
}
