package com.finsyswork.erp;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class frm_visitor_entry extends AppCompatActivity {

    Button btn_send, btn_new,btn_save, btn_cancel;
    private ImageView capturedImage, btncamera;
    Toolbar toolbar;
    EditText txtmobile, txtvisitor_name, txtcompany_name, txtaddress, txtto_meet, txtpurpose, txttime, txtmat_entry;
    String xseperator = "#~#", xbranch="00", xtype ="1N";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_visitor_entry);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        fgen.xcard_view_name = "sale_order_schedule_booking_card_view";

        finapi.context = frm_visitor_entry.this;
        finapi.deleteJsonResponseFile();


        initializeViews();
        btn_cancel.setText("EXIT");
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_visitor_entry.this);
        Highlightborder();


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_visitor_entry.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String mobile = txtmobile.getText().toString();
                                frm_lead_enquiry.xcol6_udf_data = mobile;
                                finapi.fill_record_in_listview_popup("EP864");

                                txtvisitor_name.setText(fgen.xpopup_col1);
                                txtcompany_name.setText(fgen.xpopup_col2);
                                txtaddress.setText(fgen.xpopup_col3);
                                txtto_meet.setText(fgen.xpopup_col4);
                                txtpurpose.setText(fgen.xpopup_col5);
                                progressDialog.dismiss();
                            }
                        }, 100);

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
                txttime.setText(finapi.currentTime());
                txtmobile.setBackgroundResource(R.color.light_blue);
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

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));txttime.setText("");
                    txtmobile.setBackgroundResource(R.color.light_grey);
                    capturedImage.setImageDrawable(null);
                    capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));
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
                cameraKholo();
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_visitor_entry.this, "aVisitio_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_visitor_entry.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_visitor_entry.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                if(txtmobile.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Please Fill Mobile No.!!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(txtvisitor_name.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Please Fill Visitor Name!!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(txtcompany_name.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Please Fill Company Name!!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(txtaddress.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Please Fill Address!!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(txtto_meet.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Please Fill To Meet Detail!!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(txtpurpose.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Please Fill Purpose!!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    return;
                                }

                                String post_param = "";
                                String mobile = txtmobile.getText().toString();
                                String visitor_name = txtvisitor_name.getText().toString();
                                String company_name = txtcompany_name.getText().toString();
                                String address = txtaddress.getText().toString();
                                String to_meet = txtto_meet.getText().toString();
                                String purpose = txtpurpose.getText().toString();
                                String time = txttime.getText().toString();
                                String material_caried = txtmat_entry.getText().toString();
                                if(txtmat_entry.getText().toString().isEmpty()){material_caried = "-";}
                                post_param  = xbranch + xseperator + xtype + xseperator + mobile + xseperator
                                            + visitor_name + xseperator + company_name + xseperator + address
                                            + xseperator + to_meet + xseperator + purpose + xseperator
                                            + time.trim()+ xseperator + material_caried + "!~!~!";
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aVisitio_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP903");
                                boolean msg = finapi.showAlertMessage(frm_visitor_entry.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(capturedImage.getDrawable().getConstantState() != getResources().getDrawable( R.drawable.erp).getConstantState()) {
                                    msg = finapi.sendImageToServer(imageInByte, frm_visitor_entry.this);
                                    //                      if(!msg)
                                    //                      {
                                    //                          Toast.makeText(getApplicationContext(), "Image Not Saved!!", Toast.LENGTH_LONG).show();
                                    //                          return;
                                    //                      }
                                }
                                capturedImage.setImageDrawable(null);
                                capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));
                                newBtnClickedMethod();
                                txttime.setText(finapi.currentTime());
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }

    private void enabledViewMethod() {
        txtmobile.setEnabled(true);
        txtvisitor_name.setEnabled(true);
        txtcompany_name.setEnabled(true);
        txtaddress.setEnabled(true);
        txtto_meet.setEnabled(true);
        txtpurpose.setEnabled(true);
        btn_send.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtmobile.setText("");
        txtvisitor_name.setText("");
        txtcompany_name.setText("");
        txtaddress.setText("");
        txtto_meet.setText("");
        txtpurpose.setText("");
        txttime.setText("");
    }

    private void Highlightborder() {
        txtmobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }
                else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtvisitor_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }
                else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtcompany_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }
                else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtaddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }
                else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtto_meet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }
                else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtpurpose.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }
                else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txttime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }
                else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        txtmat_entry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                }
                else{
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
    }

    private void disableViewsMethod() {
        txtmobile.setEnabled(false);
        txtvisitor_name.setEnabled(false);
        txtcompany_name.setEnabled(false);
        txtaddress.setEnabled(false);
        txtto_meet.setEnabled(false);
        txtpurpose.setEnabled(false);
        btn_send.setEnabled(false);
    }

    private void initializeViews() {
        capturedImage = findViewById(R.id.imageResultx);
        btncamera = findViewById(R.id.btncamera);

        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_send = findViewById(R.id.btn_send);

        toolbar = findViewById(R.id.toolbar);
        txtmobile = findViewById(R.id.txtmobile);
        txtvisitor_name = findViewById(R.id.txtvisitor);
        txtcompany_name = findViewById(R.id.txtcompany_name);
        txtaddress = findViewById(R.id.txtaddress);
        txtto_meet = findViewById(R.id.txtmeet);
        txtpurpose = findViewById(R.id.txtpurpose);
        txttime = findViewById(R.id.txttime);
        txtmat_entry = findViewById(R.id.txtmat_caried);

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

}