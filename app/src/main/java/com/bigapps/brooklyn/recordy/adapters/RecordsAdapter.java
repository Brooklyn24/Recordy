package com.bigapps.brooklyn.recordy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.dataBase.models.Record;
import com.bigapps.brooklyn.recordy.interfaces.RvClickedNewFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Brooklyn on 20-Mar-17.
 */

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordsViewHolder>{

    private List<Record> mRecordsList;
    Context context;

    public class RecordsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView client, date, procedure;

        public RecordsViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            client = (TextView) view.findViewById(R.id.clientTxt);
            procedure = (TextView) view.findViewById(R.id.procedureTxt);
            date = (TextView) view.findViewById(R.id.dateTxt);
        }

        @Override
        public void onClick(View v) {
            long id = mRecordsList.get(getLayoutPosition()).getmId();
            Log.d("logz", "onClick: " + id);
            ((RvClickedNewFragment) context).ReplaceFragment(id);
        }
    }

    public RecordsAdapter(List<Record> recordsList, Context context) {
        this.context = context;
        this.mRecordsList = recordsList;
    }

    @Override
    public RecordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_records_adapter, parent, false);

        return new RecordsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecordsViewHolder holder, int position) {
        Record record = mRecordsList.get(position);
        int t = record.getmDuration();
        int hours = t / 60;
        int minutes = t % 60;
        holder.client.setText(record.getmClientId().getName());
        holder.procedure
                .setText(context.getString(R.string.hours) + ": " + hours + " " +
                        context.getString(R.string.minutes) + ": " +
                        minutes);
        Date date = new Date(record.getmDate());
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm EEE, d MMM");
        holder.date.setText(df2.format(date));
    }

    @Override
    public long getItemId(int position) {
        return mRecordsList.get(position).getmId();
    }

    @Override
    public int getItemCount() {
        if (mRecordsList == null) {
            return 0;
        }
        return mRecordsList.size();
    }
}