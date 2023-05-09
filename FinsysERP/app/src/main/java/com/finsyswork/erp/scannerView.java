package com.finsyswork.erp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scannerView extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;
    sqliteHelperClass handler;
    issue_req_model issue_req_model;
    String qty = "";
    String xbuild_qr_name = "", xraw_variable = "", xicode="",xtagsca="";
    final MyProgressdialog progressDialog = new MyProgressdialog(scannerView.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        setContentView(scannerView);
        frm_paper_end.list = frm_paper_end.list;
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void handleResult(Result rawResult) {
        fgen.scanned_qr_Code = ""+rawResult;
        frm_paper_end.xicode = "";
        frm_fg_prod_qrcode.xtagsca = "";
        frm_job_issue_entry.xreel_qty = "";
        frm_paper_end.issue_req_model = null;
        handler = new sqliteHelperClass(this);
        if(frm_jobw_prod.xjob_card_no.equals("Y")) {
            try {
                frm_jobw_prod.xjob_card_no = "N";
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("JOB CARD NO?");
                builder.setMessage("ARE YOU SURE, You Want To Do Select This Job Card ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        frm_jobw_prod.txtselect_job.setText("" + rawResult);
                        frm_lead_enquiry.xfg_sub_group_code = frm_jobw_prod.txtselect_job.getText().toString();
                        ArrayList<team> xjob = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "EP835S", fgen.cdt1, fgen.cdt2, fgen.muname, frm_lead_enquiry.xfg_sub_group_code);
                        finapi.job_work_production(xjob);
                        frm_lead_enquiry.xfg_sub_group_code = "";
                        frm_jobw_prod.btn_scan.setEnabled(false);
                        frm_jobw_prod.txtselect_job.setEnabled(false);
                        frm_jobw_prod.btn_scan.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                        dialog.dismiss();
                        onBackPressed();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onBackPressed();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } catch (Exception e) {
            }
        }
        try {
            frm_record_alectricity_usage.txtmachine_qr_code.setText(""+rawResult);
            onBackPressed();
            return;
        }
        catch (Exception e){}
        try {
            if(frm_verify_asset_location.btn_clicked.equals("scan_verify_asset")) {
                ArrayList<team6> result = new ArrayList<>();
                if(fgen.mcd.equals("ADVG")) {
                    result = finapi.getApi6(fgen.mcd, "aMCTAG_check", "" + rawResult.toString(), fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
                }
                else {
                    result = finapi.getApi6(fgen.mcd, "aMCTAG_check", "" + rawResult.toString(), fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
                }
                Gson gson = new Gson();
                String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
                String data= "";
                try {
                    JSONArray array = new JSONArray(listString);
                    for(int i=0; i< 1; i++) {
                        JSONObject respObj = array.getJSONObject (i);
                        data = respObj.getString ("col1");
                        if(data.equals("Success")) {
                            frm_verify_asset_location.txtscan_asset.setText (rawResult.toString ( ));
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!data.equals("Success"))
                {
                    new android.app.AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage("This Asset Not In Your System !!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public   void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    onBackPressed();
                                    return;
                                }
                            })
                            .show();
                    return;
                }
                else {
                    frm_verify_asset_location.txtremarks1.setText (data);
                    onBackPressed();
                    return;
                }
            }
        }catch (Exception e){}



        try {
            if(frm_scaninfo.btn_clicked.equals("scan_info")) {
                ArrayList<team6> result = new ArrayList<>();
                if(fgen.mcd.equals("ADVG")) {
                    result = finapi.getApi6(fgen.mcd, "aMCTAG_check", "" + rawResult.toString(), fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
                }
                Gson gson = new Gson();
                String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
                String data= "";
                try {
                    JSONArray array = new JSONArray(listString);
                    for(int i=0; i< 1; i++) {
                        JSONObject respObj = array.getJSONObject (i);
                        data = respObj.getString ("col1");
                        if(data.equals("Success")) {
                            frm_scaninfo.txtremarks.setText (respObj.getString ("col2"));
                        }
                        frm_scaninfo.txtremarks1.setText (respObj.getString ("col3"));
                        frm_scaninfo.txtremarks2.setText (respObj.getString ("col4"));
                        frm_scaninfo.txtremarks3.setText (respObj.getString ("col5"));
                        frm_scaninfo.txtremarks4.setText (respObj.getString ("col6"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!data.equals("Success"))
                {
                    new android.app.AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage("This Asset Not In Your System !!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public   void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    onBackPressed();
                                    return;
                                }
                            })
                            .show();
                    return;
                }
                else {
                    frm_scaninfo.txtscan_asset1.setText (rawResult.toString());
                    frm_scaninfo.txtremarks6.setText (data);
                    onBackPressed();
                    return;
                }
            }
        }catch (Exception e){}


        try {
            if(frm_bar_scanner.btn_clicked.equals("frm_bar_scanner_location")) {
                frm_bar_scanner.txtlocation.setText("" + rawResult);
                onBackPressed();
                return;
            }
        }catch (Exception e){}
        try {
            if(frm_bar_scanner.btn_clicked.equals("frm_bar_scanner_reels")) {
                ArrayList<team6> result = new ArrayList<>();
                if(fgen.mcd.equals("SVPL")) {
                    result = finapi.getApi6(fgen.mcd, "areel_check", "" + rawResult.toString().substring(8, 18), fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
                }
                else {
                    if(fgen.btnid.equals("EP1503")){
                        result = finapi.getApi6(fgen.mcd, "areel_FGRcheck", "" + rawResult, fgen.muname, fgen.cdt1.substring(6, 10), "#", "aphystk_ins");
                    }
                    else {
                        result = finapi.getApi6(fgen.mcd, "areel_check", "" + rawResult, fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
                    }
                }
                Gson gson = new Gson();
                String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
                String data= "";
                try {
                    JSONArray array = new JSONArray(listString);
                    for(int i=0; i< 1; i++){
                        JSONObject respObj = array.getJSONObject(i);
                        data = respObj.getString("col1");
                        frm_bar_scanner.xicode = respObj.getString("col2");
                        frm_bar_scanner.xreel_iname = respObj.getString("col4");
                        frm_bar_scanner.txtqty2.setText(respObj.getString("col6"));
                        frm_bar_scanner.txtcompany_reel_no.setText(respObj.getString("col5"));
                        frm_bar_scanner.txtqty1.setText(respObj.getString("col6"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!data.equals("Success"))
                {
                    String  msg = "Sorry, Reel Is Invalid!!";
                    if(fgen.mcd.equals("MVIN")){msg = frm_bar_scanner.txtqty1.getText().toString();}
                    new android.app.AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage(msg)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public   void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    onBackPressed();
                                    return;
                                }
                            })
                            .show();
                    return;
                }
                else {
                    frm_bar_scanner.txt_reels.setText("" + frm_bar_scanner.xreel_iname);
                    frm_bar_scanner.reel_scan_result = "" + rawResult;
                    if(!fgen.mcd.equals("STRU")) {
                        frm_bar_scanner.btn_add.performClick();
                    }
                    onBackPressed();
                    return;
                }
            }
        }catch (Exception e){}

        try {
            if(frm_bin_scanning.btn_clicked.equals("frm_bin_scanning")) {
                frm_bin_scanning.reel_scan_result = ""+rawResult;
                frm_bin_scanning.txt_bin.setText("" + rawResult);
                xcheck_qr_code_for_bin_scanning_only(""+rawResult);
                return;
            }
        }catch (Exception e){}
        if(frm_paper_end.btn_clicked.equals("job")){
            try {
                scanned_job_verification(""+rawResult.toString().trim());
            }catch (Exception e){}
        }
        if(frm_paper_end.btn_clicked.equals("reel") || frm_reel_return_MCPL.btn_clicked.equals("reel"))
        {
            if(fgen.frm_request.equals("frm_paper_end_for_jobs"))
                multi_reel_single_job(""+rawResult.toString().trim());
            else
                multi_reel_single_job_for_paper_end(""+rawResult.toString().trim());
        }
        switch (fgen.frm_request)
        {
            case "frm_fg_physical":
                try {
                    if(frm_fg_physical.btn_clicked.equals("frm_fg_physical_location")) {
                        frm_fg_physical.txtlocation.setText("" + rawResult);
                        onBackPressed();
                        return;
                    }
                }catch (Exception e){}
                try {
                    if(frm_fg_physical.btn_clicked.equals("frm_fg_physical_reels")) {
                        ArrayList<team6> result = new ArrayList<>();
                        if(fgen.mcd.equals("SVPL")) {
                            result = finapi.getApi6(fgen.mcd, "areel_check", "" + rawResult.toString().substring(8, 18), fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
                        }
                        else {
                            if(fgen.mcd.equals("SGRP")){
                                result = finapi.getApi6(fgen.mcd, "aSGFG_check", "" + rawResult, fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
                            }
                            else {
                                result = finapi.getApi6(fgen.mcd, "areel_check", "" + rawResult, fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
                            }
                        }
                        Gson gson = new Gson();
                        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
                        String data= "";
                        try {
                            JSONArray array = new JSONArray(listString);
                            for(int i=0; i< 1; i++){
                                JSONObject respObj = array.getJSONObject(i);
                                data = respObj.getString("col1");
                                frm_fg_physical.xicode = respObj.getString("col2");
                                frm_fg_physical.xreel_iname = respObj.getString("col4");
                                frm_fg_physical.txtqty2.setText(respObj.getString("col6"));
                                frm_fg_physical.txtcompany_reel_no.setText(respObj.getString("col5"));
                                frm_fg_physical.txtqty1.setText(respObj.getString("col6"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(!data.equals("Success"))
                        {
                            new android.app.AlertDialog.Builder(this)
                                    .setTitle("Error")
                                    .setMessage("Sorry, Reel Is Invalid!!")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public   void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            onBackPressed();
                                            return;
                                        }
                                    })
                                    .show();
                            return;
                        }
                        else {
                            frm_fg_physical.txt_reels.setText("" + frm_fg_physical.xreel_iname);
                            frm_fg_physical.reel_scan_result = "" + rawResult;
                            frm_fg_physical.btn_add.performClick();
                            onBackPressed();
                            return;
                        }
                    }
                }catch (Exception e){}

                break;
            case "issue_slip" :
                xicode = ""+rawResult.toString().trim();
                boolean duplicate = false;
                if(fgen.mcd.equals("SVPL")) {try{xicode = xicode.substring(8,xicode.length()).toString();} catch (Exception  e){ xicode = ""+rawResult;}}
                String xtype = frm_issue_slip.txttype.getText().toString();
                ArrayList<team6> x = finapi.getApi6(fgen.mcd, "aNJreel_jccheck",xicode, fgen.muname, fgen.cdt1.substring(6,10), "#", xtype.split("---")[0].trim());
                boolean msg = finapi.RecieveDataFromApi(scannerView.this, x);
                if(!msg){return;}
                xicode = fgen.xpopup_col2 + "----" + fgen.xpopup_col4 + "---" + fgen.xpopup_col3;
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
                                    onBackPressed();
                                }
                            });
                    alertDialog.show();
                    return;
                }
                frm_issue_slip.requested_qty = "";
                frm_issue_slip.requested_qty = handler.issue_slip_CheckItemQty("col3", xicode.split("---")[0].trim());
                String qty = fgen.xpopup_col6.trim();
                showAlert("" + xicode, qty);
                break;

            case "frm_dispatch_finalise_mvin" :
                try {
                    x = finapi.getApi6(fgen.mcd, "areel_FGRcheck", "" + rawResult, fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
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
                                        onBackPressed();
                                        return;
                                    }
                                })
                                .show();
                        return;
                    }
                    xicode = fgen.xpopup_col2 + "----" + fgen.xpopup_col4 + "---" + fgen.xpopup_col3;
                    duplicate = handler.CheckDuplicacy("col1", "" + xicode);
                    if (duplicate) {
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("Sorry, Duplicate Items Are Not Allowed!!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        onBackPressed();
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
                                        onBackPressed();
                                    }

                                });
                        alert.show();
                    } else {
                        ++frm_dispatch_finalise_mvin.scanned_rows;
                        issue_req_model = new issue_req_model("1", xicode, qty);
                        handler.issue_slip_insert_data(issue_req_model);
                        onBackPressed();
                    }
                }catch (Exception e){
                    new android.app.AlertDialog.Builder(com.finsyswork.erp.scannerView.this)
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
                break;

            case "frm_paper_end_for_jobs":
                if(frm_paper_end.btn_clicked.equals("reel"))
                {
                    if(xraw_variable.equals("Y"))
                    {
                        return;
                    }
                    onBackPressed();
                    break;
                }
                duplicate = handler.CheckDuplicacy("col1", ""+rawResult);
                if(duplicate)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Sorry, Duplicate Rows Not Allowed!!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    onBackPressed();
                                }
                            });
                    alertDialog.show();
                    return;
                }
                if(fgen.frm_request.equals("frm_paper_end_for_jobs")) {
                    showAlert_for_job_or_reel("" + rawResult, "JOB");
                }
                break;
            case "frm_paper_end_for_reels":
            case "frm_reel_issue_return":
                if(!xraw_variable.equals("Y")) {
                    multi_reel_single_job_switch("" + rawResult);
                }
                break;
            case "frm_invoice_entry" :
                String scanned_text = ""+rawResult;
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                finapi.context = getApplicationContext();
                                fgen.xcard_view_name = "sale_order_schedule_booking_card_view";
                                frm_lead_enquiry.xfrm_lead_enquiry = "Y";
                                frm_lead_enquiry.xfg_sub_group_code = scanned_text;
                                ArrayList<String> x = finapi.fill_record_in_listview_popup("EP842D");

                                frm_invoice_entry.txtscan.setText(scanned_text);
                                fgen.frm_request = "";
                                progressDialog.dismiss();
                                onBackPressed();
                            }
                        }, 100);
                break;
            case "prod_entry_gdot" :
                scanned_text = ""+rawResult;
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                fgen._selectedVal= scanned_text;
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aFGTAG_check", scanned_text, fgen.muname, fgen.cdt1.substring(6, 10), "-", "-");
                                Gson  gson = new Gson();
                                String  listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
                                String data= "";
                                try {
                                    JSONArray array = new JSONArray(listString);
                                    for(int i=0; i< 1; i++) {
                                        JSONObject respObj = array.getJSONObject (i);
                                        data = respObj.getString ("col1");
                                        xicode = respObj.getString ("col2");
                                        xtagsca = respObj.getString ("col3");
                                    }
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
//                                if(data.equals("Failure")) {
//                                    frm_fg_prod_qrcode.txtitem.setText ("QRCode Already Scan !!");
//                                    onBackPressed();
//                                    return;
//                                }
                                if (!data.equals("Success")) {
                                    new android.app.AlertDialog.Builder (com.finsyswork.erp.scannerView.this)
                                            .setTitle ("Error")
                                            .setMessage ("QRCode Already Scan !!")
                                            .setPositiveButton ("Ok", new DialogInterface.OnClickListener ( ) {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss ( );
                                                    onBackPressed ( );
                                                    return;
                                                }
                                            })
                                            .show ( );
                                    return;
                                }
                                else
                                {
//                                  frm_fg_prod_qrcode.txtitem.setText(scanned_text);
                                    frm_fg_prod_qrcode.txtitem.setText(xtagsca);
                                    progressDialog.dismiss();
                                    onBackPressed();
                                }

                                frm_fg_prod_qrcode.btn_add.performClick();
                            }
                        }, 100);
                break;
            case "frm_quality_inspection" :
                scanned_text = ""+rawResult;
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                handler.clear_data();
                                frm_quality_inspection.list = handler.comman_get_data();
                                frm_quality_inspection.list2 = new ArrayList<>();
                                frm_quality_inspection.recycler2.setAdapter(frm_quality_inspection.adapter);
                                frm_quality_inspection.adapter2 = new quality_inspection_adapter(getApplicationContext(), frm_quality_inspection.list2);
                                frm_quality_inspection.recyclerView.setAdapter(frm_quality_inspection.adapter2);
                                frm_quality_inspection.adapter2.notifyDataSetChanged();
                                frm_quality_inspection.adapter.notifyDataSetChanged();
                                frm_quality_inspection.adapter2.notifyDataSetChanged();


                                fgen.xcard_view_name = "sale_order_schedule_booking_card_view";
                                frm_lead_enquiry.xfrm_lead_enquiry = "Y";
                                frm_lead_enquiry.xfg_sub_group_code = scanned_text;
                                if(fgen.mcd.equals("SVPL"))
                                    finapi.fill_record_in_listview_popup("EP715S");
                                else
                                    finapi.fill_record_in_listview_popup("EP715B");
                                frm_quality_inspection.txtscan.setText(scanned_text);
                                progressDialog.dismiss();
                                onBackPressed();
                            }
                        }, 100);
                break;
            case "frm_inter_stage_tfr" :
                scanned_text = ""+rawResult;
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                frm_inter_stage_tfr.xitem = "";
                                finapi.context = scannerView.this;
                                frm_lead_enquiry.xfrm_lead_enquiry = "Y";
                                frm_lead_enquiry.xfg_sub_group_code = scanned_text.split("___")[0];
                                ArrayList<team6> x = finapi.getApi6(fgen.mcd, "aInterStg_check","-", fgen.muname, fgen.cdt1.substring(6,10), "-", frm_lead_enquiry.xfg_sub_group_code);
                                boolean msg = finapi.RecieveDataFromApi(scannerView.this, x);
                                if(!msg)
                                {
                                    if(fgen.xpopup_col6.toUpperCase().contains("PLEASE FOLLOW FIFO"))
                                    {
                                        return;
                                    }
                                    else {
                                        onBackPressed();
                                    }
                                    return;
                                }
