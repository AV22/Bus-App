package com.ankit22.busapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BusAsyncTask task = new BusAsyncTask();
        task.execute("http://webservices.nextbus.com/service/publicJSONFeed?a=rutgers&command=routeList");
    }

    //Base URL String
    String baseURL = "http://webservices.nextbus.com/service/publicJSONFeed?a=rutgers&command=routeList";
    //
    String jsonResult = "";

    //gets the json fileout put from the background
    private class BusAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            URL jsonUrl = createURL(url[0]);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(jsonUrl);
            } catch (IOException e) {
                Log.e("HTTP", "Problem making the HTTP request.", e);
            }

            Log.e("Background", "Got through");
            return jsonResponse;
        }

        //returns the json string
        @Override
        protected void onPostExecute(String json){
            Log.e("PostExecution", "Returned json");
            jsonResult = json;
            //return;
        }

        //takes the string and converts it to a url
        private URL createURL(String urlString) {
            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException except) {
                //Does not have the correct format of a url
                Log.e("URL", "URL is not correctly formated");
                return null;
            }
            return url;
        }

        //makes http request through the internet
        private String makeHttpRequest(URL url) throws IOException {
            String json = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);//milliseconds
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();

                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    json = readFromStream(inputStream);
                } else {
                    Log.e("INPUTSTREAM", "Error response code: " + urlConnection.getResponseCode());
                }

            } catch (IOException e) {
                Log.e("HTTP", "Had trouble reciving the request ");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException
                    inputStream.close();
                }
            }
            return json;
        }

        //converts inputstream into a string
        private String readFromStream(InputStream inputStream) throws IOException {
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
    }


    public void jsonGet (View v){
        TextView string = findViewById(R.id.hello);
        BusAsyncTask task = new BusAsyncTask();
        task.execute(baseURL);
        string.setText(jsonResult);
    }
}
