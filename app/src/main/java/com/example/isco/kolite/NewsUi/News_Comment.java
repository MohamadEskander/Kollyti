package com.example.isco.kolite.NewsUi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.isco.kolite.R;
import com.example.isco.kolite.adapter.postAdapter;
import com.example.isco.kolite.model.Comment;
import com.example.isco.kolite.model.News2view;
import com.example.isco.kolite.model.News_Comment_Model;
import com.example.isco.kolite.model.newviewmodel;
import com.facebook.share.internal.LikeButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.OnLikeListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class News_Comment extends AppCompatActivity {
    private   EditText commenttxt;
    private ImageButton pushcomment;
    private RecyclerView mListComment;
    private DatabaseReference mDatabaseComment;
    private String News_Key_Comment_String;
    private Bundle News_Key_Comment_Bundel;
    private Bundle Posts_Key_Comment_Bundel;
    private FirebaseAuth auth;
    private String uuid;
    private ArrayList<News_Comment_Model> arrCommentsList = new ArrayList<>();
    private DatabaseReference mDatabaseUsers;
    private CommentAdapter adapter;
    private int flag = 0;
    private SwipeRefreshLayout mLrefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news__comment);
        commenttxt = (EditText) findViewById(R.id.news_comment_txt);
        pushcomment = (ImageButton) findViewById(R.id.PushComment);
        mListComment = (RecyclerView) findViewById(R.id.News_list_Comment);
        mLrefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        auth = FirebaseAuth.getInstance();
        uuid = auth.getCurrentUser().getUid();

        mListComment.setHasFixedSize(true);
        mListComment.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CommentAdapter(this, arrCommentsList);
        mListComment.setAdapter(adapter);

        News_Key_Comment_Bundel = getIntent().getBundleExtra("News_Home");
        Posts_Key_Comment_Bundel = getIntent().getBundleExtra("Posts_Home");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        if (News_Key_Comment_Bundel != null) {
            News_Key_Comment_String = News_Key_Comment_Bundel.getString("News_Key");
            mDatabaseComment = FirebaseDatabase.getInstance().getReference().child("Comment");
        }
        if (Posts_Key_Comment_Bundel != null) {
            News_Key_Comment_String = Posts_Key_Comment_Bundel.getString("Posts_Key");
            String x = Posts_Key_Comment_Bundel.getString("Group_Key");
            mDatabaseComment = FirebaseDatabase.getInstance().getReference().child("PostsComment").child(x);
        }



        mLrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLrefresh.setRefreshing(false);
            }
        });



        pushcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag =1;
                final DatabaseReference ff = mDatabaseComment.child(News_Key_Comment_String);
                final String currentKey = ff.push().getKey();
                String com = commenttxt.getText().toString();
                Comment comment = new Comment(uuid , com);
                ff.child(currentKey).setValue(comment);
                    ff.child(currentKey).setValue(comment , new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Log.v("idco", "Data could not be saved " + databaseError.getMessage());
                            } else {
                                flag = 0;
                                ff.child(currentKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot data) {
                                        commenttxt.setText("");
                                        mDatabaseUsers.child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                News_Comment_Model CommentModel = new News_Comment_Model();
                                                CommentModel.setName(dataSnapshot.child("Name").getValue().toString());
                                                CommentModel.setComent(data.child("coment").getValue().toString());
                                                CommentModel.setImg(dataSnapshot.child("Image").getValue().toString());
                                                arrCommentsList.add(CommentModel);
                                                adapter.notifyItemInserted(arrCommentsList.size() - 1);
                                                mListComment.scrollToPosition(adapter.getItemCount() - 1);
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                }
                        }
                    });
            }
        });
    }

            @Override
            protected void onStart() {
            super.onStart();
            RefreshRecycler();
        }


            public void RefreshRecycler() {
                DatabaseReference df = mDatabaseComment.child(News_Key_Comment_String);
                df.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(final DataSnapshot data, String s) {
                        if (flag == 0) {
                            mDatabaseUsers.child(data.child("name").getValue(String.class)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    News_Comment_Model CommentModel = new News_Comment_Model();
                                    CommentModel.setName(dataSnapshot.child("Name").getValue().toString());
                                    CommentModel.setComent(data.child("coment").getValue().toString());
                                    CommentModel.setImg(dataSnapshot.child("Image").getValue().toString());
                                    arrCommentsList.add(CommentModel);
                                    mListComment.scrollToPosition(arrCommentsList.size() - 1);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
//                        else {
//                            Log.v("a7aa", data.getKey());
//                            flag = 1;
//                             }

                    }

                    @Override
                    public void onChildChanged(final DataSnapshot data, String s) {

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

    public static class NewsCommentViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public NewsCommentViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setUserComment(String coment) {
            TextView news_comment_view = (TextView) mView.findViewById(R.id.news_comment_view1);
            news_comment_view.setText(coment);
        }

        public void setName(String name) {
            TextView news_comment_view = (TextView) mView.findViewById(R.id.news_comment_view);
            news_comment_view.setText(name);
        }
        public void setImg(final Context context, final String image){
            final CircleImageView new_row_image = (CircleImageView) mView.findViewById(R.id.news_comment_img);
           // Picasso.with(context).load(image).placeholder(R.drawable.gray_img).into(new_row_image);
            Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.gray_img).into(new_row_image, new Callback() {
                @Override
                public void onSuccess() {

                }
                @Override
                public void onError() {
                    Picasso.with(context).load(image).placeholder(R.drawable.gray_img).into(new_row_image);
                }
            });

        }
    }

    private  class CommentAdapter extends RecyclerView.Adapter<NewsCommentViewHolder> {

        Context context;
        List<News_Comment_Model> CommentList;

        public CommentAdapter(Context context, List<News_Comment_Model> CommentList) {
            this.context = context;
            this.CommentList = CommentList;
        }

        @Override
        public NewsCommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_row_comment, viewGroup ,false);
            NewsCommentViewHolder viewHolder = new NewsCommentViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(NewsCommentViewHolder viewHolder, int position) {

            //mDatabase.getRef(position).getKey();
            News_Comment_Model commentItem = CommentList.get(position);
            viewHolder.setName(commentItem.getName());
            viewHolder.setUserComment(commentItem.getComent());
            viewHolder.setImg(context , commentItem.getImg());

        }

        @Override
        public int getItemCount() {
            return (null != CommentList ? CommentList.size() : 0);
        }
    }

}
