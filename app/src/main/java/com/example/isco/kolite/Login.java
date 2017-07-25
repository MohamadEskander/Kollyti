package com.example.isco.kolite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.isco.kolite.model.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText Code,Password;
    private Button mLogin,mSignupBtn,mForgetPassBtn;
    private String code,pass;
    private ProgressBar progressBar;

    //FireBase
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide Tool Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        //FireBase
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        auth = FirebaseAuth.getInstance();

         mLogin = (Button) findViewById(R.id.login_btn);
         mSignupBtn = (Button) findViewById(R.id.login_signup);
         mForgetPassBtn = (Button) findViewById(R.id.login_reset_password);
         progressBar = (ProgressBar) findViewById(R.id.progressBar);

         Code     = (EditText) findViewById(R.id.login_code);
         Password = (EditText) findViewById(R.id.login_password);

//-------------------------------------------------------------------------------------
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code  = Code.getText().toString();
                pass  = Password.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                //-------------> FireBase
                if(TextUtils.isEmpty(code) || TextUtils.isEmpty(pass))
                    Toast.makeText(Login.this, "Empty Data", Toast.LENGTH_SHORT).show();

                else { checkLogin(); }

            }
        });
//-------------------------------------------------------------------------------------
        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SignUp.class));
            }
        });
//-------------------------------------------------------------------------------------
        mForgetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ForgetPassword.class));
            }
        });
    }
//-------------------------------------------------------------------------------------
    private void checkLogin() {

        auth.signInWithEmailAndPassword(code, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if (!task.isSuccessful()) {
                    // there was an error
                    if (pass.length() < 8) {
                        Password.setError("Password less than 8 ");
                    } else {
                        Toast.makeText(Login.this, "Login failed", Toast.LENGTH_LONG).show();
                        //Log.e("Login_failed",task.getResult().toString());
                    }
                } else { checkUserExist(); }
            }
        });
    }

    private void checkUserExist() {

        final String user_id = auth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    //----> set constant value
                    Constants.USER_ID = user_id;

                    if ( dataSnapshot.child(user_id).child("Image").getValue().equals("Default")){ // if the user photo equql default go to select one //else open mainActivity

                        startActivity(new Intent(Login.this, UserImage.class));
                        finish();
                        mDatabase.removeEventListener(this);

                      } else {
                          startActivity(new Intent(Login.this, MainActivity.class));
                          finish();
                        mDatabase.removeEventListener(this);
                      }

                } else{
                    Toast.makeText(Login.this, " This Account Doesn't Exist ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
