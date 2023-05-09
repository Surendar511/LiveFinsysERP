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
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
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

public class frm_issue_slip extends AppCompatActivity {

    Button btn_save,btn_new, btn_cancel, btn_scan;
    public static EditText txtreq_no, txttype, txt_barcode, txtwip_stage;
    RecyclerView recyclerView, recycler2;
    Toolbar toolbar;
    ArrayList<String> xreq_no = new ArrayList<>();
    sqliteHelperClass handler;
    public static ArrayList<issue_req_model> list = new ArrayList<>();
    public static ArrayList<scanner_model> modellList = new ArrayList<>();
    ArrayList<issue_slip_selected_req_model> obj = new ArrayList<>();
    issue_slip_adapter adapter;
    String xacode = "", xwip_code = "", yreq_dt="";
    public String xmbranch="00", xmv_type="30", xmv_dep_acd, xmv_dep_stg, xmv_dep_icd, xmv_dep_req, xmv_dep_req_no
            , xmv_dep_req_dt, xseperator = "#~#", xmv_batch_no="";
    issue_slip_selected_req_adapter myAdapter;
    public static  String qty = "";
    String frm_request = "";
    String xstore_selection = "OK";
    ArrayList<String> xlist = new ArrayList<>();
    public static ArrayList<String> xlot_enable = new ArrayList<>();
    public static String reel_wise_lot="N";
    RadioButton radio_yes, radio_no, radio_ok, radio_rej;
    LinearLayout linear_store_return;
    public static String requested_qty = "";

    public static ArrayList<String> OriginalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_issue_slip);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_issue_slip.this;
        finapi.deleteJsonResponseFile();
        frm_request = fgen.frm_request;

        handler = new sqliteHelperClass(this);
        handler.clear_data();
        initializeViews();
        finapi.setColorOfStatusBar(frm_issue_slip.this);

        fgen.frm_request = "issue_slip";
        frm_lead_enquiry.xfrm_lead_enquiry = "N";
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_issue_slip.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        check_form_request();
                        if(fgen.mcd.equals("SVPL") || fgen.mcd.equals("ARUB") || fgen.mcd.equals("R001") || fgen.mcd.equals("OMEG")){
                            frm_issue_slip.reel_wise_lot = "1";
                        }
                        else if(fgen.mcd.equals("SUNB")){
                            frm_issue_slip.reel_wise_lot = "N";
                        }
                        else {
                            frm_issue_slip.xlot_enable = finapi.fill_record_in_listview_popup("EP720");
                            for (String x : frm_issue_slip.xlot_enable) {
                                frm_issue_slip.reel_wise_lot = x.split("---")[1].trim();
                            }
                        }
                        progressDialog.dismiss();
                    }
                }, 100);

        setTextOnViews();
        disableViewsMethod();
        setSupportActionBar(toolbar);
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        Highlightborder();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new issue_slip_adapter(getApplicationContext(), list);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        myAdapter = new issue_slip_selected_req_adapter(getApplicationContext(), obj);
        recycler2.setLayoutManager(layoutManager2);
        recyclerView.setAdapter(myAdapter);

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_issue_slip.this, "Reel Wise Lot : areel_issue\n else : aIssEntry_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_issue_slip.this);
                return true;
            }
        });

