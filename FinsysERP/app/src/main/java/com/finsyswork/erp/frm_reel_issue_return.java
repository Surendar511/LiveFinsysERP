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
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

/*
    This Form Used Material Issue Slip Components
    Like : Card, Model, Adapter, Database.

 */

// NOT IN USE RIGHT NOW =========
// NOT IN USE RIGHT NOW =========
// NOT IN USE RIGHT NOW =========
// NOT IN USE RIGHT NOW =========






























































public class frm_reel_issue_return extends AppCompatActivity {

    Button btnscan_job, btnscan_reel, btn_save, btn_new, btn_cancel, btn_add, btn_read;
    public static EditText txtscan_job, txtscan_reel, txt_barcode;
    RecyclerView recyclerView;
    sqliteHelperClass handler;
    Toolbar toolbar;
    issue_slip_adapter adapter;
    public static issue_req_model issue_req_model;
    public static ArrayList<issue_req_model> list = new ArrayList<>();
    public static ArrayList<String> xjob = new ArrayList<>();
    public static String btn_clicked = "", xicode = "";
    String xseperator = "#~#", xbranch = "00", xtype = "11";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_reel_issue_return);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        final MyProgressdialog progressDialog = new MyProgressdialog(frm_reel_issue_return.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        xjob = finapi.fill_record_in_listview_popup("EP835");
                        progressDialog.dismiss();
                    }
                }, 100);

        initializeViews();
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        finapi.setColorOfStatusBar(frm_reel_issue_return.this);
        handler = new sqliteHelperClass(this);
        handler.clear_data();
        setTextOnViews();
        disableViewsMethod();
        setSupportActionBar(toolbar);

        handler.clear_data();
        list = handler.issue_req_get_data();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new issue_slip_adapter(getApplicationContext(), list);
        recyclerView.setAdapter(adapter);

        txtscan_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_reel_issue_return.this, xjob, "txtscan_job");
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
                txtscan_job.setEnabled(true);

                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);

                txtscan_job.setBackgroundResource(R.color.light_blue);
                txt_barcode.requestFocus();
            }
        });

        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_barcode.getText().toString().trim().length() > 2) {
                    txtscan_reel.setText(txt_barcode.getText().toString().trim());
                    boolean xcheck_reel_status = multi_reel_single_job(txt_barcode.getText().toString().trim());
                    if(xcheck_reel_status) {
                        fillGrid();
                    }
                    txt_barcode.setText("");
                    txtscan_reel.setText("");
                }
            }
        });

        txt_barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == 19 || keyCode == 21)) {
                    if (txt_barcode.getText().toString().trim().length() > 2) {
                        txtscan_reel.setText(txt_barcode.getText().toString().trim());
//                          scannerView.multi_reel_single_job(txtscan_reel.getText().toString());
                        boolean xcheck_reel_status = multi_reel_single_job(txt_barcode.getText().toString().trim());
                        if(xcheck_reel_status) {
                            fillGrid();
                        }
                        txt_barcode.setText("");
                        txtscan_reel.setText("");
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
                    adapter = new issue_slip_adapter(getApplicationContext(), list);
                    recyclerView.setAdapter(adapter);

                    txtscan_job.setBackgroundResource(R.color.light_grey);
                }
                if (btn_text == "EXIT") {
                    finish();
                }
            }
        });

        btnscan_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fgen.qr_scanned_with_external = "N";
                btn_clicked = "job";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
                txt_barcode.requestFocus();
            }
        });

        btnscan_reel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_clicked = "reel_return";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
                txt_barcode.requestFocus();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillGrid();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_reel_issue_return.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String xjob_dt = "";
                                xjob_dt = txtscan_job.getText().toString().substring(10, 20);
                                list = handler.issue_req_get_data();
                                String xjob_no = txtscan_job.getText().toString().substring(4, 10);
                                if (txtscan_job.getText().toString().isEmpty() || handler.issue_req_get_data().size() <= 0) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Invalid Data!!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                String post_param = "";
                                for (issue_req_model m : list) {
                                    String xicode = m.getItem().toString().split("---")[0].trim();
                                    String xmv_qty = m.getQty().toString();
                                    String xmv_reels = m.getXicode().toString();
                                    post_param += xbranch + xseperator + xtype + xseperator + xicode + xseperator
                                            + xmv_reels + xseperator + xmv_qty + xseperator + xjob_no + xseperator + xjob_dt + "!~!~!";
                                }

                                ArrayList<team> result = finapi.getApi(fgen.mcd, "areel_issue", post_param, fgen.muname, fgen.cdt1.substring(6, 10), fgen.btnid, "-");
                                boolean msg = finapi.showAlertMessage(frm_reel_issue_return.this, result);
                                if (!msg) {
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                handler.clear_data();
                                list = new ArrayList<>();
                                adapter = new issue_slip_adapter(getApplicationContext(), list);
                                recyclerView.setAdapter(adapter);
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    void fillGrid() {
        {
            String xreel = txtscan_reel.getText().toString();
            if (xreel.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Scan Job And Reel First!!", Toast.LENGTH_LONG).show();
                return;
            }
            if (fgen.frm_request.equals("frm_paper_end_for_jobs")) {
                if (frm_paper_end.issue_req_model != null) {
                    handler.issue_slip_insert_data(frm_paper_end.issue_req_model);
                    txtscan_job.setText("");
                }
            } else {
                if (frm_paper_end.issue_req_model != null) {
                    handler.issue_slip_insert_data(frm_paper_end.issue_req_model);
                    txtscan_reel.setText("");
                }
            }
            list = handler.issue_req_get_data();
            adapter = new issue_slip_adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
            txt_barcode.requestFocus();
        }
    }

    public boolean multi_reel_single_job(String rawResult) {
        String xbuild_qr_name = "";
        ArrayList<team6> result = finapi.getApi6(fgen.mcd, "areel_check",""+rawResult.trim(), fgen.muname, fgen.cdt1.substring(6,10), "#", "#");
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
                    xbuild_qr_name = frm_paper_end.xicode +"---"+ xcol4;
                }catch (Exception e){}
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
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return false;
        }
        else {
            try {
                frm_paper_end.txtscan_reel.setText("" + xbuild_qr_name);
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
        frm_paper_end.issue_req_model = new issue_req_model("1", ""+xbuild_qr_name, frm_job_issue_entry.xreel_qty, ""+rawResult.trim());
        return  true;
    }


    private void newBtnClickedMethod() {
        txtscan_job.setText("");
        txtscan_reel.setText("");
    }

    private void disableViewsMethod() {
        btn_save.setEnabled(false);
        btnscan_reel.setEnabled(false);
        btnscan_job.setEnabled(false);
        txtscan_job.setEnabled(false);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            list = handler.issue_req_get_data();
        } catch (Exception e) {
        }
        adapter = new issue_slip_adapter(getApplicationContext(), list);
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
        if (controll.equals("txtscan_job")) txtapi_code.setText("EP835");

        close_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
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
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
    }
}