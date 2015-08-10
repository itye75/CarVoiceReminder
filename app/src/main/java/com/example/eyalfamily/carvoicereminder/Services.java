package com.example.eyalfamily.carvoicereminder;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EyalFamily on 10/08/2015.
 */
public class Services{
        public static void DeleteAllReminders(Context p_context) {
            List<String> remindersList = new ArrayList<String>();

            String path = p_context.getFilesDir().toString();

            File dir = new File(path);
            File fileList[] = dir.listFiles();

            for (int i=0; i < fileList.length; i++)
            {
                fileList[i].delete();
            }
        }
}
