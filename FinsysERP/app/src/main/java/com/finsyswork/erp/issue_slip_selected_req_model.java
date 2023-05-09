package com.finsyswork.erp;

public class issue_slip_selected_req_model {
    String xiname = "";
    String xqty = "";

    public issue_slip_selected_req_model(String xiname, String xqty) {
        this.xiname = xiname;
        this.xqty = xqty;
    }

    public String getXiname() {
        return xiname;
    }

    public String getXqty() {
        return xqty;
    }

    public void setXiname(String xiname) {
        this.xiname = xiname;
    }

    public void setXqty(String xqty) {
        this.xqty = xqty;
    }
}
