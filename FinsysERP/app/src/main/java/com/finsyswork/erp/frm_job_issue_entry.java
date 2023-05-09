package com.finsyswork.erp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/*
    This Form Used Material Issue Slip Components
    Like : Card, Model, Adapter, Database.

 */
public class frm_job_issue_entry extends AppCompatActivity {
    public static String xreel_qty;
    Button btnscan_job, btnscan_reel, btn_save, btn_new, btn_cancel, btn_add, btn_read;
    public static EditText txtscan_job, txtscan_reel, txt_barcode, txtwip_stage, txtqty, txtreel_qty, txttotal_qty;
    RecyclerView recyclerView;
    sqliteHelperClass handler;
    Toolbar toolbar;
    public static String xscanned_job_no = "";
    public String xreel_for_return = "";
    public static issue_slip_adapter adapter;
    public static issue_req_model issue_req_model;
    public static ArrayList<issue_req_model> list = new ArrayList<>();
    public static ArrayList<String> xjob = new ArrayList<>();
    public static ArrayList<String> xwip_stage = new ArrayList<>();
    public static String btn_clicked = "", xicode = "", xreel_check_api = "";
    String xseperator = "#~#", xbranch = "00", xtype = "31";
    String xscan_job_first = "Y";
    RadioButton radio_scan_job, radio_scan_reel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_job_issue_entry);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_job_issue_entry.this;
        finapi.deleteJsonResponseFile();
        fgen.xextra1 = "job";
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_job_issue_entry.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        xjob = finapi.fill_record_in_listview_popup("EP735");
                        xwip_stage = finapi.fill_record_in_listview_popup("EP820");
                        progressDialog.dismiss();
                    }
                }, 100);
        initializeViews();
        frm_paper_end.xreel_check_api = "areel_check";
        if(fgen.frm_request.equals("frm_reel_issue_return"))
        {
            toolbar.setTitle("Reel Issue Return");
            frm_paper_end.xreel_check_api = "areel_retcheck";
            xtype = "11";
        }
        if(fgen.frm_request.equals("frm_job_issue_entry_for_reels") || fgen.frm_request.equals("frm_job_issue_entry_for_jobs"))
        {
            frm_paper_end.xreel_check_api = "areel_jccheck";
        }

        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        finapi.setColorOfStatusBar(frm_job_issue_entry.this);
        handler = new sqliteHelperClass(this);
        handler.clear_data();
        setTextOnViews();
        disableViewsMethod();
        setSupportActionBar(toolbar);

        handler.clear_data();
        list = handler.issue_req_get_data();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new issue_slip_adapter(frm_job_issue_entry.this, list);
        recyclerView.setAdapter(adapter);

        txtscan_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtscan_reel.getText().toString().isEmpty()) {Toast.makeText(getApplicationContext(), "Please Scan Reel First!!", Toast.LENGTH_LONG).show(); return;}
                ShowDialogBox(frm_job_issue_entry.this, xjob, "txtscan_job");
                txt_barcode.requestFocus();
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

        txtwip_stage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_job_issue_entry.this, xwip_stage, "txtwip_stage");
                txt_barcode.requestFocus();
            }
        });

        radio_scan_job.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(radio_scan_job.isChecked())
                {
                    xscan_job_first = "Y";
                }
                else{
                    xscan_job_first = "N";
                }
            }
        });

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBtnClickedMethod();
                btn_save.setEnabled(true);
                btn_new.setEnabled(false);
                btn_cancel.setText("CANCEL");


                btnscan_reel.setEnabled(true);
                btnscan_job.setEnabled(true);
