package com.blm.qiubopay.models.apuestas;

import java.io.Serializable;

public class FolioData implements Serializable {
    private int Id;
    private int IdCustomer;
    private int IdTicketNumber;
    private int IdTicketStatus;
    private String Email;
    private String PhoneNumber;
    private WagerResult WagerResult;
    private String Description;
    private String CompleteDescription;
    private String CreatedDate;
    private String SettledDate;
    private int RiskAmount;
    private int WinAmount;
    private int RiskWin;
    private boolean IsCompleteWager;
    private boolean IsWinWager;
    private int FinalWagerAmount;
    private int PaidAmount;
    private Details[] Details;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdCustomer() {
        return IdCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        IdCustomer = idCustomer;
    }

    public int getIdTicketNumber() {
        return IdTicketNumber;
    }

    public void setIdTicketNumber(int idTicketNumber) {
        IdTicketNumber = idTicketNumber;
    }

    public int getIdTicketStatus() {
        return IdTicketStatus;
    }

    public void setIdTicketStatus(int idTicketStatus) {
        IdTicketStatus = idTicketStatus;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public WagerResult getWagerResult() {
        return WagerResult;
    }

    public void setWagerResult(WagerResult wagerResult) {
        WagerResult = wagerResult;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCompleteDescription() {
        return CompleteDescription;
    }

    public void setCompleteDescription(String completeDescription) {
        CompleteDescription = completeDescription;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getSettledDate() {
        return SettledDate;
    }

    public void setSettledDate(String settledDate) {
        SettledDate = settledDate;
    }

    public int getRiskAmount() {
        return RiskAmount;
    }

    public void setRiskAmount(int riskAmount) {
        RiskAmount = riskAmount;
    }

    public int getWinAmount() {
        return WinAmount;
    }

    public void setWinAmount(int winAmount) {
        WinAmount = winAmount;
    }

    public int getRiskWin() {
        return RiskWin;
    }

    public void setRiskWin(int riskWin) {
        RiskWin = riskWin;
    }

    public boolean isCompleteWager() {
        return IsCompleteWager;
    }

    public void setCompleteWager(boolean completeWager) {
        IsCompleteWager = completeWager;
    }

    public boolean isWinWager() {
        return IsWinWager;
    }

    public void setWinWager(boolean winWager) {
        IsWinWager = winWager;
    }

    public int getFinalWagerAmount() {
        return FinalWagerAmount;
    }

    public void setFinalWagerAmount(int finalWagerAmount) {
        FinalWagerAmount = finalWagerAmount;
    }

    public int getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        PaidAmount = paidAmount;
    }

    public Details[] getDetails() {
        return Details;
    }

    public void setDetails(Details[] details) {
        Details = details;
    }
}

