package com.bigapps.brooklyn.recordy.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.fragments.show.ShowCalendarFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    String[] time;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    @BindView(R.id.spinnerStart) Spinner spinnerStart;
    @BindView(R.id.spinnerEnd) Spinner spinnerEnd;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                time
        );
        spinnerEnd.setAdapter(arrayAdapter);
        spinnerStart.setAdapter(arrayAdapter);
        spinnerStart.setSelection(mSettings.getInt("startTime", 0));
        spinnerEnd.setSelection(mSettings.getInt("endTime", 23));
        spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("endTime", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("startTime", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem settingMenuItem = menu.findItem(R.id.settingsMenu);
        settingMenuItem.setVisible(false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mSettings = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = mSettings.edit();
        time = new String[25];
        for (int i = 0; i < 25; i++) {
            time[i] = i + ":00";
        }

    }
}
