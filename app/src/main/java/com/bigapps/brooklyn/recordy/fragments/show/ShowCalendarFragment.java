package com.bigapps.brooklyn.recordy.fragments.show;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.dataBase.DatabaseHelper;
import com.bigapps.brooklyn.recordy.dataBase.models.Record;
import com.bigapps.brooklyn.recordy.fragments.add.AddRecordFragment;
import com.bigapps.brooklyn.recordy.interfaces.RvClickedNewFragment;
import com.bigapps.brooklyn.recordy.interfaces.ShowSnackbar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ShowCalendarFragment extends Fragment
        implements MonthLoader.MonthChangeListener,
        ShowSnackbar, WeekView.EventClickListener {

    private String mMessage;
    private boolean mIsMessage;
    DatabaseHelper databaseHelper;
    SharedPreferences.Editor editor;
    SharedPreferences mSettings;
    int endTime;
    int startTime;
    int NumberOfVisibleDays;
    @BindView(R.id.calendarView) WeekView calendarView;

    public ShowCalendarFragment() {}

    public static ShowCalendarFragment newInstance() {
        ShowCalendarFragment fragment = new ShowCalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.fabAddNewRecord)
    public void fabOnClick(){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLayout, AddRecordFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mSettings= getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = mSettings.edit();
        startTime = mSettings.getInt("startTime", 0);
        endTime = mSettings.getInt("endTime", 24);
        NumberOfVisibleDays = mSettings.getInt("NumberOfVisibleDays", 3);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calendar, menu);
        menu.findItem(R.id.settingsMenu).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_calendar, container, false);
        ButterKnife.bind(this, view);
        calendarView.setMonthChangeListener(this);
        calendarView.setOnEventClickListener(this);
        calendarView.setNumberOfVisibleDays(NumberOfVisibleDays);
        return view;
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelper(getContext());
        }
        return databaseHelper;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.today_view:
                calendarView.goToToday();
                break;
            case R.id.one_day_view:
                calendarView.setNumberOfVisibleDays(1);
                editor.putInt("NumberOfVisibleDays", 1);
                editor.apply();
                break;
            case R.id.three_day_view:
                calendarView.setNumberOfVisibleDays(3);
                editor.putInt("NumberOfVisibleDays", 3);
                editor.apply();
                break;
            case R.id.one_week_view:
                calendarView.setNumberOfVisibleDays(7);
                editor.putInt("NumberOfVisibleDays", 7);
                editor.apply();
                calendarView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                calendarView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                calendarView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.calendar));
        if (mIsMessage) {
            Snackbar.make(getView().findViewById(R.id.coordinatorLayout), mMessage, Snackbar.LENGTH_SHORT).show();
            mIsMessage = false;
        }
        calendarView.setLimitTime(startTime, endTime);
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
                Log.d("logz", "onMonthChange: " + event);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public void showSnackbar(String message) {
        mMessage = message;
        mIsMessage = true;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        long id = event.getId();
        ((RvClickedNewFragment)getActivity()).ReplaceFragment(id);
    }
}
