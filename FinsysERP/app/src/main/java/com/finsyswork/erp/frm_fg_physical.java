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

public class frm_fg_physical extends AppCompatActivity {

    public static TextView txtscanner;
    public static Button btn_scan, btn_cancel, btn_location, btn_add, btn_save,btn_new, btn_read, btn_auto_save_data;
    public static EditText txtlocation, txt_reels, txtqty2, txtqty1, txt_barcode, txtcompany_reel_no;
    public static RecyclerView recycler;
    public static ArrayList<scanner_model> modellList = new ArrayList<>();
    public static myAdapter adapter;
    sqliteHelperClass handler;
    Toolbar toolbar;
    public static String reel_scan_result = "", btn_clicked = "", xreel_iname="";
    String xseperator = "#~#", xbranch="00", xtype ="FP";
    public static String xicode = "";
    ArrayList<String> xselectlocation = new ArrayList<>();
    RadioButton radio_yes, radio_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_fg_physical);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_fg_physical.this;
        finapi.deleteJsonResponseFile();

        fgen.frm_request = "frm_fg_physical";

        radio_yes = findViewById(R.id.radio_yes);
        radio_no = findViewById(R.id.radio_no);
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_fg_physical.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        xselectlocation = finapi.fill_record_in_listview_popup("EP820A");
                        frm_issue_slip.reel_wise_lot = "Y";
//                        frm_issue_slip.xlot_enable = finapi.fill_record_in_listview_popup("EP720");
//                        for (String x : frm_issue_slip.xlot_enable) {
//                            frm_issue_slip.reel_wise_lot = x.split("---")[1].trim();
//                        }
//                        if (frm_issue_slip.reel_wise_lot.equals("Y"))
//                            radio_yes.setChecked(true);
//                        else radio_no.setChecked(true);

                        xtype="FP";
                        progressDialog.dismiss();
                    }
                }, 100);
