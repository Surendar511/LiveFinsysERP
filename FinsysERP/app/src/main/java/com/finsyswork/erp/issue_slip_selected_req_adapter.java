
package com.finsyswork.erp;

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

public class issue_slip_selected_req_adapter extends RecyclerView.Adapter<issue_slip_selected_req_adapter.ViewHolder> {

    Context context;
    ArrayList<issue_slip_selected_req_model> list;
    int i=0;

    public issue_slip_selected_req_adapter(Context context, ArrayList<issue_slip_selected_req_model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comman_card_view_for_recycler3, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        i++;
        int srno  = position + 1;
        issue_slip_selected_req_model m = list.get(position);
        sqliteHelperClass helper = new sqliteHelperClass(context);
        Log.d("Lv_Req_Modelclass", ""+list);
        holder.txtSrno.setText(""+srno);
        holder.txtItem.setText(m.getXiname());
        holder.txtQty.setText(m.getXqty());
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
        }
    }
}
