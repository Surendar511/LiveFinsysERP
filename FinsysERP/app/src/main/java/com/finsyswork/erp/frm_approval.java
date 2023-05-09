package com.finsyswork.erp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class frm_approval extends AppCompatActivity {
    private static final String TAG = "Appr";
    public ArrayList<team6> feedList = new ArrayList<>();
    public ArrayList<team6> feedListmain = new ArrayList<>();
    CustomAdapter adapter;
    public static ListView lv;
    CheckBox chk1rpt;
    TextView col1rpt, col2rpt, col3rpt, col4rpt, col5rpt, col6rpt, col7rpt, col8rpt, col9rpt, col10rpt;
    TextView col11rpt, col12rpt, col13rpt, col14rpt, col15rpt, col16rpt, col17rpt, col18rpt, col19rpt, col20rpt;
    TextView col21rpt, col22rpt, col23rpt, col24rpt, col25rpt, col26rpt, col27rpt, col28rpt, col29rpt, col30rpt;
    TextView col31rpt, col32rpt, col33rpt, col34rpt, col35rpt, col36rpt, col37rpt, col38rpt, col39rpt, col40rpt;
    TextView col41rpt, col42rpt, col43rpt, col44rpt, col45rpt, col46rpt, col47rpt, col48rpt, col49rpt, col50rpt;
    Button btnappr;
    String squery, mq;
    ActionBar bar;
    private String event = "";
    String mhd = "";
    String[] myValues = new String[50];
    int[] myVisibility = new int[50];
    int[] myWidth = new int[50];
    int clicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_approval);
        bar = getSupportActionBar();
        bar.setTitle("Finsys Reports");
        getSupportActionBar().setTitle(fgen.rptheader);

        getSupportActionBar().setTitle(fgen.rptheader);
        //bar.setBackgroundDrawable(ContextCompat.getDrawable(frm_approval.this, R.drawable.side_nav_bar2));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lv = (ListView) findViewById(R.id.listview1);

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
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void onDestroy() {

        super.onDestroy();

//        if (fgen.senderpage.equals("") || fgen.senderpage.equals("Rptlist")) {
//            fgen.senderpage = "Rptlist";
//            System.gc();
//            finish();
//            return;
//        }
//        String pkg = getPackageName().toString().trim();
//        String mstr = pkg + "." + fgen.senderpage;
//        Intent intent = new Intent();
//        ComponentName cName = new ComponentName(pkg, mstr);
//        intent.setComponent(cName);
//        startActivity(intent);

    }

    public class CustomAdapter extends ArrayAdapter<team6> implements Filterable {
        boolean[] checkBoxState;
        CustomAdapter.ViewHolder viewHolder;
        private Context context;
        private ArrayList<team6> feedListadp;
        private int resid;

        public CustomAdapter(Context context, int textViewResourceId,
                             ArrayList<team6> feedList) {
            super(context, textViewResourceId, feedList);
            this.context = context;
            this.feedListadp = new ArrayList<team6>();
            this.feedListadp.addAll(feedList);
            checkBoxState = new boolean[feedList.size()];
            this.resid = textViewResourceId;
        }

        public class ViewHolder {
            //ImageView photo;
            TextView col1, col2, col3, col4, col5, col6, col7, col8, col9, col10;
            TextView col11, col12, col13, col14, col15, col16, col17, col18, col19, col20;
            TextView col21, col22, col23, col24, col25, col26, col27, col28, col29, col30;
            TextView col31, col32, col33, col34, col35, col36, col37, col38, col39, col40;
            TextView col41, col42, col43, col44, col45, col46, col47, col48, col49, col50;
            HorizontalScrollView horizontalScrollView1;
            CheckBox checkBox;
        }

        @SuppressLint("WrongConstant")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            @SuppressLint("WrongConstant") LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(resid, null);
                viewHolder = new CustomAdapter.ViewHolder();
                viewHolder.horizontalScrollView1 = (HorizontalScrollView) convertView.findViewById(R.id.horizontalScrollView1);
                viewHolder.col1 = (TextView) convertView.findViewById(R.id.tvcol1);
                viewHolder.col2 = (TextView) convertView.findViewById(R.id.tvcol2);
                viewHolder.col3 = (TextView) convertView.findViewById(R.id.tvcol3);
                viewHolder.col4 = (TextView) convertView.findViewById(R.id.tvcol4);
                viewHolder.col5 = (TextView) convertView.findViewById(R.id.tvcol5);
                viewHolder.col6 = (TextView) convertView.findViewById(R.id.tvcol6);
                viewHolder.col7 = (TextView) convertView.findViewById(R.id.tvcol7);
                viewHolder.col8 = (TextView) convertView.findViewById(R.id.tvcol8);
                viewHolder.col9 = (TextView) convertView.findViewById(R.id.tvcol9);
                viewHolder.col10 = (TextView) convertView.findViewById(R.id.tvcol10);
                viewHolder.col11 = (TextView) convertView.findViewById(R.id.tvcol11);
                viewHolder.col12 = (TextView) convertView.findViewById(R.id.tvcol12);
                viewHolder.col13 = (TextView) convertView.findViewById(R.id.tvcol13);
                viewHolder.col14 = (TextView) convertView.findViewById(R.id.tvcol14);
                viewHolder.col15 = (TextView) convertView.findViewById(R.id.tvcol15);
                viewHolder.col16 = (TextView) convertView.findViewById(R.id.tvcol16);
                viewHolder.col17 = (TextView) convertView.findViewById(R.id.tvcol17);
                viewHolder.col18 = (TextView) convertView.findViewById(R.id.tvcol18);
                viewHolder.col19 = (TextView) convertView.findViewById(R.id.tvcol19);
                viewHolder.col20 = (TextView) convertView.findViewById(R.id.tvcol20);
                viewHolder.col21 = (TextView) convertView.findViewById(R.id.tvcol21);
                viewHolder.col22 = (TextView) convertView.findViewById(R.id.tvcol22);
                viewHolder.col23 = (TextView) convertView.findViewById(R.id.tvcol23);
                viewHolder.col24 = (TextView) convertView.findViewById(R.id.tvcol24);
                viewHolder.col25 = (TextView) convertView.findViewById(R.id.tvcol25);
                viewHolder.col26 = (TextView) convertView.findViewById(R.id.tvcol26);
                viewHolder.col27 = (TextView) convertView.findViewById(R.id.tvcol27);
                viewHolder.col28 = (TextView) convertView.findViewById(R.id.tvcol28);
                viewHolder.col29 = (TextView) convertView.findViewById(R.id.tvcol29);
                viewHolder.col30 = (TextView) convertView.findViewById(R.id.tvcol30);
                viewHolder.col31 = (TextView) convertView.findViewById(R.id.tvcol31);
                viewHolder.col32 = (TextView) convertView.findViewById(R.id.tvcol32);
                viewHolder.col33 = (TextView) convertView.findViewById(R.id.tvcol33);
                viewHolder.col34 = (TextView) convertView.findViewById(R.id.tvcol34);
                viewHolder.col35 = (TextView) convertView.findViewById(R.id.tvcol35);
                viewHolder.col36 = (TextView) convertView.findViewById(R.id.tvcol36);
                viewHolder.col37 = (TextView) convertView.findViewById(R.id.tvcol37);
                viewHolder.col38 = (TextView) convertView.findViewById(R.id.tvcol38);
                viewHolder.col39 = (TextView) convertView.findViewById(R.id.tvcol39);
                viewHolder.col40 = (TextView) convertView.findViewById(R.id.tvcol40);
                viewHolder.col41 = (TextView) convertView.findViewById(R.id.tvcol41);
                viewHolder.col42 = (TextView) convertView.findViewById(R.id.tvcol42);
                viewHolder.col43 = (TextView) convertView.findViewById(R.id.tvcol43);
                viewHolder.col44 = (TextView) convertView.findViewById(R.id.tvcol44);
                viewHolder.col45 = (TextView) convertView.findViewById(R.id.tvcol45);
                viewHolder.col46 = (TextView) convertView.findViewById(R.id.tvcol46);
                viewHolder.col47 = (TextView) convertView.findViewById(R.id.tvcol47);
                viewHolder.col48 = (TextView) convertView.findViewById(R.id.tvcol48);
                viewHolder.col49 = (TextView) convertView.findViewById(R.id.tvcol49);
                viewHolder.col50 = (TextView) convertView.findViewById(R.id.tvcol50);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.chk1);

                //NEW CHANGES ON 28092017
                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                        feedListadp.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                    }
                });
                //END CHANGES

                viewHolder.checkBox.setVisibility(View.GONE);
                convertView.setTag(viewHolder);
                convertView.setTag(R.id.chk1, viewHolder.checkBox);

                convertView.setTag(R.id.tvcol1, viewHolder.col1);
                convertView.setTag(R.id.tvcol2, viewHolder.col2);
                convertView.setTag(R.id.tvcol3, viewHolder.col3);
                convertView.setTag(R.id.tvcol4, viewHolder.col4);
                convertView.setTag(R.id.tvcol5, viewHolder.col5);
                convertView.setTag(R.id.tvcol6, viewHolder.col6);
                convertView.setTag(R.id.tvcol7, viewHolder.col7);
                convertView.setTag(R.id.tvcol8, viewHolder.col8);
                convertView.setTag(R.id.tvcol9, viewHolder.col9);
                convertView.setTag(R.id.tvcol10, viewHolder.col10);
                convertView.setTag(R.id.tvcol11, viewHolder.col11);
                convertView.setTag(R.id.tvcol12, viewHolder.col12);
                convertView.setTag(R.id.tvcol13, viewHolder.col13);
                convertView.setTag(R.id.tvcol14, viewHolder.col14);
                convertView.setTag(R.id.tvcol15, viewHolder.col15);
                convertView.setTag(R.id.tvcol16, viewHolder.col16);
                convertView.setTag(R.id.tvcol17, viewHolder.col17);
                convertView.setTag(R.id.tvcol18, viewHolder.col18);
                convertView.setTag(R.id.tvcol19, viewHolder.col19);
                convertView.setTag(R.id.tvcol20, viewHolder.col20);
                convertView.setTag(R.id.tvcol21, viewHolder.col21);
                convertView.setTag(R.id.tvcol22, viewHolder.col22);
                convertView.setTag(R.id.tvcol23, viewHolder.col23);
                convertView.setTag(R.id.tvcol24, viewHolder.col24);
                convertView.setTag(R.id.tvcol25, viewHolder.col25);
                convertView.setTag(R.id.tvcol26, viewHolder.col26);
                convertView.setTag(R.id.tvcol27, viewHolder.col27);
                convertView.setTag(R.id.tvcol28, viewHolder.col28);
                convertView.setTag(R.id.tvcol29, viewHolder.col29);
                convertView.setTag(R.id.tvcol30, viewHolder.col30);
                convertView.setTag(R.id.tvcol31, viewHolder.col31);
                convertView.setTag(R.id.tvcol32, viewHolder.col32);
                convertView.setTag(R.id.tvcol33, viewHolder.col33);
                convertView.setTag(R.id.tvcol34, viewHolder.col34);
                convertView.setTag(R.id.tvcol35, viewHolder.col35);
                convertView.setTag(R.id.tvcol36, viewHolder.col36);
                convertView.setTag(R.id.tvcol37, viewHolder.col37);
                convertView.setTag(R.id.tvcol38, viewHolder.col38);
                convertView.setTag(R.id.tvcol39, viewHolder.col39);
                convertView.setTag(R.id.tvcol40, viewHolder.col40);
                convertView.setTag(R.id.tvcol41, viewHolder.col41);
                convertView.setTag(R.id.tvcol42, viewHolder.col42);
                convertView.setTag(R.id.tvcol43, viewHolder.col43);
                convertView.setTag(R.id.tvcol44, viewHolder.col44);
                convertView.setTag(R.id.tvcol45, viewHolder.col45);
                convertView.setTag(R.id.tvcol46, viewHolder.col46);
                convertView.setTag(R.id.tvcol47, viewHolder.col47);
                convertView.setTag(R.id.tvcol48, viewHolder.col48);
                convertView.setTag(R.id.tvcol49, viewHolder.col49);
                convertView.setTag(R.id.tvcol50, viewHolder.col50);

                viewHolder.checkBox.setVisibility(0);

                //chk1rpt.setVisibility(View.INVISIBLE);

            } else
                viewHolder = (CustomAdapter.ViewHolder) convertView.getTag();
            int tsize = feedListadp.size();
            if (position >= tsize) {
            } else {
                final team6 teamobj = feedListadp.get(position);
                int myInd = 0;
                for (String h : teamobj.getcol2().split(fgen.textseprator)) {
                    myValues[myInd] = h;
                    myVisibility[myInd] = 0;
                    myInd++;
                }

                // set width of col
                viewHolder.col1.setWidth(myWidth[0]);
                viewHolder.col2.setWidth(myWidth[1]);
                viewHolder.col3.setWidth(myWidth[2]);
                viewHolder.col4.setWidth(myWidth[3]);
                viewHolder.col5.setWidth(myWidth[4]);
                viewHolder.col6.setWidth(myWidth[5]);
                viewHolder.col7.setWidth(myWidth[6]);
                viewHolder.col8.setWidth(myWidth[7]);
                viewHolder.col9.setWidth(myWidth[8]);
                viewHolder.col10.setWidth(myWidth[9]);
                viewHolder.col11.setWidth(myWidth[10]);
                viewHolder.col12.setWidth(myWidth[11]);
                viewHolder.col13.setWidth(myWidth[12]);
                viewHolder.col14.setWidth(myWidth[13]);
                viewHolder.col15.setWidth(myWidth[14]);
                viewHolder.col16.setWidth(myWidth[15]);
                viewHolder.col17.setWidth(myWidth[16]);
                viewHolder.col18.setWidth(myWidth[17]);
                viewHolder.col19.setWidth(myWidth[18]);
                viewHolder.col20.setWidth(myWidth[19]);
                viewHolder.col21.setWidth(myWidth[20]);
                viewHolder.col22.setWidth(myWidth[21]);
                viewHolder.col23.setWidth(myWidth[22]);
                viewHolder.col24.setWidth(myWidth[23]);
                viewHolder.col25.setWidth(myWidth[24]);
                viewHolder.col26.setWidth(myWidth[25]);
                viewHolder.col27.setWidth(myWidth[26]);
                viewHolder.col28.setWidth(myWidth[27]);
                viewHolder.col29.setWidth(myWidth[28]);
                viewHolder.col30.setWidth(myWidth[29]);
                viewHolder.col31.setWidth(myWidth[30]);
                viewHolder.col32.setWidth(myWidth[31]);
                viewHolder.col33.setWidth(myWidth[32]);
                viewHolder.col34.setWidth(myWidth[33]);
                viewHolder.col35.setWidth(myWidth[34]);
                viewHolder.col36.setWidth(myWidth[35]);
                viewHolder.col37.setWidth(myWidth[36]);
                viewHolder.col38.setWidth(myWidth[37]);
                viewHolder.col39.setWidth(myWidth[38]);
                viewHolder.col40.setWidth(myWidth[39]);
                viewHolder.col41.setWidth(myWidth[40]);
                viewHolder.col42.setWidth(myWidth[41]);
                viewHolder.col43.setWidth(myWidth[42]);
                viewHolder.col44.setWidth(myWidth[43]);
                viewHolder.col45.setWidth(myWidth[44]);
                viewHolder.col46.setWidth(myWidth[45]);
                viewHolder.col47.setWidth(myWidth[46]);
                viewHolder.col48.setWidth(myWidth[47]);
                viewHolder.col49.setWidth(myWidth[48]);
                viewHolder.col50.setWidth(myWidth[49]);
                // set visibility of col
                viewHolder.col1.setVisibility(myVisibility[0]);
                viewHolder.col2.setVisibility(myVisibility[1]);
                viewHolder.col3.setVisibility(myVisibility[2]);
                viewHolder.col4.setVisibility(myVisibility[3]);
                viewHolder.col5.setVisibility(myVisibility[4]);
                viewHolder.col6.setVisibility(myVisibility[5]);
                viewHolder.col7.setVisibility(myVisibility[6]);
                viewHolder.col8.setVisibility(myVisibility[7]);
                viewHolder.col9.setVisibility(myVisibility[8]);
                viewHolder.col10.setVisibility(myVisibility[9]);
                viewHolder.col11.setVisibility(myVisibility[10]);
                viewHolder.col12.setVisibility(myVisibility[11]);
                viewHolder.col13.setVisibility(myVisibility[12]);
                viewHolder.col14.setVisibility(myVisibility[13]);
                viewHolder.col15.setVisibility(myVisibility[14]);
                viewHolder.col16.setVisibility(myVisibility[15]);
                viewHolder.col17.setVisibility(myVisibility[16]);
                viewHolder.col18.setVisibility(myVisibility[17]);
                viewHolder.col19.setVisibility(myVisibility[18]);
                viewHolder.col20.setVisibility(myVisibility[19]);
                viewHolder.col21.setVisibility(myVisibility[20]);
                viewHolder.col22.setVisibility(myVisibility[21]);
                viewHolder.col23.setVisibility(myVisibility[22]);
                viewHolder.col24.setVisibility(myVisibility[23]);
                viewHolder.col25.setVisibility(myVisibility[24]);
                viewHolder.col26.setVisibility(myVisibility[25]);
                viewHolder.col27.setVisibility(myVisibility[26]);
                viewHolder.col28.setVisibility(myVisibility[27]);
                viewHolder.col29.setVisibility(myVisibility[28]);
                viewHolder.col30.setVisibility(myVisibility[29]);
                viewHolder.col31.setVisibility(myVisibility[30]);
                viewHolder.col32.setVisibility(myVisibility[31]);
                viewHolder.col33.setVisibility(myVisibility[32]);
                viewHolder.col34.setVisibility(myVisibility[33]);
                viewHolder.col35.setVisibility(myVisibility[34]);
                viewHolder.col36.setVisibility(myVisibility[35]);
                viewHolder.col37.setVisibility(myVisibility[36]);
                viewHolder.col38.setVisibility(myVisibility[37]);
                viewHolder.col39.setVisibility(myVisibility[38]);
                viewHolder.col40.setVisibility(myVisibility[39]);
                viewHolder.col41.setVisibility(myVisibility[40]);
                viewHolder.col42.setVisibility(myVisibility[41]);
                viewHolder.col43.setVisibility(myVisibility[42]);
                viewHolder.col44.setVisibility(myVisibility[43]);
                viewHolder.col45.setVisibility(myVisibility[44]);
                viewHolder.col46.setVisibility(myVisibility[45]);
                viewHolder.col47.setVisibility(myVisibility[46]);
                viewHolder.col48.setVisibility(myVisibility[47]);
                viewHolder.col49.setVisibility(myVisibility[48]);
                viewHolder.col50.setVisibility(myVisibility[49]);
                // setting value of texbox
                viewHolder.col1.setText(myValues[0]);
                viewHolder.col2.setText(myValues[1]);
                viewHolder.col3.setText(myValues[2]);
                viewHolder.col4.setText(myValues[3]);
                viewHolder.col5.setText(myValues[4]);
                viewHolder.col6.setText(myValues[5]);
                viewHolder.col7.setText(myValues[6]);
                viewHolder.col8.setText(myValues[7]);
                viewHolder.col9.setText(myValues[8]);
                viewHolder.col10.setText(myValues[9]);
                viewHolder.col11.setText(myValues[10]);
                viewHolder.col12.setText(myValues[11]);
                viewHolder.col13.setText(myValues[12]);
                viewHolder.col14.setText(myValues[13]);
                viewHolder.col15.setText(myValues[14]);
                viewHolder.col16.setText(myValues[15]);
                viewHolder.col17.setText(myValues[16]);
                viewHolder.col18.setText(myValues[17]);
                viewHolder.col19.setText(myValues[18]);
                viewHolder.col20.setText(myValues[19]);
                viewHolder.col21.setText(myValues[20]);
                viewHolder.col22.setText(myValues[21]);
                viewHolder.col23.setText(myValues[22]);
                viewHolder.col24.setText(myValues[23]);
                viewHolder.col25.setText(myValues[24]);
                viewHolder.col26.setText(myValues[25]);
                viewHolder.col27.setText(myValues[26]);
                viewHolder.col28.setText(myValues[27]);
                viewHolder.col29.setText(myValues[28]);
                viewHolder.col30.setText(myValues[29]);
                viewHolder.col31.setText(myValues[30]);
                viewHolder.col32.setText(myValues[31]);
                viewHolder.col33.setText(myValues[32]);
                viewHolder.col34.setText(myValues[33]);
                viewHolder.col35.setText(myValues[34]);
                viewHolder.col36.setText(myValues[35]);
                viewHolder.col37.setText(myValues[36]);
                viewHolder.col38.setText(myValues[37]);
                viewHolder.col39.setText(myValues[38]);
                viewHolder.col40.setText(myValues[39]);
                viewHolder.col41.setText(myValues[40]);
                viewHolder.col42.setText(myValues[41]);
                viewHolder.col43.setText(myValues[42]);
                viewHolder.col44.setText(myValues[43]);
                viewHolder.col45.setText(myValues[44]);
                viewHolder.col46.setText(myValues[45]);
                viewHolder.col47.setText(myValues[46]);
                viewHolder.col48.setText(myValues[47]);
                viewHolder.col49.setText(teamobj.getcol6());
                viewHolder.col50.setText(teamobj.getcol3());
                viewHolder.checkBox.setChecked(teamobj.isSelected());
            }
            viewHolder.col1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked++;
                    if (clicked > 0) {
                        myPrintOut(position);
                        clicked = 0;
                    }
                }
            });
            viewHolder.col2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myclick_adp(position);
                }
            });
            viewHolder.col3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myclick_adp(position);
                }
            });
            viewHolder.col4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myclick_adp(position);
                }
            });
            viewHolder.col5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myclick_adp(position);
                }
            });

            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        team6 tt = feedListadp.get(position);
                        tt.setSelected(isChecked);
                    }
                }
            });
