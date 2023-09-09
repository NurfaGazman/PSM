package com.example.psm.Model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psm.R;

public class ViewPeriod extends RecyclerView.ViewHolder {
    private final TextView Start,End,PeriodList,cycleList;
    //display data
    private final int averageCycleLength; // Add this variable


    public ViewPeriod(@NonNull View itemView, int averageCycleLength) { // Modify the constructor
        super(itemView);

        this.Start = itemView.findViewById(R.id.start);
        this.End = itemView.findViewById(R.id.end);
        this.PeriodList = itemView.findViewById(R.id.periodLenghtList);
        this.cycleList = itemView.findViewById(R.id.cycleLenghtList);

        this.averageCycleLength = averageCycleLength; // Initialize averageCycleLength
    }


//    public void ListPeriod(Period period) {
//        if (period.isHeader()) {
//            Start.setText("Start Date");
//            End.setText("End Date");
//            PeriodList.setText("Period Length");
//            cycleList.setText("Cycle Length");
//
//        } else {
//            Start.setText(period.getStart_date());
//            End.setText(period.getEnd_date());
//
//            // Period_Length.
//            if (period.getCycleLength() == -1) {
//                // No data
//                cycleList.setText("No Data");
//            } else {
//                // Use the passed averageCycleLength value
//                cycleList.setText("" + averageCycleLength);
//            }
//
//            String s = String.valueOf(period.getPeriodLength());
//            PeriodList.setText(s);
//        }
//    }



    public void ListPeriod(Period period){

        if(period.isHeader()){

            Start.setText("Start Date");
            End.setText("End Date");
            PeriodList.setText("Period Length");
            cycleList.setText("Cycle Length");

            //display data

        }else{

            Start.setText(period.getStart_date());
            End.setText(period.getEnd_date());

            //Period_Lenght.
            cycleList.setText("0");
            if(period.getCycleLength()==-1){
                //xdak data
                 cycleList.setText("" + averageCycleLength);
            }else{
                //ad data masuk
                //method chaning

                cycleList.setText(String.valueOf(period.getCycleLength()));
            }

            String s = String.valueOf(period.getPeriodLength());
            PeriodList.setText(s);
        }

    }
}


