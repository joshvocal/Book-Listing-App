package me.joshvocal.booklisting;

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
    private String mUrl;

    public Book (String title, String subtitle, String[] authors, String description,
                String ISBN_13, String publishDate, String url) {
        mTitle = title;
        mSubtitle = subtitle;
        mAuthors = authors;
        mDescription = description;
        mISBN_13 = ISBN_13;
        mPublishDate = publishDate;
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

    public String getmPublishDate() {
        return mPublishDate;
    }

    public String getUrl() {
        return mUrl;
    }

}
