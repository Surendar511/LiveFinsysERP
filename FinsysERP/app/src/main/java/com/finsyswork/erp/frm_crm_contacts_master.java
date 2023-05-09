package com.finsyswork.erp;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.ArrayList;

public class frm_crm_contacts_master extends AppCompatActivity {

    Button btn_new,btn_save, btn_cancel;
    Toolbar toolbar;
    EditText txtcontact_company, txtcontact_person, txtcontact_designation, txtdeals_in, txtcontact_of
            , txtreference_name, txtadd1, txtadd2, txtadd3, txtadd4, txtstate, txtcontact_no,
            txtfax_no, txtmobile_no, txtemail_id, txtcountry_name;
    String xseperator = "#~#", xmbranch="00", xmv_type ="46";

    public static ArrayList<String> xcontact_designation = new ArrayList<>();
    public static ArrayList<String> xreference_name = new ArrayList<>();
    public static ArrayList<String> xstate = new ArrayList<>();
    public static ArrayList<String> xcountry = new ArrayList<>();
    String xcountry_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_crm_contacts_master);

        finapi.context = frm_crm_contacts_master.this;
        finapi.deleteJsonResponseFile();

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_crm_contacts_master.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        new AsyncCaller_crm_contacts_master().execute();
                        progressDialog.dismiss();
                    }
                }, 100);

         initializeViews();
         btn_cancel.setText("EXIT");

         btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
         disableViewsMethod();
         setSupportActionBar(toolbar);
         finapi.setColorOfStatusBar(frm_crm_contacts_master.this);
         Highlightborder();

         txtcontact_designation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_crm_contacts_master.this, xcontact_designation, "txtcontact_designation");
            }
        });
         txtreference_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_crm_contacts_master.this, xreference_name, "txtreference_name");
            }
        });
         txtstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_crm_contacts_master.this, xstate, "txtstate");
            }
        });
         txtcountry_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_crm_contacts_master.this, xcountry, "txtcountry_name");
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

                txtcontact_company.setBackgroundResource(R.color.light_blue);
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

                    txtcontact_company.setBackgroundResource(R.color.light_grey);
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
                    fgen.showApiName(frm_crm_contacts_master.this, "aCrmcont_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_crm_contacts_master.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtcontact_company.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Fill Contact Company Name!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtcontact_person.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Fill Contact Person Name!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtmobile_no.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Fill Mobile No.!!", Toast.LENGTH_LONG).show();
                    return;
                }
                String xmv_contact_company = txtcontact_company.getText().toString();
                String xmv_contact_person = txtcontact_person.getText().toString();
                String xmv_contact_designation = txtcontact_designation.getText().toString();
                String xmv_deals_in = txtdeals_in.getText().toString();
                String xmv_contact_of = txtcontact_of.getText().toString();
                String xmv_reference_name = txtreference_name.getText().toString();
                String xmv_add1 = txtadd1.getText().toString();
                String xmv_add2 = txtadd2.getText().toString();
                String xmv_add3 = txtadd3.getText().toString();
                String xmv_add4 = txtadd4.getText().toString();
                String xmv_state = txtstate.getText().toString();
                String xmv_contact_no = txtcontact_no.getText().toString();
                String xmv_fax_no = txtfax_no.getText().toString();
                String xmv_mobile_no = txtmobile_no.getText().toString();
                String xmv_email_id = txtemail_id.getText().toString();
                String xmv_country_name = txtcountry_name.getText().toString();
                if(xmv_contact_company.equals("")){xmv_contact_company = "-";}
                if(xmv_contact_person.equals("")){xmv_contact_person = "-";}
                if(xmv_contact_designation.equals("")){xmv_contact_designation = "-";}
                if(xmv_deals_in.equals("")){xmv_deals_in = "-";}
                if(xmv_contact_of.equals("")){xmv_contact_of = "-";}
                if(xmv_reference_name.equals("")){xmv_reference_name = "-";}
                if(xmv_add1.equals("")){xmv_add1 = "-";}
                if(xmv_add2.equals("")){xmv_add2 = "-";}
                if(xmv_add3.equals("")){xmv_add3 = "-";}
                if(xmv_add4.equals("")){xmv_add4 = "-";}
                if(xmv_state.equals("")){xmv_state = "-";}
                if(xmv_contact_no.equals("")){xmv_contact_no = "-";}
                if(xmv_fax_no.equals("")){xmv_fax_no = "-";}
                if(xmv_mobile_no.equals("")){xmv_mobile_no = "-";}
                if(xmv_email_id.equals("")){xmv_email_id = "-";}
                if(xmv_country_name.equals("")){xmv_country_name = "-";}
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_crm_contacts_master.this);
                progressDialog.show();
                String finalXmv_contact_company = xmv_contact_company;
                String finalXmv_contact_person = xmv_contact_person;
                String finalXmv_contact_designation = xmv_contact_designation;
                String finalXmv_deals_in = xmv_deals_in;
                String finalXmv_contact_of = xmv_contact_of;
                String finalXmv_reference_name = xmv_reference_name;
                String finalXmv_add1 = xmv_add1;
                String finalXmv_add2 = xmv_add2;
                String finalXmv_add3 = xmv_add3;
                String finalXmv_add4 = xmv_add4;
                String finalXmv_state = xmv_state;
                String finalXmv_contact_no = xmv_contact_no;
                String finalXmv_fax_no = xmv_fax_no;
                String finalXmv_mobile_no = xmv_mobile_no;
                String finalXmv_email_id = xmv_email_id;
                String finalXmv_country_name = xmv_country_name;
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = xmbranch + xseperator + xmv_type + xseperator + finalXmv_contact_company
                                        + xseperator + finalXmv_contact_person + xseperator + finalXmv_contact_designation
                                        + xseperator + finalXmv_deals_in + xseperator + finalXmv_contact_of + xseperator + finalXmv_reference_name
                                        + xseperator + finalXmv_add1 + xseperator + finalXmv_add2 + xseperator
                                        + finalXmv_add3 + xseperator + finalXmv_add4  + xseperator + finalXmv_state
                                        + xseperator + finalXmv_contact_no + xseperator + finalXmv_fax_no
                                        + xseperator + finalXmv_mobile_no + xseperator + finalXmv_email_id
                                        + xseperator + finalXmv_country_name;
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aCrmcont_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP902");
                                boolean msg = finapi.showAlertMessage(frm_crm_contacts_master.this, result);
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
        txtcontact_company.setEnabled(true);
        txtcontact_person.setEnabled(true);
        txtcontact_designation.setEnabled(true);
        txtdeals_in.setEnabled(true);
        txtcontact_of.setEnabled(true);
        txtreference_name.setEnabled(true);
        txtadd1.setEnabled(true);
        txtadd2.setEnabled(true);
        txtadd3.setEnabled(true);
        txtadd4.setEnabled(true);
        txtstate.setEnabled(true);
        txtcontact_no.setEnabled(true);
        txtfax_no.setEnabled(true);
        txtmobile_no.setEnabled(true);
        txtemail_id.setEnabled(true);
        txtcountry_name.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtcontact_company.setText("");
        txtcontact_person.setText("");
        txtcontact_designation.setText("");
        txtdeals_in.setText("");
        txtcontact_of.setText("");
        txtreference_name.setText("");
        txtadd1.setText("");
        txtadd2.setText("");
        txtadd3.setText("");
        txtadd4.setText("");
        txtstate.setText("");
        txtcontact_no.setText("");
        txtfax_no.setText("");
        txtmobile_no.setText("");
        txtemail_id.setText("");
        txtcountry_name.setText("");
    }

    private void Highlightborder() {
        txtcontact_company.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtcontact_person.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtcontact_designation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtdeals_in.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtcontact_of.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtreference_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtadd1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtadd2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtadd3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtadd4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtstate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtfax_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtmobile_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtemail_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
    }

    private void disableViewsMethod() {
        txtcontact_company.setEnabled(false);
        txtcontact_person.setEnabled(false);
        txtcontact_designation.setEnabled(false);
        txtdeals_in.setEnabled(false);
        txtcontact_of.setEnabled(false);
        txtreference_name.setEnabled(false);
        txtadd1.setEnabled(false);
        txtadd2.setEnabled(false);
        txtadd3.setEnabled(false);
        txtadd4.setEnabled(false);
        txtstate.setEnabled(false);
        txtcontact_no.setEnabled(false);
        txtfax_no.setEnabled(false);
        txtmobile_no.setEnabled(false);
        txtemail_id.setEnabled(false);
        txtcountry_name.setEnabled(false);
    }

    private void initializeViews() {
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        toolbar = findViewById(R.id.toolbar);
        btn_cancel = findViewById(R.id.btn_cancel);
        txtcontact_company = findViewById(R.id.txtcontact_company);
        txtcontact_person = findViewById(R.id.txtcontact_person);
        txtcontact_designation = findViewById(R.id.txtcontact_designation);
        txtdeals_in = findViewById(R.id.txtdeals_in);
        txtcontact_of = findViewById(R.id.txtcontact_of);
        txtreference_name = findViewById(R.id.txtreference_name);
        txtadd1 = findViewById(R.id.txtadd1);
        txtadd2 = findViewById(R.id.txtadd2);
        txtadd3 = findViewById(R.id.txtadd3);
        txtadd4 = findViewById(R.id.txtadd4);
        txtstate = findViewById(R.id.txtstate);
        txtcontact_no = findViewById(R.id.txtcontact_no);
        txtfax_no = findViewById(R.id.txtfax_no);
        txtmobile_no = findViewById(R.id.txtmobile);
        txtemail_id = findViewById(R.id.txtemail);
        txtcountry_name = findViewById(R.id.txtcountry_name);
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
        if(controll.equals("txtstate")) txtapi_code.setText("EP825");
        if(controll.equals("txtcontact_designation")) txtapi_code.setText("EP838");

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
                    case "txtcontact_designation" : txtcontact_designation.setText(list_selected_text.trim());
                        break;
                    case "txtreference_name" : txtreference_name.setText(list_selected_text.trim());
                        break;
                    case "txtstate" : txtstate.setText(list_selected_text.trim());
                        break;
                    case "txtcountry_name" : txtcountry_name.setText(list_selected_text.trim());
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });

    }

}


class AsyncCaller_crm_contacts_master extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread
    }
    @Override
    protected Void doInBackground(Void... params) {
        frm_crm_contacts_master.xcontact_designation = new ArrayList<>();
        frm_crm_contacts_master.xcountry = new ArrayList<>();
        frm_crm_contacts_master.xreference_name = new ArrayList<>();
        frm_crm_contacts_master.xstate = new ArrayList<>();

        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
        frm_crm_contacts_master.xcontact_designation = finapi.fill_record_in_listview_popup("EP838");
        frm_crm_contacts_master.xreference_name = finapi.fill_record_in_listview_popup("EP815");
        frm_crm_contacts_master.xstate = finapi.fill_record_in_listview_popup("EP825");
        frm_crm_contacts_master.xcountry = finapi.fill_record_in_listview_popup("EP824");
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
    }
}
