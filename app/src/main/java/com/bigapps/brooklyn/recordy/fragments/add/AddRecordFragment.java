package com.bigapps.brooklyn.recordy.fragments.add;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bigapps.brooklyn.recordy.adapters.ProceduresSpinnerAdapter;
import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.dataBase.DatabaseHelper;
import com.bigapps.brooklyn.recordy.dataBase.models.Client;
import com.bigapps.brooklyn.recordy.dataBase.models.Procedure;
import com.bigapps.brooklyn.recordy.dataBase.models.Record;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
    DatabaseHelper databaseHelper;
    Procedure g;
    int myId;
    private Calendar finalDate;
    private SimpleDateFormat sdfDateTextView;
    private SimpleDateFormat sdfTimeTextView;
    private Client client;
    ProceduresSpinnerAdapter proceduresSpinnerAdapter;
    List<Procedure> proceduresList;

    @BindView(R.id.procedureSpinner) Spinner procedureSpinner;
    @BindView(R.id.nameETxt) EditText nameEtxt;
    @BindView(R.id.phoneEt) EditText phoneEtxt;
    @BindView(R.id.timePickTxt) TextView timePick;
    @BindView(R.id.datePickTxt) TextView datePick;

    @OnClick(R.id.addClientFromContact)

    public void addFromContactOnClick(){
        AddRecordFragmentPermissionsDispatcher.showContactsWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    public void showContacts(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @OnClick(R.id.floatingActionButton2)
    public void fabOnClick() {
        Record record = new Record();
        g = proceduresList.get(myId);
        record.setmProcedureId(g);
        record.setmDuration(g.getmDuration());
        record.setmClientId(client);
        record.setmDate(finalDate.getTimeInMillis());
        try {
            getHelper().getRecordDao().create(record);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        AddRecordFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick(R.id.datePickTxt)
    public void datePickOnClick() {
        DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                finalDate.set(Calendar.YEAR, year);
                finalDate.set(Calendar.MONTH, monthOfYear);
                finalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                datePick.setText(sdfDateTextView.format(finalDate.getTime()));
            }
        };
        DatePickerDialog newFragment = new DatePickerDialog(getActivity(),
                onDate,
                finalDate.get(Calendar.YEAR),
                finalDate.get(Calendar.MONTH),
                finalDate.get(Calendar.DAY_OF_MONTH)
        );
        newFragment.show();
    }

    @OnClick(R.id.timePickTxt)
    public void timePickOnClick() {
        TimePickerDialog.OnTimeSetListener onTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                finalDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                finalDate.set(Calendar.MINUTE, minute);
                timePick.setText(sdfTimeTextView.format(finalDate.getTime()));
            }
        };
        TimePickerDialog newFragment = new TimePickerDialog(getActivity(),
                onTime,
                finalDate.get(Calendar.HOUR_OF_DAY),
                finalDate.get(Calendar.MINUTE),
                false
        );
        newFragment.show();
    }

    public static AddRecordFragment newInstance() {
        AddRecordFragment fragment = new AddRecordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == getActivity().RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  getActivity().getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                        phones.moveToFirst();
                        //TODO
                        //Permission 23+ api

                        nameEtxt.setText(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        phoneEtxt.setText(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    }
                    client = new Client();
                    client.setUri(contactData.toString());
                    client.setName(nameEtxt.getText().toString());
                    client.setPhone(phoneEtxt.getText().toString());
                    try {
                        getHelper().getClientDao().create(client);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_record, container, false);
        ButterKnife.bind(this, view);
        timePick.setText(sdfTimeTextView.format(finalDate.getTime()));
        datePick.setText(sdfDateTextView.format(finalDate.getTime()));
        procedureSpinner.setAdapter(proceduresSpinnerAdapter);
        procedureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finalDate = Calendar.getInstance();
        sdfTimeTextView = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
        sdfDateTextView = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
        try {
            proceduresList = getHelper().getProcedureDao().queryForAll();
            Log.d("logz", "onCreate: " + proceduresList.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        proceduresSpinnerAdapter = new ProceduresSpinnerAdapter(proceduresList);
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelper(getContext());
        }
        return databaseHelper;
    }


}
