package com.ubtech.airport.ibm;

/**
 * Created by Administrator on 2018/3/20.
 */

public class FligjhtBean1 {
    private String startCity;
    private String endCity;
    private String startTime;
    private String endTime;
    private String startData;
    private String endData;
    private String FlightNum;
    private String Duration;//
    private String Price1;
    private String Price2;
    private String Price3;

    public FligjhtBean1(String startCity, String endCity, String startTime, String endTime, String startData, String endData, String flightNum, String duration, String price1, String price2, String price3) {
        this.startCity = startCity;
        this.endCity = endCity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startData = startData;
        this.endData = endData;
        FlightNum = flightNum;
        Duration = duration;
        Price1 = price1;
        Price2 = price2;
        Price3 = price3;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartData() {
        return startData;
    }

    public void setStartData(String startData) {
        this.startData = startData;
    }

    public String getEndData() {
        return endData;
    }

    public void setEndData(String endData) {
        this.endData = endData;
    }

    public String getFlightNum() {
        return FlightNum;
    }

    public void setFlightNum(String flightNum) {
        FlightNum = flightNum;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getPrice1() {
        return Price1;
    }

    public void setPrice1(String price1) {
        Price1 = price1;
    }

    public String getPrice2() {
        return Price2;
    }

    public void setPrice2(String price2) {
        Price2 = price2;
    }

    public String getPrice3() {
        return Price3;
    }

    public void setPrice3(String price3) {
        Price3 = price3;
    }
}
