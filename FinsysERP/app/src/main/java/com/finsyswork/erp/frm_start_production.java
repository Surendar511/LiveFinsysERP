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

import java.util.ArrayList;

public class frm_start_production extends AppCompatActivity {

    private Button btn_new,btn_save, btn_cancel, btn_scanItem, btn_ScanMachine;
    public static EditText txtmachine,txtitem,txtworkorder_no, txtdrawing_no,
            txtprocess,txtremarks;
    Toolbar toolbar;
    public static ArrayList<String> xprocess = new ArrayList<>();
    public static String xbtn_clicked = "";
    String xbranch = "00", xtype="SP",xseperator = "#~#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_start_production);


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_start_production.this;
        finapi.deleteJsonResponseFile();


        final MyProgressdialog progressDialog = new MyProgressdialog(frm_start_production.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        xprocess = finapi.fill_record_in_listview_popup("EP820C");
                        // On complete call either onLoginSuccess or onLoginFailed
                        progressDialog.dismiss();
                    }
                }, 100);

        initializeViews();
        Highlightborder();
        btn_cancel.setText("EXIT");
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        finapi.setColorOfStatusBar(frm_start_production.this);
        setSupportActionBar(toolbar);

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBtnClickedMethod();
                enabledViewMethod();
                btn_save.setEnabled(true);
                btn_new.setEnabled(false);
                btn_cancel.setText("CANCEL");

                txtmachine.setBackgroundResource(R.color.light_blue);

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
                    newBtnClickedMethod();
                    disableViewsMethod();
                    btn_save.setEnabled(false);
                    btn_new.setEnabled(true);
                    btn_cancel.setText("EXIT");

                    txtmachine.setBackgroundResource(R.color.light_grey);
                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });

        txtprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_start_production.this, xprocess, "txtprocess");
            }
        });

        btn_ScanMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fgen.frm_request = "frm_start_production";
                xbtn_clicked = "btn_ScanMachine";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
            }
        });

        btn_scanItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fgen.frm_request = "frm_start_production";
                xbtn_clicked = "btn_scanItem";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_start_production.this, "aprodmw_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_start_production.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtmachine.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please, Scan Machine QR Code First!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtitem.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please, Scan Punch QR Code First!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtworkorder_no.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please, Scan Punch QR Code Again!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtdrawing_no.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please, Scan Punch QR Code Again!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtprocess.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please, Select Process Code First!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                final MyProgressdialog progressDialog = new MyProgressdialog(frm_start_production.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String xmachine = txtmachine.getText().toString().trim();
                                String xitem = txtitem.getText().toString().trim();
                                String xworkorder_no = txtworkorder_no.getText().toString().trim();
                                String xdrawing_no =  txtdrawing_no.getText().toString().trim();
                                String xxprocess = txtprocess.getText().toString().trim();
                                String xremarks = txtremarks.getText().toString().trim();

                                // xworkorder no = bom number
                                // xitem = punch code
                                // xdrawing_no = xitem code
                                String post_param = xbranch + xseperator + xtype + xseperator + xmachine
                                        + xseperator +  xworkorder_no + xseperator + xitem + xseperator +
                                        xdrawing_no + xseperator + xxprocess + xseperator + xremarks+"!~!~!";

                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aprodmw_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                boolean msg = finapi.showAlertMessage(frm_start_production.this, result);
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

    private void disableViewsMethod() {
        txtmachine.setEnabled(false);
        txtitem.setEnabled(false);
        txtworkorder_no.setEnabled(false);
        txtdrawing_no.setEnabled(false);
        txtprocess.setEnabled(false);
        txtremarks.setEnabled(false);
    }

    private void enabledViewMethod() {
        txtmachine.setEnabled(true);
        txtitem.setEnabled(true);
        txtworkorder_no.setEnabled(true);
        txtdrawing_no.setEnabled(true);
        txtprocess.setEnabled(true);
        txtremarks.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtmachine.setText("");
        txtitem.setText("");
        txtworkorder_no.setText("");
        txtdrawing_no.setText("");
        txtprocess.setText("");
        txtremarks.setText("");
    }

    private void Highlightborder() {
        txtmachine.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtworkorder_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtdrawing_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtprocess.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        btn_ScanMachine = findViewById(R.id.btn_scanMachine);
        btn_scanItem = findViewById(R.id.btn_scanItem);
        toolbar = findViewById(R.id.toolbar);
        txtmachine = findViewById(R.id.txtmachine);
        txtitem = findViewById(R.id.txtitem);
        txtworkorder_no = findViewById(R.id.txt_work_order_no);
        txtdrawing_no = findViewById(R.id.txt_drawing_no);
        txtprocess = findViewById(R.id.txtprocess);
        txtremarks = findViewById(R.id.txtremarks);
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
        if(controll.equals("txtprocess")) txtapi_code.setText("EP820C");

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
                    case "txtprocess":
                        frm_start_production.txtprocess.setText(list_selected_text);
                        break;
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
        fgen.frm_request = "";
    }
}