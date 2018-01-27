package com.raivogaming.vegescanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;

public class BuyPremiumActivity extends AppCompatActivity {

    private BillingClient billingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_premium);

        //billingClient = BillingClient.newBuilder(this).setListener(this).build();

    }
}
