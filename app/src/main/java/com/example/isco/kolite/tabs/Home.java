package com.example.isco.kolite.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isco.kolite.R;
import com.example.isco.kolite.model.Blog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home extends Fragment {

    private DatabaseReference mDatabase;

     RecyclerView mBlogList;

//------------------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home,container,false);

        //DataBase auth & reference
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");

        mBlogList = (RecyclerView) view.findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
    //---------------------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();

       FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
               Blog.class,
               R.layout.blog_row,
               BlogViewHolder.class,
               mDatabase

       ) {
           @Override
           protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

               viewHolder.setTitle(model.getTitle());
               viewHolder.setDesc(model.getDescription());
               viewHolder.setImage(getContext() ,model.getImage());
           }
       };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }
    //----------------------------------------------------------------------------------
    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public BlogViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView post_row_title = (TextView) mView.findViewById(R.id.post_row_title);
            post_row_title.setText(title);
        }
        public void setDesc(String desc){
            TextView post_row_title = (TextView) mView.findViewById(R.id.post_row_desc);
            post_row_title.setText(desc);
        }
        public void setImage(Context context, String image){
            ImageView post_row_image = (ImageView) mView.findViewById(R.id.post_row_image);
            Picasso.with(context).load(image).into(post_row_image);
        }

    }
    /**
     * Create fragment and pass bundle with data as its' arguments
     */
    public static Home newInstance() {
        Home fragment = new Home();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Home() {
        /* Required empty public constructor*/
    }

}
