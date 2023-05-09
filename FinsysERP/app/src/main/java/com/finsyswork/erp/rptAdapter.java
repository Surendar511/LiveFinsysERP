package com.finsyswork.erp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class rptAdapter extends RecyclerView.Adapter<rptAdapter.ViewHolder> implements Filterable {

    Context context;
    ArrayList<team6> feedlist = new ArrayList<>();
    String mq;
    int clicked = 0;
    int double_click = 0;

    public rptAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_rptlevel50, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull rptAdapter.ViewHolder holder, int position) {
//        team6 m = feedlist.get(position);
        team6 m = frm_approve.feedList.get(position);
        int myInd = 0;
        final team6 teamobj = m;
        String[] myValues = new String[50];
        int[] myVisibility = new int[50];
        int[] myWidth = new int[50];
        for (String h : teamobj.getcol2().split(fgen.textseprator)) {
            myValues[myInd] = h;
            myVisibility[myInd] = 0;
            myInd++;
        }

        holder.col1.setWidth(myWidth[0]);
        holder.col2.setWidth(myWidth[1]);
        holder.col3.setWidth(myWidth[2]);
        holder.col4.setWidth(myWidth[3]);
        holder.col5.setWidth(myWidth[4]);
        holder.col6.setWidth(myWidth[5]);
        holder.col7.setWidth(myWidth[6]);
        holder.col8.setWidth(myWidth[7]);
        holder.col9.setWidth(myWidth[8]);
        holder.col10.setWidth(myWidth[9]);
        holder.col11.setWidth(myWidth[10]);
        holder.col12.setWidth(myWidth[11]);
        holder.col13.setWidth(myWidth[12]);
        holder.col14.setWidth(myWidth[13]);
        holder.col15.setWidth(myWidth[14]);
        holder.col16.setWidth(myWidth[15]);
        holder.col17.setWidth(myWidth[16]);
        holder.col18.setWidth(myWidth[17]);
        holder.col19.setWidth(myWidth[18]);
        holder.col20.setWidth(myWidth[19]);
        holder.col21.setWidth(myWidth[20]);
        holder.col22.setWidth(myWidth[21]);
        holder.col23.setWidth(myWidth[22]);
        holder.col24.setWidth(myWidth[23]);
        holder.col25.setWidth(myWidth[24]);
        holder.col26.setWidth(myWidth[25]);
        holder.col27.setWidth(myWidth[26]);
        holder.col28.setWidth(myWidth[27]);
        holder.col29.setWidth(myWidth[28]);
        holder.col30.setWidth(myWidth[29]);
        holder.col31.setWidth(myWidth[30]);
        holder.col32.setWidth(myWidth[31]);
        holder.col33.setWidth(myWidth[32]);
        holder.col34.setWidth(myWidth[33]);
        holder.col35.setWidth(myWidth[34]);
        holder.col36.setWidth(myWidth[35]);
        holder.col37.setWidth(myWidth[36]);
        holder.col38.setWidth(myWidth[37]);
        holder.col39.setWidth(myWidth[38]);
        holder.col40.setWidth(myWidth[39]);
        holder.col41.setWidth(myWidth[40]);
        holder.col42.setWidth(myWidth[41]);
        holder.col43.setWidth(myWidth[42]);
        holder.col44.setWidth(myWidth[43]);
        holder.col45.setWidth(myWidth[44]);
        holder.col46.setWidth(myWidth[45]);
        holder.col47.setWidth(myWidth[46]);
        holder.col48.setWidth(myWidth[47]);
        holder.col49.setWidth(myWidth[48]);
        holder.col50.setWidth(myWidth[49]);
        // set visibility of col
        holder.col1.setVisibility(myVisibility[0]);
        holder.col2.setVisibility(myVisibility[1]);
        holder.col3.setVisibility(myVisibility[2]);
        holder.col4.setVisibility(myVisibility[3]);
        holder.col5.setVisibility(myVisibility[4]);
        holder.col6.setVisibility(myVisibility[5]);
        holder.col7.setVisibility(myVisibility[6]);
        holder.col8.setVisibility(myVisibility[7]);
        holder.col9.setVisibility(myVisibility[8]);
        holder.col10.setVisibility(myVisibility[9]);
        holder.col11.setVisibility(myVisibility[10]);
        holder.col12.setVisibility(myVisibility[11]);
        holder.col13.setVisibility(myVisibility[12]);
        holder.col14.setVisibility(myVisibility[13]);
        holder.col15.setVisibility(myVisibility[14]);
        holder.col16.setVisibility(myVisibility[15]);
        holder.col17.setVisibility(myVisibility[16]);
        holder.col18.setVisibility(myVisibility[17]);
        holder.col19.setVisibility(myVisibility[18]);
        holder.col20.setVisibility(myVisibility[19]);
        holder.col21.setVisibility(myVisibility[20]);
        holder.col22.setVisibility(myVisibility[21]);
        holder.col23.setVisibility(myVisibility[22]);
        holder.col24.setVisibility(myVisibility[23]);
        holder.col25.setVisibility(myVisibility[24]);
        holder.col26.setVisibility(myVisibility[25]);
        holder.col27.setVisibility(myVisibility[26]);
        holder.col28.setVisibility(myVisibility[27]);
        holder.col29.setVisibility(myVisibility[28]);
        holder.col30.setVisibility(myVisibility[29]);
        holder.col31.setVisibility(myVisibility[30]);
        holder.col32.setVisibility(myVisibility[31]);
        holder.col33.setVisibility(myVisibility[32]);
        holder.col34.setVisibility(myVisibility[33]);
        holder.col35.setVisibility(myVisibility[34]);
        holder.col36.setVisibility(myVisibility[35]);
        holder.col37.setVisibility(myVisibility[36]);
        holder.col38.setVisibility(myVisibility[37]);
        holder.col39.setVisibility(myVisibility[38]);
        holder.col40.setVisibility(myVisibility[39]);
        holder.col41.setVisibility(myVisibility[40]);
        holder.col42.setVisibility(myVisibility[41]);
        holder.col43.setVisibility(myVisibility[42]);
        holder.col44.setVisibility(myVisibility[43]);
        holder.col45.setVisibility(myVisibility[44]);
        holder.col46.setVisibility(myVisibility[45]);
        holder.col47.setVisibility(myVisibility[46]);
        holder.col48.setVisibility(myVisibility[47]);
        holder.col49.setVisibility(myVisibility[48]);
        holder.col50.setVisibility(myVisibility[49]);
        // setting value of texbox
        holder.col1.setText(myValues[0]);
        holder.col2.setText(myValues[1]);
        holder.col3.setText(myValues[2]);
        holder.col4.setText(myValues[3]);
        holder.col5.setText(myValues[4]);
        holder.col6.setText(myValues[5]);
        holder.col7.setText(myValues[6]);
        holder.col8.setText(myValues[7]);
        holder.col9.setText(myValues[8]);
        holder.col10.setText(myValues[9]);
        holder.col11.setText(myValues[10]);
        holder.col12.setText(myValues[11]);
        holder.col13.setText(myValues[12]);
        holder.col14.setText(myValues[13]);
        holder.col15.setText(myValues[14]);
        holder.col16.setText(myValues[15]);
        holder.col17.setText(myValues[16]);
        holder.col18.setText(myValues[17]);
        holder.col19.setText(myValues[18]);
        holder.col20.setText(myValues[19]);
        holder.col21.setText(myValues[20]);
        holder.col22.setText(myValues[21]);
        holder.col23.setText(myValues[22]);
        holder.col24.setText(myValues[23]);
        holder.col25.setText(myValues[24]);
        holder.col26.setText(myValues[25]);
        holder.col27.setText(myValues[26]);
        holder.col28.setText(myValues[27]);
        holder.col29.setText(myValues[28]);
        holder.col30.setText(myValues[29]);
        holder.col31.setText(myValues[30]);
        holder.col32.setText(myValues[31]);
        holder.col33.setText(myValues[32]);
        holder.col34.setText(myValues[33]);
        holder.col35.setText(myValues[34]);
        holder.col36.setText(myValues[35]);
        holder.col37.setText(myValues[36]);
        holder.col38.setText(myValues[37]);
        holder.col39.setText(myValues[38]);
        holder.col40.setText(myValues[39]);
        holder.col41.setText(myValues[40]);
        holder.col42.setText(myValues[41]);
        holder.col43.setText(myValues[42]);
        holder.col44.setText(myValues[43]);
        holder.col45.setText(myValues[44]);
        holder.col46.setText(myValues[45]);
        holder.col47.setText(myValues[46]);
        holder.col48.setText(myValues[47]);
        holder.col49.setText(teamobj.getcol6());
        holder.col50.setText(teamobj.getcol3());
        holder.checkBox.setChecked(teamobj.isSelected());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(frm_approve.feedList.get(position).selected) {
                    frm_approve.feedList.get(position).selected = false;
                }else {
                    frm_approve.feedList.get(position).selected = true;
                }
                notifyDataSetChanged();
            }
        });

        holder.col1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double_click++;
                if(double_click == 2) {
                    double_click = 0;
                    clicked++;
                    if (clicked > 0) {
                        myPrintOut(position);
                        clicked = 0;
                    }
                }
            }
        });
        holder.col2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double_click++;
                if(double_click == 2) {
                    double_click = 0;
                    myclick_adp(position);
                }
            }
        });
        holder.col3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double_click++;
                if(double_click == 2) {
                    double_click = 0;
                    myclick_adp(position);
                }
            }
        });
        holder.col4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double_click++;
                if(double_click == 2) {
                    double_click = 0;
                    myclick_adp(position);
                }
            }
        });
        holder.col5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double_click++;
                if(double_click == 2) {
                    double_click = 0;
                    myclick_adp(position);
                }
            }
        });


        if (frm_approve.feedList.size() > 0) {
            team6 d = frm_approve.feedList.get(0);
            String[] myHName = new String[50];
            int[] myHWidth = new int[50];

            myInd = 0;
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

            holder.col1.setWidth(myHWidth[0]);
            holder.col2.setWidth(myHWidth[1]);
            holder.col3.setWidth(myHWidth[2]);
            holder.col4.setWidth(myHWidth[3]);
            holder.col5.setWidth(myHWidth[4]);
            holder.col6.setWidth(myHWidth[5]);
            holder.col7.setWidth(myHWidth[6]);
            holder.col8.setWidth(myHWidth[7]);
            holder.col9.setWidth(myHWidth[8]);
            holder.col10.setWidth(myHWidth[9]);
            holder.col11.setWidth(myHWidth[10]);
            holder.col12.setWidth(myHWidth[11]);
            holder.col13.setWidth(myHWidth[12]);
            holder.col14.setWidth(myHWidth[13]);
            holder.col15.setWidth(myHWidth[14]);
            holder.col16.setWidth(myHWidth[15]);
            holder.col17.setWidth(myHWidth[16]);
            holder.col18.setWidth(myHWidth[17]);
            holder.col19.setWidth(myHWidth[18]);
            holder.col20.setWidth(myHWidth[19]);
            holder.col21.setWidth(myHWidth[20]);
            holder.col22.setWidth(myHWidth[21]);
            holder.col23.setWidth(myHWidth[22]);
            holder.col24.setWidth(myHWidth[23]);
            holder.col25.setWidth(myHWidth[24]);
            holder.col26.setWidth(myHWidth[25]);
            holder.col27.setWidth(myHWidth[26]);
            holder.col28.setWidth(myHWidth[27]);
            holder.col29.setWidth(myHWidth[28]);
            holder.col30.setWidth(myHWidth[29]);
            holder.col31.setWidth(myHWidth[30]);
            holder.col32.setWidth(myHWidth[31]);
            holder.col33.setWidth(myHWidth[32]);
            holder.col34.setWidth(myHWidth[33]);
            holder.col35.setWidth(myHWidth[34]);
            holder.col36.setWidth(myHWidth[35]);
            holder.col37.setWidth(myHWidth[36]);
            holder.col38.setWidth(myHWidth[37]);
            holder.col39.setWidth(myHWidth[38]);
            holder.col40.setWidth(myHWidth[39]);
            holder.col41.setWidth(myHWidth[40]);
            holder.col42.setWidth(myHWidth[41]);
            holder.col43.setWidth(myHWidth[42]);
            holder.col44.setWidth(myHWidth[43]);
            holder.col45.setWidth(myHWidth[44]);
            holder.col46.setWidth(myHWidth[45]);
            holder.col47.setWidth(myHWidth[46]);
            holder.col48.setWidth(myHWidth[47]);
            holder.col49.setWidth(myHWidth[48]);
            holder.col50.setWidth(myHWidth[49]);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public int getItemCount() {
        return frm_approve.feedList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView col1, col2, col3, col4, col5, col6, col7, col8, col9, col10;
        TextView col11, col12, col13, col14, col15, col16, col17, col18, col19, col20;
        TextView col21, col22, col23, col24, col25, col26, col27, col28, col29, col30;
        TextView col31, col32, col33, col34, col35, col36, col37, col38, col39, col40;
        TextView col41, col42, col43, col44, col45, col46, col47, col48, col49, col50;
        HorizontalScrollView horizontalScrollView1;
        CheckBox checkBox;
        
        public ViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            horizontalScrollView1 = (HorizontalScrollView) itemView.findViewById(R.id.horizontalScrollView1);
            col1 = (TextView) itemView.findViewById(R.id.tvcol1);
            col2 = (TextView) itemView.findViewById(R.id.tvcol2);
            col3 = (TextView) itemView.findViewById(R.id.tvcol3);
            col4 = (TextView) itemView.findViewById(R.id.tvcol4);
            col5 = (TextView) itemView.findViewById(R.id.tvcol5);
            col6 = (TextView) itemView.findViewById(R.id.tvcol6);
            col7 = (TextView) itemView.findViewById(R.id.tvcol7);
            col8 = (TextView) itemView.findViewById(R.id.tvcol8);
            col9 = (TextView) itemView.findViewById(R.id.tvcol9);
            col10 = (TextView) itemView.findViewById(R.id.tvcol10);
            col11 = (TextView) itemView.findViewById(R.id.tvcol11);
            col12 = (TextView) itemView.findViewById(R.id.tvcol12);
            col13 = (TextView) itemView.findViewById(R.id.tvcol13);
            col14 = (TextView) itemView.findViewById(R.id.tvcol14);
            col15 = (TextView) itemView.findViewById(R.id.tvcol15);
            col16 = (TextView) itemView.findViewById(R.id.tvcol16);
            col17 = (TextView) itemView.findViewById(R.id.tvcol17);
            col18 = (TextView) itemView.findViewById(R.id.tvcol18);
            col19 = (TextView) itemView.findViewById(R.id.tvcol19);
            col20 = (TextView) itemView.findViewById(R.id.tvcol20);
            col21 = (TextView) itemView.findViewById(R.id.tvcol21);
            col22 = (TextView) itemView.findViewById(R.id.tvcol22);
            col23 = (TextView) itemView.findViewById(R.id.tvcol23);
            col24 = (TextView) itemView.findViewById(R.id.tvcol24);
            col25 = (TextView) itemView.findViewById(R.id.tvcol25);
            col26 = (TextView) itemView.findViewById(R.id.tvcol26);
            col27 = (TextView) itemView.findViewById(R.id.tvcol27);
            col28 = (TextView) itemView.findViewById(R.id.tvcol28);
            col29 = (TextView) itemView.findViewById(R.id.tvcol29);
            col30 = (TextView) itemView.findViewById(R.id.tvcol30);
            col31 = (TextView) itemView.findViewById(R.id.tvcol31);
            col32 = (TextView) itemView.findViewById(R.id.tvcol32);
            col33 = (TextView) itemView.findViewById(R.id.tvcol33);
            col34 = (TextView) itemView.findViewById(R.id.tvcol34);
            col35 = (TextView) itemView.findViewById(R.id.tvcol35);
            col36 = (TextView) itemView.findViewById(R.id.tvcol36);
            col37 = (TextView) itemView.findViewById(R.id.tvcol37);
            col38 = (TextView) itemView.findViewById(R.id.tvcol38);
            col39 = (TextView) itemView.findViewById(R.id.tvcol39);
            col40 = (TextView) itemView.findViewById(R.id.tvcol40);
            col41 = (TextView) itemView.findViewById(R.id.tvcol41);
            col42 = (TextView) itemView.findViewById(R.id.tvcol42);
            col43 = (TextView) itemView.findViewById(R.id.tvcol43);
            col44 = (TextView) itemView.findViewById(R.id.tvcol44);
            col45 = (TextView) itemView.findViewById(R.id.tvcol45);
            col46 = (TextView) itemView.findViewById(R.id.tvcol46);
            col47 = (TextView) itemView.findViewById(R.id.tvcol47);
            col48 = (TextView) itemView.findViewById(R.id.tvcol48);
            col49 = (TextView) itemView.findViewById(R.id.tvcol49);
            col50 = (TextView) itemView.findViewById(R.id.tvcol50);
            checkBox = (CheckBox) itemView.findViewById(R.id.chk1);

        }
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            ArrayList<team6> filtered_data = new ArrayList<>();
            if(keyword.toString().isEmpty())
            {
                filtered_data.addAll(frm_approve.backup);
            }
            else{
                for(team6 obj : frm_approve.backup){
                    if(obj.getcol1().toString().toLowerCase().contains(keyword.toString().toLowerCase()) || obj.getcol2().toString().toLowerCase().contains(keyword.toString().toLowerCase())|| obj.getcol3().toString().toLowerCase().contains(keyword.toString().toLowerCase())|| obj.getcol4().toString().toLowerCase().contains(keyword.toString().toLowerCase())|| obj.getcol5().toString().toLowerCase().contains(keyword.toString().toLowerCase())) {
                        filtered_data.add(obj);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered_data;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            frm_approve.feedList.clear();
            frm_approve.feedList.addAll((ArrayList<team6>) filterResults.values);
            notifyDataSetChanged();

        }
    };

    private void myclick_adp(int position) {
        int pos = position;
        team6 teamobj = frm_approve.feedList.get(pos);
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
        team6 teamobj = frm_approve.feedList.get(pos);
        fgen.mq5 = teamobj.getcol6().toString().trim();
        if (fgen.mq5.length() > 0) {
            try {
                Uri uri = Uri.parse(fgen.mq5);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            } catch (Exception e) {

            }
        }
    }

    public void alertboxH(String title, String mymessage) {
        new AlertDialog.Builder(context)
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

}
