package com.finsyswork.erp;

public class scanner_model {

    public String row_no;
    public String qr_Code;
    public String qty;
    public String xicode;
    public String xiname;
    public String xbook_qty;

    public scanner_model(String row_no, String qr_Code, String qty, String xicode, String xiname) {
        this.row_no = row_no;
        this.qr_Code = qr_Code;
        this.qty = qty;
        this.xicode = xicode;
        this.xiname = xiname;
    }

    public scanner_model(String row_no, String qr_Code, String qty, String xicode, String xiname, String xbook_qty) {
        this.row_no = row_no;
        this.qr_Code = qr_Code;
        this.qty = qty;
        this.xicode = xicode;
        this.xiname = xiname;
        this.xbook_qty = xbook_qty;
    }

    public void setXbook_qty(String xbook_qty) {
        this.xbook_qty = xbook_qty;
    }

    public String getXbook_qty() {
        return xbook_qty;
    }

    public String getXiname() {
        return xiname;
    }

    public void setXiname(String xiname) {
        this.xiname = xiname;
    }

    public scanner_model(String row_no, String qr_Code, String qty, String xicode) {
        this.row_no = row_no;
        this.qr_Code = qr_Code;
        this.qty = qty;
        this.xicode = xicode;
    }

    public scanner_model(String row_no, String qr_Code) {
        this.row_no = row_no;
        this.qr_Code = qr_Code;
    }

    public String getRow_no() {
        return row_no;
    }

    public String getQr_Code() {
        return qr_Code;
    }

    public String getXicode() {
        return xicode;
    }

    public void setXicode(String xicode) {
        this.xicode = xicode;
    }

    public String getQty() {
        return qty;
    }

    public void setRow_no(String row_no) {
        this.row_no = row_no;
    }

    public void setQr_Code(String qr_Code) {
        this.qr_Code = qr_Code;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
