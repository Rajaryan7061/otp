package com.example.smartshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

public class otp_page extends AppCompatActivity {
 private EditText ETphone ,ETenterotp;
 private Button getotpbtn,register ;
    private FirebaseAuth mAuth;
    private String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_page);




                mAuth = FirebaseAuth.getInstance();


                ETphone = findViewById(R.id.       ETphone);
        ETenterotp=findViewById(R.id.ETenterotp);
        getotpbtn = findViewById(R.id.get_otpbtn);
         register=findViewById(R.id.registerbtn);


                getotpbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(ETphone.getText().toString())) {

                            Toast.makeText(otp_page.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                        } else {

                            String phone = "+91" + ETphone.getText().toString();
                            sendVerificationCode(phone);
                        }
                    }
                });


                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (TextUtils.isEmpty(ETenterotp.getText().toString())) {

                            Toast.makeText(otp_page.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                        } else {

                            verifyCode(ETenterotp.getText().toString());
                        }
                    }
                });
            }

            private void signInWithCredential(PhoneAuthCredential credential) {


                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Intent i = new Intent(otp_page.this, Home.class);
                                    startActivity(i);
                                    finish();
                                } else {

                                    Toast.makeText(otp_page.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }


            private void sendVerificationCode(String number) {

                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(number)		 // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(this)				 // Activity (for callback binding)
                                .setCallbacks(mCallBack)		 // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }


            private PhoneAuthProvider.OnVerificationStateChangedCallbacks

                    mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    verificationId = s;
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    final String code = phoneAuthCredential.getSmsCode();


                    if (code != null) {

                        ETenterotp.setText(code);



                        verifyCode(code);
                    }
                }


                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(otp_page.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            };
     private void verifyCode(String code) {

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);


                signInWithCredential(credential);
            }
        }




