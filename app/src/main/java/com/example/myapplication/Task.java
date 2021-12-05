package com.example.myapplication;

import java.util.Date;

public class Task {

    public String getWorker() {
        return worker;
    }

    public String getResource() {
        return resource;
    }

    public String getNumber() {
        return number;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getWorkerId() {
        return workerId;
    }

    private String worker;
    private  String resource;
    private  String number, duration;
    private  String startTime;
    private   int workerId;






    public Task(String worker, String resource, String number, String startTime, String duration) {
        this.worker = worker;
        this.resource = resource;
        this.number = number;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int workerId, String worker, String resource, String number, String startTime) {
        this.workerId = workerId;
        this.worker = worker;
        this.resource = resource;
        this.number = number;
        this.startTime = startTime;

    }

    public Task(String worker, String resource, String number, String startTime) {
        this.worker = worker;
        this.resource = resource;
        this.number = number;
        this.startTime = startTime;
    }
}
