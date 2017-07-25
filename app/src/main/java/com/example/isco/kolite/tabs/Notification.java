package com.example.isco.kolite.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isco.kolite.R;

public class Notification extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notifications,container,false);

        TextView textView = (TextView) view.findViewById(R.id.text);
        //textView.setText("Notification");
        return view;
    }

    /**
     * Create fragment and pass bundle with data as its' arguments
     */
    public static Notification newInstance() {
        Notification fragment = new Notification();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Notification() {
        /* Required empty public constructor*/
    }

}