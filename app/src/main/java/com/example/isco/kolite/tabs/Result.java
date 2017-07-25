package com.example.isco.kolite.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isco.kolite.R;

public class Result extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_result,container,false);

        TextView textView = (TextView) view.findViewById(R.id.text);
       // textView.setText("Result");
        return view;
    }

    /**
     * Create fragment and pass bundle with data as its' arguments
     */
    public static Result newInstance() {
        Result fragment = new Result();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Result() {
        /* Required empty public constructor*/
    }
}