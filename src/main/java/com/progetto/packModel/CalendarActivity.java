package com.progetto.packModel;

import javafx.scene.Node;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class CalendarActivity extends Node {
    // Private fields to store the date, client name, and service number for the activity
    private LocalDate date;
    private String clientName;

    // Constructor to initialize the CalendarActivity object with provided data
    public CalendarActivity(LocalDate date, String clientName) {
        this.date = date; // Set the date for the activity
        this.clientName = clientName; // Set the client name for the activity
    }

    // Getter method to retrieve the date of the activity
    public LocalDate getDate() {
        return date;
    }

    // Setter method to set the date of the activity
    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Getter method to retrieve the client name of the activity
    public String getClientName() {
        return clientName;
    }

    // Setter method to set the client name of the activity
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    // Override the toString() method to create a string representation of the CalendarActivity object
    @Override
    public String toString() {
        return "CalendarActivity{" +
                "date=" + date +
                ", clientName='" + clientName + '\'' +
                '}';
    }
}
