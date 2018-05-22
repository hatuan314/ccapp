package com.example.hatuan.download;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ha Tuan on 23/03/2018.
 */

public class LogIn extends AsyncTask<String, Void, String>{
    StringBuilder data;
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            Uri.Builder uri = new Uri.Builder();
            //Truyền ngầm dữ liệu từ app vào code php
            uri.appendQueryParameter("tendangnhap",strings[1]);
            String dulieupost = uri.build().getEncodedQuery();

            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);

            writer.write(dulieupost);
            writer.flush();
            writer.close();
            outputStreamWriter.close();
            outputStream.close();

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
