package com.firebaseclound.chatonline;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ActionFile extends AppCompatActivity {

    public static void writeFile(Context context,String pathFilename,String content){
        try {
            FileOutputStream out = context.openFileOutput(pathFilename,MODE_PRIVATE);//xem Tips 1 để biết vị trí file mydata.txt
            out.write(content.getBytes());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Context context, String filename,boolean isAssets) {
        String data = "";
        try {

            InputStream inputStream;
            if (isAssets) {
                inputStream = context.getAssets().open(filename);
            }
            else {
                inputStream = context.openFileInput(filename);
            }
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            inputStream.read(bytes);
            inputStream.close();
            data = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
