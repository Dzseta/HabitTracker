package com.example.habittracker.models;

import java.sql.Time;
import java.util.Date;

public class HabitModel {

    // variables
    private CategoryModel category;
    private String name;
    private String description;
    private String type;    // yesNo, number, timer, checklist
    private String typeData;
    private String repeatType; // every x days, x times a week/month/year, m/t/w/th/f/s/su
    private String repeatNumber;
    private Date startDate;
    private Date endDate;
    private int priority;
    private boolean reminder;
    private Time reminderTime;

    public HabitModel(CategoryModel category, String name, String description, String type, String typeData, String repeatType, String repeatNumber, Date startDate, Date endDate, int priority, boolean reminder, Time reminderTime) {
        this.category = category;
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
        this.reminderTime = reminderTime;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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

    public Time getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Time reminderTime) {
        this.reminderTime = reminderTime;
    }
}
