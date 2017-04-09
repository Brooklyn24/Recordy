package com.bigapps.brooklyn.recordy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.dataBase.models.Procedure;

import java.util.List;

/**
 * Created by Brooklyn on 21-Mar-17.
 */

public class ProceduresSpinnerAdapter extends BaseAdapter {

    private List<Procedure> mProcedureList;

    public ProceduresSpinnerAdapter(List<Procedure> data) {
        mProcedureList = data;
    }

    @Override
    public int getCount() {
        if (mProcedureList == null) {
            return 0;
        }
        return mProcedureList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProcedureList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mProcedureList.get(position).getmId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spinner_items, parent, false);
        ((TextView) view.findViewById(R.id.spinneItem))
                .setText(mProcedureList.get(position).getmProcedureName());
        return view;
    }
}
