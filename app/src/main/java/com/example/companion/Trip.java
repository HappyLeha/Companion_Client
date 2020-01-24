package com.example.companion;

import java.time.LocalDateTime;

public class Trip {
    static int count=0;
    public int id;
    private LocalDateTime datetimeFrom;
    private LocalDateTime datetimeTo;
    private String from;
    private String to;
    private int countOfPlaces;
    private int currentCountOfPlaces=0;
    private String transport;
    private double cost;
    private String driver;
    public Trip(LocalDateTime datetimeFrom,LocalDateTime datetimeTo,String from,String to,int countOfPlaces,String transport,double cost,int id,String driver) {

        this.datetimeFrom=datetimeFrom;
        this.datetimeTo=datetimeTo;
        this.from=from;
        this.to=to;
        this.countOfPlaces=countOfPlaces;
        this.transport=transport;
        this.cost=cost;
        this.id=id;
        this.driver=driver;
    }
    public Trip(String from,String to,int id,String driver) {


        this.from=from;
        this.to=to;

        this.id=id;
        this.driver=driver;
    }
    public static void incrementCount() {
        count++;
    }
    public void setDateTimeFrom(LocalDateTime datetime) {
        this.datetimeFrom=datetime;
    }
    public LocalDateTime getDateTimeFrom() {
        return datetimeFrom;
    }
    public void setDateTimeTo(LocalDateTime datetime) {
        this.datetimeTo=datetime;
    }
    public LocalDateTime getDateTimeTo() {
        return datetimeTo;
    }
    public void setFrom(String from) {
        this.from=from;
    }
    public String getFrom() {
        return from;
    }
    public void setTo(String to) {
        this.to=to;
    }
    public String getTo() {
        return to;
    }
    public void setTransport(String transport) {
        this.transport=transport;
    }
    public String getTransport() {
        return transport;
    }
    public void setCountOfPlaces(int countOfPlaces) {
        this.countOfPlaces=countOfPlaces;
    }
    public int getCountOfPlaces() {
        return countOfPlaces;
    }
    public void setCurrentCountOfPlaces(int currentCountOfPlaces) {
        this.currentCountOfPlaces=currentCountOfPlaces;
    }
    public int getCurrentCountOfPlaces() {
        return currentCountOfPlaces;
    }
    public void setCost(double cost) {
        this.cost=cost;
    }
    public double getCost() {
        return cost;
    }
    public void setDriver(String driver) {
        this.driver=driver;
    }
    public String getDriver() {
        return driver;
    }
}
