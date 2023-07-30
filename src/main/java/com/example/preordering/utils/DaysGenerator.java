package com.example.preordering.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DaysGenerator {
    public static List<LocalDate> get7Days(){

        List<LocalDate> sevenDays = new ArrayList<>();

        for (int i = 0; i < 7;i++) {
            LocalDate localDate = LocalDate.now();
            sevenDays.add(localDate.plusDays(i));
        }
        return sevenDays;
    }
    public String getDayOfWeek(DayOfWeek day){
        return switch (day) {
            case MONDAY -> "Monday";
            case TUESDAY -> "Tuesday";
            case WEDNESDAY -> "Wednesday";
            case THURSDAY -> "Thursday";
            case FRIDAY -> "Friday";
            case SATURDAY -> "Saturday";
            case SUNDAY -> "Sunday";
        };
    }
    public static String chosenDate(String date){
        LocalDate localDate;
        if(date == null){
            localDate = LocalDate.now();
        }else {
            localDate = LocalDate.parse(date);
        }
        String month = String.valueOf(localDate.getMonth());
        int dayOfMonth = localDate.getDayOfMonth();
        int year = localDate.getYear();

        return dayOfMonth + " " + month + ", " + year;
    }

}