//                                ArrayList<String> x = finapi.fill_record_in_listview_popup("aInterStg_check");
                                frm_inter_stage_tfr.txtlabel.setText(scanned_text);
                                frm_inter_stage_tfr.txtstage_from.setText(fgen.xpopup_col2);
                                frm_inter_stage_tfr.txtbatch_no.setText(fgen.xpopup_col5);
                                frm_inter_stage_tfr.txtqty.setText(fgen.xpopup_col6);
                                frm_inter_stage_tfr.xitem = fgen.xpopup_col3 + "---" + fgen.xpopup_col4;
                                fgen.frm_request = "";
                                if(fgen.mcd.equals("ARUB")) {
                                    frm_inter_stage_tfr.btn_add.performClick();
                                }
                                progressDialog.dismiss();
                                onBackPressed();
                            }
                        }, 100);
                break;
            case "frm_fg_stacking":
                Handler mHandler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //   Update the value background thread to UI thread
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (frm_fg_stacking.btn_clicked.equals("frm_fg_stacking_location")) {
                                        frm_fg_stacking.txtlocation.setText("" + rawResult);
                                        onBackPressed();
                                    } else {
                                        String xtag = "";
                                        String xtag1 = "";
                                        ArrayList<team6> result = new ArrayList<>();
                                        if(fgen.btnid.equals("EP771"))
                                            result = finapi.getApi6(fgen.mcd, "aFGTAG_check", "" + rawResult.toString().trim(), fgen.muname, fgen.cdt1.substring(6, 10), "#", "FG173C");
                                        else if (fgen.mcd.equals("SGRP")) {
                                            result = finapi.getApi6(fgen.mcd, "aFGTAG_check", "" + rawResult.toString().trim(), fgen.muname, fgen.cdt1.substring(6, 10), "#", "FGSTACK");
                                        }
                                        else if (fgen.mcd.equals("GDOT") && fgen.btnid.equals("EP1207")){
//                                            result = finapi.getApi6(fgen.mcd, "aFGTAG_check", "" + rawResult.toString().trim().substring(0, 26), fgen.muname, fgen.cdt1.substring(6, 10), "#", "FGSTACK");
                                            result = finapi.getApi6(fgen.mcd, "aFGTAG_check", "" + rawResult.toString().trim(), fgen.muname, fgen.cdt1.substring(6, 10), "#", "FGSTACK");
                                        }
                                        else {
                                            if (fgen.btnid.equals("EP1501")) { // MVIN Received F.G. WIP Store
                                                result = finapi.getApi6(fgen.mcd, "aFGTAG_check", "" + rawResult.toString().trim(), fgen.muname, fgen.cdt1.substring(6, 10), "FGSTACK", frm_fg_stacking.xtype);
                                            } else {
                                                result = finapi.getApi6(fgen.mcd, "aFGTAG_check", "" + rawResult.toString().trim().substring(0, 26), fgen.muname, fgen.cdt1.substring(6, 10), "FGSTACK", frm_fg_stacking.xtype);
                                            }
                                        }
                                        Gson gson = new Gson();
                                        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {
                                        }.getType());
                                        String data = "";
                                        String xcol5 = "", xerror = "";
                                        try {
                                            JSONArray array = new JSONArray(listString);
                                            for (int i = 0; i < array.length(); i++) {
                                                JSONObject respObj = array.getJSONObject(i);
                                                data = respObj.getString("col1").trim();
                                                xtag = respObj.getString("col3").trim();
                                                xcol5 = respObj.getString("col5").trim();
                                                xtag1 = respObj.getString("col5");
                                                xerror = respObj.getString("col6").trim();   // for EP771 it's a qty of fg sticker
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (!data.equals("Success")) {
                                            new android.app.AlertDialog.Builder (com.finsyswork.erp.scannerView.this)
                                                    .setTitle ("Error")
                                                    .setMessage (xerror)
                                                    .setPositiveButton ("Ok", new DialogInterface.OnClickListener ( ) {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss ( );
                                                            onBackPressed ( );
                                                            return;
                                                        }
                                                    })
                                                    .show ( );
                                            return;
                                        }
                                        if (fgen.btnid.equals("EP771"))
                                            frm_fg_stacking.txt_reels.setText("" + xtag.trim() + " #~# " + xcol5 + " #~# " + xerror);
                                        if(fgen.btnid.equals("EP1501") && fgen.mcd.equals("MVIN")) {
                                            frm_fg_stacking_mvin.txt_reels.setText ("" + xtag.trim ( ));
                                        }
                                        else if(fgen.btnid.equals("EP1501"))
                                        {
                                            frm_fg_stacking.txt_reels.setText("" + xtag.trim());
                                        }
                                        else if (fgen.mcd.equals("GDOT") && fgen.btnid.equals("EP1207")){
//                                            frm_fg_stacking.txt_reels.setText("" + xtag.trim() + " #~# " + xcol5 + " #~# " + xerror);
                                            frm_fg_stacking.txt_reels.setText("" + xtag1.trim ( ).substring (8,25));
//                                            frm_fg_stacking.btn_add.performClick();
                                        }
                                        else
                                            frm_fg_stacking.txt_reels.setText("" + xtag.trim());
                                        if(fgen.mcd.equals("MVIN")){
                                            try{frm_fg_stacking_mvin.btn_add.performClick();}catch (Exception e){}
                                        }
                                        onBackPressed();
                                    }
                                }
                                catch (Exception e){
                                    new android.app.AlertDialog.Builder(com.finsyswork.erp.scannerView.this)
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
                            }
                        });

                    }
                }).start();
                break;

            case "frm_reel_stacking":
                if(frm_reels_stacking.btn_clicked.equals("frm_reel_stacking_location")) {
                    frm_reels_stacking.txtlocation.setText("" + rawResult);
                    onBackPressed();
                }
                else
                {
                    String xreel_no = "";
                    xicode = "";
                    ArrayList<team6> result = finapi.getApi6(fgen.mcd, "areel_check", "" + rawResult.toString().trim(), fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
                    Gson gson = new Gson();
                    String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
                    String data = "";
                    String xcol5 = "";
                    try {
                        JSONArray array = new JSONArray(listString);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject respObj = array.getJSONObject(i);
                            data = respObj.getString("col1").trim();
                            xicode = respObj.getString("col2").trim();
                            xreel_no = respObj.getString("col3").trim();
                            xcol5 = respObj.getString("col5").trim();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String xmess = "Sorry, Reel Is Invalid!!";
                    if(fgen.btnid.equals("EP743"))
                        xmess = "Sorry, Scanned QR Is Invalid!!";
                    if (!data.equals("Success")) {
                        new android.app.AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage(xmess)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        onBackPressed();
                                        return;
                                    }
                                })
                                .show();
                        return;
                    }
                    String  xscanned_text = xicode +  "#~#" + xreel_no;
                    if(fgen.btnid.equals("EP743"))
                        if(xcol5.length() < 40)
                            xscanned_text = xicode +  "#~#" + xreel_no;
                        else
                            xscanned_text = xcol5;
                    frm_reels_stacking.txt_reels.setText("" + xscanned_text.trim());
                    onBackPressed();
                }
                break;
            case "Login":
                ArrayList<team> result = finapi.getApi(Login._CocdText.getText().toString().trim(), "loginScan", "" + rawResult, "-", "-", "-");
                Gson gson = new Gson();
                String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
                String xuser_name = "";
                String xpassword = "";
                try {
                    JSONArray array = new JSONArray(listString);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject respObj = array.getJSONObject(i);
                        xuser_name = respObj.getString("col2");
                        xpassword = respObj.getString("col3");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Login._emailText.setText(xuser_name);
                Login._passwordText.setText(xpassword);
                onBackPressed();
                Login._loginButton.performClick();
                break;
            case "request_material_issue":
            case "request_material_return":
            case "request_material_purchase":
                xicode = "" + rawResult;
                if(fgen.mcd.equals("SVPL"))
                {
                    try {
                        xicode = xicode.substring(8, 18).toString();
                    }catch (Exception e){
                        xicode = ""+ rawResult.toString().trim().substring(8,xicode.length());
                    }
                }
                x = finapi.getApi6(fgen.mcd, "areel_check",xicode, fgen.muname, fgen.cdt1.substring(6,10), "#", "#");
                msg = finapi.RecieveDataFromApi(scannerView.this, x);
                xicode = fgen.xpopup_col2 +"----"+ fgen.xpopup_col4;
                duplicate = handler.CheckDuplicacy("col1", "" + xicode.trim());
                if(duplicate)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Sorry, Duplicate Items Are Not Allowed!!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    onBackPressed();
                                }
                            });
                    alertDialog.show();
                    return;
                }
                if(fgen.xpopup_col1.equals("Failure"))
                {
                    AlertDialog alert = new AlertDialog.Builder(this).create();
                    alert.setTitle("Error");
                    alert.setMessage(fgen.xpopup_col6);
                    alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    onBackPressed();
                                }
                            });
                    alert.show();
                    return;
                }
                frm_material_issue_req.txtitem.setText(xicode.trim());
                onBackPressed();
                break;
            case "frm_dispatch_entry":
                //*******//-------->gdot-despatch--work<------//
                String xtag = "";
                String xtag1 = "";
                String xtag2 = "";
                //*******//-------->gdot-despatch--work<------//
                if(frm_dispatch_entry.txtitem.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select Item First!!", Toast.LENGTH_LONG).show();
                    onBackPressed();
                    return;
                }
                if(frm_dispatch_entry.txtcustomer_name.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select Customer First!!", Toast.LENGTH_LONG).show();
                    onBackPressed();
                    return;
                }
                if(fgen.mcd.equals("SGRP")) {
                    xicode = fgen.scanned_qr_Code.substring(0, 8).toString().trim();
                }
                if (fgen.mcd.equals ("ARUB")) {
                    xicode = fgen.scanned_qr_Code.substring(0, 8).toString().trim();
                }

                else {
                    if(fgen.mcd.equals("MVIN")){
                        x = finapi.getApi6(fgen.mcd, "areel_FGRcheck", "" + rawResult, fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
                        gson = new Gson();
                        listString = gson.toJson(x, new TypeToken<ArrayList<team>>() {}.getType());
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
                            new android.app.AlertDialog.Builder(this)
                                    .setTitle("Error")
                                    .setMessage("Sorry, Scanned BarCode Is Invalid!!")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public   void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            onBackPressed();
                                            return;
                                        }
                                    })
                                    .show();
                            return;
                        }
                    }
                    //*******//--------><------//
                    if (fgen.mcd.equals ("GDOT") && fgen.btnid.equals ("EP1212")) {
                        xicode = fgen.scanned_qr_Code.substring(0, 17).toString().trim();
                        ArrayList<team6> result1 = new ArrayList<> ( );
//                        result1 = finapi.getApi6 (fgen.mcd, "aFGTAG_check", "" + rawResult.toString ( ).trim ( ).substring (0, 26), fgen.muname, fgen.cdt1.substring (6, 10), "#", "EP1212");
                        result1 = finapi.getApi6 (fgen.mcd, "aFGTAG_check", "" + rawResult.toString ( ).trim (), fgen.muname, fgen.cdt1.substring (6, 10), "#", "EP1212");
                        gson = new Gson ( );
                        listString = gson.toJson (result1, new TypeToken<ArrayList<team>> ( ) {
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
                                xtag1 = respObj.getString ("col5");
                                xerror = respObj.getString ("col6").trim ( );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace ( );
                        }
                        if (!data.equals ("Success")) {
                            new android.app.AlertDialog.Builder (com.finsyswork.erp.scannerView.this)
                                    .setTitle ("Error")
                                    .setMessage (xerror)
                                    .setPositiveButton ("Ok", new DialogInterface.OnClickListener ( ) {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss ( );
                                            onBackPressed ( );
                                            return;
                                        }
                                    })
                                    .show ( );

                            return;
                        }

                    }
                    //*******//-------->gdot-despatch--work<------//
                    else {
                        try {
                            if(fgen.mcd.equals("MIND")) {
                                xicode = fgen.scanned_qr_Code.substring(18, 26).toString().trim();
                            }
//                            else
//                                xicode = fgen.scanned_qr_Code.substring(20, 28).toString().trim();
                        } catch (Exception e) {
                            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                            alertDialog.setTitle("Error Found, Please Try Again!");
                            alertDialog.setMessage(fgen.scanned_qr_Code);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            onBackPressed();
                                        }
                                    });
                            alertDialog.show();
                            return;
                        }
                    }
                }

                String xform_item =frm_dispatch_entry.txtitem.getText().toString().split("---")[0].trim();
                if(!xform_item.equals(xicode))
                {
                    //________gdot despatch------------work-----------------------//
                    if (fgen.mcd.equals ("GDOT") && fgen.btnid.equals ("EP1212")) {
                           frm_dispatch_entry.txttag.setText ("" + xtag1.trim());
//                         frm_dispatch_entry.txt_barcode.setText ("" + xtag.trim ());
                           frm_dispatch_entry.txtqty.setText ("" + xtag2.trim ());
                           frm_dispatch_entry.btchno_ss.setText ("" + xtag.trim ());
                           onBackPressed ( );

                    }
                    //________gdot despatch------------work-----------------------//
                   else {
                        AlertDialog alertDialog = new AlertDialog.Builder (this).create ( );
                        alertDialog.setTitle ("Error");
                        alertDialog.setMessage ("Sorry, Scanned QR Code Is Invalid!!");
                        alertDialog.setButton (AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener ( ) {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss ( );
                                        onBackPressed ( );
                                    }
                                });
                        alertDialog.show ( );
                        return;
                   }
                    return;
                }
                if (fgen.mcd.equals ("GDOT") && fgen.btnid.equals ("EP1212")) {
                    frm_dispatch_entry.txttag.setText ("" + xtag1.trim ( ));
                }
