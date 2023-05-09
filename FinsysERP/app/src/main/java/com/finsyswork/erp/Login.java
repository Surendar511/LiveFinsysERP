package com.finsyswork.erp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.widget.Toast.LENGTH_LONG;

public class Login extends AppCompatActivity {

    public static EditText _emailText;
    public static EditText _passwordText;
    public static Button _loginButton, btn_scan;
    public static EditText _CocdText;
    TextView lblversion;
    TextView _lblpass;
    TextView _lblsrv, txtclear_cache;
    String myEvent = "";
    private static final int PERMISSION_REQUEST_CODE = 200;
    String mq = "", lblcocd = "", lbluser = "", lblpass = "", android_version="3", latest_android_version="";
    SharedPreferences sharedpreferences;
    private static final String TAG = "Appr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        finapi.setColorOfStatusBar(Login.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        fgen.Context = getApplicationContext();
        lblversion = (TextView) findViewById(R.id.lblversion);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        _CocdText = (EditText) findViewById(R.id.input_cocd);
        _lblpass = (TextView) findViewById(R.id.link_pass);
        _lblsrv = (TextView) findViewById(R.id.link_server);
        _lblsrv = (TextView) findViewById(R.id.link_server);
        txtclear_cache = (TextView) findViewById(R.id.cache_clear);

        finapi.context = Login.this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


        CheckVersion();

        requestPermission();
        fgen.Serverip = fgen.readFileByShivam();
//        fgen.Serverip = fgen.readfile(fgen.file_Serverinfo);

