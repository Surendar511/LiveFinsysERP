package com.finsyswork.erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;


import java.util.ArrayList;

public class DemoActivity extends AppCompatActivity {

    Button btnRegister;
    RadioButton radio_mail, radio_female;
    EditText txtfull_name, txtuser_name, txtemail, txtpassword;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_demo);

        initailiseViews();
        setSupportActionBar(toolbar);

        btnRegister.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                String full_name = txtfull_name.getText().toString ();
                String user_name = txtuser_name.getText().toString ();
                String email = txtemail.getText().toString ();
                String password = txtpassword.getText().toString ();
                String gender = "M";
                if(radio_female.isChecked ())
                    gender = "F";

                String post_param =  full_name + "!~!~!" + user_name  + "!~!~!" + email + "!~!~!" +password + "!~!~!" + gender ;

                ArrayList<team> result = finapi.getApi(fgen.mcd, "Register", ""+post_param, fgen.muname, fgen.cdt1.substring(6, 10), "-", "-");
                boolean msg = finapi.showAlertMessageForStockCheck(DemoActivity.this, result);
                if(!msg)
                {
                    return;
                }

                txtfull_name.setText ("");
                txtuser_name.setText ("");
                txtemail.setText ("");
                txtpassword.setText ("");
                radio_mail.setChecked (true);
            }
        });

    }

    private void initailiseViews() {
        btnRegister = findViewById (R.id.button);
        radio_mail = findViewById (R.id.radioButton);
        radio_female = findViewById (R.id.radioButton2);
        txtfull_name = findViewById (R.id.editTextTextPersonName);
        txtuser_name = findViewById (R.id.editTextTextPersonName2);
        txtemail = findViewById (R.id.editTextTextEmailAddress);
        txtpassword = findViewById (R.id.editTextTextPassword);
        toolbar = findViewById (R.id.toolbar2);
    }


}