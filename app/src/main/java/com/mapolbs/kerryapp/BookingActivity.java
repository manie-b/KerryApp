package com.mapolbs.kerryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mapolbs.kerryapp.Utilities.ScanActivity;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BookingActivity extends AppCompatActivity {

    Button btn_capture1,btn_capture2,btn_barcodescan;
    ImageView capture_img1,capture_img2;
    public  static final int RequestPermissionCode  = 1 ;

    RadioGroup rgrpcargo_or_courier,rgrpPayment_mthd,rgrp_packagetype,rgrpcourier_type;
    RadioButton rb_cargo,rb_courier,rb_credit,rb_topay,rb_paid,rb_foc,rb_box,rb_weight,rb_fixed,rb_docs,rb_nondocs;

    LinearLayout ll_packagetype,ll_couriertype,ll_NoOfpackage;

    public static TextView txt_barcodeResult;

    ZXingScannerView mScannerView;

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

        txt_barcodeResult=findViewById(R.id.txt_barcode_result);

        rb_cargo=findViewById(R.id.rbtn_cargo);
        rb_courier=findViewById(R.id.rbtn_courier);
        rb_credit=findViewById(R.id.rbtn_credit);
        rb_topay=findViewById(R.id.rbtn_topay);
        rb_paid=findViewById(R.id.rbtn_paid);
        rb_foc=findViewById(R.id.rbtn_foc);
        rb_box=findViewById(R.id.rbtn_box);
        rb_weight=findViewById(R.id.rbtn_weight);
        rb_fixed=findViewById(R.id.rbtn_fixed);
        rb_docs=findViewById(R.id.rbtn_docs);
        rb_nondocs=findViewById(R.id.rbtn_nondocs);

        rgrpcourier_type=findViewById(R.id.radio_grp_courier);
        rgrp_packagetype=findViewById(R.id.radio_grp_package);
        rgrpcargo_or_courier=findViewById(R.id.radio_grp_cargo_courier);

        ll_packagetype=findViewById(R.id.llayout_packg_type);
        ll_couriertype=findViewById(R.id.llayout_courier_type);
        ll_NoOfpackage=findViewById(R.id.llayout_NoOfpackage);
        //init

        EnableRuntimePermission();

        //click listener
        btn_capture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
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
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            capture_img1.setImageBitmap(bitmap);
        }else if (requestCode==2&&resultCode==RESULT_OK)
        {
            capture_img2.setVisibility(View.VISIBLE);
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            capture_img2.setImageBitmap(bitmap);
        }

    }



}
