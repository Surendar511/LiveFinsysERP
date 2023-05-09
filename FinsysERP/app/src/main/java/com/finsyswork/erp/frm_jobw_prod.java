package com.finsyswork.erp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class frm_jobw_prod extends AppCompatActivity {

    public static Button btn_new,btn_save, btn_cancel, btn_add, btn_add2, btn_pull, btn_time;
    public static Button btn_scan;
    public static EditText  txtprocess, txtbatch_no, txtqty2, txtselect, txtshift, txtmachine, txtoperator;
    public  static EditText txtselect_job,txtitem,txtqty;
    private RecyclerView recyclerView;
    private TextView txtheading;
    private FloatingActionButton btnAction;
    private LinearLayout linearOutput, linearInput;
    jobw_adapter adapter;
    jobw_adapter adapter_input;
    jobw_adapter adapter_rejection;
    jobw_adapter adapter_downtime;
    Toolbar toolbar;
    String xseperator = "#~#", xbranch="00", xtype ="AV";
    int countdown = 0;
    public static String xx = "right_arrow", xgetTab = "O";
    String xcode = "", xname = "", xqty = "", munit;
    public static String xjob_no = "", xjob_dt = "", xicode1="", xiname1 ="", xqty1="";
    public static ArrayList<jobw_model> output_list = new ArrayList<>();
    public static ArrayList<jobw_model> input_list = new ArrayList<>();
    public static ArrayList<jobw_model> reject_list = new ArrayList<>();
    public static ArrayList<jobw_model> downtime_list = new ArrayList<>();
    jobw_model model;
    public static ArrayList<String> xjob = new ArrayList<>();
    public static ArrayList<String> xshift = new ArrayList<>();
    public static ArrayList<String> xmachine = new ArrayList<>();
    public static ArrayList<String> xoperator = new ArrayList<>();
    public static ArrayList<String> xinput = new ArrayList<>();
    public static ArrayList<String> xreject = new ArrayList<>();
    public static ArrayList<String> xdown_time = new ArrayList<>();
    public static ArrayList<String> xpull = new ArrayList<>();
    public static ArrayList<String> xprocess = new ArrayList<>();
    public static String xjob_card_no = "N";
    public static StringBuilder xstart_time;
    public static StringBuilder xend_time;
    public static String xxprocess_code ="", xlist_selected_text="", form_name = "", xweight=""
            , xoutput_measurement = "P", xinput_measurment="P", xrejection_measurment= "P";
    public static String xxref_no = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_jobw_prod);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        finapi.setColorOfStatusBar(frm_jobw_prod.this);
        //33 , 34, 34
        finapi.context = frm_jobw_prod.this;
        finapi.deleteJsonResponseFile();

        frm_lead_enquiry.xfrm_lead_enquiry = "N";
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_jobw_prod.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        frm_jobw_prod.xjob = new ArrayList<>();
                        frm_jobw_prod.xshift = new ArrayList<>();
                        frm_jobw_prod.xprocess = new ArrayList<>();
                        frm_jobw_prod.xoperator = new ArrayList<>();
                        frm_jobw_prod.xpull = new ArrayList<>();
                        frm_jobw_prod.xinput = new ArrayList<>();
                        //this method will be running on background thread so don't update UI frome here
                        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
                        frm_lead_enquiry.xfrm_lead_enquiry = "N";
                        frm_jobw_prod.xjob = finapi.fill_record_in_listview_popup("EP835");
                        frm_lead_enquiry.xfrm_lead_enquiry = "N";
                        frm_jobw_prod.xshift = finapi.fill_record_in_listview_popup("EP830");
                        frm_lead_enquiry.xfrm_lead_enquiry = "N";
                        frm_jobw_prod.xprocess = finapi.fill_record_in_listview_popup("EP820");
                        frm_lead_enquiry.xfrm_lead_enquiry = "N";
                        frm_jobw_prod.xoperator = finapi.fill_record_in_listview_popup("EP832");
                        // frm_jobw_prod.xpull = finapi.fill_record_in_listview_popup("EP826");

