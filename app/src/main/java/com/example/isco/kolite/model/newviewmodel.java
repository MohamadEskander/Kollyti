package com.example.isco.kolite.model;

/**
 * Created by Isco on 6/25/2017.
 */
public class newviewmodel {
    String NewId;
    String uName;
    String uImg;
    String uINewPost;
    String uNewimg;
    Boolean Flag;


    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuImg() {
        return uImg;
    }

    public void setuImg(String uImg) {
        this.uImg = uImg;
    }

    public String getuINewPost() {
        return uINewPost;
    }

    public void setuINewPost(String uINewPost) {
        this.uINewPost = uINewPost;
    }

    public String getuNewimg() {
        return uNewimg;
    }

    public void setuNewimg(String uNewimg) {
        this.uNewimg = uNewimg;
    }

    public String getNewId() {
        return NewId;
    }

    public void setNewId(String newId) {
        NewId = newId;
    }


    public Boolean getFlag() {
        return Flag;
    }

    public void setFlag(Boolean flag) {
        Flag = flag;
    }
}
