package com.finsyswork.erp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    AnimatedExpandableListView expListView;
    ExampleAdapter adapter;
    private String TAG;
    int clickedOnce = 0, erroShown = 0;
    String tileCliecked = "N";
    Typeface typeface;
    TextView txtBox1, txtBox2, txtBox3, txtBox4, txtusername, txtemail;
    TextView txtBox1of1, txtBox1of2, txtBox1of3, txtBox1of4, txtBox1of5;
    LinearLayout layout1, layout2, layout3, layout4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleMarginStart(40);
        toolbar.setTitleTextColor(Color.GREEN);
        getSupportActionBar().setTitle("BR: " + fgen.branchcd);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.fin1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fgen.frm_request ="";

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), frm_select.class));
                finish();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        expListView = (AnimatedExpandableListView) findViewById(R.id.explist1);
        //txtsearch = (EditText) findViewById(R.id.txtsearch);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        layout4 = (LinearLayout) findViewById(R.id.layout4);
        txtusername = (TextView) header.findViewById(R.id.txtusername);
        txtemail = (TextView) header.findViewById(R.id.txtEmail);

        txtBox1 = (TextView) findViewById(R.id.txtBox1);
        txtBox2 = (TextView) findViewById(R.id.txtBox2);
        txtBox3 = (TextView) findViewById(R.id.txtBox3);
        txtBox4 = (TextView) findViewById(R.id.txtBox4);

        txtBox1of1 = (TextView) findViewById(R.id.txtBox1of1);
        txtBox1of2 = (TextView) findViewById(R.id.txtBox1of2);
        txtBox1of3 = (TextView) findViewById(R.id.txtBox1of3);
        txtBox1of4 = (TextView) findViewById(R.id.txtBox1of4);

        finapi.context = MainActivity.this;

        adapter = new ExampleAdapter(this);
        tileCliecked = "Y";
        txtusername.setText(fgen.muname);
        txtemail.setText(fgen.emailid);

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(MainActivity.this, frm_select.xmainActivity_tiles_menu);
                }
            }
        });


        page_Load();
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickedOnce > 0)
                    page_Load();
                tileCliecked = "Y";
                clickedOnce++;
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickedOnce > 0)
                    page_Load();
                tileCliecked = "Y";
                clickedOnce++;
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickedOnce > 0)
                    page_Load();
                tileCliecked = "Y";
                clickedOnce++;
            }
        });
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickedOnce > 0)
                    page_Load();
                tileCliecked = "Y";
                clickedOnce++;
            }
        });
    }

    public void page_Load() {
        Log.d(TAG, "Login");
        final MyProgressdialog progressDialog = new MyProgressdialog(this);
        progressDialog.show();

        // TODO: Implement your own authentication logic here.
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        ArrayList<team> fedicons = finapi.menuApi(fgen.mcd, fgen.muname, fgen.selected_tile);
                        if (fedicons.size() < 1) {
                            if (erroShown > 5) {
                                startActivity(new Intent(MainActivity.this, Login.class));
                            } else {
                                page_Load();
                                erroShown++;
                            }
                        } else {
                            if (tileCliecked.equals("Y"))
                                fillTiles();
                            showdata(fedicons);
                        }
                        progressDialog.dismiss();
                    }
                }, 100);
    }

    void fillTiles() {
        ArrayList<team6> dt1 = finapi.getApi6(fgen.mcd, frm_select.xmainActivity_tiles_menu, fgen.muname, "", "", "", "");
        if (dt1.size() > 0) {
            team6 dr = dt1.get(0);
            if (dr.getcol1().toUpperCase().equals("SUCCESS")) {
                try {
                    txtBox1.setText(dr.getcol2().split(fgen.textseprator)[0]);
                    txtBox1of1.setText(dr.getcol3());
                } catch (Exception ex) {
                }
                try {
                    txtBox2.setText(dr.getcol2().split(fgen.textseprator)[1]);
                    txtBox1of2.setText(dr.getcol4());
                } catch (Exception ex) {
                }
                try {
                    txtBox3.setText(dr.getcol2().split(fgen.textseprator)[2]);
                    txtBox1of3.setText(dr.getcol5());
                } catch (Exception ex) {
                }
                try {
                    txtBox4.setText(dr.getcol2().split(fgen.textseprator)[3]);
                    txtBox1of4.setText(dr.getcol6());
                } catch (Exception ex) {
                }
                try {
                    txtBox4.setText(dr.getcol2().split(fgen.textseprator)[3]);
                    txtBox1of4.setText(dr.getcol6());
                } catch (Exception ex) {
                }
            }
        }
        clickedOnce = 0;
    }

    private void showdata(ArrayList<team> fed) {
        List<GroupItem> Items = new ArrayList<GroupItem>();
        ArrayList<team> fedheader, fedchild;
        fedheader = new ArrayList<>();
        fedchild = new ArrayList<>();
        GroupItem grp1 = new GroupItem();

        String fLevel = "0";
        for (int i = 0; i < fed.size(); i++) {
            if (fed.get(i).getcol2().trim().equals("0")) {
                fedheader.add(fed.get(i));
                fLevel = fed.get(i).getcol1();
                continue;
            }
            if (fed.get(i).getcol2().trim().equals(fLevel)) {
                fedchild.add(fed.get(i));
            }
        }

        for (int j = 0; j < fedheader.size(); j++) {
            grp1 = new GroupItem();
            grp1.title = fedheader.get(j).getcol4();
            for (int k = 0; k < fedchild.size(); k++) {
                if (fedheader.get(j).getcol1().trim().equals(fedchild.get(k).getcol2().trim())) {
                    grp1.items.add(addchild(fedchild.get(k).getcol5().trim(), fedchild.get(k).getcol2().trim(), fedchild.get(k).getcol3().trim(), fedchild.get(k).getcol4().trim(), "-", fedchild.get(k).getcol4().trim(), "-", "-", fedchild.get(k).getcol2().trim(), "-"));
                }
            }
            Items.add(grp1);
        }

        adapter.setData(Items);

        expListView.setAdapter(adapter);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.

                if (expListView.isGroupExpanded(groupPosition)) {
                    //expListView.collapseGroupWithAnimation(groupPosition);
                    expListView.collapseGroup(groupPosition);
                } else {
                    expListView.expandGroup(groupPosition);
                }
                return true;
            }
        });
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                // TODO Auto-generated method stub
                if (groupPosition != previousItem)
                    expListView.collapseGroup(previousItem);
                previousItem = groupPosition;
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                android.widget.ExpandableListAdapter adap = parent.getExpandableListAdapter();
                int gp = (int) adap.getGroupId(groupPosition);
                int cp = (int) adap.getChildId(groupPosition, childPosition);
                String gpname = adapter.getGroup(groupPosition).title;
                String cpname = adapter.getChild(groupPosition, childPosition).text;
                String cpbtnid = adapter.getChild(groupPosition, childPosition).btnid;
                fgen.btnid = cpbtnid;
                fgen.rptheader = cpname;  // menu text
                String cpforname = adapter.getChild(groupPosition, childPosition).mtype;
                String pkg = getPackageName().trim();
                String mstr = "";
                fgen.expendpos = groupPosition;
                Intent intent = new Intent();
                switch (cpforname.toUpperCase()){
                    case "L":
                        mstr = pkg + "." + "frm_list";
                        break;
                    case "A":
                        mstr = pkg + "." + "frm_approve";
                        break;
                    case "G":
                        mstr = pkg + "." + "frm_graph";
                        break;
                    case "F":
                        switch (fgen.btnid){
                            case "EP745": mstr = pkg + "." + "frm_fg_physical";
                                break;
                            case "EP901" : mstr = pkg + "." + "frm_lv_req";
                                break;
                            case "EP902" : mstr = pkg + "." + "frm_adv_pay";
                                break;
                            case "EP903" : mstr = pkg + "." + "frm_loan_pay";
                                break;
                            case "EP904" :
                                fgen.frm_request = "attendance_check_in";
                                mstr = pkg + "." + "frm_lead_sarfaraz";
                                break;
                            case "EP905" :
                                fgen.frm_request = "attendance_check_out";
                                mstr = pkg + "." + "frm_lead_check_out";
                                break;
                            case "EP801" : mstr = pkg + "." + "frm_lead_sarfaraz";
                                break;
                            case "EP802" : mstr = pkg + "." + "frm_lead_check_out";
                                break;
                            case "EP501" : mstr = pkg + "." + "frm_bin_scanning";
                                break;
                            case "EP701" : fgen.frm_request = "request_material_issue";
                                mstr = pkg + "." + "frm_material_issue_req";
//                               mstr = pkg + "." + "DemoActivity";
                                break;
                            case "EP1203" : fgen.frm_request = "request_material_issue";
                                mstr = pkg + "." + "frm_material_issue_req";
                                break;
                            case "EP702" :fgen.frm_request = "request_material_return";
                                mstr = pkg + "." + "frm_material_issue_req";
                                break;
                            case "EP1205" :fgen.frm_request = "request_material_return";
                                mstr = pkg + "." + "frm_material_issue_req";
                                break;
                            case "EP703" : fgen.frm_request = "request_material_purchase";
                                mstr = pkg + "." + "frm_material_issue_req";
                                break;
                            case "EP721" : mstr = pkg + "." + "frm_issue_slip";
                                break;
                            case "EP1204" : mstr = pkg + "." + "frm_issue_slip";
                                break;
                            case "EP1206" : fgen.frm_request = "material_return_entry";
                                mstr = pkg + "." + "frm_issue_slip";
                                break;
                            case "EP722" : fgen.frm_request = "material_return_entry";
                                mstr = pkg + "." + "frm_issue_slip";
                                break;
                            case "EP803" : mstr = pkg + "." + "frm_record_expense";
                                break;
                            case "EP805" : mstr = pkg + "." + "frm_crm_contacts_master";
                                break;
                            case "EP804" : mstr = pkg + "." + "frm_lead_enquiry";
                                break;
                            case "EP1503":  // MVIN Reel Physical
                            case "EP741" : mstr = pkg + "." + "frm_bar_scanner";
                                break;
                            case "EP1202" : mstr = pkg + "." + "frm_reels_stacking";
                                break;
                            case "EP743" : mstr = pkg + "." + "frm_reels_stacking";
                                break;
                            case "EP1501":   // MVIN Received F.G. WIP Store
                                if(fgen.mcd.equals("MVIN")){
                                    mstr = pkg + "." + "frm_fg_stacking_mvin";

                                }else{
                                    mstr = pkg + "." + "frm_fg_stacking";

                                }

                                break;
                            case "EP771":
                                break;
                            case "EP744" : mstr = pkg + "." + "frm_fg_stacking";
                                break;
                            case "EP1207" : mstr = pkg + "." + "frm_fg_stacking";
                                break;
                            case "EP1208" : mstr = pkg + "." + "frm_fg_stacking";
                                break;
                            case "EP751" : fgen.frm_request = "frm_paper_end_for_reels";
                                mstr = pkg + "." + "frm_paper_end";
                                break;
                            case "EP752" : fgen.frm_request = "frm_paper_end_for_jobs";
                                mstr = pkg + "." + "frm_job_issue_entry";
                                break;
                            case "EP753" :
                                if(fgen.mcd.equals("MCPL2")){
                                    fgen.frm_request = "frm_reel_issue_return";
                                    mstr = pkg + "." + "frm_reel_return_MCPL";
                                }else{
                                    fgen.frm_request = "frm_reel_issue_return";
                                    mstr = pkg + "." + "frm_paper_end";
                                }
                                break;
                            case "EP601" : mstr = pkg + "." + "frm_maintenance_request";
                                break;
                            case "EP602" : mstr = pkg + "." + "frm_attend_maintenance_request";
                                break;
                            case "EP603" : mstr = pkg + "." + "frm_record_alectricity_usage";
                                break;
                            case "EP604" : mstr = pkg + "." + "frm_verify_asset_location";
                                break;
                            case "EP606" : mstr = pkg + "." + "frm_scaninfo";
                                break;
//                            case "EP606" : mstr = pkg + "." + "frm_fg_production_from_qrcode";
//                                break;
                            case "EP451" :  mstr = pkg + "." + "frm_record_arrival_truck";
                                break;
                            case "EP481" :  mstr = pkg + "." + "frm_record_arrival_truck";  // Incomming Vehicle
                                break;
                            case "EP452" : mstr = pkg + "." + "frm_record_loading_truck";
                                break;
                            case "EP482" : mstr = pkg + "." + "frm_record_loading_truck";   // Incomming Vehicle Un-Loading Start
                                break;
                            case "EP483" : mstr = pkg + "." + "frm_record_loading_truck";   // Incomming Vehicle Un-Loading End
                                break;
                            case "EP453" : mstr = pkg + "." + "frm_record_starting_truck";
                                break;
                            case "EP484" : mstr = pkg + "." + "frm_record_starting_truck"; // Incomming Vehicle Return
                                break;
                            case "EP471" : fgen.frm_request = "record_start_trip";
                                mstr = pkg + "." + "frm_truck_driver";
                                break;
                            case "EP472" : fgen.frm_request = "arrival_at_destination";
                                mstr = pkg + "." + "frm_truck_driver";
                                break;
                            case "EP473" : fgen.frm_request = "unloading_started";
                                mstr = pkg + "." + "frm_truck_driver";
                                break;
                            case "EP474" : fgen.frm_request = "customer_feedback";
                                mstr = pkg + "." + "frm_truck_driver";
                                break;
                            case "EP475" : fgen.frm_request = "record_return_trip";
                                mstr = pkg + "." + "frm_truck_driver";
                                break;
                            case "EP502" : mstr = pkg + "." + "frm_jobw_prod";
                                break;
                            case "EP821" : mstr = pkg + "." + "frm_quotation";
                                break;
                            case "EP822" : mstr = pkg + "." + "frm_sale_order";
                                break;
                            case "EP823" : mstr = pkg + "." + "frm_sale_schedule_booking";
                                break;
                            case "EP772" :
                            case "EP1502" : // MVIN
                                mstr = pkg + "." + "frm_dispatch_finalise_mvin";
                                break;
                            case "EP824" : mstr = pkg + "." + "frm_dispatch_entry";
                                break;
                            case "EP1212" : mstr = pkg + "." + "frm_dispatch_entry";
                                break;
                            case "EP1001" : mstr = pkg + "." + "frm_invoice_entry";
                                break;
                            case "EP1002" : mstr = pkg + "." + "frm_visitor_entry";
                                break;
                            case "EP1003" : mstr = pkg + "." + "frm_visitor_out_entry";
                                break;
                            case "EP503" : mstr = pkg + "." + "frm_prod_entry";
                                break;
                            case "EP761" : mstr = pkg + "." + "frm_quality_inspection";
                                break;
                            case "EP1201" : mstr = pkg + "." + "frm_quality_inspection";
                                break;
                            case "EP1101" : mstr = pkg + "." + "frm_collection_followup";
                                break;
                            case "EP1102" : mstr = pkg + "." + "frm_pendin_po_collection";
                                break;
                            case "EP1103" : mstr = pkg + "." + "frm_sale_lead_followup";
                                break;
                            case "EP1104" : mstr = pkg + "." + "frm_task_management";
                                break;
                            case "EP1105" : mstr = pkg + "." + "frm_task_management";
                                break;
                            case "EP1111" : mstr = pkg + "." + "frm_gate_pass";
                                break;
                            case "EP1112" : mstr = pkg + "." + "frm_daily_expense";
                                break;
                            case "EP1113" : mstr = pkg + "." + "frm_all_forms_record";
                                break;
                            case "EP504" : mstr = pkg + "." + "frm_inter_stage_tfr";
                                break;
                            case "EP505" : mstr = pkg + "." + "frm_job_status";
                                break;
                            case "EP506" : mstr = pkg + "." + "frm_start_production";
                                break;
                            case "EP507" : mstr = pkg + "." + "frm_finish_production";
                                break;
//                            case "EP1209" : mstr = pkg + "." + "IS";
                            case "EP1209" : mstr = pkg + "." + "frm_fg_prod_qrcode";
                                break;
                            default:
                                mstr = pkg + "." + "frm_select";
                                break;
                        }
                        break;
                    default:
                        mstr = cpforname;
                        break;
                }
                fgen.btnid = cpbtnid;
                ComponentName cName = new ComponentName(pkg, mstr);
                intent.setComponent(cName);
                startActivity(intent);
