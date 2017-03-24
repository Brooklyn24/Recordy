package com.bigapps.brooklyn.recordy.fragments.show;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigapps.brooklyn.recordy.adapters.RecordsAdapter;
import com.bigapps.brooklyn.recordy.dataBase.models.Record;
import com.bigapps.brooklyn.recordy.fragments.add.AddRecordFragment;
import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.dataBase.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowRecordsFragment extends Fragment {

    DatabaseHelper databaseHelper;
    List<Record> records;
    RecordsAdapter recordsAdapter;
    @BindView(R.id.recyclyView) RecyclerView recyclerView;
    @BindView(R.id.emptyRvTxt) TextView emptyRtTxt;

    @OnClick(R.id.fabAddNewRecord)
    public void fabOnClick(){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLayout, AddRecordFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public ShowRecordsFragment() {
    }

    public static ShowRecordsFragment newInstance() {
        ShowRecordsFragment fragment = new ShowRecordsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_all_records, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            records = getHelper().getRecordDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        recordsAdapter = new RecordsAdapter(records);
        recyclerView.setAdapter(recordsAdapter);
        if (recordsAdapter.getItemCount() <= 0) {
            emptyRtTxt.setVisibility(View.VISIBLE);
        } else {
            emptyRtTxt.setVisibility(View.GONE);
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelper(getContext());
        }
        return databaseHelper;
    }


}
