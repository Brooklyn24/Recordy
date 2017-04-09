package com.bigapps.brooklyn.recordy.fragments.add;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.adapters.ProceduresPickedAdapter;
import com.bigapps.brooklyn.recordy.dataBase.DatabaseHelper;
import com.bigapps.brooklyn.recordy.dataBase.models.Client;
import com.bigapps.brooklyn.recordy.dataBase.models.Procedure;
import com.bigapps.brooklyn.recordy.dataBase.models.Record;
import com.bigapps.brooklyn.recordy.interfaces.ShowSnackbar;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AddRecordFragment extends Fragment {

    private static final int PICK_CONTACT = 1088;
    private DatabaseHelper mDatabaseHelper;
    private Calendar mFinalDate;
    private SimpleDateFormat sdfDateTextView;
    private SimpleDateFormat sdfTimeTextView;
    private Context mContext;
    private Client mClient;
    int duration;
    ProceduresPickedAdapter proceduresPickedAdapter;
    private List<Procedure> mProcedureList;
    private List<Procedure> mResultProcedureList;
    private String[] mProcedureNameStrings;
    private boolean[] mIsProcedureChecked;

    @BindView(R.id.nameETxt) TextView nameEtxt;
    @BindView(R.id.phoneEt) TextView phoneEtxt;
    @BindView(R.id.timePickTxt) TextView timePick;
    @BindView(R.id.datePickTxt) TextView datePick;
    @BindView(R.id.proceduresPickedRV) RecyclerView proceduresPickedRV;
    @BindView(R.id.addNewProceduresBtn) Button addNewProceduresBtn;

    public static AddRecordFragment newInstance() {
        AddRecordFragment fragment = new AddRecordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.proceduresPickedRV)
    void onClickRV() {
        multipleProcedureChoice();
    }

    @OnClick(R.id.addNewProceduresBtn)
    void onClickAddProc() {
        multipleProcedureChoice();
    }

    @OnClick(R.id.addClientFromContact)
    public void addFromContactOnClick() {
        AddRecordFragmentPermissionsDispatcher.showContactsWithCheck(this);
    }

    @OnClick(R.id.floatingActionButton2)
    public void fabOnClick() {
        saveRecord();
    }

    @OnClick(R.id.datePickTxt)
    public void datePickOnClick() {
        showDatePickerDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFinalDate = Calendar.getInstance();
        sdfTimeTextView = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
        sdfDateTextView = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
        try {
            mProcedureList = getHelper().getProcedureDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int listSize = mProcedureList.size();
        mProcedureNameStrings = new String[listSize];
        mIsProcedureChecked = new boolean[listSize];
        for (int i = 0; i < listSize; i++) {
            mProcedureNameStrings[i] = mProcedureList.get(i).getmProcedureName();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_record, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        proceduresPickedRV.setLayoutManager(llm);
        timePick.setText(sdfTimeTextView.format(mFinalDate.getTime()));
        datePick.setText(sdfDateTextView.format(mFinalDate.getTime()));
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AddRecordFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    public void showContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.add_new_record));
    }

    @OnClick(R.id.timePickTxt)
    public void timePickOnClick() {
        showTimePickerDialog();
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == getActivity().RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getActivity().getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                        phones.moveToFirst();
                        //TODO
                        //Permission 23+ api

                        nameEtxt.setText(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        phoneEtxt.setText(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    }
                    mClient = new Client();
                    mClient.setUri(contactData.toString());
                    mClient.setName(nameEtxt.getText().toString());
                    mClient.setPhone(phoneEtxt.getText().toString());
                }
                break;
        }
    }

    private void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener onTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mFinalDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mFinalDate.set(Calendar.MINUTE, minute);
                timePick.setText(sdfTimeTextView.format(mFinalDate.getTime()));
            }
        };
        TimePickerDialog newFragment = new TimePickerDialog(getActivity(),
                onTime,
                mFinalDate.get(Calendar.HOUR_OF_DAY),
                mFinalDate.get(Calendar.MINUTE),
                false
        );
        newFragment.show();
    }

    private void saveRecord() {
        if (isValidate()) {
            Record record = new Record();
            record.setmClientId(mClient);
            record.setmDuration(duration);
            record.setmDate(mFinalDate.getTimeInMillis());
            try {
                getHelper().getClientDao().createOrUpdate(mClient);
                getHelper().getRecordDao().create(record);
                for (Procedure procedure : mResultProcedureList) {
                    duration += procedure.getmDuration();
                    record.addProcedure(procedure);
                }
                ((ShowSnackbar) mContext).showSnackbar(getResources().getString(R.string.record_added));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private boolean isValidate() {
        if (mClient == null) {
            Snackbar.make(getView().findViewById(R.id.coordinatorLayout2), "Client is required", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (mResultProcedureList == null || mResultProcedureList.size() == 0) {
            Snackbar.make(getView().findViewById(R.id.coordinatorLayout2), "Procedures is required", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mFinalDate.set(Calendar.YEAR, year);
                mFinalDate.set(Calendar.MONTH, monthOfYear);
                mFinalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                datePick.setText(sdfDateTextView.format(mFinalDate.getTime()));
            }
        };
        DatePickerDialog newFragment = new DatePickerDialog(getActivity(),
                onDate,
                mFinalDate.get(Calendar.YEAR),
                mFinalDate.get(Calendar.MONTH),
                mFinalDate.get(Calendar.DAY_OF_MONTH)
        );
        newFragment.show();
    }

    private void multipleProcedureChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.procedures_choice);
        builder.setMultiChoiceItems(mProcedureNameStrings, mIsProcedureChecked,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                    }
                });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mResultProcedureList = new ArrayList<>();
                for (int i = 0; i < mIsProcedureChecked.length; i++) {
                    if (mIsProcedureChecked[i]) {
                        duration += mProcedureList.get(i).getmDuration();
                        mResultProcedureList.add(mProcedureList.get(i));
                    }
                }
                proceduresPickedAdapter =
                        new ProceduresPickedAdapter(mResultProcedureList);
                proceduresPickedRV.setAdapter(proceduresPickedAdapter);
                Log.d("logg", "onClick: " + mResultProcedureList.size());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private DatabaseHelper getHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = DatabaseHelper.getHelper(getContext());
        }
        return mDatabaseHelper;
    }
}
