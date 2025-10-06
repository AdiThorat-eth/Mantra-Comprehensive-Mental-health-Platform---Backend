package com.mantra.chatbot.dto;

public class OrderRequest {
    private Integer amount;
    private String currency;

    // Getters and Setters
    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}