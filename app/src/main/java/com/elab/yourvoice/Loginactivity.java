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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Loginactivity extends AppCompatActivity {

   // private FirebaseUser currentuser;
    private Button login;
    private Button phn;
    private EditText email;
    private EditText pwd;
    private TextView forgot;
    private TextView newaaccount;
    private FirebaseAuth mauth;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);
        Initializefield();
        mauth=FirebaseAuth.getInstance();
        //currentuser=mauth.getCurrentUser();
        newaaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUsertoregactivity();

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowusertologin();
            }
        });
        phn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phnintent=new Intent(Loginactivity.this,PhoneloginActivity.class);
                startActivity(phnintent);
            }
        });
    }

    private void allowusertologin() {
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
            loadingbar.setTitle("Signing in");
            loadingbar.setMessage("Please wait while we are signing you in");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();
            mauth.signInWithEmailAndPassword(useremail,userpwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Loginactivity.this,"Logged in successfuly",Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                        sendUsertoMainactivity();
                        //finish();
                    }
                    else
                    {
                        String message=task.getException().toString();
                        Toast.makeText(Loginactivity.this,"Invalid Credentials",Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                    }


                }
            });

        }
    }



    private void Initializefield() {
        login=(Button)findViewById(R.id.login_btn);
        phn=(Button)findViewById(R.id.phnlogin);
        email=(EditText)findViewById(R.id.loginemail);
        pwd=(EditText)findViewById(R.id.loginpwd);
        forgot=(TextView)findViewById(R.id.fgt_pwd_link);
        newaaccount=(TextView)findViewById(R.id.newuser);
        loadingbar=new ProgressDialog(this);
    }



    private void sendUsertoregactivity() {

        Intent logininten=new Intent(Loginactivity.this,Registeractivity.class);
        startActivity(logininten);


    }
    private void sendUsertoMainactivity() {

        Intent mainintent=new Intent(Loginactivity.this,MainActivity.class);

        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();


    }
}
