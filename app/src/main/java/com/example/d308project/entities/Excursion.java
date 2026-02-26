package com.example.d308project.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.NO_ACTION;

@Entity(
        tableName = "excursions",
        foreignKeys = @ForeignKey(
                entity = Vacation.class,
                parentColumns = "vacationID",
                childColumns = "vacationID",
                onDelete = NO_ACTION
        )
)
public class Excursion {

    @PrimaryKey(autoGenerate = true)
    private int excursionId;

    private int vacationID;
    private String excursionName;
    private double price;

    public Excursion() {}

    @Ignore
    public Excursion(int vacationID, String excursionName, double price) {
        this.vacationID = vacationID;
        this.excursionName = excursionName;
        this.price = price;
    }

    // GETTERS & SETTERS

    public int getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(int excursionId) {
        this.excursionId = excursionId;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public void setExcursionName(String excursionName) {
        this.excursionName = excursionName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}