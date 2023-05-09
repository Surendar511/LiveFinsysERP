package com.finsyswork.erp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class comman_adapter extends RecyclerView.Adapter<comman_adapter.ViewHolder> {

    Context context;
    ArrayList<comman_model> list = new ArrayList<>();

    public comman_adapter(Context context, ArrayList<comman_model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = null;
        switch (fgen.xcard_view_name)
        {
            case "sale_order_card_View" :
                view = LayoutInflater.from(context).inflate(R.layout.sale_order_card_view, parent, false);
                break;
            case "sale_order_schedule_booking_card_view":
                view = LayoutInflater.from(context).inflate(R.layout.sale_order_schedule_booking_card_view, parent, false);
                break;
            default:
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull comman_adapter.ViewHolder holder, int position) {
        comman_model m = list.get(position);
        switch (fgen.xcard_view_name)
        {
            case "sale_order_card_View" :
                holder.txtitem.setText(m.getXcol2());
                holder.txtqty.setText(m.getXcol3());
                holder.txtrate.setText(m.getXcol4());
                holder.txtdate.setText(m.getXcol5());
                break;
            case "sale_order_schedule_booking_card_view" :
                holder.txtitem.setText(m.getXcol2());
                holder.txtqty.setText(m.getXcol3());
                holder.txtdate.setText(m.getXcol4());
                break;
            default:
                break;
        }

        holder.imgremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteHelperClass helper = new sqliteHelperClass(context);
                helper.comman_deletedata(m);
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.txtitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fgen.frm_request.equals("frm_quality_inspection")) {
                    for (comman_model m: frm_quality_inspection.list2) {
                        if(holder.txtitem.getText().toString().trim().equals(m.xcol1))
                        {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getRootView().getContext())
                                    .setTitle("Error Found")
                                    .setMessage("Sorry, Duplicate Rows Are Not Allowed!!")
                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            alertDialog.show();
                            return;
                        }
                    }
                    frm_quality_inspection.model = new comman_model(holder.txtitem.getText().toString().trim(), holder.txtdate.getText().toString().trim(), "0", "-");
                    frm_quality_inspection.list2.add(frm_quality_inspection.model);
                    frm_quality_inspection.adapter2 = new quality_inspection_adapter(context, frm_quality_inspection.list2);
                    frm_quality_inspection.recyclerView.setAdapter(frm_quality_inspection.adapter2);
                    frm_quality_inspection.adapter2.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtitem, txtqty, txtrate, txtdate;
        ImageView imgremove;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgremove = itemView.findViewById(R.id.imgremove);
            switch (fgen.xcard_view_name)
            {
                case "sale_order_card_View" :
                    txtitem = itemView.findViewById(R.id.txtcard_item);
                    txtqty = itemView.findViewById(R.id.txtcard_qty);
                    txtrate = itemView.findViewById(R.id.txtcard_rate);
                    txtdate = itemView.findViewById(R.id.txtcard_date);
                    break;
                case "sale_order_schedule_booking_card_view" :
                    txtitem = itemView.findViewById(R.id.txtcard_item);
                    txtqty = itemView.findViewById(R.id.txtcard_qty);
                    txtdate = itemView.findViewById(R.id.txtcard_date);
                    break;
                default:
                    break;
            }
        }
    }
}
