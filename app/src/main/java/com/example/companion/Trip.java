package com.example.companion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
public class Trip {
    private int id;
    private Calendar dateTimeFrom;
    private Calendar dateTimeTo;
    private String from;
    private String to;
    private int countOfPlaces;
    private int currentCountOfPlaces;
    private String transport;
    private double cost;
    private String driver;
    final private ArrayList<String> users=new ArrayList<>();
    public Trip(int id,String driver,String from,String to) {
        this.id=id;
        this.driver=driver;
        this.from=from;
        this.to=to;
    }
}
