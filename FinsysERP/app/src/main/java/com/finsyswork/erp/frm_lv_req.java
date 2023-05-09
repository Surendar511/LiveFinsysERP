package com.finsyswork.erp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class frm_lv_req extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    TextView txt_lv_req_dt, txt_lv_start_dt, txt_leave_end_dt, txt_leave_time, txt_return_time, txt_approval_person;
    EditText txt_lv_req_no, txt_card_no,txt_total_days, txt_time_in_hr, txt_alt_contact_person, txt_alt_contact_number, txt_section,
             txt_emp_name,txt_service_year_no, txt_airline_name, txt_dest_from, txt_dest_to, txt_ticket_no, txt_leave_add, txt_remarks
            , txtleave_type, txt_department, txt_desgination;
    Button btn_save,btn_new, btn_cancel;
    LinearLayout linear_airline, linear_ticket_no, linear_leave_type, linear_department, linear_designation;
    Toolbar toolbar;
    AutoCompleteTextView  txt_reason;
    sqliteHelperClass Helper;
    ArrayList<String> xemp_name = new ArrayList<>();
    ArrayList<String> xreason = new ArrayList<>();
    ArrayList<String> xleave_type = new ArrayList<>();
    ArrayList<String> xdepartment = new ArrayList<>();
    ArrayList<String> xdesignation = new ArrayList<>();
    String xClickView = "";
    String xbranch = "00";
    String xtype = "LR", xmv_EMPCODE="", xmv_Lreason1= "", xmv_levfrom="", xmv_levupto="", xmv_CONT_NAME="", xmv_CONT_NO ="", xmv_LRemarks="",
            xmv_LV_TIME="", xmv_RET_TIME="", xmv_TOT_DAYS="", xmv_TIME_IN_HRS="", xmv_LVSection="", xmv_DesFrom="", xmv_DesTo="", xmv_LvServYrNo="",
            xmv_TicketNo="", xmv_Airlinename="", xmv_LvAddress="", xmv_ExitReEntryEmp="", xmv_chkvisafam="", xmv_ExitReEntryfam=""
            , xmv_department="", xmv_designation="", xmv_leave_typee="";
    String xseperator = "#~#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm__lv__req);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.setColorOfStatusBar(frm_lv_req.this);

        finapi.context = frm_lv_req.this;
        finapi.deleteJsonResponseFile();

        finapi.context = frm_lv_req.this;
        finapi.deleteJsonResponseFile();

        new Thread(new Runnable() {
            public void run() {
//               xdepartment = finapi.issue_slip_fill_record_in_listview_popup("EP819");
//               xdesignation = finapi.issue_slip_fill_record_in_listview_popup("EP818R");
               xleave_type = finapi.fill_record_in_listview_popup("EP832C");
            }
        }).start();

        Helper = new sqliteHelperClass(this);
        Helper.clear_data();

        initialize_views();

        switch (fgen.mcd)
        {
            case "SGRP" :
                break;
            default:
                linear_leave_type.setVisibility(View.VISIBLE);
                linear_department.setVisibility(View.VISIBLE);
                linear_designation.setVisibility(View.VISIBLE);
                linear_airline.setVisibility(View.GONE);
                linear_ticket_no.setVisibility(View.GONE);
                break;
        }

        txtleave_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_lv_req.this, xleave_type, "txtleave_type");
            }
        });
