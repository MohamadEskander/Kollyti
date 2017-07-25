package com.example.isco.kolite.Groups;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.isco.kolite.Isco_Add_News;
import com.example.isco.kolite.adapter.postAdapter;
import com.example.isco.kolite.NewsUi.NewsView;
import com.example.isco.kolite.NewsUi.News_Comment;
import com.example.isco.kolite.R;
import com.example.isco.kolite.model.News2view;
import com.example.isco.kolite.model.newviewmodel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class groups_view extends AppCompatActivity {
    DatabaseReference mDatabase;
    Boolean likeprocess;
    private DatabaseReference mDatabaseLike;
    private RecyclerView mmNewsList;
    private LinearLayout lWriteSomthing;
    private ArrayList<newviewmodel> arrNewsList = new ArrayList<>();
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseComment;
    private  String uuid;
    private FirebaseAuth auth;
    private NewAdapter adapter;
    private String Group_Key_m;
    private boolean Isfirst =true;
    private ChildEventListener childListener;
    private ValueEventListener valueListener;
    private ValueEventListener valueListener1;
    private Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_view);

        Group_Key_m = getIntent().getBundleExtra("Group_Home").getString("Group_Key");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child(Group_Key_m);
         query = mDatabase.orderByChild("mDate");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("PostLikes").child(Group_Key_m);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseComment  = FirebaseDatabase.getInstance().getReference().child("PostsComment").child(Group_Key_m);

        mDatabase.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        mDatabaseComment.keepSynced(true);
        mDatabaseUsers.keepSynced(true);

        lWriteSomthing = (LinearLayout) findViewById(R.id.writeSomthing);
        mmNewsList = (RecyclerView) findViewById(R.id.Posts_list);
        mmNewsList.setHasFixedSize(true);
        mmNewsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new NewAdapter(groups_view.this, arrNewsList);
        mmNewsList.setAdapter(adapter);

        lWriteSomthing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(groups_view.this , Isco_Add_News.class);
                Bundle b = new Bundle();
                b.putString("test" , "1");
                b.putString("Posts" , Group_Key_m);
                intent.putExtra("Bundel" , b);
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();
        uuid = auth.getCurrentUser().getUid();

    }

    @Override
    public void onStart() {
        super.onStart();
        RefreshRecycler();
    }

    private class NewAdapter extends RecyclerView.Adapter<postAdapter> {

        Context context ;
        List<newviewmodel> newItemList;
        public NewAdapter(Context context  , List<newviewmodel> newItemList)
        {
            this.context = context;
            this.newItemList = newItemList;
        }

        @Override
        public postAdapter onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_rows, null);
            postAdapter viewHolder = new postAdapter(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(postAdapter viewHolder, int position) {

            //mDatabase.getRef(position).getKey();
            newviewmodel NewItem = newItemList.get(position);
            final String New_Key = NewItem.getNewId();
            viewHolder.setPost(NewItem.getuINewPost());
            viewHolder.setay(groups_view.this, NewItem.getuNewimg());
            viewHolder.setImage(groups_view.this,NewItem.getuImg());
            viewHolder.setName(NewItem.getuName());
            if (NewItem.getFlag()==true)
                viewHolder.NewLike1.setLiked(true);
            viewHolder.NewLike1.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    likeprocess = true;
                    mDatabaseLike.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (likeprocess) {
                                if (dataSnapshot.child(New_Key).hasChild(uuid)) {
                                    mDatabaseLike.child(New_Key).child(uuid).removeValue();
                                    likeprocess = false;
                                } else {
                                    mDatabaseLike.child(New_Key).child(uuid).setValue("true");
                                    likeprocess = false;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    likeprocess = true;
                    mDatabaseLike.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (likeprocess) {
                                if (dataSnapshot.child(New_Key).hasChild(uuid)) {
                                    mDatabaseLike.child(New_Key).child(uuid).removeValue();
                                    likeprocess = false;
                                } else {
                                    mDatabaseLike.child(New_Key).child(uuid).setValue("true");

                                    likeprocess = false;

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

            viewHolder.NewComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(groups_view.this , News_Comment.class);
                    Bundle b = new Bundle();
                    b.putString("Posts_Key" , New_Key);
                    b.putString("Group_Key" , Group_Key_m);
                    intent.putExtra("Posts_Home" , b );
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return (null != newItemList ? newItemList.size() : 0);
        }
    }

    private void RefreshRecycler()
    {
        childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                if (Isfirst) {
                    valueListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot data) {
                            valueListener1 = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot data1) {
                                    newviewmodel NewsModel = new newviewmodel();
                                    if (data1.hasChild(uuid)) {
                                        NewsModel.setFlag(true);
                                    } else {
                                        NewsModel.setFlag(false);
                                    }
                                    NewsModel.setNewId(dataSnapshot.getKey());
                                    NewsModel.setuName(data.child("Name").getValue(String.class));
                                    NewsModel.setuImg(data.child("Image").getValue(String.class));
                                    NewsModel.setuINewPost(dataSnapshot.child("post").getValue(String.class));
                                    NewsModel.setuNewimg(dataSnapshot.child("isco").getValue(String.class));
                                    arrNewsList.add(NewsModel);
                                    adapter.notifyItemInserted(arrNewsList.size() - 1);
                                    mmNewsList.scrollToPosition(adapter.getItemCount() -1);

                                    Isfirst = false;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            mDatabaseLike.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(valueListener1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    mDatabaseUsers.child(dataSnapshot.child("name").getValue(String.class)).addListenerForSingleValueEvent(valueListener);
                }
                }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        query.addChildEventListener(childListener);

    }


    @Override
    protected void onStop() {
        super.onStop();
        if(childListener != null){mDatabase.removeEventListener(childListener);}
        if(valueListener != null){mDatabaseUsers.removeEventListener(valueListener);}
        if(valueListener1 != null){mDatabaseLike.removeEventListener(valueListener1);}

    }
}