//                radio_scan_job.setEnabled(true);
                radio_scan_reel.setEnabled(true);
                txtscan_job.setEnabled(true);
                txtqty.setEnabled(true);

                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);

                txtscan_reel.setBackgroundResource(R.color.light_blue);
                txt_barcode.requestFocus();
                xscan_job_first = "Y";
                txttotal_qty.setText("0");
            }
        });

        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_barcode.getText().toString().trim().length() > 2) {
                    if(xscan_job_first.equals("Y"))
                    {
                        scanned_job_verification(txt_barcode.getText().toString().trim());
                        txt_barcode.setText("");
                        txt_barcode.requestFocus();
                        radio_scan_reel.setChecked(true);
                    }
                    else {
                        xscanned_job_no = txtscan_job.getText().toString().split("---")[0].trim();
//                        if (xscanned_job_no.equals("")) {
//                            Toast.makeText(getApplicationContext(), "Please Scan Job Scard First!!", Toast.LENGTH_LONG).show();
//                            return;
//                        }

                        txtscan_reel.setText(txt_barcode.getText().toString().trim());
                        boolean xcheck_reel_status = multi_reel_single_job(txt_barcode.getText().toString().trim());
                        if (xcheck_reel_status) {
                            fillGrid();
                        }
                        txt_barcode.setText("");
                        txtscan_reel.setText("");
                    }
                }
            }
        });

        txt_barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == 19 || keyCode == 21)) {
                    if (txt_barcode.getText().toString().trim().length() > 2) {
                        if(xscan_job_first.equals("Y"))
                        {
                            scanned_job_verification(txt_barcode.getText().toString().trim());
                            txt_barcode.setText("");
                            txt_barcode.requestFocus();
                            radio_scan_reel.setChecked(true);
                        }
                        else {
//                            xscanned_job_no = txtscan_job.getText().toString().split("---")[0].trim();
                            xscanned_job_no = "";
//                            if(xscanned_job_no.equals(""))
//                            {
//                                Toast.makeText(getApplicationContext(), "Please Scan Job Scard First!!", Toast.LENGTH_LONG).show();
//                                return false;
//                            }
                            txtscan_reel.setText(txt_barcode.getText().toString().trim());
//                          scannerView.multi_reel_single_job(txtscan_reel.getText().toString());
                            boolean xcheck_reel_status = multi_reel_single_job(txt_barcode.getText().toString().trim());
                            if (xcheck_reel_status) {
                                fillGrid();
                            }
                            txt_barcode.setText("");
                            txtscan_reel.setText("");
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn_text = btn_cancel.getText().toString();
                if (btn_text == "CANCEL") {
                    newBtnClickedMethod();
                    disableViewsMethod();
                    btn_save.setEnabled(false);
                    btn_new.setEnabled(true);
                    btn_cancel.setText("EXIT");

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                    handler.clear_data();
                    list = handler.issue_req_get_data();
                    adapter = new issue_slip_adapter(frm_job_issue_entry.this, list);
                    recyclerView.setAdapter(adapter);

//                    radio_scan_job.setChecked(true);
                    txtscan_reel.setBackgroundResource(R.color.light_grey);
                }
                if (btn_text == "EXIT") {
                    finish();
                }
            }
        });

//        btnscan_job.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fgen.qr_scanned_with_external = "N";
//                frm_paper_end.btn_clicked = "job";
//                startActivity(new Intent(getApplicationContext(), scannerView.class));
//                txt_barcode.requestFocus();
//            }
//        });

        btnscan_reel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                xscanned_job_no = txtscan_job.getText().toString().split("---")[0].trim();
                xscanned_job_no = "";
