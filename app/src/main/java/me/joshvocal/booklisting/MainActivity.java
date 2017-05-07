package me.joshvocal.booklisting;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>>{

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String GOOGLE_BOOKS_API_REQUEST_URL
            = "https://www.googleapis.com/books/v1/volumes?q=stevejobs";

    private static final int BOOK_LOADER_ID = 1;
    public static final String THUMBNAIL_CODE = "THUMBNAIL_CODE";
    public static final String TITLE_CODE = "TITLE_CODE";
    public static final String SUBTITLE_CODE = "SUBTITLE_CODE";
    public static final String AUTHORS_CODE = "AUTHORS_CODE";
    public static final String PUBLISHED_DATE_CODE = "PUBLISHED_DATE_CODE";
    public static final String ISBN_CODE = "ISBN_CODE";
    public static final String DESCRIPTION_CODE = "DESCRIPTION_CODE";

    private TextView mEmptyStateTextView;
    private BookAdapter mBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the ListView in the layout
        final ListView booksListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        booksListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of earthquakes as input.
        mBookAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the ListView
        // so the list can be populated in the user interface.
        booksListView.setAdapter(mBookAdapter);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Find the current book that was clicked on.
                Book currentBook = mBookAdapter.getItem(position);
                Intent intent = new Intent(getBaseContext(), BookDetailsActivity.class);
                intent.putExtra(THUMBNAIL_CODE, currentBook.getThumbnail());
                intent.putExtra(TITLE_CODE, currentBook.getTitle());
                intent.putExtra(SUBTITLE_CODE, currentBook.getSubtitle());
                intent.putExtra(AUTHORS_CODE, currentBook.getAuthors());
                intent.putExtra(PUBLISHED_DATE_CODE, currentBook.getPublishDate());
                intent.putExtra(ISBN_CODE, currentBook.getISBN_13());
                intent.putExtra(DESCRIPTION_CODE, currentBook.getDescription());
                startActivity(intent);

            }
        });

        // Start the AsyncTask to fetch the earthquake data.
        BookAsyncTask task = new BookAsyncTask();
        task.execute(GOOGLE_BOOKS_API_REQUEST_URL);

        isNetworkAvailable();

    }

    private void isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        } else {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_network);
        }
    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Book> result = QueryUtils.fetchBookData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Book> bookData) {
            // Clear the adapter of previous earthquake data.
            mBookAdapter.clear();

            // If there is a valid list of Books, then add them to the adapter's
            // data set. This wil trigger the ListView to update.
            if (bookData != null && !bookData.isEmpty()) {
                mBookAdapter.addAll(bookData);
            }
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, GOOGLE_BOOKS_API_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        // Set the visibility of progress bar when onLoadFinished() is called.
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books found";.
        mEmptyStateTextView.setText(R.string.no_books);

        // Clear the adapter of previous book data.
        mBookAdapter.clear();

        // If there is a valid list of Books, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            // Comment this out to add no data to the ListView.
            mBookAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mBookAdapter.clear();
    }

}
