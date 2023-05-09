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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class frm_lead_enquiry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    TextView txtappointment_dt;
    public static EditText  txtindustry, txtspecific_interest, txtparty_name, txtcontact_name, txtcontact_no, txtemail, txtqty
        , txtapprox_value, txt_remarks, txtour_remarks, txtsales_man, txtestimator, txtcountry_name, txtstate_name, txtcity_name
            ,txtfg_product;
    AutoCompleteTextView txtlead_source, txtlead_probability, txtfg_sub_group, txtcontact_level ;
    Button btn_save,btn_new, btn_list, btn_cancel;
    LinearLayout linearLayout_make_costing, linearLayout_cust_type, linearLayout_fg_sub_group, linearLayout_fg_product, linearLayout_salesmanager, linearLayout_estimator
            , linear_tab1, linear_udf_data;
    Toolbar toolbar;
    String xClickView = "";
    String xbranch = "00", xtype="LR", xmv_lead_source,xmv_industry, xmv_lead_probability, xmv_fg_sub_group, xmv_specific_interest
        ,xmv_party_name, xmv_contact_level, xmv_contact_name, xmv_contact_no, xmv_email, xmv_fg_product, xmv_qty, xmv_approx_value
            , xmv_appointment_dt, xmv_sales_man, xmv_estimator,  xmv_country_name, xmv_state_name,
        xmv_city_name, xmv_client_remarks, xmv_our_remarks, xmv_acode="-", xmv_icode="-";
    String xseperator = "#~#";
    String list_selected_text = "";
    RadioButton radio_yes, radio_no, radio_industry, radio_retailer;
    FloatingActionButton fab;
    public static String xfrm_clicked_tab = "";

    //==================== UDF Data  =====================

    public static TextView view1,view2,view3,view4,view5,view6,view7,view8,view9,view10,view11,view12,view13,view14,view15,view16,view17,
            view18,view19,view20;
    public static EditText textView1,textView2,textView3,textView4,textView5,textView6,textView7,textView8,textView9,textView10,textView11,
            textView12,textView13,textView14,textView15,textView16,textView17,textView18,textView19,textView20;
    public static LinearLayout linear_udf1,linear_udf2,linear_udf3,linear_udf4,linear_udf5,linear_udf6,linear_udf7,linear_udf8,linear_udf9,
            linear_udf10,linear_udf11,linear_udf12,linear_udf13,linear_udf14,linear_udf15,linear_udf16,linear_udf17,linear_udf18,
            linear_udf19,linear_udf20;
    public static ArrayList<String> xudf_data_views = new ArrayList<>();
    public static  String xcol6_udf_data = "", xparty_popup_sel="N";
    String xtext1,xtext2,xtext3,xtext4,xtext5,xtext6,xtext7,xtext8,xtext9,xtext10,xtext11,xtext12,xtext13,xtext14,xtext15,xtext16,xtext17,xtext18,
            xtext19,xtext20;

    //===============================================================

    public static String xfrm_lead_enquiry = "";
    public  static String xfg_sub_group_code= "";
    public static ArrayList<String> xlead_source = new ArrayList<>();
    public static ArrayList<String> xindustry_vertical = new ArrayList<>();
    public static ArrayList<String> xinquiry = new ArrayList<>();
    public static ArrayList<String> xcontact_level = new ArrayList<>();

    public static ArrayList<String> xcountry_list = new ArrayList<>();
    public static ArrayList<String> xstate_list = new ArrayList<>();
    public static ArrayList<String> xfg_product = new ArrayList<>();
    public static ArrayList<String> xparty_name = new ArrayList<>();
    public static ArrayList<String> xfg_sub_group = new ArrayList<>();
    public static ArrayList<String> xestimator = new ArrayList<>();
    final MyProgressdialog progressDialog = new MyProgressdialog(this);
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_lead_enquiry);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_lead_enquiry.this;
        finapi.deleteJsonResponseFile();

        fgen.frm_request = "";
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_lead_enquiry.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        frm_lead_enquiry.xlead_source = finapi.fill_record_in_listview_popup("EP826");
                        frm_lead_enquiry.xindustry_vertical = finapi.fill_record_in_listview_popup("EP827");
                        frm_lead_enquiry.xinquiry = finapi.fill_record_in_listview_popup("EP828");
                        frm_lead_enquiry.xcontact_level = finapi.fill_record_in_listview_popup("EP829");
                        frm_lead_enquiry.xfg_sub_group = finapi.fill_record_in_listview_popup("EP823");

                        new AsyncCaller().execute();
                        progressDialog.dismiss();
                    }
                }, 100);


        initializeViews();
        setTextOnViews();
        disableViewsMethod();
        setSupportActionBar(toolbar);
        Highlightborder();

        switch(fgen.mcd)
        {
            case "NEOP" :
                linearLayout_cust_type.setVisibility(View.VISIBLE);
                linearLayout_make_costing.setVisibility(View.GONE);
                linearLayout_fg_sub_group.setVisibility(View.GONE);
                linearLayout_fg_product.setVisibility(View.GONE);
                linearLayout_salesmanager.setVisibility(View.GONE);
                linearLayout_estimator.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        txtlead_source.setKeyListener(null);
        txtindustry.setKeyListener(null);
        txtlead_probability.setKeyListener(null);
        txtcontact_level.setKeyListener(null);
        txtcountry_name.setKeyListener(null);
        txtstate_name.setKeyListener(null);
        txtfg_sub_group.setKeyListener(null);
        txtparty_name.setKeyListener(null);
        txtfg_product.setKeyListener(null);

        radio_yes.setChecked(true);
        radio_industry.setChecked(true);


        txtlead_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_lead_enquiry.this, xlead_source, "txtlead_source");
            }
        });

        txtindustry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_lead_enquiry.this, xindustry_vertical, "txtindustry");
            }
        });

        txtlead_probability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_lead_enquiry.this, xinquiry, "txtlead_probability");
            }
        });

        txtcontact_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_lead_enquiry.this, xcontact_level, "txtcontact_level");
            }
        });

        txtcountry_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_lead_enquiry.this, xcountry_list, "txtcountry_name");
            }
        });

        txtstate_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_lead_enquiry.this, xstate_list, "txtstate_name");
            }
        });

        txtfg_sub_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_lead_enquiry.this, xfg_sub_group, "txtfg_sub_group");
            }
        });

         txtparty_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_lead_enquiry.this, xparty_name, "txtparty_name");
            }
        });

        txtestimator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_lead_enquiry.this, xestimator, "txtestimator");
            }
        });

       txtfg_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtfg_sub_group.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select FG SubGroup First!!", Toast.LENGTH_LONG).show();
                    return;
                }
                ShowDialogBox(frm_lead_enquiry.this, xfg_product, "txtfg_product");
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
                txtlead_source.setBackgroundResource(R.color.light_blue);
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

        txtappointment_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xClickView = "txtappointment_dt";
                showDatePickerDialogue();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i == 0) {
                    fab.setImageResource(R.drawable.left_arrow);
                    linear_tab1.setVisibility(View.GONE);
                    linear_udf_data.setVisibility(View.VISIBLE);

                    ArrayList<String> udf_text_views = frm_lead_enquiry.xudf_data_views;

                    for (String udf_views : udf_text_views)
                    {
                        String data = udf_views.split("---")[1].trim();
                        int index = Integer.parseInt(udf_views.split("---")[0].trim());
                        switch (index)
                        {
                            case 0:
                                frm_lead_enquiry.linear_udf1.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view1.setText(data);
                                break;
                            case 1:
                                frm_lead_enquiry.linear_udf2.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view2.setText(data);
                                break;
                            case 2:
                                frm_lead_enquiry.linear_udf3.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view3.setText(data);
                                break;
                            case 3:
                                frm_lead_enquiry.linear_udf4.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view4.setText(data);
                                break;
                            case 4:
                                frm_lead_enquiry.linear_udf5.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view5.setText(data);
                                break;
                            case 5:
                                frm_lead_enquiry.linear_udf6.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view6.setText(data);
                                break;
                            case 6:
                                frm_lead_enquiry.linear_udf7.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view7.setText(data);
                                break;
                            case 7:
                                frm_lead_enquiry.linear_udf8.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view8.setText(data);
                                break;
                            case 8:
                                frm_lead_enquiry.linear_udf9.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view9.setText(data);
                                break;
                            case 9:
                                frm_lead_enquiry.linear_udf10.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view10.setText(data);
                                break;
                            case 10:
                                frm_lead_enquiry.linear_udf11.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view11.setText(data);
                                break;
                            case 11:
                                frm_lead_enquiry.linear_udf12.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view12.setText(data);
                                break;
                            case 12:
                                frm_lead_enquiry.linear_udf13.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view13.setText(data);
                                break;
                            case 13:
                                frm_lead_enquiry.linear_udf14.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view14.setText(data);
                                break;
                            case 14:
                                frm_lead_enquiry.linear_udf15.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view15.setText(data);
                                break;
                            case 15:
                                frm_lead_enquiry.linear_udf16.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view16.setText(data);
                                break;
                            case 16:
                                frm_lead_enquiry.linear_udf17.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view17.setText(data);
                                break;
                            case 17:
                                frm_lead_enquiry.linear_udf18.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view18.setText(data);
                                break;
                            case 18:
                                frm_lead_enquiry.linear_udf19.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view19.setText(data);
                                break;
                            case 19:
                                frm_lead_enquiry.linear_udf20.setVisibility(View.VISIBLE);
                                frm_lead_enquiry.view20.setText(data);
                                break;
                            default:
                                break;
                        }
                    }
                    i++;
                }
                else
                {
                    fab.setImageResource(R.drawable.right_arrow);
                    linear_tab1.setVisibility(View.VISIBLE);
                    linear_udf_data.setVisibility(View.GONE);
                    if(fgen.mcd.equals("NEOP")) {
                        try {
                            double req_qty = Double.valueOf(textView7.getText().toString());
                            double rate = Double.valueOf(textView14.getText().toString());
                            txtapprox_value.setText("" + (req_qty * rate));
                            textView16.setText("" + (req_qty * rate));
                            txt_remarks.setText(textView5.getText().toString().trim());
                            txtour_remarks.setText(textView13.getText().toString().trim());
                        } catch (Exception e) {
                        }
                    }
                    i--;
                }
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_lead_enquiry.this, "aSlead_ins, afinudf_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_lead_enquiry.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fgen.mcd.equals("NEOP")) {
                    try {
                        double req_qty = Double.valueOf(textView7.getText().toString());
                        double rate = Double.valueOf(textView14.getText().toString());
                        txtapprox_value.setText("" + (req_qty * rate));
                        textView16.setText("" + (req_qty * rate));
                        txt_remarks.setText(textView5.getText().toString().trim());
                        txtour_remarks.setText(textView13.getText().toString().trim());
                    } catch (Exception e) {
                    }
                }
                boolean xcheck_conditions = check_conditions();
                if(!xcheck_conditions)
                {
                    return;
                }

                final MyProgressdialog progressDialog = new MyProgressdialog(frm_lead_enquiry.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String xmake_costing = "Y";
                                if(radio_no.isChecked())
                                {
                                    xmake_costing = "N";
                                }
                                switch (fgen.mcd)
                                {
                                    case "NEOP":
                                        xmake_costing = "I";
                                        if(radio_retailer.isChecked())
                                        {
                                            xmake_costing = "R";
                                        }
                                        break;
                                    default:
                                         break;
                                }
                                xmv_lead_source = txtlead_source.getText().toString();
                                xmv_industry = txtindustry.getText().toString();
                                xmv_lead_probability = txtlead_probability.getText().toString();
                                xmv_fg_sub_group = txtfg_sub_group.getText().toString();
                                xmv_specific_interest = txtspecific_interest.getText().toString();
                                xmv_party_name = txtparty_name.getText().toString();
                                xmv_contact_level = txtcontact_level.getText().toString();
                                xmv_contact_name = txtcontact_name.getText().toString();
                                xmv_contact_no = txtcontact_no.getText().toString();
                                xmv_email = txtemail.getText().toString();
                                xmv_fg_product = txtfg_product.getText().toString();
                                xmv_qty = txtqty.getText().toString();
                                xmv_approx_value = txtapprox_value.getText().toString();
                                xmv_appointment_dt = txtappointment_dt.getText().toString();
                                xmv_sales_man = txtsales_man.getText().toString();
                                xmv_estimator = txtestimator.getText().toString();
                                xmv_country_name = txtcountry_name.getText().toString();
                                xmv_state_name = txtstate_name.getText().toString();
                                xmv_city_name = txtcity_name.getText().toString();
                                xmv_client_remarks = txt_remarks.getText().toString();
                                xmv_our_remarks = txtour_remarks.getText().toString();


                                //============UDF Data ===================================
                                    xtext1 = textView1.getText().toString();
                                    xtext2 = textView2.getText().toString();
                                    xtext3 = textView3.getText().toString();
                                    xtext4 = textView4.getText().toString();
                                    xtext5 = textView5.getText().toString();
                                    xtext6 = textView6.getText().toString();
                                    xtext7 = textView7.getText().toString();
                                    xtext8 = textView8.getText().toString();
                                    xtext9 = textView9.getText().toString();
                                    xtext10 = textView10.getText().toString();
                                    xtext11 = textView11.getText().toString();
                                    xtext12 = textView12.getText().toString();
                                    xtext13 = textView13.getText().toString();
                                    xtext14 = textView14.getText().toString();
                                    xtext15 = textView15.getText().toString();
                                    xtext16 = textView16.getText().toString();
                                    xtext17 = textView17.getText().toString();
                                    xtext18 = textView18.getText().toString();
                                    xtext19 = textView19.getText().toString();
                                    xtext20 = textView20.getText().toString();
                                //========================================================
                                try {
                                    xmv_acode = xmv_party_name.substring(txtparty_name.getText().length() -7);
                                }catch(Exception e) {xmv_acode = "-"; }

                                try {
                                    xmv_icode = xmv_fg_product.substring(txtfg_product.getText().length() -8);
                                }catch(Exception e) { xmv_icode = "-"; }

                                String post_param = xbranch + xseperator + xtype + xseperator + xmv_lead_source
                                        + xseperator + xmv_lead_probability + xseperator + xmv_fg_sub_group + xseperator + xmv_specific_interest
                                        + xseperator + xmv_party_name + xseperator + xmv_contact_level + xseperator + xmv_contact_name + xseperator
                                        + xmv_contact_no + xseperator + xmv_email + xseperator + xmv_fg_product + xseperator + xmv_qty + xseperator
                                        + xmv_approx_value + xseperator + xmv_appointment_dt + xseperator + xmv_industry + xseperator + xmv_sales_man
                                        + xseperator + xmv_estimator + xseperator + xmv_country_name + xseperator + xmv_state_name + xseperator
                                        + xmv_city_name + xseperator + xmv_client_remarks + xseperator  + xmv_our_remarks
                                        + xseperator + xmv_acode + xseperator + xmv_icode + xseperator + xmake_costing + "!~!~!";

                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aSlead_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                boolean msg = finapi.showAlertMessageForMultipleSaving(frm_lead_enquiry.this, result);
                                if(!msg)
                                {
                                    progressDialog.dismiss();
                                    return;
                                }
                                String my_doc_no="";
                                my_doc_no= fgen.xpopup_col2;
                                post_param = my_doc_no + xseperator + view1.getText().toString() + xseperator + xtext1 +  "!~!~!"
                                        + my_doc_no + xseperator + view2.getText().toString() + xseperator + xtext2 +  "!~!~!"
                                        + my_doc_no + xseperator + view3.getText().toString() + xseperator + xtext3 +  "!~!~!"
                                        + my_doc_no + xseperator + view4.getText().toString() + xseperator + xtext4 +  "!~!~!"
                                        + my_doc_no + xseperator + view5.getText().toString() + xseperator + xtext5 +  "!~!~!"
                                        + my_doc_no + xseperator + view6.getText().toString() + xseperator + xtext6 +  "!~!~!"
                                        + my_doc_no + xseperator + view7.getText().toString() + xseperator + xtext7 +  "!~!~!"
                                        + my_doc_no + xseperator + view8.getText().toString() + xseperator + xtext8 +  "!~!~!"
                                        + my_doc_no + xseperator + view9.getText().toString() + xseperator + xtext9 +  "!~!~!"
                                        + my_doc_no + xseperator + view10.getText().toString() + xseperator + xtext10 +  "!~!~!"
                                        + my_doc_no + xseperator + view11.getText().toString() + xseperator + xtext11 +  "!~!~!"
                                        + my_doc_no + xseperator + view12.getText().toString() + xseperator + xtext12 +  "!~!~!"
                                        + my_doc_no + xseperator + view13.getText().toString() + xseperator + xtext13 +  "!~!~!"
                                        + my_doc_no + xseperator + view14.getText().toString() + xseperator + xtext14 +  "!~!~!"
                                        + my_doc_no + xseperator + view15.getText().toString() + xseperator + xtext15 +  "!~!~!"
                                        + my_doc_no + xseperator + view16.getText().toString() + xseperator + xtext16 +  "!~!~!"
                                        + my_doc_no + xseperator + view17.getText().toString() + xseperator + xtext17 +  "!~!~!"
                                        + my_doc_no + xseperator + view18.getText().toString() + xseperator + xtext18 +  "!~!~!"
                                        + my_doc_no + xseperator + view19.getText().toString() + xseperator + xtext19 +  "!~!~!"
                                        + my_doc_no + xseperator + view20.getText().toString() + xseperator + xtext20 +  "!~!~!";

                                result = finapi.getApi(fgen.mcd, "afinudf_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                msg = finapi.showAlertMessage(frm_lead_enquiry.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                fgen.xpopup_col2 = "";
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
        xparty_popup_sel = "N";
        xfrm_clicked_tab = "";
    }

    private boolean check_conditions() {
        switch (fgen.mcd)
        {
            case "NEOP" :
                if(txtlead_source.getText().toString().contains("BY WALK-IN"))
                {
                    if(txtparty_name.getText().toString().isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Please Select Part Name!!", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    return true;
                }
            default:
                break;
        }

        if(txtlead_source.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select Lead Source!!", Toast.LENGTH_LONG).show();
            txtlead_source.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtindustry.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select Industry/Vertical!!", Toast.LENGTH_LONG).show();
            txtindustry.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtlead_probability.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select Lead Probability!!", Toast.LENGTH_LONG).show();
            txtlead_probability.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtfg_sub_group.getText().toString().isEmpty() && !fgen.mcd.equals("NEOP"))
        {
            Toast.makeText(getApplicationContext(), "Please Select FG Sub Group!!", Toast.LENGTH_LONG).show();
            txtfg_sub_group.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtspecific_interest.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Fill Specific Interest Field!!", Toast.LENGTH_LONG).show();
            txtspecific_interest.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtparty_name.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select Party Name!!", Toast.LENGTH_LONG).show();
            txtparty_name.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtcontact_level.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select Contact Level!!", Toast.LENGTH_LONG).show();
            txtcontact_level.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtcontact_name.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Fill Contact Name Field!!", Toast.LENGTH_LONG).show();
            txtcontact_name.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtcontact_no.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Fill Contact Number Field!!", Toast.LENGTH_LONG).show();
            txtcontact_no.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtemail.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Fill Email Field!!", Toast.LENGTH_LONG).show();
            txtemail.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtfg_product.getText().toString().isEmpty() && !fgen.mcd.equals("NEOP"))
        {
            Toast.makeText(getApplicationContext(), "Please Select FG Product!!", Toast.LENGTH_LONG).show();
            txtfg_product.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtqty.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Fill Qty Field!!", Toast.LENGTH_LONG).show();
            txtqty.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtapprox_value.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Fill Approx Value Field!!", Toast.LENGTH_LONG).show();
            txtapprox_value.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtappointment_dt.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Fill Appointment Dt. Field!!", Toast.LENGTH_LONG).show();
            txtappointment_dt.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtsales_man.getText().toString().isEmpty() && !fgen.mcd.equals("NEOP"))
        {
            Toast.makeText(getApplicationContext(), "Please Fill Sales Manager Field!!", Toast.LENGTH_LONG).show();
            txtsales_man.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(fgen.mcd.equals("SGRP")) {
            if (txtestimator.getText().toString().isEmpty() && !fgen.mcd.equals("NEOP")) {
                Toast.makeText(getApplicationContext(), "Please Fill Estimator Field!!", Toast.LENGTH_LONG).show();
                txtestimator.setBackgroundResource(R.color.light_blue);
                return false;
            }
        }
        if(txtcountry_name.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select Country Name!!", Toast.LENGTH_LONG).show();
            txtcountry_name.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtstate_name.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select State!!", Toast.LENGTH_LONG).show();
            txtstate_name.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtcity_name.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Fill City Name Field!!", Toast.LENGTH_LONG).show();
            txtcity_name.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtour_remarks.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Fill Client Remarks Field!!", Toast.LENGTH_LONG).show();
            txtour_remarks.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txt_remarks.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Fill Remarks!!", Toast.LENGTH_LONG).show();
            txt_remarks.setBackgroundResource(R.color.light_blue);
            return false;
        }
        return true;
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

    private void initializeViews() {
        fab = findViewById(R.id.fab);
        linear_tab1 = findViewById(R.id.lin);
        linear_udf_data = findViewById(R.id.linear_udf_data);
        linearLayout_cust_type = findViewById(R.id.linear_condition_of_cust_type);
        linearLayout_make_costing = findViewById(R.id.linear_condition_of_make_costing);
        linearLayout_fg_sub_group = findViewById(R.id.linear_fg_sub_group);
        linearLayout_fg_product = findViewById(R.id.linear_fg_product);
        linearLayout_salesmanager = findViewById(R.id.linear_sales_manager);
        linearLayout_estimator = findViewById(R.id.linear_estimator);
        toolbar = findViewById(R.id.toolbar);
        txtindustry = findViewById(R.id.txtindustry);
        txtspecific_interest = findViewById(R.id.txtspecific_interest);
        txtparty_name = findViewById(R.id.txtparty_name);
        txtcontact_name = findViewById(R.id.txtcontact_name);
        txtcontact_no = findViewById(R.id.txtcontact_no);
        txtemail = findViewById(R.id.txtemail);
        txtqty = findViewById(R.id.txtqty);
        txtapprox_value = findViewById(R.id.txtapprox_value);
        txtsales_man = findViewById(R.id.txtsales_man);
        txtestimator = findViewById(R.id.txtestimator);
        txt_remarks = findViewById(R.id.txt_remarks);
        txtour_remarks = findViewById(R.id.txtour_remarks);
        txtlead_source = findViewById(R.id.txtlead_source);
        txtlead_probability = findViewById(R.id.txtlead_probability);
        txtfg_sub_group = findViewById(R.id.txtfg_sub_group);
        txtcontact_level = findViewById(R.id.txtcontact_level);
        txtfg_product = findViewById(R.id.txtfg_product);
        txtcountry_name = findViewById(R.id.txtcountry_name);
        txtstate_name = findViewById(R.id.txtstate_name);
        txtcity_name = findViewById(R.id.txtcity_name);
        btn_save = findViewById(R.id.btn_save);
        btn_new = findViewById(R.id.btn_new);
        btn_list = findViewById(R.id.btn_list);
        btn_cancel = findViewById(R.id.btn_cancel);
        txtappointment_dt = findViewById(R.id.txtappointment_dt);
        radio_industry = findViewById(R.id.radio_industry);
        radio_retailer = findViewById(R.id.radio_retailer);


        txtlead_source.setBackgroundResource(R.color.light_grey);
        txtindustry.setBackgroundResource(R.color.light_grey);
        txtlead_probability.setBackgroundResource(R.color.light_grey);
        txt_remarks.setBackgroundResource(R.color.light_grey);
        txtour_remarks.setBackgroundResource(R.color.light_grey);
        txtcity_name.setBackgroundResource(R.color.light_grey);
        txtstate_name.setBackgroundResource(R.color.light_grey);
        txtcountry_name.setBackgroundResource(R.color.light_grey);
        txtestimator.setBackgroundResource(R.color.light_grey);
        txtsales_man.setBackgroundResource(R.color.light_grey);
        txtappointment_dt.setBackgroundResource(R.color.light_grey);
        txtapprox_value.setBackgroundResource(R.color.light_grey);
        txtqty.setBackgroundResource(R.color.light_grey);
        txtfg_product.setBackgroundResource(R.color.light_grey);
        txtemail.setBackgroundResource(R.color.light_grey);
        txtcontact_no.setBackgroundResource(R.color.light_grey);
        txtcontact_name.setBackgroundResource(R.color.light_grey);
        txtcontact_level.setBackgroundResource(R.color.light_grey);
        txtparty_name.setBackgroundResource(R.color.light_grey);
        txtspecific_interest.setBackgroundResource(R.color.light_grey);
        txtfg_sub_group.setBackgroundResource(R.color.light_grey);

        radio_yes = findViewById(R.id.radio_yes);
        radio_no = findViewById(R.id.radio_no);





        //==================== UDF Data=======================
        linear_udf1 = findViewById(R.id.linear_udf1);
        linear_udf2 = findViewById(R.id.linear_udf2);
        linear_udf3 = findViewById(R.id.linear_udf3);
        linear_udf4 = findViewById(R.id.linear_udf4);
        linear_udf5 = findViewById(R.id.linear_udf5);
        linear_udf6 = findViewById(R.id.linear_udf6);
        linear_udf7 = findViewById(R.id.linear_udf7);
        linear_udf8 = findViewById(R.id.linear_udf8);
        linear_udf9 = findViewById(R.id.linear_udf9);
        linear_udf10 = findViewById(R.id.linear_udf10);
        linear_udf11 = findViewById(R.id.linear_udf11);
        linear_udf12 = findViewById(R.id.linear_udf12);
        linear_udf13 = findViewById(R.id.linear_udf13);
        linear_udf14 = findViewById(R.id.linear_udf14);
        linear_udf15 = findViewById(R.id.linear_udf15);
        linear_udf16 = findViewById(R.id.linear_udf16);
        linear_udf17 = findViewById(R.id.linear_udf17);
        linear_udf18 = findViewById(R.id.linear_udf18);
        linear_udf19 = findViewById(R.id.linear_udf19);
        linear_udf20 = findViewById(R.id.linear_udf20);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);
        view6 = findViewById(R.id.view6);
        view7 = findViewById(R.id.view7);
        view8 = findViewById(R.id.view8);
        view9 = findViewById(R.id.view9);
        view10 = findViewById(R.id.view10);
        view11 = findViewById(R.id.view11);
        view12 = findViewById(R.id.view12);
        view13 = findViewById(R.id.view13);
        view14 = findViewById(R.id.view14);
        view15 = findViewById(R.id.view15);
        view16 = findViewById(R.id.view16);
        view17 = findViewById(R.id.view17);
        view18 = findViewById(R.id.view18);
        view19 = findViewById(R.id.view19);
        view20 = findViewById(R.id.view20);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
        textView9 = findViewById(R.id.textView9);
        textView10 = findViewById(R.id.textView10);
        textView11 = findViewById(R.id.textView11);
        textView12 = findViewById(R.id.textView12);
        textView13 = findViewById(R.id.textView13);
        textView14 = findViewById(R.id.textView14);
        textView15 = findViewById(R.id.textView15);
        textView16 = findViewById(R.id.textView16);
        textView17 = findViewById(R.id.textView17);
        textView18 = findViewById(R.id.textView18);
        textView19 = findViewById(R.id.textView19);
        textView20 = findViewById(R.id.textView20);
    }

    private void newBtnClickedMethod() {
        txtindustry.setText("");
        txtspecific_interest.setText("");
        txtparty_name.setText("");
        txtcontact_name.setText("");
        txtcontact_no.setText("");
        txtemail.setText("");
        txtqty.setText("");
        txtapprox_value.setText("");
        txt_remarks.setText("");
        txtour_remarks.setText("");
        txtlead_source.setText("");
        txtlead_probability.setText("");
        txtfg_sub_group.setText("");
        txtcountry_name.setText("");
        txtcontact_level.setText("");
        txtfg_product.setText("");
        txtstate_name.setText("");
        txtcity_name.setText("");
        txtappointment_dt.setText("");
        txtsales_man.setText("");
        txtestimator.setText("");

        textView1.setText("");
        textView2.setText("");
        textView3.setText("");
        textView4.setText("");
        textView5.setText("");
        textView6.setText("");
        textView7.setText("");
        textView8.setText("");
        textView9.setText("");
        textView10.setText("");
        textView11.setText("");
        textView12.setText("");
        textView13.setText("");
        textView14.setText("");
        textView15.setText("");
        textView16.setText("");
        textView17.setText("");
        textView18.setText("");
        textView19.setText("");
        textView20.setText("");
    }

    private void enabledViewMethod() {
        txtindustry.setEnabled(true);
        txtspecific_interest.setEnabled(true);
        txtparty_name.setEnabled(true);
        txtcontact_name.setEnabled(true);
        txtcontact_no.setEnabled(true);
        txtemail.setEnabled(true);
        txtqty.setEnabled(true);
        txtapprox_value.setEnabled(true);
        txt_remarks.setEnabled(true);
        txtour_remarks.setEnabled(true);
        txtlead_source.setEnabled(true);
        txtlead_probability.setEnabled(true);
        txtfg_sub_group.setEnabled(true);
        txtcountry_name.setEnabled(true);
        txtcontact_level.setEnabled(true);
        txtfg_product.setEnabled(true);
        txtstate_name.setEnabled(true);
        txtcity_name.setEnabled(true);
        txtappointment_dt.setEnabled(true);
        txtsales_man.setEnabled(true);
        txtestimator.setEnabled(true);
    }

    private void disableViewsMethod() {
        txtindustry.setEnabled(false);
        txtspecific_interest.setEnabled(false);
        txtparty_name.setEnabled(false);
        txtcontact_name.setEnabled(false);
        txtcontact_no.setEnabled(false);
        txtemail.setEnabled(false);
        txtqty.setEnabled(false);
        txtapprox_value.setEnabled(false);
        txt_remarks.setEnabled(false);
        txtour_remarks.setEnabled(false);
        txtlead_source.setEnabled(false);
        txtlead_probability.setEnabled(false);
        txtfg_sub_group.setEnabled(false);
        txtcountry_name.setEnabled(false);
        txtcontact_level.setEnabled(false);
        txtfg_product.setEnabled(false);
        txtstate_name.setEnabled(false);
        txtcity_name.setEnabled(false);
        txtappointment_dt.setEnabled(false);
        txtsales_man.setEnabled(false);
        txtestimator.setEnabled(false);
    }

    private void setTextOnViews() {
        btn_cancel.setText("EXIT");
    }

    private void Highlightborder() {
        txtindustry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtspecific_interest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtparty_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtcontact_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtcontact_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtqty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtapprox_value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtsales_man.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtestimator.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtour_remarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtlead_source.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtlead_probability.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtfg_sub_group.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtcountry_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtcontact_level.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtfg_product.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtstate_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtcity_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });


        //================= UDF Data ========================

        textView1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        textView2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView6.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView7.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView8.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView9.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView10.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView11.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView12.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView13.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView14.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView15.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView16.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView17.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView18.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView18.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView19.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView20.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    @Override
    public void onDateSet(DatePicker view, int i, int i1, int i2) {
        SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");
        Date xinput_dt = null;
        Date xcurr_dt = new Date();
        String strDate= sdf.format(xcurr_dt);
        Date curr_dt = null;
        try {
            curr_dt = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

        txtappointment_dt.setText(date);
        try {
            xinput_dt = sdf.parse(txtappointment_dt.getText().toString());
            if(xinput_dt.compareTo(curr_dt) >= 0 && xClickView.equals("txtappointment_dt")) {
                txtappointment_dt.setText(date);
            }
            else
            {
                txtappointment_dt.setText("");
            }
        } catch (ParseException e) {
            e.printStackTrace();
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
        if(controll.equals("txtcountry_name")) txtapi_code.setText("EP824");
        if(controll.equals("txtfg_product")) txtapi_code.setText("EP817");
        if(controll.equals("txtstate_name")) txtapi_code.setText("EP825");
        if(controll.equals("txtfg_sub_group")) txtapi_code.setText("EP823");
        if(controll.equals("txtparty_name")) txtapi_code.setText("EP815");
        if(controll.equals("txtlead_source")) txtapi_code.setText("EP826");
        if(controll.equals("txtindustry")) txtapi_code.setText("EP827");
        if(controll.equals("txtlead_probability")) txtapi_code.setText("EP828");
        if(controll.equals("txtcontact_level")) txtapi_code.setText("EP829");
        if(controll.equals("txtestimator")) txtapi_code.setText("EP832B");

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
                list_selected_text = adapter.getItem(position).toString();
                switch (controll)
                {
                    case "txtcountry_name" : txtcountry_name.setText(list_selected_text);
                        break;
                    case "txtfg_product" :
                        txtfg_product.setText(list_selected_text);
                        break;
                    case "txtstate_name" : txtstate_name.setText(list_selected_text);
                        break;
                    case "txtfg_sub_group" :
                        final MyProgressdialog progressDialog = new MyProgressdialog(frm_lead_enquiry.this);
                        progressDialog.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        txtfg_sub_group.setText(list_selected_text);
                                        xfrm_lead_enquiry = "Y";
                                        xfg_sub_group_code = list_selected_text.substring(0,4);
                                        xfg_product = finapi.fill_record_in_listview_popup("EP817");
                                        xfg_sub_group_code = "";
                                        xfrm_lead_enquiry = "N";
                                        // On complete call either onLoginSuccess or onLoginFailed
                                        progressDialog.dismiss();
                                    }
                                }, 100);

                        break;
                    case "txtparty_name" :
                        final MyProgressdialog progressDialog2 = new MyProgressdialog(frm_lead_enquiry.this);
                        progressDialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        String xdata = list_selected_text.toString();
                                        txtcontact_no.setText(xdata.split("---")[3].trim().toString());
                                        String no = xdata.split("---")[3].trim().toString();
                                        txtcontact_name.setText(xdata.split("---")[2].trim().toString());
                                        txtemail.setText(xdata.split("---")[4].trim().toString());
                                        fgen.frm_request = "frm_lead_enquiry";
                                        txtparty_name.setText(xdata.split("---")[0].trim().toString() + "---" +xdata.split("---")[1].trim().toString());
                                        frm_lead_enquiry.xfrm_lead_enquiry = "Y";
                                        frm_lead_enquiry.xfg_sub_group_code = list_selected_text.split("---")[1].trim();
                                        xparty_popup_sel = "Y";
                                        finapi.fill_record_in_listview_popup_for_lead_enquiry("EP815A");
                                        frm_lead_enquiry.xfg_sub_group_code = "";
                                        // On complete call either onLoginSuccess or onLoginFailed
                                        progressDialog2.dismiss();
                                    }
                                }, 100);

                        break;
                    case "txtlead_source" : txtlead_source.setText(list_selected_text);
                        break;
                    case "txtindustry" : txtindustry.setText(list_selected_text);
                        break;
                    case "txtlead_probability" : txtlead_probability.setText(list_selected_text);
                        break;
                    case "txtcontact_level" : txtcontact_level.setText(list_selected_text);
                        break;
                    case "txtestimator" : txtestimator.setText(list_selected_text);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });

    }
}


