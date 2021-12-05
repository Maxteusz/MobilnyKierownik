package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.journeyapps.barcodescanner.CaptureActivity;

public class ScanActivity  extends CaptureActivity {

    //Utworzona dla orientacji skanera Barcode

    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}