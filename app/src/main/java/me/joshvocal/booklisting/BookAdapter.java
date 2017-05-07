package me.joshvocal.booklisting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by josh on 5/6/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, List<Book> books) {
        super(context, 0, books);
    }

    private String formatAuthors(String[] authors) {
        String authorsString = "";

        for (String author : authors) {
            authorsString += author + ", ";
        }

        if (authors.length < 2) {
            authorsString = authorsString.substring(0, authorsString.length() - 2);
        }

        return authorsString;
    }

    private Bitmap getBitmapFromURL(final String urlString) {

        Bitmap bitmap = null;

        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    InputStream inputStream = new URL(urlString).openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                return bitmap;
            }
        }.execute();

        return bitmap;
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
        thumbnailImageView.setImageBitmap(getBitmapFromURL(currentBook.getUrl()));

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.book_title);
        titleTextView.setText(currentBook.getTitle());

        TextView subtitleTextView = (TextView) listItemView.findViewById(R.id.book_subtitle);
        subtitleTextView.setText(currentBook.getSubtitle());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.book_author);
        authorTextView.setText(formatAuthors(currentBook.getAuthors()));

        return listItemView;
    }
}
