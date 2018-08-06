package com.example.karthi.phauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button logut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        logut=findViewById(R.id.logout);

        logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                sendToAuth();

            }
        });


    }

    private void sendToAuth() {

        Intent auth=new Intent(MainActivity.this,Auth.class);
        startActivity(auth);
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser=mAuth.getCurrentUser();
        if(currentuser==null){
            startActivity(new Intent(MainActivity.this,Auth.class));
            finish();
        }
        else{

            logut.setVisibility(View.VISIBLE);

        }
    }
}
