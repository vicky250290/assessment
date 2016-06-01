package com.wipro.assessment;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        //Initialize Universal Image Loader library.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        listView = (ListView) findViewById(R.id.listview);

        new GetDataTask().execute(url);//execute async task to load data from given url to listview

        //add listener for swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetDataTask().execute(url);//execute async task again to reload data
            }
        });
    }

    public class GetDataTask extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... params) {

            //Load data from given URL and return result
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
                getSupportActionBar().setTitle(title);//set action bar title from json data
                JSONArray rows = result.getJSONArray("rows");
                items = new ArrayList<Item>();
                for (int i = 0; i < rows.length(); i++) {
                    JSONObject itemJson = rows.getJSONObject(i);
                    Item item = new Item();
                    item.setTitle(itemJson.getString("title"));
                    item.setDescription(itemJson.getString("description"));
                    item.setImageUrl(itemJson.getString("imageHref"));
                    items.add(item);//add each item to the List Collection
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //set the list of items to be loaded in listview
            adapter = new LazyLoadAdapter(MainActivity.this, items);
            listView.setAdapter(adapter);

            // hide the refresh symbol from swipelayout after loading data
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
