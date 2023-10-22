package com.example.AbnAmroTask.repository;

import com.example.AbnAmroTask.model.SummaryEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionSummaryRepositoryTest {

    @Autowired
    private TransactionSummaryRepository transactionSummaryRepository;

    @Test
    void testFindAll() {
        // given
        SummaryEntry summaryEntry1 = new SummaryEntry();
        summaryEntry1.setClientInformation("CLI_INF_1");
        summaryEntry1.setProductInformation("PROD_INF_1");
        summaryEntry1.setTransactionAmount(10);
        transactionSummaryRepository.save(summaryEntry1);
        SummaryEntry summaryEntry2 = new SummaryEntry();
        summaryEntry2.setClientInformation("CLI_INF_2");
        summaryEntry2.setProductInformation("PROD_INF_2");
        summaryEntry2.setTransactionAmount(20);
        transactionSummaryRepository.save(summaryEntry2);

        // when
        List<SummaryEntry> result = transactionSummaryRepository.findAll();

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(summaryEntry1));
        assertTrue(result.contains(summaryEntry2));
    }

    @Test
    void testSave() {
        // given
        transactionSummaryRepository.deleteAll();
        SummaryEntry summaryEntry = new SummaryEntry();
        summaryEntry.setClientInformation("CLI_INF_1");
        summaryEntry.setProductInformation("PROD_INF_1");
        summaryEntry.setTransactionAmount(10);

        // when
        transactionSummaryRepository.save(summaryEntry);

        // then
        List<SummaryEntry> result = transactionSummaryRepository.findAll();
        assertEquals(1, result.size());
        assertTrue(result.contains(summaryEntry));
    }

    @Test
    void deleteAll() {
        // given
        SummaryEntry summaryEntry1 = new SummaryEntry();
        summaryEntry1.setClientInformation("CLI_INF_1");
        summaryEntry1.setProductInformation("PROD_INF_1");
        summaryEntry1.setTransactionAmount(10);
        transactionSummaryRepository.save(summaryEntry1);
        SummaryEntry summaryEntry2 = new SummaryEntry();
        summaryEntry2.setClientInformation("CLI_INF_2");
        summaryEntry2.setProductInformation("PROD_INF_2");
        summaryEntry2.setTransactionAmount(20);
        transactionSummaryRepository.save(summaryEntry2);

        // when
        transactionSummaryRepository.deleteAll();

        // then
        List<SummaryEntry> result = transactionSummaryRepository.findAll();
        assertEquals(0, result.size());
    }
}