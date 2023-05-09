package com.finsyswork.erp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class truch_loading_adapter extends RecyclerView.Adapter<truch_loading_adapter.viewHolder> {

    Context context;

    public truch_loading_adapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.truck_loading_card_view, parent, false);
        if(frm_attend_maintenance_request.pick_attend_maintenance.equals("Y"))
        {
            view = LayoutInflater.from(context).inflate(R.layout.attend_maintenance_req_card_view, parent, false);
        }
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        expense_model m = frm_record_loading_truck.list.get(position);
        holder.txtcustomer.setText(m.expense);
        holder.txtdestination.setText(m.amount);

        holder.imgremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteHelperClass helper = new sqliteHelperClass(context);
                helper.record_expense_deletedata(m);
                frm_record_loading_truck.list.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return frm_record_loading_truck.list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView txtcustomer, txtdestination;
        ImageView imgremove;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtcustomer = itemView.findViewById(R.id.txtcard_customer);
            txtdestination = itemView.findViewById(R.id.txtcard_destination);
            imgremove = itemView.findViewById(R.id.imgremove);
        }
    }
}
