package com.fjrapp.bigmovies.notification;

import android.content.Context;
import android.content.SharedPreferences;

public class ReminderPreferences {
    public String PREF_NAME ="ReminderPreferences";
    public String KEY_REMINDER_DAILY = "DailyReminder";
    public String KEY_REMINDER_MESSAGE_DAILY = "message";
    public String KEY_REMINDER_MESSAGE_RELEASE = "message";
    public String KEY_REMINDER_RELEASE = "release";
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public ReminderPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void setReminderReleaseTime(String time){
        editor.putString(KEY_REMINDER_RELEASE,time);
        editor.commit();
    }
    public void setReminderReleaseMessage (String message){
        editor.putString(KEY_REMINDER_MESSAGE_RELEASE,message);
    }
    public void setReminderDailyTime(String time){
        editor.putString(KEY_REMINDER_DAILY,time);
        editor.commit();
    }
    public void setReminderDailyMessage(String message){
        editor.putString(KEY_REMINDER_MESSAGE_DAILY,message);
    }
}
