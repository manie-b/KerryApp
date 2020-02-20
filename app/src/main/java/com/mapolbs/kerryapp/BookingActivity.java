package com.mapolbs.kerryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mapolbs.kerryapp.Utilities.ScanActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BookingActivity extends AppCompatActivity {

    Button btn_capture1,btn_capture2,btn_barcodescan,btn_submitBooking;
    ImageView capture_img1,capture_img2;
    public  static final int RequestPermissionCode  = 1 ;

    RadioGroup rgrpcargo_or_courier,rgrpPayment_mthd,rgrp_packagetype,rgrpcourier_type;
    RadioButton rb_cargo,rb_courier,rb_credit,rb_topay,rb_paid,rb_foc,rb_box,rb_weight,rb_fixed,rb_docs,rb_nondocs;

    LinearLayout ll_packagetype,ll_couriertype,ll_NoOfpackage;

    public static EditText et_consignNumber;

    String currentPhotoPath;
    String devicedate;
    File captureImageFile;
    private Uri fileuri;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //init
        btn_capture1=findViewById(R.id.cap1_btn_consign);
        btn_capture2=findViewById(R.id.cap2_btn_consign);
        capture_img1=findViewById(R.id.cap1_imageview);
        capture_img2=findViewById(R.id.cap2_imageview);
        btn_barcodescan=findViewById(R.id.btn_barcodescan);
        btn_submitBooking=findViewById(R.id.btn_submit_book);


        rb_cargo=findViewById(R.id.rbtn_cargo);
        rb_courier=findViewById(R.id.rbtn_courier);
        rb_credit=findViewById(R.id.rbtn_credit);
        rb_topay=findViewById(R.id.rbtn_topay);
        rb_paid=findViewById(R.id.rbtn_paid);
        rb_box=findViewById(R.id.rbtn_box);
        rb_weight=findViewById(R.id.rbtn_weight);
        rb_fixed=findViewById(R.id.rbtn_fixed);
        rb_docs=findViewById(R.id.rbtn_docs);
        rb_nondocs=findViewById(R.id.rbtn_nondocs);

        et_consignNumber=findViewById(R.id.et_consign_num);

        rgrpcourier_type=findViewById(R.id.radio_grp_courier);
        rgrp_packagetype=findViewById(R.id.radio_grp_package);
        rgrpcargo_or_courier=findViewById(R.id.radio_grp_cargo_courier);

        ll_packagetype=findViewById(R.id.llayout_packg_type);
        ll_couriertype=findViewById(R.id.llayout_courier_type);
        ll_NoOfpackage=findViewById(R.id.llayout_NoOfpackage);
        //init

        EnableRuntimePermission();

        init();

        //click listener
        btn_capture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);


                //file store
                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("ddMMyyyy_HHmmss");
                devicedate=simpleDateFormat.format(calendar.getTime());

                File destination=new File(Environment.getExternalStorageDirectory().getPath(),File.separator+".Kerry_pickup"+File.separator+"KerryCaptureImages");
                captureImageFile=new File(destination,"/IMG"+"_"+devicedate+".jpg");

                System.out.println(captureImageFile);

                if (!destination.exists())
                {
                    destination.mkdirs();
                    System.out.println("Camera Pic");
                }

                fileuri=Uri.fromFile(captureImageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,fileuri);

            }
        });

        btn_capture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,2);
            }
        });


        rgrpcargo_or_courier.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rb_cargo.isChecked())
                {
                    ll_packagetype.setVisibility(View.VISIBLE);
                    ll_couriertype.setVisibility(View.GONE);

                }else if (rb_courier.isChecked())
                {
                    ll_couriertype.setVisibility(View.VISIBLE);
                    ll_packagetype.setVisibility(View.GONE);
                }
            }
        });

        rgrp_packagetype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rb_box.isChecked())
                {
                    ll_NoOfpackage.setVisibility(View.VISIBLE);
                }else if (rb_weight.isChecked())
                {
                    ll_NoOfpackage.setVisibility(View.GONE);
                }
                else if (rb_fixed.isChecked())
                {
                    ll_NoOfpackage.setVisibility(View.GONE);
                }
            }
        });


        btn_barcodescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(BookingActivity.this, ScanActivity.class);
                startActivity(intent);

            }
        });


        btn_submitBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(BookingActivity.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                mRequestingLocationUpdates = true;
                                startLocationUpdates();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                openSettings();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });


    }

    /*locaton parts*/

    /*Update the UI displaying the location data and toggling the buttons*/
    private void updateLocationUI() {

        if (mCurrentLocation != null) {
            Toast.makeText(this, mCurrentLocation.getLatitude()+","+mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();

        }

    }

    private void startLocationUpdates()
    {

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        /*Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();*/
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(BookingActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {

                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";


                                Toast.makeText(BookingActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    private void init()
    {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mRequestingLocationUpdates = false;

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();

                updateLocationUI();
            }
        };

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /*locaton parts*/


    private void captureImage()
    {
        Intent takePicIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicIntent,RequestPermissionCode);
    }



    private void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(BookingActivity.this, Manifest.permission.CAMERA))
        {
            //Toast.makeText(this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_SHORT).show();
        }
        else {
            ActivityCompat.requestPermissions(BookingActivity.this,new String[]{Manifest.permission.CAMERA},RequestPermissionCode);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(BookingActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(BookingActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==1 && resultCode==RESULT_OK)
        {
            capture_img1.setVisibility(View.VISIBLE);
            //Glide.with(this).load(currentPhotoPath).into(capture_img1);
            Glide.with(this).load(new File(fileuri.getPath())).into(capture_img1);
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            capture_img1.setImageBitmap(bitmap);
        }
        else if (requestCode==2&&resultCode==RESULT_OK)
        {
            capture_img2.setVisibility(View.VISIBLE);
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            capture_img2.setImageBitmap(bitmap);
        }

    }


    public Uri getImageUri(Context inContext,Bitmap inImage)
    {
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        String path= MediaStore.Images.Media.insertImage(inContext.getContentResolver(),inImage,"Title",null);
        return Uri.parse(path);
    }


}
