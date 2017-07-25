package com.example.isco.kolite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText username,e_mail,password,conf_password;
    private Button regist_btn,RegistLogin_btn;
    private String Username,Email,Password,Conf_password;
    private ProgressDialog mProgress;

    //FireBase
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        //DataBase auth & reference
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        auth = FirebaseAuth.getInstance();


        username         = (EditText) findViewById(R.id.regist_username);
        e_mail           = (EditText) findViewById(R.id.regist_email);
        password         = (EditText) findViewById(R.id.regist_password);
        conf_password    = (EditText) findViewById(R.id.regist_confpassword);
        regist_btn       = (Button)   findViewById(R.id.regist_btn);
        RegistLogin_btn  = (Button)   findViewById(R.id.RegistLogin_btn);
        mProgress        = new ProgressDialog(this);

        //----------------------------Register
        regist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRegister();
            }
        });

        //----------------------------Go Back To Login
        RegistLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Login.class));
                finish();
            }
        });

    }

    private void startRegister() {

        Username      = username.getText().toString().trim();
        Email         = e_mail.getText().toString().trim();
        Password      = password.getText().toString().trim();
        Conf_password = conf_password.getText().toString().trim();

        //-------------> FireBase
        if (TextUtils.isEmpty(Username) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(Conf_password))
            Toast.makeText(SignUp.this, "Empty Data", Toast.LENGTH_SHORT).show();

        else {
                mProgress.setMessage("Login ... ");
                mProgress.show();
                auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                     if(task.isSuccessful()){
                         String user_id = auth.getCurrentUser().getUid();
                         DatabaseReference setCurrentUserDB = mDatabase.child(user_id);
                         setCurrentUserDB.child("Name").setValue(Username);
                         setCurrentUserDB.child("Image").setValue("Default");
                         mProgress.dismiss();
                     }
                    }
                });

        }
    }
}
