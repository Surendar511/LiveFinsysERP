package com.finsyswork.erp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class frm_lead_check_out extends AppCompatActivity {

    private Button btn_new,btn_save, btn_cancel, btn_location;
    TextView btn_lead;
    private TextView latitudeTextView, longitTextView;
    private EditText txtlocation, txtlead, txt_met_with_us, txtremarks, txtresult;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    Toolbar toolbar;
    private ImageView capturedImage, btncamera;
    ArrayList<String> leads_list = new ArrayList<>();
    Gson gson = new GsonBuilder().create();
    String xlead_chec_in = "";
    String xlead_orig_Str = "";
    String xmbranch="00", xmv_type="CO", xmv_sm_code="-", xmv_vis_lead="", xmv_vis_person=fgen.contactno, xmv_vis_remarks="-", xmv_vis_stage="-",
            xmv_vis_locn="", xseperator = "#~#";
    String xsaving_api = "aSmvisit_ins";
    String xpopup_api = "EP811A";
    LinearLayout linear_met_with, linear_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_lead_check_out);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_lead_check_out.this;
        finapi.deleteJsonResponseFile();

        if (!fgen.frm_request.equals("attendance_check_out"))
        {
            final MyProgressdialog progressDialog = new MyProgressdialog(frm_lead_check_out.this);
            progressDialog.show();
            new Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            leads_list = finapi.fill_record_in_listview_popup(xpopup_api);
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 100);
        }
        initializeViews();
        xcheck_form_req();
        finapi.setColorOfStatusBar(frm_lead_check_out.this);
        txtlocation.setKeyListener(null);
        txtlead.setKeyListener(null);
        btn_cancel.setText("EXIT");
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        disableViewsMethod();
        setSupportActionBar(toolbar);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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
                getLastLocation();
                getNameFromLongitudeAndLatitude();

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

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
                getNameFromLongitudeAndLatitude();
            }
        });


        txtlead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(frm_lead_check_out.this);
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
                txtapi_code.setText(xpopup_api);

                close_window.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

