package com.example.d308project.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.room.ForeignKey;

@Entity(
        tableName = "excursions",
        foreignKeys = @ForeignKey(
                entity = Vacation.class,
                parentColumns = "vacationID",
                childColumns = "vacationID",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("vacationID")}
)
public class Excursion {

    @PrimaryKey(autoGenerate = true)
    private int excursionId;

    private int vacationID;
    private String excursionName;
    private String excursionDate;

    // Required empty constructor for Room
    public Excursion() {}

    // ✅ Constructor for INSERT
    @Ignore
    public Excursion(String excursionName, String excursionDate, int vacationID) {
        this.excursionName = excursionName;
        this.excursionDate = excursionDate;
        this.vacationID = vacationID;
    }

    // ✅ Constructor for UPDATE
    @Ignore
    public Excursion(int excursionId, String excursionName, String excursionDate, int vacationID) {
        this.excursionId = excursionId;
        this.excursionName = excursionName;
        this.excursionDate = excursionDate;
        this.vacationID = vacationID;
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

    public String getExcursionDate() {
        return excursionDate;
    }

    public void setExcursionDate(String excursionDate) {
        this.excursionDate = excursionDate;
    }
}