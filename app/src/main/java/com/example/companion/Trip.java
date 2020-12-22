package com.example.companion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trip {
    private int id;
    @JsonSerialize(using= CalendarSerializer.class)
    @JsonDeserialize(using= CalendarDeserializer.class)
    private Calendar dateTimeFrom;
    @JsonSerialize(using= CalendarSerializer.class)
    @JsonDeserialize(using= CalendarDeserializer.class)
    private Calendar dateTimeTo;
    private String from;
    private String to;
    private int countOfPlaces;
    private int currentCountOfPlaces;
    private String transport;
    private double cost;
    private String driver;
    public Trip(int id,String driver,String from,String to) {
        this.id=id;
        this.driver=driver;
        this.from=from;
        this.to=to;
    }
}
