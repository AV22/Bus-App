package com.ankit22.busapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Base URL String
    String baseURL = "";
    //


    private class BusAsyncTask extends AsyncTask<URL, Void, String>{

    }
}
