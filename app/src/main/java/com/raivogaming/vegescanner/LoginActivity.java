package com.raivogaming.vegescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    EditText emailField, passwordField;
    Button loginButton;
    TextView logInText;
    LinearLayout loginProgressBarLayout;
    RelativeLayout loginFieldLayout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginFieldLayout = findViewById(R.id.loginFieldsLayout);

        emailField = findViewById(R.id.loginEmailField);
        passwordField = findViewById(R.id.loginPasswordField);

        //normaali loginbutton
        loginButton = findViewById(R.id.loginLoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        logInText = findViewById(R.id.loginLoginText);

        //Progress bar setup
        loginProgressBarLayout = findViewById(R.id.loginProgressBarLayout);
        loginProgressBarLayout.setVisibility(View.INVISIBLE);
    }

    public void toRegisterPage(View view){
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
        finish();
    }

    private void login(){

        loginFieldLayout.setVisibility(View.INVISIBLE);
        loginProgressBarLayout.setVisibility(View.VISIBLE);

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Täytä molemmat kentät!", Toast.LENGTH_SHORT).show();
            loginFieldLayout.setVisibility(View.VISIBLE);
            loginProgressBarLayout.setVisibility(View.INVISIBLE);
        }else{
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "Jokin meni vikaan :(", Toast.LENGTH_LONG).show();
                        loginFieldLayout.setVisibility(View.VISIBLE);
                        loginProgressBarLayout.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }
}
