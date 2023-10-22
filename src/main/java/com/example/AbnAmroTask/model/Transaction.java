package com.example.AbnAmroTask.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Transaction {
    private String recordCode;
    private String clientType;
    private int clientNumber;
    private int accountNumber;
    private int subaccountNumber;
    private String oppositePartyCode;
    private String productGroupCode;
    private String exchangeCode;
    private String symbol;
    private LocalDate expirationDate;
    private String currencyCode;
    private String movementCode;
    private char buySellCode;
    private char quantityLongSign;
    private int quantityLong;
    private char quantityShortSign;
    private int quantityShort;
    private double exchBrokerFeeDec;
    private char exchBrokerFeeDC;
    private String exchBrokerFeeCurCode;
    private double clearingFeeDec;
    private char clearingFeeDC;
    private String clearingFeeCurCode;
    private double commission;
    private char commissionDC;
    private String commissionCurCode;
    private LocalDate transactionDate;
    private int futureReference;
    private String ticketNumber;
    private int externalNumber;
    private double transactionPriceDec;
    private String traderInitials;
    private String oppositeTraderId;
    private char openCloseCode;
    private String filler;
}
