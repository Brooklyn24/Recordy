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
 * Created by Brooklyn on 22-Mar-17.
 */

public class ProceduresAdapter extends RecyclerView.Adapter<ProceduresAdapter.ProceduresViewHolder>{

    private List<Procedure> mProcedureList;

    public class ProceduresViewHolder extends RecyclerView.ViewHolder {
        public TextView procedureName, duration, price;

        public ProceduresViewHolder(View view) {
            super(view);
            procedureName = (TextView) view.findViewById(R.id.nameTxt);
            price = (TextView) view.findViewById(R.id.priceTxt);
            duration = (TextView) view.findViewById(R.id.durationTxt);
        }
    }

    public ProceduresAdapter(List<Procedure> procedureList) {
        this.mProcedureList = procedureList;
    }

    @Override
    public ProceduresAdapter.ProceduresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_procedure_adapter, parent, false);
        return new ProceduresViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProceduresViewHolder holder, int position) {
        Procedure procedure = mProcedureList.get(position);
        holder.procedureName.setText(procedure.getmProcedureName());
        //TODO
        //change to format.string
        holder.duration.setText(Integer.toString(procedure.getmDuration()));
        holder.price.setText(Integer.toString(procedure.getmPrice()));
    }

    @Override
    public int getItemCount() {
        if (mProcedureList == null) {
            return 0;
        }
        return mProcedureList.size();
    }
}