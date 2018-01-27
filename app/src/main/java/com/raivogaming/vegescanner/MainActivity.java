package com.raivogaming.vegescanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

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
            }
        }
    }

    public void toUserPage(View view){
        Intent userIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(userIntent);
    }

    public void toSearchPage(View view){
        Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(searchIntent);
    }

    private void sendToLoginPage(){
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.main_logout_button){
            FirebaseAuth.getInstance().signOut();
            sendToLoginPage();
        }else if(item.getItemId() == R.id.main_settings_button){
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        }
        return true;
    }
}
