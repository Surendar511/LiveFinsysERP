package com.finsyswork.erp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class frm_select extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    AnimatedExpandableListView expListView;
    ExampleAdapter adapter;
    int clickedOnce = 0, erroShown = 0;
    String tileCliecked = "N";

    Typeface typeface;
    TextView txtBox1, txtBox2, txtBox3, txtBox4, txtBox5, txtBox6, txtBox7, txtBox8, txtBox9, txtusername, txtemail;
    TextView txtBox1of1, txtBox1of2, txtBox1of3, txtBox1of4, txtBox1of5, txtBox1of6, txtBox1of7, txtBox1of8, txtBox1of9;
    LinearLayout layout1, layout2, layout3, layout4, layout5, layout6, layout7, layout8, layout9;
    team6 dr = new team6();
    String layout1_rights = "";
    String layout2_rights = "";
    String layout3_rights = "";
    String layout4_rights = "";
    String layout5_rights = "";
    String layout6_rights = "";
    String layout7_rights = "";
    String layout8_rights = "";
    String layout9_rights = "";
    public static String xmainActivity_tiles_menu = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_select);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleMarginStart(40);
        toolbar.setTitleTextColor(Color.GREEN);
        getSupportActionBar().setTitle("BR: " + fgen.branchcd);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.fin1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "For Support contact on 9015 220 220", Snackbar.LENGTH_LONG)
                        .setAction("", null).show();
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
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        layout4 = (LinearLayout) findViewById(R.id.layout4);
        layout5 = (LinearLayout) findViewById(R.id.layout5);
        layout6 = (LinearLayout) findViewById(R.id.layout6);
        layout7 = (LinearLayout) findViewById(R.id.layout7);
        layout8 = (LinearLayout) findViewById(R.id.layout8);
        layout9 = (LinearLayout) findViewById(R.id.layout9);
        txtusername = (TextView) header.findViewById(R.id.txtusername);
        txtemail = (TextView) header.findViewById(R.id.txtEmail);

        txtBox1 = (TextView) findViewById(R.id.txtBox1);
        txtBox2 = (TextView) findViewById(R.id.txtBox2);
        txtBox3 = (TextView) findViewById(R.id.txtBox3);
        txtBox4 = (TextView) findViewById(R.id.txtBox4);
        txtBox5 = (TextView) findViewById(R.id.txtBox5);
        txtBox6 = (TextView) findViewById(R.id.txtBox6);
        txtBox7 = (TextView) findViewById(R.id.txtBox7);
        txtBox8 = (TextView) findViewById(R.id.txtBox8);
        txtBox9 = (TextView) findViewById(R.id.txtBox9);

        txtBox1of1 = (TextView) findViewById(R.id.txtBox1of1);
        txtBox1of2 = (TextView) findViewById(R.id.txtBox1of2);
        txtBox1of3 = (TextView) findViewById(R.id.txtBox1of3);
        txtBox1of4 = (TextView) findViewById(R.id.txtBox1of4);
        txtBox1of5 = (TextView) findViewById(R.id.txtBox1of5);
        txtBox1of6 = (TextView) findViewById(R.id.txtBox1of6);
        txtBox1of7 = (TextView) findViewById(R.id.txtBox1of7);
        txtBox1of8 = (TextView) findViewById(R.id.txtBox1of8);
        txtBox1of9 = (TextView) findViewById(R.id.txtBox1of9);

        finapi.context = frm_select.this;

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
                    fgen.showApiName(frm_select.this, "Mob_Opt_api");
                }
            }
        });


        fillTiles();

        if (layout1_rights.equals("F")) {
            layout1.setVisibility(View.GONE);
        }
        if (layout2_rights.equals("F")) {
            layout2.setVisibility(View.GONE);
        }
        if (layout3_rights.equals("F")) {
            layout3.setVisibility(View.GONE);
        }
        if (layout4_rights.equals("F")) {
            layout4.setVisibility(View.GONE);
        }
        if (layout5_rights.equals("F")) {
            layout5.setVisibility(View.GONE);
        }
        if (layout6_rights.equals("F")) {
            layout6.setVisibility(View.GONE);
        }
        if (layout7_rights.equals("F")) {
            layout7.setVisibility(View.GONE);
        }
        if (layout8_rights.equals("F")) {
            layout8.setVisibility(View.GONE);
        }
        if (layout9_rights.equals("F")) {
            layout9.setVisibility(View.GONE);
        }
        //SALES
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fgen.selected_tile = "SALES";
                fgen.selected_tile = dr.getcol4().split(fgen.textseprator)[0];
                xmainActivity_tiles_menu = dr.getcol5().split(fgen.textseprator)[0];
                if (clickedOnce > 0)
                    page_Load();
                tileCliecked = "Y";
                clickedOnce++;
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fgen.selected_tile = dr.getcol4().split(fgen.textseprator)[1];
                xmainActivity_tiles_menu = dr.getcol5().split(fgen.textseprator)[1];
                if (clickedOnce > 0)
                    page_Load();
                tileCliecked = "Y";
                clickedOnce++;
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fgen.selected_tile = dr.getcol4().split(fgen.textseprator)[2];
                xmainActivity_tiles_menu = dr.getcol5().split(fgen.textseprator)[2];
                if (clickedOnce > 0)
                    page_Load();
                tileCliecked = "Y";
                clickedOnce++;
            }
        });
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fgen.selected_tile = dr.getcol4().split(fgen.textseprator)[3];
                xmainActivity_tiles_menu = dr.getcol5().split(fgen.textseprator)[3];
                if (clickedOnce > 0)
                    page_Load();
                tileCliecked = "Y";
                clickedOnce++;
            }
        });
        layout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fgen.selected_tile = dr.getcol4().split(fgen.textseprator)[4];
                xmainActivity_tiles_menu = dr.getcol5().split(fgen.textseprator)[4];
                if (clickedOnce > 0)
                    page_Load();
                tileCliecked = "Y";
                clickedOnce++;
            }
        });
        layout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fgen.selected_tile = dr.getcol4().split(fgen.textseprator)[5];
                xmainActivity_tiles_menu = dr.getcol5().split(fgen.textseprator)[5];
                if (clickedOnce > 0)
                    page_Load();
                tileCliecked = "Y";
                clickedOnce++;
            }
        });
        layout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fgen.selected_tile = dr.getcol4().split(fgen.textseprator)[6];
                xmainActivity_tiles_menu = dr.getcol5().split(fgen.textseprator)[6];
                if (clickedOnce > 0)
                    page_Load();
                tileCliecked = "Y";
                clickedOnce++;
            }
        });
        layout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fgen.selected_tile = dr.getcol4().split(fgen.textseprator)[7];
                xmainActivity_tiles_menu = dr.getcol5().split(fgen.textseprator)[7];
                if (clickedOnce > 0)
                    page_Load();
                tileCliecked = "Y";
                clickedOnce++;
            }
        });
        layout9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fgen.selected_tile = dr.getcol4().split(fgen.textseprator)[8];
                xmainActivity_tiles_menu = dr.getcol5().split(fgen.textseprator)[8];
                if (clickedOnce > 0)
                    page_Load();
                tileCliecked = "Y";
                clickedOnce++;
            }
        });
    }

    public void page_Load() {
        final MyProgressdialog progressDialog = new MyProgressdialog(this);
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        ArrayList<team> fedicons = finapi.menuApi(fgen.mcd, fgen.muname, fgen.selected_tile);
                        if (fedicons.size() < 1) {
                            if (erroShown > 5) {
                                new AlertDialog.Builder(frm_select.this)
                                        .setTitle("Rights Not Defined!!")
                                        .setMessage("Sorry, You Don't Have Any Rights For This Module , Please Contact Your IT Person!")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
//                                startActivity(new Intent(frm_select.this, Login.class));
                            } else {
                                page_Load();
                                erroShown++;
                            }
                        } else {
                            if (tileCliecked.equals("Y"))
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        progressDialog.dismiss();
                    }
                }, 100);
    }


    public void alertbox(String title, String mymessage) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(frm_select.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(Html.fromHtml(mymessage));
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                        dialog.dismiss();
                    }
                });
        if (!isFinishing()) {
            alertDialog.show();
        }
    }

    void fillTiles() {
        ArrayList<team6> dt1 = new ArrayList<>();
        dt1 = finapi.getApi6(fgen.mcd, "Mob_Opt_api", fgen.muname, "", "", "", "");
        if (dt1.size() > 0) {
            dr = dt1.get(0);
            if(dr.getcol2().equals("") || dr.getcol3().equals("") || dr.getcol4().equals("") || dr.getcol5().equals("") || dr.getcol6().equals(""))
            {
                alertbox("Something Wrong With Api!!", "Please, Try Again To Login OR Press Mobile Option Button Through --> ERP Data Uploading!!");
                return;
            }
            if(dr.getcol3().split("!~!~!").length != dr.getcol4().split("!~!~!").length && dr.getcol4().split("!~!~!").length != dr.getcol5().split("!~!~!").length && dr.getcol5().split("!~!~!").length != dr.getcol6().split("!~!~!").length)
            {
                alertbox("Something Wrong With Api!!", "Please, Try Again To Login bcz data not came properly through api!!");
                return;
            }
            if (dr.getcol1().toUpperCase().equals("SUCCESS")) {
                try {
                    txtBox1.setText(dr.getcol2().split(fgen.textseprator)[0]);
                    txtBox1of1.setText(dr.getcol3().split(fgen.textseprator)[0]);
                    layout1_rights = dr.getcol6().split(fgen.textseprator)[0];
                } catch (Exception ex) {
                    if(txtBox1.getText().toString().isEmpty() || txtBox1of1.getText().toString().isEmpty()){ layout1_rights = "F";}
                }
                try {
                    txtBox2.setText(dr.getcol2().split(fgen.textseprator)[1]);
                    txtBox1of2.setText(dr.getcol3().split(fgen.textseprator)[1]);
                    layout2_rights = dr.getcol6().split(fgen.textseprator)[1];
                } catch (Exception ex) {
                    if(txtBox2.getText().toString().isEmpty() || txtBox1of2.getText().toString().isEmpty()){ layout2_rights = "F";}
                }
                try {
                    txtBox3.setText(dr.getcol2().split(fgen.textseprator)[2]);
                    txtBox1of3.setText(dr.getcol3().split(fgen.textseprator)[2]);
                    layout3_rights = dr.getcol6().split(fgen.textseprator)[2];
                } catch (Exception ex) {
                    if(txtBox3.getText().toString().isEmpty() || txtBox1of3.getText().toString().isEmpty()){ layout3_rights = "F";}
                }
                try {
                    txtBox4.setText(dr.getcol2().split(fgen.textseprator)[3]);
                    txtBox1of4.setText(dr.getcol3().split(fgen.textseprator)[3]);
                    layout4_rights = dr.getcol6().split(fgen.textseprator)[3];
                } catch (Exception ex) {
                    if(txtBox4.getText().toString().isEmpty() || txtBox1of4.getText().toString().isEmpty()){ layout4_rights = "F";}
                }
                try {
                    txtBox5.setText(dr.getcol2().split(fgen.textseprator)[4]);
                    txtBox1of5.setText(dr.getcol3().split(fgen.textseprator)[4]);
                    layout5_rights = dr.getcol6().split(fgen.textseprator)[4];
                } catch (Exception ex) {
                    if(txtBox5.getText().toString().isEmpty() || txtBox1of5.getText().toString().isEmpty()){ layout5_rights = "F";}
                }
                try {
                    txtBox6.setText(dr.getcol2().split(fgen.textseprator)[5]);
                    txtBox1of6.setText(dr.getcol3().split(fgen.textseprator)[5]);
                    layout6_rights = dr.getcol6().split(fgen.textseprator)[5];
                } catch (Exception ex) {
                    if(txtBox6.getText().toString().isEmpty() || txtBox1of6.getText().toString().isEmpty()){ layout6_rights = "F";}
                }
                try {
                    txtBox7.setText(dr.getcol2().split(fgen.textseprator)[6]);
                    txtBox1of7.setText(dr.getcol3().split(fgen.textseprator)[6]);
                    layout7_rights = dr.getcol6().split(fgen.textseprator)[6];
                } catch (Exception ex) {
                    if(txtBox7.getText().toString().isEmpty() || txtBox1of7.getText().toString().isEmpty()){ layout7_rights = "F";}
                }
                try {
                    txtBox8.setText(dr.getcol2().split(fgen.textseprator)[7]);
                    txtBox1of8.setText(dr.getcol3().split(fgen.textseprator)[7]);
                    layout8_rights = dr.getcol6().split(fgen.textseprator)[7];
                } catch (Exception ex) {
                    if(txtBox8.getText().toString().isEmpty() || txtBox1of8.getText().toString().isEmpty()){ layout8_rights = "F";}
                }
                try {
                    txtBox9.setText(dr.getcol2().split(fgen.textseprator)[8]);
                    txtBox1of9.setText(dr.getcol3().split(fgen.textseprator)[8]);
                    layout9_rights = dr.getcol6().split(fgen.textseprator)[8];
                } catch (Exception ex) {
                    if(txtBox9.getText().toString().isEmpty() || txtBox1of9.getText().toString().isEmpty()){ layout9_rights = "F";}
                }
            }
        }
        else {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
        clickedOnce = 0;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                fillTiles();
                break;
            case R.id.action_go_back:
                startActivity(new Intent(getApplicationContext(), Login.class));
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
            Intent intent = new Intent(frm_select.this, changeprofile.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.nav_changepwd) {
            Intent intent = new Intent(frm_select.this, changepwd.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(frm_select.this)
                    .setMessage("Do you want to Logout ?")
                    .setTitle("Logout ?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedpreferences = getSharedPreferences("mypref",
                                    Context.MODE_PRIVATE);
                            sharedpreferences.edit().clear().commit();
                            Intent intent = new Intent(frm_select.this, Login.class);
                            startActivityForResult(intent, 1);
//                            finish();
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
                holder.title.setText(item.text.split("#~#~#")[0].trim());
                holder.title2.setText(item.text.split("#~#~#")[1].trim());
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
