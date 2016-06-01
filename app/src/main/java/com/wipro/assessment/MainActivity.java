package com.wipro.assessment;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    private String url = "https://dl.dropboxusercontent.com/u/746330/facts.json";
    List<Item> items;
    LazyLoadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        new GetDataTask().execute(url);
    }

    public class GetDataTask extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject result = new JSONObject(s);
                String title = result.getString("title");
                JSONArray rows = result.getJSONArray("rows");
                items = new ArrayList<Item>();
                for (int i = 0; i < rows.length(); i++) {
                    JSONObject itemJson = rows.getJSONObject(i);
                    Item item = new Item();
                    item.setTitle(itemJson.getString("title"));
                    item.setDescription(itemJson.getString("description"));
                    item.setImageUrl(itemJson.getString("imageHref"));
                    items.add(item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new LazyLoadAdapter(MainActivity.this, items);
            listView.setAdapter(adapter);
        }
    }
}
