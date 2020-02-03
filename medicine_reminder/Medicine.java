package com.example.medicine_reminder;

public class Medicine {
    private String medId;
    private String uid;
    private String med_name;
    private int hour;
    private int minute;

    private double dosage;

    public  Medicine() {
        //nothin'
    }
    public Medicine(String medId, String uid, String med_name, int hour, int minute, double dosage) {
        this.medId = medId;
        this.uid = uid;
        this.med_name = med_name;
        this.hour = hour;
        this.minute = minute;
        this.dosage = dosage;

    }

    public double getDosage() {
        return dosage;
    }

    public void setDosage(double dosage) {
        this.dosage = dosage;
    }

    public String getMedId() {
        return medId;
    }

    public void setMedId(String medId) {
        this.medId = medId;
    }

    public String getMed_name() {
        return med_name;
    }

    public void setMed_name(String med_name) {
        this.med_name = med_name;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
