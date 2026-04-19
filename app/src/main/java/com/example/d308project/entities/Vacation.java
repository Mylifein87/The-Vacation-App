package com.example.d308project.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "vacations")
public class Vacation {

    @PrimaryKey(autoGenerate = true)
    private int vacationID;

    private String title;
    private double price;
    private String hotel;
    private String startDate;
    private String endDate;


    public Vacation(int vacationID, String title, double price, String hotel, String startDate, String endDate) {
        this.vacationID = vacationID;
        this.title = title;
        this.price = price;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    @Ignore
    public Vacation(String title, double price, String hotel, String startDate, String endDate) {
        this.title = title;
        this.price = price;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}