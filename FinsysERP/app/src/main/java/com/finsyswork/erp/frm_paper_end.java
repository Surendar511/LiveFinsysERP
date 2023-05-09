package com.finsyswork.erp;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/*
    This Form Used Material Issue Slip Components
    Like : Card, Model, Adapter, Database.
 */
public class frm_paper_end extends AppCompatActivity {
    // Static CONSTANT VALUE
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE,
    };

    Button btnscan_job, btnscan_reel, btn_save, btn_new, btn_cancel, btn_add, btn_read, btn_print;
    public static EditText txtscan_job, txtscan_reel, txt_barcode, txtwip_stage;
    RecyclerView recyclerView;
    sqliteHelperClass handler;
    Toolbar toolbar;
    public static String xscanned_job_no = "";
    public String xreel_for_return = "";
    public static issue_slip_adapter adapter;
    public static issue_req_model issue_req_model;
    public static ArrayList<issue_req_model> list = new ArrayList<> ( );
    public static ArrayList<String> xjob = new ArrayList<> ( );
    public static ArrayList<String> xwip_stage = new ArrayList<> ( );
    public static String btn_clicked = "", xicode = "", xreel_check_api = "";
    String xseperator = "#~#", xbranch = "00", xtype = "31";
    String xscan_job_first = "Y";
    RadioButton radio_scan_job, radio_scan_reel;

    Button mainBtn;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket socket;
    BluetoothDevice bluetoothDevice;
    OutputStream outputStream;
    InputStream inputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String value = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_frm_paper_end);
        this.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        frm_lead_enquiry.xfrm_lead_enquiry = "N";
        initializeViews ( );
        if (fgen.frm_request.equals ("frm_reel_issue_return")) {
            toolbar.setTitle ("Reel Issue Return");
            xreel_check_api = "areel_retcheck";
            xtype = "11";
        }
        try {
            finapi.context = frm_paper_end.this;
            finapi.deleteJsonResponseFile ( );

            fgen.xextra1 = "reel";
            final MyProgressdialog progressDialog = new MyProgressdialog (frm_paper_end.this);
            progressDialog.show ( );
            new Handler ( ).postDelayed (
                    new Runnable ( ) {
                        public void run() {
                            xjob = finapi.fill_record_in_listview_popup ("EP835");
                            xwip_stage = finapi.fill_record_in_listview_popup ("EP820");
                            progressDialog.dismiss ( );
                        }
                    }, 100);
            xreel_check_api = "areel_check";
            if (fgen.frm_request.equals ("frm_reel_issue_return")) {
                xreel_check_api = "areel_retcheck";
            }
            if (fgen.frm_request.equals ("frm_paper_end_for_reels")) {
                xreel_check_api = "areel_jccheck";
            }

            btn_new.getBackground ( ).setTint (btn_new.getResources ( ).getColor (R.color.btn_color));
            finapi.setColorOfStatusBar (frm_paper_end.this);
            handler = new sqliteHelperClass (frm_paper_end.this);
            handler.clear_data ( );
            setTextOnViews ( );
            disableViewsMethod ( );
            setSupportActionBar (toolbar);

            handler.clear_data ( );
            list = handler.issue_req_get_data ( );

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager (this, 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager (layoutManager);
            adapter = new issue_slip_adapter (frm_paper_end.this, list);
            recyclerView.setAdapter (adapter);

            txtscan_job.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    ShowDialogBox (frm_paper_end.this, xjob, "txtscan_job");
                    txt_barcode.requestFocus ( );
                }
            });

            txtwip_stage.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    ShowDialogBox (frm_paper_end.this, xwip_stage, "txtwip_stage");
                    txt_barcode.requestFocus ( );
                }
            });

            radio_scan_job.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener ( ) {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (radio_scan_job.isChecked ( )) {
                        xscan_job_first = "Y";
                    } else {
                        xscan_job_first = "N";
                    }
                }
            });

            btn_new.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View view) {
                    try {
                        newBtnClickedMethod ( );
                        btn_save.setEnabled (true);
                        btn_new.setEnabled (false);
                        btn_cancel.setText ("CANCEL");


                        btnscan_reel.setEnabled (true);
                        btnscan_job.setEnabled (true);
                        radio_scan_job.setEnabled (true);
                        radio_scan_reel.setEnabled (true);
                        txtscan_job.setEnabled (true);


                        Drawable buttonDrawable = btn_new.getBackground ( );
                        buttonDrawable = DrawableCompat.wrap (buttonDrawable);
                        //the color is a direct color int and not a color resource
                        DrawableCompat.setTint (buttonDrawable, Color.GRAY);
                        btn_new.setBackground (buttonDrawable);

                        txtscan_job.setBackgroundResource (R.color.light_blue);
                        txt_barcode.requestFocus ( );
                        xscan_job_first = "Y";
                    } catch (Exception e) {
                        new android.app.AlertDialog.Builder (frm_paper_end.this)
                                .setTitle ("Erorr Found!")
                                .setMessage ("" + e)
                                .setPositiveButton ("Ok", new DialogInterface.OnClickListener ( ) {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss ( );
                                    }
                                })
                                .show ( );
                    }
                }
            });

            btn_read.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    if (txt_barcode.getText ( ).toString ( ).trim ( ).length ( ) > 2) {
                        if (xscan_job_first.equals ("Y")) {
                            scanned_job_verification (txt_barcode.getText ( ).toString ( ).trim ( ));
                            txt_barcode.setText ("");
                            txt_barcode.requestFocus ( );
                            radio_scan_reel.setChecked (true);
                        } else {
                            xscanned_job_no = txtscan_job.getText ( ).toString ( ).split ("---")[0].trim ( );
                            if (xscanned_job_no.equals ("")) {
                                Toast.makeText (getApplicationContext ( ), "Please Scan Job Scard First!!", Toast.LENGTH_LONG).show ( );
                                return;
                            }

                            txtscan_reel.setText (txt_barcode.getText ( ).toString ( ).trim ( ));
                            boolean xcheck_reel_status = multi_reel_single_job (txt_barcode.getText ( ).toString ( ).trim ( ));
                            if (xcheck_reel_status) {
                                fillGrid ( );
                            }
                            txt_barcode.setText ("");
                            txtscan_reel.setText ("");
                        }
                    }
                }
            });

            btn_print.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    LayoutInflater factory = LayoutInflater.from (frm_paper_end.this);

                    //text_entry is an Layout XML file containing two text field to display in alert dialog
                    final View textEntryView = factory.inflate (R.layout.print_dialoge, null);

                    final EditText vchnum = (EditText) textEntryView.findViewById (R.id.txtvchnum);
                    final EditText vchdate = (EditText) textEntryView.findViewById (R.id.txtvchdate);

                    vchnum.setOnFocusChangeListener (new View.OnFocusChangeListener ( ) {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            if (b) {
                                view.setBackgroundResource (R.drawable.edittext_border_change);
                            } else {
                                view.setBackgroundResource (R.drawable.edittext_border_remove);
                            }
                        }
                    });
                    vchdate.setOnFocusChangeListener (new View.OnFocusChangeListener ( ) {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            if (b) {
                                view.setBackgroundResource (R.drawable.edittext_border_change);
                            } else {
                                view.setBackgroundResource (R.drawable.edittext_border_remove);
                            }
                        }
                    });

                    final AlertDialog.Builder alert = new AlertDialog.Builder (frm_paper_end.this);
                    alert.setIcon (R.drawable.success).setTitle ("ENTER REEL NUMBER").setView (textEntryView).setPositiveButton ("Print",
                            new DialogInterface.OnClickListener ( ) {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String xvchnum = vchnum.getText ( ).toString ( );  // reel no.
                                    String xvchdate = vchdate.getText ( ).toString ( ); // No Use.
                                    String data = xvchnum;  // reel no.
                                    fgen.x41 = finapi.seekiname (fgen.mcd, "SELECT PARAMS AS COL1 FROM CONTROLS WHERE ID='X41'", "COL1");
                                    fgen.webReportLink = "http://" + fgen.x41 + "/dprint.aspx?STR=ERP@AND@" + fgen.mcd + "@" + fgen.cdt1.substring (6, 10) + fgen.branchcd + "@" + fgen.muid + "@BVAL@ANDREELPRINT@" + data;
                                    //fgen.printReport(frm_paper_end.this);
                                    openPDF ( );
                                    // Verify Storage Access
                                    //verifyStoragePermission(frm_paper_end.this);
                                }
                            }).setNegativeButton ("Cancel",
                            new DialogInterface.OnClickListener ( ) {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    AlertDialog alertDialog = alert.create ( );
                                    alertDialog.dismiss ( );
                                }
                            });

                    alert.show ( );
                }
            });

            txt_barcode.setOnKeyListener (new View.OnKeyListener ( ) {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == 19 || keyCode == 21)) {
                        if (txt_barcode.getText ( ).toString ( ).trim ( ).length ( ) > 2) {
                            if (xscan_job_first.equals ("Y")) {
                                scanned_job_verification (txt_barcode.getText ( ).toString ( ).trim ( ));
                                txt_barcode.setText ("");
                                txt_barcode.requestFocus ( );
                                radio_scan_reel.setChecked (true);
                            } else {
                                xscanned_job_no = txtscan_job.getText ( ).toString ( ).split ("---")[0].trim ( );
                                if (xscanned_job_no.equals ("")) {
                                    Toast.makeText (getApplicationContext ( ), "Please Scan Job Scard First!!", Toast.LENGTH_LONG).show ( );
                                    return false;
                                }
                                txtscan_reel.setText (txt_barcode.getText ( ).toString ( ).trim ( ));
//                          scanner View.multi_reel_single_job(txtscan_reel.getText().toString());
                                boolean xcheck_reel_status = multi_reel_single_job (txt_barcode.getText ( ).toString ( ).trim ( ));
                                if (xcheck_reel_status) {
                                    fillGrid ( );
                                }
                                txt_barcode.setText ("");
                                txtscan_reel.setText ("");
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });

            btn_cancel.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View view) {
                    String btn_text = btn_cancel.getText ( ).toString ( );
                    if (btn_text == "CANCEL") {
                        newBtnClickedMethod ( );
                        disableViewsMethod ( );
                        btn_save.setEnabled (false);
                        btn_new.setEnabled (true);
                        btn_cancel.setText ("EXIT");

                        btn_new.getBackground ( ).setTint (btn_new.getResources ( ).getColor (R.color.btn_color));
                        handler.clear_data ( );
                        list = handler.issue_req_get_data ( );
                        adapter = new issue_slip_adapter (frm_paper_end.this, list);
                        recyclerView.setAdapter (adapter);

                        radio_scan_job.setChecked (true);
                        txtscan_job.setBackgroundResource (R.color.light_grey);
                    }
                    if (btn_text == "EXIT") {
                        finish ( );
                    }
                }
            });

            btnscan_job.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    fgen.qr_scanned_with_external = "N";
                    btn_clicked = "job";
                    startActivity (new Intent (getApplicationContext ( ), scannerView.class));
                    txt_barcode.requestFocus ( );
                }
            });

            btnscan_reel.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    xscanned_job_no = txtscan_job.getText ( ).toString ( ).split ("---")[0].trim ( );
                    if (xscanned_job_no.equals ("")) {
                        Toast.makeText (getApplicationContext ( ), "Please Scan Job Scard First!!", Toast.LENGTH_LONG).show ( );
                        return;
                    }
                    btn_clicked = "reel";
                    startActivity (new Intent (getApplicationContext ( ), scannerView.class));
                    txt_barcode.requestFocus ( );
                    adapter = new issue_slip_adapter (frm_paper_end.this, list);
                    adapter.notifyDataSetChanged ( );
                }
            });

            btn_add.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    fillGrid ( );
                }
            });

            fgen.toolbar_click_count = 0;
            toolbar.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    ++fgen.toolbar_click_count;
                    if (fgen.toolbar_click_count == 5) {
                        fgen.showApiName (frm_paper_end.this, "areel_issue");
                    }
                }
            });

            toolbar.setOnLongClickListener (new View.OnLongClickListener ( ) {
                @Override
                public boolean onLongClick(View v) {
                    finapi.ReadJSONResponse (frm_paper_end.this);
                    return false;
                }
            });

            btn_save.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    if (txtwip_stage.getText ( ).toString ( ).isEmpty ( )) {
                        Toast.makeText (getApplicationContext ( ), "Please Fill WIP State", Toast.LENGTH_LONG).show ( );
                        return;
                    }
                    final MyProgressdialog progressDialog = new MyProgressdialog (frm_paper_end.this);
                    progressDialog.show ( );
                    new Handler ( ).postDelayed (
                            new Runnable ( ) {
                                public void run() {
                                    String xmv_wip_stg = txtwip_stage.getText ( ).toString ( ).split ("---")[0].trim ( );
                                    String xjob_dt = "";
                                    xjob_dt = txtscan_job.getText ( ).toString ( ).substring (10, 20);
                                    list = handler.issue_req_get_data ( );
                                    String xjob_no = txtscan_job.getText ( ).toString ( ).substring (4, 10);
                                    if (txtscan_job.getText ( ).toString ( ).isEmpty ( ) || handler.issue_req_get_data ( ).size ( ) <= 0) {
                                        progressDialog.dismiss ( );
                                        Toast.makeText (getApplicationContext ( ), "Invalid Data!!", Toast.LENGTH_LONG).show ( );
                                        return;
                                    }
                                    String post_param = "";
                                    for (issue_req_model m : list) {
                                        String xicode = m.getItem ( ).toString ( ).split ("---")[0].trim ( );
                                        String xmv_qty = m.getQty ( ).toString ( );
                                        String xmv_reels = m.getXicode ( ).toString ( );
                                        post_param += xbranch + xseperator + xtype + xseperator + xicode + xseperator
                                                + xmv_reels + xseperator + xmv_qty + xseperator + xjob_no + xseperator + xjob_dt
                                                + xseperator + xmv_wip_stg + "!~!~!";
                                    }

                                    ArrayList<team> result = finapi.getApi (fgen.mcd, "areel_issue", post_param, fgen.muname, fgen.cdt1.substring (6, 10), fgen.btnid, "-");
                                    boolean msg = finapi.showAlertMessageForStockCheck (frm_paper_end.this, result);
                                    if (!msg) {
                                        progressDialog.dismiss ( );
                                        return;
                                    }
                                    newBtnClickedMethod ( );
                                    handler.clear_data ( );
                                    list = new ArrayList<> ( );
                                    adapter = new issue_slip_adapter (frm_paper_end.this, list);
                                    recyclerView.setAdapter (adapter);

                                    radio_scan_job.setChecked (true);

                                    progressDialog.dismiss ( );
                                }
                            }, 100);
                }
            });

        } catch (Exception e) {
            new android.app.AlertDialog.Builder (frm_paper_end.this)
                    .setTitle ("Erorr Found!")
                    .setMessage ("" + e)
                    .setPositiveButton ("Ok", new DialogInterface.OnClickListener ( ) {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss ( );
                        }
                    })
                    .show ( );
        }
    }

    // Access pdf from storage and using to Intent get options to view application in available applications.
    private void openPDF() {
        InitPrinter ( );
        Intent pdfOpenIntent = new Intent (Intent.ACTION_VIEW);
        pdfOpenIntent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenIntent.setClipData (ClipData.newRawUri ("", Uri.parse (fgen.webReportLink)));
        pdfOpenIntent.setDataAndType (Uri.parse (fgen.webReportLink), "application/pdf");
        pdfOpenIntent.addFlags (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        try {
            startActivity (pdfOpenIntent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            Toast.makeText (this, "There is no app to load corresponding PDF", Toast.LENGTH_LONG).show ( );
        }
    }

    void fillGrid() {
        {
            String xreel = txtscan_reel.getText ( ).toString ( );
            if (xreel.isEmpty ( )) {
                Toast.makeText (getApplicationContext ( ), "Please Scan Job And Reel First!!", Toast.LENGTH_LONG).show ( );
                return;
            }
            if (fgen.frm_request.equals ("frm_paper_end_for_jobs")) {
                if (frm_paper_end.issue_req_model != null) {
                    handler.issue_slip_insert_data (frm_paper_end.issue_req_model);
                    txtscan_job.setText ("");
                }
            } else {
                if (frm_paper_end.issue_req_model != null) {
                    handler.issue_slip_insert_data (frm_paper_end.issue_req_model);
                    txtscan_reel.setText ("");
                }
            }
            list = handler.issue_req_get_data ( );
            adapter = new issue_slip_adapter (frm_paper_end.this, list);
            recyclerView.setAdapter (adapter);

            txt_barcode.requestFocus ( );
        }
    }

    public boolean multi_reel_single_job(String rawResult) {
        String xbuild_qr_name = "";

        ArrayList<team6> result = finapi.getApi6 (fgen.mcd, "" + xreel_check_api, "" + rawResult.trim ( ), fgen.muname, fgen.cdt1.substring (6, 10), "#", "" + xscanned_job_no);
        Gson gson = new Gson ( );
        String listString = gson.toJson (result, new TypeToken<ArrayList<team>> ( ) {
        }.getType ( ));
        String data = "";
        try {
            JSONArray array = new JSONArray (listString);
            for (int i = 0; i < array.length ( ); i++) {
                JSONObject respObj = array.getJSONObject (i);
                data = respObj.getString ("col1");
                try {
                    frm_paper_end.xicode = respObj.getString ("col2");
                    frm_job_issue_entry.xreel_qty = respObj.getString ("col6");
                    xreel_for_return = respObj.getString ("col6");
                    String xcol4 = respObj.getString ("col4");
                    xbuild_qr_name = frm_paper_end.xicode + "---" + xcol4 + "---" + rawResult;
                } catch (Exception e) {
                }
            }
        } catch (JSONException e) {
            e.printStackTrace ( );
        }
        if (!data.equals ("Success") || data.equals ("")) {
            new android.app.AlertDialog.Builder (this)
                    .setTitle (Html.fromHtml ("<font color='#F80303'>Error</font>"))
                    .setMessage (Html.fromHtml ("<font color='#F80303'>" + frm_job_issue_entry.xreel_qty + "\"!!\"</font>"))
                    .setIcon (R.drawable.fail)
                    .setPositiveButton ("Ok", new DialogInterface.OnClickListener ( ) {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss ( );
                        }
                    })
                    .show ( );
            return false;
        } else {
            try {
                frm_paper_end.txtscan_reel.setText ("" + xbuild_qr_name);
            } catch (Exception e) {
            }
            try {
                frm_job_issue_entry.txtscan_reel.setText ("" + xbuild_qr_name);
            } catch (Exception e) {
            }
        }

        boolean duplicate = handler.CheckDuplicacy ("col3", "" + rawResult.trim ( ));
        if (duplicate) {
            AlertDialog alertDialog = new AlertDialog.Builder (this).create ( );
            alertDialog.setTitle ("Error");
            alertDialog.setMessage ("Sorry, Duplicate Rows Not Allowed!!");
            alertDialog.setButton (AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener ( ) {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss ( );
                        }
                    });
            alertDialog.show ( );
            return false;
        }
        if (fgen.frm_request.equals ("frm_reel_issue_return")) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder (this);
            alertDialog.setTitle ("REEL Qty");
            alertDialog.setMessage ("Enter Qty");

            final EditText input = new EditText (this);
            input.setInputType (InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams (
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams (lp);
            input.setText (frm_job_issue_entry.xreel_qty);
            alertDialog.setView (input);
            String finalXbuild_qr_name = xbuild_qr_name;
            alertDialog.setPositiveButton ("OK",
                    new DialogInterface.OnClickListener ( ) {
                        public void onClick(DialogInterface dialog, int which) {
                            String qty = input.getText ( ).toString ( );
                            if (qty.equals ("")) {
                                xreel_for_return = "0";
                            } else {
                                if (Float.parseFloat (qty) > Float.parseFloat (frm_job_issue_entry.xreel_qty)) {
                                    Toast.makeText (getApplicationContext ( ), "Return Qty Should Not Be Greater Than Reel Issue Qty!!", Toast.LENGTH_LONG).show ( );
                                    return;
                                }
                                xreel_for_return = qty;
                            }
                            frm_paper_end.txtscan_reel.setText (finalXbuild_qr_name);
                            frm_paper_end.issue_req_model = new issue_req_model ("1", "" + finalXbuild_qr_name, xreel_for_return, "" + rawResult.trim ( ));
                            fillGrid ( );
                            dialog.dismiss ( );
                        }
                    });
            alertDialog.show ( );
            return false;
        } else {
            frm_paper_end.issue_req_model = new issue_req_model ("1", "" + xbuild_qr_name, frm_job_issue_entry.xreel_qty, "" + rawResult.trim ( ));
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ( );
        fgen.frm_request = "";
        fgen.xextra1 = "";
    }

    private void newBtnClickedMethod() {
        txtscan_job.setText ("");
        txtscan_reel.setText ("");
    }

    private void disableViewsMethod() {
        btn_save.setEnabled (false);
        btnscan_reel.setEnabled (false);
        btnscan_job.setEnabled (false);
        txtscan_job.setEnabled (false);
        radio_scan_reel.setEnabled (false);
        radio_scan_job.setEnabled (false);
    }

    private void setTextOnViews() {
        btn_cancel.setText ("EXIT");
    }

    private void initializeViews() {
        toolbar = findViewById (R.id.toolbar);
        btn_add = findViewById (R.id.btn_add);
        btn_read = findViewById (R.id.btn_read);
        btn_save = findViewById (R.id.btn_save);
        btn_new = findViewById (R.id.btn_new);
        btn_cancel = findViewById (R.id.btn_cancel);
        btn_print = findViewById (R.id.btn_print);
        btnscan_job = findViewById (R.id.btn_scanjob);
        btnscan_reel = findViewById (R.id.btn_scanreel);
        txtscan_job = findViewById (R.id.txt_job);
        txtscan_reel = findViewById (R.id.txt_reel);
        recyclerView = findViewById (R.id.recycler);
        txt_barcode = findViewById (R.id.txt_barcode);
        txtwip_stage = findViewById (R.id.txtwip_stage);
        radio_scan_job = findViewById (R.id.radio_scan_job);
        radio_scan_reel = findViewById (R.id.radio_scan_reel);
    }

    @Override
    protected void onResume() {
        super.onResume ( );
        try {
            list = handler.issue_req_get_data ( );
        } catch (Exception e) {
        }
        adapter = new issue_slip_adapter (frm_paper_end.this, list);
        recyclerView.setAdapter (adapter);
    }


    public void ShowDialogBox(Context context, ArrayList<String> list, String controll) {
        final Dialog dialog = new Dialog (context);
        dialog.setContentView (R.layout.search_lead);
        DisplayMetrics displayMetrics = new DisplayMetrics ( );
        getWindowManager ( ).getDefaultDisplay ( ).getMetrics (displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams ( );
        layoutParams.copyFrom (dialog.getWindow ( ).getAttributes ( ));
        int dialogWindowWidth = (int) (displayWidth * 1.0f);
        int dialogWindowHeight = (int) (displayHeight * 1.0f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow ( ).setLayout (layoutParams.width, layoutParams.height);
        dialog.show ( );

        EditText search_text = dialog.findViewById (R.id.txtsearch);


        ListView search_list = dialog.findViewById (R.id.seach_list);
        ImageView close_window = dialog.findViewById (R.id.close_window);

        TextView txtapi_code = dialog.findViewById (R.id.txtapi_code);
        if (controll.equals ("txtscan_job")) txtapi_code.setText ("EP835");
        if (controll.equals ("txtwip_stage")) txtapi_code.setText ("EP820");

        close_window.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                dialog.dismiss ( );
            }
        });


//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
//        search_list.setAdapter(adapter);

        final SearchListAdapter adapter = new SearchListAdapter (context, list);
        search_list.setAdapter (adapter);


        search_text.addTextChangedListener (new TextWatcher ( ) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter ( ).filter (s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search_list.setOnItemClickListener (new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String list_selected_text = adapter.getItem (position).toString ( );
                switch (controll) {
                    case "txtscan_job":
                        txtscan_job.setText (list_selected_text);
                        break;
                    case "txtwip_stage":
                        txtwip_stage.setText (list_selected_text);
                        break;
                    default:
                        break;
                }
                dialog.dismiss ( );
            }
        });

    }


    public void scanned_job_verification(String rawResult) {
        ArrayList<team6> result = finapi.getApi6 (fgen.mcd, "ajobc_check", "" + rawResult.trim ( ), fgen.muname, fgen.cdt1.substring (6, 10), "#", "#");
        Gson gson = new Gson ( );
        String listString = gson.toJson (result, new TypeToken<ArrayList<team>> ( ) {
        }.getType ( ));
        String data = "";
        try {
            JSONArray array = new JSONArray (listString);
            for (int i = 0; i < array.length ( ); i++) {
                JSONObject respObj = array.getJSONObject (i);
                data = respObj.getString ("col1");
                try {
                } catch (Exception e) {
                }
            }
        } catch (JSONException e) {
            e.printStackTrace ( );
        }
        if (!data.equals ("Success")) {
            new android.app.AlertDialog.Builder (this)
                    .setTitle ("Error")
                    .setMessage ("Sorry, JOB Card Is Invalid!!")
                    .setPositiveButton ("Ok", new DialogInterface.OnClickListener ( ) {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss ( );
                            return;
                        }
                    })
                    .show ( );
            return;
        } else {
            try {
                frm_paper_end.txtscan_job.setText ("" + rawResult.trim ( ));
            } catch (Exception e) {
            }
            try {
                frm_reel_issue_return.txtscan_job.setText ("" + rawResult.trim ( ));
            } catch (Exception e) {
            }
            try {
                frm_job_issue_entry.txtscan_job.setText ("" + rawResult.trim ( ));
            } catch (Exception e) {
            }
        }
    }


    public void InitPrinter() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter ( );
        try {
            if (!bluetoothAdapter.isEnabled ( )) {
                Intent enableBluetooth = new Intent (BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult (enableBluetooth, 0);
            }

            if (ActivityCompat.checkSelfPermission (this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices ( );

            if(pairedDevices.size() > 0)
            {
                for(BluetoothDevice device : pairedDevices)
                {
                    if(device.getName().equals("SP200")) //Note, you will need to change this to match the name of your device
                    {
                        bluetoothDevice = device;
                        break;
                    }
                }

                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
                Method m = bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                socket = (BluetoothSocket) m.invoke(bluetoothDevice, 1);
                bluetoothAdapter.cancelDiscovery();
                socket.connect();
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                beginListenForData();
            }
            else
            {
                value+="No Devices found";
                Toast.makeText(this, value, Toast.LENGTH_LONG).show();
                return;
            }
        }
        catch(Exception ex)
        {
            value+=ex.toString()+ "\n" +" InitPrinter \n";
            Toast.makeText(this, value, Toast.LENGTH_LONG).show();
        }
    }

    public void beginListenForData()
    {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = inputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                inputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                Log.d("e", data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}