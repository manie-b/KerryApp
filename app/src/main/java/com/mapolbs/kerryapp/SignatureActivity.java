package com.mapolbs.kerryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.util.Calendar;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        //init
        et_intime=findViewById(R.id.intime_input);
        et_outtime=findViewById(R.id.outtime_input);
        signaturePad=findViewById(R.id.sign_pad);
        btn_submit=findViewById(R.id.btnsubmit_sign);
        btn_cap1_seal=findViewById(R.id.cap1_btn_seal);
        cap1sealimageview=findViewById(R.id.cap1_sealimageview);

        //btn_savesign=findViewById(R.id.saveButton);
        btn_clrsign=findViewById(R.id.clearButton);

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

        //disable both signature buttons at start
        //btn_savesign.setEnabled(false);
        btn_clrsign.setEnabled(false);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                //btn_savesign.setEnabled(true);
                btn_clrsign.setEnabled(true);
            }

            @Override
            public void onClear() {
                //btn_savesign.setEnabled(false);
                btn_clrsign.setEnabled(false);
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
                signaturePad.clear();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignatureActivity.this, "Datas Saved", Toast.LENGTH_SHORT).show();
            }
        });

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
