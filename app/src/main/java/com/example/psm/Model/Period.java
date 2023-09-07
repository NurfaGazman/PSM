package com.example.psm.Model;

import android.graphics.Color;
import android.widget.Toast;

import com.example.psm.Controller.Row;
import com.example.psm.PeriodHome;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;
import java.util.Vector;


public class Period extends Row {

    private int user_Id;
    private int period_Id;
    private String start_date;
    private String end_date;
    private CalendarDay start;
    private CalendarDay end;
    private Integer highlight;

    //cycleLength
    private int cycleLength;

    //tambhan utk average
    //private int totalCycleLength;
    private int cycleCount;



    //bahagian calculation
    private LocalDate date_start;
    private LocalDate date_end;

    public Period(){ //constructor

        //default value utk tau ni bukan dari database

        period_Id = -1;  //variable
        cycleLength = -1;

    }

public Period(String start_date,int day){
    setStart_date(start_date); //panggil method
    setEnd_date(date_start.plusDays(day).toString("yyyy-MM-dd"));

}
    public Integer getHighlight() {  //method

        return highlight; //variable
    }

    public void setHighlight(Integer highlight) {

        this.highlight = highlight;
    }

//kira jarak hari period dengan hari user pilih. return jarak yg paling pendek. sama ada dari end ataupun start
    public int DayDifferent(LocalDate localDate){
      int fromStart = Math.abs( Days.daysBetween(localDate,date_start).getDays());
      int fromEnd = Math.abs(Days.daysBetween(localDate,date_end).getDays());
      if(fromStart <= fromEnd){
          return fromStart;
      }
      return fromEnd;
    }
    //auto update yang paling dekat dgn date
    public void setCloserDate(LocalDate localDate){
        int fromStart = Math.abs(Days.daysBetween(localDate,date_start).getDays());
        int fromEnd = Math.abs(Days.daysBetween(localDate,date_end).getDays());

        if(fromStart < fromEnd){
           //kalau start lg dekat, update start.
            setStart_date(localDate.toString("yyyy-MM-dd"));

        }else{
            setEnd_date(localDate.toString("yyyy-MM-dd"));
        }

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

                view.addSpan(new DotSpan(60,highlight));

            }
        };
    }


    //kira period length
 public int getPeriodLength (){
        //length define variable , type int kalau data xkira store dia akan return -1. sebb tu guna -1.
        //kalau ad kiraan akan berubah data, klau xdak, akan return -1
        int length = -1;

        if(date_start != null && date_end != null){
            //end date  start klu xnull msuk

            //xsimpan return obj
            //method chaining(panggil method)return type tu pnggil method. mcm multiple method tp pnggil 1 line.
            ///length = Math.abs(Days.daysBetween(date_end,date_start).getDays());
            //length =

            //simpan dalam memory .
            //simpan pastu panggil method. beza return daybetween sama ad simpn or x ja.
            Days days = Days.daysBetween(date_end,date_start);
            length = days.getDays();
            length = Math.abs(length)+1;

        }
        return length;
    }

    public int getCycleLength(){

        return cycleLength;
    }

//calculate cycleLength
    //original
    //header public method return nothing. nama method CalculateCycleLength,method
    //receive 1 paramaeter of obj dri class Period. mewakili period seterusnya

    public void CalculateCycleLength(Period nextPeriod){

        //declare variable assign -1
        //cycleLength = -1; //-ni attribute bukan variable.

        //guna utk kiraan cycle length.
        if(date_start != null && nextPeriod !=null){

            //Days days = Days.daysBetween(nextPeriod.date_start,this.date_start);
            //cycleLength = days.getDays();
            //cycleLength = Math.abs(cycleLength);
            //ganti balik

            //5/9
            Days days = Days.daysBetween(nextPeriod.getDate_start(), this.getDate_start());
            int calculatedCycleLength = Math.abs(days.getDays());
            this.cycleLength = calculatedCycleLength;
            cycleLength = Math.abs(cycleLength);

        }

    }

    public int getCycleCount() {
        return cycleCount;
    }
}