//        xselectreel = finapi.fill_record_in_listview_popup("EP826");

        radio_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(radio_yes.isChecked()) frm_issue_slip.reel_wise_lot = "Y";
                else frm_issue_slip.reel_wise_lot = "N";
            }
        });

        handler = new sqliteHelperClass(this);
        finapi.setColorOfStatusBar(frm_fg_physical.this);


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
        txtqty2 = findViewById(R.id.txtqty2);
        txtqty1 = findViewById(R.id.txtqty1);
        recycler = findViewById(R.id.listItems);
        toolbar = findViewById(R.id.toolbar);
        txtcompany_reel_no = findViewById(R.id.txtcomp_reel_no);
        txt_barcode = findViewById(R.id.txt_barcode);
        handler = new sqliteHelperClass(getApplicationContext());
        btn_cancel.setText("EXIT");

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
//                ShowDialogBox(frm_fg_physical.this, xselectreel, "txt_reels");
//            }
//        });

        txtlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_fg_physical.this, xselectlocation, "txtlocation");
                txt_barcode.requestFocus();
            }
        });

        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_barcode.getText().toString().trim().length() > 2) {
                    reel_scan_result = txt_barcode.getText().toString().trim();
                    boolean xreel_status = scan_reel_method(txt_barcode.getText().toString().trim());
                    if(xreel_status) {
                        fillGrid();
                    }
                    txt_barcode.setText("");
                    txt_reels.setText("");
                }
            }
        });

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_clicked = "frm_fg_physical_location";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
                txt_barcode.requestFocus();
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_clicked = "frm_fg_physical_reels";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
                txt_barcode.requestFocus();
            }
        });

        txt_barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == 19 || keyCode == 21)) {
                    if (txt_barcode.getText().toString().trim().length() > 2) {
//                          scannerView.multi_reel_single_job(txtscan_reel.getText().toString());
                        reel_scan_result = txt_barcode.getText().toString().trim();
                        boolean xreel_status = scan_reel_method(txt_barcode.getText().toString().trim());
                        if(xreel_status) {
                            fillGrid();
                        }
                        txt_barcode.setText("");
                        txt_reels.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillGrid();
                txt_barcode.requestFocus();
            }
        });

        txtqty2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
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

                txtlocation.setBackgroundResource(R.color.light_blue);

                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);
                txt_barcode.setText("");
                txt_barcode.requestFocus();
                txtqty2.setFocusable(true);
                txtlocation.setEnabled(true);


                modellList = handler.get_auto_save_data();
                if(!modellList.isEmpty()) {
                    new android.app.AlertDialog.Builder(frm_fg_physical.this)
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
                                    handler.clear_auto_save_data();
                                    txt_barcode.requestFocus();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn_text = btn_cancel.getText().toString();
                if(btn_text == "CANCEL")
                {
                    disableViewsMethod();
                    handler.clear_data();
                    handler.clear_auto_save_data();
                    btn_cancel.setText("EXIT");
                    modellList = new ArrayList<>();
                    adapter = new myAdapter(getApplicationContext(), modellList);
                    recycler.setAdapter(adapter);
                    btn_new.setEnabled(true);
                    txtlocation.setBackgroundResource(R.color.light_grey);

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                    txt_barcode.requestFocus();
                }
                if(btn_text == "EXIT")
                {
                    finish();
                    frm_fg_physical.btn_clicked = "";
                }
            }
        });

        btn_auto_save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.app.AlertDialog.Builder(frm_fg_physical.this)
                        .setTitle("Warning!!")
                        .setMessage("Are Your Sure, You Want To Do Clear Auto Saved Data!!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.clear_auto_save_data();
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

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_fg_physical.this, "aphystk_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_fg_physical.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xmv_location = "";
                try {
                    xmv_location = txtlocation.getText().toString().split("---")[1].trim();
                }catch (Exception e){xmv_location = txtlocation.getText().toString().trim();}
                modellList = handler.get_data();
                if(xmv_location.isEmpty() || modellList.size() <=0)
                {
                    Toast.makeText(getApplicationContext(), "Invalid Data!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_fg_physical.this);
                progressDialog.show();
                String finalXmv_location = xmv_location;
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = "";
                                for(scanner_model m : modellList)
                                {
                                    String xmv_reels = m.getQr_Code().toString();
                                    String xmv_qty = m.getQty().toString();
                                    String xmv_book_qty = m.getXbook_qty().toString();
                                    xicode = m.getXicode().toString();
                                    post_param += xbranch + xseperator + xtype + xseperator + finalXmv_location + xseperator
                                            + xicode + xseperator + xmv_reels + xseperator + xmv_qty
                                            + xseperator + xmv_book_qty + "!~!~!";
                                }

                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aphystk_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                boolean msg = finapi.showAlertMessage(frm_fg_physical.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                handler.clear_data();
                                handler.clear_auto_save_data();
                                modellList = new ArrayList<>();
                                adapter = new myAdapter(getApplicationContext(), modellList);
                                recycler.setAdapter(adapter);
                                txt_barcode.setText("");
                                txt_barcode.requestFocus();
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgen.frm_request = "";
        frm_fg_physical.btn_clicked = "";
    }

    boolean scan_reel_method(String rawResult)
    {
        ArrayList<team6> result = new ArrayList<>();
        if(fgen.mcd.equals("SVPL")) {
            result = finapi.getApi6(fgen.mcd, "areel_check", "" + rawResult.toString().substring(8, 18), fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
        }
        else {
            if (fgen.btnid.equals("EP1503")) {
                result = finapi.getApi6(fgen.mcd, "areel_FGRcheck", "" + rawResult, fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
            } else {
                result = finapi.getApi6(fgen.mcd, "areel_check", "" + rawResult.trim(), fgen.muname, fgen.cdt1.substring(6, 10), "#", "#");
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
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return false;
        }
        else {
            frm_fg_physical.txt_reels.setText("" + frm_fg_physical.xreel_iname);
            frm_fg_physical.reel_scan_result = "" + rawResult;
            return true;
        }
    }

    void fillGrid() {
        if(frm_fg_physical.xreel_iname.toString().trim().isEmpty() || txtqty2.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Scan Reel And Fill Qty. First!!", Toast.LENGTH_LONG).show();
            return;
        }
        String check_reel_code = reel_scan_result.trim();
        boolean duplicate = handler.CheckDuplicacy("col1", ""+check_reel_code);
        if(duplicate)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(frm_fg_physical.this).create();
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
//             scanner_model m = new scanner_model("1", reel_scan_result, ""+txtqty2.getText().toString(), xicode);
        scanner_model m = new scanner_model("1", reel_scan_result.trim(), ""+txtqty1.getText().toString(), xicode, xreel_iname, txtqty2.getText().toString());
        handler.insert_data(m);
        handler.insert_autosave_data(m);   // auto save data
        modellList = handler.get_data();
        adapter = new myAdapter(getApplicationContext(), modellList);
        recycler.setAdapter(adapter);
        txt_reels.setText("");
        txtqty2.setText("");
        txtqty1.setText("");
    }

    private void autoSavedDataFillMethod(){
        modellList = handler.get_auto_save_data();
        for (scanner_model data:  modellList) {
            handler.insert_data(new scanner_model("1", data.qr_Code.trim(), data.qty.trim(), data.xicode.trim(), data.xiname.trim(), data.xbook_qty.trim()));
        }
        adapter = new myAdapter(getApplicationContext(), modellList);
        recycler.setAdapter(adapter);
    }

    private void disableViewsMethod() {
        btn_location.setEnabled(false);
        btn_scan.setEnabled(false);
        btn_add.setEnabled(false);
        txtlocation.setEnabled(false);
    }

    private void enabledViewMethod() {
        btn_location.setEnabled(true);
        btn_scan.setEnabled(true);
        btn_add.setEnabled(true);
        txtqty2.setEnabled(true);
        txtlocation.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtlocation.setText("");
        txt_reels.setText("");
        txtqty2.setText("");
        txtqty1.setText("");
        txtcompany_reel_no.setText("");
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
        if(controll.equals("txt_reels")) txtapi_code.setText("EP824");
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
                    case "txt_reels" : txt_reels.setText(list_selected_text.trim());
                        break;
                    case "txtlocation" : txtlocation.setText(list_selected_text.trim());
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
    }
}