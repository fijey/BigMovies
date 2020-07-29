package com.fjrapp.bigmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.Preference;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.fjrapp.bigmovies.notification.DailyAppReminder;
import com.fjrapp.bigmovies.notification.DailyReleaseReminder;
import com.fjrapp.bigmovies.notification.ReminderPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.fjrapp.bigmovies.Tool.KEY_DAILY_REMINDER;
import static com.fjrapp.bigmovies.Tool.KEY_HEADER_DAILY_REMINDER;
import static com.fjrapp.bigmovies.Tool.KEY_HEADER_UPCOMING_REMINDER;
import static com.fjrapp.bigmovies.Tool.KEY_UPCOMING_REMINDER;
import static com.fjrapp.bigmovies.notification.DailyAppReminder.TYPE_ONE_TIME;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getFragmentManager().beginTransaction().replace(R.id.setting_content, new PreferenceFragment()).commit();

    }
}
