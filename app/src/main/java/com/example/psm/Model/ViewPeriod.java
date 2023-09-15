package com.example.psm.Model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psm.R;

import org.joda.time.LocalDate;

public class ViewPeriod extends RecyclerView.ViewHolder {
    private final TextView Start,End,PeriodList,cycleList;
    //display data


    public ViewPeriod(@NonNull View itemView) { // Modify the constructor
        super(itemView);

        this.Start = itemView.findViewById(R.id.start);
        this.End = itemView.findViewById(R.id.end);
        this.PeriodList = itemView.findViewById(R.id.periodLenghtList);
        this.cycleList = itemView.findViewById(R.id.cycleLenghtList);


    }





    public void ListPeriod(Period period){


        if (period.isHeader()) {
            Start.setText("Start Date");
            End.setText("End Date");
            PeriodList.setText("Period Length");
            cycleList.setText("Cycle Length");

        } else {
            Start.setText(period.getStart_date()); //set utk date //setDateStart kt textView .letk parameter //start:textview.
            End.setText(period.getEnd_date());

            // Period_Length.
            if (period.getCycleLength() == -1) {
                // No data
                cycleList.setText("No Data");
            } else {

                cycleList.setText("" +period.getCycleLength());
            }

            String s = String.valueOf(period.getPeriodLength());
            PeriodList.setText(s);
        }
    }


}


