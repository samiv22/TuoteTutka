package com.raivogaming.vegescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameField, emailField, passwordField;
    Button registerButton;
    private FirebaseAuth mAuth;
    TextView registeringText, createNewAccountText;
    View lineOne, lineTwo;
    LinearLayout textLayout;

    ProgressBar progressBar;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lineOne = findViewById(R.id.registerPageDividerOne);
        lineTwo = findViewById(R.id.registerPageDividerTwo);
        textLayout = findViewById(R.id.registerpage_tologinpagetext);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        progressBar = (ProgressBar)findViewById(R.id.registerProgressBar);
        registeringText = (TextView)findViewById(R.id.registeringText);
        createNewAccountText = (TextView)findViewById(R.id.createNewAccountText);

        progressBar.setVisibility(View.INVISIBLE);
        registeringText.setVisibility(View.INVISIBLE);

        usernameField = (EditText) findViewById(R.id.registerUsernameField);
        emailField = (EditText) findViewById(R.id.registerEmailField);
        passwordField = (EditText) findViewById(R.id.registerPasswordField);
        registerButton = (Button)findViewById(R.id.registerRegisterButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameField.getText().toString().trim();
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Täytä kaikki kentät", Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(email, password, username);
                }
            }
        });
    }

    public void toLoginPage(View view){
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void registerUser(String email, String password, String username){

        progressBar.setVisibility(View.VISIBLE);
        registeringText.setVisibility(View.VISIBLE);
        usernameField.setVisibility(View.INVISIBLE);
        emailField.setVisibility(View.INVISIBLE);
        passwordField.setVisibility(View.INVISIBLE);
        registerButton.setVisibility(View.INVISIBLE);
        createNewAccountText.setVisibility(View.INVISIBLE);
        textLayout.setVisibility(View.INVISIBLE);
        lineOne.setVisibility(View.INVISIBLE);
        lineTwo.setVisibility(View.INVISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            registeringText.setVisibility(View.INVISIBLE);
                            usernameField.setVisibility(View.VISIBLE);
                            emailField.setVisibility(View.VISIBLE);
                            passwordField.setVisibility(View.VISIBLE);
                            registerButton.setVisibility(View.VISIBLE);
                            createNewAccountText.setVisibility(View.VISIBLE);
                            lineOne.setVisibility(View.VISIBLE);
                            lineTwo.setVisibility(View.VISIBLE);
                            textLayout.setVisibility(View.VISIBLE);

                            String error = "";

                            if(task.getException().getMessage().equalsIgnoreCase("the email address is already in use by another account.")){
                                error = "Sähköpostiosoite on jo toisen tilin käytössä.";
                            }else if(task.getException().getMessage().equalsIgnoreCase("the email address is badly formatted.")){
                                error = "Sähköpostiosoite ei kelpaa.";
                            } else if(task.getException().getMessage().equalsIgnoreCase("the given password is invalid. [ password should be at least 6 characters ]")) {
                                error = "Salasanan pitää olla vähintään 6 merkkiä pitkä.";
                            } else {
                                error = task.getException().getMessage();
                            }

                            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_LONG).show();
                        }else{

                            String uid = mAuth.getCurrentUser().getUid();
                            String username = usernameField.getText().toString().trim();

                            User user = new User(username, "https://firebasestorage.googleapis.com/v0/b/vegescanner-4c8d6.appspot.com/o/profileimages%2Fdefaultuserimage.png?alt=media&token=b14e155b-c20d-47b1-a91e-aa379cc2a29e", true);

                            myRef.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }else{
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
    }

}
