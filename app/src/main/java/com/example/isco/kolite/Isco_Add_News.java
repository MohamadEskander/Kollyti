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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.isco.kolite.model.News;
import com.example.isco.kolite.utils.ImageUtils;
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

import id.zelory.compressor.Compressor;

public class Isco_Add_News extends AppCompatActivity {

    private EditText txtOfnew;
    private ImageButton selectImage;
    private TextView cancelPost;
    private ImageView img;
    private Button pushPost;
    private DatabaseReference mDatabase ;
    private StorageReference storageReference;
    private int var  = 1;
    private int var2 = 2;
    private ProgressDialog pdialog ;
    private FirebaseAuth auth;
    private String uuid;
    private String Group_key;
    private ValueEventListener singlevalue;
    private Uri copressedImg;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == var && resultCode == RESULT_OK) {
            Uri uri = data.getData();
             copressedImg = ImageUtils.getImageFileCompressedUri(uri , 600 , this);
            img.setImageURI(copressedImg);
        } else if (requestCode == var2 && resultCode == RESULT_OK) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isco__add__news);
        init();
        storageReference = FirebaseStorage.getInstance().getReference();
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
                if (copressedImg != null) {
                    showDialog();
                    StorageReference ss = storageReference.child("News_Images").child(copressedImg.getLastPathSegment());
                    ss.putFile(copressedImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadlink = taskSnapshot.getDownloadUrl();
                            final String Key = mDatabase.push().getKey();
                            if(txtOfnew.getText().toString().trim().equals("")) {
                                News nn = new News(" ", downloadlink.toString(), uuid, ServerValue.TIMESTAMP);
                                mDatabase.child(Key).setValue(nn);
                            }
                            else
                            {
                                News nn = new News(txtOfnew.getText().toString().trim(), downloadlink.toString(), uuid, ServerValue.TIMESTAMP);
                                mDatabase.child(Key).setValue(nn);
                            }
                            hideDialog();
                            singlevalue = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Long ss = dataSnapshot.child("mDate").getValue(long.class);
                                    mDatabase.child(Key).child("mDate").setValue(-1 * ss);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            mDatabase.child(Key).addListenerForSingleValueEvent(singlevalue);
                            reset();
                            //startActivity(new Intent(Isco_Add_News.this , MainActivity.class));
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

                else if(copressedImg == null && !txtOfnew.getText().toString().trim().equals(""))
                {
                    showDialog();
                    News nn = new News(txtOfnew.getText().toString().trim(), "Default" , uuid, ServerValue.TIMESTAMP);
                    final String Key = mDatabase.push().getKey();
                    mDatabase.child(Key).setValue(nn);
                    hideDialog();
                    singlevalue = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Long ss = dataSnapshot.child("mDate").getValue(long.class);
                            mDatabase.child(Key).child("mDate").setValue(-1 * ss);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    mDatabase.child(Key).addListenerForSingleValueEvent(singlevalue);
                    reset();
                    //startActivity(new Intent(Isco_Add_News.this , MainActivity.class));
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "write anything or chose image", Toast.LENGTH_SHORT).show();

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

        cancelPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    public void init()
    {
        pushPost = (Button) findViewById(R.id.pushbtn);
        txtOfnew = (EditText) findViewById(R.id.newtxt);
        selectImage = (ImageButton) findViewById(R.id.btnnewimg);
        cancelPost = (TextView) findViewById(R.id.cancelpost);
        img = (ImageView) findViewById(R.id.newimgpath);
    }

    public void showDialog()
    {
        pdialog = new ProgressDialog(Isco_Add_News.this);
        pdialog.setMessage("uploading");
        pdialog.show();
    }

    public void hideDialog()
    {
        pdialog.dismiss();
    }

}
