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

public class frm_scaninfo extends AppCompatActivity {
    private Button btn_new, btn_cancel, btn_scan;
    public static EditText txtremarks,txtremarks1,txtremarks2,txtremarks3,txtremarks4,txtremarks5,txtremarks6;
    public  static  EditText txtscan_asset1;
    public static String  btn_clicked = "";
    Toolbar toolbar;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_scaninfo);

        finapi.context = frm_scaninfo.this;
        finapi.deleteJsonResponseFile();


        initializeViews();
        btn_cancel.setText("EXIT");

        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
        disableViewsMethod();
        setSupportActionBar(toolbar);
        finapi.setColorOfStatusBar(frm_scaninfo.this);
        Highlightborder();

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBtnClickedMethod();
                enabledViewMethod();
                btn_new.setEnabled(false);
                btn_cancel.setText("CANCEL");

                Drawable buttonDrawable = btn_new.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                btn_new.setBackground(buttonDrawable);
                txtscan_asset1.setBackgroundResource(R.color.light_blue);

            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_clicked = "scan_info";
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
                    btn_new.setEnabled(true);
                    btn_cancel.setText("EXIT");
                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));
                    txtscan_asset1.setBackgroundResource(R.color.light_grey);
                    txtremarks.getText().clear();
                    txtremarks1.getText().clear();
                    txtremarks2.getText().clear();
                    txtremarks3.getText().clear();
                    txtremarks4.getText().clear();
                    txtremarks5.getText().clear();
                    txtremarks6.getText().clear();
                }
                if(btn_text == "EXIT")
                {
                    finish();
                    txtremarks.getText().clear();
                    txtremarks1.getText().clear();
                    txtremarks2.getText().clear();
                    txtremarks3.getText().clear();
                    txtremarks4.getText().clear();
                    txtremarks5.getText().clear();
                    txtremarks6.getText().clear();
                }
            }
        });


        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_scaninfo.this, "aMCTAG_check");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_scaninfo.this);
                return false;
            }
        });
    }

    private boolean check_conditions() {

        if(txtscan_asset1.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Scan Asset!!", Toast.LENGTH_LONG).show();
            txtscan_asset1.setBackgroundResource(R.color.light_blue);
            return false;
        }

        return true;
    }

    @SuppressLint("MissingPermission")


    private void getNameFromLongitudeAndLatitude(){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        Double lat =0.0;
        Double lon =0.0;

        try {

            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

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


    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

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

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {

        }
    }


    private void enabledViewMethod() {

        btn_scan.setEnabled(true);
        txtscan_asset1.setEnabled(true);
        txtremarks.setEnabled(true);

    }

    private void newBtnClickedMethod() {

        txtscan_asset1.setText("");
        txtremarks.setText("");

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


    }

    private void disableViewsMethod() {

        btn_scan.setEnabled(false);
        txtscan_asset1.setEnabled(false);
        txtremarks.setEnabled(false);
        txtremarks1.setEnabled(false);
        txtremarks2.setEnabled(false);
        txtremarks3.setEnabled(false);
        txtremarks4.setEnabled(false);
        txtremarks5.setEnabled(false);
        txtremarks6.setEnabled(false);

    }

    private void initializeViews() {

        btn_new = findViewById(R.id.btn_new);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_scan = findViewById(R.id.btn_scan);
        txtscan_asset1 = findViewById(R.id.txtscan_asset1);
        toolbar = findViewById(R.id.toolbar);
        txtremarks = findViewById(R.id.txtremarks);
        txtremarks1 = findViewById (R.id.txtremarks1);
        txtremarks2 = findViewById (R.id.txtremarks2);
        txtremarks3 = findViewById (R.id.txtremarks3);
        txtremarks4 = findViewById (R.id.txtremarks4);
        txtremarks5 = findViewById (R.id.txtremarks5);
        txtremarks6 = findViewById (R.id.txtremarks6);
    }
}