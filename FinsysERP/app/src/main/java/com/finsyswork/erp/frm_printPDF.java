package com.finsyswork.erp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class frm_printPDF extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_print_pdf);

        Button btn_print = findViewById(R.id.print);
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "0031";
                fgen.x41 = finapi.seekiname(fgen.mcd, "SELECT PARAMS AS COL1 FROM CONTROLS WHERE ID='X41'", "COL1");
                fgen.webReportLink = "http://" + "192.168.1.16:8082/fin-wfin/fin-base/" + "/dprint.aspx?STR=ERP@AND@" + fgen.mcd + "@" + fgen.cdt1.substring(6, 10) + fgen.branchcd + "@" + fgen.muid + "@BVAL@ANDREELPRINT@";
                fgen.printReport(frm_printPDF.this);
            }
        });
    }
}