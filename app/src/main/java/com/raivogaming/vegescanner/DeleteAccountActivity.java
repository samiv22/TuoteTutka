package com.raivogaming.vegescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteAccountActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference userReference;
    private StorageReference userimageReference;

    TextInputLayout passwordField;

    TextView poistetaanTeksti, infoTeksti, otsikkoTeksti;

    ProgressBar poistetaanProgressBar;

    Button poistaButton;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        //Asetetaan kentät
        poistetaanTeksti = (TextView)findViewById(R.id.delete_poistetaan_teksti);
        poistetaanTeksti.setVisibility(View.INVISIBLE);
        infoTeksti = (TextView)findViewById(R.id.delete_info_teksti);
        otsikkoTeksti = (TextView)findViewById(R.id.delete_otsikko_teksi);
        poistetaanProgressBar = (ProgressBar)findViewById(R.id.deletingProgressbar);
        poistetaanProgressBar.setVisibility(View.INVISIBLE);
        poistaButton = (Button)findViewById(R.id.delete_delete_account_button);
        passwordField = (TextInputLayout)findViewById(R.id.delete_account_password_field);

        //Toolbar
        toolbar = (Toolbar)findViewById(R.id.deleteaccount_page_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Poista tili");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase Authentication referenssi
        auth = FirebaseAuth.getInstance();

        //Nykyinen käyttäjä
        user = auth.getCurrentUser();

        //Database referenssi ja osoitetaan referenssi nykyiseen käyttäjään databasessa
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference().child("Users").child(user.getUid());

        //Imageref joka osoittaa FirebaseStorageen ja siellä progileimages kansioon ja siellä kuvaan jolla nykyisen käyttäjän UID
        userimageReference = FirebaseStorage.getInstance().getReference().child("profileimages").child(user.getUid());



    }

    public void deleteAccount(View view){

       String password = passwordField.getEditText().getText().toString();

        if(!password.isEmpty()){
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //Näkyville
                        poistetaanProgressBar.setVisibility(View.VISIBLE);
                        poistetaanTeksti.setVisibility(View.VISIBLE);
                        //Pois näkyvistä
                        infoTeksti.setVisibility(View.INVISIBLE);
                        otsikkoTeksti.setVisibility(View.INVISIBLE);
                        poistaButton.setVisibility(View.INVISIBLE);
                        passwordField.setVisibility(View.INVISIBLE);
                        userimageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.i("myTag", "Image Deleted");
                                userReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.i("myTag", "Deleted user from database");
                                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.i("myTag", "Deleted account");
                                                        Toast.makeText(DeleteAccountActivity.this, "Käyttäjä ja kaikki tiedot poistettu onnistuneesti", Toast.LENGTH_SHORT).show();
                                                        Intent startIntent = new Intent(DeleteAccountActivity.this, StartActivity.class);
                                                        startActivity(startIntent);
                                                        finish();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }else{
                        Toast.makeText(DeleteAccountActivity.this, "Väärä salasana", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(this, "Anna salasanasi.", Toast.LENGTH_SHORT).show();
        }
    }

}
