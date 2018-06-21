package com.ubtech.airport.ibm;

/**
 * Created by Administrator on 2018/3/20.
 */

public class WeatherBean {

    /**
     * request : success
     * option : success
     * hasData : true
     * data : {"today":true,"city":"天津","wind":"南风3-4级","week":"星期一","date":"20140804","temperature":"28℃~36℃","weather":"晴转多云"}
     */

    private String request;
    private String option;
    private boolean hasData;
    private DataBean data;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isHasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * today : true
         * city : 天津
         * wind : 南风3-4级
         * week : 星期一
         * date : 20140804
         * temperature : 28℃~36℃
         * weather : 晴转多云
         */

        private boolean today;
        private String city;
        private String wind;
        private String week;
        private String date;
        private String temperature;
        private String weather;

        public boolean isToday() {
            return today;
        }

        public void setToday(boolean today) {
            this.today = today;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }
    }
}
