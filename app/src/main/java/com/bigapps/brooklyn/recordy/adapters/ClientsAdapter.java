package com.bigapps.brooklyn.recordy.adapters;

/**
 * Created by Brooklyn on 02-Apr-17.
 */

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigapps.brooklyn.recordy.R;
import com.bigapps.brooklyn.recordy.dataBase.models.Client;
import com.bigapps.brooklyn.recordy.dataBase.models.Procedure;

import java.util.List;

/**
 * Created by Brooklyn on 22-Mar-17.
 */

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientsViewHolder>{

    private List<Client> mClientList;

    public class ClientsViewHolder extends RecyclerView.ViewHolder {
        public TextView clientName, phone, lastVisit;

        public ClientsViewHolder(View view) {
            super(view);
            clientName = (TextView) view.findViewById(R.id.nameTxt);
            lastVisit = (TextView) view.findViewById(R.id.lastVisitTxt);
            phone = (TextView) view.findViewById(R.id.phoneTxt);
        }
    }

    public ClientsAdapter(List<Client> clientList) {
        this.mClientList = clientList;
    }

    @Override
    public ClientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_clients_adapter, parent, false);
        return new ClientsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClientsViewHolder holder, int position) {
        Client client = mClientList.get(position);
        holder.clientName.setText(client.getName());
        //TODO
        //change to format.string
        holder.phone.setText(client.getPhone());
        holder.lastVisit.setText("#####");
    }

    @Override
    public int getItemCount() {
        if (mClientList == null) {
            return 0;
        }
        return mClientList.size();
    }
}