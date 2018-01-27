package com.raivogaming.vegescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    ArrayList<String> tervehdykset;
    Button registerButton, loginButton;
    TextView helloText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        registerButton = (Button) findViewById(R.id.startNotYetAccountButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        loginButton = (Button)findViewById(R.id.startAlreadyAccountButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        setTervehdys();
    }

    public void setTervehdys(){
        tervehdykset = new ArrayList<>();
        tervehdykset.add("Mukava nähdä!");
        tervehdykset.add("Hei!");
        tervehdykset.add("Tervetuloa!");
        tervehdykset.add("Kiva että olet täällä!");

        int max = tervehdykset.size();
        int min = 0;

        int random = (int)(Math.random()*max) + min;

        helloText = (TextView)findViewById(R.id.startHelloTextView);
        helloText.setText(tervehdykset.get(random));
    }
}
