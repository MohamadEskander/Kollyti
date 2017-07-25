package com.example.isco.kolite.model;

import android.util.Log;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by Isco on 6/17/2017.
 */
public class News {

    String post ;
    String isco;
    String name;
    Object mDate;
    public News(String post , String isco , String name ,  Object mDate)
    {
        this.post = post;
        this.isco = isco;
        this.name = name;
        this.mDate = mDate;
    }

}
