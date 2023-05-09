package com.finsyswork.erp;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressdialog {
    Context tcntx;

    ProgressDialog progressDialog;

    public MyProgressdialog(Context cntx) {
        this.tcntx = cntx;
    }

    public void show() {
        progressDialog = new ProgressDialog(tcntx, R.style.ThemeOverlay_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    public void dismiss() {
        progressDialog.dismiss();
    }
}
