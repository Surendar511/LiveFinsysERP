package com.finsyswork.erp;

public class expense_model {

    String expense;
    String amount;
    String remarks;

    public expense_model(String expense, String amount, String remarks) {
        this.expense = expense;
        this.amount = amount;
        this.remarks = remarks;
    }

    public expense_model(String expense, String amount) {
        this.expense = expense;
        this.amount = amount;
    }

    public expense_model() {
    }

    public String getExpense() {
        return expense;
    }

    public String getAmount() {
        return amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
