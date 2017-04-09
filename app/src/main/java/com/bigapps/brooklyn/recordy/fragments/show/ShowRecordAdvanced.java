package com.bigapps.brooklyn.recordy.fragments.show;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.dataBase.DatabaseHelper;
import com.bigapps.brooklyn.recordy.dataBase.models.Record;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowRecordAdvanced extends Fragment {

    private Long mRecordId;
    private DatabaseHelper mDatabaseHelper;
    private Record mRecord;

    @BindView(R.id.clientNameTxt) TextView clientNameTxt;
    @BindView(R.id.phoneTxt) TextView phoneTxt;
    @BindView(R.id.dateTxt) TextView dateTxt;

    public ShowRecordAdvanced() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecordId = getArguments().getLong("recordId");
        try {
            mRecord = getHelper().getRecordDao().queryForId(Integer.parseInt(mRecordId.toString()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ShowRecordAdvanced newInstance(long id) {
        ShowRecordAdvanced fragment = new ShowRecordAdvanced();
        Bundle args = new Bundle();
        args.putLong("recordId", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_record_advanced, container, false);
        ButterKnife.bind(this, view);
        Log.d("logz", "onCreateView: " + mRecordId);
        clientNameTxt.setText(mRecord.getmClientId().getName());
        phoneTxt.setText(mRecord.getmClientId().getPhone());
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm EEE, d MMM", Locale.US);
        dateTxt.setText(df2.format(mRecord.getmDate()));

        return view;
    }

    private DatabaseHelper getHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = DatabaseHelper.getHelper(getContext());
        }
        return mDatabaseHelper;
    }

}
