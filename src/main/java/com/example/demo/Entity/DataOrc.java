package com.example.demo.Entity;

import lombok.Data;

@Data
public class DataOrc {
    private String letterCredit;
    private String issueDate;
    private String amount;
    private String beneficiary;
    public String getLetterCredit() {
        return letterCredit;
    }

    public void setLetterCredit(String letterCredit) {
        this.letterCredit = letterCredit;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "DataOrc{" +
                "letterCredit='" + letterCredit + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
