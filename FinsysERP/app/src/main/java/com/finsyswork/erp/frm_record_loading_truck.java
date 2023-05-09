package com.finsyswork.erp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.AsyncTask;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class frm_record_loading_truck extends AppCompatActivity {
    Button btn_save,btn_new, btn_cancel;
    public static EditText txtarrived_truck, txtloading_supervisor, txtlocation, txtcustomer;
    public static TextView longitTextView, latitudeTextView, txtviewCustomer;
    private ImageView capturedImage, btncamera;
    RadioButton radio_y, radio_n;
    Toolbar toolbar;
    FusedLocationProviderClient mFusedLocationClient;
    public static truch_loading_adapter adapter;
    int PERMISSION_ID = 44;
    public static ArrayList<String> xsel_truck_arrived = new ArrayList<>();
    public static ArrayList<String> xcustomer_destination = new ArrayList<>();
    public static ArrayList<String> xloading_supervisor = new ArrayList<>();
    public static String xseperator = "#~#", xbranch="00", xtype ="12";
    String xmv_location = "", mv_log_vehi = "", mv_log_drv="-", mv_log_rmk="-"
            , mv_log_supv="-", mv_log_cust="-", mv_log_prev="-";
    String xvehicle_orig_Str = "", xvehicle= "-";
    public static sqliteHelperClass handler;
    RecyclerView recyclerView;

    public static ArrayList<expense_model> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_record_loading_truck);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_record_loading_truck.this;
        finapi.deleteJsonResponseFile();

        //EP482  Incomming Vehicle Un-Loading Start
        //EP483  Incomming Vehicle Un-Loading End

        initializeViews();
        setTextOnViews();
        finapi.setColorOfStatusBar(frm_record_loading_truck.this);
        disableViewsMethod();
        handler = new sqliteHelperClass(this);
        handler.clear_data();
        list = handler.record_expense_get_data();

        toolbar.setTitle(fgen.rptheader);
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new truch_loading_adapter(getApplicationContext());
        recyclerView.setAdapter(adapter);

        final MyProgressdialog progressDialog = new MyProgressdialog(frm_record_loading_truck.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        new Thread(new Runnable() {
                            public void run() {
                                frm_lead_enquiry.xfrm_lead_enquiry = "N";
                                if(fgen.btnid.equals("EP482")){
                                    frm_record_loading_truck.xtype = "52";
                                    frm_record_loading_truck.txtviewCustomer.setText("Supplier");
                                    frm_record_loading_truck.xsel_truck_arrived = finapi.fill_record_in_listview_popup("EP845_IVLB");
                                    frm_record_loading_truck.xcustomer_destination = finapi.fill_record_in_listview_popup_for_truck_loading("EP815_IV");
                                }
                                else if(fgen.btnid.equals("EP483")){
                                    frm_record_loading_truck.xtype = "53";
                                    frm_record_loading_truck.txtviewCustomer.setText("Supplier");
                                    frm_record_loading_truck.xsel_truck_arrived = finapi.fill_record_in_listview_popup("EP846_IVLE");
                                    frm_record_loading_truck.xcustomer_destination = finapi.fill_record_in_listview_popup_for_truck_loading("EP815_IV");
                                }else {
                                    frm_record_loading_truck.xsel_truck_arrived = finapi.fill_record_in_listview_popup("EP845");
                                    frm_record_loading_truck.xcustomer_destination = finapi.fill_record_in_listview_popup_for_truck_loading("EP815");
                                }
                                new AsyncCaller_truck_loading().execute();
                            }
                        }).start();
                        // On complete call either onLoginSuccess or onLoginFailed
                        progressDialog.dismiss();
                    }
                }, 100);

        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraKholo();
            }
        });

        txtloading_supervisor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

                try {
                    getLastLocation();
                    getNameFromLongitudeAndLatitude();
                }catch (Exception e){}
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

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));handler.clear_data();
                    list = handler.record_expense_get_data();
                    adapter.notifyDataSetChanged();
                    capturedImage.setImageDrawable(null);
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
                    fgen.showApiName(frm_record_loading_truck.this, "aLogis_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_record_loading_truck.this);
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
                if(txtarrived_truck.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please, Select Vehicle!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtloading_supervisor.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please, Select Loading Supervisor!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(list.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please, Customer/Destination Field!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_record_loading_truck.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String post_param = "";
                                xmv_location = txtlocation.getText().toString();
                                mv_log_vehi = txtarrived_truck.getText().toString();
                                mv_log_drv = "-";
                                mv_log_rmk = "-";
                                mv_log_supv= txtloading_supervisor.getText().toString();
                                mv_log_cust = txtcustomer.getText().toString();
                                mv_log_prev = xvehicle;

                                String xcustomer_save= "";
                                String xdestination_save= "";
                                ArrayList<expense_model> bullets = list;
                                for(expense_model b : bullets) {
                                    xcustomer_save = b.expense;
                                    xdestination_save = b.amount;
                                    post_param += xbranch + xseperator + xtype + xseperator + xmv_location + xseperator + mv_log_vehi
                                            + xseperator + mv_log_drv + xseperator + mv_log_rmk + xseperator + mv_log_supv + xseperator
                                            + xcustomer_save + xseperator + mv_log_prev + xseperator + xdestination_save + "-" + "-" + "!~!~!";
                                }
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aLogis_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), fgen.btnid, "-");
                                boolean msg = finapi.showAlertMessage(frm_record_loading_truck.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                if(capturedImage.getDrawable().getConstantState() != getResources().getDrawable( R.drawable.erp).getConstantState()) {
                                    msg = finapi.sendImageToServer(imageInByte, frm_record_loading_truck.this);
                                    //                              if(!msg
                                    //                              {
                                    //                                  Toast.makeText(getApplicationContext(), "Image Not Saved!!", Toast.LENGTH_LONG).show();
                                    //                                  return;
                                    //                              }
                                }
                                xsel_truck_arrived = finapi.fill_record_in_listview_popup("EP845");
                                handler.clear_data();
                                list = handler.record_expense_get_data();
                                adapter.notifyDataSetChanged();
                                newBtnClickedMethod();
                                getLastLocation();
                                getNameFromLongitudeAndLatitude();
                                capturedImage.setImageDrawable(null);
                                capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                                imageInByte = null;
                            }
                        }, 100);
            }
        });

        txtarrived_truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_record_loading_truck.this, xsel_truck_arrived, "txtarrived_truck");
            }
        });

        txtcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_record_loading_truck.this, xcustomer_destination, "txtcustomer");
            }
        });

        txtloading_supervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_record_loading_truck.this, xloading_supervisor, "txtloading_supervisor");
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
                                photoURI = FileProvider.getUriForFile(frm_record_loading_truck.this,
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


    private void enabledViewMethod() {
        radio_y.setEnabled(true);
        radio_n.setEnabled(true);
        txtarrived_truck.setEnabled(true);
        txtloading_supervisor.setEnabled(true);
        txtcustomer.setEnabled(true);
    }

    private void disableViewsMethod() {
        radio_y.setEnabled(false);
        radio_n.setEnabled(false);
        txtarrived_truck.setEnabled(false);
        txtloading_supervisor.setEnabled(false);
        txtcustomer.setEnabled(false);
    }

    private void newBtnClickedMethod() {
        txtlocation.setText("");
        txtcustomer.setText("");
        txtloading_supervisor.setText("");
        radio_y.setChecked(true);
        txtarrived_truck.setText("");
    }

    private void setTextOnViews() {
        btn_cancel.setText("EXIT");
    }

    private void initializeViews() {
        capturedImage = findViewById(R.id.imageResultx);
        btncamera = findViewById(R.id.btncamera);

        toolbar = findViewById(R.id.toolbar);
        btn_save = findViewById(R.id.btn_save);
        btn_new = findViewById(R.id.btn_new);
        btn_cancel = findViewById(R.id.btn_cancel);
        txtarrived_truck = findViewById(R.id.txttruck_arrived);
        txtlocation = findViewById(R.id.txtlocation);
        txtcustomer = findViewById(R.id.txtcustomer);
        radio_y = findViewById(R.id.radio_yes);
        radio_n = findViewById(R.id.radio_no);
        txtloading_supervisor = findViewById(R.id.txtloading_supervisor);
        longitTextView = findViewById(R.id.lonTextView);
        latitudeTextView = findViewById(R.id.latTextView);
        txtviewCustomer = findViewById(R.id.txtviewCustomer);
        recyclerView = findViewById(R.id.recycler);

        radio_y.setChecked(true);
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
        if(fgen.btnid.equals("EP482")){
            if (controll.equals("txtarrived_truck")) txtapi_code.setText("EP845_IVLB");
            if (controll.equals("txtcustomer")) txtapi_code.setText("EP815_IV");
            if (controll.equals("txtloading_supervisor")) txtapi_code.setText("EP832A");
        }
        else if(fgen.btnid.equals("EP483")){
            if (controll.equals("txtarrived_truck")) txtapi_code.setText("EP845_IVLE");
            if (controll.equals("txtcustomer")) txtapi_code.setText("EP815_IV");
            if (controll.equals("txtloading_supervisor")) txtapi_code.setText("EP832A");
        }
        else{
            if (controll.equals("txtarrived_truck")) txtapi_code.setText("EP845");
            if (controll.equals("txtcustomer")) txtapi_code.setText("EP815");
            if (controll.equals("txtloading_supervisor")) txtapi_code.setText("EP832A");
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
                String list_selected_text = adapter.getItem(position).toString().trim();
                switch (controll)
                {
                    case "txtarrived_truck" :
                        final MyProgressdialog progressDialog = new MyProgressdialog(frm_record_loading_truck.this);
                        progressDialog.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        if(fgen.btnid.equals("EP483")){
                                            ArrayList<String> obj = finapi.fill_record_in_listview_popup_for_item_part_no("MEGH702", list_selected_text.toString().split("---")[2].trim());
                                            for (String x: obj) {
                                                String data = x.split("#~#")[1].trim();
                                                txtloading_supervisor.setText(x.split("#~#")[0].trim());
                                                txtcustomer.setText(data);
                                                fill_grid(data+ "--- -");
                                            }
                                        }
                                        xvehicle_orig_Str = list_selected_text.toString();
                                        xvehicle = xvehicle_orig_Str.substring(xvehicle_orig_Str.length() -23);
                                        xvehicle_orig_Str=xvehicle_orig_Str.replace("---"+xvehicle,"");
                                        txtarrived_truck.setText(xvehicle_orig_Str);
                                        // On complete call either onLoginSuccess or onLoginFailed
                                        progressDialog.dismiss();
                                    }
                                }, 100);
                        break;
                    case "txtcustomer" :
                        txtcustomer.setText(list_selected_text);
                        fill_grid(list_selected_text);
                        break;
                    case "txtloading_supervisor" : txtloading_supervisor.setText(list_selected_text);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });

    }

    public void fill_grid(String list_selected_text){
        String xcustomer = list_selected_text.split("---")[0].trim() + "---" + list_selected_text.split("---")[1].trim();
        String xdestination = list_selected_text.split("---")[2].trim();
        boolean check_duplicacy = handler.CheckDuplicacy("col1", xcustomer);
        if(check_duplicacy)
        {
            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_record_loading_truck.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Sorry, Duplicate Rows Are Not Allowed!!");
            alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }
        handler.record_expense_insert_data(new expense_model(xcustomer, xdestination));
        frm_record_loading_truck.list = handler.record_expense_get_data();
        frm_record_loading_truck.adapter.notifyDataSetChanged();
        txtcustomer.setText("");
    }
}


class AsyncCaller_truck_loading extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread
    }
    @Override
    protected Void doInBackground(Void... params) {
        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
        frm_lead_enquiry.xfrm_lead_enquiry = "N";
        frm_record_loading_truck.xloading_supervisor = finapi.fill_record_in_listview_popup("EP832A");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
    }
}
