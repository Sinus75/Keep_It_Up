package com.example.keepitup;

import static com.example.keepitup.MainActivity.APP_PREFERENCES_NOTIFICATIONS;
import static com.example.keepitup.MainActivity.cancelNotifications;
import static com.example.keepitup.MainActivity.editor;
import static com.example.keepitup.MainActivity.setNotifications;
import static com.example.keepitup.MainActivity.settings;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

public class SettingsFragment extends Fragment {
    CheckBox cbAllowNotification;
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_settings, container, false);
        initFields(thisView);
        initAdapters(thisView);
        return thisView;
    }

    private void initFields(View thisView) {
        cbAllowNotification = thisView.findViewById(R.id.checkBox_Allow_Notifications);
    }

    private void initAdapters(View thisView) {
        cbAllowNotification.setChecked(settings.getBoolean(APP_PREFERENCES_NOTIFICATIONS, false));

        cbAllowNotification.setOnClickListener(view -> {
            Context context = view.getContext();
            boolean checked = cbAllowNotification.isChecked();

            editor.putBoolean(APP_PREFERENCES_NOTIFICATIONS, checked);
            editor.apply();
            if (checked) setNotifications(context);
            else cancelNotifications(context);
        });
    }
}