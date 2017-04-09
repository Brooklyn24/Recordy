package com.bigapps.brooklyn.recordy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.dataBase.models.Procedure;

import java.util.List;

/**
 * Created by Brooklyn on 05-Apr-17.
 */

public class ProceduresPickedAdapter extends RecyclerView.Adapter<ProceduresPickedAdapter.ProceduresViewHolder>{

    private List<Procedure> mProcedureList;

    public class ProceduresViewHolder extends RecyclerView.ViewHolder {
        public TextView procedureName;

        public ProceduresViewHolder(View view) {
            super(view);
            procedureName = (TextView) view.findViewById(android.R.id.text1);
        }
    }

    public ProceduresPickedAdapter(List<Procedure> procedureList) {
        this.mProcedureList = procedureList;
    }

    @Override
    public ProceduresPickedAdapter.ProceduresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ProceduresPickedAdapter.ProceduresViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProceduresPickedAdapter.ProceduresViewHolder holder, int position) {
        Procedure procedure = mProcedureList.get(position);
        holder.procedureName.setText(procedure.getmProcedureName());
    }

    @Override
    public int getItemCount() {
        if (mProcedureList == null) {
            return 0;
        }
        return mProcedureList.size();
    }
}