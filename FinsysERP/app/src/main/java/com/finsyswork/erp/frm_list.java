package com.finsyswork.erp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class frm_list extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public ArrayList<team> feedList;
    Boolean exit = false;
    private String TAG = "frm_list";
    ActionBar bar;
    String[] headarr;
    int widtharr[];
    int columncount = 0, rowcount = 0;
    int drillLevel = 0;
    TableFixHeader sg1;
    BaseTableAdapter baseTableAdapter;
    EditText txt1;
    TextView txtRowsCount, txt_from_dt, txt_to_dt;
    String xClickView = "";
    Button btn_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_list);

        finapi.context = frm_list.this;
        finapi.deleteJsonResponseFile();

        bar = getSupportActionBar();
        bar.setTitle(fgen.rptheader);

        getSupportActionBar().setTitle(fgen.rptheader);
        //bar.setBackgroundDrawable(ContextCompat.getDrawable(frm_list.this, R.drawable.side_nav_bar2));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fgen.currentview = this.getWindow().getDecorView().findViewById(android.R.id.content);


        txt1 = (EditText) findViewById(R.id.txtSrch);
        txt_from_dt = findViewById(R.id.txt_from_dt);
        txt_to_dt = findViewById(R.id.txt_to_dt);
        btn_show = findViewById(R.id.btn_show);
        txtRowsCount = (TextView) findViewById(R.id.txtRcount);
        txt1.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        txt1.addTextChangedListener(watcher);

        page_Load();
    }

    public void page_Load() {
        Log.d(TAG, "Login");

        txt_from_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xClickView = "from_dt";
                showDatePickerDialogue();
            }
        });

        txt_to_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xClickView = "to_dt";
                showDatePickerDialogue();
            }
        });

       btn_show.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showdata();
           }
       });

        final MyProgressdialog progressDialog = new MyProgressdialog(frm_list.this);
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (drillLevel == 0)
                            showdata();
                        progressDialog.dismiss();
                    }
                }, 100);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            columncount = headarr.length;
            rowcount = fgen.feedListresult.size() + 2;
            BaseTableAdapter baseTableAdapter = new FamilyNexusAdapter(frm_list.this, fgen.feedListresult, headarr, widtharr);
            sg1.setAdapter(baseTableAdapter);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            filterteam filterteam = new filterteam(fgen.feedListresult, s.toString());
            filterteam.performFiltering(s);
            columncount = headarr.length;
            rowcount = fgen.feedListresult.size() + 2;
            BaseTableAdapter baseTableAdapter = new FamilyNexusAdapter(frm_list.this, fgen.feedListresult, headarr, widtharr);
            sg1.setAdapter(baseTableAdapter);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void onDestroy() {

        super.onDestroy();

        if (fgen.senderpage.equals("") || fgen.senderpage.equals("Rptlist")) {
            fgen.senderpage = "Rptlist";
            System.gc();
            finish();
            return;
        }
        String pkg = getPackageName().toString().trim();
        String mstr = pkg + "." + fgen.senderpage;
        Intent intent = new Intent();
        ComponentName cName = new ComponentName(pkg, mstr);
        intent.setComponent(cName);
        startActivity(intent);

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
                        String mq = "update scratch set COL1='Y' WHERE BRANCHCD||TYPE||TRIM(VCHNUM)||TO_CHAR(VCHDATE,'DDMMYYYY')='" + param1.toString().trim() + "'";
                        //fgen.mq = wservice_json.execute_transaction(fgen.mcd, mq);
                        if (!fgen.mq.equals("Data Saved")) {
                            Toast.makeText(frm_list.this, "Error in Updating", Toast.LENGTH_LONG).show();
                        }
                        fgen.squery = "select SUBSTR(TRIM(ACODE),1,4) AS COL1,SUBSTR(TRIM(COL9),1,10) AS COL2,SUBSTR(TRIM(REMARKS),1,15) AS COL3,TO_CHAR(VCHDATE,'DD/MM/YYYY') COL4,BRANCHCD||TYPE||TRIM(VCHNUM)||TO_CHAR(VCHDATE,'DDMMYYYY') AS COL5  FROM SCRATCH WHERE TYPE='MT' and NVL(TRIM(COL1),'-')!='Y' ORDER BY  COL5 DESC ";
                        Intent intent = getIntent();
//						finish();
                        startActivity(intent);

                    }
                })
                .show();


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

    public void alertbox(String title, String mymessage) {
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                .show();
    }

    private void showdata() {
        String fromDate = fgen.cdt1;
        String toDate = fgen.cdt2;
        if (!txt_from_dt.getText().toString().isEmpty() && !txt_to_dt.getText().toString().isEmpty()) {
            fromDate = txt_from_dt.getText().toString();
            toDate = txt_to_dt.getText().toString();
        }
        if (drillLevel == 0)
            feedList = finapi.getApi(fgen.mcd, "DataListA_api", fgen.btnid.trim(), fromDate, toDate, fgen.muname, "-");
        if (drillLevel == 1)
            feedList = finapi.getApi(fgen.mcd, "DataListB_api", fgen.btnid.trim(), fromDate, toDate, fgen.hf1col1, "-");
        if (drillLevel == 2)
            feedList = finapi.getApi(fgen.mcd, "DataListC_api", fgen.btnid.trim(), fromDate, toDate, fgen.hf1col2, "-");
        if (drillLevel > 2) {
            alertboxH("No Further Drill Down", "Please Click on Back Button to go back");
            drillLevel--;
        }

        if (drillLevel < 3) {
            if (feedList.size() > 0) {
                fillGrid(feedList);
                bar.setTitle(fgen.rptheader + " (Level - " + (drillLevel + 1) + ")");
            } else {
                alertboxH("Not Data Exist", "No Data Found!!");
            }
        }
    }

    public void fillGrid(ArrayList<team> arrayList) {
        sg1 = (TableFixHeader) findViewById(R.id.sg1);


        if (arrayList.size() > 0) {
            team t1 = arrayList.get(0);

            int len = t1.getcol1().split(",").length;
            headarr = new String[len];
            widtharr = new int[len];
            int wid = 10;
            int ind = 0;
            for (String h1 : t1.getcol1().split(",")) {
                wid = 0;
                headarr[ind] = h1;
                try {
                    wid = 10 * t1.getcol2().split(fgen.textseprator)[ind].length();
                } catch (Exception ex) {
                }
                if (wid < 80) wid = 80;
                if (wid > 200) wid = 200;
                widtharr[ind] = wid;
                ind++;
            }


            fgen.feedListresult = arrayList;
            columncount = headarr.length;
            rowcount = arrayList.size() + 2;
            baseTableAdapter = new FamilyNexusAdapter(frm_list.this, fgen.feedListresult, headarr, widtharr);
            sg1.setAdapter(baseTableAdapter);
        }
    }

    public class FamilyNexusAdapter extends BaseTableAdapter {

        private final NexusTypes familys[];
        private String[] headers = {
                "Name",
                "RAM",
        };

        private int[] widths = {
                120,
                100,
        };
        private final float density;

        public FamilyNexusAdapter(Context context, ArrayList<team> fedlist, String[] headarr, int[] widtharr) {
            familys = new NexusTypes[]{
                    new NexusTypes(""),
//                    new NexusTypes("Tablets"),
//                    new NexusTypes("Others"),
            };


            headers = headarr;
            widths = widtharr;
            density = context.getResources().getDisplayMetrics().density;
            try {
                String[] rowdata;
                for (int i = 0; i < fedlist.size(); i++) {
                    rowdata = new String[columncount];
                    for (int j = 0; j <= columncount - 1; j++) {
                        try {
                            rowdata[j] = fedlist.get(i).getcol2().split(fgen.textseprator)[j].toString().trim();
                        } catch (Exception ex) {
                            rowdata[j] = "";
                        }
                    }

                    familys[0].list.add(new Nexus(rowdata));
                }
            } catch (Exception e) {
                alertbox("", e.getMessage());
            }
        }

        @Override
        public int getRowCount() {
            return rowcount;
        }

        @Override
        public int getColumnCount() {
            return columncount;
        }

        @Override
        public View getView(final int row, final int column, View convertView, ViewGroup parent) {
            final View view;
            switch (getItemViewType(row, column)) {
                case 0:
                    view = getFirstHeader(row, column, convertView, parent);
                    break;
                case 1:
                    view = getHeader(row, column, convertView, parent);
                    break;
                case 2:
                    view = getFirstBody(row, column, convertView, parent);
                    break;
                case 3:
                    view = getBody(row, column, convertView, parent);
                    break;
                case 4:
                    view = getFamilyView(row, column, convertView, parent);
                    break;
                default:
                    throw new RuntimeException("wtf?");
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (row == -1) {
                        return;
                    } else {
                        drillLevel++;
                        if (drillLevel == 1)
                            fgen.hf1col1 = fgen.feedListresult.get(row - 1).getcol5().trim();
                        if (drillLevel == 2)
                            fgen.hf1col2 = fgen.feedListresult.get(row - 1).getcol5().trim();
                        showdata();
                    }
                }
            });
            return view;
        }

        private View getFirstHeader(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_table_header_first, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.text1)).setText(headers[0]);
            return convertView;
        }

        private View getHeader(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_table_header, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.text1)).setText(headers[column + 1]);
            return convertView;
        }

        private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_table_first, parent, false);
            }
            convertView.setBackgroundResource(row % 2 == 0 ? R.drawable.bg_table_color1 : R.drawable.bg_table_color2);
            ((TextView) convertView.findViewById(R.id.text1)).setText(getDevice(row).data[column + 1]);
            return convertView;
        }

        private View getBody(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_table, parent, false);
            }
            convertView.setBackgroundResource(row % 2 == 0 ? R.drawable.bg_table_color1 : R.drawable.bg_table_color2);
            ((TextView) convertView.findViewById(R.id.text1)).setText(getDevice(row).data[column + 1]);
            return convertView;
        }

        private View getFamilyView(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_table_family, parent, false);
            }
            final String string;
            if (column == -1) {
                string = getFamily(row).name;
            } else {
                string = "";
            }
            ((TextView) convertView.findViewById(R.id.text1)).setText(string);
            return convertView;
        }

        @Override
        public int getWidth(int column) {
            return Math.round(widths[column + 1] * density);
        }

        @Override
        public int getHeight(int row) {
            final int height;
            if (row == -1) {
                height = 35;
            } else if (isFamily(row)) {
                height = 1;
            } else {
                height = 45;
            }
            return Math.round(height * density);
        }

        @Override
        public int getItemViewType(int row, int column) {
            final int itemViewType;
            if (row == -1 && column == -1) {
                itemViewType = 0;
            } else if (row == -1) {
                itemViewType = 1;
            } else if (isFamily(row)) {
                itemViewType = 4;
            } else if (column == -1) {
                itemViewType = 2;
            } else {
                itemViewType = 3;
            }
            return itemViewType;
        }

        private boolean isFamily(int row) {
            int family = 0;
            while (row > 0) {
                row -= familys[family].size() + 1;
                family++;
            }
            return row == 0;
        }

        private NexusTypes getFamily(int row) {
            int family = 0;
            while (row >= 0) {
                row -= familys[family].size() + 1;
                family++;
            }
            return familys[family - 1];
        }

        private Nexus getDevice(int row) {
            int family = 0;
            while (row >= 0) {
                row -= familys[family].size() + 1;
                family++;
            }
            family--;
            return familys[family].get(row + familys[family].size());
        }

        @Override
        public int getViewTypeCount() {
            return 5;
        }
    }

    private class NexusTypes {
        private final String name;
        private final List<Nexus> list;

        NexusTypes(String name) {
            this.name = name;
            list = new ArrayList<Nexus>();
        }

        public int size() {
            return list.size();
        }

        public Nexus get(int i) {
            return list.get(i);
        }
    }

    private class Nexus {
        private final String[] data;

        private Nexus(String[] names) {
            data = names;
        }
    }

    public void onBackPressed() {
        if (drillLevel > 0) {
            drillLevel--;
            backP();
        } else {
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.backmenu, menu);
        return true;
    }

    void backP() {
        Log.d(TAG, "Login");


        final MyProgressdialog progressDialog = new MyProgressdialog(frm_list.this);
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        showdata();
                        progressDialog.dismiss();
                    }
                }, 100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.btnBack:
                if (drillLevel > 0) {
                    drillLevel--;
                    backP();
                } else {
                    super.onBackPressed();
                    this.finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDatePickerDialogue() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePickerDialog.OnDateSetListener) this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        String day = "";
        String month = "";
        if (String.valueOf(i2).length() == 1) {
            day = "0" + String.valueOf(i2);
        } else {
            day = String.valueOf(i2);
        }

        if (String.valueOf(i1).length() == 1) {
            month = "0" + String.valueOf(i1 + 1);
            if(i1 >=9)
            {
                month = String.valueOf(i1 + 1);
            }
        } else {
            month = String.valueOf(i1);
            if(i1 > 9)
            {
                month = String.valueOf(i1 + 1);
            }
        }
        String date = (day + "/" + month + "/" + i).toString();
        if (xClickView == "from_dt") {
           txt_from_dt.setText(date);
        }
        if (xClickView == "to_dt") {
            txt_to_dt.setText(date);
        }
    }

}