package com.finsyswork.erp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class frm_sale_lead_followup extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button btn_new,btn_save, btn_cancel;
    private EditText txtremarks, txtcustomer_name, txtlead, txtspoken_to, txtreason, txtdate, txtorder_qty;
    Toolbar toolbar;
    public static ArrayList<String> xcustomer_name = new ArrayList<>();
    public static ArrayList<String> xlead = new ArrayList<>();
    public static ArrayList<String> xreason = new ArrayList<>();
    String xmbranch="00", xmv_type="CO", xseperator = "#~#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_sale_lead_followup);

        finapi.context = frm_sale_lead_followup.this;
        finapi.deleteJsonResponseFile();

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        initializeViews();
        new AsyncCallerForFrmSaleLeadFollowUp().execute();

        finapi.setColorOfStatusBar(frm_sale_lead_followup.this);
        btn_cancel.setText("EXIT");

        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        disableViewsMethod();
        setSupportActionBar(toolbar);

        Highlightborder();
        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialogue();
            }
        });

        txtcustomer_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_sale_lead_followup.this, xcustomer_name, "txtcustomer_name");
            }
        });

        txtlead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_sale_lead_followup.this, xlead, "txtlead");
            }
        });

        txtreason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_sale_lead_followup.this, xreason, "txtreason");
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
                    fgen.showApiName(frm_sale_lead_followup.this, "aSmvisit_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_sale_lead_followup.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtcustomer_name.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Select Customer Name!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtlead.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Select Lead!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtspoken_to.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Fill Spoken To Field!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtreason.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Select Reason!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtorder_qty.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Select Tentative Order Qty!!", Toast.LENGTH_LONG).show();
                    return;
                }
                String xmv_customer_name = txtcustomer_name.getText().toString();
                String xmv_lead = txtlead.getText().toString();
                String xmv_spoken_to = txtspoken_to.getText().toString();
                String xmv_reason = txtreason.getText().toString();
                String xmv_date = txtdate.getText().toString();
                String xmv_amount = txtorder_qty.getText().toString();
                String xmv_remarks = txtremarks.getText().toString();
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_sale_lead_followup.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = xmbranch + xseperator + xmv_type + xseperator + xmv_customer_name
                                        + xseperator + xmv_lead + xseperator + xmv_spoken_to + xseperator
                                        + xmv_reason + xseperator + xmv_date + xseperator + xmv_amount + xseperator
                                        + xmv_remarks + "!~!~!";
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aSmvisit_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP902");
                                boolean msg = finapi.showAlertMessage(frm_sale_lead_followup.this, result);
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

    private void Highlightborder() {
        txtcustomer_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtlead.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtspoken_to.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtorder_qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        if(controll.equals("txtcustomer_name")) txtapi_code.setText("EP826");
        if(controll.equals("txtlead")) txtapi_code.setText("EP826");
        if(controll.equals("txtreason")) txtapi_code.setText("EP826");

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
                    case "txtcustomer_name" : txtcustomer_name.setText(list_selected_text);
                        break;
                    case "txtlead" : txtlead.setText(list_selected_text);
                        break;
                    case "txtreason" : txtreason.setText(list_selected_text);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });

    }

    private void enabledViewMethod() {
        txtcustomer_name.setEnabled(true);
        txtlead.setEnabled(true);
        txtspoken_to.setEnabled(true);
        txtreason.setEnabled(true);
        txtdate.setEnabled(true);
        txtorder_qty.setEnabled(true);
        txtremarks.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtremarks.setText("");
        txtcustomer_name.setText("");
        txtlead.setText("");
        txtspoken_to.setText("");
        txtreason.setText("");
        txtdate.setText("");
        txtorder_qty.setText("");
        txtremarks.setText("");
    }

    private void disableViewsMethod() {
        txtcustomer_name.setEnabled(false);
        txtlead.setEnabled(false);
        txtspoken_to.setEnabled(false);
        txtreason.setEnabled(false);
        txtdate.setEnabled(false);
        txtorder_qty.setEnabled(false);
        txtremarks.setEnabled(false);
    }

    private void initializeViews() {
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        toolbar = findViewById(R.id.toolbar);
        txtcustomer_name = findViewById(R.id.txtcustomer_name);
        txtlead = findViewById(R.id.txtlead);
        txtspoken_to = findViewById(R.id.txtspoken_to);
        txtreason = findViewById(R.id.txtreason);
        txtdate = findViewById(R.id.txtnext_date_follow_up);
        txtorder_qty = findViewById(R.id.txtorder_qty);
        txtremarks = findViewById(R.id.txtremarks);
    }



    private void showDatePickerDialogue() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int i, int i1, int i2) {
        String day = "";
        String month = "";
        if(String.valueOf(i2).length() == 1) {
            day = "0" + String.valueOf(i2);
        }
        else{
            day = String.valueOf(i2);
        }

        if(String.valueOf(i1).length() == 1) {
            month = "0" + String.valueOf(i1 + 1);
            if(i1 >=9)
            {
                month = String.valueOf(i1 + 1);
            }
        }
        else {
            month = String.valueOf(i1);
            if(i1 > 9)
            {
                month = String.valueOf(i1 + 1);
            }
        }
        String date = (day +"/"+ month +"/"+ i).toString();
        txtdate.setText(date);
    }
}


class AsyncCallerForFrmSaleLeadFollowUp extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread

    }
    @Override
    protected Void doInBackground(Void... params) {
        frm_sale_lead_followup.xcustomer_name = new ArrayList<>();
        frm_sale_lead_followup.xlead = new ArrayList<>();
        frm_sale_lead_followup.xreason = new ArrayList<>();
        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
        frm_sale_lead_followup.xcustomer_name = finapi.fill_record_in_listview_popup("EP826");
        frm_sale_lead_followup.xlead = finapi.fill_record_in_listview_popup("EP826");
        frm_sale_lead_followup.xreason = finapi.fill_record_in_listview_popup("EP826");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
    }
}
