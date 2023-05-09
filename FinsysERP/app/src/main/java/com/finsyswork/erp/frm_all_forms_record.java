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

public class frm_all_forms_record extends AppCompatActivity {

    Button btn_new,btn_save, btn_cancel;
    Toolbar toolbar;
    EditText txtactivity_name, txttype_of_activity, txtqty, txtinform_to, txtremarks;
    String xseperator = "#~#", xbranch="00", xtype ="1N";
    public static ArrayList<String> xactivity_name = new ArrayList<>();
    public static ArrayList<String> xtype_of_activity_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_all_forms_record);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        new AsyncCaller_all_forms_record().execute();
        initializeViews();
        btn_cancel.setText("EXIT");
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_all_forms_record.this);
        Highlightborder();

        finapi.context = frm_all_forms_record.this;
        finapi.deleteJsonResponseFile();


        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBtnClickedMethod();
                enabledViewMethod();
                btn_save.setEnabled(true);
                btn_new.setEnabled(false);
                btn_cancel.setText("CANCEL");
                txtactivity_name.setBackgroundResource(R.color.light_blue);

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
                    txtactivity_name.setBackgroundResource(R.color.light_grey);

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });
        txtactivity_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_all_forms_record.this, xactivity_name, "txtactivity_name");
            }
        });
        txttype_of_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_all_forms_record.this, xtype_of_activity_name, "txttype_of_activity");
            }
        });


        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_all_forms_record.this, "aVisitio_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_all_forms_record.this);
                return false;
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtactivity_name.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select Activity Name!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txttype_of_activity.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select Type!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtqty.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Fill Qty!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtinform_to.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Fill Inform Field!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_all_forms_record.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = "";
                                String xactivity_name = txtactivity_name.getText().toString();
                                String xtype_of_activity = txttype_of_activity.getText().toString();
                                String xqty = txtqty.getText().toString();
                                String xinform = txtinform_to.getText().toString();
                                String xremarks = txtremarks.getText().toString();
                                post_param  = xbranch + xseperator + xtype + xseperator + xactivity_name + xseperator
                                        + xtype_of_activity + xseperator + xqty + xseperator + xinform
                                        + xseperator + xremarks + "!~!~!";
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aVisitio_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP903");
                                boolean msg = finapi.showAlertMessage(frm_all_forms_record.this, result);
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

    private void enabledViewMethod() {
        txtactivity_name.setEnabled(true);
        txttype_of_activity.setEnabled(true);
        txtqty.setEnabled(true);
        txtinform_to.setEnabled(true);
        txtremarks.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtactivity_name.setText("");
        txttype_of_activity.setText("");
        txtqty.setText("");
        txtinform_to.setText("");
        txtremarks.setText("");
    }

    private void Highlightborder() {
        txtqty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtinform_to.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
    }

    private void disableViewsMethod() {
        txtactivity_name.setEnabled(false);
        txttype_of_activity.setEnabled(false);
        txtqty.setEnabled(false);
        txtinform_to.setEnabled(false);
        txtremarks.setEnabled(false);
    }

    private void initializeViews() {
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        toolbar = findViewById(R.id.toolbar);
        txtactivity_name = findViewById(R.id.txtactivity_name);
        txttype_of_activity = findViewById(R.id.txttype_of_activity);
        txtqty = findViewById(R.id.txtqty);
        txtinform_to = findViewById(R.id.txtinform);
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
        if(controll.equals("txtactivity_name")) txtapi_code.setText("EP835");
        if(controll.equals("txttype_of_activity")) txtapi_code.setText("EP835");

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
                listViewadapter.getFilter().filter(s.toString());
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
                    case "txtactivity_name" : txtactivity_name.setText(list_selected_text.trim());
                        break;
                    case "txttype_of_activity" : txttype_of_activity.setText(list_selected_text.trim());
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });

    }

}


class AsyncCaller_all_forms_record extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread
    }
    @Override
    protected Void doInBackground(Void... params) {
        frm_all_forms_record.xactivity_name = new ArrayList<>();
        frm_all_forms_record.xtype_of_activity_name = new ArrayList<>();
        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
        frm_all_forms_record.xactivity_name = finapi.fill_record_in_listview_popup("EP835");
        frm_all_forms_record.xtype_of_activity_name = finapi.fill_record_in_listview_popup("EP835");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
    }
}
