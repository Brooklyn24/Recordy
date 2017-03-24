package com.bigapps.brooklyn.recordy.fragments.show;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.adapters.ProceduresAdapter;
import com.bigapps.brooklyn.recordy.adapters.RecordsAdapter;
import com.bigapps.brooklyn.recordy.dataBase.DatabaseHelper;
import com.bigapps.brooklyn.recordy.dataBase.models.Procedure;
import com.bigapps.brooklyn.recordy.fragments.add.AddProcedureFragment;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowProceduresFragment extends Fragment {

    private DatabaseHelper mDatabaseHelper;
    private List<Procedure> mProceduresList;
    private ProceduresAdapter mProceduresAdapter;

    @BindView(R.id.recycleView) RecyclerView recyclerView;
    @BindView(R.id.emptyRvTxt) TextView emptyRtTxt;

    @OnClick(R.id.fabAddNewProcedure)
    public void fabOnClick(){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLayout, AddProcedureFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public ShowProceduresFragment() {
    }

    public static ShowProceduresFragment newInstance() {
        ShowProceduresFragment fragment = new ShowProceduresFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mProceduresList = getHelper().getProcedureDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mProceduresAdapter = new ProceduresAdapter(mProceduresList);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mProceduresList = getHelper().getProcedureDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mProceduresAdapter = new ProceduresAdapter(mProceduresList);
        recyclerView.setAdapter(mProceduresAdapter);
        if (mProceduresAdapter.getItemCount() <= 0) {
            emptyRtTxt.setVisibility(View.VISIBLE);
        } else {
            emptyRtTxt.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_procedures, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mProceduresAdapter);
        return view;
    }

    private DatabaseHelper getHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = DatabaseHelper.getHelper(getContext());
        }
        return mDatabaseHelper;
    }

}
