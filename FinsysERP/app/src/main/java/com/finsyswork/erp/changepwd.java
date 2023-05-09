package com.finsyswork.erp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class changepwd extends AppCompatActivity {

    EditText txtoldpwd, txtnewpwd, txtconfirmpwd;
    Button btnchangepwd;
    ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);
        bar = getSupportActionBar();
        bar.setTitle("Change Password");

        txtoldpwd = (EditText) findViewById(R.id.txtoldpwd);
        txtnewpwd = (EditText) findViewById(R.id.txtNewPwd);
        txtconfirmpwd = (EditText) findViewById(R.id.txtConfirmPwd);
        btnchangepwd = (Button) findViewById(R.id.btnchangepwd);
        finapi.context = changepwd.this;

        pageload();
    }

    void pageload() {
        btnchangepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtoldpwd.getText().length() <= 0) {
                    alertbox("Old Password Not Entered", "Please Enter Old Password to proceed");
                    return;
                }
                if (txtnewpwd.getText().length() <= 0) {
                    alertbox("New Password Not Entered", "Please Enter New Password to proceed");
                    return;
                }
                if (txtconfirmpwd.getText().length() <= 0) {
                    alertbox("Confirm Password Not Entered", "Please Enter Confirm Password to proceed");
                    return;
                }
                if (txtconfirmpwd.getText().length() > 0 && txtnewpwd.getText().length() > 0) {
                    if (txtconfirmpwd.getText().toString().trim().toUpperCase().equals(txtnewpwd.getText().toString().trim().toUpperCase())) {
                        String oldPwd = finapi.seekiname(fgen.mcd, "SELECT UPPER(TRIM(LEVEL3PW)) AS PWD FROM EVAS WHERE USERNAME='" + fgen.muname + "'", "pwd");
                        if (oldPwd.equals(txtoldpwd.getText().toString().toUpperCase().trim())) {
                            finapi.executecmd(fgen.mcd, "UPDATE EVAS SET LEVEL3PW='" + txtnewpwd.getText().toString().toUpperCase().trim() + "' WHERE TRIM(USERNAME)='" + fgen.muname + "'");
                            alertbox2("Password has been Changed!!", "Password has been Changed Successfully");
                        } else {
                            alertbox("Password Not Matched!!", "Old Password not matched!!");
                            return;
                        }
                    } else {
                        alertbox(   "Password Not Matched!!", "New Password and Confirm Password not matched!!");
                        return;
                    }
                }
            }
        });
    }

    public void alertbox(String title, String mymessage) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(changepwd.this);
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

    public void alertbox2(String title, String mymessage) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(changepwd.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(Html.fromHtml(mymessage));
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedpreferences = getSharedPreferences("mypref",
                                Context.MODE_PRIVATE);
                        sharedpreferences.edit().clear().commit();
                        Intent intent = new Intent(changepwd.this, Login.class);
                        startActivityForResult(intent, 1);
                        finish();
                    }
                });
        if (!isFinishing()) {
            alertDialog.show();
        }
    }
}
