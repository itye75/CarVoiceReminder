package com.example.eyalfamily.carvoicereminder;

import java.io.File;

/**
 * Created by EyalFamily on 13/08/2015.
 */
public class ReminderRecord {
    public ReminderRecord(File p_file, String p_text)
    {
        ReminderFile = p_file;
        ReminderText = p_text;
    }

    @Override
    public String toString() {
        return ReminderText;
    }

    public File ReminderFile;
    public String ReminderText;
}
