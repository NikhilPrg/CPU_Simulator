package com.kurama.nikhil.cpusimulator;

/**
 * Created by Nikhil on 19/04/17.
 */
public class Process {
    private String processName;
    private int burstTime;
    private int arrivalTime;

    public Process(String processName, int burstTime, int arrivalTime) {
        this.processName = processName;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
