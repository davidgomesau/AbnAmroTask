package com.example.AbnAmroTask.service;

import com.example.AbnAmroTask.model.SummaryEntry;
import com.example.AbnAmroTask.model.Transaction;
import com.example.AbnAmroTask.repository.TransactionSummaryRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProcessReportServiceImpl implements ProcessReportService {

    private final TransactionSummaryRepository transactionSummaryRepository;

    @Value("${abnamrotask.outputfilename}")
    private String filename;

    @Value("${abnamrotask.summaryseparator}")
    private String summarySeparator;

    public ProcessReportServiceImpl(TransactionSummaryRepository transactionSummaryRepository) {
        this.transactionSummaryRepository = transactionSummaryRepository;
    }

    public Transaction transactionMapper(String input) throws NumberFormatException, DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        Transaction transaction = new Transaction();
        transaction.setRecordCode(input.substring(0, 3).trim());
        transaction.setClientType(input.substring(3, 7).trim());
        transaction.setClientNumber(Integer.parseInt(input.substring(7, 11).trim()));
        transaction.setAccountNumber(Integer.parseInt(input.substring(11, 15).trim()));
        transaction.setSubaccountNumber(Integer.parseInt(input.substring(15, 19).trim()));
        transaction.setOppositePartyCode(input.substring(19, 25).trim());
        transaction.setProductGroupCode(input.substring(25, 27).trim());
        transaction.setExchangeCode(input.substring(27, 31).trim());
        transaction.setSymbol(input.substring(31, 37).trim());
        transaction.setExpirationDate(LocalDate.parse(input.substring(37, 45).trim(), formatter));
        transaction.setCurrencyCode(input.substring(45, 48).trim());
        transaction.setMovementCode(input.substring(48, 50).trim());
        transaction.setBuySellCode(input.charAt(50));
        transaction.setQuantityLongSign(input.charAt(51));
        transaction.setQuantityLong(Integer.parseInt(input.substring(52, 62).trim()));
        transaction.setQuantityShortSign(input.charAt(62));
        transaction.setQuantityShort(Integer.parseInt(input.substring(63, 73).trim()));
        transaction.setExchBrokerFeeDec(Double.parseDouble(input.substring(73, 85).trim()) / 100);
        transaction.setExchBrokerFeeDC(input.charAt(85));
        transaction.setExchBrokerFeeCurCode(input.substring(86, 89).trim());
        transaction.setClearingFeeDec(Double.parseDouble(input.substring(89, 101).trim()) / 100);
        transaction.setClearingFeeDC(input.charAt(101));
        transaction.setClearingFeeCurCode(input.substring(102, 105).trim());
        transaction.setCommission(Double.parseDouble(input.substring(105, 117).trim()) / 100);
        transaction.setCommissionDC(input.charAt(117));
        transaction.setCommissionCurCode(input.substring(118, 121).trim());
        transaction.setTransactionDate(LocalDate.parse(input.substring(121, 129).trim(), formatter));
        transaction.setFutureReference(Integer.parseInt(input.substring(129, 135).trim()));
        transaction.setTicketNumber(input.substring(135, 141).trim());
        transaction.setExternalNumber(Integer.parseInt(input.substring(141, 147).trim()));
        transaction.setTransactionPriceDec(Double.parseDouble(input.substring(147, 162).trim()) / 10000000);
        transaction.setTraderInitials(input.substring(162, 168).trim());
        transaction.setOppositeTraderId(input.substring(168, 175).trim());
        transaction.setOpenCloseCode(input.charAt(175));
        transaction.setFiller(input.substring(176));

        return transaction;
    }

    public void addResultsToRepository(@NotNull Transaction transaction) {
        SummaryEntry summaryEntry = new SummaryEntry();
        // Client_Information = CLIENT TYPE, CLIENT NUMBER, ACCOUNT NUMBER, SUBACCOUNT NUMBER
        summaryEntry.setClientInformation(transaction.getClientType() +
                summarySeparator + transaction.getClientNumber() +
                summarySeparator + transaction.getAccountNumber() +
                summarySeparator + transaction.getSubaccountNumber());

        // Product_Information = EXCHANGE CODE, PRODUCT GROUP CODE, SYMBOL, EXPIRATION DATE
        summaryEntry.setProductInformation(transaction.getExchangeCode() +
                summarySeparator + transaction.getProductGroupCode() +
                summarySeparator + transaction.getSymbol() +
                summarySeparator + transaction.getExpirationDate().format(DateTimeFormatter.BASIC_ISO_DATE));

        // Total_Transaction_Amount = Net Total of (QUANTITY LONG - QUANTITY SHORT)
        summaryEntry.setTransactionAmount(transaction.getQuantityLong() - transaction.getQuantityShort());

        transactionSummaryRepository.save(summaryEntry);
    }

    public void transactionProcessor(String input) {
        Transaction transaction = transactionMapper(input);
        addResultsToRepository(transaction);
    }

    public File generateReport() throws IOException {
        File csvOutputFile = new File(filename);

        Map<String, Map<String, Integer>> sumTransactions = transactionSummaryRepository.findAll().stream()
                .collect(Collectors.groupingBy(SummaryEntry::getClientInformation,
                        Collectors.groupingBy(SummaryEntry::getProductInformation,
                                Collectors.summingInt(SummaryEntry::getTransactionAmount))));

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            sumTransactions.forEach((clientInformation, productInformationMap) -> {
                productInformationMap.forEach((productInformation, sum) -> {
                    pw.println(clientInformation + "," + productInformation + "," + sum);
                });
            });
        }
        
        transactionSummaryRepository.deleteAll();

        return csvOutputFile;
    }
}
