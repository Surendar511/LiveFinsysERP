package com.finsyswork.erp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class frm_record_alectricity_usage extends AppCompatActivity {

    private Button btn_new,btn_save, btn_cancel, btn_scan;
    private EditText txtremarks, txtst_reading, txtend_reading;
    public  static  EditText txtmachine_qr_code;
    Toolbar toolbar;
    String xseperator = "#~#", xbranch="00", xtype ="PC";
    public static ArrayList<String> xmachine = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_record_alectricity_usage);

        finapi.context = frm_record_alectricity_usage.this;
        finapi.deleteJsonResponseFile();


        final MyProgressdialog progressDialog = new MyProgressdialog(frm_record_alectricity_usage.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        frm_lead_enquiry.xfrm_lead_enquiry = "";
                        xmachine = new ArrayList<>();
                        //this method will be running on background thread so don't update UI frome here
                        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
                        xmachine = finapi.fill_record_in_listview_popup("EP831");
//                        new AsyncCaller_jobw_prod().execute();
                        progressDialog.dismiss();
                    }
                }, 100);


        initializeViews();
        btn_cancel.setText("EXIT");
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_record_alectricity_usage.this);
        Highlightborder();

         txtmachine_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_record_alectricity_usage.this, xmachine, "txtmachine_qr_code");
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
                txtmachine_qr_code.setBackgroundResource(R.color.light_blue);

            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), scannerView.class));
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

                    txtmachine_qr_code.setBackgroundResource(R.color.light_grey);
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
                    fgen.showApiName(frm_record_alectricity_usage.this, "apowcon_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_record_alectricity_usage.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xmachine_qr_code = txtmachine_qr_code.getText().toString();
                String xst_reading = txtst_reading.getText().toString();
                String xend_reading = txtend_reading.getText().toString();
                if(xmachine_qr_code.isEmpty() || xst_reading.isEmpty() || xend_reading.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Invalid Data!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_record_alectricity_usage.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String xremarks = txtremarks.getText().toString();
                                String post_param = xbranch + xseperator + xtype + xseperator + xmachine_qr_code.split("---")[0].trim() + xseperator
                                        + xst_reading + xseperator + xend_reading + xseperator + xremarks + "!~!~!";

                                ArrayList<team> result = finapi.getApi(fgen.mcd, "apowcon_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), fgen.btnid, "-");
                                boolean msg = finapi.showAlertMessage(frm_record_alectricity_usage.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    private void Highlightborder() {
        txtst_reading.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txtend_reading.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    private void initializeViews() {
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_scan = findViewById(R.id.btn_scan);
        txtremarks = findViewById(R.id.txtremarks);
        txtst_reading = findViewById(R.id.txtst_meter_reading);
        txtend_reading = findViewById(R.id.txtend_meter_reading);
        toolbar = findViewById(R.id.toolbar);
        txtmachine_qr_code = findViewById(R.id.txtmachine_qr_code);
    }

    private void disableViewsMethod() {
        btn_save.setEnabled(false);
        btn_scan.setEnabled(false);
        txtst_reading.setEnabled(false);
        txtremarks.setEnabled(false);
        txtend_reading.setEnabled(false);
    }

    private void enabledViewMethod() {
        btn_scan.setEnabled(true);
        txtst_reading.setEnabled(true);
        txtremarks.setEnabled(true);
        txtend_reading.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtend_reading.setText("");
        txtst_reading.setText("");
        txtremarks.setText("");
        txtmachine_qr_code.setText("");
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
        if(controll.equals("txtmachine_qr_code")) txtapi_code.setText("EP831");
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
                    case "txtmachine_qr_code" : txtmachine_qr_code.setText(list_selected_text.trim());
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });

    }
}