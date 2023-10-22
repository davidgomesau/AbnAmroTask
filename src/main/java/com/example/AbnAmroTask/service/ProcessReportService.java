package com.example.AbnAmroTask.service;

import com.example.AbnAmroTask.model.Transaction;

import java.io.File;
import java.io.IOException;

public interface ProcessReportService {

    public Transaction transactionMapper(String input);

    public void addResultsToRepository(Transaction transaction);

    public void transactionProcessor(String input);

    public File generateReport() throws IOException;
}
