package com.example.habittracker.models;

import java.sql.Time;
import java.util.Date;

public class HabitModel {

    // variables
    private CategoryModel categoryModel;
    private String type;    // yesNo, number, timer, checklist
    private String name;
    private String description;
    private String repeatType;
    private String repeatNumber;
    private Date startDate;
    private Date endDate;
    private int priority;
    private Time reminder;

    public HabitModel(CategoryModel categoryModel, String type, String name, String description, String repeatType, String repeatNumber, Date startDate, Date endDate, int priority, Time reminder) {
        this.categoryModel = categoryModel;
        this.type = type;
        this.name = name;
        this.description = description;
        this.repeatType = repeatType;
        this.repeatNumber = repeatNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priority = priority;
        this.reminder = reminder;
    }

    public CategoryModel getCategory() {
        return categoryModel;
    }

    public void setCategory(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Time getReminder() {
        return reminder;
    }

    public void setReminder(Time reminder) {
        this.reminder = reminder;
    }
}
