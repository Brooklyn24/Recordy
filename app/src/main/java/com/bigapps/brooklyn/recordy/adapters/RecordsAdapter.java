package com.bigapps.brooklyn.recordy.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.dataBase.models.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Brooklyn on 20-Mar-17.
 */

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordsViewHolder>{

    private List<Record> mRecordsList;

    public class RecordsViewHolder extends RecyclerView.ViewHolder {
        public TextView client, date, procedure;

        public RecordsViewHolder(View view) {
            super(view);
            client = (TextView) view.findViewById(R.id.clientTxt);
            procedure = (TextView) view.findViewById(R.id.procedureTxt);
            date = (TextView) view.findViewById(R.id.dateTxt);
        }
    }

    public RecordsAdapter(List<Record> recordsList) {
        this.mRecordsList = recordsList;
    }

    @Override
    public RecordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_view_item, parent, false);

        return new RecordsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecordsViewHolder holder, int position) {
        Record record = mRecordsList.get(position);
        holder.client.setText(record.getmClientId().getName());
        Date date = new Date(record.getmDate());
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm EEE, d MMM");
        holder.date.setText(df2.format(date));
        holder.procedure.setText(record.getmProcedureId().getmProcedureName());

    }

    @Override
    public int getItemCount() {
        return mRecordsList.size();
    }
}