//
            return convertView;
        }


        private void myclick_adp(int position) {
            int pos = position;
            team6 teamobj = feedListadp.get(pos);
            fgen.mq5 = teamobj.getcol5().toString().trim();

            ArrayList<team6> dtHistList = finapi.getApi6(fgen.mcd, "RateHist_api", fgen.btnid, teamobj.getcol3().toString().trim(), "", "", "");
            if (dtHistList.size() > 0) {
                mq = "";
                for (int m = 0; m < dtHistList.size(); m++) {
                    team6 dtHist = dtHistList.get(0);
                    String[] mshHeading = new String[dtHist.getcol1().split(",").length];
                    String[] myCol1 = new String[6];
                    int z = 0;
                    for (String myH : dtHist.getcol1().split(",")) {
                        mshHeading[z] = myH;
                        z++;
                    }
                    myCol1[0] = dtHist.getcol2().trim();
                    myCol1[1] = dtHist.getcol3().trim();
                    myCol1[2] = dtHist.getcol4().trim();
                    myCol1[3] = dtHist.getcol5().trim();
                    myCol1[4] = dtHist.getcol6().trim();

                    mq += mshHeading[0] + " : <b>" + myCol1[0] + "</b><br>";
                    mq += mshHeading[1] + " : <b>" + myCol1[1] + "</b><br>";
                    mq += mshHeading[2] + " : <b>" + myCol1[2] + "</b><br>";
                    mq += mshHeading[3] + " : <b>" + myCol1[3] + "</b><br>";
                    mq += mshHeading[4] + " : <b>" + myCol1[4] + "</b><br><br>";
                }
                alertboxH("Rate History", mq);
            }
        }

        void myPrintOut(int position) {
            int pos = position;
            team6 teamobj = feedListadp.get(pos);
            fgen.mq5 = teamobj.getcol6().toString().trim();
            if (fgen.mq5.length() > 0) {
                try {
                    Uri uri = Uri.parse(fgen.mq5);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {

                }
            }
        }

        public class filter_here extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // TODO Auto-generated method stub

                FilterResults Result = new FilterResults();
                // if constraint is empty return the original names
                if (constraint.length() == 0) {
                    Result.values = feedListmain;
                    Result.count = feedListmain.size();
                    return Result;
                }

                ArrayList<team6> Filtered_Names = new ArrayList<>();
                String filterString = constraint.toString().toLowerCase();
                team6 filterableString;


                for (int i = 0; i < feedListmain.size(); i++) {
                    filterableString = feedListmain.get(i);
                    String temp = filterString.toLowerCase().trim();
                    if (filterableString.getcol1().toString().trim().toLowerCase().contains(temp) ||
                            filterableString.getcol2().toString().trim().toLowerCase().contains(temp) ||
                            filterableString.getcol3().toString().trim().toLowerCase().contains(temp) ||
                            filterableString.getcol4().toString().trim().toLowerCase().contains(temp) ||
                            filterableString.getcol5().toString().trim().toLowerCase().contains(temp)) {
                        Filtered_Names.add(filterableString);
                    }
                }
                Result.values = Filtered_Names;
                Result.count = Filtered_Names.size();

                return Result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // TODO Auto-generated method stub
                feedList = (ArrayList<team6>) results.values;
                //feedList=feedListadp;
                adapter = new CustomAdapter(frm_approval.this, R.layout.view_item_rptlevel50, feedList);
                lv.setAdapter(adapter);
                notifyDataSetChanged();
            }

        }

        @Override
        public Filter getFilter() {
            // TODO Auto-generated method stub
            return new CustomAdapter.filter_here();
        }

    }

    public void alertbox(String title, String mymessage, final String param1) {
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setPositiveButton("Pending", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fgen.mq = "";
                    }
                })
                .setNegativeButton("Completed", new DialogInterface.OnClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();
//						finish();
                        startActivity(intent);

                    }
                })
                .show();


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

    public void alertboxH(String title, String mymessage) {
        new AlertDialog.Builder(this)
                .setMessage(Html.fromHtml(mymessage))
                .setTitle(title)
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                .show();
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

    public void page_Load() {
        Log.d(TAG, "Login");
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_approval.this);
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
        ArrayList<team6> stateList = adapter.feedListadp;
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
        feedList = finapi.getApi6(fgen.mcd, "PendAppr_api", "EP203", fgen.cdt1, fgen.cdt2, fgen.muid, "-");
        bar.setTitle(fgen.rptheader);
        if (feedList.size() > 0) {
            feedListmain = feedList;
            set_colwidth();
            adapter = new CustomAdapter(this, R.layout.view_item_rptlevel50, feedList);
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            alertbox("No Data Exist", "No Data Found for " + fgen.rptheader + " approval");
        }
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
}