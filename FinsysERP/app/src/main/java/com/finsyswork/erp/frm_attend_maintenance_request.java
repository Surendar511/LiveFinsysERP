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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class frm_attend_maintenance_request extends AppCompatActivity {

    private Button btn_new,btn_save, btn_cancel, btn_location, btn_add;
    FusedLocationProviderClient mFusedLocationClient;
    private TextView latitudeTextView, longitTextView;
    private EditText txtlocation , txthrs, txtmin, txtcomplain_type, txtremarks, txtshift, txtdeptt, txtmachine_code, txttime, txtprev_complaint, txtprev_remarks
            , txtissue_observed, txtcorrective_act, txtpreventive_act, txtqty, txtitem, txtattend_by;
    int PERMISSION_ID = 44;
    Toolbar toolbar;
    private ImageView capturedImage, btncamera;
    FloatingActionButton fab;
    LinearLayout linear_tab1, linear_tab2;
    public static ArrayList<String> xcomplaint_type = new ArrayList<>();
    public static ArrayList<String> xattend_by = new ArrayList<>();
    public static ArrayList<String> xitem = new ArrayList<>();
    String xseperator = "#~#", xbranch="00", xtype ="MS";
    int i=0;
    public static truch_loading_adapter adapter;
    public static String pick_attend_maintenance = "N";
    sqliteHelperClass handler;
    RecyclerView recyclerView;
    String xref_no = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_attend_maintenance_request);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_attend_maintenance_request.this;
        finapi.deleteJsonResponseFile();

        frm_lead_enquiry.xfrm_lead_enquiry = "";
        pick_attend_maintenance= "Y";
        final MyProgressdialog progressDialog = new MyProgressdialog(frm_attend_maintenance_request.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        frm_attend_maintenance_request.xcomplaint_type = finapi.fill_record_in_listview_popup_for_truck_loading("EP862");
                        frm_attend_maintenance_request.xattend_by = finapi.fill_record_in_listview_popup("EP715C");
                        new AsyncCaller_attended_maintenance_request().execute();
                        // On complete call either onLoginSuccess or onLoginFailed
                        progressDialog.dismiss();
                    }
                }, 100);

        initializeViews();

        Highlightborder();
        btn_cancel.setText("EXIT");
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        finapi.setColorOfStatusBar(frm_attend_maintenance_request.this);
        txtlocation.setKeyListener(null);
        txtshift.setKeyListener(null);
        txtdeptt.setKeyListener(null);
        txtmachine_code.setKeyListener(null);
        txttime.setKeyListener(null);
        txtprev_complaint.setKeyListener(null);
        txtprev_remarks.setKeyListener(null);

        setSupportActionBar(toolbar);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        final String[] currentDateandTime = {sdf.format(new Date())};

        handler = new sqliteHelperClass(this);
        handler.clear_data();
        frm_record_loading_truck.list = handler.record_expense_get_data();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new truch_loading_adapter(getApplicationContext());
        recyclerView.setAdapter(adapter);

        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraKholo();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtitem.getText().toString().isEmpty() || txtqty.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Select Item And Qty!!", Toast.LENGTH_LONG).show();
                    return;
                }
                String xitem = txtitem.getText().toString();
                String xqty = txtqty.getText().toString();
                boolean check_duplicacy = handler.CheckDuplicacy("col1", xitem);
                if(check_duplicacy)
                {
                    androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(frm_attend_maintenance_request.this).create();
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
                handler.record_expense_insert_data(new expense_model(xitem, xqty));
                frm_record_loading_truck.list = handler.record_expense_get_data();
                adapter.notifyDataSetChanged();
                txtitem.setText("");
                txtqty.setText("");
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
                txthrs.setText(currentDateandTime[0].substring(0,2));
                txtmin.setText(currentDateandTime[0].substring(3,5));
                getLastLocation();
                getNameFromLongitudeAndLatitude();

                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);

                pick_attend_maintenance= "Y";
                txtshift.setText("Shift");
                txtdeptt.setText("Department");
                txtmachine_code.setText("M/C");
                txttime.setText("Time");
                txtcomplain_type.setText("Complaint");
                txtprev_remarks.setText("Remarks");
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
                    txtshift.setText("Shift");
                    txtdeptt.setText("Department");
                    txtmachine_code.setText("M/C");
                    txttime.setText("Time");
                    txtprev_complaint.setText("Complaint");
                    txtprev_remarks.setText("Remarks");

                    pick_attend_maintenance= "N";
                    getLastLocation();
                    getNameFromLongitudeAndLatitude();
                    handler.clear_data();
                    frm_record_loading_truck.list = handler.record_expense_get_data();
                    adapter.notifyDataSetChanged();
                    capturedImage.setImageDrawable(null);
                    capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                }
                if(btn_text == "EXIT")
                {
                    pick_attend_maintenance= "N";
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i == 0) {
                    fab.setImageResource(R.drawable.left_arrow);
                    linear_tab1.setVisibility(View.GONE);
                    linear_tab2.setVisibility(View.VISIBLE);
                    i++;
                }
                else
                {
                    fab.setImageResource(R.drawable.right_arrow);
                    linear_tab1.setVisibility(View.VISIBLE);
                    linear_tab2.setVisibility(View.GONE);
                    i--;
                }
            }
        });

        txtitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_attend_maintenance_request.this, xitem, "txtitem");
            }
        });

        txtcomplain_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_attend_maintenance_request.this, xcomplaint_type, "txtcomplain_type");
            }
        });

        txtattend_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_attend_maintenance_request.this, xattend_by, "txtattend_by");
            }
        });


        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_attend_maintenance_request.this, "amaintspare_ins, aMaintsolv_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_attend_maintenance_request.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtcomplain_type.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "COMPLAINT TYPE Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtissue_observed.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Issue Observed Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtpreventive_act.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Presentive Act Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtcorrective_act.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Corrective Act Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_attend_maintenance_request.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String xmv_location = txtlocation.getText().toString();
                                String xmv_shift = txtshift.getText().toString();
                                String xmv_departmeent = txtdeptt.getText().toString();
                                String xmv_machine_code = txtmachine_code.getText().toString();
                                String xmv_hrs = txthrs.getText().toString();
                                String xmv_min = txtmin.getText().toString();
                                String xmv_issue_observed = txtissue_observed.getText().toString();
                                String xmv_corrective_act = txtcorrective_act.getText().toString();
                                String xmv_preventive_act = txtpreventive_act.getText().toString();
                                String xmv_spare_cons = "-";
                                String xmv_spare_cost = "-";
                                String xmv_complain_type = txtcomplain_type.getText().toString();
                                String xmv_remarks = txtremarks.getText().toString();


                                frm_record_loading_truck.list = handler.record_expense_get_data();
                                ArrayList<expense_model> bullets = frm_record_loading_truck.list;
                                String post_param = "";
                                for(expense_model b : bullets) {
                                    String  expense = b.expense;
                                    String amount = b.amount;
                                    post_param  += expense + xseperator + amount + xseperator + xref_no + "!~!~!";
                                }

                                ArrayList<team> result = finapi.getApi(fgen.mcd, "amaintspare_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                boolean msg = finapi.showAlertMessageForMultipleSaving(frm_attend_maintenance_request.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }

                                post_param = xbranch + xseperator + xtype + xseperator + xmv_location + xseperator
                                        + xmv_shift + xseperator + xmv_departmeent + xseperator + xmv_machine_code
                                        + xseperator + xmv_hrs + xseperator + xmv_min + xseperator + xmv_issue_observed
                                        + xseperator + xmv_corrective_act + xseperator + xmv_preventive_act
                                        + xseperator + xmv_spare_cons + xseperator + xmv_spare_cost + xseperator
                                        + xmv_remarks + xseperator + xref_no + "!~!~!";

                                result = finapi.getApi(fgen.mcd, "aMaintsolv_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                msg = finapi.showAlertMessage(frm_attend_maintenance_request.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }

                                if (capturedImage.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.erp).getConstantState()) {
                                    msg = finapi.sendImageToServer(imageInByte, frm_attend_maintenance_request.this);
                                    //                      if(!msg)
                                    //                      {
                                    //                          Toast.makeText(getApplicationContext(), "Image Not Saved!!", Toast.LENGTH_LONG).show();
                                    //                          return;
                                    //                      }
                                }
                                capturedImage.setImageDrawable(null);
                                capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));

                                txtshift.setText("Shift");
                                txtdeptt.setText("Department");
                                txtmachine_code.setText("M/C");
                                txttime.setText("Time");
                                txtprev_complaint.setText("Complaint");
                                txtprev_remarks.setText("Remarks");
                                newBtnClickedMethod();
                                getLastLocation();
                                getNameFromLongitudeAndLatitude();

                                handler.clear_data();
                                frm_record_loading_truck.list = handler.record_expense_get_data();
                                adapter.notifyDataSetChanged();

                                frm_attend_maintenance_request.xcomplaint_type = finapi.fill_record_in_listview_popup_for_truck_loading("EP862");
                                currentDateandTime[0] = sdf.format(new Date());
                                txthrs.setText(currentDateandTime[0].substring(0,2));
                                txtmin.setText(currentDateandTime[0].substring(3,5));
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });
    }


    private void enabledViewMethod() {
        txthrs.setEnabled(true);
        txtmin.setEnabled(true);
        txtcomplain_type.setEnabled(true);
        txtremarks.setEnabled(true);
        txtissue_observed.setEnabled(true);
        txtcorrective_act.setEnabled(true);
        txtpreventive_act.setEnabled(true);
    }

    private void disableViewsMethod() {
        txthrs.setEnabled(false);
        txtmin.setEnabled(false);
        txtcomplain_type.setEnabled(false);
        txtremarks.setEnabled(false);
        txtissue_observed.setEnabled(false);
        txtcorrective_act.setEnabled(false);
        txtpreventive_act.setEnabled(false);
    }

    private void newBtnClickedMethod() {
        txtlocation.setText("");
        txthrs.setText("");
        txtmin.setText("");
        txtcomplain_type.setText("");
        txtremarks.setText("");
        txtissue_observed.setText("");
        txtcorrective_act.setText("");
        txtpreventive_act.setText("");
    }

    private void initializeViews() {
        capturedImage = findViewById(R.id.imageResultx);
        btncamera = findViewById(R.id.btncamera);


        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_location = findViewById(R.id.btn_location);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_add = findViewById(R.id.btn_add);
        fab = findViewById(R.id.fab);
        txtlocation = findViewById(R.id.txt_location);
        longitTextView = findViewById(R.id.lonTextView);
        latitudeTextView = findViewById(R.id.latTextView);
        toolbar = findViewById(R.id.toolbar);
        txtshift = findViewById(R.id.txtshift);
        txtdeptt = findViewById(R.id.txtdepartment);
        txtmachine_code = findViewById(R.id.txtmachine);
        txttime = findViewById(R.id.txttime);
        txthrs = findViewById(R.id.txthrs);
        txtmin = findViewById(R.id.txtmin);
        txtqty = findViewById(R.id.txtqty);
        txtitem = findViewById(R.id.txtitem);
        txtcomplain_type = findViewById(R.id.txtcomplain_type);
        txtremarks = findViewById(R.id.txtremarks);
        txtissue_observed = findViewById(R.id.txtissue_observed);
        txtcorrective_act = findViewById(R.id.txtcorrective_act);
        txtpreventive_act = findViewById(R.id.txtpreventive_act);
        linear_tab1 = findViewById(R.id.lin);
        linear_tab2 = findViewById(R.id.linear_tab2);
        recyclerView = findViewById(R.id.recycler);
        txtprev_complaint = findViewById(R.id.txtcomp_type);
        txtprev_remarks = findViewById(R.id.txtprev_remarks);
        txtattend_by = findViewById(R.id.txtattend_by);
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
//        if(controll.equals("txtitem")) txtapi_code.setText("EP821");
        if(fgen.mcd.equals("GGRP")){
            if(controll.equals("txtitem")) txtapi_code.setText("EP821M");
        }
        else
        {
            if(controll.equals("txtitem")) txtapi_code.setText("EP821");
        }
        if(controll.equals("txtmachine_code")) txtapi_code.setText("EP825");
        if(controll.equals("txtdeptt")) txtapi_code.setText("EP838");
        if(controll.equals("txtcomplain_type")) txtapi_code.setText("EP862");

        close_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
//        search_list.setAdapter(adapter);

        final SearchListAdapter listViewadapter = new SearchListAdapter(this, list);
        search_list.setAdapter(listViewadapter);


        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listViewadapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String list_selected_text = listViewadapter.getItem(position).toString();
                switch (controll)
                {
                    case "txtitem" : txtitem.setText(list_selected_text.trim());
                        break;
                    case "txtmachine_code"  : txtmachine_code.setText(list_selected_text.trim());
                        break;
                    case "txtdeptt" : txtdeptt.setText(list_selected_text.trim());
                        break;
                    case "txtattend_by" : txtattend_by.setText(list_selected_text.trim());
                        break;
                    case "txtcomplain_type" :
                        final MyProgressdialog progressDialog = new MyProgressdialog(frm_attend_maintenance_request.this);
                        progressDialog.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        String xstring = list_selected_text.trim();
                                        String xcomp_type = xstring.split("---")[0] + "---" + xstring.split("---")[1] + "---" + xstring.split("---")[2];
                                        try {
                                            xref_no = xstring.split("---")[3];
                                        }catch (Exception e){
                                            xref_no = xstring.split("---")[2];
                                        }
                                        txtcomplain_type.setText(xcomp_type);
                                        frm_lead_enquiry.xfrm_lead_enquiry = "Y";
                                        frm_lead_enquiry.xfg_sub_group_code = xref_no;
                                        finapi.fill_record_in_listview_popup("EP863");
                                        frm_lead_enquiry.xfg_sub_group_code = "";
                                        frm_lead_enquiry.xfrm_lead_enquiry = "";
                                        txtshift.setText(fgen.xpopup_col1);
                                        txtdeptt.setText(fgen.xpopup_col2);
                                        txtmachine_code.setText(fgen.xpopup_col3);
                                        txttime.setText(fgen.xpopup_col4);
                                        txtprev_complaint.setText(fgen.xpopup_col5);
                                        txtprev_remarks.setText(fgen.xpopup_col6);
                                        // On complete call either onLoginSuccess or onLoginFailed
                                        progressDialog.dismiss();
                                    }
                                }, 100);

                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });

    }

    private void Highlightborder() {

        txtissue_observed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtcorrective_act.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtpreventive_act.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
            }
        });
        txtqty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    view.setBackgroundResource(R.drawable.edittext_border_change);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_border_remove);
                }
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
}



class AsyncCaller_attended_maintenance_request extends AsyncTask<Void, Void, Void>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        frm_attend_maintenance_request.xitem = new ArrayList<>();
        //this method will be running on UI thread
    }
    @Override
    protected Void doInBackground(Void... params) {
        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
//        frm_attend_maintenance_request.xitem = finapi.fill_record_in_listview_popup("EP821");
        if(fgen.mcd.equals("GGRP")){
            frm_attend_maintenance_request.xitem = finapi.fill_record_in_listview_popup("EP821M");
        }
        else
        {
            frm_attend_maintenance_request.xitem = finapi.fill_record_in_listview_popup("EP821M");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
    }
}
