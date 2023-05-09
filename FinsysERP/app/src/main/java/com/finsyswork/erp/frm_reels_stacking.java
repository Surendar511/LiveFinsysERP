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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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

public class frm_reels_stacking extends AppCompatActivity {

    public static TextView txtscanner;
    Button btn_scan, btn_cancel, btn_location, btn_add, btn_save,btn_new, btn_read;
    public static EditText txtlocation, txt_reels, txt_barcode;
    public static RecyclerView recycler;
    public static ArrayList<scanner_model> modellList = new ArrayList<>();
    public static myAdapter adapter;
    sqliteHelperClass handler;
    Toolbar toolbar;
    public static String reel_scan_result = "", btn_clicked = "", xreel_iname="";
    String xseperator = "#~#", xbranch="00", xtype ="FG";
    public static String xicode = "";
    ArrayList<String> xselectlocation = new ArrayList<>();
    RadioButton radio_yes, radio_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_reels_stacking);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_reels_stacking.this;
        finapi.deleteJsonResponseFile();


        radio_yes = findViewById(R.id.radio_yes);
        radio_no = findViewById(R.id.radio_no);
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_reels_stacking.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        xselectlocation = finapi.fill_record_in_listview_popup("EP820A");
                        if(fgen.btnid.equals("EP743")) {
                            frm_issue_slip.reel_wise_lot = "Y";
//                            frm_issue_slip.xlot_enable = finapi.fill_record_in_listview_popup("EP720");
//                            for (String x : frm_issue_slip.xlot_enable) {
//                                frm_issue_slip.reel_wise_lot = x.split("---")[1].trim();
//                            }
                        }
//                        if(frm_issue_slip.reel_wise_lot.equals("Y")) radio_yes.setChecked(true);
                        else radio_no.setChecked(true);
                        progressDialog.dismiss();
                    }
                }, 100);
//        xselectreel = finapi.fill_record_in_listview_popup("EP826");

        handler = new sqliteHelperClass(this);
        finapi.setColorOfStatusBar(frm_reels_stacking.this);
        btn_scan = findViewById(R.id.btn_scanner);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_add = findViewById(R.id.btn_add);
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_read = findViewById(R.id.btn_read);
        btn_location = findViewById(R.id.btn_location);
        btn_cancel = findViewById(R.id.btn_cancel);
        txtscanner = findViewById(R.id.txtscanner);
        txtlocation = findViewById(R.id.txt_location);
        txt_reels = findViewById(R.id.txt_reels);
        recycler = findViewById(R.id.listItems);
        toolbar = findViewById(R.id.toolbar);
        txt_barcode = findViewById(R.id.txt_barcode);
        handler = new sqliteHelperClass(getApplicationContext());
        btn_cancel.setText("EXIT");

        if(fgen.btnid.equals("EP743"))
        {
            toolbar.setTitle("RM Stacking");
            btn_scan.setText("Scan RM");
            btn_add.setText("Add Item To List");
        }

        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        txtlocation.setKeyListener(null);
        txt_reels.setKeyListener(null);
        disableViewsMethod();
        setSupportActionBar(toolbar);
        handler.clear_data();
        adapter = new myAdapter(getApplicationContext(), modellList);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        recycler.setAdapter(adapter);

//        txt_reels.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShowDialogBox(frm_bar_scanner.this, xselectreel, "txt_reels");
//            }
//        });

        radio_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(radio_yes.isChecked()) frm_issue_slip.reel_wise_lot = "Y";
                else frm_issue_slip.reel_wise_lot = "N";
            }
        });

        txtlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_reels_stacking.this, xselectlocation, "txtlocation");
                txt_barcode.requestFocus();
            }
        });

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fgen.frm_request = "frm_reel_stacking";
                btn_clicked = "frm_reel_stacking_location";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
                txt_barcode.requestFocus();
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fgen.frm_request = "frm_reel_stacking";
                btn_clicked = "frm_reel_stacking_reel";
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
                txtlocation.setBackgroundResource(R.color.light_blue);
                txt_barcode.requestFocus();
            }
        });

        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_barcode.getText().toString().trim().length() > 2) {
                    boolean xcheck_reel_status = reel_stacking_for_grid(txt_barcode.getText().toString().trim());
                    if(xcheck_reel_status) {
                        fillGrid();
                    }
                    txt_barcode.setText("");
                    txt_barcode.requestFocus();
                }
            }
        });

        txt_barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == 19 || keyCode == 21)) {
                    if (txt_barcode.getText().toString().trim().length() > 2) {
//                          scannerView.multi_reel_single_job(txtscan_reel.getText().toString());
                        boolean xcheck_reel_status = reel_stacking_for_grid(txt_barcode.getText().toString().trim());
                        if(xcheck_reel_status) {
                            fillGrid();
                        }
                        txt_barcode.setText("");
                        txt_barcode.requestFocus();
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
                if(btn_text == "CANCEL")
                {
                    handler.clear_data();
                    btn_cancel.setText("EXIT");
                    modellList = new ArrayList<>();
                    adapter = new myAdapter(getApplicationContext(), modellList);
                    recycler.setAdapter(adapter);
                    btn_new.setEnabled(true);
                    txtlocation.setBackgroundResource(R.color.light_grey);
                    newBtnClickedMethod();
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
                    fgen.showApiName(frm_reels_stacking.this, "aREEL_stack");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_reels_stacking.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xmv_location = txtlocation.getText().toString().split("---")[0].trim();
                modellList = handler.get_data();
                if(xmv_location.isEmpty() || modellList.size() <=0)
                {
                    Toast.makeText(getApplicationContext(), "Invalid Data!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_reels_stacking.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = "";
                                for(scanner_model m : modellList)
                                {
                                    String xmv_reels = m.getQr_Code().toString();
                                    post_param += xbranch + xseperator + xtype + xseperator + xmv_location + xseperator
                                            + xmv_reels + "!~!~!";
                                }

                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aREEL_stack",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                boolean msg = finapi.showAlertMessage(frm_reels_stacking.this, result);
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


    boolean reel_stacking_for_grid(String rawResult){
        String xreel_no = "";
        xicode = "";
        ArrayList<team6> result = finapi.getApi6(fgen.mcd, "areel_check", "" + rawResult.toString().trim(), fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
        String data = "";
        try {
            JSONArray array = new JSONArray(listString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject respObj = array.getJSONObject(i);
                data = respObj.getString("col1").trim();
                xicode = respObj.getString("col2").trim();
                xreel_no = respObj.getString("col3").trim();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!data.equals("Success")) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Sorry, Item Is Invalid!!")
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
        String  xscanned_text = xicode +  "#~#" + xreel_no;
        frm_reels_stacking.txt_reels.setText("" + xscanned_text.trim());
        return true;
    }

    void fillGrid() {
        if(txt_reels.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Scan REEL First!!", Toast.LENGTH_LONG).show();
            return;
        }
        reel_scan_result = txt_reels.getText().toString();
        String check_reel_code = reel_scan_result;
        boolean duplicate = handler.CheckDuplicacy("col1", ""+check_reel_code);
        if(duplicate)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(frm_reels_stacking.this).create();
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
        modellList = handler.get_data();
        adapter = new myAdapter(getApplicationContext(), modellList);
        recycler.setAdapter(adapter);
//        txt_reels.setText("");
        txt_barcode.requestFocus();
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
        btn_read.setEnabled(true);
        txtlocation.setEnabled(true);
        txt_reels.setEnabled(true);
        btn_add.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtlocation.setText("");
        txt_reels.setText("");
        txt_barcode.setText("");
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