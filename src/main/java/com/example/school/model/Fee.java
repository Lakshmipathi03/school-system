package com.example.school.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private String className;
    private Integer rollNumber;
    private Double amount;
    private String status; // Paid / Unpaid
    private String paymentMode; // Cash / UPI
    private String transactionId;
    private LocalDate paymentDate;
    private String receiptNumber;
    private LocalDate receiptDate;

    // Getters & Setters
    public Long getId() { return id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public Integer getRollNumber() { return rollNumber; }
    public void setRollNumber(Integer rollNumber) { this.rollNumber = rollNumber; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }
}