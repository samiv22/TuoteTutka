package com.raivogaming.vegescanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    Button changeImageButton, deleteAccountButton, logoutButton;
    TextView usernameTextView;
    CircleImageView userImageView;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference userReference;

    private StorageReference imageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            imageReference = FirebaseStorage.getInstance().getReference();

            usernameTextView = findViewById(R.id.settingsUsernameText);
            userImageView = findViewById(R.id.settingsUserImage);

            changeImageButton = findViewById(R.id.settingsChangeImageButton);

            changeImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(SettingsActivity.this);
                }
            });

            deleteAccountButton = findViewById(R.id.settingsDeleteAccountButton);

            deleteAccountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAccount();
                }
            });

            database = FirebaseDatabase.getInstance();
            userReference = database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user != null){
                        usernameTextView.setText(user.getUsername());
                        if(!user.getImagepath().equalsIgnoreCase("https://firebasestorage.googleapis.com/v0/b/vegescanner-4c8d6.appspot.com/o/profileimages%2Fdefaultuserimage.png?alt=media&token=b14e155b-c20d-47b1-a91e-aa379cc2a29e")){
                            Picasso.with(getApplicationContext()).load(user.getImagepath()).into(userImageView);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if(FirebaseAuth.getInstance().getCurrentUser() != null){
                        Toast.makeText(getApplicationContext(), "Jokin meni vikaan", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        logoutButton = findViewById(R.id.settingsKirjauduUlosButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();
                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(SettingsActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //==============================================================================================
    //======================================CHANGE IMAGE============================================
    //==============================================================================================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                final Uri resultUri = result.getUri();

                String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                final StorageReference filepath = imageReference.child("profileimages").child(currentUserUid);

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            @SuppressWarnings("VisibleForTests") String downloadURL = task.getResult().getDownloadUrl().toString();

                            userReference.child("imagepath").setValue(downloadURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                    }else{
                                        //HANDLE ERRORS
                                        Toast.makeText(SettingsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(SettingsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }

    //==============================================================================================
    //=====================================ACCOUNT DELETE===========================================
    //==============================================================================================
    private void deleteAccount(){

        Intent deleteIntent = new Intent(SettingsActivity.this, DeleteAccountActivity.class);
        startActivity(deleteIntent);

    }
}