        txtclear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_cache_method();
            }
        });

        lblversion.setText("Version 12 dated 04/04/2023 12:30");

        try{
//            mq = fgen.readfile("CO");
            mq = fgen.readFileByShivam();
            if (mq.length() <= 4 && mq.length() > 0) _CocdText.setText(mq);
        }catch (Exception e) {
        }
        sharedpreferences = getSharedPreferences(fgen.mypreferencegroup, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(fgen.sp_mymcd)) {
            _CocdText.setText(sharedpreferences.getString(fgen.sp_mymcd, ""));
            lblcocd = sharedpreferences.getString(fgen.sp_mymcd, "");
        }

        if (sharedpreferences.contains(fgen.sp_myuname)) {
            _emailText.setText(sharedpreferences.getString(fgen.sp_myuname, ""));
            lbluser = sharedpreferences.getString(fgen.sp_myuname, "");
        }
        if (sharedpreferences.contains(fgen.sp_mypass)) {
            _passwordText.setText(sharedpreferences.getString(fgen.sp_mypass, ""));
            lblpass = sharedpreferences.getString(fgen.sp_mypass, "");
        }
        pageload_fun();
    }

    private void CheckVersion() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://finsysappversioncontroller-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference("AppVersion");


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        myRef.setValue("Hello, World!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgen.frm_request = "";
    }

    private void clear_cache_method() {
        deleteCache(this);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Success");
        builder1.setMessage("Cache Cleared Successfully..");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        startActivity(getIntent());

                        overridePendingTransition(0, 0);
                        String time = System.currentTimeMillis() + "";
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (checkPermission()) return;
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

        Dexter.withContext(Login.this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if(multiplePermissionsReport.isAnyPermissionPermanentlyDenied())
                            {
                                startActivity(new Intent(getApplicationContext(), Login.class));
                            }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void pageload_fun() {
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_CocdText.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Company Code!!", LENGTH_LONG).show();
                    return;
                }
                fgen.frm_request = "Login";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
            }
        });


        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEvent = "login";
               // fgen.FileServerip = "mis.finsys.biz";
                load();
            }
        });
        _lblsrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
        _lblpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _lblpass.setEnabled(false);
                String smtp_host = "mail.finsys.biz";
                int port = 587;
                Boolean ssl = false;
                String sender = "app@finsys.biz";
                String Pass = "misapp";
                String cc = "";
                String bcc = "";
                String sender_name = "Finsys ERP";
                String subject = "(From : " + sender_name + ") Password Recovery Mail";
                String xmail_body = "";
                String email = "";
                String tpass = "";
                xmail_body = xmail_body + "<html><body style='font-family: Arial, Helvetica, sans-serif; font-weight: 700; font-size: 13px; font-style: italic; color: #474646'>";

                xmail_body = xmail_body + "Hello " + fgen.muname + ",<br/><br/>";

                fgen.muname = _emailText.getText().toString().toUpperCase().trim();
                fgen.muserpwd = _passwordText.getText().toString().toUpperCase().trim();
                mq = finapi.seekiname(_CocdText.getText().toString().toUpperCase().trim(), "select trim(level3pw)||'" + fgen.textseprator + "'||trim(emailid) as pass from  EVAS where trim(username)='" + fgen.muname + "'", "pass");
                try {
                    tpass = mq.split(fgen.textseprator)[0].trim();
                    if (email.trim().length() < 4) {
                        email = mq.split(fgen.textseprator)[1];
                    }
                } catch (Exception e) {
                    alertbox("Finsys ERP Alert", "Email ID Not Available");
                    return;
                }

                xmail_body = xmail_body + " Your Password For login ID :- " + fgen.muname + " is : <b>" + tpass + "</b>";
                xmail_body = xmail_body + "<br><br><br>";
                xmail_body = xmail_body + "Thanks & Regards,<br>";
                xmail_body = xmail_body + sender_name + "<br>";
                xmail_body = xmail_body + "<br>===========================================================";
                xmail_body = xmail_body + "</body></html>";

                String mq2 = finapi.SendEmail1(sender_name, email, cc, bcc, smtp_host, sender, sender_name, Pass, port, ssl, subject, xmail_body);

                if (mq2.trim().equals("Mail Sent Successfully")) {
                    alertbox("Finsys ERP", "Mail Sent to your Register Email Id Successfully");
                    mq2 = "";
                    return;
                } else {
                    alertbox("Finsys ERP", "Recovery Failed.Please Contact to Administrator");
                }
                mq2 = "";
                _lblpass.setEnabled(true);
            }
        });

        Handler handler_thread = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler_thread.post(new Runnable() { // This thread runs in the UI
                    @Override
                    public void run() {
                        fgen.x41 = finapi.seekiname(fgen.mcd, "SELECT PARAMS AS COL1 FROM CONTROLS WHERE ID='X41'", "COL1");
                        if(fgen.x41.equals("")) {finapi.seekiname(fgen.mcd, "SELECT PARAMS AS COL1 FROM CONTROLS WHERE ID='X41'", "COL1");}

                        // On complete call either onLoginSuccess or onLoginFailed
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

     void load() {
        Log.d(TAG, "Login");
        final MyProgressdialog progressDialog = new MyProgressdialog(Login.this);
        progressDialog.show();
            // TODO: Implement your own authentication logic here.
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (myEvent.equals("login")) login();
                        // On complete call either onLoginSuccess or onLoginFailed
                        progressDialog.dismiss();
                    }
                }, 100);
    }

   void login() {
       String cocd = _CocdText.getText().toString().toUpperCase().trim();
       fgen.mcd = cocd;
       CheckUserApiHosting(fgen.mcd);
       //fgen.Serverip = "app3.finsys.biz";
       if (fgen.Serverip.length() <= 2 || fgen.Serverip == null) {
           if(fgen.mcd.equals("SGRP")){
               fgen.Serverip = "sgtest.finsys.biz";
           }
           else {
               fgen.Serverip = "app3.finsys.biz";
           }
       }

       String mhd = finapi.seekiname(fgen.mcd, "Select Enable_yn AS COL1 froM CONTROLS WHERE ID='X60'", "COL1");
       if (mhd.equals("Y")) {
           mhd = finapi.seekiname(fgen.mcd, "Select trim(averchk) as averchk from co where CODE LIKE '" + fgen.mcd + "%'", "averchk");
           if (fgen.make_double(android_version) > fgen.make_double(mhd)) {
               finapi.executecmd(fgen.mcd, "UPDATE CO SET AVERCHK='" + android_version + "' WHERE CODE LIKE '" + fgen.mcd + "%' ");
           }
           if (fgen.make_double(mhd) > fgen.make_double(android_version)) {
               alertbox("Version Issue Found!", "You are using Old Version, Please Update New Android Version App. Latest Version is " + mhd);
               return ;
           }
       }

       String macAddress = "";
       if (!fgen.mcd.equals("MVIN")) macAddress = fgen.getmac(Login.this);
       macAddress = "";
       fgen.muname = _emailText.getText().toString().toUpperCase().trim();
       fgen.muserpwd = _passwordText.getText().toString().toUpperCase().trim();
       ArrayList<team6> dt = finapi.getApi6(_CocdText.getText().toString().toUpperCase().trim(), "loginAndwork", fgen.muname, fgen.muserpwd, macAddress, "", "");
        if (dt.size() > 0) {
            if (dt.get(0).getcol1().trim().toUpperCase().equals("OK")) {
                fgen.muid = dt.get(0).getcol2();
                fgen.emailid = dt.get(0).getcol4();
                try {
                    fgen.contactno = dt.get(0).getcol5().split("!~!~!")[0].trim();
                } catch (Exception e) {
                    fgen.contactno = "-";
                }
                try {
                    fgen.department = dt.get(0).getcol5().split("!~!~!")[1].trim();
                } catch (Exception e) {
                    fgen.department = "-";
                }
                try {
                    fgen.designation = dt.get(0).getcol5().split("!~!~!")[2].trim();
                } catch (Exception e) {
                    fgen.designation = "-";
                }
                try {
                    fgen.branchcd = dt.get(0).getcol6().split("!~!~!")[0].trim();
                } catch (Exception e) {
                    fgen.branchcd = "-";
                }
                String myCocd = fgen.mcd;
                if (fgen.mcd.equals("MCPL2") || fgen.mcd.equals("MCPL4")) myCocd = "MCPL";

                String mydate = finapi.seekiname(fgen.mcd, "SELECT TO_CHAR(SYSDATE,'DD/MM/YYYY') AS COL1 FROM DUAL ", "COL1");
                String myear = finapi.seekiname(fgen.mcd, "Select to_Char(fmdate,'yyyy') as fmdt from co where code like '" + myCocd + "%' and FMDATE<=TO_DATE('" + mydate + "','DD/MM/YYYY') AND TODATE>=TO_DATE('" + mydate + "','DD/MM/YYYY') order by fmdate desc", "fmdt");
                String squery = "Select max(code) as col1, max(TO_CHAR(fmdate,'DD/MM/YYYY'))  AS col2,max(to_char(todate,'DD/MM/YYYY'))  as col3,'-' as col4,'-' as  col5 from co where TRIM(CODE) LIKE '" + myCocd + myear + "%'";
                ArrayList<team> dt2 = finapi.getData_login(fgen.mcd, squery);
                if (dt2.size() > 0) {
                    try {
                        ArrayList<team> dt3 = finapi.getData_login(fgen.mcd, "select TRIM(MAIL_FLD1) as col1,TRIM(MAIL_FLD2) as col2,TRIM(MAIL_FLD3) as col3,TRIM(MAIL_FLD4) as col4, '-' as col5 from type where id='B'");
                        Utils.FromEmail = dt3.get(0).getcol1();
                        Utils.FromEmailPassword = dt3.get(0).getcol2();
                        Utils.SMTPHOST = dt3.get(0).getcol3();
                        Utils.EmailPortNo = dt3.get(0).getcol4();
                    }catch (Exception e){}
                    fgen.cdt1 = dt2.get(0).getcol2();
                    fgen.cdt2 = dt2.get(0).getcol3();

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(fgen.sp_mbr, "00");
                    editor.putString(fgen.sp_mymcd, _CocdText.getText().toString().toUpperCase());
                    editor.putString(fgen.sp_myuname, _emailText.getText().toString().toUpperCase());
                    editor.putString(fgen.sp_mypass, _passwordText.getText().toString().toUpperCase());
                    editor.commit();
                    //startActivity(new Intent(Login.this, MainActivity.class));
                    startActivity(new Intent(Login.this, frm_select.class));
                }
            } else {
                alertbox("", dt.get(0).getcol1().trim());
            }
        } else {
            alertbox("Wrong User Name and Password", "Please Check Entered Username and password and try again.");
        }
    }

    private void CheckUserApiHosting(String mcd){
        switch(mcd.trim().toUpperCase()){
            case "CHPL":
                finapi.urlextra = "/FinWebApisv.svc/";
                break;
            default:
                finapi.urlextra = "/finapi/FinWebApisv.svc/";
                break;
        }
    }

    public void alertbox(String title, String mymessage) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(Html.fromHtml(mymessage));
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if (!isFinishing()) {
            alertDialog.show();
        }
    }

    protected void showInputDialog() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Login.this);
        View promptView = layoutInflater.inflate(R.layout.input_value, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
        alertDialogBuilder.setView(promptView);

        final EditText editip = (EditText) promptView.findViewById(R.id.edittext);
        try {
//            editip.setText(fgen.readfile(fgen.file_Serverinfo).trim());
            editip.setText(fgen.readFileByShivam().trim());

        } catch (Exception e) {
            Toast.makeText(Login.this, e.toString(), LENGTH_LONG).show();
            editip.setText("-");
        }
        //   final EditText editport = (EditText) promptView.findViewById(R.id.edittext1);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        fgen.writefile(fgen.file_Serverinfo, editip.getText().toString().trim());
                        fgen.Serverip = editip.getText().toString().trim();
                        fgen.writeFileByShivam();
//                        if (fgen.writefile(fgen.file_Serverinfo, editip.getText().toString().trim())) {
//                            _lblsrv.setText("Click to Change Server Info");
//                            Toast.makeText(getApplicationContext(), "Information saved", Toast.LENGTH_SHORT).show();
//                        }
                         if (fgen.writeFileByShivam()) {
                            _lblsrv.setText("Click to Change Server Info");
                            Toast.makeText(getApplicationContext(), "Information saved", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "I/O error", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