//                final ArrayAdapter<String> adapter = new ArrayAdapter<>(frm_lead_check_out.this, android.R.layout.simple_list_item_1, leads_list);
//                search_list.setAdapter(adapter);

                final SearchListAdapter adapter = new SearchListAdapter(frm_lead_check_out.this, leads_list);
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
//                        txtlead.setText(adapter.getItem(position));
                        xlead_orig_Str = adapter.getItem(position).toString();
                        xlead_chec_in = xlead_orig_Str.substring(xlead_orig_Str.length() - 23);
                        xlead_orig_Str = xlead_orig_Str.replace("---" + xlead_chec_in, "");
                        txtlead.setText(xlead_orig_Str);
                        dialog.dismiss();
                    }
                });
            }
        });

        txt_met_with_us.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        txtremarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtresult.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_lead_check_out.this, xsaving_api);
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_lead_check_out.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xmv_vis_locn = txtlocation.getText().toString();
                xmv_vis_lead = txtlead.getText().toString();
                if(!fgen.frm_request.equals("attendance_check_out")) {
                    xmv_vis_person = txt_met_with_us.getText().toString();
                }
                xmv_vis_remarks = txtremarks.getText().toString();
                xmv_vis_stage = txtresult.getText().toString();

                boolean xcheck_conditions = check_conditions();
                if(!xcheck_conditions)
                {
                    return;
                }

                final MyProgressdialog progressDialog = new MyProgressdialog(frm_lead_check_out.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = xmbranch + xseperator + xmv_type + xseperator + xmv_sm_code + xseperator + xmv_vis_lead
                                        + xseperator + xmv_vis_person + xseperator + xmv_vis_remarks + xseperator + xmv_vis_stage + xseperator
                                        + xmv_vis_locn + xseperator + xlead_chec_in + "!~!~!";
                                ArrayList<team> result = finapi.getApi(fgen.mcd, ""+xsaving_api,post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP902");
                                boolean msg = finapi.showAlertMessage(frm_lead_check_out.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(capturedImage.getDrawable().getConstantState() != getResources().getDrawable( R.drawable.erp).getConstantState()) {
                                    msg = finapi.sendImageToServer(imageInByte, frm_lead_check_out.this);
            //                      if(!msg)
            //                      {
            //                          Toast.makeText(getApplicationContext(), "Image Not Saved!!", Toast.LENGTH_LONG).show();
            //                          return;
            //                      }
                                }
                                capturedImage.setImageDrawable(null);
                                capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));
                                newBtnClickedMethod();
                                leads_list = finapi.fill_record_in_listview_popup(xpopup_api);
                                getLastLocation();
                                getNameFromLongitudeAndLatitude();
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                                imageInByte = null;
                            }
                        }, 100);
            }
        });
    }

    private void xcheck_form_req() {
        switch (fgen.frm_request)
        {
            case "attendance_check_out":
                xpopup_api = "EP811E";
                xsaving_api = "aEmpattn_ins";
                toolbar.setTitle("Attendance Check Out");
                btn_lead.setText("Sel. Attendance");
                txtlead.setHint(R.string.attendance);
                linear_met_with.setVisibility(View.GONE);
                linear_result.setVisibility(View.GONE);
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_lead_check_out.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                leads_list = finapi.fill_record_in_listview_popup(xpopup_api);
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 100);

                break;
            default:
                break;
        }
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


    private boolean check_conditions() {
        if(txtlocation.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select Location!!", Toast.LENGTH_LONG).show();
            txtlocation.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtlead.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select Lead!!", Toast.LENGTH_LONG).show();
            txtlead.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(!fgen.frm_request.equals("attendance_check_out")) {
            if (txt_met_with_us.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Fill Met With Field!!", Toast.LENGTH_LONG).show();
                txt_met_with_us.setBackgroundResource(R.color.light_blue);
                return false;
            }
            if (txtresult.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Fill Result Field!!", Toast.LENGTH_LONG).show();
                txtresult.setBackgroundResource(R.color.light_blue);
                return false;
            }
        }
        return true;
    }

    private void disableViewsMethod() {
        btn_save.setEnabled(false);
        btn_location.setEnabled(false);
        btn_lead.setEnabled(false);
        //txtlocation.setEnabled(false);
        //txtlead.setEnabled(false);
        txt_met_with_us.setEnabled(false);
        txtremarks.setEnabled(false);
        txtresult.setEnabled(false);
    }

    private void enabledViewMethod() {
        btn_location.setEnabled(true);
        btn_lead.setEnabled(true);
        txt_met_with_us.setEnabled(true);
        txtremarks.setEnabled(true);
        txtresult.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtlocation.setText("");
        txtlead.setText("");
        txtremarks.setText("");
        txtresult.setText("");
        txt_met_with_us.setText("");

        txtlocation.setBackgroundResource(R.color.light_grey);
        txtlead.setBackgroundResource(R.color.light_grey);
        txtremarks.setBackgroundResource(R.color.light_grey);
        txtresult.setBackgroundResource(R.color.light_grey);
        txt_met_with_us.setBackgroundResource(R.color.light_grey);
    }

    private void initializeViews() {
        capturedImage = findViewById(R.id.imageResultx);
        btncamera = findViewById(R.id.btncamera);

        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_location = findViewById(R.id.btn_location);
        btn_lead = findViewById(R.id.btn_lead);
        btn_cancel = findViewById(R.id.btn_cancel);
        txtlead = findViewById(R.id.txt_lead);
        txtlocation = findViewById(R.id.txt_location);
        longitTextView = findViewById(R.id.lonTextView);
        latitudeTextView = findViewById(R.id.latTextView);
        txt_met_with_us = findViewById(R.id.txt_met_with_us);
        toolbar = findViewById(R.id.toolbar);
        txtremarks = findViewById(R.id.txtremarks);
        txtresult = findViewById(R.id.txtresult);
        linear_met_with = findViewById(R.id.linear_met_with);
        linear_result = findViewById(R.id.linear_result);
    }

    private void getNameFromLongitudeAndLatitude(){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        Double lat;
        Double lon;

        lat = Double.parseDouble(latitudeTextView.getText().toString());
        lon = Double.parseDouble(longitTextView.getText().toString());
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            txtlocation.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            if(isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitudeTextView.setText(location.getLatitude() + "");
                            longitTextView.setText(location.getLongitude() + "");
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgen.frm_request ="";
    }
}