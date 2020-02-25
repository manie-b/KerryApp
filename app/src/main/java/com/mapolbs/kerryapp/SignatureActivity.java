package com.mapolbs.kerryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
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
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SignatureActivity extends AppCompatActivity {

    TextView et_intime,et_outtime;
    SignaturePad signaturePad;
    Button btn_cap1_seal,btn_clrsign,btn_submit;
    ImageView cap1sealimageview;

    Calendar calendar;
    int mHour, mMinute;
    String AmPm;
    TimePickerDialog timePickerDialog;

    /*image capture componets*/
    public  static final int RequestPermissionCode  = 1 ;
    File mPhotoFile;

    /*Singnature new*/
    Bitmap bitmap;
    SignatureView signatureView;
    String path;
    private static final String IMAGE_DIRECTORY = "/Kerry_Sign";

    ScrollView scrollView;

    String devicedate1;
    private Uri fileUri;
    File captureimgfile;

    /*loaction apis*/
    // boolean flag to toggle the ui
    private Boolean nRequestingLocationUpdates;

    // bunch of location related apis
    private FusedLocationProviderClient nFusedLocationClient;
    private SettingsClient nSettingsClient;
    private LocationRequest nLocationRequest;
    private LocationSettingsRequest nLocationSettingsRequest;
    private LocationCallback nLocationCallback;
    private Location nCurrentLocation;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDSS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDSS = 5000;

    private static final int REQUEST_CHECK_SETTINGSS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        //init
        et_intime=findViewById(R.id.intime_input);
        et_outtime=findViewById(R.id.outtime_input);
        signatureView=findViewById(R.id.signature_view);
        btn_submit=findViewById(R.id.btnsubmit_sign);
        btn_cap1_seal=findViewById(R.id.cap1_btn_seal);
        cap1sealimageview=findViewById(R.id.cap1_sealimageview);

        //btn_savesign=findViewById(R.id.saveButton);
        btn_clrsign=findViewById(R.id.clearButton);

        scrollView=findViewById(R.id.scrollView);

        EnableRuntimePermission();

        init();

        //click listener
        et_intime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar=Calendar.getInstance();
                mHour=calendar.get(Calendar.HOUR_OF_DAY);
                mMinute=calendar.get(Calendar.MINUTE);

                timePickerDialog=new TimePickerDialog(SignatureActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            AmPm = "PM";
                        } else {
                            AmPm = "AM";
                        }
                        et_intime.setText(String.format("%02d:%02d", hourOfDay, minute) + AmPm);
                    }
                },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });


        et_outtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar=Calendar.getInstance();
                mHour=calendar.get(Calendar.HOUR_OF_DAY);
                mMinute=calendar.get(Calendar.MINUTE);

                timePickerDialog=new TimePickerDialog(SignatureActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            AmPm = "PM";
                        } else {
                            AmPm = "AM";
                        }
                        et_outtime.setText(String.format("%02d:%02d", hourOfDay, minute) + AmPm);
                    }
                },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });


        btn_cap1_seal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);

            }
        });

        btn_clrsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureView.clearCanvas();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*signature part*/
                bitmap = signatureView.getSignatureBitmap();
                path = saveImage(bitmap);
                requestPermissions();
                /*signature part*/


                /*location run time permission*/
                Dexter.withActivity(SignatureActivity.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                nRequestingLocationUpdates = true;
                                startLocationUpdatess();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                openSettingss();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });


        signatureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int motion=motionEvent.getAction();

                switch (motion)
                {
                    case MotionEvent.ACTION_DOWN:
                        // Disable the scroll view to intercept the touch event
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow scroll View to intercept the touch event
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                        default:
                            return true;
                }
            }
        });

    }

    private void init() {
        nFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        nSettingsClient = LocationServices.getSettingsClient(this);
        nRequestingLocationUpdates = false;

        nLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                nCurrentLocation = locationResult.getLastLocation();

                updateLocationsUI();
            }
        };

        nLocationRequest = new LocationRequest();
        nLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDSS);
        nLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDSS);
        nLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(nLocationRequest);
        nLocationSettingsRequest = builder.build();
    }

    private void openSettingss() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void startLocationUpdatess() {

        nSettingsClient
                .checkLocationSettings(nLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();
                        //noinspection MissingPermission
                        nFusedLocationClient.requestLocationUpdates(nLocationRequest,
                                nLocationCallback, Looper.myLooper());

                        updateLocationsUI();
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
                                    rae.startResolutionForResult(SignatureActivity.this, REQUEST_CHECK_SETTINGSS);
                                } catch (IntentSender.SendIntentException sie) {

                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";


                                Toast.makeText(SignatureActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationsUI();
                    }
                });

    }

    private void updateLocationsUI() {
        /*Update the UI displaying the location data and toggling the buttons*/
        if (nCurrentLocation != null) {
            /*output of the location*/
            Toast.makeText(this, nCurrentLocation.getLatitude()+","+ nCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }


    /*runtime permission - storage*/
    private void requestPermissions()
    {

        Dexter.withActivity(SignatureActivity.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
        ,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                    }

                    // check for permanent denial of any permission
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        // show alert dialog navigating to Settings
                        openSettingsDialog();
                    }
                }).check();

    }


    private void openSettingsDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(SignatureActivity.this);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
    /*runtime permission - storage*/


    private void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SignatureActivity.this, Manifest.permission.CAMERA))
        {
            //Toast.makeText(SignatureActivity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_SHORT).show();
        }
        else {
            ActivityCompat.requestPermissions(SignatureActivity.this,new String[]{Manifest.permission.CAMERA},RequestPermissionCode);
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

                    Toast.makeText(SignatureActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }

    }


    /*convert signature to image file*/
    public String saveImage(Bitmap myBitmap)
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG,90,baos);

        File wallpaperDirectory=new File(Environment.getExternalStorageDirectory()+"/"+IMAGE_DIRECTORY);

        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
            Log.d("hhhhh",wallpaperDirectory.toString());
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();

            FileOutputStream fo = new FileOutputStream(f);
            fo.write(baos.toByteArray());
            MediaScannerConnection.scanFile(SignatureActivity.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
            //Toast.makeText(this, e1.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return "";
    }


    /*capture image stored internal storage*/
    private void storeCameraPhotoInSDCard(Bitmap bitmap,String currentDate)
    {
        File directory=new File(Environment.getExternalStorageDirectory().getPath(),File.separator+".Kerry_Root"+ File.separator+"Kerry_Images");
        File outputFile=new File(directory, "/IMG_"+devicedate1+"_"+".jpg");

        if (!directory.exists())
        {
            directory.mkdirs();
        }

        try {
            FileOutputStream fileOutputStream=new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*new*/
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("ddMMyyyy_HHmmss");
        devicedate1=simpleDateFormat.format(calendar.getTime());

        if (requestCode==1 && resultCode==RESULT_OK)
        {
            cap1sealimageview.setVisibility(View.VISIBLE);
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            cap1sealimageview.setImageBitmap(bitmap);

            /*new*/
            File destination = null;
            File directory=null;
            String imagename = "";
            String imagepath = "";

            directory=new File(Environment.getExternalStorageDirectory().getPath(),File.separator+".Kerry_Root"+ File.separator+"Kerry_Images");
            destination=new File(directory, "/IMG_"+devicedate1+"_"+".jpg");
            storeCameraPhotoInSDCard(bitmap,devicedate1);

            Log.i("Folder Created--->", String.valueOf(directory));
            Log.i("File Created--->", String.valueOf(destination));

        }

    }
}
