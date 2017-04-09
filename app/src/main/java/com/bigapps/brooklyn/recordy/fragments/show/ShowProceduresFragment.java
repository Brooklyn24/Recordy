package com.bigapps.brooklyn.recordy.fragments.show;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.adapters.ClientsAdapter;
import com.bigapps.brooklyn.recordy.adapters.ProceduresAdapter;
import com.bigapps.brooklyn.recordy.dataBase.DatabaseHelper;
import com.bigapps.brooklyn.recordy.dataBase.models.Procedure;
import com.bigapps.brooklyn.recordy.fragments.add.AddProcedureFragment;
import com.bigapps.brooklyn.recordy.interfaces.ShowSnackbar;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowProceduresFragment extends Fragment implements ShowSnackbar {

    private DatabaseHelper mDatabaseHelper;
    private List<Procedure> mProcedureList;
    private ProceduresAdapter mProceduresAdapter;
    private String mMessage;
    private boolean mIsMessage;

    @BindView(R.id.recycleView) RecyclerView recyclerView;
    @BindView(R.id.proceduresEmptyTxt) TextView emptyRtTxt;
    @BindView(R.id.fabAddNewProcedure) FloatingActionButton fab;


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

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.procedures));
        try {
            mProcedureList = getHelper().getProcedureDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mProceduresAdapter = new ProceduresAdapter(mProcedureList);
        recyclerView.setAdapter(mProceduresAdapter);
        if (mProceduresAdapter.getItemCount() <= 0) {
            emptyRtTxt.setVisibility(View.VISIBLE);
        } else {
            emptyRtTxt.setVisibility(View.GONE);
        }
        if (mIsMessage) {
            Snackbar.make(getView().findViewById(R.id.coordinatorLayout), mMessage, Snackbar.LENGTH_SHORT).show();
            mIsMessage = false;
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

    private DatabaseHelper getHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = DatabaseHelper.getHelper(getContext());
        }
        return mDatabaseHelper;
    }

    @Override
    public void showSnackbar(String message) {
        mMessage = message;
        mIsMessage = true;
    }

}
