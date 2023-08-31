package com.example.psm.Model;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psm.R;

public class ViewPeriod extends RecyclerView.ViewHolder {
    private final TextView Start,End,PeriodList,cycleList;
//display data
    public ViewPeriod(@NonNull View itemView) {
        super(itemView);

        this.Start = itemView.findViewById(R.id.start);
        this.End = itemView.findViewById(R.id.end);
        this.PeriodList = itemView.findViewById(R.id.periodLenghtList);
        this.cycleList = itemView.findViewById(R.id.cycleLenghtList);


    }
    public static int total = 0;
    public static int count= 0;

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
            cycleList.setText("0");

            if(period.getCycleLength()==-1){
                //xdak data
                cycleList.setText("No Data");
            }else{
                //ad data masuk
                //method chaning
                cycleList.setText(String.valueOf(period.getCycleLength()));

                total += period.getCycleLength();
                count ++;

            }

            String s = String.valueOf(period.getPeriodLength());
            PeriodList.setText(s);


            //tambahan average
            // Calculate and display average cycle length
            if (period.getCycleCount() > 0) {
                int averageCycleLength = period.getAverageCycleLength();
                if (averageCycleLength != -1) {
                    cycleList.setText(String.valueOf(averageCycleLength));
                }
                //tambahan average
            }else{
                int average = total / count;
                cycleList.setText("Average : "+average);

            }

        }

    }
}
