package com.example.isco.kolite.utils;

/**
 * Constants class store most important strings and paths of the app
 */
public final class Constants {
    public static final String FIREBASE_LOCATION_CLASS = "Class";
    public static final String FIREBASE_LOCATION_COMMENT = "Comment";
    public static final String FIREBASE_LOCATION_GROUP = "Group";
    public static final String FIREBASE_LOCATION_LIKES = "Likes";
    public static final String FIREBASE_LOCATION_POSTSLIKES = "PostLikes";
    public static final String FIREBASE_LOCATION_POSTS = "Posts";
    public static final String FIREBASE_LOCATION_POSTSCOMMENT = "PostsComment";
    public static final String FIREBASE_LOCATION_USERS = "Users";
    public static final String FIREBASE_LOCATION_NEWS = "news";
    public static final String FIREBASE_LOCATION_IMAGES = "News_Images";
    /**
     * Constants for Firebase Database URL
     */
    public static final String FIREBASE_URL = "https://fireapp22-b7032.firebaseio.com/";

    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;
    public static final String FIREBASE_URL_news = FIREBASE_URL + "/" + FIREBASE_LOCATION_NEWS;
    public static final String FIREBASE_URL_CLASS = FIREBASE_URL + "/" + FIREBASE_LOCATION_CLASS;
    public static final String FIREBASE_URL_COMMENT = FIREBASE_URL + "/" + FIREBASE_LOCATION_COMMENT;
    public static final String FIREBASE_URL_GROUP = FIREBASE_URL + "/" + FIREBASE_LOCATION_GROUP;
    public static final String FIREBASE_URL_LIKES = FIREBASE_URL + "/" + FIREBASE_LOCATION_LIKES;
    public static final String FIREBASE_POSTSLIKES = FIREBASE_URL + "/" + FIREBASE_LOCATION_POSTSLIKES;
    public static final String FIREBASE_URL_POSTS = FIREBASE_URL + "/" + FIREBASE_LOCATION_POSTS;
    public static final String FIREBASE_URL_POSTSCOMMENT= FIREBASE_URL + "/" + FIREBASE_LOCATION_POSTSCOMMENT;

}
