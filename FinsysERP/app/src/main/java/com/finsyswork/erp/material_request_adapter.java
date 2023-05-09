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

public class material_request_adapter extends RecyclerView.Adapter<material_request_adapter.ViewHolder> {

    Context context;
    ArrayList<issue_req_model> list;


    public material_request_adapter(Context context, ArrayList<issue_req_model> list) {
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
        sqliteHelperClass helper = new sqliteHelperClass(context);
        Log.d("Lv_Req_Modelclass", ""+list);
        holder.txtSrno.setText(m.getRow_no());
        holder.txtItem.setText(m.getItem());
        holder.txtQty.setText(m.getQty());

        holder.imgremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteHelperClass helper = new sqliteHelperClass(context);
                helper.issue_req_deletedata(m);
                list.remove(position);
                notifyDataSetChanged();
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
