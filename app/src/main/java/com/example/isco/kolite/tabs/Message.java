package com.example.isco.kolite.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isco.kolite.R;

public class Message extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_message,container,false);

        TextView textView = (TextView) view.findViewById(R.id.text);
       // textView.setText("Message");
        return view;
    }

    /**
     * Create fragment and pass bundle with data as its' arguments
     */
    public static Message newInstance() {
        Message fragment = new Message();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Message() {
        /* Required empty public constructor*/
    }

}