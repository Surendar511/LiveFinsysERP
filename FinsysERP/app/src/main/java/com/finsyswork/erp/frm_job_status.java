package com.finsyswork.erp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class frm_job_status extends AppCompatActivity {

    RecyclerView recycler;
    Button btn_cancel;
    ArrayList<comman_model> list = new ArrayList<>();
    ArrayList<team> xjob = new ArrayList<>();
    job_status_adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_job_status);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        finapi.setColorOfStatusBar(frm_job_status.this);
        recycler = findViewById(R.id.recycler);
        btn_cancel = findViewById(R.id.btn_cancel);

        finapi.context = frm_job_status.this;
        finapi.deleteJsonResponseFile();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fgen.frm_request = "";
                finish();
                return;
            }
        });


        fgen.frm_request = "frm_job_status";

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        adapter = new job_status_adapter(frm_job_status.this, list);
        recycler.addItemDecoration(new DividerItemDecoration(recycler.getContext(), DividerItemDecoration.VERTICAL));
        recycler.setAdapter(adapter);

        final MyProgressdialog progressDialog = new MyProgressdialog(frm_job_status.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        frm_jobw_prod.xjob = new ArrayList<>();
                        frm_jobw_prod.xjob = finapi.fill_record_in_listview_popup("EP835");

                        for (String job: frm_jobw_prod.xjob) {
                            String xstart = "";
                            try {
                                xstart = job.split("---")[2].trim();
                            }catch (Exception e){xstart = "";}

                            String job_list = job.substring(0,20).trim();
                            frm_lead_enquiry.xfg_sub_group_code = job_list.trim();
                            frm_jobw_prod.form_name = "frm_jobw_prod";
                            xjob = finapi.getApiForPOPUP(fgen.mcd, "", "EP835S", fgen.cdt1, fgen.cdt2, fgen.muname, frm_lead_enquiry.xfg_sub_group_code);
                            String xitem = frm_jobw_prod.xicode1 + "---" + frm_jobw_prod.xiname1;
                            String xjob_data = frm_jobw_prod.xjob_no +"---" + frm_jobw_prod.xjob_dt;

                            if(!xstart.isEmpty())
                            {
                                if(!xstart.trim().equals("-")) {
                                    list.add(new comman_model(xitem, xjob_data, xstart, "---", job, frm_jobw_prod.xqty1));
                                }
                                else{
                                    list.add(new comman_model(xitem, xjob_data, "Waiting...", "---", job, frm_jobw_prod.xqty1));
                                }
                            }
                            else
                            {
                                list.add(new comman_model(xitem, xjob_data, "Waiting...", "---", job, frm_jobw_prod.xqty1));
                            }
                        }
                        recycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        progressDialog.dismiss();
                    }
                }, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgen.frm_request = "";
    }
}