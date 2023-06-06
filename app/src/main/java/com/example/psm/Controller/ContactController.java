package com.example.psm.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psm.Model.Contact;
import com.example.psm.Model.ViewContact;
import com.example.psm.R;

import java.util.Vector;

public class ContactController extends RecyclerView.Adapter<ViewContact> {

    private final LayoutInflater layoutInflater;
    private final Vector<Contact> contacts;

    public ContactController(LayoutInflater layoutInflater, Vector<Contact> contacts){
        this.contacts = contacts;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public ViewContact onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewContact(layoutInflater.inflate(R.layout.contact_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewContact holder, int position) {
    holder.setContact(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void removeItem(int position){
        contacts.remove(position);
        notifyItemRemoved(position);
    }
}
