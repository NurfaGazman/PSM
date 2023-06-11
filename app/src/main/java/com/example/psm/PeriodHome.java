package com.example.psm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.example.psm.databinding.ActivityPeriodHomeBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Calendar;
import java.util.Date;

public class PeriodHome extends AppCompatActivity {

    private ActivityPeriodHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding = ActivityPeriodHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.calendarView.setOnDateLongClickListener(
                new OnDateLongClickListener() {
                    @Override
                    public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {

                        DayViewDecorator selectedRange = new DayViewDecorator() {
                            @Override
                            public boolean shouldDecorate(CalendarDay day) {
                                //snio setting date
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(date.getYear(),date.getMonth(),date.getDay());
                                calendar.add(Calendar.DAY_OF_MONTH,5/*berapa hari nk tambah*/);

                                LocalDate endDate = LocalDateTime.fromCalendarFields(calendar).toLocalDate();
                                CalendarDay end = CalendarDay.from(endDate.getYear(),endDate.getMonthOfYear()-1,endDate.getDayOfMonth());
                                Toast.makeText(PeriodHome.this, date+"to"+endDate, Toast.LENGTH_SHORT).show();



                                return ((day.isAfter(date)|| day.equals(date)) &&
                                        (day.isBefore(end)) || day.equals(end) );
                            }

                            @Override
                            public void decorate(DayViewFacade view) {
                                view.addSpan(new DotSpan(10, Color.RED));

                            }
                        };
                        binding.calendarView.addDecorator(selectedRange);
                    }
                }
        );
    }
}