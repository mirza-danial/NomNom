package com.example.danialmirza.nomnom.Utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;

public class saveUserData {
    File file;
    Context context;

    public saveUserData(Context context) {
        this.context = context;
        file = new File(context.getFilesDir(), "userData.txt");
    }
    public void saveData(String data)
    {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
