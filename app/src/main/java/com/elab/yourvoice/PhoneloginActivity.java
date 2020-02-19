package com.elab.yourvoice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneloginActivity extends AppCompatActivity {
    private Button sendcode, verify;
    private EditText phone, verificationcode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_phonelogin);
        sendcode = (Button) findViewById(R.id.sendcode);
        verify = (Button) findViewById(R.id.verifybtn);
        phone = (EditText) findViewById(R.id.phninput);
        loadingbar = new ProgressDialog(this);
        verificationcode = (EditText) findViewById(R.id.codeverification);
        sendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phnno = phone.getText().toString();
                if (TextUtils.isEmpty(phnno)) {
                    Toast.makeText(PhoneloginActivity.this, "Please enter your phone number first", Toast.LENGTH_LONG).show();
                } else {
                    loadingbar.setTitle("Phone Verification");
                    loadingbar.setMessage("Please wait,while we authenticate your phone");
                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phnno,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            PhoneloginActivity.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks
                }


            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loadingbar.dismiss();

                Toast.makeText(PhoneloginActivity.this, "Invalid please enter correct phone number with your country code", Toast.LENGTH_LONG).show();

                phone.setVisibility(View.VISIBLE);
                verify.setVisibility(View.INVISIBLE);
                verificationcode.setVisibility(View.INVISIBLE);
                sendcode.setVisibility(View.VISIBLE);

            }

            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                loadingbar.dismiss();
                Toast.makeText(PhoneloginActivity.this, "Code sent", Toast.LENGTH_LONG).show();
                verificationcode.setVisibility(View.VISIBLE);
                phone.setVisibility(View.INVISIBLE);
                verify.setVisibility(View.VISIBLE);
                sendcode.setVisibility(View.INVISIBLE);


            }

        };
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.setVisibility(View.INVISIBLE);
                sendcode.setVisibility(View.INVISIBLE);
                String code = verificationcode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(PhoneloginActivity.this, "Please enter the code", Toast.LENGTH_LONG).show();
                } else {

                    loadingbar.setTitle("Code Verification");
                    loadingbar.setMessage("Please wait,while we are verifying");
                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingbar.dismiss();
                            Toast.makeText(PhoneloginActivity.this, "Logged in Successfully", Toast.LENGTH_LONG).show();
                            sendtomainactivity();


                        } else {
                            String msg = task.getException().toString();
                            Toast.makeText(PhoneloginActivity.this, msg, Toast.LENGTH_LONG).show();


                        }

                    }
                });
    }


    private void sendtomainactivity() {
        Intent mainintent = new Intent(PhoneloginActivity.this, MainActivity.class);
        startActivity(mainintent);
        finish();
    }
}




