package com.finsyswork.erp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class frm_verify_asset_location extends AppCompatActivity {

    private Button btn_new,btn_save, btn_cancel, btn_scan, btn_location;
    private EditText txtremarks,txtlocation, txtquantity;
    public  static  EditText txtscan_asset,txtremarks1;
    private TextView latitudeTextView, longitTextView;
    Toolbar toolbar;
    public static String  btn_clicked = "";
    RadioButton radio_working, radio_notworking;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    String xseperator = "#~#", xbranch="00", xtype ="AV";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_verify_asset_location);

        finapi.context = frm_verify_asset_location.this;
        finapi.deleteJsonResponseFile();


        initializeViews();
        btn_cancel.setText("EXIT");

        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_verify_asset_location.this);
        txtlocation.setKeyListener(null);

        Highlightborder();
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
                txtscan_asset.setBackgroundResource(R.color.light_blue);

                getLastLocation();
                getNameFromLongitudeAndLatitude();
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_clicked = "scan_verify_asset";
                startActivity(new Intent(getApplicationContext(), scannerView.class));
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
                    txtscan_asset.setBackgroundResource(R.color.light_grey);
                    txtquantity.setBackgroundResource(R.color.light_grey);
                    radio_working.setChecked(true);
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

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_verify_asset_location.this, "asset_ver_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_verify_asset_location.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check_conditions = check_conditions();
                String xlocation = txtlocation.getText().toString();
                String xasset = txtscan_asset.getText().toString();
                String xquantity = txtquantity.getText().toString();
                String xremarks = txtremarks.getText().toString();
                if(!check_conditions)
                {
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_verify_asset_location.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String xcondition = "NW";
                                if(radio_working.isChecked())
                                {
                                    xcondition = "W";
                                }

                                String post_param = xbranch + xseperator + xtype + xseperator + xlocation + xseperator
                                        + xasset + xseperator + xquantity + xseperator + xcondition + xseperator + xremarks + "!~!~!";
                                ArrayList<team> result = finapi.getApi(fgen.mcd, "asset_ver_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), fgen.btnid, "-");
                                boolean msg = finapi.showAlertMessage(frm_verify_asset_location.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }
                                newBtnClickedMethod();
                                // On complete call either onLoginSuccess or onLoginFailed
                                progressDialog.dismiss();
                            }
                        }, 100);


            }
        });

    }

    private boolean check_conditions() {
        if(txtlocation.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Select Location!!", Toast.LENGTH_LONG).show();
            txtlocation.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtscan_asset.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Scan Asset!!", Toast.LENGTH_LONG).show();
            txtscan_asset.setBackgroundResource(R.color.light_blue);
            return false;
        }
        if(txtquantity.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Fill Quantity!!", Toast.LENGTH_LONG).show();
            txtquantity.setBackgroundResource(R.color.light_blue);
            return false;
        }
        return true;
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

    private void getNameFromLongitudeAndLatitude(){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        Double lat =0.0;
        Double lon =0.0;

        try {
            lat = Double.parseDouble(latitudeTextView.getText().toString());
            lon = Double.parseDouble(longitTextView.getText().toString());
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            txtlocation.setText(address);
        } catch (Exception e) {
            e.printStackTrace();
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


    private void enabledViewMethod() {
        btn_location.setEnabled(true);
        btn_scan.setEnabled(true);
        txtscan_asset.setEnabled(true);
        txtquantity.setEnabled(true);
        txtremarks.setEnabled(true);
        txtremarks1.setEnabled(true);
        radio_working.setEnabled(true);
        radio_notworking.setEnabled(true);
    }

    private void newBtnClickedMethod() {
        txtlocation.setText("");
        txtscan_asset.setText("");
        txtquantity.setText("");
        radio_working.setChecked(true);
        txtremarks.setText("");
        txtremarks1.setText("");

    }

    private void Highlightborder() {
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

        txtquantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    private void disableViewsMethod() {
        btn_location.setEnabled(false);
        btn_scan.setEnabled(false);
        txtscan_asset.setEnabled(false);
        txtquantity.setEnabled(false);
        txtremarks.setEnabled(false);
        txtremarks1.setEnabled(false);
        radio_working.setEnabled(false);
        radio_notworking.setEnabled(false);
    }

    private void initializeViews() {
        longitTextView = findViewById(R.id.lonTextView);
        latitudeTextView = findViewById(R.id.latTextView);
        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_scan = findViewById(R.id.btn_scan);
        btn_location = findViewById(R.id.btn_location);
        txtremarks = findViewById(R.id.txtremarks);
        txtremarks1 = findViewById(R.id.txtremarks1);
        txtlocation = findViewById(R.id.txt_location);
        txtscan_asset = findViewById(R.id.txtscan_asset);
        txtquantity = findViewById(R.id.txtqty);
        radio_working = findViewById(R.id.radio_working);
        radio_notworking = findViewById(R.id.radio_notworking);
        toolbar = findViewById(R.id.toolbar);
        radio_working.setChecked(true);
    }
}