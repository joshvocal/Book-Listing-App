package me.joshvocal.booklisting;

import android.graphics.Bitmap;

/**
 * Created by josh on 5/6/17.
 */

public class Book {

    private String mTitle;
    private String mSubtitle;
    private String[] mAuthors;
    private String mDescription;
    private String mISBN_13;
    private String mPublishDate;
    private Bitmap mThumbnail;

    public Book (String title, String subtitle, String[] authors, String description,
                String ISBN_13, String publishDate, Bitmap thumbnail) {
        mTitle = title;
        mSubtitle = subtitle;
        mAuthors = authors;
        mDescription = description;
        mISBN_13 = ISBN_13;
        mPublishDate = publishDate;
        mThumbnail = thumbnail;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public String[] getAuthors() {
        return mAuthors;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getISBN_13() {
        return mISBN_13;
    }

    public String getPublishDate() {
        return mPublishDate;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

}
