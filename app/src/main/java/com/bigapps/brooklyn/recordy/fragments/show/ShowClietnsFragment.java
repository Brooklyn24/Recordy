package com.bigapps.brooklyn.recordy.fragments.show;


import android.os.Bundle;
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
import com.bigapps.brooklyn.recordy.dataBase.models.Client;
import com.bigapps.brooklyn.recordy.fragments.add.AddRecordFragment;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowClietnsFragment extends Fragment {

    @BindView(R.id.recycleView) RecyclerView recyclerView;
    private List<Client> mClientList;
    private ClientsAdapter mClientAdapter;
    private DatabaseHelper mDatabaseHelper;

    @BindView(R.id.emptyRtTxt) TextView emptyRtTxt;

    public ShowClietnsFragment() {
    }

    public static ShowClietnsFragment newInstance() {
        ShowClietnsFragment fragment = new ShowClietnsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.clients));
        try {
            mClientList = getHelper().getClientDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mClientAdapter = new ClientsAdapter(mClientList);
        recyclerView.setAdapter(mClientAdapter);
        if (mClientAdapter.getItemCount() <= 0) {
            emptyRtTxt.setVisibility(View.VISIBLE);
        } else {
            emptyRtTxt.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_clietns, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        return view;
    }

    private DatabaseHelper getHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = DatabaseHelper.getHelper(getContext());
        }
        return mDatabaseHelper;
    }

}
