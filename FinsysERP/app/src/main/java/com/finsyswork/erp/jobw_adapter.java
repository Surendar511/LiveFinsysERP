package com.finsyswork.erp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.BreakIterator;

public class jobw_adapter extends RecyclerView.Adapter<jobw_adapter.ViewHolder>{

    Context context;

    public jobw_adapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        switch (frm_jobw_prod.xgetTab)
        {
            case "O":
                view = LayoutInflater.from(context).inflate(R.layout.jobw_prod_output_card_view, parent, false);
                break;
            default:
                view = LayoutInflater.from(context).inflate(R.layout.jobw_prod_input_card_view, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull jobw_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        jobw_model m;
        switch (frm_jobw_prod.xgetTab)
        {
            case "O":
                m = frm_jobw_prod.output_list.get(position);
                holder.txtcard_item.setText(m.xcol1);
                holder.txtcard_batch.setText(m.xcol2);
                holder.txtcard_qty.setText(m.xcol3);
                holder.imgremove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        frm_jobw_prod.output_list.remove(position);
                        notifyDataSetChanged();
                    }
                });
                break;
            case "I":
                m = frm_jobw_prod.input_list.get(position);
                holder.txtcard_code.setText(m.xcol1);
                holder.txtcard_name.setText(m.xcol2);
                holder.txtcard_qty.setText(m.xcol3);
                holder.imgremove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        frm_jobw_prod.input_list.remove(position);
                        notifyDataSetChanged();
                    }
                });
                break;
            case "R":
                m = frm_jobw_prod.reject_list.get(position);
                holder.txtcard_code.setText(m.xcol1);
                holder.txtcard_name.setText(m.xcol2);
                holder.txtcard_qty.setText(m.xcol3);
                holder.txtcard_unit.setText(m.xcol4);
                holder.imgremove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        frm_jobw_prod.reject_list.remove(position);
                        notifyDataSetChanged();
                    }
                });
                break;
            case "D":
                m = frm_jobw_prod.downtime_list.get(position);
                holder.txtcard_code.setText(m.xcol1);
                holder.txtcard_name.setText(m.xcol2);
                holder.txtcard_qty.setText(m.xcol3);
                holder.imgremove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        frm_jobw_prod.downtime_list.remove(position);
                        notifyDataSetChanged();
                    }
                });
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        int xsize = 0;
        switch (frm_jobw_prod.xgetTab)
        {
            case "O":
                xsize = frm_jobw_prod.output_list.size();
                break;
            case "I":
                xsize =frm_jobw_prod.input_list.size();
                break;
            case "R":
                xsize =frm_jobw_prod.reject_list.size();
                break;
            case "D":
                xsize =frm_jobw_prod.downtime_list.size();
                break;
            default:
                xsize = frm_jobw_prod.output_list.size();
                break;
        }
        return xsize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtcard_item, txtcard_batch, txtcard_qty, txtcard_code, txtcard_name,txtcard_unit;
        ImageView imgremove;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgremove = itemView.findViewById(R.id.imgremove);
            txtcard_qty = itemView.findViewById(R.id.txtcard_qty);
            if(frm_jobw_prod.xgetTab.equals("O")) {
                txtcard_item = itemView.findViewById(R.id.txtcard_item);
                txtcard_batch = itemView.findViewById(R.id.txtcard_batch);
            }
            else{
                txtcard_code = itemView.findViewById(R.id.txtcard_code);
                txtcard_name = itemView.findViewById(R.id.txtcard_name);
                txtcard_qty = itemView.findViewById(R.id.txtcard_qty);
                txtcard_unit = itemView.findViewById(R.id.txtcard_unit);
            }
        }
    }
}