//        txt_department.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShowDialogBox(frm_lv_req.this, xdepartment, "txt_department");
//            }
//        });
//        txt_desgination.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShowDialogBox(frm_lv_req.this, xdesignation, "txt_desgination");
//            }
//        });


        txt_emp_name.setText(fgen.contactno);
        setTextOnViews();
        disableViewsMethod();
        setSupportActionBar(toolbar);

        Highlightborder();

        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBtnClickedMethod();
                enabledViewMethod();
                btn_save.setEnabled(true);
                btn_new.setEnabled(false);
                txt_total_days.setEnabled(false);
                btn_cancel.setText("CANCEL");
                txt_emp_name.setText(fgen.contactno);
                txt_department.setText(fgen.department);
                txt_desgination.setText(fgen.designation);

                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);

                txt_emp_name.setBackgroundResource(R.color.light_blue);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    txt_lv_req_dt.setText(dtf.format(now));
                    Log.d("currentDate : ", txt_lv_req_dt.getText().toString());
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_lv_req.this);
                return false;
            }
        });

          toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_lv_req.this);
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
                    txt_lv_req_no.setEnabled(false);
                    txt_lv_req_dt.setEnabled(false);
                    btn_save.setEnabled(false);
                    btn_new.setEnabled(true);
                    btn_cancel.setText("EXIT");

                    txt_emp_name.setBackgroundResource(R.color.light_grey);
                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                    txt_emp_name.setText(fgen.contactno);
                    txt_department.setText(fgen.department);
                    txt_desgination.setText(fgen.designation);
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });

        txt_lv_start_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xClickView = "txt_lv_start_dt";
                showDatePickerDialogue();
            }
        });

        txt_leave_end_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xClickView = "txt_leave_end_dt";
                showDatePickerDialogue();
            }
        });

        txt_leave_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(frm_lv_req.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txt_leave_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        txt_return_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(frm_lv_req.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txt_return_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_lv_req.this, "aLeave_ins");
                }
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xmv_EMPCODE = txt_emp_name.getText().toString();
                xmv_Lreason1 = txt_reason.getText().toString();
                xmv_levfrom = txt_lv_start_dt.getText().toString();
                xmv_levupto = txt_leave_end_dt.getText().toString();
                xmv_CONT_NAME = txt_alt_contact_person.getText().toString();
                xmv_CONT_NO = txt_alt_contact_number.getText().toString();
                xmv_LRemarks = txt_remarks.getText().toString();
                xmv_LV_TIME = txt_leave_time.getText().toString();
                xmv_RET_TIME = txt_return_time.getText().toString();
                xmv_TOT_DAYS = txt_total_days.getText().toString();
                xmv_TIME_IN_HRS = txt_time_in_hr.getText().toString();
                xmv_LVSection = txt_section.getText().toString();
                xmv_DesFrom = txt_dest_from.getText().toString();
                xmv_DesTo = txt_dest_to.getText().toString();
                xmv_LvServYrNo = txt_service_year_no.getText().toString();
                xmv_TicketNo = txt_ticket_no.getText().toString();
                xmv_Airlinename = txt_airline_name.getText().toString();
                xmv_LvAddress = txt_leave_add.getText().toString();
                xmv_department = txt_department.getText().toString() ;
                xmv_designation = txt_desgination.getText().toString();
                xmv_leave_typee = txtleave_type.getText().toString();
                xmv_ExitReEntryEmp = "-";
                xmv_chkvisafam = "-";
                xmv_ExitReEntryfam = "-";

                boolean check = xcheck_compulsory_element();
                if(!check)
                {   return;}

                if(txt_lv_req_dt.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Fill LV Req Dt", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_lv_req.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = xbranch + xseperator + xtype + xseperator + xmv_EMPCODE + xseperator + xmv_Lreason1 + xseperator + xmv_levfrom
                                        + xseperator + xmv_levupto + xseperator + xmv_CONT_NAME + xseperator + xmv_CONT_NO + xseperator + xmv_LRemarks  +xseperator
                                        + xmv_LV_TIME + xseperator + xmv_RET_TIME + xseperator + xmv_TOT_DAYS + xseperator + xmv_TIME_IN_HRS + xseperator + xmv_LVSection
                                        + xseperator + xmv_DesFrom + xseperator + xmv_DesTo + xseperator + xmv_LvServYrNo + xseperator + xmv_TicketNo + xseperator
                                        + xmv_Airlinename + xseperator + xmv_LvAddress + xseperator + xmv_ExitReEntryEmp + xseperator + xmv_chkvisafam + xseperator
                                        + xmv_ExitReEntryfam  + "!~!~!";

                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aLeave_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP904");
                                boolean msg = finapi.showAlertMessage(frm_lv_req.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                    LocalDateTime now = LocalDateTime.now();
                                    txt_lv_req_dt.setText(dtf.format(now));
                                    Log.d("currentDate : ", txt_lv_req_dt.getText().toString());
                                }
                                txt_emp_name.setText(fgen.contactno);
                                txt_department.setText(fgen.department);
                                txt_desgination.setText(fgen.designation);
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });

        txt_lv_start_dt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("total ", "text get changed");
                String xstart_dt = txt_lv_start_dt.getText().toString();
                String xend_dt = txt_leave_end_dt.getText().toString();
                String xleave_time = txt_leave_time.getText().toString();
                String xreturn_time = txt_return_time.getText().toString();
                if(!xstart_dt.isEmpty() && !xend_dt.isEmpty()){
                    total_days_count(xstart_dt, xend_dt);
                }
                if(!xstart_dt.isEmpty() && !xend_dt.isEmpty() && !xleave_time.isEmpty() && !xreturn_time.isEmpty()){
                    String Start_from = xstart_dt +" "+ xleave_time + ":00";
                    String End_From = xend_dt + " " + xreturn_time + ":00";
                    Log.d("full_date ", Start_from);
                    Log.d("full_date ", End_From);
                    total_time_count(Start_from, End_From);
                }
            }
        });

        txt_leave_end_dt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void afterTextChanged(Editable editable) {
                String xstart_dt = txt_lv_start_dt.getText().toString();
                String xend_dt = txt_leave_end_dt.getText().toString();
                String xleave_time = txt_leave_time.getText().toString();
                String xreturn_time = txt_return_time.getText().toString();
                if(!xstart_dt.isEmpty() && !xend_dt.isEmpty()){
                    total_days_count(xstart_dt, xend_dt);
                }
                if(!xstart_dt.isEmpty() && !xend_dt.isEmpty() && !xleave_time.isEmpty() && !xreturn_time.isEmpty()){
                    String Start_from = xstart_dt +" "+ xleave_time + ":00";
                    String End_From = xend_dt + " " + xreturn_time + ":00";
                    Log.d("full_date ", Start_from);
                    Log.d("full_date ", End_From);
                    total_time_count(Start_from, End_From);
                }
            }
        });

        txt_leave_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                NumberFormat f = new DecimalFormat("00");
                String xstart_dt = txt_lv_start_dt.getText().toString();
                String xend_dt = txt_leave_end_dt.getText().toString();
                String xleave_time = txt_leave_time.getText().toString();
                String xreturn_time = txt_return_time.getText().toString();
                if(!xstart_dt.isEmpty() && !xend_dt.isEmpty() && !xleave_time.isEmpty() && !xreturn_time.isEmpty()){
                    String Start_from = xstart_dt +" "+ xleave_time + ":00";
                    String End_From = xend_dt + " " + xreturn_time + ":00";
                    Log.d("full_date ", Start_from);
                    Log.d("full_date ", End_From);
                    total_time_count(Start_from, End_From);
                }
            }
        });

        txt_return_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                NumberFormat f = new DecimalFormat("00");
                String xstart_dt = txt_lv_start_dt.getText().toString();
                String xend_dt = txt_leave_end_dt.getText().toString();
                String xleave_time = txt_leave_time.getText().toString();
                String xreturn_time = txt_return_time.getText().toString();
                if(!xstart_dt.isEmpty() && !xend_dt.isEmpty() && !xleave_time.isEmpty() && !xreturn_time.isEmpty()){
                    String Start_from = xstart_dt +" "+ xleave_time + ":00";
                    String End_From = xend_dt + " " + xreturn_time + ":00";
                    Log.d("full_date ", Start_from);
                    Log.d("full_date ", End_From);
                    total_time_count(Start_from, End_From);
                }
            }
        });
    }

    private boolean xcheck_compulsory_element() {
        if(xmv_EMPCODE.isEmpty())
        {
            txt_emp_name.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill EMP Mobile No.!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xmv_Lreason1.isEmpty())
        {
            txt_reason.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Reason!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xmv_levfrom.isEmpty())
        {
            txt_lv_start_dt.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Leave Start From!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xmv_levupto.isEmpty())
        {
            txt_leave_end_dt.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Leave Start Date!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!fgen.mcd.equals("SGRP")) {
            if (xmv_department.isEmpty()) {
                txt_department.setBackgroundResource(R.color.light_blue);
                Toast.makeText(getApplicationContext(), "Please, Select Department!!", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (xmv_designation.isEmpty()) {
                txt_desgination.setBackgroundResource(R.color.light_blue);
                Toast.makeText(getApplicationContext(), "Please, Select Designation!!", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (xmv_leave_typee.isEmpty()) {
                txtleave_type.setBackgroundResource(R.color.light_blue);
                Toast.makeText(getApplicationContext(), "Please, Select Leave Type!!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if(xmv_RET_TIME.isEmpty())
        {
            txt_return_time.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Return Time!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xmv_CONT_NAME.isEmpty())
        {
            txt_alt_contact_person.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Alternate Contact Person!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xmv_CONT_NO.isEmpty())
        {
            txt_alt_contact_number.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Alternate Contact Number!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xmv_DesFrom.isEmpty())
        {
            txt_dest_from.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Destination!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xmv_DesTo.isEmpty())
        {
            txt_dest_to.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Destination!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xmv_LvAddress.isEmpty())
        {
            txt_leave_add.setBackgroundResource(R.color.light_blue);
            Toast.makeText(getApplicationContext(), "Fill Leave Address!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void Highlightborder() {
        txt_reason.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        txt_leave_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txt_lv_start_dt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txt_leave_end_dt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txt_return_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txt_alt_contact_person.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txt_alt_contact_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txt_airline_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txt_dest_from.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txt_dest_to.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txt_ticket_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txt_leave_add.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txt_remarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });



    }


    private void total_time_count(String xstart_from, String xend_from){
        // Create an instance of the SimpleDateFormat class
        SimpleDateFormat obj = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date date1 = obj.parse(xstart_from);
            Date date2 = obj.parse(xend_from);
            long time_difference = date2.getTime() - date1.getTime();
            long days_difference = (time_difference / (1000*60*60*24)) % 365;
            long years_difference = (time_difference / (1000l*60*60*24*365));
            long seconds_difference = (time_difference / 1000)% 60;
            long minutes_difference = (time_difference / (1000*60)) % 60;

            long hours_difference = (time_difference / (1000*60*60)) % 24;
            txt_time_in_hr.setText(String.valueOf(hours_difference) +":"+ String.valueOf(minutes_difference));
        }
        catch (ParseException excep) {
            excep.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void total_days_count(String xstart_dt, String xend_dt){
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        final String start_dt = xstart_dt;
        final String end_dt = xend_dt;
        final LocalDate firstDate;
        final LocalDate secondDate;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            firstDate = LocalDate.parse(start_dt, formatter);
            secondDate = LocalDate.parse(end_dt, formatter);
            long days = ChronoUnit.DAYS.between(firstDate, secondDate);
            days = days + 1;
            txt_total_days.setText(String.valueOf(days));
        }
    }


    private void setTextOnViews() {
        btn_cancel.setText("EXIT");
    }

    private void disableViewsMethod() {
        btn_save.setEnabled(false);
        txt_lv_req_no.setEnabled(false);
        txt_lv_req_dt.setEnabled(false);
        txt_total_days.setEnabled(false);
        txt_time_in_hr.setEnabled(false);
        txt_emp_name.setEnabled(false);
        txt_reason.setEnabled(false);
        txt_lv_start_dt.setEnabled(false);
        txt_leave_end_dt.setEnabled(false);
        txt_alt_contact_person.setEnabled(false);
        txt_alt_contact_number.setEnabled(false);
        txt_remarks.setEnabled(false);
        txt_leave_time.setEnabled(false);
        txt_return_time.setEnabled(false);
        txt_section.setEnabled(false);
        txt_dest_from.setEnabled(false);
        txt_dest_to.setEnabled(false);
        txt_service_year_no.setEnabled(false);
        txt_ticket_no.setEnabled(false);
        txt_airline_name.setEnabled(false);
        txt_leave_add.setEnabled(false);
        txt_card_no.setEnabled(false);
    }

    private void newBtnClickedMethod() {
        txt_emp_name.setText("");
        txt_reason.setText("");
        txt_lv_start_dt.setText("");
        txt_leave_end_dt.setText("");
        txt_alt_contact_person.setText("");
        txt_alt_contact_number.setText("");
        txt_remarks.setText("");
        txt_leave_time.setText("");
        txt_return_time.setText("");
        txt_total_days.setText("");
        txt_time_in_hr.setText("");
        txt_section.setText("");
        txt_dest_from.setText("");
        txt_dest_to.setText("");
        txt_service_year_no.setText("");
        txt_ticket_no.setText("");
        txt_airline_name.setText("");
        txt_leave_add.setText("");
        txt_lv_req_dt.setText("");
        txt_lv_req_no.setText("");
        txt_card_no.setText("");
        txt_leave_time.setText("");
        txt_approval_person.setText("-");

        txt_reason.setBackgroundResource(R.color.light_grey);
        txt_lv_start_dt.setBackgroundResource(R.color.light_grey);
        txt_leave_end_dt.setBackgroundResource(R.color.light_grey);
        txt_leave_add.setBackgroundResource(R.color.light_grey);
        txt_dest_to.setBackgroundResource(R.color.light_grey);
        txt_dest_from.setBackgroundResource(R.color.light_grey);
        txt_alt_contact_number.setBackgroundResource(R.color.light_grey);
        txt_alt_contact_person.setBackgroundResource(R.color.light_grey);
        txt_return_time.setBackgroundResource(R.color.light_grey);
        txt_leave_time.setBackgroundResource(R.color.light_grey);
    }

    private void enabledViewMethod()
    {
        txt_lv_req_no.setEnabled(true);
        txt_lv_req_dt.setEnabled(true);
        txt_total_days.setEnabled(true);
        txt_time_in_hr.setEnabled(true);
        txt_reason.setEnabled(true);
        txt_lv_start_dt.setEnabled(true);
        txt_leave_end_dt.setEnabled(true);
        txt_alt_contact_person.setEnabled(true);
        txt_alt_contact_number.setEnabled(true);
        txt_remarks.setEnabled(true);
        txt_leave_time.setEnabled(true);
        txt_return_time.setEnabled(true);
        txt_section.setEnabled(true);
        txt_dest_from.setEnabled(true);
        txt_dest_to.setEnabled(true);
        txt_service_year_no.setEnabled(true);
        txt_ticket_no.setEnabled(true);
        txt_airline_name.setEnabled(true);
        txt_leave_add.setEnabled(true);
        txt_card_no.setEnabled(true);
        txt_emp_name.setEnabled(true);
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

    private void initialize_views() {
        toolbar =  findViewById(R.id.toolbar);
        btn_save = findViewById(R.id.btn_save);
        btn_new = findViewById(R.id.btn_new);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_save = findViewById(R.id.btn_save);
        txt_lv_req_no = findViewById(R.id.txt_lv_req_no);
        txt_lv_req_dt = findViewById(R.id.txt_lv_req_dt);
        txt_emp_name = findViewById(R.id.txt_emp_name);
        txt_reason = findViewById(R.id.txt_reason);
        txt_department = findViewById(R.id.txt_department);
        txt_desgination = findViewById(R.id.txt_designation);
        txt_lv_start_dt = findViewById(R.id.txt_lv_start_dt);
        txt_card_no = findViewById(R.id.txt_card_no);
        txt_leave_time = findViewById(R.id.txt_leave_time);
        txt_return_time = findViewById(R.id.txt_return_time);
        txt_total_days = findViewById(R.id.txt_total_days);
        txt_time_in_hr = findViewById(R.id.txt_time_in_hr);
        txt_alt_contact_person = findViewById(R.id.txt_grade);
        txt_alt_contact_number = findViewById(R.id.txt_cur_salary);
        txt_section = findViewById(R.id.txt_section);
        txt_service_year_no = findViewById(R.id.txt_service_year_no);
        txt_airline_name = findViewById(R.id.txt_loan_paid);
        txt_dest_from = findViewById(R.id.txt_inst_amt);
        txt_dest_to = findViewById(R.id.txt_dest_to);
        txt_ticket_no = findViewById(R.id.txt_ticket_no);
        txt_leave_add = findViewById(R.id.txt_leave_add);
        txt_remarks = findViewById(R.id.txt_remarks);
        txt_leave_end_dt = findViewById(R.id.txt_leave_end_dt);
        txtleave_type = findViewById(R.id.txt_leave_type);
        txt_approval_person = findViewById(R.id.txt_approved_person);

        linear_airline = findViewById(R.id.linear_air_line);
        linear_ticket_no = findViewById(R.id.linear_ticket_no);
        linear_leave_type = findViewById(R.id.linear_lave_type);
        linear_department = findViewById(R.id.linear_department);
        linear_designation = findViewById(R.id.linear_designation);
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

        xreason.add("Reason 1");
        xreason.add("Reason 2");
        xreason.add("Reason 3");
        xreason.add("Reason 4");
        xreason.add("Reason 5");
        xreason.add("Reason 6");
        xreason.add("Reason 7");
        xreason.add("Reason 8");
        xreason.add("Reason 9");
        xreason.add("Reason 10");

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
        if(xClickView == "txt_leave_end_dt")
        {
            txt_leave_end_dt.setText(date);
        }
        if(xClickView == "txt_lv_req_dt") {
            txt_lv_req_dt.setText(date);
        }
        if(xClickView == "txt_lv_start_dt"){
            txt_lv_start_dt.setText(date);
        }

        try {
            compare_dates_method(xClickView);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void compare_dates_method(String xClickView) throws ParseException {

        SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");
        Date xinput_dt = null;
        try {
            if(xClickView == "txt_lv_start_dt") {
                if(txt_lv_start_dt.getText().toString().isEmpty())
                {
                    return;
                }
                xinput_dt = sdf.parse(txt_lv_start_dt.getText().toString());
            }
            else {
                if(txt_leave_end_dt.getText().toString().isEmpty())
                {
                    return;
                }
                xinput_dt = sdf.parse(txt_leave_end_dt.getText().toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String xReturn_dt = txt_leave_end_dt.getText().toString();
        String xleave_dt = txt_lv_start_dt.getText().toString();
        if(xReturn_dt.toString().isEmpty()) {
            xReturn_dt = xleave_dt;
        }
        Date Return_dt = sdf.parse(xReturn_dt);
        Date Leave_dt = sdf.parse(xleave_dt);

        Date xcurr_dt = new Date();
        String strDate= sdf.format(xcurr_dt);
        Date curr_dt = null;
        try {
            curr_dt = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(xinput_dt.compareTo(curr_dt) >= 0) {
            if(xinput_dt.after(Return_dt)) {
                if (txt_leave_end_dt.getText().toString().isEmpty()) {

                } else {
                    txt_lv_start_dt.setText("");
                }
            }
            if(xinput_dt.before(Leave_dt)){
                if(txt_lv_start_dt.getText().toString().isEmpty()){

                }
                else {
                    txt_leave_end_dt.setText("");
                }

            }
        } else {
            if(xClickView == "txt_lv_start_dt") {
                txt_lv_start_dt.setText("");
            }
            else {
                txt_leave_end_dt.setText("");
            }
            Toast.makeText(getApplicationContext(), "Date Should Not Be < Current Dt!!", Toast.LENGTH_LONG).show();
        }
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
        txtapi_code.setText("EP832C");

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
                    case "txtleave_type" : txtleave_type.setText(list_selected_text);
                        break;
                    case "txt_department" : txt_department.setText(list_selected_text);
                        break;
                    case "txt_desgination" : txt_desgination.setText(list_selected_text);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });

    }

}