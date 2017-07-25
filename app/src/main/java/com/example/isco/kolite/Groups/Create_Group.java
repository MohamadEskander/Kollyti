package com.example.isco.kolite.Groups;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isco.kolite.Isco_Add_News;
import com.example.isco.kolite.NewsUi.News_Comment;
import com.example.isco.kolite.R;
import com.example.isco.kolite.utils.Constants;
import com.example.isco.kolite.model.GroupModel;
import com.example.isco.kolite.model.News;
import com.example.isco.kolite.model.News2view;
import com.example.isco.kolite.model.Sec;
import com.example.isco.kolite.model.Student;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.rey.material.drawable.CheckBoxDrawable;
import com.rey.material.widget.Button;
import com.rey.material.widget.Spinner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Create_Group extends AppCompatActivity {

    private DatabaseReference mDatabaseGroup;
    private DatabaseReference mDatabaseSection;

    private RecyclerView mmGroupList;
    private TextView create_group;
    private EditText group_name;
    private CircleImageView group_img;
    private Toolbar myToolbar;
    private HashMap<String , String> StudentsID ;
    private Uri uri;
    private StorageReference storageReference;
    private ProgressDialog pdialog;
    private android.widget.Spinner spinner;
    private int numberOfSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__group);
        // database references firebase
        mDatabaseGroup = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_GROUP);
        mDatabaseSection = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL_CLASS);
        storageReference = FirebaseStorage.getInstance().getReference();

        StudentsID = new HashMap<String, String>();

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        create_group = (TextView) findViewById(R.id.group_create_btn);
        group_name = (EditText) findViewById(R.id.group_name_new);
        group_img = (CircleImageView) findViewById(R.id.group_img);


        spinner  = (android.widget.Spinner) findViewById(R.id.group_select_department);

        String[] data = getResources().getStringArray(R.array.depart);
        ArrayAdapter<String> adapter = new com.example.isco.kolite.SpinnerAdapter(Create_Group.this, android.R.layout.simple_spinner_item, data);

        spinner.setAdapter(adapter);
        mmGroupList = (RecyclerView) findViewById(R.id.group_list_student);
        mmGroupList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        } else {
            linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        }
        mmGroupList.setLayoutManager(linearLayoutManager);



        // action for create group
        create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pdialog =   new ProgressDialog(Create_Group.this);
                pdialog.setMessage("uploading");
                pdialog.show();
                StorageReference ss = storageReference.child("News_Images").child(uri.getLastPathSegment());
                ss.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadlink = taskSnapshot.getDownloadUrl();
                        GroupModel gModel = new GroupModel(group_name.getText().toString() , downloadlink.toString());
                        DatabaseReference newDb = mDatabaseGroup.push();
                        newDb.setValue(gModel);
                        for (int x = 0 ; x < StudentsID.size() ; x++)
                        {
                            newDb.child("gUsers").setValue(StudentsID);
                        }
                        pdialog.dismiss();

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



        // select group image
        group_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent , 1);
            }
        });
//

        // action for spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
              DatabaseReference df ;
                StudentsID = new HashMap<String, String>();
                if (position == 0)
                {
                    numberOfSec = 0;
                    df =  mDatabaseSection.child("d1");
                   Firebaseadapter1(df);
                }
                else if (position == 1)
                {
                    numberOfSec = 1;
                   df =  mDatabaseSection.child("d2");
                    Firebaseadapter1(df);
                }
                else if (position == 2)
                {
                    numberOfSec = 2;
                   df =  mDatabaseSection.child("d3");
                    Firebaseadapter1(df);
                }
                else if (position == 3)
                {
                    numberOfSec = 3;
                  df =  mDatabaseSection.child("d4");
                    Firebaseadapter1(df);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
         }


    @Override
    public void onStart() {
        super.onStart();
        numberOfSec = 0;
        DatabaseReference df = mDatabaseSection.child("d1") ;
        Firebaseadapter1(df);
    }


    // select sections from spinner data like class one has two sections
    public void Firebaseadapter1(DatabaseReference df)
    {
        FirebaseRecyclerAdapter<Sec, GroupSecViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Sec, GroupSecViewHolder>
                (
                        Sec.class,
                        R.layout.group_sections_row,
                        GroupSecViewHolder.class,
                        df
                ) {
            @Override
            protected void populateViewHolder(final GroupSecViewHolder viewHolder, Sec model, int position) {
                viewHolder.setName(model.getName());
                final String Sec_Key = getRef(position).getKey();
                viewHolder.selectSec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(numberOfSec == 0) {
                           DatabaseReference df =  mDatabaseSection.child("d1").child(Sec_Key);
                            if (viewHolder.checkB.isChecked()) {
                                viewHolder.checkB.setChecked(false);
                               removeGusers(df);

                            } else {
                                viewHolder.checkB.setChecked(true);
                                 getGusers(df);
                            }
                        }
                        else if(numberOfSec == 1) {
                            DatabaseReference df =  mDatabaseSection.child("d2").child(Sec_Key);
                            if (viewHolder.checkB.isChecked()) {
                                viewHolder.checkB.setChecked(false);
                                removeGusers(df);

                            } else {
                                viewHolder.checkB.setChecked(true);
                                getGusers(df);
                            }
                        }

                    }
                });
            }

        };
        mmGroupList.setAdapter(firebaseRecyclerAdapter);
    }



    // add users to HashMap
    public void getGusers(DatabaseReference df)
    {
        DatabaseReference newDf = df.child("users");
        newDf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    StudentsID.put(postSnapshot.getValue().toString() , postSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //// remove users from HashMap

    public void removeGusers(DatabaseReference df)
    {
        DatabaseReference newDf = df.child("users");
        newDf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    StudentsID.remove(postSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



//        public static class GroupStudentViewHolder extends RecyclerView.ViewHolder {
//
//            View mView;
//            LinearLayout selectStd;
//            com.rey.material.widget.CheckBox checkB ;
//
//            public GroupStudentViewHolder(View itemView){
//                super(itemView);
//                mView = itemView;
//                selectStd = (LinearLayout) mView.findViewById(R.id.group_select_std);
//                checkB = (com.rey.material.widget.CheckBox) mView.findViewById(R.id.group_true_std);
//            }
//
//            public void setName(String name1)
//            {
//                TextView name = (TextView) mView.findViewById(R.id.group_name_std);
//                name.setText(name1);
//            }
//
//        }


     // adapter for recycler view
    public static class GroupSecViewHolder extends RecyclerView.ViewHolder {

        View mView;
        LinearLayout selectSec;
        com.rey.material.widget.CheckBox checkB ;

        public GroupSecViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            selectSec = (LinearLayout) mView.findViewById(R.id.group_select_sec);
            checkB = (com.rey.material.widget.CheckBox) mView.findViewById(R.id.group_true_sec);
        }

        public void setName(String name1)
        {
            TextView name = (TextView) mView.findViewById(R.id.group_name_sec);
            name.setText(name1);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            uri = data.getData();
            group_img.setImageURI(uri);
        }
    }
}
