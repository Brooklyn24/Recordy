package com.bigapps.brooklyn.recordy.fragments.add;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.dataBase.DatabaseHelper;
import com.bigapps.brooklyn.recordy.dataBase.models.Procedure;
import com.bigapps.brooklyn.recordy.interfaces.ShowSnackbar;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddProcedureFragment extends Fragment {

    DatabaseHelper databaseHelper;
    @BindView(R.id.procedureNameEt) EditText nameEt;
    @BindView(R.id.procedurePriceEt) EditText priceEt;
    private String[] mMinutesLabels;
    private int[] mMinutesValues;
    private ArrayAdapter mArrayAdapter;
    private int mDurationSelectedId;
    @BindView(R.id.durationSpinner) Spinner durationSpinner;
    private Context context;


    @OnClick(R.id.addProcedureFab)
    public void fabOnClick(){
        Procedure procedure = new Procedure(
                nameEt.getText().toString(),
                Integer.parseInt(priceEt.getText().toString()),
                mMinutesValues[mDurationSelectedId]
                );
        try {
            getHelper().getProcedureDao().create(procedure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getActivity().getSupportFragmentManager().popBackStack();
        ((ShowSnackbar) context).showSnackbar(getResources().getString(R.string.record_added));
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMinutesLabels = getResources().getStringArray(R.array.minutes_labels);
        mMinutesValues = new int[] {10,20,30,40,50,60,70,80,90,120,150,180};
        mArrayAdapter =  new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_1,
                        mMinutesLabels
                );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_procedure, container, false);
        ButterKnife.bind(this, view);
        durationSpinner.setAdapter(mArrayAdapter);
        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDurationSelectedId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.add_new_procedure));

    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelper(getContext());
        }
        return databaseHelper;
    }
}
