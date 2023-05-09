package com.finsyswork.erp;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;


public class frm_adv_pay extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView txt_adv_req_dt, txt_pay_ins_st_dt;
    EditText txt_adv_req_no, txt_card_no, txt_grade, txt_cur_salary,
             txt_adv_paid, txt_inst_amt, txt_remarks, txt_emp_name;
    Button btn_save,btn_new, btn_list, btn_cancel;
    Toolbar toolbar;
    ArrayAdapter<String> adapter;
    AutoCompleteTextView  txt_department;
    sqliteHelperClass Helper;
    ArrayList<String> xemp_name = new ArrayList<>();
    ArrayList<String> xreason = new ArrayList<>();
    String xbranch = "00";
    String xtype = "RA", xmv_EMPCODE="", xmv_deptt= "", xmv_dr_amt, xmv_cr_amt, xmv_inst_st_dt,xmv_cur_loan, xmv_os_amt, xmv_grade, xmv_cur_salary,xmv_inst_amt, xmv_LRemarks;
    String xseperator = "#~#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_adv_pay);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);


        finapi.setColorOfStatusBar(frm_adv_pay.this);

        finapi.context = frm_adv_pay.this;
        finapi.deleteJsonResponseFile();



        Helper = new sqliteHelperClass(this);
        Helper.clear_data();

        initialize_views();

        txt_emp_name.setText(fgen.contactno);
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        initialize_list();
        setTextOnViews();
        disableViewsMethod();
        Highlightborder();
        setSupportActionBar(toolbar);


        txt_pay_ins_st_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialogue();
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
                txt_department.setText(fgen.department);
                txt_grade.setText(fgen.designation);

                finapi.context = frm_adv_pay.this;

                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);

                txt_emp_name.setBackgroundResource(R.color.light_blue);
                txt_emp_name.setText(fgen.contactno);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    txt_adv_req_dt.setText(dtf.format(now));
                    Log.d("currentDate : ", txt_adv_req_dt.getText().toString());
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_adv_pay.this);
                return false;
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
                    txt_adv_req_no.setEnabled(false);
                    txt_adv_req_dt.setEnabled(false);
                    btn_save.setEnabled(false);
                    btn_new.setEnabled(true);
                    btn_cancel.setText("EXIT");
                    txt_emp_name.setBackgroundResource(R.color.light_grey);
                    txt_emp_name.setText(fgen.contactno);

                    txt_department.setText(fgen.department);
                    txt_grade.setText(fgen.designation);

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
                    fgen.showApiName(frm_adv_pay.this, "aAdvance_ins");
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xmv_EMPCODE = txt_emp_name.getText().toString();
                xmv_deptt = txt_department.getText().toString();
                xmv_grade = txt_grade.getText().toString();
                xmv_dr_amt = txt_adv_paid.getText().toString();
                xmv_cr_amt = "0";
                xmv_inst_amt = txt_inst_amt.getText().toString();
                xmv_inst_st_dt = txt_pay_ins_st_dt.getText().toString();
                xmv_os_amt = "0";
                xmv_LRemarks = txt_remarks.getText().toString();
                xmv_cur_salary = txt_cur_salary.getText().toString();
                if(xmv_cur_salary.equals("")){xmv_cur_salary = "0";}

                boolean check = xcheck_compulsory_element();
                if(!check)
                {   return;}

                final MyProgressdialog progressDialog = new MyProgressdialog(frm_adv_pay.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
//                                ArrayList<team> result = finapi.getApi(fgen.mcd, "avg_emp_phone",xmv_EMPCODE, fgen.muname, fgen.cdt1.substring(6,10), "#", "#");
//                                Gson gson = new Gson();
//                                String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());
//                                String data= "";
//                                try {
//                                    JSONArray array = new JSONArray(listString);
//                                    for(int i=0; i< array.length(); i++){
//                                        JSONObject respObj = array.getJSONObject(i);
//                                        data = respObj.getString("col1");
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                if(!data.equals("Success"))
//                                {
//                                    new AlertDialog.Builder(frm_adv_pay.this)
//                                            .setTitle("Error")
//                                            .setMessage("Sorry User Mobile No. Is Invalid!!")
//                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    dialog.dismiss();
//                                                }
//                                            })
//                                            .show();
//                                    return;
//                                }

                                String post_param = xbranch + xseperator + xtype + xseperator + xmv_EMPCODE  + xseperator + xmv_deptt  + xseperator + xmv_grade + xseperator + xmv_dr_amt
                                        + xseperator + xmv_cr_amt + xseperator + xmv_inst_amt + xseperator + xmv_inst_st_dt + xseperator + xmv_LRemarks + xseperator + xmv_cur_salary + "!~!~!";

                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aAdvance_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP905");
                                Log.d("result ", ""+result);
                                boolean msg = finapi.showAlertMessage(frm_adv_pay.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                    LocalDateTime now = LocalDateTime.now();
                                    txt_adv_req_dt.setText(dtf.format(now));
                                    Log.d("currentDate : ", txt_adv_req_dt.getText().toString());
                                }
                                txt_emp_name.setText(fgen.contactno);
                                txt_department.setText(fgen.department);
                                txt_grade.setText(fgen.designation);

                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    private boolean xcheck_compulsory_element() {
        if(xmv_EMPCODE.isEmpty())
        {
            txt_emp_name.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill EMP Mobile No!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xmv_deptt.isEmpty())
        {
            txt_department.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Department!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xmv_grade.isEmpty())
        {
            txt_grade.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Grade!!", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if(xmv_cur_salary.isEmpty())
//        {
//            txt_cur_salary.setBackgroundResource(R.color.light_blue);
//            Toast.makeText(getApplicationContext(), "Fill Current Salary!!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if(xmv_dr_amt.isEmpty())
        {
            txt_adv_paid.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Advanced Request!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xmv_inst_st_dt.isEmpty())
        {
            txt_pay_ins_st_dt.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Deduction Start Date!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xmv_inst_amt.isEmpty())
        {
            txt_inst_amt.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Inst Amt!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xmv_LRemarks.isEmpty())
        {
            txt_remarks.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Remarks Field!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setTextOnViews() {
        btn_cancel.setText("EXIT");
    }

    private void disableViewsMethod() {
        btn_save.setEnabled(false);
        txt_adv_req_no.setEnabled(false);
        txt_adv_req_dt.setEnabled(false);
        txt_emp_name.setEnabled(false);
        txt_department.setEnabled(false);
        txt_grade.setEnabled(false);
        txt_cur_salary.setEnabled(false);
        txt_remarks.setEnabled(false);
        txt_inst_amt.setEnabled(false);
        txt_adv_paid.setEnabled(false);
        txt_card_no.setEnabled(false);
    }

    private void newBtnClickedMethod() {
        txt_emp_name.setText("");
        txt_department.setText("");
        txt_grade.setText("");
        txt_cur_salary.setText("");
        txt_remarks.setText("");
        txt_inst_amt.setText("");
        txt_adv_paid.setText("");
        txt_adv_req_dt.setText("");
        txt_adv_req_no.setText("");
        txt_card_no.setText("");

        txt_inst_amt.setBackgroundResource(R.color.light_grey);
        txt_pay_ins_st_dt.setBackgroundResource(R.color.light_grey);
        txt_adv_paid.setBackgroundResource(R.color.light_grey);
        txt_cur_salary.setBackgroundResource(R.color.light_grey);
        txt_grade.setBackgroundResource(R.color.light_grey);
        txt_department.setBackgroundResource(R.color.light_grey);
        txt_emp_name.setBackgroundResource(R.color.light_grey);
        txt_remarks.setBackgroundResource(R.color.light_grey);
    }

    private void enabledViewMethod()
    {
        txt_adv_req_no.setEnabled(true);
        txt_adv_req_dt.setEnabled(true);
        txt_department.setEnabled(true);
        txt_grade.setEnabled(true);
        txt_cur_salary.setEnabled(true);
        txt_remarks.setEnabled(true);
        txt_inst_amt.setEnabled(true);
        txt_adv_paid.setEnabled(true);
        txt_card_no.setEnabled(true);
        txt_emp_name.setEnabled(true);
    }

    private void initialize_views() {
        txt_pay_ins_st_dt = findViewById(R.id.txt_pay_ins_st_dt);
        toolbar =  findViewById(R.id.toolbar);
        btn_save = findViewById(R.id.btn_save);
        btn_new = findViewById(R.id.btn_new);
        btn_list = findViewById(R.id.btn_list);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_save = findViewById(R.id.btn_save);
        txt_adv_req_no = findViewById(R.id.txt_loan_req_no);
        txt_adv_req_dt = findViewById(R.id.txt_loan_req_dt);
        txt_emp_name = findViewById(R.id.txt_emp_name);
        txt_department = findViewById(R.id.txt_department);
        txt_card_no = findViewById(R.id.txt_card_no);
        txt_grade = findViewById(R.id.txt_grade);
        txt_cur_salary = findViewById(R.id.txt_cur_salary);
        txt_adv_paid = findViewById(R.id.txt_loan_paid);
        txt_inst_amt = findViewById(R.id.txt_inst_amt);
        txt_remarks = findViewById(R.id.txt_remarks);

    }

    private void initialize_list(){
        xemp_name.add("EMP 1");
        xemp_name.add("EMP 2");
        xemp_name.add("EMP 3");
        xemp_name.add("EMP 4");
        xemp_name.add("EMP 5");
        xemp_name.add("EMP 6");
        xemp_name.add("EMP 7");
        xemp_name.add("EMP 8");
        xemp_name.add("EMP 9");
        xemp_name.add("EMP 10");

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
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
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
        txt_pay_ins_st_dt.setText(date);

    }

    private void Highlightborder() {
        txt_department.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txt_grade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txt_cur_salary.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txt_adv_paid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txt_inst_amt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txt_remarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
}