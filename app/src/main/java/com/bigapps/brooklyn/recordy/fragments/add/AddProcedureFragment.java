package com.bigapps.brooklyn.recordy.fragments.add;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.dataBase.DatabaseHelper;
import com.bigapps.brooklyn.recordy.dataBase.models.Procedure;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddProcedureFragment extends Fragment {

    DatabaseHelper databaseHelper;
    @BindView(R.id.procedureNameEt) EditText nameEt;
    @BindView(R.id.procedurePriceEt) EditText priceEt;
    @BindView(R.id.procedureDurationEt) EditText durationEt;


    @OnClick(R.id.addProcedureFab)
    public void fabOnClick(){
        Procedure procedure = new Procedure(
                nameEt.getText().toString(),
                Integer.parseInt(priceEt.getText().toString()),
                Integer.parseInt(durationEt.getText().toString())
                );
        try {
            getHelper().getProcedureDao().create(procedure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public AddProcedureFragment() {
    }

    public static AddProcedureFragment newInstance() {
        AddProcedureFragment fragment = new AddProcedureFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_new_procedure, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelper(getContext());
        }
        return databaseHelper;
    }
}
