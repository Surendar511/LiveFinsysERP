package com.finsyswork.erp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class frm_quality_inspection extends AppCompatActivity {

    Button btn_save,btn_new, btn_cancel, btn_scan;
    public static EditText txtscan, txtvendor;
    public static RecyclerView recyclerView, recycler2;
    Toolbar toolbar;
    public static sqliteHelperClass handler;
    public static ArrayList<comman_model> list = new ArrayList<>();
    public static ArrayList<comman_model> list2 = new ArrayList<>();
    public static ArrayList<String> xscan_list = new ArrayList<>();
    public static comman_model model = new comman_model();
    public static comman_adapter adapter;
    public static quality_inspection_adapter adapter2;
    String xseperator = "#~#", xbranch="00", xtype ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_quality_inspection);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_quality_inspection.this;
        finapi.deleteJsonResponseFile();

        handler = new sqliteHelperClass(this);
        initializeViews();
        btn_cancel.setText("EXIT");
        fgen.xcard_view_name = "sale_order_schedule_booking_card_view";
        finapi.context = frm_quality_inspection.this;

        final MyProgressdialog progressDialog = new MyProgressdialog(frm_quality_inspection.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        fgen.frm_request = "frm_quality_inspection";
                        if(fgen.mcd.equals("SVPL")) txtscan.setEnabled(false);
                        else xscan_list = finapi.issue_slip_fill_record_in_listview_popup("EP715A");
                        // On complete call either onLoginSuccess or onLoginFailed
                        progressDialog.dismiss();
                    }
                }, 100);

        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_quality_inspection.this);

        handler.clear_data();
        list = handler.comman_get_data();


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter2 = new quality_inspection_adapter(frm_quality_inspection.this, list2);
        recyclerView.setAdapter(adapter2);

        recycler2.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        adapter = new comman_adapter(frm_quality_inspection.this, list);

        recycler2.setAdapter(adapter);

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBtnClickedMethod();
                enabledViewMethod();
                btn_save.setEnabled(true);
                btn_new.setEnabled(false);
                btn_cancel.setText("CANCEL");

                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);
                txtscan.setBackgroundResource(R.color.light_blue);
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

                    handler.clear_data();
                    list = handler.comman_get_data();
                    list2 = new ArrayList<>();
                    adapter = new comman_adapter(frm_quality_inspection.this, list);
                    adapter2 = new quality_inspection_adapter(frm_quality_inspection.this, list2);
                    recycler2.setAdapter(adapter);
                    recyclerView.setAdapter(adapter2);
                    txtscan.setBackgroundResource(R.color.light_grey);

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fgen.frm_request = "frm_quality_inspection";
                finapi.context = frm_quality_inspection.this;
                startActivity(new Intent(getApplicationContext(), scannerView.class));
            }
        });

        txtscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_quality_inspection.this, xscan_list, "txtscan");
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_quality_inspection.this, "aSV_MRRIQC_upd");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_quality_inspection.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_quality_inspection.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
//                              String  xscan = txtscan.getText().toString();
//                              xtype = xscan.substring(14,16);
                                String  xxscan = "";
                                String xscan = "";
                                xxscan = txtscan.getText().toString();
                                if(xxscan.contains("---"))
                                {
//                                  xxscan = txtscan.getText().toString();
                                    xscan = xxscan.split(" --- ")[3].trim();
                                    xtype = xscan.substring(14,16);
                                }
                                else
                                {
                                    xscan = txtscan.getText().toString();
                                    xtype = xscan.substring(14,16);
                                }
                                if(xscan.equals(""))
                                {
                                    Toast.makeText(getApplicationContext(), "Please Scan BAR Code First!!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String post_param = "";
                                for(comman_model data : list2)
                                {
                                    String xitem = data.getXcol1().split("---")[0].trim();
                                    String xok_qty = data.getXcol2().trim();
                                    String xrej_qty = data.getXcol3().trim();
                                    String xreason = data.getXcol4().trim();

                                    post_param += xbranch + xseperator + xtype + xseperator + xscan
                                            + xseperator + xitem + xseperator + xok_qty + xseperator
                                            + xrej_qty + xseperator + xreason +  "!~!~!";
                                }
                                ArrayList<team> result = new ArrayList<>();
                                if(fgen.mcd.equals("SVPL")) {
                                    result = finapi.getApi(fgen.mcd, "aSV_MRRIQC_upd", post_param, fgen.muname, fgen.cdt1.substring(6, 10), "-", "EP903");
                                }
                                else{
                                    result = finapi.getApi(fgen.mcd, "aSV_MRRIQC_upd", post_param, fgen.muname, fgen.cdt1.substring(6, 10), "-", "EP903");
                                }
                                boolean msg = finapi.showAlertMessage(frm_quality_inspection.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                handler.clear_data();
                                list = handler.comman_get_data();
                                list2 = new ArrayList<>();
                                adapter = new comman_adapter(getApplicationContext(), list);
                                adapter2 = new quality_inspection_adapter(getApplicationContext(), list2);
                                recyclerView.setAdapter(adapter2);
                                recycler2.setAdapter(adapter);
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    private void enabledViewMethod() {
        btn_scan.setEnabled(true);
        txtscan.setEnabled(true);
        if(fgen.mcd.equals("SVPL")) txtscan.setEnabled(false);
    }

    private void newBtnClickedMethod() {
        txtscan.setText("");
        txtvendor.setText("");
    }

    private void disableViewsMethod() {
        btn_scan.setEnabled(true);
        txtscan.setEnabled(true);
    }

    private void initializeViews() {
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_new = findViewById(R.id.btn_new);
        btn_scan = findViewById(R.id.btn_scan);
        txtscan = findViewById(R.id.txtscan);
        txtvendor = findViewById(R.id.txtvender);
        recycler2 = findViewById(R.id.recycler2);
        recyclerView = findViewById(R.id.recycler);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgen.frm_request = "";
        fgen.xcard_view_name = "";
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
        if(controll.equals("txtscan")) {
            txtapi_code.setText("EP715A");
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
                    case "txtscan" : txtscan.setText(list_selected_text);
                        final MyProgressdialog progressDialog = new MyProgressdialog(frm_quality_inspection.this);
                        progressDialog.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        handler.clear_data();
                                        frm_quality_inspection.list = handler.comman_get_data();
                                        frm_quality_inspection.list2 = new ArrayList<>();
                                        recycler2.setAdapter(frm_quality_inspection.adapter);
                                        adapter2 = new quality_inspection_adapter(getApplicationContext(), list2);
                                        recyclerView.setAdapter(adapter2);
                                        adapter2.notifyDataSetChanged();
                                        frm_quality_inspection.adapter.notifyDataSetChanged();
                                        adapter2.notifyDataSetChanged();

                                        frm_lead_enquiry.xfrm_lead_enquiry = "Y";
                                        frm_lead_enquiry.xfg_sub_group_code = list_selected_text.split("---")[3].trim();
                                        txtvendor.setText(list_selected_text.split("---")[2].trim());
                                        ArrayList<String> x = finapi.fill_record_in_listview_popup("EP715B");
                                        recycler2.setAdapter(frm_quality_inspection.adapter);
                                        frm_quality_inspection.adapter.notifyDataSetChanged();
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
}

