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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class
frm_truck_driver extends AppCompatActivity {

    Button btn_save,btn_new, btn_cancel;
    ImageView img;
    EditText txtlocation, txtremarks, txttruck_departure, txtcustomer;
    TextView longitTextView, latitudeTextView, viewtruck_departure;
    RadioGroup radioGroup1, radioGroup2, radioGroup3;
    private ImageView capturedImage, btncamera;
    RadioButton radio_condition_of_goods_yes, radio_condition_of_goods_no, radio_delivery_service_yes, radio_delivery_service_no,
    radio_driver_attitude_yes, radio_driver_attitude_no;
    Toolbar toolbar;
    LinearLayout linear_customer;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    String ApiName = "", xmv_type = "";
    ArrayList<String> xtruck_departure = new ArrayList<>();
    ArrayList<String> xcustomer_destination = new ArrayList<>();
    LinearLayout linear_driver_attitude, linear_delivery_service, linear_condition_of_goods;
    String xseperator = "#~#", xbranch="00", xtype ="11";
    String xmv_location = "", mv_log_vehi = "", mv_log_drv="", mv_log_rmk=""
            , mv_log_supv="-", mv_log_cust="-", mv_log_prev="-";
    String xvehicle_orig_Str = "", xvehicle= "-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_truck_driver);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_truck_driver.this;
        finapi.deleteJsonResponseFile();


        initializeViews();
        setTextOnViews();
        finapi.setColorOfStatusBar(frm_truck_driver.this);
        check_form_request();
        disableViewsMethod();

       btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraKholo();
            }
        });

        txtremarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));capturedImage.setImageDrawable(null);
                    capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));
                }
                if(btn_text == "EXIT")
                {
                    finish();
                }
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_truck_driver.this, "aLogis_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_truck_driver.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(capturedImage.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.erp).getConstantState())
//                {
//                    Toast.makeText(getApplicationContext(), "Please, Click Image First!!", Toast.LENGTH_LONG).show();
//                    return;
//                }
                if(txttruck_departure.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select "+ viewtruck_departure.getText().toString() +" Field First", Toast.LENGTH_LONG).show();
                    return;
                }
                switch (fgen.frm_request)
                {
                    case "record_start_trip":
                        if(txtcustomer.getText().toString().isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Please Select Customer Field!!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        break;
                    default:
                        break;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_truck_driver.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = "";
                                xmv_location = txtlocation.getText().toString();
                                mv_log_vehi = txttruck_departure.getText().toString();
                                mv_log_drv = "-";
                                mv_log_rmk = txtremarks.getText().toString();
                                mv_log_supv= "";
                                mv_log_cust = txtcustomer.getText().toString();
                                mv_log_prev = xvehicle;
                                post_param = xbranch + xseperator + xmv_type + xseperator + xmv_location + xseperator + mv_log_vehi
                                        + xseperator + mv_log_drv + xseperator + mv_log_rmk + xseperator + mv_log_supv + xseperator
                                        + mv_log_cust + xseperator + mv_log_prev + "!~!~!";
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aLogis_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), fgen.btnid, "-");
                                boolean msg = finapi.showAlertMessage(frm_truck_driver.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(capturedImage.getDrawable().getConstantState() != getResources().getDrawable( R.drawable.erp).getConstantState()) {
                                    msg = finapi.sendImageToServer(imageInByte, frm_truck_driver.this);
    //                              if(!msg)
    //                              {
    //                                  Toast.makeText(getApplicationContext(), "Image Not Saved!!", Toast.LENGTH_LONG).show();
    //                                  return;
    //                              }
                                }
                                newBtnClickedMethod();
                                getLastLocation();
                                getNameFromLongitudeAndLatitude();
                                capturedImage.setImageDrawable(null);
                                capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));

                                switch (fgen.frm_request)
                                {
                                    case "record_start_trip":
                                        xtruck_departure = finapi.fill_record_in_listview_popup_for_truck("EP847");
                                        break;
                                    case "arrival_at_destination":
                                        xtruck_departure = finapi.fill_record_in_listview_popup_for_truck("EP848");
                                        break;
                                    case "unloading_started" :
                                        xtruck_departure = finapi.fill_record_in_listview_popup_for_truck("EP849");
                                        break;
                                    case "customer_feedback" :
                                        xtruck_departure = finapi.fill_record_in_listview_popup_for_truck("EP850");
                                        break;
                                    case "record_return_trip" :
                                        xtruck_departure = finapi.fill_record_in_listview_popup_for_truck("EP851");
                                        break;
                                    default:
                                        break;
                                }
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                                imageInByte = null;
                            }
                        }, 100);
