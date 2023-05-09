package com.finsyswork.erp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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

import java.util.ArrayList;
import java.util.Calendar;

public class frm_material_issue_req extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static  ArrayList<String> xitem = new ArrayList<>();
    public static ArrayList<String> xdept = new ArrayList<>();
    public static ArrayList<String> xwip_stage = new ArrayList<>();
    public static EditText txtdepartment, txtwip_stage, txtitem, txtqty, txtremarks, txttype, txt_barcode;
    TextView viewwip_stage, txtlast_dt;
    Button btn_save,btn_new, btn_cancel, btn_add, btn_scan;
    RecyclerView recyclerView;
    Toolbar toolbar;
    sqliteHelperClass handler;
    LinearLayout linear_last_dt,linear_type;
    public static ArrayList<issue_req_model> list = new ArrayList<>();
    material_request_adapter adapter;
    String ApiName = "aIssEntry_ins";
    LinearLayout linear_remarks, linear_wip_stage;
    public static String frm_request = "";
    public static ArrayList<String> xtype = new ArrayList<>();
    public static ArrayList<String> xlot_enable = new ArrayList<>();
    public static String reel_wise_lot="N";
    RadioButton radio_yes, radio_no;

    String xmbranch="00", xmv_type="30", xmv_dep_acd, xmv_dep_stg, xmv_dep_icd, xmv_dep_req, xseperator = "#~#", type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_material_issue_req);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_material_issue_req.this;
        finapi.deleteJsonResponseFile();

        handler = new sqliteHelperClass(this);
        finapi.setColorOfStatusBar(frm_material_issue_req.this);

        initializeViews();

        final MyProgressdialog progressDialog = new MyProgressdialog(frm_material_issue_req.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        check_form_request();
                        frm_material_issue_req.xdept = finapi.fill_record_in_listview_popup("EP819");
                        frm_material_issue_req.xwip_stage = finapi.fill_record_in_listview_popup("EP820");
                        reel_wise_lot = "Y";
                        new AsyncCaller_material_issue_req().execute();
                        // On complete call either onLoginSuccess or onLoginFailed
                        progressDialog.dismiss();
                    }
                }, 100);


        setTextOnViews();
        disableViewsMethod();
        setSupportActionBar(toolbar);
        Highlightborder();
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        handler.clear_data();
        list = handler.issue_req_get_data();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new material_request_adapter(getApplicationContext(), list);
        recyclerView.setAdapter(adapter);


        radio_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(radio_yes.isChecked()) reel_wise_lot = "Y";
                else reel_wise_lot = "N";
            }
        });

        txttype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_material_issue_req.this, xtype, "txttype");
                txt_barcode.requestFocus();
            }
        });

        txt_barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == 19 || keyCode == 21)) {
                    if (txt_barcode.getText().toString().trim().length() > 2) {
                        scan_reel_method(txt_barcode.getText().toString().trim());
                        return true;
                    }
                }
                return false;
            }
        });

        txtdepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_material_issue_req.this, xdept, "txtdepartment");
                txt_barcode.requestFocus();
            }
        });

        txtwip_stage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_material_issue_req.this, xwip_stage, "txtwip_stage");
                txt_barcode.requestFocus();
            }
        });

        txtitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_material_issue_req.this, xitem, "txtitem");
                txt_barcode.requestFocus();
            }
        });

        txtlast_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialogue();
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), scannerView.class));
                txt_barcode.requestFocus();
                // On complete call either onLoginSuccess or onLoginFailed
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
                txttype.setText(frm_material_issue_req.xtype.get(0));
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn_text = btn_cancel.getText().toString();
                if(btn_text == "CANCEL")
                {
                    final MyProgressdialog progressDialog = new MyProgressdialog(frm_material_issue_req.this);
                    progressDialog.show();
                    new Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    newBtnClickedMethod();
                                    disableViewsMethod();
                                    btn_save.setEnabled(false);
                                    btn_new.setEnabled(true);
                                    btn_cancel.setText("EXIT");

                                    handler.clear_data();
                                    list = handler.issue_req_get_data();
                                    adapter = new material_request_adapter(getApplicationContext(), list);
                                    recyclerView.setAdapter(adapter);

                                    txttype.setBackgroundResource(R.color.light_grey);
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

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fill_grid();
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_material_issue_req.this, ApiName);
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_material_issue_req.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = txttype.getText().toString().split("---")[0].trim();
                xmv_type = type;
                if(txttype.getText().toString().isEmpty() && !fgen.frm_request.equals("request_material_purchase"))
                {
                    Toast.makeText(getApplicationContext(), "Please Select Type First!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtdepartment.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill Department", Toast.LENGTH_LONG).show();
                    return;
                }

                if(type.equals("30")) {
                    if (txtwip_stage.getText().toString().isEmpty() && fgen.frm_request.equals("request_material_return")){
                        Toast.makeText(getApplicationContext(), "Please Fill WIP State", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                final MyProgressdialog progressDialog = new MyProgressdialog(frm_material_issue_req.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param ="";
                                list = handler.issue_req_get_data();
                                if(list.size() <=0)
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Invalid Data!!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                switch (fgen.frm_request) {
                                    case "request_material_issue" :
                                        for (issue_req_model item : list) {
                                            String litem = (item.getItem().split("---")[0]).toString();
                                            try {
                                                if(fgen.mcd.equals("SVPL")) {
                                                    litem = litem.substring(0, 8);
                                                }else{litem = litem.substring(18, 26);}
                                            }catch (Exception e){}
                                            String[] ldept = txtdepartment.getText().toString().split("---");
                                            String[] xstage = txtwip_stage.getText().toString().split("---");
                                            String xremarks = txtremarks.getText().toString();
                                            xmv_dep_acd = ldept[0];
                                            xmv_dep_icd = litem;
                                            xmv_dep_stg = xstage[0];
                                            xmv_dep_req = item.getQty();

                                            post_param += xmbranch + xseperator + xmv_type + xseperator + xmv_dep_acd
                                                    + xseperator + xmv_dep_stg + xseperator + xmv_dep_icd + xseperator
                                                    + xmv_dep_req + "!~!~!";
                                        }
                                        break;
                                    case "request_material_return":
                                        for (issue_req_model item : list) {
                                            String litem = (item.getItem().split("---")[0]).toString();
                                            try {
                                                if(fgen.mcd.equals("SVPL")) {
                                                    litem = litem.substring(0, 8);
                                                }else{litem = litem.substring(18, 26);}
                                            }catch (Exception e){}
                                            String[] ldept = txtdepartment.getText().toString().split("---");
                                            String[] xstage = txtwip_stage.getText().toString().split("---");
                                            xmv_dep_acd = ldept[0];
                                            xmv_dep_stg = xstage[0];
                                            xmv_dep_icd = litem;
                                            xmv_dep_req = item.getQty();

                                            post_param += xmbranch + xseperator + xmv_type + xseperator + xmv_dep_acd
                                                    + xseperator + xmv_dep_stg + xseperator + xmv_dep_icd + xseperator
                                                    + xmv_dep_req + "!~!~!";
                                        }
                                        break;
                                    case "request_material_purchase":
                                        if(txtlast_dt.getText().toString().isEmpty())
                                        {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Please Fill Last Date!!", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        String xlast_dt = txtlast_dt.getText().toString();
                                        for (issue_req_model item : list) {
                                            String litem = (item.getItem().split("---")[0]).toString();
                                            try {
                                                if(fgen.mcd.equals("SVPL")) {
                                                    litem = litem.substring(0, 8);
                                                }else{litem = litem.substring(18, 26);}
                                            }catch (Exception e){}
                                            String[] ldept = txtdepartment.getText().toString().split("---");
                                            String xremarks = txtremarks.getText().toString();
                                            xmv_dep_acd = ldept[0];
                                            xmv_dep_icd = litem;
                                            xmv_dep_req = item.getQty();

                                            post_param += xmbranch + xseperator + xmv_type + xseperator + xmv_dep_acd
                                                    + xseperator + xmv_dep_icd + xseperator + xmv_dep_req + xseperator + xlast_dt
                                                    + xseperator + xremarks + "!~!~!";
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                ArrayList<team> result = finapi.getApi(fgen.mcd, ApiName,post_param, fgen.muname, fgen.cdt1.substring(6, 10), "-", "-");
                                boolean msg = finapi.showAlertMessage(frm_material_issue_req.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                handler.clear_data();
                                list = handler.issue_req_get_data();
                                adapter = new material_request_adapter(getApplicationContext(), list);
                                recyclerView.setAdapter(adapter);
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    private boolean scan_reel_method(String rawResult) {
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_material_issue_req.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String xicode =  rawResult;
                        if(fgen.mcd.equals("SVPL"))
                        {
                            try {
                                xicode = xicode.substring(8, 18).toString();
                            }catch (Exception e){
                                xicode = ""+ rawResult.toString().trim().substring(8,xicode.length());
                            }
                        }
                        txt_barcode.setText("");
                        boolean duplicate = false;
                        ArrayList<team6> x = finapi.getApi6(fgen.mcd, "areel_check",xicode, fgen.muname, fgen.cdt1.substring(6,10), "#", "#");
                        boolean msg = finapi.RecieveDataFromApi(frm_material_issue_req.this, x);
                        xicode = fgen.xpopup_col2 +"----"+ fgen.xpopup_col4;
                        duplicate = handler.CheckDuplicacy("col1", "" + xicode.trim());
                        if(duplicate)
                        {
                            AlertDialog alertDialog = new AlertDialog.Builder(frm_material_issue_req.this).create();
                            alertDialog.setTitle("Error");
                            alertDialog.setMessage("Sorry, Duplicate Items Are Not Allowed!!");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            txt_barcode.setText("");
                            progressDialog.dismiss();
                            return;
                        }
                        if(fgen.xpopup_col1.equals("Failure"))
                        {
                            AlertDialog alert = new AlertDialog.Builder(frm_material_issue_req.this).create();
                            alert.setTitle("Error");
                            alert.setMessage(fgen.xpopup_col6);
                            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alert.show();
                            return;
                        }

                        frm_material_issue_req.txtitem.setText(""+xicode.trim());
                        txt_barcode.setText("");
                        progressDialog.dismiss();
                    }
                }, 100);
        return  true;
    }

    private void fill_grid() {

        String item = txtitem.getText().toString().trim();
        String qty =  txtqty.getText().toString();

        if(item.isEmpty() || qty.isEmpty() || qty.length() > 6) {
            Toast.makeText(getApplicationContext(), "Invalid Item Detail!!", Toast.LENGTH_LONG).show();
            return;
        }
        boolean duplicacy = handler.CheckDuplicacy("col1", item);
        if(duplicacy)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(frm_material_issue_req.this).create();
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
        handler.issue_req_insert_data(new issue_req_model("1", item, qty));
        list = handler.issue_req_get_data();
        adapter = new material_request_adapter(getApplicationContext(), list);
        recyclerView.setAdapter(adapter);
        txtitem.setText("");
        txtqty.setText("");
        txt_barcode.setText("");
        txt_barcode.requestFocus();
    }

    private void check_form_request() {
        switch (fgen.frm_request)
        {
            case "request_material_issue":
                frm_material_issue_req.xtype = finapi.fill_record_in_listview_popup("EP816B");
                ApiName = "aIssReq_ins";
                xmv_type = "30";
                toolbar.setTitle("Material Issue Request");
                break;
            case "request_material_return":
                frm_material_issue_req.xtype = finapi.fill_record_in_listview_popup("EP816C");
                xmv_type = "11";
                ApiName = "aissreq_ins";
                toolbar.setTitle("Material Return Request");
                break;
            case "request_material_purchase" :
                xmv_type = "60";
                ApiName = "aPurReq_ins";
                toolbar.setTitle("Material Purchase Request");
                linear_remarks.setVisibility(View.VISIBLE);
                linear_wip_stage.setVisibility(View.GONE);
                linear_type.setVisibility(View.GONE);
                linear_last_dt.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void enabledViewMethod() {
        txtdepartment.setEnabled(true);
        txtwip_stage.setEnabled(true);
        txtitem.setEnabled(true);
        txtqty.setEnabled(true);
        btn_scan.setEnabled(true);
        txtremarks.setEnabled(true);
        txtlast_dt.setEnabled(true);
        txt_barcode.setEnabled(true);
        txttype.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtdepartment.setText("");
        txtwip_stage.setText("");
        txtitem.setText("");
        txtqty.setText("");
        txtremarks.setText("");
        txtlast_dt.setText("");
        txttype.setText("");
        txt_barcode.setText("");
    }

    private void disableViewsMethod() {
        txtdepartment.setEnabled(false);
        txtwip_stage.setEnabled(false);
        txtitem.setEnabled(false);
        txtqty.setEnabled(false);
        btn_scan.setEnabled(false);
        txtremarks.setEnabled(false);
        txtlast_dt.setEnabled(false);
        txt_barcode.setEnabled(false);
        txttype.setEnabled(false);
    }
    private void Highlightborder() {
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
        txtremarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    private void setTextOnViews() {
        btn_cancel.setText("EXIT");
    }

    private void initializeViews() {
        radio_yes = findViewById(R.id.radio_yes);
        radio_no = findViewById(R.id.radio_no);
        toolbar = findViewById(R.id.toolbar);
        btn_scan = findViewById(R.id.btn_scan);
        linear_remarks = findViewById(R.id.linear_remarks);
        linear_wip_stage = findViewById(R.id.linear_wip_stage);
        viewwip_stage = findViewById(R.id.viewwip_stage);
        txtdepartment = findViewById(R.id.txtdepartment);
        txtwip_stage = findViewById(R.id.txtwip_stage);
        txtlast_dt = findViewById(R.id.txtlast_dt);
        txtitem = findViewById(R.id.txtitem);
        txtqty = findViewById(R.id.txtqty);
        btn_save = findViewById(R.id.btn_save);
        btn_new = findViewById(R.id.btn_new);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_add = findViewById(R.id.btn_add);
        recyclerView = findViewById(R.id.recycler);
        txtremarks = findViewById(R.id.txtremarks);
        linear_last_dt = findViewById(R.id.linear_last_dt);
        linear_type = findViewById(R.id.linear_type);
        txttype = findViewById(R.id.txttype);
        txt_barcode = findViewById(R.id.txt_barcode);
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
        if(controll.equals("txtdepartment")) txtapi_code.setText("EP819");
        if(controll.equals("txtwip_stage")) txtapi_code.setText("EP820");
        if(controll.equals("txtitem")) txtapi_code.setText("EP821");
        if(controll.equals("txttype")) {
            if(fgen.frm_request.equals("request_material_issue")) {
                txtapi_code.setText("EP816B");
            }
            else
            {
                txtapi_code.setText("EP816C");
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
                    case "txtdepartment" : txtdepartment.setText(list_selected_text);
                        break;
                    case "txtwip_stage" : txtwip_stage.setText(list_selected_text);
                        break;
                    case "txtitem" : txtitem.setText(list_selected_text);
                        break;
                    case "txttype" : txttype.setText(list_selected_text);
                        type = list_selected_text.split("---")[0].trim();
                        if(type.equals("11"))
                        {
                            linear_wip_stage.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
    }


    private void showDatePickerDialogue() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
        txt_barcode.requestFocus();
    }

    @Override
    public void onDateSet(DatePicker view, int i, int i1, int i2) {
        String day = "";
        String month = "";
        if(String.valueOf(i2).length() == 1) {
            day = "0" + String.valueOf(i2);
        }
        else{
            day = String.valueOf(i2);
        }

        if(String.valueOf(i1).length() == 1) {
            month = "0" + String.valueOf(i1 + 1);
            if(i1 >=9)
            {
                month = String.valueOf(i1 + 1);
            }
        }
        else {
            month = String.valueOf(i1);
            if(i1 > 9)
            {
                month = String.valueOf(i1 + 1);
            }
        }
        String date = (day +"/"+ month +"/"+ i).toString();
        txtlast_dt.setText(date);
    }
}


class AsyncCaller_material_issue_req extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread
    }
    @Override
    protected Void doInBackground(Void... params) {
        frm_material_issue_req.xitem = new ArrayList<>();
        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
        frm_material_issue_req.xitem = finapi.fill_record_in_listview_popup("EP821");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
    }
}
