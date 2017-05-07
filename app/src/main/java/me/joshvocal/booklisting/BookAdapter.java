package me.joshvocal.booklisting;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by josh on 5/6/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, List<Book> books) {
        super(context, 0, books);
    }

    public static String formatAuthors(String[] authors) {
        String authorsString = "";

        for (String author : authors) {
            authorsString += author + ", ";
        }

        if (authors.length < 2) {
            authorsString = authorsString.substring(0, authorsString.length() - 2);
        }

        return authorsString;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        ImageView thumbnailImageView = (ImageView) listItemView.findViewById(R.id.book_thumbnail);
        thumbnailImageView.setImageBitmap(currentBook.getThumbnail());

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.book_title);
        titleTextView.setText(currentBook.getTitle());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.book_author);
        authorTextView.setText(formatAuthors(currentBook.getAuthors()));

        TextView subtitleTextView = (TextView) listItemView.findViewById(R.id.book_published_date);
        subtitleTextView.setText(currentBook.getPublishDate());

        return listItemView;
    }
}
