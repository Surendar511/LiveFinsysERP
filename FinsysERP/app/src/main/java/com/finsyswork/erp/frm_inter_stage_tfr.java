package com.finsyswork.erp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class frm_inter_stage_tfr extends AppCompatActivity {

    public static Button btn_new,btn_save, btn_cancel, btn_scan, btn_add;
    Toolbar toolbar;
    public static EditText  txtqty, txtstage_to, txtremarks, txtbatch_no;
    public static EditText txtstage_from,txtlabel;
    String xseperator = "#~#", xbranch="00", xtype ="1N";
    public static ArrayList<String> xlabel = new ArrayList<>();
    public static ArrayList<String> xstage_to = new ArrayList<>();
    jobw_model model;
    jobw_adapter adapter;
    public static sqliteHelperClass handler;
    private RecyclerView recyclerView;
    public static ArrayList<jobw_model> inter_stage_list = new ArrayList<>();
    public  static  String xitem = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_inter_stage_tfr);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_inter_stage_tfr.this;
        finapi.deleteJsonResponseFile();
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_inter_stage_tfr.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        frm_inter_stage_tfr.xstage_to = finapi.fill_record_in_listview_popup("EP820");
                        new AsyncCaller_inter_stage_tfr().execute();
                        progressDialog.dismiss();
                    }
                }, 100);

        fgen.frm_request = "frm_inter_stage_tfr";
        initializeViews();
        handler = new sqliteHelperClass(this);
        btn_cancel.setText("EXIT");
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_inter_stage_tfr.this);
        Highlightborder();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new jobw_adapter(getApplicationContext());
        recyclerView.setAdapter(adapter);

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fgen.frm_request="frm_inter_stage_tfr";
                txtlabel.requestFocus();
                startActivity(new Intent(getApplicationContext(), scannerView.class));
                txtlabel.requestFocus();
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
                txtlabel.requestFocus();
                txtlabel.setBackgroundResource(R.color.light_blue);

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
                    txtlabel.setEnabled(true);
                    txtlabel.requestFocus();
                    txtlabel.setBackgroundResource(R.color.light_grey);
                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

                    frm_jobw_prod.output_list = new ArrayList<>();
                    adapter.notifyDataSetChanged();
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });

        txtlabel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == 19 || keyCode == 21)) {
                    if (txtlabel.getText().toString().trim().length() > 2) {
                        ScannedText(txtlabel.getText().toString().trim());
                        txtlabel.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frm_jobw_prod.xgetTab = "O";
                String xbatch = txtbatch_no.getText().toString();
                String xqty = txtqty.getText().toString();

                for (jobw_model m : frm_jobw_prod.output_list){
                    if(m.xcol2.equals(xbatch))
                    {
                        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_inter_stage_tfr.this).create();
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
                }
                if(xitem.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Scanned QR Correctly!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtlabel.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Scan Stage First!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtqty.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Fill Qty!!", Toast.LENGTH_LONG).show();
                    return;
                }
                model = new jobw_model(xitem, xbatch, xqty, "-");
                frm_jobw_prod.output_list.add(model);
                recyclerView.setAdapter(adapter);
                txtqty.setText("");
                txtlabel.setText("");
                txtlabel.requestFocus();
            }
        });

        txtstage_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_inter_stage_tfr.this, xstage_to, "txtstage_to");
                txtlabel.requestFocus();
            }
        });

