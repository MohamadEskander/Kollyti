package com.example.isco.kolite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.isco.kolite.model.News;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Isco_Add_News extends AppCompatActivity {
    EditText txtOfnew;
    Button selectImage;
    ImageView img;
    Button pushPost;
    DatabaseReference mDatabase ;
    StorageReference storageReference;
    Uri uri;
    Uri fileUri;
    int var  = 1;
    int var2 = 2;
    ProgressDialog pdialog ;
    private FirebaseAuth auth;
    private String uuid;
    private String Group_key;
    private ValueEventListener singlevalue;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == var && resultCode == RESULT_OK) {
            uri = data.getData();
            img.setImageURI(uri);

        } else if (requestCode == var2 && resultCode == RESULT_OK) {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isco__add__news);
        storageReference = FirebaseStorage.getInstance().getReference();
        pushPost = (Button) findViewById(R.id.pushbtn);
        txtOfnew = (EditText) findViewById(R.id.newtxt);
        selectImage = (Button) findViewById(R.id.btnnewimg);
        img = (ImageView) findViewById(R.id.newimgpath);

        auth = FirebaseAuth.getInstance();
        uuid = auth.getCurrentUser().getUid();

        if(getIntent().getBundleExtra("Bundel").getString("test").equals("2")){
            mDatabase = FirebaseDatabase.getInstance().getReference().child("news");
        }
        else if (getIntent().getBundleExtra("Bundel").getString("test").equals("1"))
        {
            Group_key = getIntent().getBundleExtra("Bundel").getString("Posts");
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child(Group_key);
        }

        pushPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference ss = storageReference.child("News_Images").child(uri.getLastPathSegment());
                pdialog =   new ProgressDialog(Isco_Add_News.this);
                pdialog.setMessage("uploading");
                pdialog.show();
                ss.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadlink = taskSnapshot.getDownloadUrl();
                        News nn = new News(txtOfnew.getText().toString().trim() , downloadlink.toString() , uuid , ServerValue.TIMESTAMP);
                        final String Key = mDatabase.push().getKey();
                        mDatabase.child(Key).setValue(nn);
                        pdialog.dismiss();
                        singlevalue = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Long ss = dataSnapshot.child("mDate").getValue(long.class);
                                mDatabase.child(Key).child("mDate").setValue(-1*ss);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        mDatabase.child(Key).addListenerForSingleValueEvent(singlevalue);
                        reset();
                        Toast.makeText(getApplicationContext() , "push sucess" , Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pdialog.dismiss();
                        Toast.makeText(getApplicationContext() , "error" , Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent , var);
            }
        });

    }


//    // select files
//    private void showFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), var2);
//    }

    public void nTimestamp(Object object)
    {
        object = ServerValue.TIMESTAMP;
        Log.v("idco" , object.toString());
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        mDatabase.removeEventListener(singlevalue);
//    }

    public void reset()
    {
        txtOfnew.setText("");
        img.setImageURI(null);

    }
}
