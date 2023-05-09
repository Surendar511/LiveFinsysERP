package com.finsyswork.erp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
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

import androidx.annotation.RequiresApi;
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

public class frm_dispatch_finalise_mvin extends AppCompatActivity {

    Button btn_save,btn_new, btn_cancel, btn_scan;
    public static TextView txttotal_rows, txttotal_scanned_rows;
    public static EditText txtreq_no, txttype, txt_barcode;
    RecyclerView recyclerView, recycler2;
    Toolbar toolbar;
    ArrayList<String> xreq_no = new ArrayList<>();
    sqliteHelperClass handler;
    public static ArrayList<issue_req_model> list = new ArrayList<>();
    public static ArrayList<scanner_model> modellList = new ArrayList<>();
    ArrayList<issue_slip_selected_req_model> obj = new ArrayList<>();
    issue_slip_adapter adapter;
    String xacode = "", yreq_dt="";
    public String xmbranch="00", xmv_type="40", xmv_dep_acd, xmv_dep_stg, xmv_dep_icd, xmv_dep_req, xmv_dep_req_no
            , xmv_dep_req_dt, xseperator = "#~#", xmv_batch_no="";
    issue_slip_selected_req_adapter myAdapter;
    public static  String qty = "";
    String frm_request = "";
    String xstore_selection = "OK";
    ArrayList<String> xlist = new ArrayList<>();
    public static ArrayList<String> xtype = new ArrayList<>();
    public static String reel_wise_lot="N";
    RadioButton radio_yes, radio_no, radio_ok, radio_rej;
    LinearLayout linear_store_return;
    public static String requested_qty = "";
    public static int rows=0, scanned_rows=0;