//        txtstage_from.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShowDialogBox(frm_inter_stage_tfr.this, xstage_to, "txtstage_from");
//                txtlabel.requestFocus();
//            }
//        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_inter_stage_tfr.this, "aInterStg_ins");
                    txtlabel.requestFocus();
                }
                txtlabel.requestFocus();
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_inter_stage_tfr.this);
                txtlabel.requestFocus();
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtstage_from.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Stage From is Empty!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtstage_to.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select Stage To!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(frm_jobw_prod.output_list.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Add Stage Transfer To Grid!!", Toast.LENGTH_LONG).show();
                    return;
                }

                final MyProgressdialog progressDialog = new MyProgressdialog(frm_inter_stage_tfr.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = "";
                                String xstage_from = txtstage_from.getText().toString();
                                String xstage_to = txtstage_to.getText().toString();
                                String xremarks = txtremarks.getText().toString();
                                for (jobw_model m : frm_jobw_prod.output_list) {
                                    post_param  += xbranch + xseperator + xtype + xseperator + m.xcol1.split("---")[0] + xseperator
                                            + xstage_to.split("---")[0] + xseperator + xstage_from  + xseperator
                                            + m.xcol2 +xseperator + m.xcol3 + xseperator + xremarks + "!~!~!";
                                }
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aInterStg_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP903");
                                boolean msg = finapi.showAlertMessage(frm_inter_stage_tfr.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                frm_jobw_prod.output_list = new ArrayList<>();
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                newBtnClickedMethod();
                                txtlabel.requestFocus();
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    private void ScannedText(String rawResult){
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_inter_stage_tfr.this);
        String scanned_text = ""+rawResult;
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        frm_inter_stage_tfr.xitem = "";
                        finapi.context = frm_inter_stage_tfr.this;
                        frm_lead_enquiry.xfrm_lead_enquiry = "Y";
                        frm_lead_enquiry.xfg_sub_group_code = scanned_text.split("___")[0];
                        ArrayList<team6> x = finapi.getApi6(fgen.mcd, "aInterStg_check","-", fgen.muname, fgen.cdt1.substring(6,10), "-", frm_lead_enquiry.xfg_sub_group_code);
                        boolean msg = finapi.RecieveDataFromApi(frm_inter_stage_tfr.this, x);
                        if(!msg)
                        {
                            txtlabel.setText("");
                            progressDialog.dismiss();
                            return;
                        }
//                                ArrayList<String> x = finapi.fill_record_in_listview_popup("aInterStg_check");
                        frm_inter_stage_tfr.txtlabel.setText(scanned_text);
                        frm_inter_stage_tfr.txtstage_from.setText(fgen.xpopup_col2);
                        frm_inter_stage_tfr.txtbatch_no.setText(fgen.xpopup_col5);
                        frm_inter_stage_tfr.txtqty.setText(fgen.xpopup_col6);
                        frm_inter_stage_tfr.xitem = fgen.xpopup_col3 + "---" + fgen.xpopup_col4;
                        if(fgen.mcd == "ARUB") {
                            btn_add.performClick();
                        }
                        txtlabel.requestFocus();
                        progressDialog.dismiss();
                    }
                }, 100);

    }

    private void enabledViewMethod() {
        txtlabel.setEnabled(true);
        txtstage_from.setEnabled(true);
        txtqty.setEnabled(true);
        txtstage_to.setEnabled(true);
        txtremarks.setEnabled(true);
        txtbatch_no.setEnabled(true);
        txtlabel.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtlabel.setText("");
        txtstage_from.setText("");
        txtqty.setText("");
        txtstage_to.setText("");
        txtremarks.setText("");
        txtbatch_no.setText("");

        txtlabel.requestFocus();
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
        txtstage_to.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtbatch_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgen.frm_request = "";    }

    private void disableViewsMethod() {
//        txtlabel.setEnabled(false);
        txtstage_from.setEnabled(false);
        txtqty.setEnabled(false);
        txtstage_to.setEnabled(false);
        txtremarks.setEnabled(false);
        txtbatch_no.setEnabled(false);
    }

    private void initializeViews() {
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_add = findViewById(R.id.btn_add);
        btn_scan = findViewById(R.id.btn_scan);
        toolbar = findViewById(R.id.toolbar);
        txtlabel = findViewById(R.id.txtlabel);
        txtstage_from = findViewById(R.id.txtstage_from);
        txtqty = findViewById(R.id.txtqty);
        txtstage_to = findViewById(R.id.txtstage_to);
        txtremarks = findViewById(R.id.txtremarks);
        txtbatch_no = findViewById(R.id.txtbatch_no);
        recyclerView = findViewById(R.id.recycler);
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
        if(controll.equals("txtstage_to")) txtapi_code.setText("EP820");
        if(controll.equals("txtlabel")) txtapi_code.setText("EP825");

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
                    case "txtlabel" : txtlabel.setText(list_selected_text);
                        break;
                    case "txtstage_to" : txtstage_to.setText(list_selected_text);
                        break;
                    case "txtstage_from" : txtstage_from.setText(list_selected_text);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });

    }

}


class AsyncCaller_inter_stage_tfr extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread
    }
    @Override
    protected Void doInBackground(Void... params) {
        frm_inter_stage_tfr.xlabel = new ArrayList<>();
        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
        frm_inter_stage_tfr.xlabel = finapi.fill_record_in_listview_popup("EP835");

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
    }
}
