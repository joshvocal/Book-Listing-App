package me.joshvocal.booklisting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.TextView;

public class BookDetailsActivity extends AppCompatActivity {

    TextView titleTextView;
    TextView subtitleTextView;
    TextView authorsTextView;
    TextView publishedDateTextView;
    TextView isbnTextView;
    TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Get all the extras from the previous intent from MainActivity.
        Intent intent = getIntent();
        String title = intent.getStringExtra(MainActivity.TITLE_CODE);
        String subtitle = intent.getStringExtra(MainActivity.SUBTITLE_CODE);
        String[] authors = intent.getStringArrayExtra(MainActivity.AUTHORS_CODE);
        String publishedDate = intent.getStringExtra(MainActivity.PUBLISHED_DATE_CODE);
        String isbn = intent.getStringExtra(MainActivity.ISBN_CODE);
        String description = intent.getStringExtra(MainActivity.DESCRIPTION_CODE);

        // Find all the ids for the views.
        titleTextView = (TextView) findViewById(R.id.book_details_title);
        subtitleTextView = (TextView) findViewById(R.id.book_details_subtitle);
        authorsTextView = (TextView) findViewById(R.id.book_details_authors);
        publishedDateTextView = (TextView) findViewById(R.id.book_details_published_date);
        isbnTextView = (TextView) findViewById(R.id.book_details_isbn);
        descriptionTextView = (TextView) findViewById(R.id.book_details_description);

        // Set the text of all the views from the book details.
        titleTextView.setText(append("Title: ", title));
        subtitleTextView.setText(append("Subtitle: ", subtitle));
        authorsTextView.setText(append("Authors: ", BookAdapter.formatAuthors(authors)));
        publishedDateTextView.setText(append("Published: ", publishedDate));
        isbnTextView.setText(append("ISBN: ", isbn));
        descriptionTextView.setText(append("Description: ", description));
    }

    // Concatenate a string you want to bold with another non-bold string.
    private SpannableStringBuilder append(String boldText, String text) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        StyleSpan boldSpan = new StyleSpan(android.graphics.Typeface.BOLD);
        builder.append(boldText, boldSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE).append(text);
        return builder;
    }
}
