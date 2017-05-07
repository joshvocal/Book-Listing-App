package me.joshvocal.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static me.joshvocal.booklisting.MainActivity.LOG_TAG;

/**
 * Created by josh on 5/6/17.
 */

public final class QueryUtils {

    private QueryUtils() {

    }

    private static List<Book> extractBooksFromJson(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<Book> books = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {

                JSONObject currentBookItem = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = currentBookItem.getJSONObject("volumeInfo");

                String title = "";
                if (volumeInfo.has("title")) {
                    title = volumeInfo.getString("title");
                }


                String subtitle = "";
                if (volumeInfo.has("subtitle")) {
                    subtitle = volumeInfo.getString("subtitle");
                }

                JSONArray bookAuthors = volumeInfo.getJSONArray("authors");
                String[] authors = new String[bookAuthors.length()];
                for (int authorIndex = 0; authorIndex < bookAuthors.length(); authorIndex++) {
                    authors[authorIndex] = bookAuthors.get(authorIndex).toString();
                }

                String publishDate = "";
                if (volumeInfo.has("publishedDate")) {
                    publishDate = volumeInfo.getString("publishedDate");
                }

                String description = "";
                if (volumeInfo.has("description")) {
                    description = volumeInfo.getString("description");
                }

                String ISBN_13 = "";
                JSONArray bookIndustryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");
                for (int industryIdentifierIndex = 0; industryIdentifierIndex < bookIndustryIdentifiers.length();
                     industryIdentifierIndex++) {

                    JSONObject bookIndustryIdentifier = bookIndustryIdentifiers.getJSONObject(industryIdentifierIndex);

                    if (bookIndustryIdentifier.getString("type").equals("ISBN_13")) {
                        ISBN_13 = bookIndustryIdentifier.getString("identifier");
                    }

                }

                String url = "";
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                if (imageLinks.has("smallThumbnail")) {
                    url = imageLinks.getString("smallThumbnail");
                }

                Book book = new Book(title, subtitle, authors,
                        description, ISBN_13, publishDate, url);
                books.add(book);

            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Book> fetchBookData(String requestUrl) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Book> books = extractBooksFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return books;
    }
}
