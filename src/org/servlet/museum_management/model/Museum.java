package org.servlet.museum_management.model;

import java.sql.Time;

public class Museum {
    private Integer Id;
    private String Name;
    private String Description;
    private String Address;
    private Time TimeOpening;
    private Time TimeClosing;
    private Integer exhibitionCount;

    public Museum() {
    }

    public Museum(Integer Id, String Name, String Description, String Address, Time TimeOpening, Time TimeClosing) {
        this.Id = Id;
        this.Name = Name;
        this.Description = Description;
        this.Address = Address;
        this.TimeOpening = TimeOpening;
        this.TimeClosing = TimeClosing;
    }

    public int getId() {
        return this.Id;
    }

    public void setId(int value) {
        this.Id = value;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String value) {
        this.Name = value;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String value) {
        this.Description = value;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setAddress(String value) {
        this.Address = value;
    }

    public Time getTimeOpening() {
        return this.TimeOpening;
    }

    public void setTimeOpening(Time value) {
        this.TimeOpening = value;
    }

    public Time getTimeClosing() {
        return this.TimeClosing;
    }

    public void setTimeClosing(Time value) {
        this.TimeClosing = value;
    }

    public Integer getExhibitionCount() {
        return exhibitionCount;
    }

    public void setExhibitionCount(Integer exhibitionCount) {
        this.exhibitionCount = exhibitionCount;
    }

}
