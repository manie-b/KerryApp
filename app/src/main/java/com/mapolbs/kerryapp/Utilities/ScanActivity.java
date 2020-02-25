package com.mapolbs.kerryapp.Utilities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.mapolbs.kerryapp.BookingActivity;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView mScannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView=new ZXingScannerView(this); // Programmatically initialize the scanner view
        setContentView(mScannerView); // Set the scanner view as the content view
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();  // Start camera on resume
    }


    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();  // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {

        BookingActivity.et_consignNumber.setText(result.getText());
        onBackPressed();

        /*restrict edittext editable after get the result from scanner here*/
        if (!(result.getText() ==null))
        {
            BookingActivity.et_consignNumber.setKeyListener(null);
        }else {
            Toast.makeText(this, "Dont OverWrite Here..!", Toast.LENGTH_SHORT).show();
        }

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
}