//                        new AsyncCaller_jobw_prod().execute();
                        progressDialog.dismiss();
                    }
                }, 100);

        initializeViews();

        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        btn_cancel.setText("EXIT");
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_jobw_prod.this);
        Highlightborder();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new jobw_adapter(getApplicationContext());
        adapter_input = new jobw_adapter(getApplicationContext());
        adapter_rejection = new jobw_adapter(getApplicationContext());
        adapter_downtime = new jobw_adapter(getApplicationContext());
        recyclerView.setAdapter(adapter);

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBtnClickedMethod();
                enabledViewMethod();
                btn_save.setEnabled(true);
                btn_new.setEnabled(false);
                btn_cancel.setText("CANCEL");
                xsetStartTimeAndEndTime();
                txtselect_job.setBackgroundResource(R.color.light_blue);

                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);
            }
        });

        // frm_job_status work
        try {
            Bundle b = getIntent().getExtras();
            if(fgen.xjob_status_call_count > 0) {
                btn_new.performClick();
                txtitem.setText(b.getString("item"));
                txtbatch_no.setText(b.getString("job_no"));
                txtselect_job.setText(b.getString("job"));
                txtqty.setText(b.getString("qty"));
                btn_scan.setEnabled(false);
                txtselect_job.setEnabled(false);

                fgen.xjob_status_call_count = 0;
                btn_cancel.setText("EXIT");
            }
        } catch (Exception e) {}
        // frm_job_status work


        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xjob_card_no = "Y";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
            }
        });

        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(frm_jobw_prod.this);
                builder1.setMessage("Start Time : " + xstart_time +"\n"+ "End Time : " + xend_time);
                builder1.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                xsetStartTimeAndEndTime();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn_text = btn_cancel.getText().toString();
                if(btn_text == "CANCEL")
                {
                    final MyProgressdialog progressDialog = new MyProgressdialog(frm_jobw_prod.this);
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            newBtnClickedMethod();
                            disableViewsMethod();
                            btn_save.setEnabled(false);
                            btn_new.setEnabled(true);
                            btn_cancel.setText("EXIT");
                            xx = "right_arrow";

                            txtselect_job.setBackgroundResource(R.color.light_grey);
                            txtitem.setBackgroundResource(R.color.light_grey);
                            txtprocess.setBackgroundResource(R.color.light_grey);
                            txtbatch_no.setBackgroundResource(R.color.light_grey);
                            txtqty.setBackgroundResource(R.color.light_grey);

                            input_list = new ArrayList<>();
                            xgetTab = "I";
                            adapter_input.notifyDataSetChanged();

                            reject_list = new ArrayList<>();
                            xgetTab = "R";
                            adapter_rejection.notifyDataSetChanged();

                            downtime_list = new ArrayList<>();
                            xgetTab = "D";
                            adapter_downtime.notifyDataSetChanged();

                            output_list = new ArrayList<>();
                            xgetTab = "O";
                            adapter = new jobw_adapter(getApplicationContext());
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            frm_lead_enquiry.xfrm_lead_enquiry = "N";
                            btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));Intent intent = getIntent();
                            frm_jobw_prod.xjob = finapi.fill_record_in_listview_popup("EP835");
                            frm_jobw_prod.xshift = finapi.fill_record_in_listview_popup("EP830");
                            frm_jobw_prod.xprocess = finapi.fill_record_in_listview_popup("EP820");
                            frm_jobw_prod.xoperator = finapi.fill_record_in_listview_popup("EP832");
                            //  frm_jobw_prod.xpull = finapi.fill_record_in_listview_popup("EP826");


