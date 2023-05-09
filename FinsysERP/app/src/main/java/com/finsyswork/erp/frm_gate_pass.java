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

public class frm_gate_pass extends AppCompatActivity {

    Button btn_send, btn_new,btn_save, btn_cancel;
    private ImageView capturedImage, btncamera;
    Toolbar toolbar;
    EditText txtmobile, txtemployee, txtdepartment, txtdesignation, txtpurpose, txtclient_name, txttime;
    String xseperator = "#~#", xbranch="00", xtype ="1N";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_gate_pass);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_gate_pass.this;
        finapi.deleteJsonResponseFile();
        initializeViews();
        btn_cancel.setText("EXIT");

        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_gate_pass.this);
        Highlightborder();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_gate_pass.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String mobile = txtmobile.getText().toString();
                                frm_lead_enquiry.xcol6_udf_data = mobile;
                                finapi.fill_record_in_listview_popup("EP864");

                                txtemployee.setText(fgen.xpopup_col1);
                                txtdepartment.setText(fgen.xpopup_col2);
                                txtdesignation.setText(fgen.xpopup_col3);
                                txtpurpose.setText(fgen.xpopup_col4);
                                txtclient_name.setText(fgen.xpopup_col5);
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
                txttime.setText(finapi.currentTime());
                txtmobile.setBackgroundResource(R.color.light_blue);

                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);
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
                    txttime.setText("");
                    txtmobile.setBackgroundResource(R.color.light_grey);
                    capturedImage.setImageDrawable(null);
                    capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
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
                    fgen.showApiName(frm_gate_pass.this, "aVisitio_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_gate_pass.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_gate_pass.this);
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
                                if(txtemployee.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Please Fill Employee Name!!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(txtdepartment.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Please Fill Department!!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(txtdesignation.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Please Fill Desgination!!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(txtpurpose.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Please Fill Purpose Field!!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(txtclient_name.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Please Fill Client Name!!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    return;
                                }

                                String post_param = "";
                                String mobile = txtmobile.getText().toString();
                                String employee_name = txtemployee.getText().toString();
                                String department = txtdepartment.getText().toString();
                                String designation = txtdesignation.getText().toString();
                                String purpose = txtpurpose.getText().toString();
                                String client_name = txtclient_name.getText().toString();
                                String time = txttime.getText().toString();
                                post_param  = xbranch + xseperator + xtype + xseperator + mobile + xseperator
                                        + employee_name + xseperator + department + xseperator + designation
                                        + xseperator + purpose + xseperator + client_name + xseperator
                                        + time.trim()+ xseperator + "!~!~!";
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aVisitio_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP903");
                                boolean msg = finapi.showAlertMessage(frm_gate_pass.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(capturedImage.getDrawable().getConstantState() != getResources().getDrawable( R.drawable.erp).getConstantState()) {
                                    msg = finapi.sendImageToServer(imageInByte, frm_gate_pass.this);
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
        txtemployee.setEnabled(true);
        txtdepartment.setEnabled(true);
        txtdesignation.setEnabled(true);
        txtpurpose.setEnabled(true);
        txtclient_name.setEnabled(true);
        btn_send.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtmobile.setText("");
        txtemployee.setText("");
        txtdepartment.setText("");
        txtdesignation.setText("");
        txtpurpose.setText("");
        txtclient_name.setText("");
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
        txtemployee.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtdepartment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtdesignation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtclient_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
    }

    private void disableViewsMethod() {
        txtmobile.setEnabled(false);
        txtemployee.setEnabled(false);
        txtdepartment.setEnabled(false);
        txtdesignation.setEnabled(false);
        txtpurpose.setEnabled(false);
        txtclient_name.setEnabled(false);
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
        txtemployee = findViewById(R.id.txtemployee);
        txtdepartment = findViewById(R.id.txtdepartment);
        txtdesignation = findViewById(R.id.txtdesignation);
        txtpurpose = findViewById(R.id.txtpurpose);
        txtclient_name = findViewById(R.id.txtclient_name);
        txttime = findViewById(R.id.txttime);
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