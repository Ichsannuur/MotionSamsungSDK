package com.ics.callmotionsample.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ics.callmotionsample.CallActivity;
import com.ics.callmotionsample.MainActivity;
import com.ics.callmotionsample.Object.Contact;
import com.ics.callmotionsample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ichsan.Fatiha on 1/2/2018.
 */

public class ListContactAdapter extends RecyclerView.Adapter<ListContactAdapter.ContactViewHolder> {
    private Context context;
    private List<Contact> contactList = new ArrayList<>();

    public ListContactAdapter(Context context) {
        this.context = context;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }


    @Override
    public ListContactAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListContactAdapter.ContactViewHolder holder, int position) {
        holder.tvName.setText(getContactList().get(position).getName());
        holder.tvNumber.setText(getContactList().get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return getContactList().size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvName;
        TextView tvNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvName = (TextView)itemView.findViewById(R.id.tv_item_name);
            tvNumber = (TextView)itemView.findViewById(R.id.tv_item_number);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Contact contact = contactList.get(position);
            Intent callActivity = new Intent(context,CallActivity.class);
            callActivity.putExtra("name",contact.getName());
            callActivity.putExtra("number",contact.getNumber());
            context.startActivity(callActivity);
        }
    }
}
