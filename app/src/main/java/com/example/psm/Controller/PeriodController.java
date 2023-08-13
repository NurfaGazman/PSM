package com.example.psm.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psm.Model.Period;
import com.example.psm.Model.ViewContact;
import com.example.psm.Model.ViewPeriod;
import com.example.psm.R;

import java.util.List;
import java.util.Vector;

public class PeriodController extends RecyclerView.Adapter<ViewPeriod> {

    private final LayoutInflater layoutInflater;
    private final Vector<Period> periods;
    private PeriodClick periodClick = null;

    public PeriodController(LayoutInflater layoutInflater, Vector<Period> periods, PeriodClick periodClick) {
        this.layoutInflater = layoutInflater;
        this.periods = periods;
        this.periodClick = periodClick;
    }

    @NonNull
    @Override
    public ViewPeriod onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewPeriod(layoutInflater.inflate(R.layout.period_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewPeriod holder, int position) {
        holder.ListPeriod(periods.get(position));
        if(periodClick != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    periodClick.clickPeriod(periods.get(holder.getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return periods.size();
    }

}
