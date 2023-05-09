package com.finsyswork.erp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class changeprofile extends AppCompatActivity {

    EditText txtContactNo, txtEmailLId;
    Button btnsave;
    ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeprofile);
        bar = getSupportActionBar();
        bar.setTitle("Edit Profile");

        txtContactNo = (EditText) findViewById(R.id.txtContactNo);
        txtEmailLId = (EditText) findViewById(R.id.txtEmailLId);
        btnsave = (Button) findViewById(R.id.btnsave);
        finapi.context = changeprofile.this;

        pageload();
    }

    void pageload() {
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(changeprofile.this)
                        .setMessage("Do you want to Save the Details ?")
                        .setTitle("Proceed ?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finapi.executecmd(fgen.mcd, "UPDATE EVAS SET EMAILID='" + txtEmailLId.getText().toString().trim() + "', CONTACTNO='" + txtContactNo.getText().toString().trim() + "' WHERE TRIM(USERNAME)='" + fgen.muname + "' ");

                                Intent intent = new Intent(changeprofile.this, MainActivity.class);
                                startActivityForResult(intent, 1);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
    }
}
