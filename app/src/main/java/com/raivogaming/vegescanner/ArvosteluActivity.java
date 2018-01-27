package com.raivogaming.vegescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ArvosteluActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference ratingRef, userRef, tuoteRef;
    private EditText arvosteluText;
    private RatingBar ratingBar;
    private CheckBox checkBox;

    @Override
    protected void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(ArvosteluActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arvostelu);

        arvosteluText = findViewById(R.id.arvostelu_arvosteluteksti);
        ratingBar = findViewById(R.id.arvostelu_ratingbar);
        checkBox = findViewById(R.id.arvostelu_checkBox);

        database = FirebaseDatabase.getInstance();
        ratingRef = database.getReference().child("Arvostelut");

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blackStatusBar));

    }

    public void lahetaArvostelu(View view){

        final String barcode = getIntent().getStringExtra("barcode");

        //Hae tuote databasesta
        tuoteRef = database.getReference().child("Tuotteet").child(barcode);
        tuoteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Jos haku onnistui lisää uusi arvostelu tuotteen tietoihin
                Tuote tuote = dataSnapshot.getValue(Tuote.class);
                tuoteRef.child("arvostelut").setValue(tuote.getArvostelut() + ratingBar.getRating());
                tuoteRef.child("arvosteluKerrat").setValue(tuote.getArvosteluKerrat() + 1);
                userRef = database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);
                        Arvostelu arvostelu = new Arvostelu(user.getUsername(), arvosteluText.getText().toString(), ratingBar.getRating(), checkBox.isChecked());

                        ratingRef.child(barcode).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(arvostelu);

                        Toast.makeText(getApplicationContext(), "Arvostelun lähettäminen onnistui!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("ERROR", databaseError.getMessage());
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("ERROR", databaseError.getMessage());
            }
        });
    }

    public void goBack(View view){
        finish();
    }

}
