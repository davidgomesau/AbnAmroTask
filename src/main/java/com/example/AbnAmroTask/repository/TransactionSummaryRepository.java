package com.example.AbnAmroTask.repository;

import com.example.AbnAmroTask.model.SummaryEntry;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionSummaryRepository {

    private final List<SummaryEntry> transactions = new ArrayList<>();

    public TransactionSummaryRepository() {
    }

    public List<SummaryEntry> findAll() {
        return transactions;
    }

    public void save(SummaryEntry summaryEntry) {
        transactions.add(summaryEntry);
    }

    public void deleteAll() {
        transactions.clear();
    }
}
