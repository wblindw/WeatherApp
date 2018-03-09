package com.blind.wakemeup.utils;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Utility class to load context data.
 */
public abstract class ContextLoader {

    /**
     * Read and return properties from {@link Context} and the ressource id.
     * @param ctx the app. {@link Context}.
     * @param resId the resource id of the file.
     * @return the content of the file. Can not be <code>null</code>. Can be empty if empty file or if error.
     */
    public static Properties getProperties(Context ctx, int resId) {
        final Properties properties = new Properties();
        InputStream io = ctx.getResources().openRawResource(resId);
        try {
            properties.load(io);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * Get resource id from app. {@link Context}, directory, and name
     * @param ctx the app. {@link Context}.
     * @param resSubDir the directory name the resource is stored. Can not be <code>null</code>, empty or whitespace only.
     * @param name the name of the resource. Can not be <code>null</code>, empty or whitespace only.
     * @return the resource id. Can be <code>null</code>.
     */
    public static Integer getResource(Context ctx, String resSubDir, String name) {

        if(ctx == null || StringUtils.isAllBlank(resSubDir) || StringUtils.isAllBlank(name)) {
            return null;
        }

        final Integer nameResourceID = ctx.getResources().getIdentifier(name, resSubDir, ctx.getApplicationInfo().packageName);
        if (nameResourceID == 0) {
            return null;
        } else {
            return nameResourceID;
        }
    }

    /**
     * Read file content from {@link Context} and the app. ressource id.
     * @param ctx the app. {@link Context}.
     * @param resId the resource id of the file.
     * @return the content of the file. Can be <code>null</code> or empty.
     */
    public static String readRawTextFile(Context ctx, int resId)
    {
        final InputStream inputStream = ctx.getResources().openRawResource(resId);
        final BufferedReader buffReader = new BufferedReader(new InputStreamReader(inputStream));

        final StringBuilder text = new StringBuilder();

        String line;
        try {
            while (( line = buffReader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }
}
