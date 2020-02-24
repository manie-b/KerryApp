package com.mapolbs.kerryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
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
import java.io.FileOutputStream;
import java.io.IOException;
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
                bitmap = signatureView.getSignatureBitmap();
                path = saveImage(bitmap);
                requestPermissions();
                //Toast.makeText(SignatureActivity.this, "Datas Saved", Toast.LENGTH_SHORT).show();
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
                        // Allow scroll View to interceot the touch event
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


    /*runtime permission - storage*/
    private void requestPermissions()
    {

        Dexter.withActivity(SignatureActivity.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
        ,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION
        ,Manifest.permission.ACCESS_COARSE_LOCATION)
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
            /*if (!f.exists())
            {
                f.createNewFile();
            }*/

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode==RESULT_OK)
        {
            cap1sealimageview.setVisibility(View.VISIBLE);
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            cap1sealimageview.setImageBitmap(bitmap);
        }

    }
}
