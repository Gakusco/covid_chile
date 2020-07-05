package com.ubb.covidchile.Common;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class Utilities {

    public static Utilities instance;

    public static Utilities getInstance() {
        if (instance == null) instance = new Utilities();
        return instance;
    }

    public void showToastShort(String message) {
        Toast.makeText(MyApp.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String message) {
        Toast.makeText(MyApp.getContext(), message, Toast.LENGTH_LONG).show();
    }

    static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }
}