//        radio_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(radio_yes.isChecked()) frm_issue_slip.reel_wise_lot = "Y";
//                else frm_issue_slip.reel_wise_lot = "N";
//            }
//        });

        radio_ok.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(radio_ok.isChecked()) xstore_selection = "OK";
                else xstore_selection = "REJ";
            }
        });

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBtnClickedMethod();
                enabledViewMethod();
                btn_save.setEnabled(true);
                btn_new.setEnabled(false);
                btn_cancel.setText("CANCEL");

                txt_barcode.requestFocus();
                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);
                txttype.setBackgroundResource(R.color.light_blue);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn_text = btn_cancel.getText().toString();
                if(btn_text == "CANCEL")
                {
                    final MyProgressdialog progressDialog = new MyProgressdialog(frm_issue_slip.this);
                    progressDialog.show();
                    new Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    txtreq_no.setBackgroundResource(R.color.light_grey);
                                    newBtnClickedMethod();
                                    disableViewsMethod();
                                    btn_save.setEnabled(false);
                                    btn_new.setEnabled(true);
                                    btn_cancel.setText("EXIT");

                                    txttype.setBackgroundResource(R.color.light_grey);
                                    handler.clear_data();
                                    list = handler.issue_req_get_data();
                                    adapter = new issue_slip_adapter(getApplicationContext(), list);
                                    recyclerView.setAdapter(adapter);
                                    obj.clear();
                                    obj = new ArrayList<>();
                                    myAdapter = new issue_slip_selected_req_adapter(getApplicationContext(), obj);
                                    recycler2.setAdapter(myAdapter);


                                    if(frm_request.equals("material_return_entry"))
                                    {
                                        xreq_no = finapi.issue_slip_fill_record_in_listview_popup("EP818R");
                                    }
                                    else{
                                        xreq_no = finapi.issue_slip_fill_record_in_listview_popup("EP818");
                                    }
                                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    progressDialog.dismiss();
                                }
                            }, 100);
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });


        txt_barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == 19 || keyCode == 21)) {
                    if(txtreq_no.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Enter Req No.!!", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if (txt_barcode.getText().toString().trim().length() > 2) {
                        fill_grid(txt_barcode.getText().toString().trim());
                        txt_barcode.setText("");
                        txt_barcode.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });


        txttype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_issue_slip.this, frm_material_issue_req.xtype, "txttype");
                txt_barcode.requestFocus();
            }
        });

        txtreq_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_issue_slip.this, xreq_no, "txtreq_no");
                txt_barcode.requestFocus();
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtreq_no.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Req No.!!", Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(getApplicationContext(), scannerView.class));
                txt_barcode.requestFocus();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txttype.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Select Type!!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(txtreq_no.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Select Req_no!!", Toast.LENGTH_LONG).show();
                    return;
                }

                list = handler.issue_req_get_data();
                if(list.size() <= 0)
                {
                    Toast.makeText(getApplicationContext(), "Invalid Record!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_issue_slip.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param ="";
                                for(issue_req_model item : list) {
                                    String litem = "";
                                    if(item.getItem().split("---")[0].trim().toString().length() < 10)
                                    {
                                        litem = item.getItem().toString();
                                    }
                                    else {
                                        litem = item.getItem().substring(18, 26);
                                    }
                                    String[] lreq_no = txtreq_no.getText().toString().split("---");
                                    xmv_dep_acd = xacode;
                                    xmv_dep_stg = xwip_code;
                                    xmv_dep_icd = litem;
                                    xmv_dep_req = item.getQty();
                                    xmv_dep_req_no = lreq_no[0];
                                    xmv_dep_req_dt = yreq_dt;
                                    xmv_batch_no = item.getItem().split("---")[2].trim();

                                    if(reel_wise_lot.equals("1"))
                                    {
                                        xmv_dep_icd = litem.split("---")[0].trim();
                                    }

                                    if(reel_wise_lot.equals("1"))
                                    {
                                        post_param += xmbranch + xseperator + xmv_type + xseperator + xmv_dep_icd
                                                + xseperator + xmv_batch_no + xseperator + xmv_dep_req + xseperator + xmv_dep_req_no + xseperator
                                                + xmv_dep_req_dt + xseperator + xmv_dep_stg + "!~!~!";
                                    }
                                    else {
                                        post_param += xmbranch + xseperator + xmv_type + xseperator + xmv_dep_acd
                                                + xseperator + xmv_dep_stg + xseperator + xmv_dep_icd.split("---")[0] + xseperator
                                                + xmv_dep_req + xseperator + xmv_dep_req_no + xseperator
                                                + xmv_dep_req_dt + xseperator + xmv_batch_no + "!~!~!";
                                    }
                                }
                                ArrayList<team> result = new ArrayList<>();
                                if(reel_wise_lot.equals("1"))
                                {
                                    result = finapi.getApi(fgen.mcd, "areel_issue", post_param, fgen.muname, fgen.cdt1.substring(6, 10), "-", "-");
                                }
                                else {
                                    result = finapi.getApi(fgen.mcd, "aIssEntry_ins", post_param, fgen.muname, fgen.cdt1.substring(6, 10), "-", "-");
                                }
//                                boolean msg = finapi.showAlertMessage(frm_issue_slip.this, result);
                                boolean msg = finapi.showAlertMessageForStockCheck(frm_issue_slip.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                xreq_no = finapi.issue_slip_fill_record_in_listview_popup("EP818");
                                if(frm_request.equals("material_return_entry"))
                                {
                                    xreq_no = finapi.issue_slip_fill_record_in_listview_popup("EP818R");
                                }
                                String xold_type = txttype.getText().toString();
                                newBtnClickedMethod();
                                txttype.setText(xold_type);
                                handler.clear_data();
                                list = handler.issue_req_get_data();
                                adapter = new issue_slip_adapter(getApplicationContext(), list);
                                recyclerView.setAdapter(adapter);

                                obj.clear();
                                obj = new ArrayList<>();
                                myAdapter = new issue_slip_selected_req_adapter(getApplicationContext(), obj);
                                recycler2.setAdapter(myAdapter);

                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                                txt_barcode.requestFocus();
                            }
                        }, 100);
            }
        });
    }

    public void fill_grid(String rawResult)
    {
        String xicode = ""+rawResult;
        if(fgen.mcd.equals("SVPL")){try{xicode = xicode.substring(8,xicode.length()).toString();} catch (Exception  e){ xicode = ""+rawResult;}} //xicode = xicode.substring(8,18).toString();
        boolean duplicate = false;
        String xtype = frm_issue_slip.txttype.getText().toString();
        ArrayList<team6> x = finapi.getApi6(fgen.mcd, "aNJreel_jccheck",xicode, fgen.muname, fgen.cdt1.substring(6,10), "#", xtype.split("---")[0].trim());
        boolean msg = finapi.RecieveDataFromApi(frm_issue_slip.this, x);
        if(!msg){return;}
        xicode = fgen.xpopup_col2 +"----"+ fgen.xpopup_col4 + "---" + fgen.xpopup_col3;
        duplicate = handler.CheckDuplicacy("col1", "" + xicode);
        if(duplicate)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
        frm_issue_slip.requested_qty = "";
        frm_issue_slip.requested_qty = handler.issue_slip_CheckItemQty("col3", xicode.split("---")[0].trim());
        String qty = fgen.xpopup_col6.trim();
        showAlert("" + xicode, qty);
    }

    private boolean showAlert(String item,String max_qty){
        if(frm_issue_slip.requested_qty.equals(""))
        {
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Error");
            if(fgen.mcd.equals("SGRP")){alert.setMessage(fgen.xpopup_col6);}
            else{
                alert.setMessage("Sorry, Item Not Found Against This Issue Req.!!");
            }
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alert.show();
        }
        else if(fgen.xpopup_col6.toUpperCase().contains("PLEASE FOLLOW FIFO")){
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle(fgen.xpopup_col2);
            alert.setMessage(fgen.xpopup_col6);
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alert.show();
        }
        else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Message");
            alertDialog.setMessage("Requested Qty : "+frm_issue_slip.requested_qty+"\nBatch Qty     : " + max_qty);

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            input.setText(max_qty); // set input text qty
            input.setEnabled(true);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            qty = input.getText().toString();
                            if (qty.equals("")) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "NULL Value Could Not Allowed!!", Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), "Invalid Qty!!", Toast.LENGTH_SHORT).show();
                            } else {
                                float max = Float.parseFloat(max_qty);
                                float enter_qty = Float.parseFloat(qty);
                                if (enter_qty > max && !frm_issue_slip.reel_wise_lot.equals("3")) {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Qty Should Not Be Greater Than Batch Qty!!", Toast.LENGTH_LONG).show();
                                } else {
                                    issue_req_model issue_req_model = new issue_req_model("1", item, qty);
                                    handler.issue_slip_insert_data(issue_req_model);
                                    list = handler.issue_req_get_data();
                                    adapter = new issue_slip_adapter(getApplicationContext(), list);
                                    recyclerView.setAdapter(adapter);
                                    dialog.dismiss();
                                }
                            }
                        }
                    });
            alertDialog.show();
        }
        return true;
    }

    private void check_form_request() {
        switch (frm_request)
        {
            case "material_return_entry":
                xmv_type="11";
                frm_material_issue_req.xtype = finapi.fill_record_in_listview_popup("EP816C");
                toolbar.setTitle("Material Return Entry");
                break;
            default:
                frm_material_issue_req.xtype = finapi.fill_record_in_listview_popup("EP816B");
                break;
        }
    }

    private void enabledViewMethod() {
        txtreq_no.setEnabled(true);
        txttype.setEnabled(true);
    }
    private void newBtnClickedMethod() {
        txtreq_no.setText("");
        txttype.setText("");
        txtwip_stage.setText("");
    }
    private void disableViewsMethod() {
        txtreq_no.setEnabled(false);
        txttype.setEnabled(false);
    }
    private void Highlightborder() {
        txtreq_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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


    private void initializeViews() {
        btn_save = findViewById(R.id.btn_save);
        btn_new = findViewById(R.id.btn_new);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_scan = findViewById(R.id.btn_scan);
        recyclerView = findViewById(R.id.recycler);
        recycler2 = findViewById(R.id.recycler2);
        txtreq_no = findViewById(R.id.txtreq_no);
        toolbar = findViewById(R.id.toolbar);
        txttype = findViewById(R.id.txttype);
        txt_barcode = findViewById(R.id.txt_barcode);
        txtwip_stage = findViewById(R.id.txtwip_stage);
        radio_yes = findViewById(R.id.radio_yes);
        radio_no = findViewById(R.id.radio_no);
        linear_store_return = findViewById(R.id.linear_store_return);
        radio_ok = findViewById(R.id.radio_ok);
        radio_rej = findViewById(R.id.radio_rej);
    }

    private void setTextOnViews() {
        btn_cancel.setText("EXIT");
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
        if(controll.equals("txtreq_no")) {
            if(frm_request.equals("material_return_entry")) {
                txtapi_code.setText("EP818R -----> EP818S");
            } else {
                if((fgen.mcd.equals("VIGP") || fgen.mcd.equals("CHPL") || fgen.mcd.equals("GDOT") || fgen.mcd.equals("GCAP")) && txttype.getText().toString().split("---")[0].trim().equals("30"))
                {
                    txtapi_code.setText("EP818  -----> EP818M2");
                }
                else {
                    txtapi_code.setText("EP818 -----> EP818A");
                }
            }
        }
        if(controll.equals("txttype")) {
            if(frm_request.equals("material_return_entry")) {
                txtapi_code.setText("EP816C -----> EP818R");
            }
            else {
                if((fgen.mcd.equals("VIGP") || fgen.mcd.equals("CHPL") || fgen.mcd.equals("GDOT")|| fgen.mcd.equals("GCAP")) && txttype.getText().toString().split("---")[0].trim().equals("30"))
                {
                    txtapi_code.setText("EP816B -----> EP818M1");
                }
                else {
                    txtapi_code.setText("EP816B -----> EP818");
                }
            }
        }

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
                adapter.getFilter().filter(s.toString());
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
                    case "txtreq_no" : txtreq_no.setText(list_selected_text);
                        final MyProgressdialog progressDialog = new MyProgressdialog(frm_issue_slip.this);
                        progressDialog.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        if(frm_request.equals("material_return_entry"))
                                        {
                                            insert_selected_req_no_in_db("EP818S");
                                        }
                                        else
                                        {
                                            if((fgen.mcd.equals("VIGP") || fgen.mcd.equals("CHPL") || fgen.mcd.equals("GDOT")|| fgen.mcd.equals("GCAP")) && txttype.getText().toString().split("---")[0].trim().equals("30")) {
                                                insert_selected_req_no_in_db("EP818M2");
                                            }
                                            else{
                                                insert_selected_req_no_in_db("EP818A");
                                            }
                                        }
                                        progressDialog.dismiss();
                                    }
                                }, 100);
                        break;
                    case "txttype":
                        final MyProgressdialog progressDialog1 = new MyProgressdialog(frm_issue_slip.this);
                        progressDialog1.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        handler.clear_data();
                                        ArrayList<issue_req_model> listt = handler.issue_req_get_data();
                                        issue_slip_adapter adapter2 = new issue_slip_adapter(getApplicationContext(), listt);
                                        recyclerView.setAdapter(adapter2);
                                        obj.clear();
                                        obj = new ArrayList<>();
                                        myAdapter = new issue_slip_selected_req_adapter(getApplicationContext(), obj);
                                        recycler2.setAdapter(myAdapter);
                                        txtreq_no.setText("");

                                        fgen.xextra1_for_popup = list_selected_text.split("---")[0].trim();
                                        txttype.setText(list_selected_text);
                                        xmv_type = fgen.xextra1_for_popup;
                                        if(frm_request.equals("material_return_entry"))
                                        {
                                            xreq_no = finapi.issue_slip_fill_record_in_listview_popup("EP818R");
                                        }
                                        else {
                                            if((fgen.mcd.equals("VIGP") || fgen.mcd.equals("CHPL") || fgen.mcd.equals("GDOT")|| fgen.mcd.equals("GCAP")) && txttype.getText().toString().split("---")[0].trim().equals("30")) {
                                                xreq_no = finapi.issue_slip_fill_record_in_listview_popup("EP818M1");
                                            }
                                            else
                                            {
                                                xreq_no = finapi.issue_slip_fill_record_in_listview_popup("EP818");
                                            }
                                        }

                                        progressDialog1.dismiss();
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


    public ArrayList<String> insert_selected_req_no_in_db(String form_code){
        obj.clear();
        obj = new ArrayList<>();
        myAdapter = new issue_slip_selected_req_adapter(this, obj);
        recycler2.setAdapter(myAdapter);

        ArrayList<String> list_view = new ArrayList<>();
        ArrayList<team> result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins",""+form_code, fgen.cdt1, fgen.cdt2, fgen.muname, txtreq_no.getText().toString().split("---")[3].trim());

        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        String xreq_no = "";
        String xreq_dt = "";
        String xiname = "";
        String xicode = "";
        String xdeptt = "";
        String xqty = "";
        String xselected_req_no = txtreq_no.getText().toString();
        String xstring_req_no[] = xselected_req_no.split("---");
        xselected_req_no = xstring_req_no[0];
        String xFound = "Y";
        try {
            JSONArray array = new JSONArray(listString);
            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                String data = respObj.getString("col2");
                Log.d("allData  ", data);
                String[] arrOfStr = data.split("!~!~!");
                int x=0;
                for (String col2 : arrOfStr){
                    Log.d("allData  ", col2);
                    if(x==0) {
                        xFound = "Y";
                        if(!col2.trim().equals(xselected_req_no.trim())){
                            xFound = "N";
                            break;
                        }
                        xreq_no = col2;
                    }
                    switch (x){
                        case 1 :
                            yreq_dt = col2;
                            xreq_dt = col2;
                            break;
//                        case 2 :
//                            break;
                        case 3 :
                            xiname = col2;
                            break;
                        case 4 :
                            xqty = col2;
                            break;
                        case 5 :
                            xdeptt = col2;
                            xacode = col2;
                            break;
                        case 6 :
                            xwip_code = col2;
                            break;
                        case 7 :
                            xicode = col2;
                            break;
                        default:
                            break;
                    }
//                    if(x==1) {
//                        yreq_dt = col2;
//                        xreq_dt = col2;
//                    }
//                    if(x==3) {
//                        xiname = col2;
//                    }
//                    if(x==4) {
//                        xqty = col2;
//                    }
//                    if(x==5) {
//                        xdeptt = col2;
//                        xacode = col2;
//                    }
//                    if(x==6) {
//                        xwip_code = col2;
//                    }
//                    if(x==7) {
//                        xicode = col2;
//                    }
                    x +=1;
                }
                if(xFound.equals("Y")) {
                    txtwip_stage.setText(xwip_code.trim());
                    handler.issue_slip_insert_data(xreq_no, xreq_dt, xicode, xdeptt, xqty);
                    obj.add(new issue_slip_selected_req_model(xiname, xqty));
                }
                Log.d("addData ", "Success");
            }
            myAdapter = new issue_slip_selected_req_adapter(this, obj);
            qty = handler.issue_slip_CheckItemQty("col3", "30030031");
            recycler2.setAdapter(myAdapter);
            qty = handler.issue_slip_CheckItemQty("col3", "30030031");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list_view;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            list = handler.issue_req_get_data();
        }catch (Exception e){}
        adapter = new issue_slip_adapter(getApplicationContext(), list);
        Log.d("size : ", ""+modellList.size());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgen.frm_request = "";
    }
}