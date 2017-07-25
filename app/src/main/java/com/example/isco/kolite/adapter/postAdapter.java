package com.example.isco.kolite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isco.kolite.R;
import com.like.LikeButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Isco on 6/23/2017.
 */
public class postAdapter extends RecyclerView.ViewHolder {

        public View mView;
        public ImageButton NewComment;
        public  LikeButton NewLike1;

        public postAdapter(View itemView){
            super(itemView);
            mView = itemView;
            NewComment = (ImageButton) mView.findViewById(R.id.NewComment);
            NewLike1 = (LikeButton) mView.findViewById(R.id.NewLike1);

        }
        public void setPost(String post) {
            TextView new_row_post = (TextView) mView.findViewById(R.id.new_row_post);
            new_row_post.setText(post);
        }
        public void setay(final Context context, final String image){
            final ImageView new_row_image = (ImageView) mView.findViewById(R.id.new_row_image);
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

        public void setImage(final Context context, final String image){
            final ImageView new_row_image = (ImageView) mView.findViewById(R.id.news_ID);
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
        public void setName(String name1)
        {
            TextView name = (TextView) mView.findViewById(R.id.new_row_ID);
            name.setText(name1);
        }

    }