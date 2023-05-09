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

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class frm_maintenance_request extends AppCompatActivity {
    private Button btn_new,btn_save, btn_cancel, btn_location;
    FusedLocationProviderClient mFusedLocationClient;
    private TextView latitudeTextView, longitTextView;
    private EditText txtlocation, txtshift, txtdeptt, txtmachine_code, txthrs, txtmin, txtcomplain_type, txtremarks;
    int PERMISSION_ID = 44;
    Toolbar toolbar;
    private ImageView  btncamera;
    private PhotoView capturedImage;

    public static ArrayList<String> xshift = new ArrayList<>();
    public static ArrayList<String> xdeptt = new ArrayList<>();
    public static ArrayList<String> xmachine_code = new ArrayList<>();
    public static ArrayList<String> xcomplaint_type = new ArrayList<>();
    String xseperator = "#~#", xbranch="00", xtype ="MQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_maintenance_request);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        finapi.context = frm_maintenance_request.this;
        finapi.deleteJsonResponseFile();

        final MyProgressdialog progressDialog = new MyProgressdialog(frm_maintenance_request.this);
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        frm_lead_enquiry.xfrm_lead_enquiry = "";
                        frm_maintenance_request.xshift = finapi.fill_record_in_listview_popup("EP830");
                        frm_maintenance_request.xdeptt = finapi.fill_record_in_listview_popup("EP819");
                        frm_maintenance_request.xmachine_code = finapi.fill_record_in_listview_popup("EP831");
                        frm_maintenance_request.xcomplaint_type = finapi.fill_record_in_listview_popup("EP861");

                        // On complete call either onLoginSuccess or onLoginFailed
                        progressDialog.dismiss();
                    }
                }, 100);

        initializeViews();
        Highlightborder();
        btn_cancel.setText("EXIT");
        btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));

        finapi.setColorOfStatusBar(frm_maintenance_request.this);
        txtlocation.setKeyListener(null);
        setSupportActionBar(toolbar);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        final String[] currentDateandTime = {sdf.format(new Date())};

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

                txthrs.setText(currentDateandTime[0].substring(0,2));
                txtmin.setText(currentDateandTime[0].substring(3,5));
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

                    btn_new.getBackground().setTint(btn_new.getResources().getColor(R.color.btn_color));getLastLocation();
                    getNameFromLongitudeAndLatitude();

                    capturedImage.setImageDrawable(null);
                    capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));
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

        txtshift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_maintenance_request.this, xshift, "txtshift");
            }
        });
        txtdeptt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_maintenance_request.this, xdeptt, "txtdeptt");
            }
        });
        txtmachine_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_maintenance_request.this, xmachine_code, "txtmachine_code");
            }
        });
        txtcomplain_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogBox(frm_maintenance_request.this, xcomplaint_type, "txtcomplain_type");
            }
        });

        fgen.toolbar_click_count = 0;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fgen.toolbar_click_count;
                if(fgen.toolbar_click_count == 5){
                    fgen.showApiName(frm_maintenance_request.this, "aMaintcomp_ins");
                }
            }
        });

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finapi.ReadJSONResponse(frm_maintenance_request.this);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtshift.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "SHIFT Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtdeptt.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "DEPARTMENT Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtmachine_code.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "MACHINE CODE Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtcomplain_type.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "COMPLAINT TYPE Record Shouldn't Be NULL!!", Toast.LENGTH_LONG).show();
                    return;
                }
                final MyProgressdialog progressDialog = new MyProgressdialog(frm_maintenance_request.this);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                String xmv_location = txtlocation.getText().toString();
                                String xmv_shift = txtshift.getText().toString().split("---")[0].trim();
                                String xmv_departmeent = txtdeptt.getText().toString();
                                String xmv_machine_code = txtmachine_code.getText().toString();
                                String xmv_hrs = txthrs.getText().toString();
                                String xmv_min = txtmin.getText().toString();
                                String xmv_complain_type = txtcomplain_type.getText().toString();
                                String xmv_remarks = txtremarks.getText().toString();

                                String post_param = xbranch + xseperator + xtype + xseperator + xmv_location + xseperator
                                        + xmv_shift + xseperator + xmv_departmeent + xseperator + xmv_machine_code
                                        + xseperator + xmv_hrs + xseperator + xmv_min + xseperator + xmv_complain_type
                                        + xseperator + xmv_remarks  + "!~!~!";

                                ArrayList<team> result = finapi.getApi(fgen.mcd, "aMaintcomp_ins",post_param, fgen.muname, fgen.cdt1.substring(6,10), "-", "-");
                                boolean msg = finapi.showAlertMessage(frm_maintenance_request.this, result);
                                if(!msg){
                                    progressDialog.dismiss();
                                    return;
                                }

                                if(capturedImage.getDrawable().getConstantState() != getResources().getDrawable( R.drawable.erp).getConstantState()) {
                                    msg = finapi.sendImageToServer(imageInByte, frm_maintenance_request.this);
                                    //                      if(!msg)
                                    //                      {
                                    //                          Toast.makeText(getApplicationContext(), "Image Not Saved!!", Toast.LENGTH_LONG).show();
                                    //                          return;
                                    //                      }
                                }
                                capturedImage.setImageDrawable(null);
                                capturedImage.setImageDrawable(getResources().getDrawable(R.drawable.erp));


                                newBtnClickedMethod();
                                getLastLocation();
                                getNameFromLongitudeAndLatitude();
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
        txtshift.setEnabled(true);
        txtdeptt.setEnabled(true);
        txtmachine_code.setEnabled(true);
        txthrs.setEnabled(true);
        txtmin.setEnabled(true);
        txtcomplain_type.setEnabled(true);
        txtremarks.setEnabled(true);
    }

    private void disableViewsMethod() {
        txtshift.setEnabled(false);
        txtdeptt.setEnabled(false);
        txtmachine_code.setEnabled(false);
        txthrs.setEnabled(false);
        txtmin.setEnabled(false);
        txtcomplain_type.setEnabled(false);
        txtremarks.setEnabled(false);
    }

    private void newBtnClickedMethod() {
        txtlocation.setText("");
        txtshift.setText("");
        txtdeptt.setText("");
        txtmachine_code.setText("");
        txthrs.setText("");
        txtmin.setText("");
        txtcomplain_type.setText("");
        txtremarks.setText("");
    }


    private void initializeViews() {
        capturedImage = findViewById(R.id.imageResultx);
        btncamera = findViewById(R.id.btncamera);

        btn_new = findViewById(R.id.btn_new);
        btn_save = findViewById(R.id.btn_save);
        btn_location = findViewById(R.id.btn_location);
        btn_cancel = findViewById(R.id.btn_cancel);

        txtlocation = findViewById(R.id.txt_location);
        longitTextView = findViewById(R.id.lonTextView);
        latitudeTextView = findViewById(R.id.latTextView);
        toolbar = findViewById(R.id.toolbar);
        txtshift = findViewById(R.id.txtshift);
        txtdeptt = findViewById(R.id.txtdepartment);
        txtmachine_code = findViewById(R.id.txtmachine);
        txthrs = findViewById(R.id.txthrs);
        txtmin = findViewById(R.id.txtmin);
        txtcomplain_type = findViewById(R.id.txtcomplain_type);
        txtremarks = findViewById(R.id.txtremarks);
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

            Picasso.with(this)
                    .load(new File(currentPhotoPath))
                    .into(capturedImage);

//            capturedImage.setImageBitmap(rotatedBitmap);
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
        if(controll.equals("txtshift")) txtapi_code.setText("EP830");
        if(controll.equals("txtmachine_code")) txtapi_code.setText("EP831");
        if(controll.equals("txtdeptt")) txtapi_code.setText("EP819");
        if(controll.equals("txtcomplain_type")) txtapi_code.setText("EP861");

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
                    case "txtshift"  : txtshift.setText(list_selected_text.trim());
                        break;
                    case "txtmachine_code"  : txtmachine_code.setText(list_selected_text.trim());
                        break;
                    case "txtdeptt" : txtdeptt.setText(list_selected_text.trim());
                        break;
                    case "txtcomplain_type" : txtcomplain_type.setText(list_selected_text.trim());
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
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


    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
}