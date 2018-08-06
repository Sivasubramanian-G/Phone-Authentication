package com.example.karthi.phauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Auth extends AppCompatActivity {

    private EditText phno;
    private EditText otp;
    private Button send;
    private ProgressBar progressBar;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callBacks;
    private FirebaseAuth mAuth;
    private TextView error;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private int btnType=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        phno=findViewById(R.id.phno);
        otp=findViewById(R.id.otp);
        send=findViewById(R.id.send);
        progressBar=findViewById(R.id.progressBar);
        error=findViewById(R.id.show);
        mAuth=FirebaseAuth.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(btnType==0) {
                    phno.setEnabled(false);
                    send.setEnabled(false);
                    String phonenumber = phno.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phonenumber,
                            60,
                            TimeUnit.SECONDS,
                            Auth.this,
                            callBacks

                    );

                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    send.setEnabled(true);
                    otp.setVisibility(View.VISIBLE);
                    String verification=otp.getText().toString();
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVerificationId,verification);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        callBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                error.setText("There was an error in verification...");
                error.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;
                btnType=1;
                otp.setVisibility(View.VISIBLE);
                send.setText("Verify Code");
                send.setEnabled(true);

            }


        };


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = task.getResult().getUser();
                            Intent main=new Intent(Auth.this,MainActivity.class);
                            startActivity(main);
                            finish();

                        } else {

                            error.setText("There was an error in logging in...");
                            error.setVisibility(View.VISIBLE);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                error.setText("Invalid verification code");
                            }
                        }
                    }
                });
    }



}