//				finish();
                return true;

            }
        });

    }

    private ChildItem addchild(String btnid, String mlevel, String mtype, String text, String allow_level, String web_action, String search_key,
                               String submenu, String form, String param) {
        ChildItem child23 = new ChildItem();
        child23.btnid = btnid;
        child23.mlevel = mlevel;
        child23.mtype = mtype;
        if (text.trim().length() > 58) {
            text = text.substring(0, 55);
        }
        child23.text = text;
        child23.allow_level = allow_level;
        child23.web_action = web_action;
        child23.search_key = search_key;
        child23.submenu = submenu;
        child23.form = form;
        child23.param = param;
        return child23;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_settings:
                page_Load();
                break;
            case R.id.action_go_back:
                startActivity(new Intent(getApplicationContext(), frm_select.class));
//                finish();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, changeprofile.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.nav_changepwd) {
            Intent intent = new Intent(MainActivity.this, changepwd.class);
            startActivityForResult(intent, 1);
        }
        else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Do you want to Logout ?")
                    .setTitle("Logout ?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedpreferences = getSharedPreferences("mypref",
                                    Context.MODE_PRIVATE);
                            sharedpreferences.edit().clear().commit();
                            Intent intent = new Intent(MainActivity.this, Login.class);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static class ChildItem {

        String btnid;
        String mlevel;
        String mtype;
        String text;
        String allow_level;
        String web_action;
        String search_key;
        String submenu;
        String form;
        String param;
    }

    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildHolder {
        TextView title;
        TextView title2;
    }

    private static class GroupHolder {
        TextView title;
        CardView grpcard;
        ImageView headerImage;

    }

    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @SuppressLint("WrongConstant")
        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item1, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.lblListItem);
                holder.title2 = (TextView) convertView.findViewById(R.id.lblListItem2);

                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setTypeface(typeface);
            holder.title2.setTypeface(typeface);

            try {
                holder.title2.setVisibility(View.VISIBLE);
                holder.title.setText(item.text.split("#~#~#")[0]);
                holder.title2.setText(item.text.split("#~#~#")[1]);
//                holder.title.setGravity(Gravity.RIGHT);

            } catch (Exception e) {
                holder.title.setText(item.text);
                holder.title2.setVisibility(View.GONE);
//                holder.title.setGravity(Gravity.CENTER);
            }
            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.list_group1, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.lblListHeader);
                holder.grpcard = (CardView) convertView.findViewById(R.id.grpcard);
                holder.headerImage = (ImageView) convertView.findViewById(R.id.headerImage);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
//
            holder.title.setTypeface(typeface);

            if (expListView.isGroupExpanded(groupPosition))
                holder.headerImage.setBackgroundResource(R.drawable.bluedoubledown);
            else holder.headerImage.setBackgroundResource(R.drawable.bluedouble);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }
}