package com.raivogaming.vegescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class TuoteActivity extends AppCompatActivity {

    ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuote);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blackStatusBar));

        viewPager = findViewById(R.id.tuoteTabPager);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(sectionsPagerAdapter);

        tabLayout = findViewById(R.id.tuoteTabLayout);

        tabLayout.setupWithViewPager(viewPager);
    }

    public void toUserPage(View view){
        Intent userIntent = new Intent(TuoteActivity.this, SettingsActivity.class);
        startActivity(userIntent);
    }

    public void toSearchPage(View view){
        Intent searchIntent = new Intent(TuoteActivity.this, SearchActivity.class);
        startActivity(searchIntent);
    }

    public void scan(View view){
        Scanner.scan(this);
    }

    //HANDLAA SKANNERI KUN SE LUKENUT VIIVAKOODIN
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() != null){
                Intent i = new Intent(this, TuoteActivity.class);
                i.putExtra("barcode", result.getContents());
                startActivity(i);
                finish();
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        ImageView userImage = findViewById(R.id.main_bottombar_user);
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            userImage.setImageResource(R.drawable.ic_enter);
        }else{
            userImage.setImageResource(R.drawable.ic_user);
        }
    }
}
