package com.finsyswork.erp;

import static com.finsyswork.erp.fgen.scanned_qr_Code;

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
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

public class frm_dispatch_entry extends AppCompatActivity{

    public static Button btn_add, btn_new,btn_save, btn_cancel, btn_scantag;
    Toolbar toolbar;
    public static EditText txtcustomer_name, txtitem, txtqty,btchno_ss, txtaddress, txtpart_no, txtsale_type, txtrows, txttotal_qty;
    public static EditText txttag,txt_barcode;
    RecyclerView recyclerView;
    comman_adapter adapter;
    sqliteHelperClass handler;
    String xseperator = "#~#", xbranch="00", xtype ="";
    String xcustomer_Code = "", xitem_code = "-";
    String xitem_order_qty = "";
    public static ArrayList<String> xcustomer_name = new ArrayList<>();
    public static ArrayList<String> xitem = new ArrayList<>();
    public static ArrayList<comman_model> list = new ArrayList<>();
    public static ArrayList<String> xsale_type = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_dispatch_entry);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        finapi.context = frm_dispatch_entry.this;
        finapi.deleteJsonResponseFile();
        fgen.xcard_view_name = "sale_order_schedule_booking_card_view";
        handler = new sqliteHelperClass(this);
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_dispatch_entry.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        frm_dispatch_entry.xsale_type = finapi.fill_record_in_listview_popup("EP816A");
                        progressDialog.dismiss();
                    }
                }, 100);

        initializeViews();
        btn_cancel.setText("EXIT");
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_dispatch_entry.this);
        Highlightborder();

        handler.clear_data();
        list = handler.comman_get_data();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new comman_adapter(getApplicationContext(), list);
        recyclerView.setAdapter(adapter);

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBtnClickedMethod();
                enabledViewMethod();
                btn_save.setEnabled(true);
                btn_new.setEnabled(false);
                btn_cancel.setText("CANCEL");

                txttag.requestFocus();
                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);

                txtsale_type.setBackgroundResource(R.color.light_blue);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn_text = btn_cancel.getText().toString();
                if(btn_text == "CANCEL")
                {
                    newBtnClickedMethod();
                    disableViewsMethod();
                    btn_save.setEnabled(false);
                    btn_new.setEnabled(true);
                    btn_cancel.setText("EXIT");

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));handler.clear_data();
                    list = handler.comman_get_data();
                    adapter = new comman_adapter(getApplicationContext(), list);
                    recyclerView.setAdapter(adapter);
                    txtsale_type.setBackgroundResource(R.color.light_grey);
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });

        txttag.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == 19 || keyCode == 21)) {
                    if (txttag.getText().toString().trim().length() > 2) {

                        if(txtitem.getText().toString().isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Please Select Item First!!", Toast.LENGTH_LONG).show();
                            return false;
                        }
                        if(txtcustomer_name.getText().toString().isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Please Select Customer First!!", Toast.LENGTH_LONG).show();
                            return false;
                        }
                        String xscanned_text = txttag.getText().toString().trim ();
                        scanned_qr_Code = txttag.getText().toString().trim ();
                        if(xscanned_text.isEmpty() || xscanned_text.equals ("-")) {
                            frm_dispatch_entry.txtqty.setText ("");
                            progressDialog.dismiss ( );
                        }
                        try {
                            String xtag = "";
                            String xtag1 = "";
                            String xtag2 = "";
                            txtqty.setText("");
                            String xicode = "";
                            if(fgen.mcd.equals("SGRP")) {
                                xicode = scanned_qr_Code.substring(0, 8).toString().trim();
                            }
                            else {
                                if(fgen.mcd.equals("MVIN")){
                                    ArrayList<team6> x = finapi.getApi6(fgen.mcd, "areel_FGRcheck", "" + scanned_qr_Code, fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
                                    Gson gson = new Gson();
                                    String listString = gson.toJson(x, new TypeToken<ArrayList<team>>() {}.getType());
                                    String data= "";
                                    try {
                                        JSONArray array = new JSONArray(listString);
                                        for(int i=0; i< 1; i++){
                                            JSONObject respObj = array.getJSONObject(i);
                                            data = respObj.getString("col1");
                                            xicode = respObj.getString("col2");
                                            frm_bar_scanner.xreel_iname = respObj.getString("col4");
                                            frm_dispatch_entry.txtqty.setText(respObj.getString("col6"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(!data.equals("Success"))
                                    {
                                        new android.app.AlertDialog.Builder(frm_dispatch_entry.this)
                                                .setTitle("Error")
                                                .setMessage("Sorry, Scanned BarCode Is Invalid!!")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public   void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        return;
                                                    }
                                                })
                                                .show();
                                        return false;
                                    }
                                }
                                //CHANGE BY SURENDAR FOR GDOT
                                if (fgen.mcd.equals ("GDOT") && fgen.btnid.equals ("EP1212")) {
                                    xicode = scanned_qr_Code.substring(0, 17).toString().trim();
                                    ArrayList<team6> result1 = new ArrayList<> ( );
//                                  result1 = finapi.getApi6 (fgen.mcd, "aFGTAG_check", "" + rawResult.toString ( ).trim ( ).substring (0, 26), fgen.muname, fgen.cdt1.substring (6, 10), "#", "EP1212");
                                    result1 = finapi.getApi6 (fgen.mcd, "aFGTAG_check", "" + scanned_qr_Code.toString ( ).trim (), fgen.muname, fgen.cdt1.substring (6, 10), "#", "EP1212");
                                    Gson gson = new Gson();
                                    String listString = gson.toJson (result1, new TypeToken<ArrayList<team>> ( ) {
                                    }.getType ( ));
                                    String data = "";
                                    String xcol5 = "", xerror = "";
                                    try {
                                        JSONArray array = new JSONArray (listString);
                                        for (int i = 0; i < array.length ( ); i++) {
                                            JSONObject respObj = array.getJSONObject (i);
                                            data = respObj.getString ("col1").trim ( );
                                            xtag2 = respObj.getString ("col2").trim ( );
                                            xtag = respObj.getString ("col3").trim ( );
                                            xcol5 = respObj.getString ("col5").trim ( );
                                            xtag1 = respObj.getString ("col5").trim ();
                                            xerror = respObj.getString ("col6").trim ( );
                                        }
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace ( );
                                    }
                                    if (!data.equals ("Success")) {
                                        frm_dispatch_entry.txttag.setText ("");
                                        AlertDialog alertDialog = new AlertDialog.Builder(frm_dispatch_entry.this).create();
                                        alertDialog.setTitle("Error");
                                        alertDialog.setMessage("No Record Found!!");
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                        return false;
                                    }
                                    frm_dispatch_entry.txttag.setText ("" + xtag1.trim());
//                                  frm_dispatch_entry.txt_barcode.setText ("" + xtag.trim ());
                                    frm_dispatch_entry.txtqty.setText ("" + xtag2.trim ());
                                    frm_dispatch_entry.btchno_ss.setText ("" + xtag.trim ());
//                                  onBackPressed ( );
                                }
                                //SURENDAR
                                else {
                                    try {
                                        xicode = xscanned_text.substring(20, 28).toString().trim();
                                    } catch (Exception e) {
                                        AlertDialog alertDialog = new AlertDialog.Builder(frm_dispatch_entry.this).create();
                                        alertDialog.setTitle("Error Found, Please Try Again!");
                                        alertDialog.setMessage(scanned_qr_Code);
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
//                                                        onBackPressed();
                                                    }
                                                });
                                        alertDialog.show();
                                        return false;
                                    }
                                }
                            }
                            String xform_item =frm_dispatch_entry.txtitem.getText().toString().split("---")[0].trim();
                            if(!xform_item.equals(xicode) && !fgen.mcd.equals ("GDOT"))
                            {
                                AlertDialog alertDialog = new AlertDialog.Builder(frm_dispatch_entry.this).create();
                                alertDialog.setTitle("Error");
                                alertDialog.setMessage("Sorry, Scanned QR Code Is Invalid!!");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                return false;
                            }
                            if (fgen.mcd.equals ("GDOT") && fgen.btnid.equals ("EP1212")) {
                                frm_dispatch_entry.txttag.setText ("" + xtag1.trim ( ));
                            }
                            else {
                                frm_dispatch_entry.txttag.setText (xscanned_text);
                            }
                            if(fgen.mcd.equals("SGRP")) {
                                ArrayList<team6> x = finapi.getApi6(fgen.mcd, "aSGFG_check", xscanned_text.trim(), fgen.muname, fgen.cdt1.substring(6, 10), "#", xscanned_text.trim());
                                boolean msg = finapi.RecieveDataFromApi(frm_dispatch_entry.this, x);
                                frm_dispatch_entry.txtqty.setText(fgen.xpopup_col6);
                                if(!msg){return true;}
                                frm_dispatch_entry.txtqty.setText(fgen.xpopup_col6);
                            }
                            else {
                                try {
                                    if(!fgen.mcd.equals("MVIN")){
                                        if (fgen.mcd.equals ("GDOT") && fgen.btnid.equals ("EP1212")) {
                                            frm_dispatch_entry.txttag.setText ("" + xtag1.trim ( ));
                                        }
                                        else{
                                            frm_dispatch_entry.txtqty.setText(scanned_qr_Code.split("_____________")[1]);
                                        }
                                    }
                                }
                                catch (Exception  e){
                                    AlertDialog alertDialog = new AlertDialog.Builder(frm_dispatch_entry.this).create();
                                    alertDialog.setTitle("Error");
                                    alertDialog.setMessage(scanned_qr_Code);
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
//                                                    onBackPressed();
                                                }
                                            });
                                    alertDialog.show();
                                    return false;
                                }
                            }
                            if (fgen.mcd.equals ("GDOT") && fgen.btnid.equals ("EP1212")) {
                                frm_dispatch_entry.btn_add.performClick ();
                            }
                            else {
                                fill_grid ( );
                            }
                            txttag.requestFocus();
                            adapter.notifyDataSetChanged();
                        }
                        catch (Exception e){
                            Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show();
                        }

                        return true;
                    }
                }
                return false;
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fgen.mcd.equals ("GDOT") && fgen.btnid.equals ("EP1212")){
                    fill_grid_1 ();
                }
                else {
                    fill_grid ( );
                }
            }
        });

        txtsale_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogBox(frm_dispatch_entry.this, xsale_type, "txtsale_type");
            }
        });

        btn_scantag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fgen.frm_request = "frm_dispatch_entry";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
                txttag.requestFocus();
            }
        });

        txtcustomer_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_dispatch_entry.this, frm_sale_schedule_booking.xcustomer_name, "txtcustomer_name");
                txttag.requestFocus();
            }
        });

        txtitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtcustomer_name.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select Customer First!!", Toast.LENGTH_LONG).show();
                    return;
                }
                ShowDialogBox(frm_dispatch_entry.this, frm_sale_schedule_booking.xitem, "txtitem");
                txttag.requestFocus();
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_dispatch_entry.this, "aDnote_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_dispatch_entry.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtsale_type.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please \"Select Type\" For This Entry!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_dispatch_entry.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                list = handler.comman_get_data();
                                if(list.size() <=0)
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Invalid Data!!", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                String xcustomer_name = txtcustomer_name.getText().toString();
                                String xaddress = txtaddress.getText().toString();
                                ArrayList<comman_model> bullets = list;
                                String post_param = "";
                                for(comman_model b : bullets) {
                                    String xicode = b.xcol2.split("---")[0].trim();
                                    String xitem = b.xcol2;
                                    String xqty = b.xcol3;
                                    String xdate = b.xcol4;
                                    String order_no = txtpart_no.getText().toString().split("---")[0].trim();
                                    String order_dt = txtpart_no.getText().toString().split("---")[1].trim();
                                    post_param  += xbranch + xseperator + xtype + xseperator + xcustomer_Code + xseperator
                                            + xicode + xseperator + xqty + xseperator + xdate + xseperator + order_no +
                                            xseperator + order_dt + "!~!~!";
                                }
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aDnote_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP903");
                                boolean msg = finapi.showAlertMessage(frm_dispatch_entry.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                handler.clear_data();
                                list = handler.comman_get_data();
                                adapter = new comman_adapter(getApplicationContext(), list);
                                recyclerView.setAdapter(adapter);
                                txttag.requestFocus();
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    private void rawMethod(){

            if (txttag.getText().toString().trim().length() > 2) {

                if(txtitem.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select Item First!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtcustomer_name.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select Customer First!!", Toast.LENGTH_LONG).show();
                }

                ArrayList<team6> result = new ArrayList<>();
                String xscanned_text = txttag.getText().toString();
                scanned_qr_Code = txttag.getText().toString();
                try {
                    txtqty.setText("");
                    String xicode = "";
                    if(fgen.mcd.equals("SGRP")) {
                        xicode = scanned_qr_Code.substring(0, 8).toString().trim();
                    }
                    else {
                        try {
                            if(fgen.mcd.equals("MVIN")){
                                result = finapi.getApi6(fgen.mcd, "areel_FGRcheck", "" + scanned_qr_Code, fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
                            }
                            else {
                                xicode = xscanned_text.substring(20, 28).toString().trim();
                            }
                        } catch (Exception e) {
                            AlertDialog alertDialog = new AlertDialog.Builder(frm_dispatch_entry.this).create();
                            alertDialog.setTitle("Error Found, Please Try Again!");
                            alertDialog.setMessage(scanned_qr_Code);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
//                                            onBackPressed();
                                              return;
                                        }
                                    });
                            alertDialog.show();

                        }
                    }
                    String xform_item =frm_dispatch_entry.txtitem.getText().toString().split("---")[0].trim();
                    if(!xform_item.equals(xicode))
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(frm_dispatch_entry.this).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("Sorry, Scanned QR Code Is Invalid!!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    frm_dispatch_entry.txttag.setText(xscanned_text);

                    if(fgen.mcd.equals("SGRP")) {
                        ArrayList<team6> x = finapi.getApi6(fgen.mcd, "aSGFG_check", xscanned_text.trim(), fgen.muname, fgen.cdt1.substring(6, 10), "#", xscanned_text.trim());
                        boolean msg = finapi.RecieveDataFromApi(frm_dispatch_entry.this, x);
                        frm_dispatch_entry.txtqty.setText(fgen.xpopup_col6);
                        if(!msg){}
                        frm_dispatch_entry.txtqty.setText(fgen.xpopup_col6);
                    }
                    else {
                        try {
                            frm_dispatch_entry.txtqty.setText(scanned_qr_Code.split("_____________")[1]);
                        }catch (Exception  e){
                            AlertDialog alertDialog = new AlertDialog.Builder(frm_dispatch_entry.this).create();
                            alertDialog.setTitle("Error");
                            alertDialog.setMessage(scanned_qr_Code);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
//                                          onBackPressed();
                                            return;
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                    fill_grid ( );
                    txttag.requestFocus();
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show();
                }
            }
    }

    private void fill_grid() {
        try {
            String xitem = txtitem.getText().toString().trim();
            String xqty = txtqty.getText().toString().trim();
            String xdate = txttag.getText().toString().trim();
            String xorder_qty = txtpart_no.getText().toString().trim();
            if (xitem.isEmpty() || txtqty.getText().toString().isEmpty() || txttag.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Select All Fields First!!", Toast.LENGTH_LONG).show();
                return;
            }
            boolean check_duplicacy = handler.CheckDuplicacy("col4", xitem + xdate);
            if (check_duplicacy) {
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_dispatch_entry.this).create();
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
            boolean CheckItemQtyIsGreaterThanOrNot = handler.CheckItemQtyIsGreaterThanOrNot("col1", "" + xitem, "col2", "" + xqty, "" + xitem_order_qty);
            if (CheckItemQtyIsGreaterThanOrNot) {
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_dispatch_entry.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Sorry, Item Code \"" + xitem.split("---")[0].trim() + "\" Qty Should Not Be Greater\n" +
                        " Than Order Qty \"" + xorder_qty + "\" !!");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }
            handler.commaninsert_data(new comman_model(xitem, xqty, xdate, xitem + xdate));
            list = handler.comman_get_data();
            adapter = new comman_adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            int rows = handler.CountTableRows();
            double total_qty = handler.TotalQtySum("col2");

            txttotal_qty.setText("" + total_qty);
            txtrows.setText("" + rows);

            txtqty.setText("");
            txttag.setText("");
            txttag.requestFocus();
        }catch (Exception e){
            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_dispatch_entry.this).create();
            alertDialog.setTitle("Error Found In FillGrid()");
            alertDialog.setMessage(""+e);
            alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }
    private void fill_grid_1() {
        try {
            String xitem = txttag.getText().toString().trim();
            String xqty = txtqty.getText().toString().trim();
            String xbatch = txt_barcode.getText().toString().trim();
//          String xdate = txtpart_no.getText().toString().trim();
            String xdate = btchno_ss.getText().toString().trim();
            String xorder_qty = txtpart_no.getText().toString().trim();
            if (xitem.isEmpty() || txtqty.getText().toString().isEmpty() || txttag.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Select All Fields First!!", Toast.LENGTH_LONG).show();
                return;
            }
            boolean check_duplicacy = handler.CheckDuplicacy("col4", xitem + xdate);
            if (check_duplicacy) {
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_dispatch_entry.this).create();
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
            boolean CheckItemQtyIsGreaterThanOrNot = handler.CheckItemQtyIsGreaterThanOrNot("col1", "" + xitem, "col2", "" + xqty, "" + xitem_order_qty);
            if (CheckItemQtyIsGreaterThanOrNot) {
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_dispatch_entry.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Sorry, Item Code \"" + xitem.split("---")[0].trim() + "\" Qty Should Not Be Greater\n" +
                        " Than Order Qty \"" + xorder_qty + "\" !!");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }
            handler.commaninsert_data(new comman_model(xitem, xqty, xdate, xitem + xdate));
            list = handler.comman_get_data();
            adapter = new comman_adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            int rows = handler.CountTableRows();
            double total_qty = handler.TotalQtySum("col2");

            txttotal_qty.setText("" + total_qty);
            txtrows.setText("" + rows);

            txtqty.setText("");
            txttag.setText("");
            txttag.requestFocus();
        }catch (Exception e){
            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_dispatch_entry.this).create();
            alertDialog.setTitle("Error Found In FillGrid()");
            alertDialog.setMessage(""+e);
            alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    private void enabledViewMethod() {
        txtcustomer_name.setEnabled(true);
        txtaddress.setEnabled(true);
        txtitem.setEnabled(true);
        txtpart_no.setEnabled(true);
        txtqty.setEnabled(true);
        btchno_ss.setEnabled(true);
        txttag.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtcustomer_name.setText("");
        txtaddress.setText("");
        txtitem.setText("");
        txtpart_no.setText("");
        txtqty.setText("");
        txttag.setText("");
        btchno_ss.setText ("");
        txttotal_qty.setText("");
        txtrows.setText("");
    }

    private void Highlightborder() {
        txtcustomer_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtaddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        txtpart_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        btchno_ss.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txttag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    private void disableViewsMethod() {
        txtcustomer_name.setEnabled(false);
        txtaddress.setEnabled(false);
        txtitem.setEnabled(false);
        txtpart_no.setEnabled(false);
        txtqty.setEnabled(false);
        btchno_ss.setEnabled(false);
        txttag.setEnabled(false);
    }

    private void initializeViews() {
        txtcustomer_name = findViewById(R.id.txtcustomer_name);
        txtitem = findViewById(R.id.txtitem);
        toolbar = findViewById(R.id.toolbar);
        txtaddress = findViewById(R.id.txtaddress);
        txtpart_no = findViewById(R.id.txtpart_no);
        txtqty = findViewById(R.id.txtqty);
        btchno_ss = findViewById(R.id.btchno_ss);
        recyclerView = findViewById(R.id.recycler);
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_add = findViewById(R.id.btn_add);
        txt_barcode = findViewById(R.id.txt_barcode);
        txttag = findViewById(R.id.txttag);
        btn_scantag = findViewById(R.id.btn_scan);
        txtsale_type = findViewById(R.id.txtsale_type);
        txtrows = findViewById(R.id.txtrows);
        txttotal_qty = findViewById(R.id.txttotal_qty);
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
        if(controll.equals("txtcustomer_name")) txtapi_code.setText("EP815D");
        if(controll.equals("txtitem")) txtapi_code.setText("EP837A");
        if(controll.equals("txtsale_type")) txtapi_code.setText("EP816A");

        close_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
//        search_list.setAdapter(adapter);

        final SearchListAdapter listViewadapter = new SearchListAdapter(this, list);
        search_list.setAdapter(listViewadapter);


        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listViewadapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String list_selected_text = listViewadapter.getItem(position).toString();
                switch (controll)
                {
                    case "txtcustomer_name" :
                        final MyProgressdialog progressDialog = new MyProgressdialog(frm_dispatch_entry.this);
                        progressDialog.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        txtitem.setText("");
                                        String xcustomer = list_selected_text.split("---")[0].trim() + "---" + list_selected_text.split("---")[1].trim();
                                        String xdestination = list_selected_text.split("---")[2].trim();
                                        String xtype = txtsale_type.getText().toString().split("---")[0].trim();
                                        xcustomer_Code = xcustomer.split("---")[1].trim();
                                        txtcustomer_name.setText(xcustomer);
                                        txtaddress.setText(xdestination);
                                        fgen.frm_request = "frm_dispatch_entry";
                                        frm_sale_schedule_booking.xitem = finapi.fill_record_in_listview_popup_for_item_part_no("EP837A", xcustomer_Code+"-"+xtype);
                                        progressDialog.dismiss();
                                    }
                                }, 100);
                        break;
                    case "txtitem" :
                        String xiname = list_selected_text.split("---")[0].trim() + "---" + list_selected_text.split("---")[1].trim();
                        xitem_order_qty = list_selected_text.split("---")[2].trim();
                        String xorder_no = list_selected_text.split("---")[3].trim();
                        String xorder_dt = list_selected_text.split("---")[4].trim();
                        xitem_code = list_selected_text.split("---")[0].trim();
                        txtitem.setText(xiname);
                        txtpart_no.setText(xorder_no + " --- "+ xorder_dt);
                        break;
                    case "txtsale_type" :
//                        final MyProgressdialog progressDialog2 = new MyProgressdialog(frm_dispatch_entry.this);
//                        progressDialog2.show();
//                        new Handler().postDelayed(
//                                new Runnable() {
//                                    public void run() {
                                        xtype = list_selected_text.split("---")[0].trim();
                                        txtsale_type.setText(list_selected_text);

                                        frm_lead_enquiry.xfrm_lead_enquiry = "Y";
                                        frm_lead_enquiry.xfg_sub_group_code = xtype;
                                        frm_sale_schedule_booking.xcustomer_name = finapi.fill_record_in_listview_popup_for_truck_loading("EP815D");
                                        frm_lead_enquiry.xfrm_lead_enquiry = "";
//                                    }
//                                }, 100);
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
        fgen.xcard_view_name = "";
        fgen.frm_request = "";
    }
}