package com.techcoderz.ruchira.ModelClasses;

/**
 * Created by Shahriar on 10/17/2016.
 */

public class OrderSummary {
    String orderDate;
    String invoiceNo;
    String amount;

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
