package com.example.weatheralertservice.model;

public class WeatherDTO {
    private int temp;
    private int humidity;
    private String sky;

    public WeatherDTO(int temp, int humidity, String sky) {
        this.temp = temp;
        this.humidity = humidity;
        this.sky = sky;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getSky() {
        return sky;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    @Override
    public String toString() {
        return "WeatherDTO{" +
                "temp=" + temp +
                ", humidity=" + humidity +
                ", sky='" + sky + '\'' +
                '}';
    }
}
