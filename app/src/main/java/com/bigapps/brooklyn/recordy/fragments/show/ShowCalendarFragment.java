package com.bigapps.brooklyn.recordy.fragments.show;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.dataBase.DatabaseHelper;
import com.bigapps.brooklyn.recordy.dataBase.models.Record;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShowCalendarFragment extends Fragment
        implements MonthLoader.MonthChangeListener
{

    DatabaseHelper databaseHelper;
    @BindView(R.id.calendarView) WeekView calendarView;

    public ShowCalendarFragment() {}

    public static ShowCalendarFragment newInstance() {
        ShowCalendarFragment fragment = new ShowCalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_calendar, container, false);
        ButterKnife.bind(this, view);
        calendarView.setMonthChangeListener(this);
        return view;
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelper(getContext());
        }
        return databaseHelper;
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<>();
        try {
            List<Record> list = getHelper().getRecordDao().queryForAll();
            for (Record record : list) {
                WeekViewEvent event = record.toEvent(newMonth);
                if (event != null){
                events.add(event);}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
}
