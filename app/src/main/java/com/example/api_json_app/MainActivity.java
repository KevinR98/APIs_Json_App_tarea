package com.example.api_json_app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final ArrayList<String> bookTitles = new ArrayList<String>();

    public void refreshClicked(View view){
        loadBooks();
    }

    public void loadBooks(){
        String url = "https://mighty-hollows-85080.herokuapp.com/books.json";

        Log.i("url", url);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);

    }

    public void chargeBooksList(){
        Log.i("Log", "Started listView.");
        ListView bookList = findViewById(R.id.bookList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bookTitles);
        bookList.setAdapter(adapter);
        Log.i("Log", "Finished listView.");
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            Log.i("Log", "Charging file.");
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1){
                    char current = (char)data;
                    result += current;
                    data = inputStreamReader.read();
                }
                Log.i("Log", "Charging success.");
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("Log", "Charging error.");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("Log", "Starting parsing.");
            super.onPostExecute(result);
            try {

                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject json = jsonArray.getJSONObject(i);
                    bookTitles.add(json.getString("title"));
                    Log.i("Log", json.getString("title"));
                }
                chargeBooksList();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("Log", "Finished parsing.");
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadBooks();
    }
}
