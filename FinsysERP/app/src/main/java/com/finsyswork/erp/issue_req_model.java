package com.finsyswork.erp;

public class issue_req_model {

    String row_no;
    String item;
    String qty;
    String xicode= "";

    public issue_req_model(String row_no, String item, String qty, String xicode) {
        this.row_no = row_no;
        this.item = item;
        this.qty = qty;
        this.xicode = xicode;
    }

    public issue_req_model(String row_no, String item, String qty) {
        this.row_no = row_no;
        this.item = item;
        this.qty = qty;

    }

    public String getXicode() {
        return xicode;
    }

    public void setXicode(String xicode) {
        this.xicode = xicode;
    }

    public issue_req_model() {
    }

    public String getRow_no() {
        return row_no;
    }

    public String getItem() {
        return item;
    }

    public String getQty() {
        return qty;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