    public static ArrayList<String> OriginalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_dispatch_finalise_mvin);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        frm_lead_enquiry.xfrm_lead_enquiry = "N";
        finapi.context = frm_dispatch_finalise_mvin.this;
        finapi.deleteJsonResponseFile();

        frm_request = "frm_dispatch_finalise_mvin";

        handler = new sqliteHelperClass(this);
        handler.clear_data();
        initializeViews();
        finapi.setColorOfStatusBar(frm_dispatch_finalise_mvin.this);

        fgen.frm_request = "frm_dispatch_finalise_mvin";

        final MyProgressdialog progressDialog = new MyProgressdialog(frm_dispatch_finalise_mvin.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        check_form_request();
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
                    fgen.showApiName(frm_dispatch_finalise_mvin.this, "Reel Wise Lot : areel_issue\n else : aIssEntry_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_dispatch_finalise_mvin.this);
                return true;
            }
        });

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

                txttotal_rows.setText("Total Rows : "+rows);
                txttotal_scanned_rows.setText("Scanned Rows : "+scanned_rows);
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
                    final MyProgressdialog progressDialog = new MyProgressdialog(frm_dispatch_finalise_mvin.this);
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

                                    txttotal_rows.setText("Total Rows : "+rows);
                                    txttotal_scanned_rows.setText("Scanned Rows : "+scanned_rows);
                                    txttype.setBackgroundResource(R.color.light_grey);
                                    handler.clear_data();
                                    list = handler.issue_req_get_data();
                                    adapter = new issue_slip_adapter(getApplicationContext(), list);
                                    recyclerView.setAdapter(adapter);
                                    obj.clear();
                                    obj = new ArrayList<>();
                                    myAdapter = new issue_slip_selected_req_adapter(getApplicationContext(), obj);
                                    recycler2.setAdapter(myAdapter);
                                    xreq_no = finapi.issue_slip_fill_record_in_listview_popup("MVIN700");
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
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_dispatch_finalise_mvin.this, frm_dispatch_finalise_mvin.xtype, "txttype");
                txt_barcode.requestFocus();
            }
        });

        txtreq_no.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_dispatch_finalise_mvin.this, xreq_no, "txtreq_no");
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
                    Toast.makeText(getApplicationContext(), "Invalid Records!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_dispatch_finalise_mvin.this);
                progressDialog.show();
                Handler handler_thread = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler_thread.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                try {
                                    String post_param = "";
                                    for (issue_req_model item : list) {
                                        String litem = "", roll_no = "";
                                        litem = item.getItem().toString().split("---")[0].toString().trim();
                                        roll_no = item.getItem().toString().split("---")[2].toString().trim();
                                        String packno = txtreq_no.getText().toString().split("---")[0].trim();
                                        String packdate = txtreq_no.getText().toString().split("---")[1].trim();
                                        String acode = txtreq_no.getText().toString().split("---")[2].trim();

                                        post_param += xmbranch + xseperator + xmv_type + xseperator + acode
                                                + xseperator + litem + xseperator + roll_no + xseperator + packno + xseperator
                                                + packdate + "!~!~!";
                                    }
                                    ArrayList<team> result = new ArrayList<>();
                                    result = finapi.getApi(fgen.mcd, "MVIN_Dispatch_Note", post_param, fgen.muname, fgen.cdt1.substring(6, 10), "-", "-");
//                                boolean msg = finapi.showAlertMessage(frm_dispatch_finalise_mvin.this, result);
                                    boolean msg = finapi.showAlertMessageForStockCheck(frm_dispatch_finalise_mvin.this, result);
                                    if (!msg) {
                                        progressDialog.dismiss();
                                        return;
                                    }
                                    String xold_type = txttype.getText().toString();
                                    xreq_no = finapi.issue_slip_fill_record_in_listview_popup("MVIN700");
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

                                } catch (Exception e) {
                                    new android.app.AlertDialog.Builder(frm_dispatch_finalise_mvin.this)
                                            .setTitle("Error Found!!")
                                            .setMessage("" + e)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                    return;
                                }

                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        });
                    }
                };
                new Thread(runnable).start();
            }
        });
    }

    public void fill_grid(String rawResult)
    {
        try {
            ArrayList<team6> x = finapi.getApi6(fgen.mcd, "areel_FGRcheck", "" + rawResult, fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
            Gson gson = new Gson();
            String listString = gson.toJson(x, new TypeToken<ArrayList<team>>() {
            }.getType());
            String data = "";
            try {
                JSONArray array = new JSONArray(listString);
                for (int i = 0; i < 1; i++) {
                    JSONObject respObj = array.getJSONObject(i);
                    data = respObj.getString("col1");
                    fgen.xpopup_col2 = respObj.getString("col2"); // icode
                    fgen.xpopup_col3 = respObj.getString("col3"); // roll no
                    fgen.xpopup_col4 = respObj.getString("col4"); // iname
                    fgen.xpopup_col6 = respObj.getString("col6");  //qty
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!data.equals("Success")) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Sorry, Scanned BarCode Is Invalid!!")
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
            String xicode = fgen.xpopup_col2 + "----" + fgen.xpopup_col4 + "---" + fgen.xpopup_col3;
            boolean duplicate = handler.CheckDuplicacy("col1", "" + xicode);
            if (duplicate) {
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
            String makeIcode = xicode.split("---")[0].trim() + xicode.split("---")[2].trim();
            frm_dispatch_finalise_mvin.requested_qty = "";
            frm_dispatch_finalise_mvin.requested_qty = handler.issue_slip_CheckItemQty("col3", makeIcode);
            qty = fgen.xpopup_col6.trim();

            if (frm_dispatch_finalise_mvin.requested_qty.equals("")) {
                AlertDialog alert = new AlertDialog.Builder(this).create();
                alert.setTitle("Error");
                alert.setMessage("Sorry, Item Not Found In This Dispatch Note!!");
                alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                        });
                alert.show();
            } else {
                ++frm_dispatch_finalise_mvin.scanned_rows;
                issue_req_model issue_req_model = new issue_req_model("1", xicode, qty);
                handler.issue_slip_insert_data(issue_req_model);
                list = handler.issue_req_get_data();
                adapter = new issue_slip_adapter(getApplicationContext(), list);
                recyclerView.setAdapter(adapter);
                txttotal_scanned_rows.setText("Scanned Rows : " + scanned_rows);
            }
        }catch (Exception e){
            new android.app.AlertDialog.Builder(frm_dispatch_finalise_mvin.this)
                    .setTitle("Error Found!!")
                    .setMessage("" + e)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    private void check_form_request() {
        switch (frm_request)
        {
            case "frm_dispatch_finalise_mvin":
                frm_lead_enquiry.xfrm_lead_enquiry = "N";
                frm_dispatch_finalise_mvin.xtype = finapi.fill_record_in_listview_popup("EP816A");
                break;
        }
    }

    private void enabledViewMethod() {
        txtreq_no.setEnabled(true);
        txttype.setEnabled(true);
    }
    private void newBtnClickedMethod() {
        rows = 0; scanned_rows=0;
        txtreq_no.setText("");
        txttype.setText("");
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
        radio_yes = findViewById(R.id.radio_yes);
        radio_no = findViewById(R.id.radio_no);
        linear_store_return = findViewById(R.id.linear_store_return);
        radio_ok = findViewById(R.id.radio_ok);
        radio_rej = findViewById(R.id.radio_rej);
        txttotal_rows = findViewById(R.id.txttotal_rows);
        txttotal_scanned_rows = findViewById(R.id.txttotal_scanned_rows);

    }

    private void setTextOnViews() {
        btn_cancel.setText("EXIT");
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
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
             txtapi_code.setText("MVIN700 -----> MVIN703");
        }
        if(controll.equals("txttype")) {
             txtapi_code.setText("EP816A ---> MVIN700");
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

        search_list.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });

        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String list_selected_text = adapter.getItem(position).toString();
                switch (controll)
                {
                    case "txtreq_no" : txtreq_no.setText(list_selected_text);
                        final MyProgressdialog progressDialog = new MyProgressdialog(frm_dispatch_finalise_mvin.this);
                        progressDialog.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        insert_selected_req_no_in_db("MVIN703");
                                        progressDialog.dismiss();
                                    }
                                }, 100);
                        break;
                    case "txttype":
                        final MyProgressdialog progressDialog1 = new MyProgressdialog(frm_dispatch_finalise_mvin.this);
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
                                        xreq_no = finapi.issue_slip_fill_record_in_listview_popup("MVIN700");
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
        ArrayList<team> result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins",""+form_code, fgen.cdt1, fgen.cdt2, fgen.muname, txtreq_no.getText().toString().trim());

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
        rows=0;
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
//                        if(!col2.trim().equals(xselected_req_no.trim())){
//                            xFound = "N";
//                            break;
//                        }
                        xreq_no = col2; // icode
                    }
                    if(x==1) {
                        yreq_dt = col2;
                        xreq_dt = col2; // roll no
                    }
                    if(x==2) {
                        xiname = col2; //qty
                    }
                    if(x==4) {
                        xqty = col2;
                    }
                    if(x==5) {
                        xdeptt = col2;
                        xacode = col2;
                    }
                    if(x==7) {
                        xicode = col2;
                    }
                    x +=1;
                }
                if(xFound.equals("Y")) {
                     rows++;
                    // col3 for Comparison.
                    //col5 for insert qty.
                    handler.issue_slip_insert_data(xreq_no, xreq_dt, xreq_no+xreq_dt, xdeptt, xiname);
                    obj.add(new issue_slip_selected_req_model(xreq_no+xreq_dt, xiname));
                }
                Log.d("addData ", "Success");
            }
            txttotal_rows.setText("Total Rows : "+rows);
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
        txttotal_scanned_rows.setText("Scanned Rows : "+scanned_rows);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgen.frm_request = "";
        fgen.GetRequest = false;
    }
}
