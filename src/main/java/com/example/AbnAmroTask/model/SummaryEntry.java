package com.example.AbnAmroTask.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryEntry {
    private String clientInformation;
    private String productInformation;
    private int transactionAmount;
}
