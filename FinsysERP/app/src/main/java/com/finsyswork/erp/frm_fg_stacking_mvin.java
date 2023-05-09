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
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class frm_fg_stacking_mvin extends AppCompatActivity {

    public static TextView txtscanner;
    LinearLayout linearLocation;
    public static Button btn_scan, btn_cancel, btn_location, btn_add, btn_save,btn_new, btn_read, btn_auto_save_data;
    public static EditText txtlocation, txt_reels, txt_barcode;
    public static RecyclerView recycler;
    public static ArrayList<scanner_model> modellList = new ArrayList<>();
    public static myAdapter adapter;
    sqliteHelperClass handler;
    Toolbar toolbar;
    public static String reel_scan_result = "", btn_clicked = "", xreel_iname="";
    public static String xseperator = "#~#", xbranch="00", xtype ="QC";
    public static String xicode = "", xapi_name="aFGTAG_stack";
    ArrayList<String> xselectreel = new ArrayList<>();
    ArrayList<String> xselectlocation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_fg_stacking_mvin);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_fg_stacking_mvin.this;
        finapi.deleteJsonResponseFile();

        final MyProgressdialog progressDialog = new MyProgressdialog(frm_fg_stacking_mvin.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(!fgen.mcd.equals("SVPL"))
                        {xselectlocation = finapi.fill_record_in_listview_popup("EP820A");}
                        new Thread(new Runnable() {
                            public void run() {
                                frm_lead_enquiry.xfrm_lead_enquiry = "N";
                                // a potentially time consuming task
                                if(fgen.btnid.equals("EP1207")) { xtype ="QC"; xapi_name="aFGTAG_stack";}   //SVPL
                                else if(fgen.btnid.equals("EP771"))   //SGRP Receive FG Wip
                                {
                                    xtype = "17";
                                    xapi_name = "aFG173C_ins";
                                }
                                else{ xtype = "FG";   xapi_name = "aFGTAG_stack";}
                            }
                        }).start();

                        progressDialog.dismiss();
                    }
                }, 100);
