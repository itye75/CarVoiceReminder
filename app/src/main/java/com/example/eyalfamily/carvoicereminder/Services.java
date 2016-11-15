package com.example.eyalfamily.carvoicereminder;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by EyalFamily on 10/08/2015.
 */
public class Services{
    private static int m_nId=0;

    public static List<ReminderRecord> GetFilesList(Context p_context) {
        List<ReminderRecord> remindersList = new ArrayList<>();

        String path = p_context.getFilesDir().toString();

        File dir = new File(path);

        FilenameFilter fileFilter = new FilenameFilter() {
            File f;
            public boolean accept(File dir, String name) {
                if(name.endsWith(".3gp")) {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        };

        File fileList[] = dir.listFiles(fileFilter);


        Log.d("Files", "Size: " + fileList.length);
        for (int i=0; i < fileList.length; i++)
        {
            Log.d("Files", "FileName:" + fileList[i].getName());
            remindersList.add(new ReminderRecord(fileList[i], formatFileName(fileList[i].getName())));
        }

        Collections.sort(remindersList, new Comparator<ReminderRecord>() {
            @Override
            public int compare(ReminderRecord s1, ReminderRecord s2) {
                return s1.ReminderText.compareTo(s2.ReminderText);
            }
        });
//        Arrays.sort(remindersList, new Comparator<ReminderRecord>() {
//            @Override
//            public int compare(ReminderRecord entry1, ReminderRecord entry2) {
//                return entry1.ReminderText.compareTo(entry2.ReminderText);
//            }
//        });

        return remindersList;
    }

    private static String formatFileName(String p_fileName) {
        return p_fileName.replace(Consts.Extension, "").replace(Consts.FilePrefix, "").replace("_"," ").replace("-","/").replace("+",":");
    }

    public static void DeleteAllReminders(Context p_context) {
            List<String> remindersList = new ArrayList<String>();

            String path = p_context.getFilesDir().toString();

            File dir = new File(path);
            File fileList[] = dir.listFiles();

            for (int i=0; i < fileList.length; i++)
            {
                fileList[i].delete();
            }

            RemoveNotification(p_context);
        }

    public static void AddNotification(Context p_this) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(p_this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("You have a reminder");

        Intent resultIntent = new Intent(p_this, RemindersListActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        p_this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) p_this.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(m_nId, mBuilder.build());
    }

    public static void RemoveNotification(Context p_this) {
        NotificationManager mNotificationManager =
                (NotificationManager) p_this.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancel(m_nId);
    }
}
