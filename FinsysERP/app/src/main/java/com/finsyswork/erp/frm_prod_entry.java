package com.finsyswork.erp;

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

public class frm_prod_entry extends AppCompatActivity{
    Button btn_add, btn_new,btn_save, btn_cancel;
    Toolbar toolbar;
    EditText txtdeptt, txtitem, txtqty, txtpart_no, txtjob_no;
    RecyclerView recyclerView;
    comman_adapter adapter;
    sqliteHelperClass handler;
    String xseperator = "#~#", xbranch="00", xtype ="EX";
    public static ArrayList<String> xdeptt = new ArrayList<>();
    public static ArrayList<String> xitem = new ArrayList<>();
    public static ArrayList<comman_model> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_prod_entry);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_prod_entry.this;
        finapi.deleteJsonResponseFile();

        fgen.xcard_view_name = "sale_order_schedule_booking_card_view";
        handler = new sqliteHelperClass(this);
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_prod_entry.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        frm_prod_entry.xdeptt = finapi.fill_record_in_listview_popup_for_truck_loading("EP819");
                        frm_prod_entry.xitem = finapi.fill_record_in_listview_popup_for_item_part_no("EP821F", "-");
//                        new AsyncCallesr_frm_prod_entry().execute();
                        progressDialog.dismiss();
                    }
                }, 100);

        initializeViews();
        btn_cancel.setText("EXIT");
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_prod_entry.this);
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
                txtdeptt.setBackgroundResource(R.color.light_blue);
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
                    txtdeptt.setBackgroundResource(R.color.light_grey);
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
                String xjob_no = txtjob_no.getText().toString();
                if(xitem.isEmpty() || txtqty.getText().toString().isEmpty() || txtjob_no.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select All Fields First!!", Toast.LENGTH_LONG).show();
                    return;
                }
                boolean check_duplicacy = handler.CheckDuplicacy("col1", xitem);
                if(check_duplicacy)
                {
                    androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_prod_entry.this).create();
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

                handler.commaninsert_data(new comman_model(xitem, xqty, xjob_no));
                list = handler.comman_get_data();
                adapter = new comman_adapter(getApplicationContext(), list);
                recyclerView.setAdapter(adapter);

                txtitem.setText("");
                txtpart_no.setText("");
                txtqty.setText("");
                txtqty.setText("");
                txtjob_no.setText("");
            }
        });

        txtdeptt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_prod_entry.this, xdeptt, "txtdeptt");
            }
        });

        txtitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_prod_entry.this, xitem, "txtitem");
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_prod_entry.this, "aBasprod_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_prod_entry.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_prod_entry.this);
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
                                String xdeptt = txtdeptt.getText().toString().split("---")[0].trim();
                                ArrayList<comman_model> bullets = list;
                                String post_param = "";
                                for(comman_model b : bullets) {
                                    String xitem = b.xcol2.split("---")[0].trim();
                                    String xqty = b.xcol3;
                                    String xdate = b.xcol4;
                                    post_param  += xbranch + xseperator + xtype + xseperator + xdeptt + xseperator
                                            + xitem + xseperator + xqty + xseperator + xdate +  "!~!~!";
                                }
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aBasprod_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP903");
                                boolean msg = finapi.showAlertMessage(frm_prod_entry.this, result);
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

    private void enabledViewMethod() {
        txtdeptt.setEnabled(true);
        txtitem.setEnabled(true);
        txtpart_no.setEnabled(true);
        txtqty.setEnabled(true);
        txtjob_no.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtdeptt.setText("");
        txtitem.setText("");
        txtpart_no.setText("");
        txtqty.setText("");
        txtjob_no.setText("");
    }

    private void Highlightborder() {
        txtdeptt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtjob_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtdeptt.setEnabled(false);
        txtitem.setEnabled(false);
        txtpart_no.setEnabled(false);
        txtqty.setEnabled(false);
        txtjob_no.setEnabled(false);
    }

    private void initializeViews() {
        txtdeptt = findViewById(R.id.txtdeptt);
        txtitem = findViewById(R.id.txtitem);
        txtpart_no = findViewById(R.id.txtpart_no);
        txtqty = findViewById(R.id.txtqty);
        txtjob_no = findViewById(R.id.txtjob_no);
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
        if(controll.equals("txtdeptt")) txtapi_code.setText("EP819");
        if(controll.equals("txtitem")) txtapi_code.setText("EP821F");

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
                    case "txtdeptt" :
                        txtdeptt.setText(list_selected_text);
                        break;
                    case "txtitem" :
                        String xiname = list_selected_text.split("---")[0].trim() + "---" + list_selected_text.split("---")[1].trim();
                        String xpart_no = list_selected_text.split("---")[2].trim();
                        txtitem.setText(xiname);
                        txtpart_no.setText(xpart_no);
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
        fgen.xcard_view_name = "";
    }



    class AsyncCaller_frm_prod_entry extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
        }

        @Override
        protected Void doInBackground(Void... params) {
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
        }
    }
}
