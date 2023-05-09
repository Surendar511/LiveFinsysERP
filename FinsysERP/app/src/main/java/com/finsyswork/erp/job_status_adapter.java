package com.finsyswork.erp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class job_status_adapter extends RecyclerView.Adapter<job_status_adapter.ViewHolder> {

    ArrayList<comman_model> list;
    Context context;
    public job_status_adapter(Context context, ArrayList<comman_model> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.job_status_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull job_status_adapter.ViewHolder holder, int position) {
        comman_model m = list.get(position);
        holder.txtcard_item.setText(m.xcol1);
        holder.txtcard_job_no.setText(m.xcol2);
        holder.txtcard_start.setText(m.xcol3);
        holder.txtcard_stop.setText(m.xcol4);
        holder.txtcard_job.setText(m.xcol5);
        holder.txtcard_job_qty.setText(m.xcol6);

        if(!holder.txtcard_start.getText().toString().contains("Waiting..."))
        {
            holder.txtcard_start.setTextColor(ContextCompat.getColor(context, R.color.success));
        }

        holder.txtcard_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fgen.xjob_status_call_count = 1;
                Intent intent = new Intent(context, frm_jobw_prod.class);
                intent.putExtra("item", holder.txtcard_item.getText().toString());
                intent.putExtra("job_no", holder.txtcard_job_no.getText().toString().split("---")[0]);
                intent.putExtra("job", holder.txtcard_job.getText().toString().split("---")[0]);
                intent.putExtra("qty", holder.txtcard_job_qty.getText().toString().trim());
                context.startActivity(intent);
            }
        });

        holder.txtcard_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("JOB STATUS");
                builder.setMessage("ARE YOU SURE,Do You Want To Start This Job ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final MyProgressdialog progressDialog = new MyProgressdialog(context);
                        progressDialog.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        String xcol1 = "";
                                        frm_lead_enquiry.xfrm_lead_enquiry = "Y";
                                        frm_lead_enquiry.xfg_sub_group_code = holder.txtcard_job.getText().toString().split("---")[0].trim();
                                        ArrayList<team> x = finapi.getApi(fgen.mcd, "aJOB_START","-", fgen.muname, fgen.cdt1.substring(6,10), frm_lead_enquiry.xfg_sub_group_code);
                                        for (team m: x) {
                                            xcol1 = m.getcol1();
                                            holder.txtcard_start.setText(xcol1.trim());
                                        }
                                        holder.txtcard_start.setTextColor(ContextCompat.getColor(context, R.color.success));
                                        progressDialog.dismiss();
                                    }
                                }, 100);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtcard_item, txtcard_job_no, txtcard_start, txtcard_stop, txtcard_job, txtcard_job_qty;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtcard_item = itemView.findViewById(R.id.txtcard_item);
            txtcard_job_no = itemView.findViewById(R.id.txtcard_job_no);
            txtcard_start = itemView.findViewById(R.id.txtcard_start);
            txtcard_stop = itemView.findViewById(R.id.txtcard_stop);
            txtcard_job = itemView.findViewById(R.id.txtcard_job);
            txtcard_job_qty = itemView.findViewById(R.id.txtcard_job_qty);
        }
    }
}
