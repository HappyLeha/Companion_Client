package com.example.companion;

import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
public class TripForm {
    private Calendar dateFrom;
    private Calendar dateTo;
    private Calendar timeStart;
    private Calendar timeEnd;
    private String from;
    private String to;
    private Double cost;
    private static TripForm instance=new TripForm(null,null,null,null,null,null,null);
    private TripForm(Calendar dateFrom,Calendar dateTo,Calendar timeStart,Calendar timeEnd,String from,String to,Double cost) {
        this.dateFrom=dateFrom;
        this.dateTo=dateTo;
        this.timeStart=timeStart;
        this.timeEnd=timeEnd;
        this.from=from;
        this.to=to;
        this.cost=cost;
    }
    public static void set(Calendar dateFrom,Calendar dateTo,Calendar timeStart,Calendar timeEnd,String from,String to,Double cost) {
        instance=new TripForm(dateFrom,dateTo,timeStart,timeEnd,from,to,cost);
    }
    public static void reset() {
        instance=new TripForm(null,null,null,null,null,null,null);
    }
    public static TripForm get() {
        return instance;
    }
}