//                        finish();
//                        startActivity(intent);
                            //   On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 100);
                }
                if(btn_text == "EXIT")
                {
                    input_list = new ArrayList<>();
                    xgetTab = "I";
                    adapter_input.notifyDataSetChanged();

                    reject_list = new ArrayList<>();
                    xgetTab = "R";
                    adapter_rejection.notifyDataSetChanged();

                    downtime_list = new ArrayList<>();
                    xgetTab = "D";
                    adapter_downtime.notifyDataSetChanged();

                    output_list = new ArrayList<>();
                    xgetTab = "O";
                    adapter = new jobw_adapter(getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    finish();
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xitem = txtitem.getText().toString();
                String xbatch = txtbatch_no.getText().toString();
                xbatch = "AUTO";
                String xqty = txtqty.getText().toString();
                if(txtselect_job.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Scan Job Card First!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtqty.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Fill Qty!!", Toast.LENGTH_LONG).show();
                    return;
                }
                model = new jobw_model(xitem, xbatch, xqty, xoutput_measurement);
                output_list.add(model);
                recyclerView.setAdapter(adapter);
                txtqty.setText("0");
                Toast.makeText(getApplicationContext(), "JOB Wise Output Is Added Successfully", Toast.LENGTH_LONG).show();
            }
        });

        btn_add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtselect.getText().toString().isEmpty() || txtqty2.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Fill Name And Qty First!!", Toast.LENGTH_LONG).show();
                    return;
                }
                String xrecord = txtselect.getText().toString();
                switch (xgetTab)
                {
                    case "I":
                        xcode = xrecord.split("!~!~!")[3];
                        xname = xrecord.split("!~!~!")[0] + "---" + xrecord.split("!~!~!")[1];
                        xqty =txtqty2.getText().toString();
                        for (jobw_model m: input_list) {
                            if(m.xcol2.equals(xname)) {
                                AlertDialog alertDialog = new AlertDialog.Builder(frm_jobw_prod.this).create();
                                alertDialog.setTitle("Error");
                                alertDialog.setMessage("Sorry, Duplicate Rows Are Not Allowed!!");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                return;
                            }
                        }
                        model = new jobw_model(xcode, xname, xqty, xinput_measurment);
                        input_list.add(model);
                        recyclerView.setAdapter(adapter_input);
                        adapter_input.notifyDataSetChanged();
                        break;
                    case "R":
                        xcode = xrecord.split("---")[1];
                        xname = xrecord.split("---")[0];
                        xqty =txtqty2.getText().toString();
                        for (jobw_model m: reject_list) {
                            if(m.xcol1.equals(xcode)) {
                                AlertDialog alertDialog = new AlertDialog.Builder(frm_jobw_prod.this).create();
                                alertDialog.setTitle("Error");
                                alertDialog.setMessage("Sorry, Duplicate Items Are Not Allowed!!");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                return;
                            }
                        }
                        if ((xrejection_measurment) == "K") {
                            munit = "KGS";
                        }
                        else{
                            munit="NOS";
                        }
                        model = new jobw_model(xcode, xname, xqty, munit);
                        reject_list.add(model);
                        recyclerView.setAdapter(adapter_rejection);
                        adapter_rejection.notifyDataSetChanged();
                        break;
                    case "D":
                        xcode = xrecord.split("---")[1];
                        xname = xrecord.split("---")[0];
                        xqty =txtqty2.getText().toString();
                        for (jobw_model m: downtime_list) {
                            if(m.xcol1.equals(xcode)) {
                                AlertDialog alertDialog = new AlertDialog.Builder(frm_jobw_prod.this).create();
                                alertDialog.setTitle("Error");
                                alertDialog.setMessage("Sorry, Duplicate Items Are Not Allowed!!");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                return;
                            }
                        }

                        model = new jobw_model(xcode, xname, xqty);
                        downtime_list.add(model);
                        recyclerView.setAdapter(adapter_downtime);
                        adapter_downtime.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
                txtselect.setText("");
                txtqty2.setText("");
            }
        });

        txtprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_jobw_prod.this, xprocess, "txtprocess");
            }
        });

        btn_pull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtselect.setText("");
                txtqty2.setText("");
                if(countdown < 3 && xx.equals("right_arrow"))
                {
                    txtselect.setVisibility(View.GONE);
                    txtqty2.setVisibility(View.GONE);
                    btn_add2.setVisibility(View.GONE);
                    btn_add.setVisibility(View.VISIBLE);

                    btn_pull.setVisibility(View.GONE);
                    btn_add.setEnabled(true);

                    btnAction.setImageResource(R.drawable.right_arrow);
                    countdown++;
                    if(countdown == 3)
                    {
                        btnAction.setImageResource(R.drawable.left_arrow);
                        xx = "left_arrow";
                    }
                    linearOutput.setVisibility(View.GONE);
                    linearInput.setVisibility(View.VISIBLE);
                    if(countdown == 1)
                    {
                        txtheading.setText("INPUT");
                        xgetTab = "I";
                        btn_add.setEnabled(false);
                        txtselect.setVisibility(View.VISIBLE);
                        txtqty2.setVisibility(View.VISIBLE);
                        btn_add2.setVisibility(View.VISIBLE);
                        btn_add.setVisibility(View.INVISIBLE);
//    done by surendar  btn_pull.setVisibility(View.VISIBLE);
                        btn_pull.setVisibility(View.GONE);
                        recyclerView.setAdapter(adapter_input);
                        adapter_input.notifyDataSetChanged();
                    }
                    if(countdown == 2)
                    {
                        xgetTab = "R";
                        txtheading.setText("REJECT");
                        txtselect.setVisibility(View.VISIBLE);
                        txtqty2.setVisibility(View.VISIBLE);
                        btn_add2.setVisibility(View.VISIBLE);
                        btn_add.setVisibility(View.INVISIBLE);
                        btn_add.setEnabled(false);
                        recyclerView.setAdapter(adapter_rejection);
                        adapter.notifyDataSetChanged();
                    }
                    if(countdown == 3)
                    {
                        xgetTab = "D";
                        txtheading.setText("DOWN TIME");
                        txtselect.setVisibility(View.VISIBLE);
                        txtqty2.setVisibility(View.VISIBLE);
                        btn_add2.setVisibility(View.VISIBLE);
                        btn_add.setVisibility(View.INVISIBLE);
                        btn_add.setEnabled(false);
                        recyclerView.setAdapter(adapter_downtime);
                        adapter.notifyDataSetChanged();
                    }
                }
                else {
                    btnAction.setImageResource(R.drawable.left_arrow);
                    countdown--;

                    btn_add.setEnabled(true);
                    txtselect.setVisibility(View.GONE);
                    txtqty2.setVisibility(View.GONE);
                    btn_add2.setVisibility(View.GONE);
                    btn_pull.setVisibility(View.GONE);
                    btn_add.setVisibility(View.VISIBLE);

                    if(countdown == 0)
                    {
                        xgetTab = "O";
                        txtheading.setText("OUTPUT");
                        btnAction.setImageResource(R.drawable.right_arrow);
                        linearOutput.setVisibility(View.VISIBLE);
                        linearInput.setVisibility(View.GONE);
                        xx = "right_arrow";
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        btn_add.setVisibility(View.VISIBLE);
                    }
                    if(countdown == 1)
                    {
                        xgetTab = "I";
                        btn_add.setEnabled(false);
                        txtheading.setText("INPUT");
                        txtselect.setVisibility(View.VISIBLE);
                        txtqty2.setVisibility(View.VISIBLE);
                        btn_add2.setVisibility(View.VISIBLE);
                        btn_pull.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(adapter_input);
                        btn_add.setVisibility(View.INVISIBLE);
                        adapter.notifyDataSetChanged();
                    }
                    if(countdown == 2)
                    {
                        xgetTab = "R";
                        btn_add.setEnabled(false);
                        txtheading.setText("REJECT");
                        txtselect.setVisibility(View.VISIBLE);
                        txtqty2.setVisibility(View.VISIBLE);
                        btn_add2.setVisibility(View.VISIBLE);
                        btn_add.setVisibility(View.INVISIBLE);
                        recyclerView.setAdapter(adapter_rejection);
                        adapter.notifyDataSetChanged();
                    }
                    if(countdown == 3)
                    {
                        xgetTab = "D";
                        btn_add.setEnabled(false);
                        txtheading.setText("DOWN TIME");
                        txtselect.setVisibility(View.VISIBLE);
                        txtqty2.setVisibility(View.VISIBLE);
                        btn_add2.setVisibility(View.VISIBLE);
                        btn_add.setVisibility(View.INVISIBLE);
                        recyclerView.setAdapter(adapter_downtime);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        txtselect_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_jobw_prod.this, xjob, "txtselect_job");
            }
        });
        txtshift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_jobw_prod.this, xshift, "txtshift");
            }
        });
        txtmachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtprocess.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please, Select Process First!!", Toast.LENGTH_LONG).show();
                    return;
                }
                ShowDialogBox(frm_jobw_prod.this, xmachine, "txtmachine");
            }
        });
        txtoperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_jobw_prod.this, xoperator, "txtoperator");
            }
        });
        txtselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtprocess.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please, Select Process First!!", Toast.LENGTH_LONG).show();
                    return;
                }
                switch (xgetTab) {
                    case "I":
                        ShowDialogBox(frm_jobw_prod.this, xinput, "txtselect");
                        break;
                    case "R":
                        ShowDialogBox(frm_jobw_prod.this, xreject, "txtselect");
                        break;
                    case "D":
                        ShowDialogBox(frm_jobw_prod.this, xdown_time, "txtselect");
                        break;
                    default:
                        break;
                }
            }
        });


        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_jobw_prod.this, "Type 40 : aEATMAKE_ins\n" +
                            "Type 25 : aEATMAKE_ins\n" +
                            "Type 45 : aDownREJ_ins\n" +
                            "Type 55 : aDownREJ_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_jobw_prod.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(output_list.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "OUTPUT Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(input_list.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "INPUT Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(reject_list.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "REJECTION Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(downtime_list.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "DOWNTIME Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtshift.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "SHIFT Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtmachine.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "MACHINE Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtoperator.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "INCHARGE Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_jobw_prod.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = "";
                                String xxjob_card = txtselect_job.getText().toString();
//                                String xxjob_card1 = txtbatch_no2.getText().toString();
//                                String xxjob_card2 = txtbatch_no3.getText().toString();
                                String xxprocess_code = txtprocess.getText().toString().split("---")[0].trim();
                                String xxshift_code = txtshift.getText().toString().split("---")[0].trim();
                                String xxmachine_code = txtmachine.getText().toString().split("---")[0].trim();
                                String xxincharge_code = txtoperator.getText().toString().split("---")[1].trim();


                                double in_tin_qty_in_kg=0;
                                double in_tin_qty_in_pc=0;
                                double in_tin_qty=0;
                                double in_tot_qty_in_kg=0;
                                double in_tot_qty_in_pc=0;
                                double in_tot_qty=0;
                                double in_trj_qty_in_kg=0;
                                double in_trj_qty_in_pcs=0;
                                double in_trj_qty=0;
                                double xtotal_output_qty=0;
                                double xtotal_reject_qty=0;
                                for (jobw_model xcal_input: input_list) {
                                    String xunit = xcal_input.xcol4;
                                    if(xunit.equals("K")) {
                                        in_tin_qty_in_kg = in_tin_qty_in_kg + Double.parseDouble(xcal_input.xcol3);
                                    }
                                    else {
                                        in_tin_qty_in_pc = in_tin_qty_in_pc + (Double.parseDouble(xweight) * Double.parseDouble(xcal_input.xcol3));
                                    }
                                }
                                in_tin_qty = in_tin_qty_in_kg + in_tin_qty_in_pc;

                                for (jobw_model xcal_input: output_list) {
                                    String xunit = xcal_input.xcol4;
                                    if(xunit.equals("K")) {
                                        in_tot_qty_in_kg = in_tot_qty_in_kg + Double.parseDouble(xcal_input.xcol3);
                                    }
                                    else {
                                        in_tot_qty_in_pc = in_tot_qty_in_pc +( Double.parseDouble(xweight) * Double.parseDouble(xcal_input.xcol3));
                                    }
                                }
                                in_tot_qty = in_tot_qty_in_kg + in_tot_qty_in_pc;

                                for (jobw_model xcal_input: output_list) {
                                    String xunit = xcal_input.xcol4;
                                    xtotal_output_qty =  xtotal_output_qty + Double.parseDouble(xcal_input.xcol3);
                                }
                                for (jobw_model xcal_input: reject_list) {
                                    String xunit = xcal_input.xcol4.trim().toUpperCase();
                                    if(xunit.equals("NOS")) {
                                        String str = "NOS";
                                        xtotal_reject_qty =  xtotal_reject_qty + Double.parseDouble(xcal_input.xcol3);
                                    }
                                }

                                for (jobw_model xcal_input: reject_list) {
                                    String xunit = xcal_input.xcol4.trim().toUpperCase();
                                    if(xunit.equals("KGS")) {
                                        in_trj_qty_in_kg = in_trj_qty_in_kg + Double.parseDouble(xcal_input.xcol3);
                                    }
                                    else {
                                        in_trj_qty_in_pcs = in_trj_qty_in_pcs + (Double.parseDouble(xweight) + Double.parseDouble(xcal_input.xcol3));
                                    }
                                }
                                in_trj_qty = in_trj_qty_in_kg + in_trj_qty_in_pcs;

                                Double xoutput_kgs = xtotal_output_qty * Double.parseDouble(xweight);
                                Double xscr_weight = in_trj_qty_in_kg + (xtotal_reject_qty * Double.parseDouble(xweight));

                                if(!xxprocess_code.equals("61"))  // For Printing
                                {
                                    in_tin_qty = in_tin_qty * Double.parseDouble(xweight);
                                }

                                long xdiff = Math.round(Math.round((in_tin_qty - (xoutput_kgs + xscr_weight))*100.00)/100.00);
                                if(xdiff != 0.00)
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(frm_jobw_prod.this);
                                    builder.setTitle("Finsys");
                                    String myHtml="";

                                    myHtml="<table width='100%'> " +"<tr> "
                                            +"<td width='50%'>Please See Total Input</td>"
                                            +"<td>" + finapi.space(5) + in_tin_qty + "</td>"
                                            +"</tr>"
                                            +"<tr><td colspan='2'  style='border-bottom: groove 1px;'><hr></hr> <br></td>"
                                            +"<tr>"
                                            +"<tr> "
                                            +"<td colspan='2'><br> <b> <u> Output </u> </b><br></td> "
                                            +"</tr>"
                                            +" <tr>"
                                            +"<td>&nbsp;a)Ok </td>"
                                            +"<td>=&nbsp; "+ finapi.space(10) + String.format("%.2f", xtotal_output_qty) + "<br></td>"
                                            +"</tr>"
                                            +"<tr>"
                                            +"<td >&nbsp;b)Reject (Nos) </td>"
                                            +"<td>=&nbsp; " + finapi.space(8) + String.format("%.2f", xtotal_reject_qty) + "<br></td>"
                                            +"</tr>"
                                            +"<tr>"
                                            +"<td >&nbsp;c)Reject (KG) </td>"
                                            +"<td>=&nbsp; " + finapi.space(8) + String.format("%.2f", in_trj_qty_in_kg) + "<br></td>"
                                            +"</tr>"
                                            +"<tr>"
                                            +"<td></td>"
                                            +"<td colspan='2' style='border-bottom: groove 1px;' ><hr></hr></td>"
//                                            +"<tr>"
//                                            +"<td></td>"
//                                            +"<td>"
//                                            +"&nbsp;&nbsp; " + finapi.space(21) + String.format("%.2f", xtotal_output_qty  + xtotal_reject_qty)
//                                            +"<br></td>"
//                                            +"<tr>"
                                            +"<tr>"
                                            +"<td></td>"
                                            +"<td colspan='2' style='border-bottom: groove 1px;'><hr></hr></td>"
                                            +"<tr>"
                                            +"<td colspan='2'><br>Std Wt/Pc<br></td>"
                                            +"</tr>"
                                            +"<tr>"
                                            +"<td>(As Per Job Card Paper Wt.)</td>"
                                            +"<td>&nbsp;&nbsp; " + finapi.space(3) + xweight + "<br></td>"
                                            +"</tr>"
                                            +"<tr>"
                                            +"<td>Output Kgs.</td>"
                                            +"<td>&nbsp;="+ finapi.space(3) + String.format("%.2f", xoutput_kgs) +"<br></td>"
                                            +"</tr>"
                                            +"<tr>"
                                            +"<td>Scrp Wt.</td>"
                                            +"<td>&nbsp;=&nbsp; " + finapi.space(7) + String.format("%.2f", xscr_weight) + "<br></td>"
                                            +"</tr>"
                                            +" <tr>"
                                            +"<td colspan='2' style='border-bottom: groove 1px;'>"
                                            +"<hr></hr><br>"
                                            +"</td>"
                                            +"</tr>"
                                            +"<tr>"
                                            +"<td>Diff.</td>"
                                            +"<td>&nbsp;&nbsp;" + String.valueOf(xdiff) + "</td>"
                                            +"</tr>"
                                            +"<tr>"
                                            +"<td  colspan='2'><br><br> It Should Be Nil <br> </td>"
                                            +"</tr>"
                                            +"<tr>"
                                            +"<td colspan='2'>Please Check Output Wt. Less <br></td>"
                                            +"</tr>"
                                            +"<tr>"
                                            +"<td colspan='2'>Input More</td>"
                                            +"</tr>"
                                            +"</table>";


                                    builder.setMessage(Html.fromHtml(myHtml));
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                    progressDialog.dismiss();
                                    return;
                                }

                                // output
                                xtype = "40";
                                for (int i=0; i<output_list.size(); i++) {
                                    String xcol1 = output_list.get(i).getXcol1();  // icode + iname
                                    String xcol2 = output_list.get(i).getXcol2();  // batch no
                                    String xcol3 = output_list.get(i).getXcol3();  // qty
                                    xicode1 = xcol1.split("---")[0];

                                    post_param += xbranch + xseperator + xtype + xseperator + xicode1
                                            + xseperator + xcol2 + xseperator + xcol3 + xseperator
                                            + xxjob_card + xseperator + xxprocess_code + xseperator
                                            + xxshift_code + xseperator + xxmachine_code + xseperator
                                            + xxincharge_code + xseperator + xstart_time + xseperator + xend_time + "!~!~!";
                                }
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aEATMAKE_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                boolean msg = finapi.showAlertMessageForMultipleSaving(frm_jobw_prod.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }

                                // input
                                xtype = "25";
                                post_param = "";
                                for (int i=0; i<input_list.size(); i++) {
                                    String xcol1 = input_list.get(i).getXcol1();  // Code
                                    String xcol2 = input_list.get(i).getXcol2();  // Reel Code + Reel Name
                                    String xcol3 = input_list.get(i).getXcol3();  // Qty

                                    String xreel_code =  xcol2.split("---")[0];
                                    post_param += xbranch + xseperator + xtype + xseperator + xcol1
                                            + xseperator + xreel_code + xseperator + xcol3 + xseperator
                                            + xxjob_card + xseperator + xxprocess_code + xseperator
                                            + xxshift_code + xseperator + xxmachine_code + xseperator
                                            + xxincharge_code  + xseperator + xstart_time + xseperator + xend_time + "!~!~!";
                                }
                                result = finapi.getApi(fgen.mcd, "aEATMAKE_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10),  xxref_no, "-");
                                msg = finapi.showAlertMessageForMultipleSaving(frm_jobw_prod.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }

                                // Rejection
                                xtype = "45";
                                post_param = "";
                                for (int i=0; i<reject_list.size(); i++) {
                                    String xcol1 = reject_list.get(i).getXcol1();  // Code
                                    String xcol2 = reject_list.get(i).getXcol2();  // Name
                                    String xcol3 = reject_list.get(i).getXcol3();  // Qty
                                    String xcol4 = reject_list.get(i).getXcol4();  // REJECTION UNIT

                                    post_param += xbranch + xseperator + xtype + xseperator + xcol1
                                            + xseperator + xcol2 + xseperator + xcol3 + xseperator
                                            + xxjob_card + xseperator + xxprocess_code + xseperator
                                            + xxshift_code + xseperator + xxmachine_code + xseperator
                                            + xxincharge_code + xseperator +  xcol4 + "!~!~!";
                                }
                                result = finapi.getApi(fgen.mcd, "aDownREJ_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10),  xxref_no, "-");
                                msg = finapi.showAlertMessageForMultipleSaving(frm_jobw_prod.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }

                                // DownTime
                                xtype = "55";
                                post_param = "";
                                for (int i=0; i<downtime_list.size(); i++) {
                                    String xcol1 = downtime_list.get(i).getXcol1();  // Code
                                    String xcol2 = downtime_list.get(i).getXcol2();  // Name
                                    String xcol3 = downtime_list.get(i).getXcol3();  // Qty
                                    String xxcol6 ="-";

                                    post_param += xbranch + xseperator + xtype + xseperator + xcol1
                                            + xseperator + xcol2 + xseperator + xcol3 + xseperator
                                            + xxjob_card + xseperator + xxprocess_code + xseperator
                                            + xxshift_code + xseperator + xxmachine_code + xseperator
                                            + xxincharge_code + xseperator + xxcol6 + "!~!~!";
                                }
                                result = finapi.getApi(fgen.mcd, "aDownREJ_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), xxref_no, "-");
                                msg = finapi.showAlertMessage(frm_jobw_prod.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();

                                txtselect_job.setEnabled(true);
                                input_list = new ArrayList<>();
                                xgetTab = "I";
                                adapter_input.notifyDataSetChanged();

                                reject_list = new ArrayList<>();
                                xgetTab = "R";
                                adapter_rejection.notifyDataSetChanged();

                                downtime_list = new ArrayList<>();
                                xgetTab = "D";
                                adapter_downtime.notifyDataSetChanged();

                                output_list = new ArrayList<>();
                                xgetTab = "O";
                                adapter = new jobw_adapter(getApplicationContext());
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();


                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });


    }

    private void xsetStartTimeAndEndTime() {
        final View dialogView = View.inflate(frm_jobw_prod.this, R.layout.time_picker_for_production, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(frm_jobw_prod.this).create();
        alertDialog.setCanceledOnTouchOutside(false);

        TimePicker timePickerStart = dialogView.findViewById(R.id.time_picker_entry);
        TimePicker timePickerEnd = dialogView.findViewById(R.id.time_picker_exit);

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(view -> {
            int hour = timePickerStart.getCurrentHour();
            int min =  timePickerStart.getCurrentMinute();
            xsetStartTime(hour, min);

            hour = timePickerEnd.getCurrentHour();
            min =  timePickerEnd.getCurrentMinute();
            xsetEndTime(hour, min);

            alertDialog.dismiss();
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    public void xsetStartTime(int hour, int min) {
        String format="";
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        xstart_time = new StringBuilder().append(String.format("%02d:%02d", hour, min))
                .append(" ").append(format);
    }

    public void xsetEndTime(int hour, int min) {
        String format="";
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        xend_time = new StringBuilder().append(String.format("%02d:%02d", hour, min))
                .append(" ").append(format);
    }



    private void Highlightborder() {
        txtselect_job.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txtitem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtprocess.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtbatch_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtqty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
    }

    private void enabledViewMethod() {
        btn_scan.setEnabled(true);
        txtselect_job.setEnabled(true);
        txtitem.setEnabled(true);
        txtprocess.setEnabled(true);
        txtbatch_no.setEnabled(true);
        txtqty.setEnabled(true);
        txtshift.setEnabled(true);
        txtmachine.setEnabled(true);
        txtoperator.setEnabled(true);
        txtselect.setEnabled(true);
        txtqty2.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtselect_job.setText("");
        txtitem.setText("");
        txtprocess.setText("");
        txtbatch_no.setText("");
        txtqty.setText("");
        txtshift.setText("");
        txtmachine.setText("");
        txtoperator.setText("");
        txtselect.setText("");
        txtqty2.setText("");
        xstart_time = null;
        xend_time = null;
    }

    private void disableViewsMethod() {
        btn_scan.setEnabled(false);
        txtselect_job.setEnabled(false);
        txtitem.setEnabled(false);
        txtprocess.setEnabled(false);
        txtbatch_no.setEnabled(false);
        txtqty.setEnabled(false);
        txtshift.setEnabled(false);
        txtmachine.setEnabled(false);
        txtoperator.setEnabled(false);
        txtselect.setEnabled(false);
        txtqty2.setEnabled(false);
    }

    private void initializeViews() {
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_scan = findViewById(R.id.btn_scan);
        btn_add = findViewById(R.id.btn_add);
        btn_add2 = findViewById(R.id.btn_add2);
        btn_pull = findViewById(R.id.btn_pull);
        btn_time = findViewById(R.id.btn_time);
        txtselect_job = findViewById(R.id.txtscan_job);
        txtitem = findViewById(R.id.txtitem);
        txtshift = findViewById(R.id.txtshift);
        txtmachine = findViewById(R.id.txtmachine);
        txtoperator = findViewById(R.id.txtoperator);
        txtprocess = findViewById(R.id.txtprocess);
        txtbatch_no = findViewById(R.id.txtbatch);
        txtqty = findViewById(R.id.txtqty);
        txtqty2 = findViewById(R.id.txtqty2);
        txtselect = findViewById(R.id.txtselect);
        recyclerView = findViewById(R.id.recycler);
        toolbar = findViewById(R.id.toolbar);
        txtheading = findViewById(R.id.txtheading);
        txtheading = findViewById(R.id.txtheading);
        btnAction = findViewById(R.id.fab);
        linearOutput = findViewById(R.id.linear_output);
        linearInput = findViewById(R.id.linear_input);
    }

    public void ShowDialogBox(Context context, ArrayList<String> list, String controll){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.search_lead);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 1.0f);
        int dialogWindowHeight = (int) (displayHeight * 1.0f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setLayout(layoutParams.width, layoutParams.height);
        dialog.show();

        EditText search_text = dialog.findViewById(R.id.txtsearch);
        ListView search_list = dialog.findViewById(R.id.seach_list);
        ImageView close_window = dialog.findViewById(R.id.close_window);

        TextView txtapi_code = dialog.findViewById(R.id.txtapi_code);
        if(controll.equals("txtselect_job")) txtapi_code.setText("EP835S");
        if(controll.equals("txtshift")) txtapi_code.setText("EP830");
        if(controll.equals("txtmachine")) txtapi_code.setText("EP831");
        if(controll.equals("txtoperator")) txtapi_code.setText("EP832");
        if(controll.equals("txtselect"))
        {
            if(xgetTab.equals("I")) txtapi_code.setText("EP839");
            if(xgetTab.equals("R")) txtapi_code.setText("EP833");
            if(xgetTab.equals("D")) txtapi_code.setText("EP834");

        }
        if(controll.equals("txtprocess")) txtapi_code.setText("EP820");


        close_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
//        search_list.setAdapter(adapter);

        final SearchListAdapter adapter = new SearchListAdapter(context, list);
        search_list.setAdapter(adapter);


        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String list_selected_text = adapter.getItem(position).toString();
                switch (controll)
                {
                    case "txtselect_job" :
//                      showAlertDialog("O");
                        xoutput_measurement = "P";
                        xlist_selected_text = list_selected_text.trim();
                        AlertDialog.Builder builder = new AlertDialog.Builder(frm_jobw_prod.this);
                        builder.setTitle("JOB CARD NO?");
                        builder.setMessage("ARE YOU SURE, You Want To Do Select This Job Card ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txtselect_job.setText(xlist_selected_text.substring(0,20).trim());
                                frm_lead_enquiry.xfg_sub_group_code = xlist_selected_text.substring(0, 20).trim();
                                xlist_selected_text = xlist_selected_text.trim().toString();
                                form_name = "frm_jobw_prod";
                                ArrayList<team> xjob = finapi.getApiForPOPUP(fgen.mcd, "", "EP835S", fgen.cdt1, fgen.cdt2, fgen.muname, frm_lead_enquiry.xfg_sub_group_code);
                                btn_scan.setEnabled(false);
                                txtselect_job.setEnabled(false);
                                txtbatch_no.setText(xlist_selected_text.substring(4,10).trim());
                                btn_scan.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                    case "txtshift"  : txtshift.setText(list_selected_text.trim());
                        break;
                    case "txtmachine"  : txtmachine.setText(list_selected_text.trim());
                        break;
                    case "txtoperator" : txtoperator.setText(list_selected_text.trim());
                        break;
                    case "txtselect" :
                        try {
                            txtqty2.setText(list_selected_text.split("!~!~!")[2].trim());
                            txtselect.setText(list_selected_text.trim());
                            xinput_measurment = "K";
//                            showAlertDialog(xgetTab);
                        }
                        catch (Exception e){
                            txtselect.setText(list_selected_text.trim());
                            if(xgetTab.equals("R"))
                            {
                                showAlertDialog(xgetTab);
                            }
                        }
                        break;
                    case "txtprocess" : txtprocess.setText(list_selected_text.trim());
                        final MyProgressdialog progressDialog = new MyProgressdialog(frm_jobw_prod.this);
                        progressDialog.show();
                        xxprocess_code = list_selected_text.substring(0,2).trim();
                        frm_lead_enquiry.xfg_sub_group_code = list_selected_text.substring(0,2).trim();
                        frm_lead_enquiry.xfrm_lead_enquiry = "Y";
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        frm_lead_enquiry.xfrm_lead_enquiry = "Y";
                                        frm_lead_enquiry.xfg_sub_group_code = frm_jobw_prod.xxprocess_code;
                                        frm_jobw_prod.xinput = finapi.fill_record_in_listview_popup_for_jobwprod("EP839");
                                        frm_lead_enquiry.xfrm_lead_enquiry = "Y";
                                        frm_lead_enquiry.xfg_sub_group_code = frm_jobw_prod.xxprocess_code;
                                        frm_jobw_prod.xmachine = finapi.fill_record_in_listview_popup("EP831");
                                        new AsyncCaller_jobw_prod_process().execute();
                                        progressDialog.dismiss();
                                    }
                                }, 100);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        frm_lead_enquiry.xfrm_lead_enquiry = "N";
        xx = "right_arrow";
    }




    private void showAlertDialog(String getTab) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(frm_jobw_prod.this);
        alertDialog.setTitle("Select Unit");
        String[] items = {"Pcs","Kg"};
        int checkedItem = 1;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        switch (getTab){
                            case "O":
                                xoutput_measurement = "P";
                                dialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(frm_jobw_prod.this);
                                builder.setTitle("JOB CARD NO?");
                                builder.setMessage("ARE YOU SURE, You Want To Do Select This Job Card ?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        txtselect_job.setText(xlist_selected_text.substring(0,20).trim());
                                        frm_lead_enquiry.xfg_sub_group_code = xlist_selected_text.substring(0, 20).trim();
                                        xlist_selected_text = xlist_selected_text.toString().trim();
                                        form_name = "frm_jobw_prod";
                                        ArrayList<team> xjob = finapi.getApi(fgen.mcd, "", "EP835S", fgen.cdt1, fgen.cdt2, fgen.muname, frm_lead_enquiry.xfg_sub_group_code);
                                        btn_scan.setEnabled(false);
                                        txtselect_job.setEnabled(false);
                                        txtbatch_no.setText(xlist_selected_text.substring(4,10).trim());
                                        btn_scan.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                AlertDialog dialog2 = builder.create();
                                dialog2.show();
                                break;
                            case "R":
                                xrejection_measurment = "P";
                                dialog.dismiss();
                                break;
                            case "I" :
                                xinput_measurment = "P";
                                dialog.dismiss();
                                break;
                        }

                        break;
                    case 1:
                        switch (getTab){
                            case "O":
                                xoutput_measurement = "K";
                                dialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(frm_jobw_prod.this);
                                builder.setTitle("JOB CARD NO?");
                                builder.setMessage("ARE YOU SURE, You Want To Do Select This Job Card ?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        txtselect_job.setText(xlist_selected_text.substring(0,20).trim());
                                        frm_lead_enquiry.xfg_sub_group_code = xlist_selected_text.substring(0, 20).trim();
                                        xlist_selected_text = xlist_selected_text.toString().trim();
                                        form_name = "frm_jobw_prod";
                                        ArrayList<team> xjob = finapi.getApi(fgen.mcd, "", "EP835S", fgen.cdt1, fgen.cdt2, fgen.muname, frm_lead_enquiry.xfg_sub_group_code.trim());
                                        btn_scan.setEnabled(false);
                                        txtselect_job.setEnabled(false);
                                        txtbatch_no.setText(xlist_selected_text.substring(4,10).trim());
                                        btn_scan.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                AlertDialog dialog2 = builder.create();
                                dialog2.show();
                                break;
                            case "R":
                                xrejection_measurment = "K";
                                dialog.dismiss();
                                break;
                            case "I" :
                                xinput_measurment = "K";
                                dialog.dismiss();
                                break;
                        }
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}


class AsyncCaller_jobw_prod extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread
    }
    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
    }
}


class AsyncCaller_jobw_prod_process extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread
    }
    @Override
    protected Void doInBackground(Void... params) {
        frm_lead_enquiry.xfrm_lead_enquiry = "Y";
        frm_lead_enquiry.xfg_sub_group_code = frm_jobw_prod.xxprocess_code;
        frm_jobw_prod.xreject = finapi.fill_record_in_listview_popup("EP833");
        frm_lead_enquiry.xfrm_lead_enquiry = "Y";
        frm_lead_enquiry.xfg_sub_group_code = frm_jobw_prod.xxprocess_code;
        frm_jobw_prod.xdown_time = finapi.fill_record_in_listview_popup("EP834");
        frm_lead_enquiry.xfrm_lead_enquiry = "N";
        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
    }



}
