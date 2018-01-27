package com.raivogaming.vegescanner;

import android.app.Activity;

import com.google.zxing.integration.android.IntentIntegrator;

public class Scanner {

    static void scan(Activity activity){
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Skannaa tuotteen viivakoodi");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

}
