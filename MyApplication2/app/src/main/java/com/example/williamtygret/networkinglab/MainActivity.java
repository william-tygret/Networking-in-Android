package com.example.williamtygret.networkinglab;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button mButtonCereal;
    Button mButtonChocolate;
    Button mButtonTea;
    private ListView mListView;
    private ArrayList<String> mStringArray;
    private ArrayAdapter<String> mAdapter;

    private String urlStringCereal;
    private String urlStringChocolate;
    private String urlStringTea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mListView = (ListView)findViewById(R.id.listView);
        mStringArray = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mStringArray);
        mListView.setAdapter(mAdapter);

        urlStringCereal = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=3edm9cvfgkubxv4wg3gc59jv";
        urlStringChocolate = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=3edm9cvfgkubxv4wg3gc59jv";
        urlStringTea = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=3edm9cvfgkubxv4wg3gc59jv";

        mButtonCereal = (Button)findViewById(R.id.buttonCereal);
        mButtonCereal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadAsyncWalmart downloadAsyncWalmart = new DownloadAsyncWalmart();
                downloadAsyncWalmart.execute(urlStringCereal);
            }
        });

        mButtonChocolate = (Button)findViewById(R.id.buttonChocolate);
        mButtonChocolate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadAsyncWalmart downloadAsyncWalmart = new DownloadAsyncWalmart();
                downloadAsyncWalmart.execute(urlStringChocolate);
            }
        });

        mButtonTea = (Button)findViewById(R.id.buttonTea);
        mButtonTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadAsyncWalmart downloadAsyncWalmart = new DownloadAsyncWalmart();
                downloadAsyncWalmart.execute(urlStringTea);
            }
        });

    }


    public class DownloadAsyncWalmart extends AsyncTask<String, Void, String> {

        String data;

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inStream = connection.getInputStream();

                data = getInputData(inStream);
                Log.d("StupidGuy","we got the data "+data);

            } catch (Throwable thro) {
                thro.printStackTrace();
            }


            try {
                JSONObject dataObject = new JSONObject(data);
                JSONArray itemsArray = dataObject.getJSONArray("items");
                mStringArray.clear();

                for(int counter=0;counter<itemsArray.length();counter++){
                    JSONObject theObject = itemsArray.optJSONObject(counter);
                   // Log.d("MainActivity", theObject.getString("name"));
                    mStringArray.add(theObject.getString("name"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);

            mAdapter.notifyDataSetChanged();

        }

        private String getInputData(InputStream inStream) throws IOException {
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

            String data;

            while((data = reader.readLine()) != null){
                builder.append(data);
            }

            reader.close();

            return builder.toString();
        }
    }
}
