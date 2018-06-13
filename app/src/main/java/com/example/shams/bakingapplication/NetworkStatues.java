package com.example.shams.bakingapplication;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatues {

    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;

        if (connectivityManager != null){
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return (networkInfo != null && networkInfo.isConnectedOrConnecting());

    }
}
