package com.example.eyalfamily.carvoicereminder;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private boolean m_recording;
    MediaRecorder myAudioRecorder = new MediaRecorder();
    MediaPlayer m_mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Button button = (Button)findViewById(R.id.RecordButton);

        m_recording = !m_recording;

        if(m_recording)
        {
            button.setText("Stop Recording");

            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            String outputFile;//
            outputFile = getFilesDir() + "/recording.3gp";
            myAudioRecorder.setOutputFile(outputFile);


            myAudioRecorder.prepare();
            myAudioRecorder.start();

            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
        }
        else
        {
            button.setText("Start Recording");
            myAudioRecorder.stop();
            //myAudioRecorder.release();
            //myAudioRecorder  = null;

            Toast.makeText(getApplicationContext(), "Recording stopped", Toast.LENGTH_LONG).show();
        }


    }

    public void play(View view)
    {
        String recordFile = getFilesDir() + "/recording.3gp";
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
    }
}
