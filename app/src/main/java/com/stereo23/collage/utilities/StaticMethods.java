package com.stereo23.collage.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;

import com.stereo23.collage.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StaticMethods {
    public static String streamToString(InputStream inputStream){
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total.toString();
    }
    public static ArrayList<URL> parseImagesJSON(JSONArray jsonArray){
        ArrayList<URL> list = new ArrayList<URL>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String string = jsonArray.getJSONObject(i).getJSONObject("images").getJSONObject("low_resolution").getString("url");
                URL url = new URL(string);
                list.add(url);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static int trueCount(boolean[] array){
        int count=0;
        for (boolean anArray : array) {
            if (anArray) count++;
        }
        return count;
    }

    public static List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (int i=0; i<files.length;i++) {
            Log.d(MainActivity.TAG,"here");
            if (files[i].isDirectory()) {
                inFiles.addAll(getListFiles(files[i]));
            } else {
                if(files[i].getName().endsWith(".jpg")){
                    inFiles.add(files[i]);
                }
            }
        }
        return inFiles;
    }
    public static void cleanAppFolder() {
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/collage/");
        File[] Files = root.listFiles();
        if(Files != null) {
            int j;
            for(j = 0; j < Files.length; j++) {
                System.out.println(Files[j].getAbsolutePath());
                System.out.println(Files[j].delete());
            }
        }
    }

    public static Bitmap mergeFilesToBitmap(List<File> files){
        Bitmap previous = BitmapFactory.decodeFile(files.get(0).getAbsolutePath());
        for (int i=1; i < files.size(); i++){
            Bitmap current = BitmapFactory.decodeFile(files.get(i).getAbsolutePath());
            previous = combineImagesHorizontally(previous, current);
        }

        return previous;
    }

    public static Bitmap combineImagesHorizontally(Bitmap c, Bitmap s) {
        Bitmap cs = null;

        int width, height = 0;

        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth() + s.getWidth();
            height = c.getHeight();
        } else {
            width = s.getWidth() + s.getWidth();
            height = c.getHeight();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, c.getWidth(), 0f, null);
        return cs;
    }
    public static Bitmap combineImagesVertically(Bitmap c, Bitmap s) {
        Bitmap cs = null;

        int width, height = 0;

        if(c.getHeight() > s.getHeight()) {
            width = c.getWidth() + s.getWidth();
            height = c.getHeight();
        } else {
            height = s.getHeight() + s.getHeight();
            width = c.getWidth();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, 0f, c.getHeight(), null);
        return cs;
    }
}


