package com.example.psm.Model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psm.R;

public class ViewContact extends RecyclerView.ViewHolder {
    private final TextView contactName, contactNo;

    public ViewContact(@NonNull View itemView){
        super(itemView);
        this.contactName = itemView.findViewById(R.id.name_contact);
        this.contactNo = itemView.findViewById(R.id.contact_no);

    }

    public void setContact(Contact contact) {
        contactName.setText(contact.getName());
        contactNo.setText(contact.getContact_no());
    }
}
