package com.example.eyalfamily.carvoicereminder;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean m_recording;
    MediaRecorder myAudioRecorder = new MediaRecorder();
    private int m_nId;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Services.GetFilesList(this).size()!=0)
        {
            Services.AddNotification(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void recordAction(View view) throws IOException {
        Button button = (Button) findViewById(R.id.RecordButton);

        m_recording = !m_recording;

        if(m_recording)
        {
            try {
                myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                String outputFile;

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH+mm+ss");
                String formattedDateTime = df.format(c.getTime());

                outputFile = getFilesDir() + "/"+ Consts.FilePrefix + formattedDateTime + Consts.Extension;
                myAudioRecorder.setOutputFile(outputFile);


                myAudioRecorder.prepare();

                myAudioRecorder.start();

                button.setText("Stop Recording");
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }catch(Exception e)
            {
                e.printStackTrace();
                return;
            }
        }
        else
        {
            try {
                myAudioRecorder.stop();


                //myAudioRecorder.release();
                //myAudioRecorder  = null;

                button.setText("Start Recording");
                Toast.makeText(getApplicationContext(), "Recording stopped", Toast.LENGTH_LONG).show();

                Services.AddNotification(this);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return;
            }
        }


    }



/*    public void play(View view)
    {
        *//* recordFile = getFilesDir() + "/recording.3gp";
        try
        {
            if(m_mediaPlayer != null)
            {
                m_mediaPlayer.release();
            }

            m_mediaPlayer = new MediaPlayer();

            m_mediaPlayer.setDataSource(recordFile);
            m_mediaPlayer.prepare();
            m_mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        *//*
    }*/

    public void onListClick(View view) {
        Intent myIntent = new Intent(MainActivity.this,RemindersListActivity.class);
        startActivity(myIntent);
    }

    public void onDeleteReminders(MenuItem item) {
        Services.DeleteAllReminders(this);
    }


}