//                if(xscanned_job_no.equals(""))
//                {
//                    Toast.makeText(getApplicationContext(), "Please Scan Job Scard First!!", Toast.LENGTH_LONG).show();
//                    return;
//                }
                frm_paper_end.btn_clicked = "reel";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
                txt_barcode.requestFocus();
                adapter = new issue_slip_adapter(frm_job_issue_entry.this, list);
                adapter.notifyDataSetChanged();
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillGrid();
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_job_issue_entry.this, "areel_issue_mj");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_job_issue_entry.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtwip_stage.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Fill WIP State", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_job_issue_entry.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String xmv_wip_stg = txtwip_stage.getText().toString().split("---")[0].trim();
                                String xicode = txtscan_reel.getText().toString().split("---")[0].trim();
                                String xmv_reels = txtscan_reel.getText().toString().split("---")[2].trim();


                                list = handler.issue_req_get_data();
                                if (txtscan_reel.getText().toString().isEmpty() || handler.issue_req_get_data().size() <= 0) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Invalid Data!!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                String post_param = "";
                                for (issue_req_model m : list) {
//                                    String xjob_no = m.getItem().toString().substring(4, 10).trim();
//                                    String xjob_dt = m.getItem().toString().substring(10, 20).trim();
                                    String xmv_qty = m.getQty().toString().trim();
                                    String xjob_details = m.getItem().toString().split("---")[0].trim()
                                            + xseperator + m.getItem().toString().split("---")[1].trim();
                                    post_param += xbranch + xseperator + xtype + xseperator + xicode.trim() + xseperator
                                            + xmv_reels.trim() + xseperator + xmv_qty.trim() + xseperator + xjob_details.trim()
                                            + xseperator + xmv_wip_stg.trim() + xseperator + frm_job_issue_entry.xreel_qty.trim() + "!~!~!";
                                }

                                ArrayList<team> result = finapi.getApi(fgen.mcd, "areel_issue_mj", post_param, fgen.muname, fgen.cdt1.substring(6, 10), fgen.btnid, "-");
                                boolean msg = finapi.showAlertMessageForStockCheck(frm_job_issue_entry.this, result);
                                if (!msg) {
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                handler.clear_data();
                                list = new ArrayList<>();
                                adapter = new issue_slip_adapter(frm_job_issue_entry.this, list);
                                recyclerView.setAdapter(adapter);

//                                radio_scan_job.setChecked(true);

                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    void fillGrid() {
        {
            String xreel = txtscan_reel.getText().toString().trim();
            String xjob_qty = txtqty.getText().toString().trim();
            String xjobcard = txtscan_job.getText().toString().trim();


            if (xreel.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Scan Job And Reel First!!", Toast.LENGTH_LONG).show();
                return;
            }

            frm_paper_end.issue_req_model = new issue_req_model("1", xjobcard, xjob_qty, frm_paper_end.xicode);
            boolean check_duplicacy = handler.CheckDuplicacy("col1", xjobcard);
            if(check_duplicacy)
            {
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_job_issue_entry.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Sorry, Duplicate Rows Are Not Allowed!!");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }
            boolean CheckItemQtyIsGreaterThanOrNot = handler.CheckJobQtyIsGreaterThanOrNot( "col2", ""+xjob_qty, ""+frm_job_issue_entry.xreel_qty);
            if(CheckItemQtyIsGreaterThanOrNot)
            {
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_job_issue_entry.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Sorry, Job Code \""+xjobcard.trim()+ "\" Qty Should Not Be Greater\n" +
                        " Than Reel Qty \""+frm_job_issue_entry.xreel_qty+"\" !!");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }

            if (fgen.frm_request.equals("frm_paper_end_for_jobs")) {
                if (frm_paper_end.issue_req_model != null) {
                    handler.issue_slip_insert_data(frm_paper_end.issue_req_model);
                    txtscan_job.setText("");
                    txtqty.setText("");
                }
            } else {
                if (frm_paper_end.issue_req_model != null) {
                    handler.issue_slip_insert_data(frm_paper_end.issue_req_model);
                    txtscan_reel.setText("");
                }
            }

            double total_qty = handler.TotalQtySum("col2");
            txttotal_qty.setText(""+total_qty);

            list = handler.issue_req_get_data();
            adapter = new issue_slip_adapter(frm_job_issue_entry.this, list);
            recyclerView.setAdapter(adapter);

            txt_barcode.requestFocus();
        }
    }

    public boolean multi_reel_single_job(String rawResult) {
        String xbuild_qr_name = "";
        ArrayList<team6> result = finapi.getApi6(fgen.mcd, ""+frm_paper_end.xreel_check_api,""+rawResult.trim(), fgen.muname, fgen.cdt1.substring(6,10), "#", ""+xscanned_job_no);
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
        String data= "";
        try {
            JSONArray array = new JSONArray(listString);
            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                data = respObj.getString("col1");
                try {
                    frm_job_issue_entry.xicode = respObj.getString("col2");
                    frm_job_issue_entry.xreel_qty = respObj.getString("col6");
                    xreel_for_return = respObj.getString("col6");
                    String xcol4 =  respObj.getString("col4");
                    xbuild_qr_name = frm_job_issue_entry.xicode +"---"+ xcol4+"---"+rawResult;
                }catch (Exception e){}
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!data.equals("Success") || data.equals(""))
        {
            new android.app.AlertDialog.Builder(this)
                    .setTitle(Html.fromHtml("<font color='#F80303'>Error</font>"))
                    .setMessage(Html.fromHtml("<font color='#F80303'>" +frm_job_issue_entry.xreel_qty +"\"!!\"</font>"))
                    .setIcon(R.drawable.fail)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return false;
        }
        else {
            try {
                frm_job_issue_entry.txtscan_reel.setText("" + xbuild_qr_name);
            } catch (Exception e) {
            }
            try {
                frm_job_issue_entry.txtscan_reel.setText("" + xbuild_qr_name);
            } catch (Exception e) {
            }
        }

        boolean duplicate = handler.CheckDuplicacy("col3", ""+rawResult.trim());
        if(duplicate)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Sorry, Duplicate Rows Not Allowed!!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return false;
        }
        if(fgen.frm_request.equals("frm_reel_issue_return"))
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("REEL Qty");
            alertDialog.setMessage("Enter Qty");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            String finalXbuild_qr_name = xbuild_qr_name;
            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String qty = input.getText().toString();
                            if (qty.equals("")) {
                                xreel_for_return = "0";
                            } else {
                                xreel_for_return = qty;
                            }
                            frm_job_issue_entry.txtscan_reel.setText(finalXbuild_qr_name);
                            frm_job_issue_entry.issue_req_model = new issue_req_model("1", ""+ finalXbuild_qr_name, xreel_for_return, ""+rawResult.trim());
                            fillGrid();
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return  false;
        }else{
            frm_job_issue_entry.issue_req_model = new issue_req_model("1", ""+xbuild_qr_name, frm_job_issue_entry.xreel_qty, ""+rawResult.trim());
        }
        return  true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgen.frm_request = "";
        fgen.xextra1="";
    }


    private void newBtnClickedMethod() {
        txtscan_job.setText("");
        txtscan_reel.setText("");
        txtqty.setText("");
        txtreel_qty.setText("");
        txttotal_qty.setText("");
    }

    private void disableViewsMethod() {
        btn_save.setEnabled(false);
        btnscan_reel.setEnabled(false);
        btnscan_job.setEnabled(false);
        txtscan_job.setEnabled(false);
        radio_scan_reel.setEnabled(false);
        radio_scan_job.setEnabled(false);
        txtqty.setEnabled(false);
    }

    private void setTextOnViews() {
        btn_cancel.setText("EXIT");
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        btn_add = findViewById(R.id.btn_add);
        btn_read = findViewById(R.id.btn_read);
        btn_save = findViewById(R.id.btn_save);
        btn_new = findViewById(R.id.btn_new);
        btn_cancel = findViewById(R.id.btn_cancel);
        btnscan_job = findViewById(R.id.btn_scanjob);
        btnscan_reel = findViewById(R.id.btn_scanreel);
        txtscan_job = findViewById(R.id.txt_job);
        txtscan_reel = findViewById(R.id.txt_reel);
        recyclerView = findViewById(R.id.recycler);
        txt_barcode = findViewById(R.id.txt_barcode);
        txtwip_stage = findViewById(R.id.txtwip_stage);
        radio_scan_job = findViewById(R.id.radio_scan_job);
        radio_scan_reel = findViewById(R.id.radio_scan_reel);
        txtqty = findViewById(R.id.txtqty);
        txtreel_qty = findViewById(R.id.txtreel_qty);
        txttotal_qty = findViewById(R.id.txttotal_qty);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            list = handler.issue_req_get_data();
        } catch (Exception e) {
        }
        adapter = new issue_slip_adapter(frm_job_issue_entry.this, list);
        recyclerView.setAdapter(adapter);
    }


    public void ShowDialogBox(Context context, ArrayList<String> list, String controll) {
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
        if (controll.equals("txtscan_job")) txtapi_code.setText("EP735");
        if(controll.equals("txtwip_stage")) txtapi_code.setText("EP820");

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
                switch (controll) {
                    case "txtscan_job":
                        txtscan_job.setText(list_selected_text);
                        txtqty.setText(list_selected_text.split("---")[2].trim());
                        break;
                    case "txtwip_stage" : txtwip_stage.setText(list_selected_text);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });

    }


    public void scanned_job_verification(String rawResult) {
        ArrayList<team6> result = finapi.getApi6(fgen.mcd, "ajobc_check",""+rawResult.trim(), fgen.muname, fgen.cdt1.substring(6,10), "#", "#");
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
        String data= "";
        try {
            JSONArray array = new JSONArray(listString);
            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                data = respObj.getString("col1");
                try {
                }catch (Exception e){}
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!data.equals("Success"))
        {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Sorry, JOB Card Is Invalid!!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            return;
                        }
                    })
                    .show();
            return;
        }
        else {
            try {
                frm_job_issue_entry.txtscan_job.setText("" + rawResult.trim());
            } catch (Exception e) {
            }
            try {
                frm_reel_issue_return.txtscan_job.setText("" + rawResult.trim());
            } catch (Exception e) {
            }
            try {
                frm_job_issue_entry.txtscan_job.setText("" + rawResult.trim());
            } catch (Exception e) {
            }
        }

        showAlert_for_job_or_reel("" + rawResult, "JOB");
    }


    private void showAlert_for_job_or_reel(String s, String name) {
        final boolean[] move_or_not = {false};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(name + " Qty");
        alertDialog.setMessage("Enter Qty");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String qty = input.getText().toString();
                        if (qty.equals("")) {
                            Toast.makeText(getApplicationContext(), "NULL Value Could Not Allowed!!", Toast.LENGTH_LONG).show();
                            move_or_not[0] = true;
                            Toast.makeText(getApplicationContext(), "Invalid Qty!!", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            float enter_qty = Float.parseFloat(qty);
                            frm_paper_end.issue_req_model = new issue_req_model("1", s, qty, frm_paper_end.xicode);
                            move_or_not[0] = true;
                            dialog.dismiss();
                            onBackPressed();
                        }
                    }
                });
        alertDialog.show();
    }
}