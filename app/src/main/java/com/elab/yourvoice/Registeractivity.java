package com.elab.yourvoice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registeractivity extends AppCompatActivity {

    private Button reg;
    private EditText email;
    private EditText pwd;
    private TextView already;
    private FirebaseAuth mauth;
   private ProgressDialog loadingbar;
   private DatabaseReference rootrefernce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);
        reg=(Button)findViewById(R.id.regsiter_btn);
        email=(EditText)findViewById(R.id.registeremail);
        pwd=(EditText)findViewById(R.id.registerpwd);
        mauth=FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(this);
        rootrefernce= FirebaseDatabase.getInstance().getReference();

        already=(TextView)findViewById(R.id.already_have_account);
        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUsertologintivity();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createnewaccount();
            }
        });
    }

    private void createnewaccount() {
        String useremail=email.getText().toString();
        String userpwd=pwd.getText().toString();
        if(TextUtils.isEmpty(useremail))
        {
            Toast.makeText(this,"Please enter the email id",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(userpwd))
        {
            Toast.makeText(this,"Please enter the password",Toast.LENGTH_LONG).show();
        }
        else
        {
            loadingbar.setTitle("Creating new Account");
            loadingbar.setMessage("Please wait while we are creating account for you");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();
            mauth.createUserWithEmailAndPassword(useremail,userpwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        String currentuserid=mauth.getCurrentUser().getUid();
                        rootrefernce.child("Users").child(currentuserid).setValue("");
                        Toast.makeText(Registeractivity.this,"Account created suuccsefuly",Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                        sendUsertomaintivity();
                    }
                    else
                    {
                        String message=task.getException().toString();
                        Toast.makeText(Registeractivity.this,message,Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                    }

                }
            });

        }
    }

    private void sendUsertomaintivity() {

        Intent mainintent=new Intent(Registeractivity.this,MainActivity.class);

        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();

    }
    private void sendUsertologintivity() {

        Intent logintent=new Intent(Registeractivity.this,MainActivity.class);


        startActivity(logintent);


    }
}