class AsyncCaller extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread
        fgen.xpopup_col2 = "";
        frm_lead_enquiry.xcountry_list = new ArrayList<>();
        frm_lead_enquiry.xstate_list = new ArrayList<>();
        frm_lead_enquiry.xfg_product = new ArrayList<>();
        frm_lead_enquiry.xparty_name = new ArrayList<>();
        frm_lead_enquiry.xestimator = new ArrayList<>();
        frm_lead_enquiry.xudf_data_views = new ArrayList<>();

    }
    @Override
    protected Void doInBackground(Void... params) {
        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
        frm_lead_enquiry.xestimator = finapi.fill_record_in_listview_popup("EP832B");
        frm_lead_enquiry.xfrm_clicked_tab = "txtparty";
        frm_lead_enquiry.xparty_name = finapi.fill_record_in_listview_popup("EP815");
        frm_lead_enquiry.xfrm_clicked_tab = "";
        frm_lead_enquiry.xcountry_list = finapi.fill_record_in_listview_popup("EP824");
        frm_lead_enquiry.xstate_list = finapi.fill_record_in_listview_popup("EP825");
        frm_lead_enquiry.xcol6_udf_data = "F45101";
        frm_lead_enquiry.xudf_data_views = finapi.fill_record_in_listview_popup("EP860");
        frm_lead_enquiry.xcol6_udf_data = "";
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
    }
}


