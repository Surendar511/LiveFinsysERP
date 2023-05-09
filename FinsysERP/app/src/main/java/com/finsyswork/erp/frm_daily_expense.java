package com.finsyswork.erp;

import android.app.Dialog;
import android.content.Context;
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

public class frm_daily_expense extends AppCompatActivity {

    Button btn_save,btn_new, btn_cancel;
    Toolbar toolbar;
    public static EditText txtexpense_name, txtnature_of_expense, txtpaid_to, txtamount_paid, txtremarks;
    public static ArrayList<String> xexpense_name = new ArrayList<>();
    public static ArrayList<String> xnature_of_expense = new ArrayList<>();
    String xseperator = "#~#", xbranch="00", xtype ="31";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_daily_expense);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_daily_expense.this;
        finapi.deleteJsonResponseFile();

        initializeViews();
        finapi.setColorOfStatusBar(frm_daily_expense.this);
        setSupportActionBar(toolbar);
        btn_cancel.setText("EXIT");

        final MyProgressdialog progressDialog = new MyProgressdialog(frm_daily_expense.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        xexpense_name = finapi.fill_record_in_listview_popup("EP835");
                        xnature_of_expense = finapi.fill_record_in_listview_popup("EP835");
                        progressDialog.dismiss();
                    }
                }, 100);

        txtexpense_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_daily_expense.this, xexpense_name , "txtexpense_name");
            }
        });
        txtnature_of_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_daily_expense.this, xnature_of_expense , "txtnature_of_expense");
            }
        });


        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBtnClickedMethod();
                enabledViewMethod();
                btn_save.setEnabled(true);
                btn_new.setEnabled(false);
                btn_cancel.setText("CANCEL");

                txtexpense_name.setBackgroundResource(R.color.light_blue);
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

                    txtexpense_name.setBackgroundResource(R.color.light_grey);
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
                    fgen.showApiName(frm_daily_expense.this, "areel_issue");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_daily_expense.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xmv_expense_name = txtexpense_name.getText().toString();
                String xmv_nature_of_expense = txtnature_of_expense.getText().toString();
                String xmv_paid_to = txtpaid_to.getText().toString();
                String xmv_amount_paid = txtamount_paid.getText().toString();
                String xmv_remarks = txtremarks.getText().toString();

                if(xmv_expense_name.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Select, Expense Name!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(xmv_nature_of_expense.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Select, Nature Of Expense!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(xmv_paid_to.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Fill, Paid To Field!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(xmv_amount_paid.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Fill, Amount Paid Field!!", Toast.LENGTH_LONG).show();
                    return;
                }

                final MyProgressdialog progressDialog = new MyProgressdialog(frm_daily_expense.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = "";
                                post_param = xbranch + xseperator + xtype + xseperator + xmv_expense_name + xseperator
                                        + xmv_nature_of_expense + xseperator + xmv_paid_to + xseperator + xmv_amount_paid
                                        + xseperator + xmv_remarks + "!~!~!";
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "areel_issue",post_param, fgen.muname, fgen.cdt1.substring(6,10), fgen.btnid, "-");
                                boolean msg = finapi.showAlertMessage(frm_daily_expense.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    private void enabledViewMethod() {
        txtexpense_name.setEnabled(true);
        txtnature_of_expense.setEnabled(true);
        txtpaid_to.setEnabled(true);
        txtamount_paid.setEnabled(true);
        txtremarks.setEnabled(true);
    }

    private void disableViewsMethod() {
        txtexpense_name.setEnabled(false);
        txtnature_of_expense.setEnabled(false);
        txtpaid_to.setEnabled(false);
        txtamount_paid.setEnabled(false);
        txtremarks.setEnabled(false);
    }

    private void newBtnClickedMethod() {
        txtexpense_name.setText("");
        txtnature_of_expense.setText("");
        txtpaid_to.setText("");
        txtamount_paid.setText("");
        txtremarks.setText("");
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
        TextView txtapi_code = dialog.findViewById(R.id.txtapi_code);
        ListView search_list = dialog.findViewById(R.id.seach_list);
        ImageView close_window = dialog.findViewById(R.id.close_window);

        if(controll.equals("txtexpense_name")) txtapi_code.setText("EP821");
        if(controll.equals("txtnature_of_expense")) txtapi_code.setText("EP821");

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
                    case "txtexpense_name" : txtexpense_name.setText(list_selected_text);
                        break;
                    case "txtnature_of_expense" : txtnature_of_expense.setText(list_selected_text);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });

    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        btn_save = findViewById(R.id.btn_save);
        btn_new = findViewById(R.id.btn_new);
        btn_cancel = findViewById(R.id.btn_cancel);
        txtexpense_name = findViewById(R.id.txtexpense_name);
        txtnature_of_expense = findViewById(R.id.txtnature_of_expense);
        txtpaid_to = findViewById(R.id.txtpaid_to);
        txtamount_paid = findViewById(R.id.txtamount_paid);
        txtremarks = findViewById(R.id.txtremarks);
    }
}