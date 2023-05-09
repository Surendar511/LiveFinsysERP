package com.finsyswork.erp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class issue_slip_adapter extends RecyclerView.Adapter<issue_slip_adapter.ViewHolder> {

    Context context;
    ArrayList<issue_req_model> list;

    public issue_slip_adapter(Context context, ArrayList<issue_req_model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.material_issue_req_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        issue_req_model m = list.get(position);
        int srno = position + 1;
        sqliteHelperClass helper = new sqliteHelperClass(context);
        Log.d("Lv_Req_Modelclass", ""+list);

        //holder.txtSrno.setText(m.getRow_no());
        holder.txtSrno.setText(""+srno);
        holder.txtItem.setText(m.getItem());
        holder.txtQty.setText(m.getQty());

        holder.imgremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --frm_dispatch_finalise_mvin.scanned_rows;
                sqliteHelperClass helper = new sqliteHelperClass(context);
                helper.issue_slip_deletedata(m);
                list.remove(position);
                notifyDataSetChanged();
                try{
                    frm_dispatch_finalise_mvin.txttotal_scanned_rows.setText("Scanned Rows : "+frm_dispatch_finalise_mvin.scanned_rows);
                }catch (Exception e){}
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtSrno, txtItem, txtQty;
        ImageView imgremove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSrno = itemView.findViewById(R.id.txtcard_sno);
            txtItem = itemView.findViewById(R.id.txtcard_item);
            txtQty = itemView.findViewById(R.id.txtcard_qty);
            imgremove = itemView.findViewById(R.id.imgremove);
        }
    }
}
