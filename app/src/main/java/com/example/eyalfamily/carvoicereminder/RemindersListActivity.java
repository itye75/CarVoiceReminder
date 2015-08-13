package com.example.eyalfamily.carvoicereminder;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RemindersListActivity extends AppCompatActivity {

    private List<String> filesList;
    MediaPlayer m_mediaPlayer;
    private android.content.Context m_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        m_context = this;
        buildList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        buildList();
    }

    private void buildList() {
        ListView lv = (ListView) findViewById(R.id.RemindersList);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<ReminderRecord> your_array_list = getFilesList();

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<ReminderRecord> arrayAdapter = new ArrayAdapter<>(
                this,
                R.layout.my_text_view,
                your_array_list );

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                playReminder(parent, position);
            }
        });

        AdapterView.OnItemLongClickListener listener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ReminderRecord reminderRecord = (ReminderRecord) parent.getAdapter().getItem(position);

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                reminderRecord.ReminderFile.delete();
                                buildList();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
                builder.setMessage("Delete?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                return true;
            }
        };
        lv.setOnItemLongClickListener(listener);
    }

    public void playReminder(AdapterView<?> parent, int position) {
        try {
            if (m_mediaPlayer != null) {
                m_mediaPlayer.release();
            }

            m_mediaPlayer = new MediaPlayer();

            ReminderRecord reminderRecord = (ReminderRecord) parent.getAdapter().getItem(position);
            m_mediaPlayer.setDataSource(getFilesDir() + "/"+reminderRecord.ReminderFile.getName());
            m_mediaPlayer.prepare();
            m_mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reminders, menu);
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


    public void onBack(View view) {
        Intent myIntent = new Intent(RemindersListActivity.this,MainActivity.class);
        startActivity(myIntent);
    }

    public List<ReminderRecord> getFilesList() {
        List<ReminderRecord> remindersList = new ArrayList<>();

        String path = getFilesDir().toString();

        File dir = new File(path);
        File fileList[] = dir.listFiles();
        Log.d("Files", "Size: "+ fileList.length);
        for (int i=0; i < fileList.length; i++)
        {
            Log.d("Files", "FileName:" + fileList[i].getName());
            remindersList.add(new ReminderRecord(fileList[i], formatFileName(fileList[i].getName())));
        }
        return remindersList;
    }

    private String formatFileName(String p_fileName) {
        return p_fileName.replace(Consts.Extension, "").replace(Consts.FilePrefix, "").replace("_"," ").replace("-","/").replace("+",":");
    }

    public void onDeleteReminders(MenuItem item) {
        Services.DeleteAllReminders(this);
        buildList();
    }
}
