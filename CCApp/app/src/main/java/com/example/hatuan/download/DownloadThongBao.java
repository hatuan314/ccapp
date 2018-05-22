package com.example.hatuan.download;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ha Tuan on 4/10/2018.
 */

public class DownloadThongBao extends AsyncTask<String, Void, String> {
    StringBuilder data;
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.connect();

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            data = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null){
                data.append(line);
            }
            Log.d("kiemtra", data.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }
}
