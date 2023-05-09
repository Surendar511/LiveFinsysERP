package com.finsyswork.erp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class quality_inspection_adapter extends RecyclerView.Adapter<quality_inspection_adapter.ViewHolder> {

    Context context;
    ArrayList<comman_model> list = new ArrayList<>();

    public quality_inspection_adapter(Context context, ArrayList<comman_model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.quality_inspection_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull quality_inspection_adapter.ViewHolder holder, int position) {
        comman_model m = list.get(position);
        holder.txtitem.setText(m.getXcol1());
        holder.txtokqty.setText(m.getXcol2());
        holder.txtrejqty.setText(m.getXcol3());
        holder.txtreason.setText(m.getXcol4());

        holder.imgremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteHelperClass helper = new sqliteHelperClass(context);
                helper.comman_deletedata(m);
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        if(fgen.frm_request.equals("frm_quality_inspection"))
        {
            holder.txtitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowDialogBox(context, holder.txtitem.getText().toString().trim(), position, holder.txtitem.getText().toString().trim(), holder.txtokqty.getText().toString().trim(), holder.txtrejqty.getText().toString().trim(), holder.txtreason.getText().toString().trim());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtitem, txtokqty, txtrejqty, txtreason;
        ImageView imgremove;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgremove = itemView.findViewById(R.id.imgremove);
            txtitem = itemView.findViewById(R.id.txtcard_item);
            txtokqty = itemView.findViewById(R.id.txtcard_okqty);
            txtrejqty = itemView.findViewById(R.id.txtcard_rejqty);
            txtreason = itemView.findViewById(R.id.txtcard_reason);
        }
    }

    public void ShowDialogBox(Context context, String xitem, int position, String item, String xok_qty, String xrej_qty, String xreason){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.quality_dialoge);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }catch (Exception e)
        { }

        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        int dialogWindowHeight = (int) (displayHeight * 0.4f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setLayout(layoutParams.width, layoutParams.height);
        dialog.show();

        EditText txtok_qty = dialog.findViewById(R.id.txtaccept);
        EditText txtrej_qty = dialog.findViewById(R.id.txtreject);
        EditText txtreason = dialog.findViewById(R.id.txtreason);
        Button btn_submit = dialog.findViewById(R.id.btn_submit);
        ImageView btn_close = dialog.findViewById(R.id.close_window);
        TextView txterror = dialog.findViewById(R.id.txterror);
        TextView txttotal_qty = dialog.findViewById(R.id.txttotal_qty);

        txtok_qty.setText(xok_qty.trim());
        txtrej_qty.setText(xrej_qty.trim());
        txtreason.setText(xreason.trim());

        for (comman_model m: frm_quality_inspection.list) {
            String xitem1 = m.xcol2.trim();
            if(xitem1.equals(item)){
                txttotal_qty.setText("Qty As Per MRR : " + m.xcol4.trim());
            }
        }
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                for(comman_model m: frm_quality_inspection.list){
                    String xitem = m.xcol2.trim();
                    String xok_qty = m.xcol4.trim();
                    try {
                        String xbal = String.valueOf(Double.parseDouble(m.xcol4.trim()) - Double.parseDouble(txtrej_qty.getText().toString()));
                        if(xitem.equals(item) && Double.parseDouble(txtok_qty.getText().toString()) > Double.parseDouble(xok_qty))
                        {
                            txterror.setText("Ok Qty Is Invalid!!");
                            txterror.setVisibility(View.VISIBLE);
                            return;
                        }
                        if(Double.parseDouble(txtrej_qty.getText().toString()) > Double.parseDouble(txtok_qty.getText().toString()))
                        {
                            txterror.setText("Reject Qty Is Invalid!!");
                            txterror.setVisibility(View.VISIBLE);
                            return;
                        }
                        if(xitem.equals(item)) {
                            txtok_qty.setText(xbal);
                        }
                    }
                    catch (Exception e){
                        txterror.setText("Ok OR Rej Qty Is Invalid!!");
                        txterror.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                if(txtreason.getText().toString().trim().isEmpty()) {txtreason.setText("-");}
                frm_quality_inspection.model = new comman_model(xitem, txtok_qty.getText().toString(), txtrej_qty.getText().toString()
                        ,txtreason.getText().toString());
                frm_quality_inspection.list2.set(position, frm_quality_inspection.model);
                frm_quality_inspection.adapter2.notifyItemChanged(position);
//               frm_paper_end.adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }
}

