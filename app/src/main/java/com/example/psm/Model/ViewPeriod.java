package com.example.psm.Model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psm.R;

public class ViewPeriod extends RecyclerView.ViewHolder {
    private final TextView Start,End,Period_Lenght,Cycle_Lenght;

    public ViewPeriod(@NonNull View itemView) {
        super(itemView);
        this.Start = itemView.findViewById(R.id.start);
        this.End = itemView.findViewById(R.id.end);
        this.Period_Lenght = itemView.findViewById(R.id.periodLenghtList);
        this.Cycle_Lenght = itemView.findViewById(R.id.cycleLenghtList);

    }
    public void ListPeriod(Period period){
        Start.setText(period.getStart_date());
        End.setText(period.getEnd_date());

        //Period_Lenght.
        Period_Lenght.setText(period.getPeriodLength());
        Cycle_Lenght.setText("0");
    }
}
