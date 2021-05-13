package com.example.moove;

import java.util.Date;

public class workout {

    private  double distance;
    private int numSteps;
    private String typeOfExercise;
    private Date date;
    private String duration;
    private int  averageSpeed;
    private String userID;

    @Override
    public String toString() {
        return "workout{" +
                "distance=" + distance +
                ", numSteps=" + numSteps +
                ", typeOfExercise='" + typeOfExercise + '\'' +
                ", date=" + date +
                ", duration='" + duration + '\'' +
                ", averageSpeed=" + averageSpeed +
                ", userID='" + userID + '\'' +
                '}';
    }
    private workout(){};
    public workout(double distance, int numSteps, String typeOfExercise, Date date, String duration, int averageSpeed, String userID) {
        this.distance = distance;
        this.numSteps = numSteps;
        this.typeOfExercise = typeOfExercise;
        this.date = date;
        this.duration = duration;
        this.averageSpeed = averageSpeed;
        this.userID = userID;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }

    public String getTypeOfExercise() {
        return typeOfExercise;
    }

    public void setTypeOfExercise(String typeOfExercise) {
        this.typeOfExercise = typeOfExercise;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(int averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


}