//                capturedImage.setImageResource(R.drawable.erp);
            }
        });
        txttruck_departure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_truck_driver.this, xtruck_departure, "txttruck_departure");
            }
        });
    }

    Uri photoURI;
    void cameraKholo(){
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
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
                                photoURI = FileProvider.getUriForFile(frm_truck_driver.this,
                                        "com.finsyswork.android.fileprovider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, 1);
                            }
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
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


    private void disableViewsMethod() {
        txtlocation.setEnabled(false);
        txtremarks.setEnabled(false);
        radioGroup1.setEnabled(false);
        radioGroup2.setEnabled(false);
        radioGroup3.setEnabled(false);
        txtcustomer.setEnabled(false);
    }

    private void enabledViewMethod() {
        txtlocation.setEnabled(true);
        txtremarks.setEnabled(true);
        radioGroup1.setEnabled(true);
        radioGroup2.setEnabled(true);
        radioGroup3.setEnabled(true);
        txtcustomer.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtlocation.setText("");
        txtremarks.setText("");
        txtcustomer.setText("");
        txttruck_departure.setText("");
        radio_condition_of_goods_yes.setChecked(true);
        radio_delivery_service_yes.setChecked(true);
        radio_driver_attitude_yes.setChecked(true);
    }

    private void setTextOnViews() {
        btn_cancel.setText("EXIT");
    }

    private void initializeViews() {
        capturedImage = findViewById(R.id.imageResultx);
        btncamera = findViewById(R.id.btncamera);

        linear_condition_of_goods = findViewById(R.id.linear_condition_of_goods);
        linear_delivery_service = findViewById(R.id.linear_delivery_service);
        linear_driver_attitude = findViewById(R.id.linear_driver_attitude);
        linear_customer = findViewById(R.id.linear_customer);
        toolbar = findViewById(R.id.toolbar);
        btn_save = findViewById(R.id.btn_save);
        btn_new = findViewById(R.id.btn_new);
        btn_cancel = findViewById(R.id.btn_cancel);
        txtlocation = findViewById(R.id.txtlocation);
        txtremarks = findViewById(R.id.txtremarks);
        txtcustomer = findViewById(R.id.txtcustomer);
        txttruck_departure = findViewById(R.id.txttruck_departure);
        viewtruck_departure = findViewById(R.id.viewtruck_departure);
        img = findViewById(R.id.img);
        radio_condition_of_goods_yes = findViewById(R.id.radio_condition_of_goods_yes);
        radio_condition_of_goods_no = findViewById(R.id.radio_condition_of_goods_no);
        radio_delivery_service_yes = findViewById(R.id.radio_delivery_service_yes);
        radio_delivery_service_no = findViewById(R.id.radio_delivery_service_no);
        radio_driver_attitude_yes = findViewById(R.id.radio_driver_attitude_yes);
        radio_driver_attitude_no = findViewById(R.id.radio_driver_attitude_no);
        radioGroup1 = findViewById(R.id.radio_group1);
        radioGroup2 = findViewById(R.id.radio_group2);
        radioGroup3 = findViewById(R.id.radio_group3);
        longitTextView = findViewById(R.id.lonTextView);
        latitudeTextView = findViewById(R.id.latTextView);

        radio_driver_attitude_yes.setChecked(true);
        radio_delivery_service_yes.setChecked(true);
        radio_condition_of_goods_yes.setChecked(true);
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


    private void check_form_request() {
        switch (fgen.frm_request)
        {
            case "record_start_trip":
                ApiName = "aIssReq_ins";
                xmv_type = "21";
                toolbar.setTitle("Start Trip");
                linear_customer.setVisibility(View.VISIBLE);
                xtruck_departure = finapi.fill_record_in_listview_popup_for_truck("EP847");
                break;
            case "arrival_at_destination":
                xmv_type = "22";
                ApiName = "aissreq_ins";
                toolbar.setTitle("Arrival At Destination");
                viewtruck_departure.setText("Truck Start");
                txttruck_departure.setHint(R.string.truckstart);
                xtruck_departure = finapi.fill_record_in_listview_popup_for_truck("EP848");
                break;
            case "unloading_started" :
                xmv_type = "23";
                ApiName = "aPurReq_ins";
                toolbar.setTitle("Unloading Started");
                viewtruck_departure.setText("Truck Arrival");
                txttruck_departure.setHint(R.string.truckunload);
                xtruck_departure = finapi.fill_record_in_listview_popup_for_truck("EP849");
                break;
            case "customer_feedback" :
                xmv_type = "24";
                ApiName = "aPurReq_ins";
                toolbar.setTitle("Customer Feedback");
                viewtruck_departure.setText("Truck Unload");
                txttruck_departure.setHint(R.string.truckunload);
                linear_driver_attitude.setVisibility(View.VISIBLE);
                linear_delivery_service.setVisibility(View.VISIBLE);
                linear_condition_of_goods.setVisibility(View.VISIBLE);
                xtruck_departure = finapi.fill_record_in_listview_popup_for_truck("EP850");
                break;
            case "record_return_trip" :
                xmv_type = "25";
                ApiName = "aPurReq_ins";
                viewtruck_departure.setText("Returning Truck");
                txttruck_departure.setHint(R.string.truckreturning);
                toolbar.setTitle("Return Trip");
                xtruck_departure = finapi.fill_record_in_listview_popup_for_truck("EP851");
                break;
            default:
                break;
        }
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
        switch (fgen.frm_request)
        {
            case "record_start_trip":
                txtapi_code.setText("EP847");
                break;
            case "arrival_at_destination":
                txtapi_code.setText("EP848");
                break;
            case "unloading_started" :
                txtapi_code.setText("EP849");
                break;
            case "customer_feedback" :
                txtapi_code.setText("EP850");
                break;
            case "record_return_trip" :
                txtapi_code.setText("EP851");
                break;
            default:
                break;
        }

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
                String list_selected_text = adapter.getItem(position).toString();
                switch (controll)
                {
                    case "txttruck_departure" :
                        xvehicle_orig_Str = list_selected_text.toString();
                        xvehicle = xvehicle_orig_Str.substring(xvehicle_orig_Str.length() -23);
                        xvehicle_orig_Str = xvehicle_orig_Str.split("!~!~!")[0].trim();
                        txttruck_departure.setText(xvehicle_orig_Str);
                        txtcustomer.setText(list_selected_text.split("!~!~!")[1].trim());
                        break;
                    case "txtcustomer" : txtcustomer.setText(list_selected_text);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
    }
}