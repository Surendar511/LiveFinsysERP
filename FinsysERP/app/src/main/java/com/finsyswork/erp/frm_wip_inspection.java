package com.finsyswork.erp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class frm_wip_inspection extends AppCompatActivity{

    EditText txtStage, txtMachine, txtJob_no, txtAction, txtlast_job_complaint, txtinstruction_to_follow ;
    Button btn_save,btn_new, btn_list, btn_cancel;
    LinearLayout linearLayout_make_costing, linearLayout_cust_type, linearLayout_fg_sub_group, linearLayout_fg_product, linearLayout_salesmanager, linearLayout_estimator
            , linear_tab1, linear_udf_data;
    Toolbar toolbar;
    String xbranch = "00", xtype="LR", xmv_stage,xmv_machine, xmv_job_no, xmv_action;
    String xseperator = "#~#";
    String list_selected_text = "";
    FloatingActionButton fab;
    public static String xfrm_clicked_tab = "";
    private ImageView  btncamera;
    private ImageView capturedImage;


    //==================== UDF Data  =====================

    public static TextView view1,view2,view3,view4,view5,view6,view7,view8,view9,view10,view11,view12,view13,view14,view15,view16,view17,
            view18,view19,view20;
    public static EditText textView1,textView2,textView3,textView4,textView5,textView6,textView7,textView8,textView9,textView10,textView11,
            textView12,textView13,textView14,textView15,textView16,textView17,textView18,textView19,textView20;
    public static LinearLayout linear_udf1,linear_udf2,linear_udf3,linear_udf4,linear_udf5,linear_udf6,linear_udf7,linear_udf8,linear_udf9,
            linear_udf10,linear_udf11,linear_udf12,linear_udf13,linear_udf14,linear_udf15,linear_udf16,linear_udf17,linear_udf18,
            linear_udf19,linear_udf20;
    public static  String xparty_popup_sel="N";
    String xtext1,xtext2,xtext3,xtext4,xtext5,xtext6,xtext7,xtext8,xtext9,xtext10,xtext11,xtext12,xtext13,xtext14,xtext15,xtext16,xtext17,xtext18,
            xtext19,xtext20;

    //===============================================================

    public static String xfrm_lead_enquiry = "";
    public  static String xfg_sub_group_code= "";
    public static ArrayList<String> xstage = new ArrayList<>();
    public static ArrayList<String> xmachine = new ArrayList<>();
    public static ArrayList<String> xjob_no = new ArrayList<>();
    public static ArrayList<String> xaction = new ArrayList<>();
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_wip_inspection);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_wip_inspection.this;
        finapi.deleteJsonResponseFile();


        fgen.frm_request = "";
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_wip_inspection.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        finapi.setColorOfStatusBar(frm_wip_inspection.this);
                        frm_wip_inspection.xstage = finapi.fill_record_in_listview_popup("EP820");
                        frm_wip_inspection.xmachine = finapi.fill_record_in_listview_popup("EP831");
                        frm_wip_inspection.xjob_no = finapi.fill_record_in_listview_popup("EP835");
                        frm_wip_inspection.xaction = finapi.fill_record_in_listview_popup("EP829");

                        new WipInspectionAsyncCaller().execute();
                        progressDialog.dismiss();
                    }
                }, 100);


        initializeViews();
        setTextOnViews();
        disableViewsMethod();
        setSupportActionBar(toolbar);

        Highlightborder();

        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        txtStage.setKeyListener(null);
        txtAction.setKeyListener(null);
        txtMachine.setKeyListener(null);
        txtJob_no.setKeyListener(null);


        txtStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_wip_inspection.this, xstage, "txtlead_source");
            }
        });

        txtMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_wip_inspection.this, xmachine, "txtindustry");
            }
        });

        txtJob_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_wip_inspection.this, xjob_no, "txtlead_probability");
            }
        });

        txtAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_wip_inspection.this, xaction, "txtcontact_level");
            }
        });

         btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraKholo();
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
                txtStage.setBackgroundResource(R.color.light_blue);
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
                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                    txtStage.setBackgroundResource(R.color.light_grey);
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i == 0) {
                    fab.setImageResource(R.drawable.left_arrow);
                    linear_tab1.setVisibility(View.GONE);
                    linear_udf_data.setVisibility(View.VISIBLE);

                    ArrayList<String> udf_text_views = frm_lead_enquiry.xudf_data_views;

                    for (String udf_views : udf_text_views)
                    {
                        String data = udf_views.split("---")[1].trim();
                        int index = Integer.parseInt(udf_views.split("---")[0].trim());
                        switch (index)
                        {
                            case 0:
                                frm_wip_inspection.linear_udf1.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view1.setText(data);
                                break;
                            case 1:
                                frm_wip_inspection.linear_udf2.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view2.setText(data);
                                break;
                            case 2:
                                frm_wip_inspection.linear_udf3.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view3.setText(data);
                                break;
                            case 3:
                                frm_wip_inspection.linear_udf4.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view4.setText(data);
                                break;
                            case 4:
                                frm_wip_inspection.linear_udf5.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view5.setText(data);
                                break;
                            case 5:
                                frm_wip_inspection.linear_udf6.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view6.setText(data);
                                break;
                            case 6:
                                frm_wip_inspection.linear_udf7.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view7.setText(data);
                                break;
                            case 7:
                                frm_wip_inspection.linear_udf8.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view8.setText(data);
                                break;
                            case 8:
                                frm_wip_inspection.linear_udf9.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view9.setText(data);
                                break;
                            case 9:
                                frm_wip_inspection.linear_udf10.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view10.setText(data);
                                break;
                            case 10:
                                frm_wip_inspection.linear_udf11.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view11.setText(data);
                                break;
                            case 11:
                                frm_wip_inspection.linear_udf12.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view12.setText(data);
                                break;
                            case 12:
                                frm_wip_inspection.linear_udf13.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view13.setText(data);
                                break;
                            case 13:
                                frm_wip_inspection.linear_udf14.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view14.setText(data);
                                break;
                            case 14:
                                frm_wip_inspection.linear_udf15.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view15.setText(data);
                                break;
                            case 15:
                                frm_wip_inspection.linear_udf16.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view16.setText(data);
                                break;
                            case 16:
                                frm_wip_inspection.linear_udf17.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view17.setText(data);
                                break;
                            case 17:
                                frm_wip_inspection.linear_udf18.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view18.setText(data);
                                break;
                            case 18:
                                frm_wip_inspection.linear_udf19.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view19.setText(data);
                                break;
                            case 19:
                                frm_wip_inspection.linear_udf20.setVisibility(View.VISIBLE);
                                frm_wip_inspection.view20.setText(data);
                                break;
                            default:
                                break;
                        }
                    }
                    i++;
                }
                else
                {
                    fab.setImageResource(R.drawable.right_arrow);
                    linear_tab1.setVisibility(View.VISIBLE);
                    linear_udf_data.setVisibility(View.GONE);
                    i--;
                }
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_wip_inspection.this, "aSlead_ins, afinudf_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_wip_inspection.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean xcheck_conditions = check_conditions();
                if(!xcheck_conditions)
                {
                    return;
                }

                final MyProgressdialog progressDialog = new MyProgressdialog(frm_wip_inspection.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                xmv_stage = txtStage.getText().toString();
                                xmv_machine = txtMachine.getText().toString();
                                xmv_job_no = txtJob_no.getText().toString();
                                xmv_action = txtAction.getText().toString();


                                //============UDF Data ===================================
                                xtext1 = textView1.getText().toString();
                                xtext2 = textView2.getText().toString();
                                xtext3 = textView3.getText().toString();
                                xtext4 = textView4.getText().toString();
                                xtext5 = textView5.getText().toString();
                                xtext6 = textView6.getText().toString();
                                xtext7 = textView7.getText().toString();
                                xtext8 = textView8.getText().toString();
                                xtext9 = textView9.getText().toString();
                                xtext10 = textView10.getText().toString();
                                xtext11 = textView11.getText().toString();
                                xtext12 = textView12.getText().toString();
                                xtext13 = textView13.getText().toString();
                                xtext14 = textView14.getText().toString();
                                xtext15 = textView15.getText().toString();
                                xtext16 = textView16.getText().toString();
                                xtext17 = textView17.getText().toString();
                                xtext18 = textView18.getText().toString();
                                xtext19 = textView19.getText().toString();
                                xtext20 = textView20.getText().toString();
                                //========================================================

                                String post_param = xbranch + xseperator + xtype + xseperator + xmv_stage
                                        + xseperator + xmv_machine + xseperator + xmv_job_no + xseperator + xmv_action
                                        + "!~!~!";

                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aSlead_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                boolean msg = finapi.showAlertMessageForMultipleSaving(frm_wip_inspection.this, result);
                                if(!msg)
                                {
                                    progressDialog.dismiss();
                                    return;
                                }
                                String my_doc_no="";
                                my_doc_no= fgen.xpopup_col2;
                                post_param = my_doc_no + xseperator + view1.getText().toString() + xseperator + xtext1 +  "!~!~!"
                                        + my_doc_no + xseperator + view2.getText().toString() + xseperator + xtext2 +  "!~!~!"
                                        + my_doc_no + xseperator + view3.getText().toString() + xseperator + xtext3 +  "!~!~!"
                                        + my_doc_no + xseperator + view4.getText().toString() + xseperator + xtext4 +  "!~!~!"
                                        + my_doc_no + xseperator + view5.getText().toString() + xseperator + xtext5 +  "!~!~!"
                                        + my_doc_no + xseperator + view6.getText().toString() + xseperator + xtext6 +  "!~!~!"
                                        + my_doc_no + xseperator + view7.getText().toString() + xseperator + xtext7 +  "!~!~!"
                                        + my_doc_no + xseperator + view8.getText().toString() + xseperator + xtext8 +  "!~!~!"
                                        + my_doc_no + xseperator + view9.getText().toString() + xseperator + xtext9 +  "!~!~!"
                                        + my_doc_no + xseperator + view10.getText().toString() + xseperator + xtext10 +  "!~!~!"
                                        + my_doc_no + xseperator + view11.getText().toString() + xseperator + xtext11 +  "!~!~!"
                                        + my_doc_no + xseperator + view12.getText().toString() + xseperator + xtext12 +  "!~!~!"
                                        + my_doc_no + xseperator + view13.getText().toString() + xseperator + xtext13 +  "!~!~!"
                                        + my_doc_no + xseperator + view14.getText().toString() + xseperator + xtext14 +  "!~!~!"
                                        + my_doc_no + xseperator + view15.getText().toString() + xseperator + xtext15 +  "!~!~!"
                                        + my_doc_no + xseperator + view16.getText().toString() + xseperator + xtext16 +  "!~!~!"
                                        + my_doc_no + xseperator + view17.getText().toString() + xseperator + xtext17 +  "!~!~!"
                                        + my_doc_no + xseperator + view18.getText().toString() + xseperator + xtext18 +  "!~!~!"
                                        + my_doc_no + xseperator + view19.getText().toString() + xseperator + xtext19 +  "!~!~!"
                                        + my_doc_no + xseperator + view20.getText().toString() + xseperator + xtext20 +  "!~!~!";

                                result = finapi.getApi(fgen.mcd, "afinudf_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                msg = finapi.showAlertMessage(frm_wip_inspection.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                fgen.xpopup_col2 = "";
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgen.frm_request = "";
        xparty_popup_sel = "N";
        xfrm_clicked_tab = "";
    }

    private boolean check_conditions() {
        if(txtStage.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select Stage!!", Toast.LENGTH_LONG).show();
            txtStage.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtMachine.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select Machine!!", Toast.LENGTH_LONG).show();
            txtMachine.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtJob_no.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select Job No.!!", Toast.LENGTH_LONG).show();
            txtJob_no.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtAction.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select Action Req.!!", Toast.LENGTH_LONG).show();
            txtAction.setBackgroundResource(R.color.light_blue);
            return false;
        }
        return true;
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

            Picasso.with(this)
                    .load(new File(currentPhotoPath))
                    .into(capturedImage);

//            capturedImage.setImageBitmap(rotatedBitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
            imageInByte = stream.toByteArray();
        }
    }


    private void initializeViews() {
        fab = findViewById(R.id.fab);
        linear_tab1 = findViewById(R.id.lin);
        linear_udf_data = findViewById(R.id.linear_udf_data);
        linearLayout_cust_type = findViewById(R.id.linear_condition_of_cust_type);
        linearLayout_make_costing = findViewById(R.id.linear_condition_of_make_costing);
        linearLayout_fg_sub_group = findViewById(R.id.linear_fg_sub_group);
        linearLayout_fg_product = findViewById(R.id.linear_fg_product);
        linearLayout_salesmanager = findViewById(R.id.linear_sales_manager);
        linearLayout_estimator = findViewById(R.id.linear_estimator);
        toolbar = findViewById(R.id.toolbar);
        txtStage = findViewById(R.id.txtStage);
        txtMachine = findViewById(R.id.txtMachine);
        txtJob_no = findViewById(R.id.txtJob_no);
        txtAction = findViewById(R.id.txtAction);
        btn_save = findViewById(R.id.btn_save);
        btn_new = findViewById(R.id.btn_new);
        btn_list = findViewById(R.id.btn_list);
        btn_cancel = findViewById(R.id.btn_cancel);
        txtlast_job_complaint = findViewById(R.id.txtlast_job_complaint);
        txtinstruction_to_follow = findViewById(R.id.txtinst_to_follow);
        capturedImage = findViewById(R.id.imageResultx);
        btncamera = findViewById(R.id.btncamera);



        //==================== UDF Data=======================
        linear_udf1 = findViewById(R.id.linear_udf1);
        linear_udf2 = findViewById(R.id.linear_udf2);
        linear_udf3 = findViewById(R.id.linear_udf3);
        linear_udf4 = findViewById(R.id.linear_udf4);
        linear_udf5 = findViewById(R.id.linear_udf5);
        linear_udf6 = findViewById(R.id.linear_udf6);
        linear_udf7 = findViewById(R.id.linear_udf7);
        linear_udf8 = findViewById(R.id.linear_udf8);
        linear_udf9 = findViewById(R.id.linear_udf9);
        linear_udf10 = findViewById(R.id.linear_udf10);
        linear_udf11 = findViewById(R.id.linear_udf11);
        linear_udf12 = findViewById(R.id.linear_udf12);
        linear_udf13 = findViewById(R.id.linear_udf13);
        linear_udf14 = findViewById(R.id.linear_udf14);
        linear_udf15 = findViewById(R.id.linear_udf15);
        linear_udf16 = findViewById(R.id.linear_udf16);
        linear_udf17 = findViewById(R.id.linear_udf17);
        linear_udf18 = findViewById(R.id.linear_udf18);
        linear_udf19 = findViewById(R.id.linear_udf19);
        linear_udf20 = findViewById(R.id.linear_udf20);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);
        view6 = findViewById(R.id.view6);
        view7 = findViewById(R.id.view7);
        view8 = findViewById(R.id.view8);
        view9 = findViewById(R.id.view9);
        view10 = findViewById(R.id.view10);
        view11 = findViewById(R.id.view11);
        view12 = findViewById(R.id.view12);
        view13 = findViewById(R.id.view13);
        view14 = findViewById(R.id.view14);
        view15 = findViewById(R.id.view15);
        view16 = findViewById(R.id.view16);
        view17 = findViewById(R.id.view17);
        view18 = findViewById(R.id.view18);
        view19 = findViewById(R.id.view19);
        view20 = findViewById(R.id.view20);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
        textView9 = findViewById(R.id.textView9);
        textView10 = findViewById(R.id.textView10);
        textView11 = findViewById(R.id.textView11);
        textView12 = findViewById(R.id.textView12);
        textView13 = findViewById(R.id.textView13);
        textView14 = findViewById(R.id.textView14);
        textView15 = findViewById(R.id.textView15);
        textView16 = findViewById(R.id.textView16);
        textView17 = findViewById(R.id.textView17);
        textView18 = findViewById(R.id.textView18);
        textView19 = findViewById(R.id.textView19);
        textView20 = findViewById(R.id.textView20);
    }

    private void newBtnClickedMethod() {
        txtStage.setText("");
        txtMachine.setText("");
        txtJob_no.setText("");
        txtAction.setText("");
        txtlast_job_complaint.setText("");
        txtinstruction_to_follow.setText("");

        textView1.setText("");
        textView2.setText("");
        textView3.setText("");
        textView4.setText("");
        textView5.setText("");
        textView6.setText("");
        textView7.setText("");
        textView8.setText("");
        textView9.setText("");
        textView10.setText("");
        textView11.setText("");
        textView12.setText("");
        textView13.setText("");
        textView14.setText("");
        textView15.setText("");
        textView16.setText("");
        textView17.setText("");
        textView18.setText("");
        textView19.setText("");
        textView20.setText("");
    }

    private void enabledViewMethod() {
        txtStage.setEnabled(true);
        txtMachine.setEnabled(true);
        txtJob_no.setEnabled(true);
        txtAction.setEnabled(true);
        txtlast_job_complaint.setEnabled(true);
        txtinstruction_to_follow.setEnabled(true);
    }

    private void disableViewsMethod() {
        txtStage.setEnabled(false);
        txtMachine.setEnabled(false);
        txtJob_no.setEnabled(false);
        txtAction.setEnabled(false);
        txtlast_job_complaint.setEnabled(false);
        txtinstruction_to_follow.setEnabled(false);
    }

    private void setTextOnViews() {
        btn_cancel.setText("EXIT");
    }

    private void Highlightborder() {
        txtStage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtMachine.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtJob_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtAction.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });


        //================= UDF Data ========================

        textView1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });

        textView2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView6.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView7.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView8.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView9.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView10.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView11.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView12.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView13.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView14.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView15.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView16.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView17.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView18.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView18.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView19.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        textView20.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
    }


    public void ShowDialogBox(Context context, ArrayList<String> list, String controll){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.search_lead);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 1.0f);
        int dialogWindowHeight = (int) (displayHeight * 1.0f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setLayout(layoutParams.width, layoutParams.height);
        dialog.show();

        EditText search_text = dialog.findViewById(R.id.txtsearch);
        ListView search_list = dialog.findViewById(R.id.seach_list);
        ImageView close_window = dialog.findViewById(R.id.close_window);

        TextView txtapi_code = dialog.findViewById(R.id.txtapi_code);
        if(controll.equals("txtcountry_name")) txtapi_code.setText("EP824");
        if(controll.equals("txtfg_product")) txtapi_code.setText("EP817");
        if(controll.equals("txtstate_name")) txtapi_code.setText("EP825");
        if(controll.equals("txtfg_sub_group")) txtapi_code.setText("EP823");
        if(controll.equals("txtparty_name")) txtapi_code.setText("EP815");
        if(controll.equals("txtlead_source")) txtapi_code.setText("EP826");
        if(controll.equals("txtindustry")) txtapi_code.setText("EP827");
        if(controll.equals("txtlead_probability")) txtapi_code.setText("EP828");
        if(controll.equals("txtcontact_level")) txtapi_code.setText("EP829");
        if(controll.equals("txtestimator")) txtapi_code.setText("EP832B");

        close_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
//        search_list.setAdapter(adapter);
        final SearchListAdapter adapter = new SearchListAdapter(context, list);
        search_list.setAdapter(adapter);


        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list_selected_text = adapter.getItem(position).toString();
                switch (controll)
                {
                    case "txtStage" : txtStage.setText(list_selected_text);
                        break;
                    case "txtMachine" : txtMachine.setText(list_selected_text);
                        break;
                    case "txtJob_no" : txtJob_no.setText(list_selected_text);
                        break;
                    case "txtAction" : txtAction.setText(list_selected_text);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });

    }
}


class WipInspectionAsyncCaller extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread
        fgen.xpopup_col2 = "";
        frm_lead_enquiry.xudf_data_views = new ArrayList<>();

    }
    @Override
    protected Void doInBackground(Void... params) {
        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
        frm_wip_inspection.xfrm_clicked_tab = "";
        frm_lead_enquiry.xcol6_udf_data = "F45101";
        frm_lead_enquiry.xudf_data_views = finapi.fill_record_in_listview_popup("EP860");
        frm_lead_enquiry.xcol6_udf_data = "";
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
    }
}


