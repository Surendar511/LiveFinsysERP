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

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {

    Context context;
    ArrayList<scanner_model> list;

    public myAdapter(Context context, ArrayList<scanner_model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.scanner_card_view, parent, false);
        if(fgen.frm_request.equals("frm_bin_scanning") || fgen.frm_request.equals("frm_fg_stacking") || fgen.frm_request.equals("frm_reel_stacking"))
        {
            view = LayoutInflater.from(context).inflate(R.layout.scanner_bin_card_view, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        scanner_model m = list.get(position);
        int srno = position +1;
        Log.d("Lv_Req_Modelclass", ""+list);
        //holder.txtSrno.setText(m.getRow_no());
        holder.txtSrno.setText(""+srno);
        holder.txtQrCode.setText(m.getQr_Code());
        if(!fgen.frm_request.equals("frm_bin_scanning")) {
            if (!fgen.frm_request.equals("frm_fg_stacking") && !fgen.frm_request.equals("frm_reel_stacking") )
            {
                holder.txtQty.setText(m.getQty());
                holder.txtQrCode.setText(m.getXiname());
                if(!m.getQr_Code().isEmpty()) {
//                    holder.txtQrCode.setText(m.getXiname() + "---" + m.getQr_Code());
                    holder.txtQrCode.setText(m.getXiname() + "" + m.getQr_Code());
                }

            }
        }
        holder.imgremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteHelperClass helper = new sqliteHelperClass(context);
                helper.deletedata(m);
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
        TextView txtSrno, txtQrCode, txtQty;
        ImageView imgremove;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSrno = itemView.findViewById(R.id.txtsrno);
            txtQrCode = itemView.findViewById(R.id.txtqrcode);
            if(!fgen.frm_request.equals("frm_bin_scanning")) {
                txtQty = itemView.findViewById(R.id.txtqty);
            }
            imgremove = itemView.findViewById(R.id.imgremove);
        }
    }
}
