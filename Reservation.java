package com.oasis.reservation.model;

import com.oasis.reservation.util.Validator;

import java.time.LocalDate;

/** One ticket booking. */
public class Reservation {
    private long pnr;
    private String username;
    private String passengerName;
    private int trainNo;
    private String trainName;
    private String classType;
    private LocalDate journeyDate;
    private String source;
    private String destination;
    private String bookedAt;

    public Reservation() { }

    public Reservation(String username, String passengerName, int trainNo, String trainName,
                       String classType, LocalDate journeyDate, String source, String destination) {
        this.username = username;
        this.passengerName = passengerName;
        this.trainNo = trainNo;
        this.trainName = trainName;
        this.classType = classType;
        this.journeyDate = journeyDate;
        this.source = source;
        this.destination = destination;
    }

    public long getPnr() { return pnr; }
    public void setPnr(long pnr) { this.pnr = pnr; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public int getTrainNo() { return trainNo; }
    public void setTrainNo(int trainNo) { this.trainNo = trainNo; }
    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }
    public LocalDate getJourneyDate() { return journeyDate; }
    public void setJourneyDate(LocalDate journeyDate) { this.journeyDate = journeyDate; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getBookedAt() { return bookedAt; }
    public void setBookedAt(String bookedAt) { this.bookedAt = bookedAt; }

    /** Text used in the confirmation dialog and the cancellation screen. */
    public String toDisplayString() {
        return "PNR             : " + pnr + "\n"
                + "Passenger       : " + passengerName + "\n"
                + "Train           : " + trainNo + " - " + trainName + "\n"
                + "Class           : " + classType + "\n"
                + "Date of Journey : " + journeyDate.format(Validator.DATE_FMT) + "\n"
                + "From            : " + source + "\n"
                + "To              : " + destination + "\n"
                + "Booked At       : " + bookedAt;
    }
}
