package com.example.isco.kolite.tabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.isco.kolite.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class Friends extends Fragment {

    ImageButton imageButton;
    EditText titleText, descText;
    Button addPost;
    private static final int GALLERY_REQUEST = 1;
    private Uri imageUri;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_friends,container,false);

         imageButton  = (ImageButton) view.findViewById(R.id.imageButton);
        imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
         titleText    = (EditText)    view.findViewById(R.id.post_title);
         descText     = (EditText)    view.findViewById(R.id.post_desc);
         addPost      = (Button)      view.findViewById(R.id.add_post);
        mStorage      = FirebaseStorage.getInstance().getReference();
        mDatabase     = FirebaseDatabase.getInstance().getReference().child("Posts");
        mProgress     = new ProgressDialog(getContext());

    //-------------------------------------------------------------------------------
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });
    //-------------------------------------------------------------------------------
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });
    //-------------------------------------------------------------------------------
        return view;
    }

    private void uploadPost() {
        mProgress.setMessage("Posting to Blog...");
        mProgress.show();
        final String title   = titleText.getText().toString().trim();
        final String descrep = descText.getText().toString().trim();
        if( !TextUtils.isEmpty(title) && !TextUtils.isEmpty(descrep) && imageUri != null){

            StorageReference filepath = mStorage.child("Blog_Images").child(imageUri.getLastPathSegment()); //StorageReferance !!
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabase.push();            // DatabaseReferance !!
                    newPost.child("Title").setValue(title);
                    newPost.child("Description").setValue(descrep);
                    newPost.child("Image").setValue(downloadUrl.toString());

                    mProgress.dismiss();
                    // Reset
                    titleText.setText("");
                    descText.setText("");
                    imageButton.setImageURI(Uri.EMPTY);
                   // imageButton.setImageDrawable(Drawable.createFromPath("@android:drawable/ic_menu_gallery"));

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

           imageUri = data.getData();
           imageButton.setImageURI(imageUri);
        } else
            Toast.makeText(getContext(), "select image", Toast.LENGTH_SHORT).show();
    }

    /**
     * Create fragment and pass bundle with data as its' arguments
     */
    public static Friends newInstance() {
        Friends fragment = new Friends();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Friends() {
        /* Required empty public constructor*/
    }
}
