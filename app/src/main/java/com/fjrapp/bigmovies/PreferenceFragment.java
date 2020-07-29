package com.fjrapp.bigmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fjrapp.bigmovies.Movie.Movie;
import com.fjrapp.bigmovies.notification.DailyAppReminder;
import com.fjrapp.bigmovies.notification.DailyReleaseReminder;

import java.util.ArrayList;
import java.util.List;


public class PreferenceFragment extends android.preference.PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private DailyAppReminder dailyAppReminder = new DailyAppReminder();
    private DailyReleaseReminder releaseReminder = new DailyReleaseReminder();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        SwitchPreference dailyReminderSwitch = (SwitchPreference) findPreference("key_daily_reminder");
        SwitchPreference releaseReminderSwitch = (SwitchPreference) findPreference("key_release_reminder");

        dailyReminderSwitch.setOnPreferenceChangeListener(this);
        releaseReminderSwitch.setOnPreferenceChangeListener(this);
        findPreference("key_setting_language").setOnPreferenceClickListener(this);


    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals("key_setting_language")) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        boolean isSelected = (boolean) newValue;
        if (key.equals("key_daily_reminder")) {
            if (isSelected) {
                dailyAppReminder.setOneTimeAlarm(getActivity());
            } else {
                dailyAppReminder.cancelNotification(getActivity());
            }
        }
        else {
            if (isSelected) {
                releaseReminder.setReminder(getActivity());
            }
            else {
                releaseReminder.cancelReminder(getActivity());
            }
        }
        return true;
    }
}