package com.bigapps.brooklyn.recordy.fragments.show;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.bigapps.brooklyn.recordy.interfaces.ShowSnackbar;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowRecordsFragment extends Fragment implements ShowSnackbar {

    DatabaseHelper databaseHelper;
    List<Record> records;
    private String mMessage;
    private boolean mIsMessage;
    RecordsAdapter recordsAdapter;
    @BindView(R.id.recyclyView) RecyclerView recyclerView;
    @BindView(R.id.emptyRvTxt) TextView emptyRvTxt;
    @BindView(R.id.fabAddNewRecord) FloatingActionButton fab;

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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fab.isShown())
                    fab.hide();
                else if (dy < 0 && !fab.isShown()){
                    fab.show();
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("logd", "onViewCreated: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.records));
        try {
            records = getHelper().getRecordDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        recordsAdapter = new RecordsAdapter(records, getActivity());
        recyclerView.setAdapter(recordsAdapter);
        if (recordsAdapter.getItemCount() <= 0) {
            emptyRvTxt.setVisibility(View.VISIBLE);
        } else {
            emptyRvTxt.setVisibility(View.GONE);
        }
        if (mIsMessage) {
            Snackbar.make(getView().findViewById(R.id.coordinatorLayout), mMessage, Snackbar.LENGTH_SHORT).show();
            mIsMessage = false;
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelper(getContext());
        }
        return databaseHelper;
    }

    @Override
    public void showSnackbar(String message) {
        mMessage = message;
        mIsMessage = true;
    }
}
