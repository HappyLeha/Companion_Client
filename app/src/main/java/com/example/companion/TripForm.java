package com.example.companion;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;


public class TripForm {
    @Getter
    @Setter
    private static Calendar dateFrom;
    @Getter
    @Setter
    private static Calendar dateTo;
    @Getter
    @Setter
    private static Calendar timeStart;
    @Getter
    @Setter
    private static Calendar timeEnd;
    @Getter
    @Setter
    private static String from;
    @Getter
    @Setter
    private static String to;
    @Getter
    @Setter
    private static Double cost;
    public static void reset() {
        dateFrom=null;
        dateTo=null;
        timeStart=null;
        timeEnd=null;
        from=null;
        to=null;
        cost=null;
    }
}
