package com.finsyswork.erp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class record_expense_adapter extends RecyclerView.Adapter<record_expense_adapter.viewHolder> {

    Context context;

    public record_expense_adapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.record_expense_card_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        expense_model m = frm_record_expense.list.get(position);
        holder.txtexpense.setText(m.expense);
        holder.txtamount.setText(m.amount);
        holder.txtremarks.setText(m.remarks);

        holder.imgremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteHelperClass helper = new sqliteHelperClass(context);
                helper.record_expense_deletedata(m);
                frm_record_expense.list.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return frm_record_expense.list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView txtexpense, txtamount, txtremarks;
        ImageView imgremove;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtexpense = itemView.findViewById(R.id.txtcard_expense);
            txtamount = itemView.findViewById(R.id.txtcard_amount);
            txtremarks = itemView.findViewById(R.id.txtcard_remarks);
            imgremove = itemView.findViewById(R.id.imgremove);
        }
    }
}
