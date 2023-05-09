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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class frm_lead_sarfaraz extends AppCompatActivity {

    private Button btn_new,btn_save, btn_cancel, btn_location;
    TextView btn_lead;
    private TextView latitudeTextView, longitTextView;
    private EditText txtlocation, txtlead, txtremarks, txtresult;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    private ImageView capturedImage, btncamera;
    Toolbar toolbar;
    ArrayList<String> leads_list;

    String xmbranch="00", xmv_type="CI", xmv_sm_code="-", xmv_vis_lead="", xmv_vis_person=fgen.contactno, xmv_vis_remarks="", xmv_vis_stage="",
    xmv_vis_locn="", xseperator = "#~#";
    String xsaving_api = "aSmvisit_ins";
    String xpopup_api = "EP814";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_lead_sarfaraz);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);


        finapi.context = frm_lead_sarfaraz.this;
        finapi.deleteJsonResponseFile();

        final MyProgressdialog progressDialog = new MyProgressdialog(frm_lead_sarfaraz.this);
            progressDialog.show();
            new Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            fill_leads_in_listview(xpopup_api);
                            // On complete call either onLoginSuccess or onLoginFailed
                            progressDialog.dismiss();
                        }
                    }, 100);


        initializeViews();

        xcheck_form_req();

        btn_cancel.setText("EXIT");

        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        txtlocation.setKeyListener(null);
        txtlead.setKeyListener(null);
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_lead_sarfaraz.this);

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

                txtlead.setBackgroundResource(R.color.light_blue);
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

                    txtlead.setBackgroundResource(R.color.light_grey);
                    txtremarks.setBackgroundResource(R.color.light_grey);
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
                final Dialog dialog = new Dialog(frm_lead_sarfaraz.this);
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


//                final ArrayAdapter<String> adapter = new ArrayAdapter<>(frm_lead_sarfaraz.this, android.R.layout.simple_list_item_1, leads_list);
//                search_list.setAdapter(adapter);

                final SearchListAdapter adapter = new SearchListAdapter(frm_lead_sarfaraz.this, leads_list);
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
                        txtlead.setText(adapter.getItem(position).toString());
                        dialog.dismiss();
                    }
                });
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_lead_sarfaraz.this, xsaving_api);
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_lead_sarfaraz.this);
                return true;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xmv_vis_locn = txtlocation.getText().toString();
                xmv_vis_lead = txtlead.getText().toString();
                xmv_vis_remarks = txtremarks.getText().toString();
                xmv_vis_stage = txtresult.getText().toString();

                boolean xcheck_conditions = check_conditions();
                if(!xcheck_conditions)
                {
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_lead_sarfaraz.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = xmbranch + xseperator + xmv_type + xseperator + xmv_sm_code + xseperator + xmv_vis_lead
                                        + xseperator + xmv_vis_person + xseperator + xmv_vis_remarks + xseperator + xmv_vis_stage + xseperator
                                        + xmv_vis_locn + "!~!~!";
                                ArrayList<team> result = finapi.getApi(fgen.mcd, xsaving_api,post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "EP901");
                                boolean msg = finapi.showAlertMessage(frm_lead_sarfaraz.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(capturedImage.getDrawable().getConstantState() != getResources().getDrawable( R.drawable.erp).getConstantState()) {
                                    msg = finapi.sendImageToServer(imageInByte, frm_lead_sarfaraz.this);
                //                 if(!msg)
                //                 {
                //                    Toast.makeText(getApplicationContext(), "Image Not Saved!!", Toast.LENGTH_LONG).show();
                //                    return;
                //                 }
                                }
                                capturedImage.setImageDrawable(null);
                                capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));
                                newBtnClickedMethod();
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
            case "attendance_check_in":
                xpopup_api = "EP814DA";
                toolbar.setTitle("Attendance Check In");
                btn_lead.setText("Sel. Attendance");
                txtlead.setHint(R.string.attendance);
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_lead_sarfaraz.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                fill_leads_in_listview(xpopup_api);
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 100);
                xsaving_api = "aEmpattn_ins";
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
        if(txtremarks.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Fill Remarks!!", Toast.LENGTH_LONG).show();
            txtremarks.setBackgroundResource(R.color.light_blue);
            return false;
        }
        return true;
    }


    private void disableViewsMethod() {
        btn_save.setEnabled(false);
        btn_location.setEnabled(false);
        btn_lead.setEnabled(false);
        //txtlocation.setEnabled(false);
        //txtlead.setEnabled(false);
        txtremarks.setEnabled(false);
        txtresult.setEnabled(false);
    }

    private void enabledViewMethod() {
        btn_location.setEnabled(true);
        btn_lead.setEnabled(true);
        txtremarks.setEnabled(true);
        txtresult.setEnabled(true);

    }

    private void newBtnClickedMethod() {
        txtlocation.setText("");
        txtlead.setText("");
        txtremarks.setText("");
        txtresult.setText("");
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
        txtremarks = findViewById(R.id.txtremarks);
        txtresult = findViewById(R.id.txtresult);
        longitTextView = findViewById(R.id.lonTextView);
        latitudeTextView = findViewById(R.id.latTextView);
        toolbar = findViewById(R.id.toolbar);
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

            // check if location is enabled
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
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
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

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
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

    // If everything is alright then
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


    private void fill_leads_in_listview(String popup_api){
        leads_list = new ArrayList<>();
        ArrayList<team> result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins",popup_api, fgen.cdt1, fgen.cdt2, fgen.muname, "-");

        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        String leads_code = "";
        String leads_name = "";

        try {
            JSONArray array = new JSONArray(listString);

            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                String data = respObj.getString("col2");
                Log.d("allData  ", data);
                String[] arrOfStr = data.split("!~!~!");
                int x=0;
                for (String col2 : arrOfStr){
                    Log.d("allData  ", col2);
                    if(x==0) {
                        leads_code = col2;
                    }
                    if(x==2) {
                        leads_name = col2;
                        leads_list.add(leads_code + "---" + leads_name);
                    }

                    x +=1;
                }
                Log.d("addData ", "Success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgen.frm_request ="";
    }

}
