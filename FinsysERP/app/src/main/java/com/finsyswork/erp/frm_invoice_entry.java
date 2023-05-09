package com.finsyswork.erp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class frm_invoice_entry extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener {

    Button btn_scan, btn_new,btn_save, btn_cancel, btn_sendMail;
    public static EditText txtscan, txtparty_name, txtaddress, txtinv_no, txtinv_dt, txtitem, txtqty, txtno_of_box;
    String xseperator = "#~#", xbranch="00", xtype ="EX";
    public static comman_adapter adapter;
    public static sqliteHelperClass handler;
    Toolbar toolbar;
    public static RecyclerView recyclerView;
    ImageView capturedImage, btncamera;
    public static ArrayList<comman_model> list = new ArrayList<>();
    final MyProgressdialog progressDialog = new MyProgressdialog(frm_invoice_entry.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_invoice_entry);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_invoice_entry.this;
        finapi.deleteJsonResponseFile();

        handler = new sqliteHelperClass(this);
        initializeViews();
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        btn_cancel.setText("EXIT");
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_invoice_entry.this);

        handler.clear_data();
        list = handler.comman_get_data();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new comman_adapter(getApplicationContext(), list);
        recyclerView.setAdapter(adapter);

//        txtinv_dt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDatePickerDialogue();
//            }
//        });

        txtscan.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == 19 || keyCode == 21)) {
                    if (txtscan.getText().toString().trim().length() > 2) {
                        fgen.frm_request = "frm_invoice_entry";
                        String scanned_text = ""+txtscan.getText().toString().trim();
                        progressDialog.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        handler.clear_data();
                                        list = handler.comman_get_data();
                                        adapter.notifyDataSetChanged();
                                        finapi.context = getApplicationContext();
                                        fgen.xcard_view_name = "sale_order_schedule_booking_card_view";
                                        frm_lead_enquiry.xfrm_lead_enquiry = "Y";
                                        frm_lead_enquiry.xfg_sub_group_code = scanned_text;
                                        ArrayList<String> x = finapi.fill_record_in_listview_popup("EP842D");
                                        frm_invoice_entry.txtscan.setText(scanned_text);
                                        progressDialog.dismiss();
                                    }
                                }, 100);
                        progressDialog.dismiss();
                        txtscan.setText("");
                        txtscan.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBtnClickedMethod();
                enabledViewMethod();
                btn_save.setEnabled(true);
                btn_new.setEnabled(false);
                btn_cancel.setText("CANCEL");

                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);

                txtscan.requestFocus();
                txtscan.setBackgroundResource(R.color.light_blue);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn_text = btn_cancel.getText().toString();
                if(btn_text == "CANCEL")
                {
                    newBtnClickedMethod();
                    disableViewsMethod();
                    btn_save.setEnabled(false);
                    btn_new.setEnabled(true);
                    btn_cancel.setText("EXIT");

                    handler.clear_data();
                    list = handler.comman_get_data();
                    adapter = new comman_adapter(getApplicationContext(), list);
                    recyclerView.setAdapter(adapter);
                    txtscan.setBackgroundResource(R.color.light_grey);

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                    capturedImage.setImageDrawable(null);
                    capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));
                    txtscan.requestFocus();
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });

        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtscan.requestFocus();
                cameraKholo();
                txtscan.requestFocus();
            }
        });


        btn_sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_mail();
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.clear_data();
                list = handler.comman_get_data();
                adapter.notifyDataSetChanged();
                fgen.frm_request = "frm_invoice_entry";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
                txtscan.requestFocus();
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_invoice_entry.this, "aInvchlOut_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_invoice_entry.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_invoice_entry.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                list = handler.comman_get_data();
                                if(list.size() <=0)
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Invalid Data!!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                String xparty_name = txtparty_name.getText().toString().split("---")[0].trim();
                                String xinv_no = txtinv_no.getText().toString();
                                String xinv_dt = txtinv_dt.getText().toString();
                                String xvehicle_no = txtaddress.getText().toString().trim();
                                ArrayList<comman_model> bullets = list;
                                String post_param = "";
                                for(comman_model b : bullets) {
                                    String xitem = b.xcol2.split("---")[0].trim();
                                    String xqty = b.xcol3;
                                    String xno_of_box = b.xcol4;
                                    if(xno_of_box.isEmpty())
                                    {
                                        xno_of_box = "-";
                                    }
                                    post_param  += xbranch + xseperator + xtype + xseperator + xparty_name + xseperator
                                            + xinv_no + xseperator + xinv_dt + xseperator + xitem
                                            + xseperator + xqty + xseperator + xno_of_box + xseperator + xvehicle_no +  "!~!~!";
                                }
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aInvchlOut_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP903");
                                boolean msg = finapi.showAlertMessage(frm_invoice_entry.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }

                                if(capturedImage.getDrawable().getConstantState() != getResources().getDrawable( R.drawable.erp).getConstantState()) {
                                    msg = finapi.sendImageToServer(imageInByte, frm_invoice_entry.this);
                                    //                      if(!msg)
                                    //                      {
                                    //                          Toast.makeText(getApplicationContext(), "Image Not Saved!!", Toast.LENGTH_LONG).show();
                                    //                          return;
                                    //                      }
                                }
                                capturedImage.setImageDrawable(null);
                                capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));
                                newBtnClickedMethod();
                                handler.clear_data();
                                list = handler.comman_get_data();
                                adapter = new comman_adapter(getApplicationContext(), list);
                                recyclerView.setAdapter(adapter);
                                txtscan.requestFocus();
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    private void showDatePickerDialogue() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    Uri photoURI;
    void cameraKholo(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.finsyswork.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private void send_mail()
    {
        String xparty = txtparty_name.getText().toString();
        String xvehicle = txtaddress.getText().toString();
        String xinv_no = txtinv_no.getText().toString();
        String xinv_dt = txtinv_dt.getText().toString();

        try {
            ArrayList<team> dt = finapi.getData_login(fgen.mcd, "select TRIM(PONO)||'!~!~!'||TRIM(PODATE) as col1,TRIM(DRV_NAME) as col2,TRIM(DRV_MOBILE) as col3, to_char(sysdate,'DD/MM/YYYY HH24:MI:SS') as col4, '-' as col5 from SALE where branchcd='" + fgen.branchcd + "' and trim(type)='" + txtscan.getText().toString().trim().substring(2, 4) + "' and trim(vchnum)='" + xinv_no.trim() + "' and to_char(vchdate, 'dd/mm/yyyy')='" + xinv_dt + "'");
            String xpono = dt.get(0).getcol1().split("!~!~!")[0];
            String xpodt = dt.get(0).getcol1().split("!~!~!")[1];
            String xdrv_name = dt.get(0).getcol2();
            String xdrv_mobile = dt.get(0).getcol3();
            String xcurr_dt = dt.get(0).getcol4();

            dt = finapi.getData_login(fgen.mcd, "select TRIM(a.EMAIL) as col1,, b.CPARTNO as col2,'-' as col3, '-' as col4, '-' as col5 from famst a, item b where trim(a.branchcd)='" + fgen.branchcd + "' and trim(a.acode)='" + xparty.split("---")[0].trim() + "' and a.branchcd=b.branchcd");
            Utils.EmailTo = dt.get(0).getcol1().toString();
            String xpart_no = dt.get(0).getcol2().toString();
            list = handler.comman_get_data();
            ArrayList<comman_model> bullets = list;
            String body = "<b>" + xparty.split("---")[1].trim() + "<b>" +
                    ", <br />" +
                    "<b> For your kind information below items has been despatched to you. </b>" +
                    "<br /><br /><table border='2px solid black' width ='100%'>" +
                    "<tr>" +
                        "<th>Invoice No.</th>" +
                        "<th>Invoice Dt.</th>" +
                        "<th>Po.No.</th>" +
                        "<th>Description</th>" +
                        "<th>Qty</th>" +
                        "<th>Gate Out Time</th>" +
                        "<th>Driver Name</th>" +
                        "<th>Driver Mob.No.</th>" +
                        "<th>Vehicle No.</th>" +
                    "</tr>";
            String tbl_rows = "";
            for (comman_model b : bullets) {
                String xitem = b.xcol2.trim();
                String xqty = b.xcol3;
                String xno_of_box = b.xcol4;
                tbl_rows =
                        "<tr>" +
                                "<td>" + xinv_no + "</td>" +
                                "<td>" + xinv_dt + "</td>" +
                                "<td>" + xpono + " Dt." + xpodt + "</td>" +
                                "<td>" + xitem.split("---")[1] + " <br/> ITEM CODE : " + xitem.split("---")[0] + " Part No. : " + xpart_no + "</td>" +
                                "<td>" + xqty + "</td>" +
                                "<td>" + xcurr_dt + "</td>" +
                                "<td>" + xdrv_name + "</td>" +
                                "<td>" + xdrv_mobile + "</td>" +
                                "<td>" + xvehicle + "</td>" +
                        "</tr>";
            }
            body = body + tbl_rows + "</table>" +
                    "<br />" +
                    "================================================= <br />" +
                    "<b>This Report is Auto generated from Finsys ERP. Please do not reply to this email/sender. <br />" +
                    "The above details are to be best of information and data available to the ERP system. <br />" +
                    "Errors or Omissions if any are regretted. <br />" +
                    "Thanks and Regards, <br />" +
                    fgen.mcd + "</b>";

            String result = finapi.SendEmail1(fgen.mcd, ""+Utils.EmailTo, "", "", Utils.SMTPHOST, Utils.FromEmail, "" + fgen.muid, Utils.FromEmailPassword, Integer.valueOf(Utils.EmailPortNo), true, "Material Gate Outward", body);
//            new AlertDialog.Builder(frm_invoice_entry.this)
//                    .setTitle("Sending Mail")
//                    .setMessage(result)-
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .show();
        }catch (Exception e){
            new AlertDialog.Builder(frm_invoice_entry.this)
                    .setTitle("Sending Mail Error!!")
                    .setMessage("Mail Not Sent Sucessfully!!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    })
                    .show();
            Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show();
        }
//                JavaMailAPI javaMailAPI = new JavaMailAPI(frm_invoice_entry.this, "shivamchauhanit@gmail.com", "Subject", "Mail Sent Sucessfully!!", Utils.SMTPHOST, Utils.EmailPortNo);
//                javaMailAPI.execute();
    }

    String currentPhotoPath = "";
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    byte[] imageInByte = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            capturedImage.setImageDrawable(null);
            // Get the dimensions of the View
            int targetW = capturedImage.getWidth();
            int targetH = capturedImage.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(currentPhotoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = finapi.rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = finapi.rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = finapi.rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            capturedImage.setImageBitmap(rotatedBitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
            imageInByte = stream.toByteArray();
        }
    }



    private void enabledViewMethod() {
        btn_scan.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtscan.setText("");
        txtparty_name.setText("");
        txtaddress.setText("");
        txtinv_no.setText("");
        txtinv_dt.setText("");
    }

    private void disableViewsMethod() {
        btn_scan.setEnabled(false);
    }

    private void initializeViews() {
        capturedImage = findViewById(R.id.imageResultx);
        btncamera = findViewById(R.id.btncamera);

        btn_new = findViewById(R.id.btn_new);
        btn_sendMail = findViewById(R.id.btn_sendMail);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_scan = findViewById(R.id.btn_scan);
        txtscan = findViewById(R.id.txtscan);
        txtparty_name = findViewById(R.id.txtparty_name);
        txtaddress = findViewById(R.id.txtaddress);
        txtinv_no = findViewById(R.id.txtinvoice_no);
        txtinv_dt = findViewById(R.id.txtinvoice_dt);
        recyclerView = findViewById(R.id.recycler);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgen.xcard_view_name = "";
        fgen.frm_request  = "";
    }

    @Override
    public void onDateSet(DatePicker view, int i, int i1, int i2) {

        String day = "";
        String month = "";
        if(String.valueOf(i2).length() == 1) {
            day = "0" + String.valueOf(i2);
        }
        else{
            day = String.valueOf(i2);
        }

        if(String.valueOf(i1).length() == 1) {
            month = "0" + String.valueOf(i1 + 1);
            if(i1 >=9)
            {
                month = String.valueOf(i1 + 1);
            }
        }
        else {
            month = String.valueOf(i1);
            if(i1 > 9)
            {
                month = String.valueOf(i1 + 1);
            }
        }
        String date = (day +"/"+ month +"/"+ i).toString();
        txtinv_dt.setText(date);
    }
}