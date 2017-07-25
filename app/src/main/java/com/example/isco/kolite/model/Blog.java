package com.example.isco.kolite.model;

/**
 * Created by Ra Mzy on 27/04/2017.
 */

public class Blog {

    private String Title;
    private String Description;
    private String Image;

    public Blog(){}

    public Blog(String title, String description, String image) {
        Title = title;
        Description = description;
        Image = image;
    }

    public String getTitle() {
        return Title;
    }
    public String getDescription() {
        return Description;
    }
    public String getImage() {
        return Image;
    }

    public void setTitle(String title) {
        Title = title;
    }
    public void setDescription(String description) {
        Description = description;
    }
    public void setImage(String image) {
        Image = image;
    }
}
