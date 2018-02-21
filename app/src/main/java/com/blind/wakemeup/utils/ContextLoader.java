package com.blind.wakemeup.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by delor on 30/01/2018.
 */

public class ContextLoader {

    public static Properties getProperties(int id,Context context) throws IOException {
        Properties properties = new Properties();
        InputStream io = context.getResources().openRawResource(id);
        properties.load(io);
        return properties;
    }

    public static Integer getResource(String name, String resSubDir, Context context) {
        Integer nameResourceID = context.getResources().getIdentifier(name, resSubDir, context.getApplicationInfo().packageName);
        if (nameResourceID == 0) {
            return null;
        } else {
            return nameResourceID;
        }
    }

    public static String readRawTextFile(Context ctx, int resId)
    {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        BufferedReader buffreader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while (( line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }
}
