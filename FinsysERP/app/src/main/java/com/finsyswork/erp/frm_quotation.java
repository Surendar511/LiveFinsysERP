package com.finsyswork.erp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class frm_quotation extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button btn_add, btn_new,btn_save, btn_cancel;
    Toolbar toolbar;
    EditText txtcustomer_name, txtitem, txtqty, txtrate, txtpo_no, txtpart_no, txtsale_type;
    TextView txtdate;
    RecyclerView recyclerView;
    comman_adapter adapter;
    sqliteHelperClass handler;
    String xseperator = "#~#", xbranch="00", xtype ="";
    public static ArrayList<String> xcustomer_name = new ArrayList<>();
    public static ArrayList<String> xitem = new ArrayList<>();
    public static ArrayList<String> xsale_type = new ArrayList<>();
    public static ArrayList<comman_model> list = new ArrayList<>();
    String xcustomer_code = "", xitem_code = "-";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_quotation);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        finapi.context = frm_quotation.this;
        finapi.deleteJsonResponseFile();


        fgen.xcard_view_name = "sale_order_card_View";
        handler = new sqliteHelperClass(this);
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_quotation.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        frm_quotation.xsale_type = finapi.fill_record_in_listview_popup("EP816A");
                        frm_quotation.xcustomer_name = finapi.fill_record_in_listview_popup_for_truck_loading("EP815");
                        new AsyncCaller_quotation().execute();
                        progressDialog.dismiss();
                    }
                }, 100);

        initializeViews();
        btn_cancel.setText("EXIT");
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_quotation.this);
        Highlightborder();

        handler.clear_data();
        list = handler.comman_get_data();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new comman_adapter(getApplicationContext(), list);
        recyclerView.setAdapter(adapter);

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

                txtsale_type.setBackgroundResource(R.color.light_blue);
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
                    list = handler.comman_get_data();
                    adapter = new comman_adapter(getApplicationContext(), list);
                    recyclerView.setAdapter(adapter);
                    txtsale_type.setBackgroundResource(R.color.light_grey);
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
                String xitem = txtitem.getText().toString();
                String xqty = txtqty.getText().toString();
                String xrate = txtrate.getText().toString();
                String xdate = txtdate.getText().toString();

                if(xitem.isEmpty() || txtqty.getText().toString().isEmpty() || txtrate.getText().toString().isEmpty() || txtdate.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select All Fields First!!", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean check_duplicacy = handler.CheckDuplicacy("col1", xitem);
                if(check_duplicacy)
                {
                    androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_quotation.this).create();
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
                handler.commaninsert_data(new comman_model(xitem, xqty, xrate, xdate));
                list = handler.comman_get_data();
                adapter = new comman_adapter(getApplicationContext(), list);
                recyclerView.setAdapter(adapter);
                txtitem.setText("");
                txtpart_no.setText("");
                txtqty.setText("");
                txtqty.setText("");
                txtrate.setText("");
                txtdate.setText("");
            }
        });

        txtcustomer_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_quotation.this, xcustomer_name, "txtcustomer_name");
            }
        });
        txtitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_quotation.this, xitem, "txtitem");
            }
        });
        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialogue();
            }
        });
        txtsale_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogBox(frm_quotation.this, xsale_type, "txtsale_type");
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_quotation.this, "aSomasQ_ins");
                }
            }
        });

          toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_quotation.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtsale_type.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select Sale Type First!!", Toast.LENGTH_LONG).show();
                    txtsale_type.setBackgroundResource(R.color.light_blue);
                    return;
                }
                if(txtpo_no.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Enquiry No.!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_quotation.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                list = handler.comman_get_data();
                                if(list.size() <=0)
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Invalid Data!!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                xtype = txtsale_type.getText().toString();
                                ArrayList<comman_model> bullets = list;
                                String xpo_no = txtpo_no.getText().toString();
                                String post_param = "";
                                for(comman_model b : bullets) {
                                    String xicode  =b.xcol2.split("---")[0].trim();
                                    String xqty = b.xcol3;
                                    String xrate = b.xcol4;
                                    String xdate = b.xcol5;
                                    post_param  += xbranch + xseperator + xtype + xseperator + xcustomer_code + xseperator
                                            + xicode + xseperator + xqty + xseperator + xrate + xseperator + xdate + xseperator
                                            + xpo_no + "!~!~!";
                                }
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aSomasQ_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP903");
                                boolean msg = finapi.showAlertMessage(frm_quotation.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                handler.clear_data();
                                list = handler.comman_get_data();
                                adapter = new comman_adapter(getApplicationContext(), list);
                                recyclerView.setAdapter(adapter);
                                progressDialog.dismiss();
                            }
                        }, 100);
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

    private void enabledViewMethod() {
        txtcustomer_name.setEnabled(true);
        txtpo_no.setEnabled(true);
        txtitem.setEnabled(true);
        txtpart_no.setEnabled(true);
        txtqty.setEnabled(true);
        txtrate.setEnabled(true);
        txtdate.setEnabled(true);
        txtsale_type.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtcustomer_name.setText("");
        txtpo_no.setText("");
        txtitem.setText("");
        txtpart_no.setText("");
        txtqty.setText("");
        txtrate.setText("");
        txtdate.setText("");
        txtsale_type.setText("");
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
        txtpo_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtitem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtpart_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtrate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtcustomer_name.setEnabled(false);
        txtpo_no.setEnabled(false);
        txtitem.setEnabled(false);
        txtpart_no.setEnabled(false);
        txtqty.setEnabled(false);
        txtrate.setEnabled(false);
        txtdate.setEnabled(false);
        txtsale_type.setEnabled(false);
    }

    private void initializeViews() {
        txtcustomer_name = findViewById(R.id.txtcustomer_name);
        txtitem = findViewById(R.id.txtitem);
        toolbar = findViewById(R.id.toolbar);
        txtsale_type = findViewById(R.id.txtsale_type);
        txtpo_no = findViewById(R.id.txtpo_no);
        txtpart_no = findViewById(R.id.txtpart_no);
        txtrate = findViewById(R.id.txtrate);
        txtqty = findViewById(R.id.txtqty);
        txtdate = findViewById(R.id.txtdelivery_dt);
        recyclerView = findViewById(R.id.recycler);
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_add = findViewById(R.id.btn_add);
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
        if(controll.equals("txtcustomer_name")) txtapi_code.setText("EP815");
        if(controll.equals("txtitem")) txtapi_code.setText("EP821F");
        if(controll.equals("txtsale_type")) txtapi_code.setText("EP816A");

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
                String list_selected_text = adapter.getItem(position).toString().trim();
                switch (controll)
                {
                    case "txtcustomer_name" :
                        String xcustomer = list_selected_text.split("---")[0].trim() + "---" + list_selected_text.split("---")[1].trim();
                        String xdestination = list_selected_text.split("---")[2].trim();
                        xcustomer_code = xcustomer.split("---")[1];
                        txtcustomer_name.setText(xcustomer);
                        break;
                    case "txtitem" :
                        String xiname = list_selected_text.split("---")[0].trim() + "---" + list_selected_text.split("---")[1].trim();
                        String xpart_no = list_selected_text.split("---")[2].trim();
                        xitem_code = list_selected_text.split("---")[0].trim();
                        txtitem.setText(xiname);
                        txtpart_no.setText(xpart_no);
                        break;
                    case "txtsale_type" : txtsale_type.setText(list_selected_text.trim());
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
        fgen.xcard_view_name = "";
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


class AsyncCaller_quotation extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread
    }
    @Override
    protected Void doInBackground(Void... params) {
        frm_quotation.xitem = new ArrayList<>();

        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here


        frm_quotation.xitem = finapi.fill_record_in_listview_popup_for_item_part_no("EP821F", "-");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
    }
}
