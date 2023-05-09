package com.finsyswork.erp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class frm_approve extends AppCompatActivity {

    RecyclerView recyclerView;
    public static ArrayList<team6> feedList = new ArrayList<>();
    public ArrayList<team6> feedListmain = new ArrayList<>();
    public static ArrayList<team6> backup = feedList;
    rptAdapter adapter;
    public static RecyclerView lv;
    CheckBox chk1rpt;
    TextView col1rpt, col2rpt, col3rpt, col4rpt, col5rpt, col6rpt, col7rpt, col8rpt, col9rpt, col10rpt;
    TextView col11rpt, col12rpt, col13rpt, col14rpt, col15rpt, col16rpt, col17rpt, col18rpt, col19rpt, col20rpt;
    TextView col21rpt, col22rpt, col23rpt, col24rpt, col25rpt, col26rpt, col27rpt, col28rpt, col29rpt, col30rpt;
    TextView col31rpt, col32rpt, col33rpt, col34rpt, col35rpt, col36rpt, col37rpt, col38rpt, col39rpt, col40rpt;
    TextView col41rpt, col42rpt, col43rpt, col44rpt, col45rpt, col46rpt, col47rpt, col48rpt, col49rpt, col50rpt;
    Button btnappr;
    String mq;
    ActionBar bar;
    private String event = "";
    int[] myVisibility = new int[50];
    int[] myWidth = new int[50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_approve);

        bar = getSupportActionBar();
        bar.setTitle("Finsys Reports");
        getSupportActionBar().setTitle(fgen.rptheader);
        initializeViews();

        //bar.setBackgroundDrawable(ContextCompat.getDrawable(frm_approval.this, R.drawable.side_nav_bar2));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lv = (RecyclerView) findViewById(R.id.listview1);

        chk1rpt = (CheckBox) findViewById(R.id.chk1rpt);
        col1rpt = (TextView) findViewById(R.id.tvcol1rpt);
        col2rpt = (TextView) findViewById(R.id.tvcol2rpt);
        col3rpt = (TextView) findViewById(R.id.tvcol3rpt);
        col4rpt = (TextView) findViewById(R.id.tvcol4rpt);
        col5rpt = (TextView) findViewById(R.id.tvcol5rpt);
        col6rpt = (TextView) findViewById(R.id.tvcol6rpt);
        col7rpt = (TextView) findViewById(R.id.tvcol7rpt);
        col8rpt = (TextView) findViewById(R.id.tvcol8rpt);
        col9rpt = (TextView) findViewById(R.id.tvcol9rpt);
        col10rpt = (TextView) findViewById(R.id.tvcol10rpt);
        col11rpt = (TextView) findViewById(R.id.tvcol11rpt);
        col12rpt = (TextView) findViewById(R.id.tvcol12rpt);
        col13rpt = (TextView) findViewById(R.id.tvcol13rpt);
        col14rpt = (TextView) findViewById(R.id.tvcol14rpt);
        col15rpt = (TextView) findViewById(R.id.tvcol15rpt);
        col16rpt = (TextView) findViewById(R.id.tvcol16rpt);
        col17rpt = (TextView) findViewById(R.id.tvcol17rpt);
        col18rpt = (TextView) findViewById(R.id.tvcol18rpt);
        col19rpt = (TextView) findViewById(R.id.tvcol19rpt);
        col20rpt = (TextView) findViewById(R.id.tvcol20rpt);
        col21rpt = (TextView) findViewById(R.id.tvcol21rpt);
        col22rpt = (TextView) findViewById(R.id.tvcol22rpt);
        col23rpt = (TextView) findViewById(R.id.tvcol23rpt);
        col24rpt = (TextView) findViewById(R.id.tvcol24rpt);
        col25rpt = (TextView) findViewById(R.id.tvcol25rpt);
        col26rpt = (TextView) findViewById(R.id.tvcol26rpt);
        col27rpt = (TextView) findViewById(R.id.tvcol27rpt);
        col28rpt = (TextView) findViewById(R.id.tvcol28rpt);
        col29rpt = (TextView) findViewById(R.id.tvcol29rpt);
        col30rpt = (TextView) findViewById(R.id.tvcol30rpt);
        col31rpt = (TextView) findViewById(R.id.tvcol31rpt);
        col32rpt = (TextView) findViewById(R.id.tvcol32rpt);
        col33rpt = (TextView) findViewById(R.id.tvcol33rpt);
        col34rpt = (TextView) findViewById(R.id.tvcol34rpt);
        col35rpt = (TextView) findViewById(R.id.tvcol35rpt);
        col36rpt = (TextView) findViewById(R.id.tvcol36rpt);
        col37rpt = (TextView) findViewById(R.id.tvcol37rpt);
        col38rpt = (TextView) findViewById(R.id.tvcol38rpt);
        col39rpt = (TextView) findViewById(R.id.tvcol39rpt);
        col40rpt = (TextView) findViewById(R.id.tvcol40rpt);
        col41rpt = (TextView) findViewById(R.id.tvcol41rpt);
        col42rpt = (TextView) findViewById(R.id.tvcol42rpt);
        col43rpt = (TextView) findViewById(R.id.tvcol43rpt);
        col44rpt = (TextView) findViewById(R.id.tvcol44rpt);
        col45rpt = (TextView) findViewById(R.id.tvcol45rpt);
        col46rpt = (TextView) findViewById(R.id.tvcol46rpt);
        col47rpt = (TextView) findViewById(R.id.tvcol47rpt);
        col48rpt = (TextView) findViewById(R.id.tvcol48rpt);
        col49rpt = (TextView) findViewById(R.id.tvcol49rpt);
        col50rpt = (TextView) findViewById(R.id.tvcol50rpt);

        btnappr = (Button) findViewById(R.id.btnappr);
        feedList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        lv.setLayoutManager(layoutManager);
//        adapter = new rptAdapter(frm_approve.this, feedList);
        adapter = new rptAdapter(frm_approve.this);
        lv.setAdapter(adapter);

        for (int w = 0; w < 50; w++) {
            myVisibility[w] = 4;
        }

        page_Load();
        EditText txtsearch = (EditText) findViewById(R.id.txtsearch);

        txtsearch.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        btnappr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event = "appr";
                page_Load();
            }
        });
        txtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                adapter.getFilter().filter(cs.toString());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void page_Load() {

        final MyProgressdialog progressDialog = new MyProgressdialog(frm_approve.this);
        progressDialog.show();
        // TODO: Implement your own authentication logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (event.equals("")) {
                            showdata();
                        } else if (event.equals("appr")) {
                            approve();
                        }
                        progressDialog.dismiss();
                    }
                }, 100);
    }

    private void approve() {
        ArrayList<team6> stateList = feedList;
        mq = "";
        String mq3 = "";
        for (int i = 0; i < stateList.size(); i++) {
            team6 state = stateList.get(i);
            if (state.isSelected()) {
                mq3 = state.getcol3();
                if (mq.length() > 0)
                    mq = mq + fgen.textseprator + "Y" + mq3;
                else mq = "Y" + mq3;
            }
        }

        if (mq.trim().length() > 1) {
            //mq = mq.substring(0, mq.length() - 1);

            finapi.getApi(fgen.mcd, "ApprAction_api", mq, fgen.btnid, fgen.muname, "");
            alertbox("", "Approval Done Successfully");
        } else {
            alertbox("", "Select Atleast One Row to Approve");
        }
    }

    private void showdata() {
        feedList = finapi.getApi6(fgen.mcd, "PendAppr_api", fgen.btnid, fgen.cdt1, fgen.cdt2, fgen.muname, "-");
        backup = new ArrayList<>(feedList);
        bar.setTitle(fgen.rptheader);
        if (feedList.size() > 0) {
            feedListmain = feedList;
            set_colwidth();
//            adapter = new rptAdapter(frm_approve.this, feedList);
            adapter = new rptAdapter(frm_approve.this);
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            alertbox("No Data Exist", "No Data Found for " + fgen.rptheader + " approval");
        }
    }

    public void set_colwidth() {
        //0 VISIBLE,4 INVISIBLE,8 GONE  FOR VISIBILITYs
        if (feedList.size() > 0) {
            team6 d = feedList.get(0);
            String[] myHName = new String[50];
            int[] myHWidth = new int[50];
            int w = 80;
            int k = 0;
            for (String h1 : d.getcol1().split(",")) {
                myHName[k] = h1;
                w = h1.length() * 10;
                if (w < 80) w = 100;
                if (w > 250) w = 250;
                k++;
            }

            int myInd = 0;
            int myWid = 80;
            for (String h : d.getcol2().split(fgen.textseprator)) {
                myVisibility[myInd] = 0;
                myWid = 20 * h.toString().length();
                if (myWid < 120) myWid = 120;
                if (myWid > 300) myWid = 300;
                myWidth[myInd] = myWid;
                myHWidth[myInd] = myWid + 5;
                myInd++;
            }

            col1rpt.setText(myHName[0]);
            col2rpt.setText(myHName[1]);
            col3rpt.setText(myHName[2]);
            col4rpt.setText(myHName[3]);
            col5rpt.setText(myHName[4]);
            col6rpt.setText(myHName[5]);
            col7rpt.setText(myHName[6]);
            col8rpt.setText(myHName[7]);
            col9rpt.setText(myHName[8]);
            col10rpt.setText(myHName[9]);
            col11rpt.setText(myHName[10]);
            col12rpt.setText(myHName[11]);
            col13rpt.setText(myHName[12]);
            col14rpt.setText(myHName[13]);
            col15rpt.setText(myHName[14]);
            col16rpt.setText(myHName[15]);
            col17rpt.setText(myHName[16]);
            col18rpt.setText(myHName[17]);
            col19rpt.setText(myHName[18]);
            col20rpt.setText(myHName[19]);
            col21rpt.setText(myHName[20]);
            col22rpt.setText(myHName[21]);
            col23rpt.setText(myHName[22]);
            col24rpt.setText(myHName[23]);
            col25rpt.setText(myHName[24]);
            col26rpt.setText(myHName[25]);
            col27rpt.setText(myHName[26]);
            col28rpt.setText(myHName[27]);
            col29rpt.setText(myHName[28]);
            col30rpt.setText(myHName[29]);
            col31rpt.setText(myHName[30]);
            col32rpt.setText(myHName[31]);
            col33rpt.setText(myHName[32]);
            col34rpt.setText(myHName[33]);
            col35rpt.setText(myHName[34]);
            col36rpt.setText(myHName[35]);
            col37rpt.setText(myHName[36]);
            col38rpt.setText(myHName[37]);
            col39rpt.setText(myHName[38]);
            col40rpt.setText(myHName[39]);
            col41rpt.setText(myHName[40]);
            col42rpt.setText(myHName[41]);
            col43rpt.setText(myHName[42]);
            col44rpt.setText(myHName[43]);
            col45rpt.setText(myHName[44]);
            col46rpt.setText(myHName[45]);
            col47rpt.setText(myHName[46]);
            col48rpt.setText(myHName[47]);
            col49rpt.setText(myHName[48]);
            col50rpt.setText(myHName[49]);

            col1rpt.setWidth(myHWidth[0]);
            col2rpt.setWidth(myHWidth[1]);
            col3rpt.setWidth(myHWidth[2]);
            col4rpt.setWidth(myHWidth[3]);
            col5rpt.setWidth(myHWidth[4]);
            col6rpt.setWidth(myHWidth[5]);
            col7rpt.setWidth(myHWidth[6]);
            col8rpt.setWidth(myHWidth[7]);
            col9rpt.setWidth(myHWidth[8]);
            col10rpt.setWidth(myHWidth[9]);
            col11rpt.setWidth(myHWidth[10]);
            col12rpt.setWidth(myHWidth[11]);
            col13rpt.setWidth(myHWidth[12]);
            col14rpt.setWidth(myHWidth[13]);
            col15rpt.setWidth(myHWidth[14]);
            col16rpt.setWidth(myHWidth[15]);
            col17rpt.setWidth(myHWidth[16]);
            col18rpt.setWidth(myHWidth[17]);
            col19rpt.setWidth(myHWidth[18]);
            col20rpt.setWidth(myHWidth[19]);
            col21rpt.setWidth(myHWidth[20]);
            col22rpt.setWidth(myHWidth[21]);
            col23rpt.setWidth(myHWidth[22]);
            col24rpt.setWidth(myHWidth[23]);
            col25rpt.setWidth(myHWidth[24]);
            col26rpt.setWidth(myHWidth[25]);
            col27rpt.setWidth(myHWidth[26]);
            col28rpt.setWidth(myHWidth[27]);
            col29rpt.setWidth(myHWidth[28]);
            col30rpt.setWidth(myHWidth[29]);
            col31rpt.setWidth(myHWidth[30]);
            col32rpt.setWidth(myHWidth[31]);
            col33rpt.setWidth(myHWidth[32]);
            col34rpt.setWidth(myHWidth[33]);
            col35rpt.setWidth(myHWidth[34]);
            col36rpt.setWidth(myHWidth[35]);
            col37rpt.setWidth(myHWidth[36]);
            col38rpt.setWidth(myHWidth[37]);
            col39rpt.setWidth(myHWidth[38]);
            col40rpt.setWidth(myHWidth[39]);
            col41rpt.setWidth(myHWidth[40]);
            col42rpt.setWidth(myHWidth[41]);
            col43rpt.setWidth(myHWidth[42]);
            col44rpt.setWidth(myHWidth[43]);
            col45rpt.setWidth(myHWidth[44]);
            col46rpt.setWidth(myHWidth[45]);
            col47rpt.setWidth(myHWidth[46]);
            col48rpt.setWidth(myHWidth[47]);
            col49rpt.setWidth(myHWidth[48]);
            col50rpt.setWidth(myHWidth[49]);
        }
    }

    public void alertbox(final String title, final String mymessage) {
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (title.toUpperCase().contains("NO ") || mymessage.toUpperCase().contains("SUCCESSFULLY")) {
                                    onBackPressed();
                                    finish();
                                }
                            }
                        })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.backmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.btnBack:
                super.onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler);
    }
}