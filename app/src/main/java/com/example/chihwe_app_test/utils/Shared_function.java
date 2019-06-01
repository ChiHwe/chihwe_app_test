package com.example.chihwe_app_test.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class Shared_function {

    public static URLConnection makeGETRequest(String method, String apiAddress) throws IOException {
        URL url = new URL(apiAddress);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(false);
        urlConnection.setRequestMethod(method);

        urlConnection.connect();

        return urlConnection;
    }

    public static String checkNetworkConnection(Context context)
    {
        final ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);

            if (capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))){
                return "YES";
            }else{
                return "No";
            }

        }else {


            if (activeNetwork != null && activeNetwork.isConnected()) {
                if (activeNetwork.getSubtype() == ConnectivityManager.TYPE_WIFI) {

                    try {

                        return new check_connection_task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();

                    } catch (ExecutionException | InterruptedException ei) {
                        ei.printStackTrace();
                    }

                }
                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                    try {

                        return new check_connection_task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();

                    } catch (ExecutionException | InterruptedException ei) {
                        ei.printStackTrace();
                    }

                }
            } else {
                return "No";
            }
        }
        return "";
    }

    public static class check_connection_task extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String[] params) {
            try {

                URL url = new URL("http://www.google.com");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();

                if (urlc.getResponseCode() == 200) {
                    return "Wifi";
                } else {
                    return "No";
                }
            } catch (IOException e) {
                Log.i("warning", e.toString());
                return "No";
            }
        }

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected void onPostExecute(String result){

            super.onPostExecute(result);
        }
    }



}