//                else
//                frm_dispatch_entry.txttag.setText(fgen.scanned_qr_Code);
                if(fgen.mcd.equals("SGRP")) {
                    x = finapi.getApi6(fgen.mcd, "aSGFG_check", fgen.scanned_qr_Code.trim(), fgen.muname, fgen.cdt1.substring(6, 10), "#", fgen.scanned_qr_Code.trim());
                    msg = finapi.RecieveDataFromApi(scannerView.this, x);
                    if(!msg){return;}
                    frm_dispatch_entry.txtqty.setText(fgen.xpopup_col6);
                }
                String iname_ss ="";
                String qnty_ss = "";
                String btchno_ss = "";
                if (fgen.mcd.equals ("ARUB") && fgen.btnid.equals ("EP824")) {
//                    frm_dispatch_entry.txttag.setText(xicode);
//                  x = finapi.getApi6(fgen.mcd, "aSGFG_check", fgen.scanned_qr_Code.trim(), fgen.muname, fgen.cdt1.substring(6, 10), "#", fgen.scanned_qr_Code.trim());
                    x = finapi.getApi6 (fgen.mcd, "aFGTAG_check", "" +fgen.scanned_qr_Code.trim(), fgen.muname, fgen.cdt1.substring (6, 10), "#", "EP824");
                    gson = new Gson();
                    listString = gson.toJson(x, new TypeToken<ArrayList<team>>() {}.getType());
                    String data= "";
                    try {
                        JSONArray array = new JSONArray(listString);
                        for(int i=0; i< 1; i++){
                            JSONObject respObj = array.getJSONObject(i);
                            data = respObj.getString("col1");
                            xicode = respObj.getString("col2");
                            btchno_ss = respObj.getString("col3");
//                            iname_ss = respObj.getString("col4");
                            qnty_ss =  respObj.getString("col6");
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(!data.equals("Success"))
                    {
                        new android.app.AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage("Sorry, Scanned BarCode Is Invalid!!")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public   void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        onBackPressed();
                                        return;
                                    }
                                })
                                .show();
                        return;
                    }
