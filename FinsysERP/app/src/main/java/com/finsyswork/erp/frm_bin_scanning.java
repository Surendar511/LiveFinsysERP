package com.finsyswork.erp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class frm_bin_scanning extends AppCompatActivity {

    Button btn_scan, btn_cancel, btn_add, btn_save,btn_new, btn_exit;
    public static EditText txt_bin;
    public static RecyclerView recycler;
    public static TextView txtcol1, txtcol2, txtcol3, txtcol4, txtcol5, txtcol6;
    public static ArrayList<scanner_model> modellList = new ArrayList<>();
    public static myAdapter adapter;
    sqliteHelperClass handler;
    Toolbar toolbar;
    public static String reel_scan_result = "", btn_clicked = "";
    public static String xseperator = "#~#", xbranch="00", xtype ="BS";
    public static String xcol1 = "", xcol2 = "", xcol3 = "0", xcol4="",xcol5 = "-", xcol6="";
    public static ImageView bin_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_bin_scanning);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        fgen.frm_request = "frm_bin_scanning";
        handler = new sqliteHelperClass(this);
        finapi.setColorOfStatusBar(frm_bin_scanning.this);

        finapi.context = frm_bin_scanning.this;
        finapi.deleteJsonResponseFile();

        btn_scan = findViewById(R.id.btn_scanner);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_exit = findViewById(R.id.btn_exit);
        btn_add = findViewById(R.id.btn_add);
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        txt_bin = findViewById(R.id.txt_bin);
        recycler = findViewById(R.id.listItems);
        toolbar = findViewById(R.id.toolbar);
        txtcol1 = findViewById(R.id.txtcol1);
        txtcol2 = findViewById(R.id.txtcol2);
        txtcol3 = findViewById(R.id.txtcol3);
        txtcol4 = findViewById(R.id.txtcol4);
        txtcol5 = findViewById(R.id.txtcol5);
        txtcol6 = findViewById(R.id.txtcol6);
        bin_imageView = findViewById(R.id.bin_imageView);

        handler = new sqliteHelperClass(getApplicationContext());
        btn_cancel.setText("EXIT");
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        disableViewsMethod();
        btn_scan.setEnabled(true); // extra line added.
        setSupportActionBar(toolbar);
        handler.clear_data();
        adapter = new myAdapter(getApplicationContext(), modellList);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        recycler.setAdapter(adapter);

        txt_bin.requestFocus();

        txt_bin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == 19 || keyCode == 21)) {
                    if (txt_bin.getText().toString().trim().length() > 2) {
                        reel_scan_result = "";
                        btn_clicked = "frm_bin_scanning";
//                        newBtnClickedMethod();
                        fill_data(txt_bin.getText().toString());
//                          scannerView.multi_reel_single_job(txtscan_reel.getText().toString());
                        txt_bin.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });



        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_bin.requestFocus();
                reel_scan_result = "";
                btn_clicked = "frm_bin_scanning";
                newBtnClickedMethod();
                startActivity(new Intent(getApplicationContext(), scannerView.class));
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_bin.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Scan Bin First!!", Toast.LENGTH_LONG).show();
                    return;
                }
                boolean duplicate = handler.CheckDuplicacy("col1", ""+reel_scan_result);
                if(duplicate)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(frm_bin_scanning.this).create();
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
                scanner_model m = new scanner_model("1", reel_scan_result);
                handler.insert_data(m);
                modellList = handler.get_data();
                adapter = new myAdapter(getApplicationContext(), modellList);
                recycler.setAdapter(adapter);

                txt_bin.setText("");
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

                txt_bin.requestFocus();
                txt_bin.setBackgroundResource(R.color.light_blue);
                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn_text = btn_cancel.getText().toString();
                if(btn_text == "CANCEL")
                {
                    handler.clear_data();
                    btn_cancel.setText("EXIT");
                    modellList = new ArrayList<>();
                    adapter = new myAdapter(getApplicationContext(), modellList);
                    recycler.setAdapter(adapter);

                    txt_bin.setBackgroundResource(R.color.light_grey);
                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_bin_scanning.this, "aBscan_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_bin_scanning.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modellList = handler.get_data();
                if(modellList.size() <=0)
                {
                    Toast.makeText(getApplicationContext(), "Invalid Data!!", Toast.LENGTH_LONG).show();
                    return;
                }

                final MyProgressdialog progressDialog = new MyProgressdialog(frm_bin_scanning.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = "";
                                for(scanner_model m : modellList)
                                {
                                    String xmv_reels = m.getQr_Code().toString();
                                    post_param += xbranch + xseperator + xtype + xseperator
                                            + xmv_reels + xseperator + "!~!~!";
                                }
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aBscan_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                boolean msg = finapi.showAlertMessage(frm_bin_scanning.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                handler.clear_data();
                                modellList = new ArrayList<>();
                                adapter = new myAdapter(getApplicationContext(), modellList);
                                recycler.setAdapter(adapter);

                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    private void fill_data(String rawResult) {
        try {
            if(frm_bin_scanning.btn_clicked.equals("frm_bin_scanning")) {
                frm_bin_scanning.reel_scan_result = ""+rawResult;
                frm_bin_scanning.txt_bin.setText("" + rawResult);
                xcheck_qr_code_for_bin_scanning_only(""+rawResult);
                return;
            }
        }catch (Exception e){}
    }

    private void xcheck_qr_code_for_bin_scanning_only(String qr_code)
    {
        try{
            qr_code.substring(0,14);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Please Scan Right QR Code!!", Toast.LENGTH_LONG).show();
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
                    frm_bin_scanning.txtcol1.setBackgroundResource(R.color.danger);
                    frm_bin_scanning.bin_imageView.setBackgroundResource(R.drawable.fail);
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
                            txt_bin.setText("");
                            txt_bin.requestFocus();
                            dialog.dismiss();
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
            boolean msg = finapi.showAlertMessageForBinScanninge(this, res);
            if(!msg){
                return;
            }
        }
    }


    private void disableViewsMethod() {
        btn_scan.setEnabled(false);
        btn_add.setEnabled(false);
    }

    private void enabledViewMethod() {
        btn_scan.setEnabled(true);
        btn_add.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txt_bin.setText("");
        txtcol1.setText("");
        txtcol2.setText("");
        txtcol3.setText("");
        txtcol4.setText("");
        txtcol5.setText("");
        txtcol6.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            modellList = handler.get_data();
        }catch (Exception e){}
        adapter = new myAdapter(getApplicationContext(), modellList);
        Log.d("size : ", ""+modellList.size());
        recycler.setAdapter(adapter);
    }
    
}