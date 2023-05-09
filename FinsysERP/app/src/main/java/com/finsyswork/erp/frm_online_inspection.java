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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class frm_online_inspection extends AppCompatActivity {

    private Button btn_new,btn_save, btn_cancel;
    public static EditText txtselect_job,txtitem,txtqty, txttype_of_inspection,
            txtview1,txtview2,txtview3,txtview4,txtview5,txtview6,txtview7,txtview8, txtremarks;
    Toolbar toolbar;
    public static LinearLayout linear1,linear2,linear3,linear4,linear5,linear6,linear7,linear8;
    public static TextView view1,view2,view3,view4,view5,view6,view7,view8;
    String xseperator = "#~#", xbranch="00", xtype ="MS";
    int i=0;
    public static String  xlist_selected_text="";
    public static ArrayList<String> xjob = new ArrayList<>();
    public static ArrayList<String> xtype_of_inspection = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_online_inspection);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_online_inspection.this;
        finapi.deleteJsonResponseFile();

        frm_lead_enquiry.xfrm_lead_enquiry = "";
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_online_inspection.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        frm_jobw_prod.xjob = finapi.fill_record_in_listview_popup("EP835");
                        // On complete call either onLoginSuccess or onLoginFailed
                        progressDialog.dismiss();
                    }
                }, 100);

        initializeViews();
        Highlightborder();
        btn_cancel.setText("EXIT");
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        finapi.setColorOfStatusBar(frm_online_inspection.this);
        setSupportActionBar(toolbar);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        final String[] currentDateandTime = {sdf.format(new Date())};

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBtnClickedMethod();
                enabledViewMethod();
                btn_save.setEnabled(true);
                btn_new.setEnabled(false);
                btn_cancel.setText("CANCEL");

                txtselect_job.setBackgroundResource(R.color.light_blue);

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

                    txtselect_job.setBackgroundResource(R.color.light_grey);
                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                    extraFields();
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });

        txtselect_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_online_inspection.this, xjob, "txtselect_job");
            }
        });

        txttype_of_inspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_online_inspection.this, xtype_of_inspection, "txttype_of_inspection");
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_online_inspection.this, "aMaintsolv_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_online_inspection.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xjob_no = txtselect_job.getText().toString();
                String xitem = txtitem.getText().toString();
                String xqty = txtqty.getText().toString();
                String xview1 = view1.getText().toString();
                String xview2 = view2.getText().toString();
                String xview3 = view3.getText().toString();
                String xview4 = view4.getText().toString();
                String xview5 = view5.getText().toString();
                String xview6 = view6.getText().toString();
                String xview7 = view7.getText().toString();
                String xview8 = view8.getText().toString();
                String xedittxt1 = txtview1.getText().toString();
                String xedittxt2 = txtview2.getText().toString();
                String xedittxt3 = txtview3.getText().toString();
                String xedittxt4 = txtview4.getText().toString();
                String xedittxt5 = txtview5.getText().toString();
                String xedittxt6 = txtview6.getText().toString();
                String xedittxt7 = txtview7.getText().toString();
                String xedittxt8 = txtview8.getText().toString();

                if(xjob_no.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select Job Card!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(xitem.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select Job Card Again!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txttype_of_inspection.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Inspection Type!!", Toast.LENGTH_LONG).show();
                    return;
                }

                final MyProgressdialog progressDialog = new MyProgressdialog(frm_online_inspection.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String xmv_spare_cons = "-";
                                String xmv_spare_cost = "-";


                                ArrayList<expense_model> bullets = frm_record_loading_truck.list;
                                String post_param = "";
                                for(expense_model b : bullets) {
                                    String  expense = b.expense;
                                    String amount = b.amount;
                                    post_param  += expense + xseperator + amount+ "!~!~!";
                                }

                                ArrayList<team> result = finapi.getApi(fgen.mcd, "amaintspare_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                boolean msg = finapi.showAlertMessageForMultipleSaving(frm_online_inspection.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }

                                post_param = xbranch + xseperator + xtype + xseperator + "!~!~!";

                                result = finapi.getApi(fgen.mcd, "aMaintsolv_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                msg = finapi.showAlertMessage(frm_online_inspection.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                extraFields();
                                newBtnClickedMethod();
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    private void extraFields(){
        linear1.setVisibility(View.GONE);
        linear2.setVisibility(View.GONE);
        linear3.setVisibility(View.GONE);
        linear4.setVisibility(View.GONE);
        linear5.setVisibility(View.GONE);
        linear6.setVisibility(View.GONE);
        linear7.setVisibility(View.GONE);
        linear8.setVisibility(View.GONE);
        view1.setText("View1");
        view2.setText("View2");
        view3.setText("View3");
        view4.setText("View4");
        view5.setText("View5");
        view6.setText("View6");
        view7.setText("View7");
        view8.setText("View8");
        txtview1.setText("");
        txtview2.setText("");
        txtview3.setText("");
        txtview4.setText("");
        txtview5.setText("");
        txtview6.setText("");
        txtview7.setText("");
        txtview8.setText("");
    }

    private void enabledViewMethod() {
        txtselect_job.setEnabled(true);
        txttype_of_inspection.setEnabled(true);
        txtview1.setEnabled(true);
        txtview2.setEnabled(true);
        txtview3.setEnabled(true);
        txtview4.setEnabled(true);
        txtview5.setEnabled(true);
        txtview6.setEnabled(true);
        txtview7.setEnabled(true);
        txtview8.setEnabled(true);
    }

    private void disableViewsMethod() {
        txtselect_job.setEnabled(false);
        txttype_of_inspection.setEnabled(false);
        txtview1.setEnabled(false);
        txtview2.setEnabled(false);
        txtview3.setEnabled(false);
        txtview4.setEnabled(false);
        txtview5.setEnabled(false);
        txtview6.setEnabled(false);
        txtview7.setEnabled(false);
        txtview8.setEnabled(false);
    }

    private void newBtnClickedMethod() {
        txtselect_job.setText("");
        txtitem.setText("");
        txtqty.setText("");
        txttype_of_inspection.setText("");
        txtview1.setText("");
        txtview2.setText("");
        txtview3.setText("");
        txtview4.setText("");
        txtview5.setText("");
        txtview6.setText("");
        txtview7.setText("");
        txtview8.setText("");
        view1.setText("View1");
        view2.setText("View2");
        view3.setText("View3");
        view4.setText("View4");
        view5.setText("View5");
        view6.setText("View6");
        view7.setText("View7");
        view8.setText("View8");
    }

    private void initializeViews() {
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        toolbar = findViewById(R.id.toolbar);
        txtqty = findViewById(R.id.txtqty);
        txtitem = findViewById(R.id.txtitem);
        txtremarks = findViewById(R.id.txtremarks);
        txtselect_job = findViewById(R.id.txtscan_job);
        txttype_of_inspection = findViewById(R.id.txttype_of_inspection);
        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        linear3 = findViewById(R.id.linear3);
        linear4 = findViewById(R.id.linear4);
        linear5 = findViewById(R.id.linear5);
        linear6 = findViewById(R.id.linear6);
        linear7 = findViewById(R.id.linear7);
        linear8 = findViewById(R.id.linear8);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);
        view6 = findViewById(R.id.view6);
        view7 = findViewById(R.id.view7);
        view8 = findViewById(R.id.view8);
        txtview1 = findViewById(R.id.txttxtview1);
        txtview2 = findViewById(R.id.txttxtview2);
        txtview3 = findViewById(R.id.txttxtview3);
        txtview4 = findViewById(R.id.txttxtview4);
        txtview5 = findViewById(R.id.txttxtview5);
        txtview6 = findViewById(R.id.txttxtview6);
        txtview7 = findViewById(R.id.txttxtview7);
        txtview8 = findViewById(R.id.txttxtview8);

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
        if(controll.equals("txtselect_job")) txtapi_code.setText("EP835");
        if(controll.equals("txttype_of_inspection")) txtapi_code.setText("EP825");

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
                    case "txttype_of_inspection":
                        final MyProgressdialog progressDialog = new MyProgressdialog(frm_online_inspection.this);
                        progressDialog.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        txttype_of_inspection.setText(list_selected_text.trim());
                                        frm_lead_enquiry.xcol6_udf_data = list_selected_text.split("---")[0].trim();
                                        frm_lead_enquiry.xudf_data_views = finapi.fill_record_in_listview_popup("EP860");
                                        frm_lead_enquiry.xcol6_udf_data = "";

                                        ArrayList<String> udf_text_views = frm_lead_enquiry.xudf_data_views;

                                        for (String udf_views : udf_text_views)
                                        {
                                            String data = udf_views.split("---")[1].trim();
                                            int index = Integer.parseInt(udf_views.split("---")[0].trim());
                                            switch (index)
                                            {
                                                case 0:
                                                    frm_online_inspection.linear1.setVisibility(View.VISIBLE);
                                                    frm_online_inspection.view1.setText(data);
                                                    break;
                                                case 1:
                                                    frm_online_inspection.linear2.setVisibility(View.VISIBLE);
                                                    frm_online_inspection.view2.setText(data);
                                                    break;
                                                case 2:
                                                    frm_online_inspection.linear3.setVisibility(View.VISIBLE);
                                                    frm_online_inspection.view3.setText(data);
                                                    break;
                                                case 3:
                                                    frm_online_inspection.linear4.setVisibility(View.VISIBLE);
                                                    frm_online_inspection.view4.setText(data);
                                                    break;
                                                case 4:
                                                    frm_online_inspection.linear5.setVisibility(View.VISIBLE);
                                                    frm_online_inspection.view5.setText(data);
                                                    break;
                                                case 5:
                                                    frm_online_inspection.linear6.setVisibility(View.VISIBLE);
                                                    frm_online_inspection.view6.setText(data);
                                                    break;
                                                case 6:
                                                    frm_online_inspection.linear7.setVisibility(View.VISIBLE);
                                                    frm_online_inspection.view7.setText(data);
                                                    break;
                                                case 7:
                                                    frm_online_inspection.linear8.setVisibility(View.VISIBLE);
                                                    frm_online_inspection.view8.setText(data);
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }

                                        // On complete call either onLoginSuccess or onLoginFailed
                                        progressDialog.dismiss();
                                    }
                                }, 100);
                        break;

                    case "txtselect_job":
                        xlist_selected_text = list_selected_text.trim();
                        txtselect_job.setText(xlist_selected_text.substring(0,20).trim());
                        frm_lead_enquiry.xfg_sub_group_code = xlist_selected_text.substring(0, 20).trim();
                        xlist_selected_text = xlist_selected_text.trim().toString();
                        frm_jobw_prod.form_name = "frm_jobw_prod";
                        ArrayList<team> xjob = finapi.getApiForPOPUP(fgen.mcd, "", "EP835S", fgen.cdt1, fgen.cdt2, fgen.muname, frm_lead_enquiry.xfg_sub_group_code);
                        frm_online_inspection.txtitem.setText(frm_jobw_prod.xicode1.trim()+" --- "+frm_jobw_prod.xiname1.trim());
                        frm_online_inspection.txtqty.setText(frm_jobw_prod.xqty1.trim());
                        frm_jobw_prod.form_name = "";
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
        frm_jobw_prod.form_name = "";
    }

    private void Highlightborder() {

        txtview1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txtview2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txtview3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txtview4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txtview5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txtview6.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txtview7.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txtview8.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

}


