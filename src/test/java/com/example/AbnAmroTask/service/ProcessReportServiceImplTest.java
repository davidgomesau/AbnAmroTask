package com.example.AbnAmroTask.service;

import com.example.AbnAmroTask.model.SummaryEntry;
import com.example.AbnAmroTask.model.Transaction;
import com.example.AbnAmroTask.repository.TransactionSummaryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProcessReportServiceImplTest {

    @Autowired
    private ProcessReportServiceImpl processReportService;

    @Autowired
    private TransactionSummaryRepository transactionSummaryRepository;

    @Test
    void testTransactionMapper() {
        // given
        String input = "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012380     688032000092500000000             O";

        // when
        Transaction result = processReportService.transactionMapper(input);

        // then
        assertEquals(result.getRecordCode(), "315");
        assertEquals(result.getClientType(), "CL");
        assertEquals(result.getClientNumber(), 4321);
        assertEquals(result.getAccountNumber(), 2);
        assertEquals(result.getSubaccountNumber(), 1);
        assertEquals(result.getOppositePartyCode(), "SGXDC");
        assertEquals(result.getProductGroupCode(), "FU");
        assertEquals(result.getExchangeCode(), "SGX");
        assertEquals(result.getSymbol(), "NK");
        assertEquals(result.getExpirationDate(), LocalDate.of(2010, 9, 10));
        assertEquals(result.getCurrencyCode(), "JPY");
        assertEquals(result.getMovementCode(), "01");
        assertEquals(result.getBuySellCode(), 'B');
        assertEquals(result.getQuantityLongSign(), ' ');
        assertEquals(result.getQuantityLong(), 1);
        assertEquals(result.getQuantityShortSign(), ' ');
        assertEquals(result.getQuantityShort(), 0);
        assertEquals(result.getExchBrokerFeeDec(), 0.60);
        assertEquals(result.getExchBrokerFeeDC(), 'D');
        assertEquals(result.getExchBrokerFeeCurCode(), "USD");
        assertEquals(result.getClearingFeeDec(), 0.3);
        assertEquals(result.getClearingFeeDC(), 'D');
        assertEquals(result.getClearingFeeCurCode(), "USD");
        assertEquals(result.getCommission(), 0);
        assertEquals(result.getCommissionDC(), 'D');
        assertEquals(result.getCommissionCurCode(), "JPY");
        assertEquals(result.getTransactionDate(), LocalDate.of(2010, 8, 20));
        assertEquals(result.getFutureReference(), 1238);
        assertEquals(result.getTicketNumber(), "0");
        assertEquals(result.getExternalNumber(), 688032);
        assertEquals(result.getTransactionPriceDec(), 9250);
        assertEquals(result.getTraderInitials(), "");
        assertEquals(result.getOppositeTraderId(), "");
        assertEquals(result.getOpenCloseCode(), 'O');
        assertEquals(result.getFiller(), "");
    }

    @Test
    void testTransactionMapperWithBadInteger() {
        // given
        String input = "315CL  XXXX00020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012380     688032000092500000000             O";

        assertThrows(NumberFormatException.class, () -> processReportService.transactionMapper(input));
    }

    @Test
    void testTransactionMapperWithBadDate() {
        String input = "315CL  432100020001SGXDC FUSGX NK    XXXX0910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012380     688032000092500000000             O";

        assertThrows(DateTimeParseException.class, () -> processReportService.transactionMapper(input));
    }

    @Test
    void testAddResultsToRepository() {
        // given
        Transaction transaction = new Transaction();
        transaction.setClientType("CL");
        transaction.setClientNumber(1234);
        transaction.setAccountNumber(7);
        transaction.setSubaccountNumber(8);
        transaction.setExchangeCode("EX");
        transaction.setProductGroupCode("PR");
        transaction.setSymbol("SYM");
        transaction.setExpirationDate(LocalDate.of(2000, 1, 1));
        transaction.setQuantityLong(5);
        transaction.setQuantityShort(2);

        // when
        processReportService.addResultsToRepository(transaction);

        // then
        List<SummaryEntry> result = transactionSummaryRepository.findAll();
        assertEquals(1, result.size());
        assertEquals(result.get(0).getClientInformation(), "CL_1234_7_8");
        assertEquals(result.get(0).getProductInformation(), "EX_PR_SYM_20000101");
        assertEquals(result.get(0).getTransactionAmount(), 3);
    }

    @Test
    void generateReport() throws IOException {
        // given
        transactionSummaryRepository.deleteAll();
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
        File result = processReportService.generateReport();

        // then
        assertEquals(result.getName(), "Output.csv");
        BufferedReader reader = new BufferedReader(new FileReader(result));
        assertLinesMatch(reader.lines(), Stream.of("CLI_INF_1,PROD_INF_1,10", "CLI_INF_2,PROD_INF_2,20"));
    }
}