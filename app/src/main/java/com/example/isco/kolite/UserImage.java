package com.example.isco.kolite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UserImage extends AppCompatActivity {

    Button imageSelect_btn,imageSave,image_skip;
    ImageView imageView;
    private static final int   PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_image);

        imageSelect_btn = (Button)    findViewById(R.id.imageSelect_btn);
        imageSave       = (Button)    findViewById(R.id.imageSave);
        image_skip      = (Button)    findViewById(R.id.image_skip);
        imageView       = (ImageView) findViewById(R.id.imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mStorage      = FirebaseStorage.getInstance().getReference();
        mDatabase     = FirebaseDatabase.getInstance().getReference();

        //---------------------------------------------------------------
        imageSelect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        //---------------------------------------------------------------
        image_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UserImage.this, MainActivity.class));
                finish();
            }
        });
        //---------------------------------------------------------------
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StorageReference filepath = mStorage.child("User_Images").child(imageUri.getLastPathSegment()); //StorageReferance !!
                filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                      //  Uri downloadUrl = taskSnapshot.getDownloadUrl();
                      //  mDatabase.child("Users").child(Constants.USER_ID).child("Image").setValue(downloadUrl.toString()); // DatabaseReferance !!
                        // Reset
                        imageView.setImageURI(Uri.EMPTY);
                    }
                });
            }
        });
    }
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(UserImage.this, " Please select a valid photo ", Toast.LENGTH_SHORT).show();
    }
}
