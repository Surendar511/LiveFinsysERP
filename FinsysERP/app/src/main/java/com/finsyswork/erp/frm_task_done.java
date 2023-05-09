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
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class frm_task_done extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button btn_new,btn_save, btn_cancel;
    Toolbar toolbar;
    EditText txtremarks, txttask_target_dt, txtnext_date_follow_up,txturgency;
    public static EditText txttask_given_by,txttask_topic;
    String xseperator = "#~#", xbranch="00", xtype ="1N";
    public static ArrayList<String> xtask_topic = new ArrayList<>();
    RadioButton radio_yes, radio_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_task_done);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_task_done.this;
        finapi.deleteJsonResponseFile();


        new AsyncCaller_task_done().execute();
        initializeViews();
        btn_cancel.setText("EXIT");

       btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_task_done.this);
        Highlightborder();


        txttask_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogBox(frm_task_done.this, xtask_topic, "txttask_topic");
            }
        });

        txtnext_date_follow_up.setOnClickListener(new View.OnClickListener() {
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

                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);
                txttask_topic.setBackgroundResource(R.color.light_blue);
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

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));txttask_topic.setBackgroundResource(R.color.light_grey);
                    radio_yes.setChecked(true);
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
                    fgen.showApiName(frm_task_done.this, "aVisitio_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_task_done.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txttask_topic.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Fill Task Topic!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txttask_given_by.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Task Give BY Field Should Not Be Empty!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txttask_target_dt.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Target Date Was Field Should Not Be Empty!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txturgency.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Urgency Field Should Not Be Empty!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_task_done.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = "";
                                String xradio = "Y";
                                String xassigned = txttask_topic.getText().toString();
                                String xtask_given_by = txttask_given_by.getText().toString();
                                String xtask_target_dt = txttask_target_dt.getText().toString();
                                String xurgency = txturgency.getText().toString();
                                if(radio_no.isChecked()) xradio = "N";
                                String xremarks = txtremarks.getText().toString();
                                String xnext_follow_up_date = txtnext_date_follow_up.getText().toString();
                                post_param  = xbranch + xseperator + xtype + xseperator + xassigned + xseperator
                                        + xtask_given_by + xseperator + xtask_target_dt + xseperator + xurgency
                                        + xseperator + xradio + xseperator + xremarks + xseperator +xnext_follow_up_date + "!~!~!";
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aVisitio_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP903");
                                boolean msg = finapi.showAlertMessage(frm_task_done.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                radio_yes.setChecked(true);
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    private void enabledViewMethod() {
        txttask_topic.setEnabled(true);
        txttask_given_by.setEnabled(true);
        txttask_target_dt.setEnabled(true);
        txturgency.setEnabled(true);
        txtnext_date_follow_up.setEnabled(true);
        txtremarks.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txttask_topic.setText("");
        txttask_given_by.setText("");
        txttask_target_dt.setText("");
        txturgency.setText("");
        txtnext_date_follow_up.setText("");
        txtremarks.setText("");
    }

    private void Highlightborder() {
        txttask_topic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txttask_topic.setEnabled(false);
        txttask_given_by.setEnabled(false);
        txttask_target_dt.setEnabled(false);
        txturgency.setEnabled(false);
        txtnext_date_follow_up.setEnabled(false);
        txtremarks.setEnabled(false);
    }

    private void initializeViews() {
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        toolbar = findViewById(R.id.toolbar);
        txttask_target_dt = findViewById(R.id.txttask_target_dt);
        txtnext_date_follow_up = findViewById(R.id.txtnext_date_follow_up);
        txturgency = findViewById(R.id.txturgency);
        txttask_topic = findViewById(R.id.txttask_topic);
        txttask_given_by = findViewById(R.id.txttask_given_by);
        txtremarks = findViewById(R.id.txtremarks);
        radio_yes = findViewById(R.id.radio_yes);
        radio_no = findViewById(R.id.radio_no);
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
                    case "txttask_topic" :
                        String xfull_string = list_selected_text;
                        txttask_topic.setText(list_selected_text);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
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
        txtnext_date_follow_up.setText(date);
    }
}

class AsyncCaller_task_done extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread
    }
    @Override
    protected Void doInBackground(Void... params) {
        frm_task_done.xtask_topic = new ArrayList<>();
        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
        frm_task_done.xtask_topic = finapi.fill_record_in_listview_popup("EP835");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
    }
}
