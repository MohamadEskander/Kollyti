package com.example.isco.kolite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.isco.kolite.R;
import com.like.LikeButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Isco on 6/23/2017.
 */
public class GroupsAdapter extends RecyclerView.ViewHolder {

        public View mView;
        public LinearLayout linearLayout;

        public GroupsAdapter(View itemView){
            super(itemView);
            mView = itemView;
            linearLayout = (LinearLayout) mView.findViewById(R.id.group_linear_1);

        }
        public void setImage(final Context context, final String image){
           final ImageView gImg = (ImageView) mView.findViewById(R.id.group_img_1);
            Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.gray_img).into(gImg, new Callback() {
                @Override
                public void onSuccess() {

                }
                @Override
                public void onError() {
                    Picasso.with(context).load(image).placeholder(R.drawable.gray_img).into(gImg);
                }
            });
        }

        public void setName(String name1)
        {
            TextView gName = (TextView) mView.findViewById(R.id.group_name_1);
            gName.setText(name1);
        }

    }