package com.octopusbeach.wearforgit.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.octopusbeach.wearforgit.Helpers.AuthHelper;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hudson on 6/25/15.
 */
public class NotificationService extends IntentService {
    private static final String TAG = NotificationService.class.getSimpleName();
    private static final String URL = "https://api.github.com/notifications?access_token=";


    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String token = getSharedPreferences("token", MODE_PRIVATE).getString(AuthHelper.TOKEN_KEY, null);
        if (token == null) {// We do not have an access token.
            Log.e(TAG, "Token was null");
            new BroadcastReceiver().cancelAlarm(getApplicationContext()); // Stop the alarm.
            return;
        }

        try {
            URL url = new URL(URL + token);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonBuilder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                jsonBuilder.append(line);
                line = reader.readLine();
            }
            JSONArray array = new JSONArray(jsonBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        publishResults();
    }

    private void publishResults() {
    }
}
