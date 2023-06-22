package com.example.psm.Model;

import android.graphics.Color;
import android.widget.Toast;

import com.example.psm.PeriodHome;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;


public class Period {

    private int user_Id;
    private int period_Id;
    private String start_date;
    private String end_date;
    private CalendarDay start;
    private CalendarDay end;
    private Integer highlight;


    //bahagian calculation
    private LocalDate date_start;
    private LocalDate date_end;

    public Integer getHighlight() {
        return highlight;
    }

    public void setHighlight(Integer highlight) {
        this.highlight = highlight;
    }


    public CalendarDay getStart() {
        return start;
    }

    public void setStart(CalendarDay start) {
        this.start = start;
    }

    public CalendarDay getEnd() {
        return end;
    }

    public void setEnd(CalendarDay end) {
        this.end = end;
    }


    public int getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
    }

    public int getPeriod_Id() {
        return period_Id;
    }

    public void setPeriod_Id(int period_Id) {
        this.period_Id = period_Id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {

        this.start_date = start_date;
        //generate localdate
        setDate_start(new LocalDate(start_date));

    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
        setDate_end(new LocalDate(end_date));
    }

    public LocalDate getDate_start() {
        return date_start;
    }

    public void setDate_start(LocalDate date_start) {
        this.date_start = date_start;
        setStart(CalendarDay.from(date_start.getYear(),date_start.getMonthOfYear(),date_start.getDayOfMonth()));
    }

    public LocalDate getDate_end() {
        return date_end;
    }

    public void setDate_end(LocalDate date_end) {
        this.date_end = date_end;
        setEnd(CalendarDay.from(date_end.getYear(),date_end.getMonthOfYear(),date_end.getDayOfMonth()));
    }

    public  DayViewDecorator getDecorator(){
        if(end == null){
            LocalDate sekarang = LocalDate.now();
            setEnd(CalendarDay.from(sekarang.getYear(),sekarang.getMonthOfYear(),sekarang.getDayOfMonth()));
            //set warna sekarang
            if(highlight == null){
                highlight = Color.YELLOW;
            }

        }else{
            //set warna previous date
            highlight = Color.GREEN;
        }
        return new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {


//start - user highlight days && end - stop highlight

                return ((day.isAfter(start)|| day.equals(start)) &&
                        (day.isBefore(end)) || day.equals(end) );
            }

            @Override
            public void decorate(DayViewFacade view) {

                view.addSpan(new DotSpan(50,highlight));

            }
        };
    }
}
