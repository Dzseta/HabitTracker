package com.example.habittracker.models;

import java.sql.Time;
import java.util.Date;

public class HabitModel {

    // variables
    private String categoryName;
    private String name;
    private String description;
    private String type;    // yesNo, number, timer, checklist
    private String typeData;
    private String repeatType; // every x days, x times a week/month/year, m/t/w/th/f/s/su
    private String repeatNumber;
    private String startDate;
    private String endDate;
    private int priority;
    private boolean reminder;
    private int reminderHour;
    private int reminderMinute;

    public HabitModel(String categoryName, String name, String description, String type, String typeData, String repeatType, String repeatNumber, String startDate, String endDate, int priority, boolean reminder, int reminderHour, int reminderMinute) {
        this.categoryName = categoryName;
        this.name = name;
        this.description = description;
        this.type = type;
        this.typeData = typeData;
        this.repeatType = repeatType;
        this.repeatNumber = repeatNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priority = priority;
        this.reminder = reminder;
        this.reminderHour = reminderHour;
        this.reminderMinute = reminderMinute;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeData() {
        return typeData;
    }

    public void setTypeData(String typeData) {
        this.typeData = typeData;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public String getRepeatNumber() {
        return repeatNumber;
    }

    public void setRepeatNumber(String repeatNumber) {
        this.repeatNumber = repeatNumber;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public int getReminderHour() {
        return reminderHour;
    }

    public void setReminderHour(int reminderHour) {
        this.reminderHour = reminderHour;
    }

    public int getReminderMinute() {
        return reminderMinute;
    }

    public void setReminderMinute(int reminderMinute) {
        this.reminderMinute = reminderMinute;
    }
}