//        xselectreel = finapi.fill_record_in_listview_popup("EP826");

        handler = new sqliteHelperClass(this);
        finapi.setColorOfStatusBar(frm_fg_stacking_mvin.this);

        btn_scan = findViewById(R.id.btn_scanner);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_add = findViewById(R.id.btn_add);
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_read = findViewById(R.id.btn_read);
        btn_location = findViewById(R.id.btn_location);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_auto_save_data = findViewById(R.id.btn_auto_save_data);
        txtscanner = findViewById(R.id.txtscanner);
        txtlocation = findViewById(R.id.txt_location);
        txt_reels = findViewById(R.id.txt_reels);
        recycler = findViewById(R.id.listItems);
        toolbar = findViewById(R.id.toolbar);
        txt_barcode = findViewById(R.id.txt_barcode);
        linearLocation = findViewById(R.id.linearLocation);
        handler = new sqliteHelperClass(getApplicationContext());
        btn_cancel.setText("EXIT");


        toolbar.setTitle(fgen.rptheader);

        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        txtlocation.setKeyListener(null);
        txt_reels.setKeyListener(null);
        disableViewsMethod();
        setSupportActionBar(toolbar);
        handler.clear_data();
        adapter = new myAdapter(getApplicationContext(), modellList);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        recycler.setAdapter(adapter);

        if(fgen.mcd.equals("SVPL")){
            linearLocation.setVisibility(View.GONE);
        }

        txtlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_fg_stacking_mvin.this, xselectlocation, "txtlocation");
                txt_barcode.requestFocus();
            }
        });

        txt_barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == 19 || keyCode == 21)) {
                    if (txt_barcode.getText().toString().trim().length() > 2) {
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        String xmv_location = txtlocation.getText().toString().split("---")[0].trim();
                                        if(xmv_location.isEmpty() && !fgen.mcd.equals("SVPL"))
                                        {
                                            Toast.makeText(getApplicationContext(), "Please Select Location First!!", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        // scannerView.multi_reel_single_job(txtscan_reel.getText().toString());
                                        boolean xcheck_reel_status = reel_stacking_for_grid(txt_barcode.getText().toString().trim());
                                        if(xcheck_reel_status) {
                                            fillGrid();
                                        }
                                        txt_barcode.setText("");
                                        txt_barcode.requestFocus();
                                    }
                                }, 100);
                        return true;
                    }
                }
                return false;
            }
        });

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fgen.frm_request = "frm_fg_stacking_mvin";
                btn_clicked = "frm_fg_stacking_mvin_location";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
                txt_barcode.requestFocus();
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String xmv_location = txtlocation.getText().toString().split("---")[0].trim();
                if(xmv_location.isEmpty() && !fgen.mcd.equals("SVPL"))
                {
                    Toast.makeText(getApplicationContext(), "Please Select Location First!!", Toast.LENGTH_LONG).show();
                    return;
                }

                fgen.frm_request = "frm_fg_stacking";
                btn_clicked = "frm_fg_stacking_tag";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
                txt_barcode.requestFocus();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillGrid();
                txt_barcode.requestFocus();
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

                modellList = handler.get_auto_save_data_fg_stacking();
                if(!modellList.isEmpty()) {
                    new android.app.AlertDialog.Builder(frm_fg_stacking_mvin.this)
                            .setTitle("MESSAGE")
                            .setMessage("Data Found In AutoSaved Table, So Do You Want To Fill Data From AutoSaved Table OR NOT?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    autoSavedDataFillMethod();
                                    txt_barcode.requestFocus();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    handler.clear_auto_save_data_fg_stacking();
                                    txt_barcode.requestFocus();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);
                txtlocation.setBackgroundResource(R.color.light_blue);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn_text = btn_cancel.getText().toString();
                if(btn_text == "CANCEL")
                {
                    newBtnClickedMethod();
                    handler.clear_auto_save_data_fg_stacking();  // auto save
                    handler.clear_data();
                    btn_cancel.setText("EXIT");
                    modellList = new ArrayList<>();
                    adapter = new myAdapter(getApplicationContext(), modellList);
                    recycler.setAdapter(adapter);
                    btn_new.setEnabled(true);
                    txtlocation.setBackgroundResource(R.color.light_grey);

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });

        btn_auto_save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.app.AlertDialog.Builder(frm_fg_stacking_mvin.this)
                        .setTitle("Warning!!")
                        .setMessage("Are Your Sure, You Want To Do Clear Auto Saved Data!!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.clear_auto_save_data_fg_stacking();
                                txt_barcode.requestFocus();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txt_barcode.requestFocus();
                                dialog.dismiss();
                            }
                        })
                        .show();
                txt_barcode.requestFocus();
            }
        });

        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_barcode.getText().toString().trim().length() > 2) {
                    String xmv_location = txtlocation.getText().toString().split("---")[0].trim();
                    if(xmv_location.isEmpty() && !fgen.mcd.equals("SVPL"))
                    {
                        Toast.makeText(getApplicationContext(), "Please Select Location First!!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    boolean xcheck_reel_status = reel_stacking_for_grid(txt_barcode.getText().toString().trim());
                    if(xcheck_reel_status) {
                        fillGrid();
                    }
                    txt_barcode.setText("");
                    txt_barcode.requestFocus();
                }
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_fg_stacking_mvin.this, xapi_name);
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_fg_stacking_mvin.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xmv_location = txtlocation.getText().toString().split("---")[0].trim();
                modellList = handler.get_data();

                if(xmv_location.isEmpty() && !fgen.mcd.equals("SVPL"))
                {
                    Toast.makeText(getApplicationContext(), "Please Select Location First!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(fgen.mcd.equals("SVPL")){xmv_location = "-";}
                if(xmv_location.isEmpty() || modellList.size() <=0)
                {
                    Toast.makeText(getApplicationContext(), "Invalid Data!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_fg_stacking_mvin.this);
                String finalXmv_location = xmv_location;

                progressDialog.show();
                Handler handler_thread = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler_thread.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                String post_param = "";
                                for (scanner_model m : modellList) {
                                    String xmv_reels = m.getQr_Code().toString();
                                    post_param += xbranch + xseperator + xtype + xseperator + finalXmv_location + xseperator
                                            + xmv_reels + "!~!~!";
                                }
                                try {
                                    ArrayList<team> result = finapi.getApi(fgen.mcd, xapi_name, post_param, fgen.muname, fgen.cdt1.substring(6, 10), "-", "-");
                                    boolean msg = finapi.showAlertMessage(frm_fg_stacking_mvin.this, result);
                                    if (!msg) {
                                        progressDialog.dismiss();
                                        return;
                                    }
                                } catch (Exception e) {
                                    new android.app.AlertDialog.Builder(frm_fg_stacking_mvin.this)
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
                                //newBtnClickedMethod();
                                txt_reels.setText("");
                                txtscanner.setText("");
                                handler.clear_auto_save_data_fg_stacking();
                                handler.clear_data();
                                modellList = new ArrayList<>();
                                adapter = new myAdapter(getApplicationContext(), modellList);
                                recycler.setAdapter(adapter);
                                txt_barcode.requestFocus();
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

    boolean reel_stacking_for_grid(String rawResult){
        if(rawResult.isEmpty()) return false;
        xicode = "";
        ArrayList<team6> result = new ArrayList<>();
        try {
            if (fgen.btnid.equals("EP771")) {
                result = finapi.getApi6(fgen.mcd, "aFGTAG_check", "" + rawResult.toString().trim(), fgen.muname, fgen.cdt1.substring(6, 10), "#", "FG173C");
            } else {
                if (fgen.mcd.equals("SGRP")) {
                    result = finapi.getApi6(fgen.mcd, "aFGTAG_check", "" + rawResult.toString().trim(), fgen.muname, fgen.cdt1.substring(6, 10), "#", "FGSTACK");
                } else {
                    if (fgen.btnid.equals("EP1501")) { // MVIN Received F.G. WIP Store
                        result = finapi.getApi6(fgen.mcd, "aFGTAG_check", "" + rawResult.toString().trim(), fgen.muname, fgen.cdt1.substring(6, 10), "FGSTACK", frm_fg_stacking_mvin.xtype);
                    } else {
                        result = finapi.getApi6(fgen.mcd, "aFGTAG_check", "" + rawResult.toString().trim().substring(0, 26), fgen.muname, fgen.cdt1.substring(6, 10), "FGSTACK", "#");
                    }
                }
            }
            Gson gson = new Gson();
            String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {
            }.getType());
            String data = "", xcol5 = "", xerror = "", xtag = "";
            try {
                JSONArray array = new JSONArray(listString);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject respObj = array.getJSONObject(i);
                    data = respObj.getString("col1").trim();
                    xtag = respObj.getString("col3").trim();
                    xcol5 = respObj.getString("col5").trim();
                    xerror = respObj.getString("col6").trim();   // for EP771 it's a qty of fg sticker
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!data.equals("Success")) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(xerror)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                return;
                            }
                        })
                        .show();
                return false;
            }
            if (fgen.btnid.equals("EP771"))
                frm_fg_stacking_mvin.txt_reels.setText("" + xtag.trim() + " #~# " + xcol5 + " #~# " + xerror);
            else
                frm_fg_stacking_mvin.txt_reels.setText("" + xtag.trim());
        }catch (Exception e){
            AlertDialog alertDialog = new AlertDialog.Builder(frm_fg_stacking_mvin.this).create();
            alertDialog.setTitle("Error Found");
            alertDialog.setMessage("" + e);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return false;
        }
        return true;
    }


    private void fillGrid() {
        if(txt_reels.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Scan TAG First!!", Toast.LENGTH_LONG).show();
            return;
        }
        reel_scan_result = txt_reels.getText().toString();
        String check_reel_code = reel_scan_result;
        boolean duplicate = handler.CheckDuplicacy("col1", ""+check_reel_code);
        if(duplicate)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(frm_fg_stacking_mvin.this).create();
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
//               scanner_model m = new scanner_model("1", reel_scan_result, ""+txtqty.getText().toString(), xicode);
        scanner_model m = new scanner_model("1", reel_scan_result.trim(), "", xicode, xreel_iname);
        handler.insert_data(m);
        handler.insert_autosave_data_fg_stacking(m);   // auto save data
        modellList = handler.get_data();
        adapter = new myAdapter(getApplicationContext(), modellList);
        recycler.setAdapter(adapter);

        txt_reels.setText("");
        txt_barcode.requestFocus();

        btn_save.performClick();
    }

    private void autoSavedDataFillMethod(){
        modellList = handler.get_auto_save_data_fg_stacking();
        for (scanner_model data:  modellList) {
            handler.insert_data(new scanner_model("1", data.qr_Code.trim(), "", data.xicode.trim(), data.xiname.trim()));
        }
        adapter = new myAdapter(getApplicationContext(), modellList);
        recycler.setAdapter(adapter);
    }

    private void disableViewsMethod() {
        btn_location.setEnabled(false);
        btn_scan.setEnabled(false);
        btn_add.setEnabled(false);
        btn_read.setEnabled(false);
        txtlocation.setEnabled(false);
        txt_reels.setEnabled(false);
    }

    private void enabledViewMethod() {
        btn_location.setEnabled(true);
        btn_scan.setEnabled(true);
        btn_add.setEnabled(true);
        btn_read.setEnabled(true);
        txtlocation.setEnabled(true);
        txt_reels.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtlocation.setText("");
        txt_reels.setText("");
        txtscanner.setText("");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgen.frm_request = "";
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
        if(controll.equals("txtlocation")) txtapi_code.setText("EP820A");

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
                    case "txt_reels" : txt_reels.setText(list_selected_text);
                        break;
                    case "txtlocation" : txtlocation.setText(list_selected_text);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
    }
}