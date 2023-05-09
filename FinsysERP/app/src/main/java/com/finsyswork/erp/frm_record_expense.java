package com.finsyswork.erp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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

public class frm_record_expense extends AppCompatActivity {

    Spinner spinner_expense_list;
    Button btn_add, btn_new,btn_save, btn_cancel;
    Toolbar toolbar;
    EditText txtremarks, txtamount;
    RecyclerView recyclerView;
    record_expense_adapter adapter;
    sqliteHelperClass handler;
    String expense = "";
    String amount =  "";
    String remarks = "";
    String xseperator = "#~#", xbranch="00", xtype ="EX";

    ArrayList<String> expense_list = new ArrayList<>();

    public static ArrayList<expense_model> list = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_record_expense);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_record_expense.this;
        finapi.deleteJsonResponseFile();


        final MyProgressdialog progressDialog = new MyProgressdialog(frm_record_expense.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        expense_list = finapi.fill_record_in_listview_popup("EP822");

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(frm_record_expense.this
                                ,R.layout.spinner_text_color
                                ,expense_list);
                        spinner_expense_list.setAdapter(spinnerAdapter);
                        progressDialog.dismiss();
                    }
                }, 100);

        finapi.setColorOfStatusBar(frm_record_expense.this);
        initializeViews();
        disableViewsMethod();
        setSupportActionBar(toolbar);
        btn_cancel.setText("EXIT");
        spinner_expense_list.setAutofillHints("EP822");
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        handler.clear_data();
        list = handler.record_expense_get_data();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new record_expense_adapter(getApplicationContext());
        recyclerView.setAdapter(adapter);

        txtremarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }
                else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txtamount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }
                else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
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
                spinner_expense_list.setBackgroundColor(Color.parseColor("#FFFF00"));
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

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));handler.clear_data();
                    list = handler.record_expense_get_data();
                    adapter.notifyDataSetChanged();
                    spinner_expense_list.setBackgroundResource(R.color.light_grey);
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amount =  txtamount.getText().toString();
                remarks = txtremarks.getText().toString();

                if(spinner_expense_list == null)
                {
                    Toast.makeText(getApplicationContext(), "Please Create Expenses First!!", Toast.LENGTH_LONG).show();
                    return;
                }
                expense = spinner_expense_list.getSelectedItem().toString();
                if(expense_list.isEmpty() || txtamount.getText().toString().isEmpty() || txtremarks.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select All Fields First!!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(amount.length() >6 || amount.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Amount Shouldn't Be NULL OR Greater Than 6 Digits!!", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean check_duplicacy = handler.CheckDuplicacy("col1", expense);
                if(check_duplicacy)
                {
                    androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_record_expense.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Sorry, Duplicate Rows Are Not Allowed!!");
                    alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }
                handler.record_expense_insert_data(new expense_model(expense, amount, remarks));
                list = handler.record_expense_get_data();
                adapter.notifyDataSetChanged();
                newBtnClickedMethod();
            }
        });

        fgen.toolbar_click_count = 0;
        try {
//            toolbar.bringToFront();
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ++fgen.toolbar_click_count;
                    if (fgen.toolbar_click_count == 5) {
                        fgen.showApiName(frm_record_expense.this, "aSmexp_ins");
                    }
                }
            });
        }catch (Exception e){
            Log.e("erroe",""+e);
        }

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_record_expense.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_record_expense.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                list = handler.record_expense_get_data();
                                if(list.size() <=0)
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Invalid Data!!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                ArrayList<expense_model> bullets = list;
                                String post_param = "";
                                for(expense_model b : bullets) {
                                    expense = b.expense;
                                    amount = b.amount;
                                    remarks = b.remarks;
                                    post_param  += xbranch + xseperator + xtype + xseperator + expense + xseperator + amount + xseperator + remarks + "!~!~!";
                                }
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aSmexp_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP903");
                                String Message = "";
                                Gson gson = new Gson();
                                String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

                                try {
                                    JSONArray array = new JSONArray(listString);
                                    for(int i=0; i< array.length(); i++){
                                        JSONObject respObj = array.getJSONObject(i);
                                        Message = respObj.getString("col1");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                new AlertDialog.Builder(frm_record_expense.this)
                                        .setTitle("Message")
                                        .setMessage(Message)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                                if(Message.contains("Data Not Saved")){
                                    progressDialog.dismiss();
                                    return;
                                }
                                //Toast.makeText(getApplicationContext(), "Record Saved Successfully...", Toast.LENGTH_LONG).show();
                                newBtnClickedMethod();
                                handler.clear_data();
                                list = handler.record_expense_get_data();
                                adapter.notifyDataSetChanged();
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    private void disableViewsMethod() {
        btn_save.setEnabled(false);
        spinner_expense_list.setEnabled(false);
        txtremarks.setEnabled(false);
        txtamount.setEnabled(false);
        btn_add.setEnabled(false);
    }

    private void initializeViews() {
        handler = new sqliteHelperClass(this);
        spinner_expense_list = (Spinner) findViewById(R.id.spinner_expense_list);
        btn_add = findViewById(R.id.btn_add);
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        txtremarks = findViewById(R.id.txtremarks);
        txtamount = findViewById(R.id.txtamount);
        recyclerView = findViewById(R.id.recycler);
        toolbar = findViewById(R.id.toolbar);

    }

    private void enabledViewMethod() {
        txtremarks.setEnabled(true);
        txtamount.setEnabled(true);
        btn_add.setEnabled(true);
        spinner_expense_list.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtremarks.setText("");
        txtamount.setText("");

    }

}