package com.example.isco.kolite.NewsUi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isco.kolite.adapter.postAdapter;
import com.example.isco.kolite.adapter.postAdapter;
import com.example.isco.kolite.model.News;
import com.example.isco.kolite.model.newviewmodel;

import com.example.isco.kolite.Isco_Add_News;
import com.example.isco.kolite.MainActivity;
import com.example.isco.kolite.R;
import com.example.isco.kolite.model.News2view;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsView extends Fragment {

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseLike;
    private RecyclerView mmNewsList;
    private boolean likeprocess;
    private DatabaseReference mDatabaseComment;
    private LinearLayout newPosetLinear;
    private  String uuid;
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseUsers;
    private List<newviewmodel> arrNewsList = new ArrayList<newviewmodel>();
    private NewAdapter adapter;
    private boolean Isfirst =true;
    private Query BYtimestamp;
    private ImageView news_user_img;

    //------------------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_news_view,container,false);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("news");
        BYtimestamp = mDatabase.orderByChild("mDate");
        news_user_img = (ImageView) view.findViewById(R.id.news_user_img);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike  = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseComment  = FirebaseDatabase.getInstance().getReference().child("Comment");

        mDatabase.keepSynced(true);
        BYtimestamp.keepSynced(true);
        mDatabaseUsers.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        mDatabaseComment.keepSynced(true);

        auth = FirebaseAuth.getInstance();
        uuid = auth.getCurrentUser().getUid();

        newPosetLinear = (LinearLayout) view.findViewById(R.id.new_comment_linear);

        mmNewsList = (RecyclerView) view.findViewById(R.id.News_list);
        mmNewsList.setHasFixedSize(true);
        mmNewsList.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new NewAdapter(getContext(), arrNewsList);
        mmNewsList.setAdapter(adapter);

        newPosetLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , Isco_Add_News.class);
                Bundle b = new Bundle();
                b.putString("test" , "2");
                intent.putExtra("Bundel" , b);
                startActivity(intent);

            }
        });

             return view;

    }
    //---------------------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();
        RefreshRecycler();
    }

    private void RefreshRecycler()
    {
        BYtimestamp.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                if (Isfirst) {
                    mDatabaseUsers.child(dataSnapshot.child("name").getValue(String.class))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot data) {
                                    if (Isfirst) {
                                        mDatabaseLike.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot data1) {
                                                newviewmodel NewsModel = new newviewmodel();
                                                if (data1.hasChild(uuid)) {
                                                    NewsModel.setFlag(true);
                                                } else {
                                                    NewsModel.setFlag(false);
                                                }
                                                if (uuid.equals(dataSnapshot.child("name").getValue(String.class))){
                                                    Picasso.with(getContext()).load(data.child("Image").getValue(String.class)).placeholder(R.drawable.gray_img).into(news_user_img);
                                                }
                                                NewsModel.setNewId(dataSnapshot.getKey());
                                                NewsModel.setuName(data.child("Name").getValue(String.class));
                                                NewsModel.setuImg(data.child("Image").getValue(String.class));
                                                NewsModel.setuINewPost(dataSnapshot.child("post").getValue(String.class));
                                                NewsModel.setuNewimg(dataSnapshot.child("isco").getValue(String.class));
                                                arrNewsList.add(NewsModel);
                                                adapter.notifyItemInserted(arrNewsList.size() - 1);
                                                mmNewsList.scrollToPosition(adapter.getItemCount() - 1);

                                                Isfirst = false;
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

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
        });
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
            viewHolder.setay(getContext(), NewItem.getuNewimg());
            viewHolder.setImage(getContext(),NewItem.getuImg());
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
                        Intent intent = new Intent(getContext() , News_Comment.class);
                        Bundle b = new Bundle();
                        b.putString("News_Key" , New_Key);
                        intent.putExtra("News_Home" , b );
                        startActivity(intent);
                    }
                });
        }

        @Override
        public int getItemCount() {
            return (null != newItemList ? newItemList.size() : 0);
        }
    }
    //----------------------------------------------------------------------------------

    /**
     * Create fragment and pass bundle with data as its' arguments
     */
    public static NewsView newInstance() {
        NewsView fragment = new NewsView();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public NewsView() {
        /* Required empty public constructor*/
    }
}