//                    msg = finapi.RecieveDataFromApi(scannerView.this, x);
//                    if(!msg){return;}
                    frm_dispatch_entry.txttag.setText(btchno_ss.trim ());
                    frm_dispatch_entry.txtqty.setText(qnty_ss.trim());
                    frm_dispatch_entry.txtqty.requestFocus();
                    onBackPressed ();
                }
                else {
                    try {
                        if(!fgen.mcd.equals("MVIN")){
                            int count=0; String _underscore="";
                            count = StringUtils.countMatches(fgen.scanned_qr_Code, "_");
                            for(int i=0; i< count; i++){
                                _underscore +="_";
                            }
                            frm_dispatch_entry.txtqty.setText(fgen.scanned_qr_Code.split(_underscore)[1]);
                        }
                    }catch (Exception  e){
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage(fgen.scanned_qr_Code);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        onBackPressed();
                                    }
                                });
                        alertDialog.show();
                        return;
                    }
                    onBackPressed();
                }
//                 onBackPressed();
//                frm_dispatch_entry.btn_add.performClick();
                break;
            case "frm_start_production":
                MyProgressdialog progressDialog = new MyProgressdialog(this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                if(frm_start_production.xbtn_clicked.equals("btn_ScanMachine"))
                                {
                                    frm_start_production.txtmachine.setText(""+rawResult);
                                    onBackPressed();
                                }
                                else{
                                    String xitem = fgen.scanned_qr_Code.split(Pattern.quote("|"))[0].trim();
                                    String xbom_number = fgen.scanned_qr_Code.split(Pattern.quote("|"))[1].trim();
                                    String xerp_Code = fgen.scanned_qr_Code.split(Pattern.quote("|"))[2].trim();

                                    frm_start_production.txtitem.setText(xitem);
                                    frm_start_production.txtworkorder_no.setText(xbom_number);
                                    frm_start_production.txtdrawing_no.setText(xerp_Code);
                                    onBackPressed();
                                }
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 100);
                break;
            case "frm_finish_production":
                progressDialog = new MyProgressdialog(this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                if(frm_finish_production.xbtn_clicked.equals("btn_ScanMachine"))
                                {
                                    frm_finish_production.txtmachine.setText(""+rawResult);
                                    onBackPressed();
                                }
                                else{
                                    String xitem = fgen.scanned_qr_Code.split(Pattern.quote("|"))[0].trim();
                                    String xbom_number = fgen.scanned_qr_Code.split(Pattern.quote("|"))[1].trim();
                                    String xerp_Code = fgen.scanned_qr_Code.split(Pattern.quote("|"))[2].trim();

                                    frm_finish_production.txtitem.setText(xitem);
                                    frm_finish_production.txtworkorder_no.setText(xbom_number);
                                    frm_finish_production.txtdrawing_no.setText(xerp_Code);
                                    onBackPressed();
                                }
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 100);
                break;
            default:
                break;
        }

    }

    private void multi_reel_single_job_switch(String rawResult) {
        if(frm_paper_end.btn_clicked.equals("job"))
        {
            if(!xraw_variable.equals("Y")) {
                onBackPressed();
            }
            return;
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
                            onBackPressed();
                        }
                    });
            alertDialog.show();
            return;
        }

        if(fgen.frm_request.equals("frm_reel_issue_return"))
        {
            xraw_variable = "Y";
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("REEL Qty");
            alertDialog.setMessage("Enter Qty");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setText(frm_job_issue_entry.xreel_qty);
            alertDialog.setView(input);
            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            qty = input.getText().toString();
                            if (qty.equals("")) {
                                frm_job_issue_entry.xreel_qty = "0";
                            } else {
                                if(Float.parseFloat(qty) > Float.parseFloat(frm_job_issue_entry.xreel_qty))
                                {
                                    Toast.makeText(getApplicationContext(), "Return Qty Should Not Be Greater Than Reel Issue Qty!!", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                    return;
                                }
                                frm_job_issue_entry.xreel_qty = qty;
                            }
                            frm_paper_end.issue_req_model = new issue_req_model("1", ""+xbuild_qr_name, frm_job_issue_entry.xreel_qty, ""+rawResult.trim());
                            onBackPressed();
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        frm_paper_end.issue_req_model = new issue_req_model("1", ""+xbuild_qr_name, frm_job_issue_entry.xreel_qty, ""+rawResult.trim());
        if(!xraw_variable.equals("Y")) {
            onBackPressed();
        }
        return;
    }

    public void scanned_job_verification(String rawResult) {
        xraw_variable = "";
        ArrayList<team6> result = finapi.getApi6(fgen.mcd, "ajobc_check",""+rawResult.trim(), fgen.muname, fgen.cdt1.substring(6,10), "#", "#");
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
        String data= "";
        try {
            JSONArray array = new JSONArray(listString);
            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                data = respObj.getString("col1");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!data.equals("Success"))
        {
            xraw_variable = "Y";
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Sorry, JOB Card Is Invalid!!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            xraw_variable = "";
                            dialog.dismiss();
                            onBackPressed();
                            return;
                        }
                    })
                    .show();
            return;
        }
        else {
            try {
                frm_paper_end.txtscan_job.setText("" + rawResult.trim());
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
    }


    // work pending for MCPL in this function
    public void multi_reel_single_job_for_paper_end(String rawResult) {
        xraw_variable = "";
        ArrayList<team6> result = finapi.getApi6(fgen.mcd, ""+frm_paper_end.xreel_check_api,""+rawResult.trim(), fgen.muname, fgen.cdt1.substring(6,10), "#", frm_paper_end.xscanned_job_no);
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
        String data= "";
        try {
            JSONArray array = new JSONArray(listString);
            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                data = respObj.getString("col1");
                try {
                    frm_paper_end.xicode = respObj.getString("col2");
                    frm_job_issue_entry.xreel_qty = respObj.getString("col6");
                    String xcol4 =  respObj.getString("col4");
                    xbuild_qr_name = frm_paper_end.xicode +"---"+ xcol4 +"---"+rawResult;
                }catch (Exception e){}
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!data.equals("Success") || data.equals(""))
        {
            xraw_variable = "Y";
            new android.app.AlertDialog.Builder(com.finsyswork.erp.scannerView.this)
                    .setTitle(Html.fromHtml("<font color='#F80303'>Error</font>"))
                    .setMessage(Html.fromHtml("<font color='#F80303'>" +frm_job_issue_entry.xreel_qty +"\"!!\"</font>"))
                    .setIcon(R.drawable.fail)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            xraw_variable = "";
                            dialog.dismiss();
                            onBackPressed();
                            return;
                        }
                    })
                    .show();
            return;
        }
        else {
            try {
                frm_paper_end.txtscan_reel.setText("" + xbuild_qr_name);
            } catch (Exception e) {
            }
            try {
                frm_reel_issue_return.txtscan_reel.setText("" + xbuild_qr_name);
            } catch (Exception e) {
            }
            try {
                frm_job_issue_entry.txtscan_reel.setText("" + xbuild_qr_name);
                frm_job_issue_entry.txtreel_qty.setText(frm_job_issue_entry.xreel_qty);
                try {
                    frm_lead_enquiry.xcol6_udf_data = xbuild_qr_name.split("---")[0].trim();
                }catch (Exception e){progressDialog.dismiss(); return;}
                frm_job_issue_entry.xjob = finapi.fill_record_in_listview_popup("EP735");
            } catch (Exception e) {
            }
        }
    }


    public void multi_reel_single_job(String rawResult) {
        final MyProgressdialog progressDialog = new MyProgressdialog(scannerView.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        xraw_variable = "";
                        ArrayList<team6> result = finapi.getApi6(fgen.mcd, ""+frm_paper_end.xreel_check_api,""+rawResult.trim(), fgen.muname, fgen.cdt1.substring(6,10), "#", frm_paper_end.xscanned_job_no);
                        Gson gson = new Gson();
                        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
                        String data= "";
                        try {
                            JSONArray array = new JSONArray(listString);
                            for(int i=0; i< array.length(); i++){
                                JSONObject respObj = array.getJSONObject(i);
                                data = respObj.getString("col1");
                                try {
                                    frm_paper_end.xicode = respObj.getString("col2");
                                    frm_job_issue_entry.xreel_qty = respObj.getString("col6");
                                    String xcol4 =  respObj.getString("col4");
                                    xbuild_qr_name = frm_paper_end.xicode +"---"+ xcol4 +"---"+rawResult;
                                }catch (Exception e){}
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(!data.equals("Success") || data.equals(""))
                        {
                            xraw_variable = "Y";
                            new android.app.AlertDialog.Builder(com.finsyswork.erp.scannerView.this)
                                    .setTitle(Html.fromHtml("<font color='#F80303'>Error</font>"))
                                    .setMessage(Html.fromHtml("<font color='#F80303'>" +frm_job_issue_entry.xreel_qty +"\"!!\"</font>"))
                                    .setIcon(R.drawable.fail)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            xraw_variable = "";
                                            dialog.dismiss();
                                            onBackPressed();
                                            return;
                                        }
                                    })
                                    .show();
                            return;
                        }
                        else {
                            try {
                                frm_paper_end.txtscan_reel.setText("" + xbuild_qr_name);
                                frm_paper_end.issue_req_model = new issue_req_model("1", ""+xbuild_qr_name, frm_job_issue_entry.xreel_qty, ""+rawResult.trim());
                            } catch (Exception e) {
                            }
                            try {
                                frm_reel_issue_return.txtscan_reel.setText("" + xbuild_qr_name);
                            } catch (Exception e) {
                            }
                            try {
                                frm_job_issue_entry.txtscan_reel.setText("" + xbuild_qr_name);
                                frm_job_issue_entry.txtreel_qty.setText(frm_job_issue_entry.xreel_qty);
                                try {
                                    frm_lead_enquiry.xcol6_udf_data = xbuild_qr_name.split("---")[0].trim();
                                }catch (Exception e){progressDialog.dismiss(); return;}
                                frm_job_issue_entry.xjob = finapi.fill_record_in_listview_popup("EP735");
                            } catch (Exception e) {
                            }
                        }                progressDialog.dismiss();
                    }
                }, 100);
    }

    private void xcheck_qr_code_for_bin_scanning_only(String qr_code)
    {
        try{
            qr_code.substring(0,14);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Please Scan Right QR Code!!", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
        ArrayList<team6> result = finapi.getApi6(fgen.mcd, "aCure_check",""+qr_code.substring(0,14), fgen.muname, fgen.cdt1.substring(6,10), "#", "#");
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team6>>() {}.getType());
        try {
            JSONArray array = new JSONArray(listString);
            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                frm_bin_scanning.xcol1 = respObj.getString("col1");
                frm_bin_scanning.xcol2 = respObj.getString("col2");
                frm_bin_scanning.xcol3 = respObj.getString("col3");
                frm_bin_scanning.xcol4 = respObj.getString("col4");
                frm_bin_scanning.xcol5 = respObj.getString("col5");
                frm_bin_scanning.xcol6 = respObj.getString("col6");

                frm_bin_scanning.txtcol1.setText(frm_bin_scanning.xcol1);
                frm_bin_scanning.txtcol2.setText(frm_bin_scanning.xcol2);
                frm_bin_scanning.txtcol3.setText(frm_bin_scanning.xcol3);
                frm_bin_scanning.txtcol4.setText(frm_bin_scanning.xcol4);
                frm_bin_scanning.txtcol5.setText(frm_bin_scanning.xcol5);
                frm_bin_scanning.txtcol6.setText(frm_bin_scanning.xcol6);

                if(frm_bin_scanning.txtcol1.getText().toString().toLowerCase().contains("ok to tfr"))
                {
                    frm_bin_scanning.txtcol1.setBackgroundResource(R.color.success);
                    frm_bin_scanning.bin_imageView.setBackgroundResource(R.drawable.success);
                }
                else
                {
                    frm_bin_scanning.bin_imageView.setBackgroundResource(R.drawable.fail);
                    frm_bin_scanning.txtcol1.setBackgroundResource(R.color.danger);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(frm_bin_scanning.xcol1.equals("Failure"))
        {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Sorry, QR Code Is Invalid!!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            onBackPressed();
                            return;
                        }
                    })
                    .show();
            return;
        }
        else {
            String post_param = "";
            post_param += frm_bin_scanning.xbranch + frm_bin_scanning.xseperator + frm_bin_scanning.xtype + frm_bin_scanning.xseperator
                    + frm_bin_scanning.xcol1 + frm_bin_scanning.xseperator + frm_bin_scanning.xcol2 + frm_bin_scanning.xseperator
                    + frm_bin_scanning.xcol3 + frm_bin_scanning.xseperator + frm_bin_scanning.xcol4 + frm_bin_scanning.xseperator
                    + frm_bin_scanning.xcol5 + frm_bin_scanning.xseperator + frm_bin_scanning.xcol6 + "!~!~!";
            ArrayList<team> res = finapi.getApi(fgen.mcd, "aCure_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
            boolean msg = finapi.showAlertMessageForBinScanning(this, res);
            if(!msg){
                return;
            }
        }
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
                        qty = input.getText().toString();
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

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    private boolean showAlert(String item,String max_qty){
        final boolean[] move_or_not = {false};
        qty = "";
        if(frm_issue_slip.requested_qty.equals(""))
        {
            move_or_not[0] = true;
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Error");
            if(fgen.mcd.equals("SGRP")){alert.setMessage("Sorry, Scanned QR Code Item Not In Request Slip.!!");}
            else{
                alert.setMessage("Sorry, Item Not Found Against This Issue Req.!!");
            }
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            onBackPressed();
                        }
                    });
            alert.show();
        }
        else if(fgen.xpopup_col6.toUpperCase().contains("PLEASE FOLLOW FIFO")){
            move_or_not[0] = true;
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle(fgen.xpopup_col2);
            alert.setMessage(fgen.xpopup_col6);
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            onBackPressed();
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
                                Toast.makeText(getApplicationContext(), "NULL Value Could Not Allowed!!", Toast.LENGTH_LONG).show();
                                move_or_not[0] = true;
                                Toast.makeText(getApplicationContext(), "Invalid Qty!!", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                float max = Float.parseFloat(max_qty);
                                float enter_qty = Float.parseFloat(qty);
                                if (enter_qty > max && !frm_issue_slip.reel_wise_lot.equals("3")) {
                                    move_or_not[0] = true;
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Qty Should Not Be Greater Than Batch Qty!!", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                } else {
                                    issue_req_model = new issue_req_model("1", item, qty);
                                    handler.issue_slip_insert_data(issue_req_model);
                                    move_or_not[0] = true;
                                    dialog.dismiss();
                                    onBackPressed();
                                }
                            }
                        }
                    });
            alertDialog.show();
        }
        if (move_or_not[0]) {
            return  true;
        } else {
            return false;
        }
    }
}