
/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code has been developed by a group of CSULB students working on their 
 *  Computer Science senior project called Tutors4You.

 *  Tutors4You is a web application that students can utilize to find a tutor and
 *  ask them to meet at any location of their choosing. Students that struggle to understand 
 *  the courses they are taking would benefit from this peer to peer tutoring service.

 *  2017 Amanda Pie <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import tut4you.model.*;
import tut4you.model.Tut4YouApp;

/**
 * Stores the availability of a tutor
 *
 * @author Andrew Kaichi <Andrew.Kaichi@student.csulb.edu>
 */
@Named
@RequestScoped
public class AvailabilityBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("AvailabilityBean");

    @EJB
    private Tut4YouApp tut4youApp;

    private Availability availability;
    private String stringStartTime;
    private String stringEndTime;
    private String startHour;
    private String startMinute;
    private String startPeriod;

    private String endHour;
    private String endMinute;
    private String endPeriod;
    private List<Availability> availabilityList = new ArrayList();
    private boolean editable = false;

    /**
     * Creates a new instance of the Availability entity
     */
    public AvailabilityBean() {
        availability = new Availability();
        stringStartTime = "";
        stringEndTime = "";
        editable = false;
    }

    /**
     * Gets the availability of the tutor
     *
     * @return the availability of the tutor
     */
    public Availability getAvailability() {
        return availability;
    }

    /**
     * Sets the availability of the tutor (not used)
     *
     * @param availability the availability of the tutor
     */
    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    /**
     * Sets the hour of the tutor
     *
     * @param startHour
     */
    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    /**
     * Sets the hour of the tutor
     *
     * @param startHour
     */
    public String getStartHour() {
        return startHour;
    }

    /**
     * Sets the hour of the tutor
     *
     * @param startHour
     */
    public void setStartMinute(String startMinute) {
        this.startMinute = startMinute;
    }

    /**
     * Sets the hour of the tutor
     *
     * @param startMinute
     * @return
     */
    public String getStartMinute() {
        return startMinute;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    /**
     * Sets the hour of the tutor
     *
     * @param startPeriod
     * @return
     */
    public String getStartPeriod() {
        return startPeriod;
    }

    /**
     * Sets the hour of the tutor
     *
     * @param endHour
     */
    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    /**
     * Sets the hour of the tutor
     *
     * @param endHour
     * @return
     */
    public String getEndHour() {
        return endHour;
    }

    /**
     * Sets the hour of the tutor
     *
     * @param endMinute
     */
    public void setEndMinute(String endMinute) {
        this.endMinute = endMinute;
    }

    /**
     * Sets the hour of the tutor
     *
     * @param endHour
     */
    public String getEndMinute() {
        return endMinute;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }

    /**
     * Sets the hour of the tutor
     *
     * @param endPeriod
     */
    public String getEndPeriod() {
        return endPeriod;
    }

    /**
     * Sets the start time of the tutor
     *
     * @return stringStartTime of the tutor
     */
    public String getStringStartTime() {
        return stringStartTime;
    }

    /**
     * Gets the start time of the tutor
     *
     * @param stringStartTime
     */
    public void setStringStartTime(String stringStartTime) {
        this.stringStartTime = stringStartTime;
    }

    /**
     * Gets the end time of the tutor
     *
     * @return stringEndTime of the tutor
     */
    public String getStringEndTime() {
        return stringEndTime;
    }

    /**
     * Sets the end time of the tutor
     *
     * @param stringEndTime
     */
    public void setStringEndTime(String stringEndTime) {
        this.stringEndTime = stringEndTime;
    }

    /**
     * Gets a list of the availabilities of the Tutor in the EJB
     *
     * @return a list of subjects
     */
    public List<Availability> getAvailabilityList() {
        availabilityList = tut4youApp.getAvailabilityList();
        return availabilityList;
    }

    /**
     * Adds the availability to the tutor
     *
     * @return result based on if the availability form was filled out properly
     * @throws java.text.ParseException
     */
    public String addAvailability() throws ParseException {
        String result = "failure";
        concatDateTime();
        System.out.println("Start Time:" + stringStartTime);
        System.out.println("End Time:" + stringEndTime);

        availability.setStartTime(StringToTime(stringStartTime));
        availability.setEndTime(StringToTime(stringEndTime));
        availability = tut4youApp.addAvailability(availability);
        if (availability != null) {
            result = "success";
        }
        return result;
    }

    /**
     * Updates the current availability of the tutor
     *
     * @param avail
     * @throws java.text.ParseException
     */
    public void updateAvailability(Availability avail) throws ParseException {
        tut4youApp.updateAvailability(avail, avail.getStartTime(), avail.getEndTime());
        System.out.println("test");
        changeEditable();
    }

    public void changeEditable() {
        editable = editable == false;
    }

    public String goToEditPage(Availability avail) throws ParseException {
        String result;
        Long id = avail.getId();
        result = "editAvailability";
        /*
        if (true) {
            System.out.println(avail.getStartTime());
            System.out.println(avail.getEndTime());
            System.out.println(avail.getDayOfWeek());
        }
       
        DateFormat df = new SimpleDateFormat("hh:mm a");
        
        stringStartTime = df.format(avail.getStartTime());
        System.out.println("Start Time: " + stringStartTime);
        
        
        stringEndTime = df.format(avail.getEndTime());
        System.out.println("End Time: " + stringEndTime);
        */
        
        

        return result;
    }

    /**
     * Delete the availability from the tutor
     *
     * @param avail
     */
    public void deleteAvailability(Availability avail) {
        tut4youApp.deleteAvailability(avail);
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void concatDateTime() {
        if (startPeriod.equals("AM")) {
            stringStartTime = startHour + ":" + startMinute;
        }
        if (startPeriod.equals("PM")) {
            int start = Integer.parseInt(startHour);
            start = start + 12;
            startHour = String.valueOf(start);
            stringStartTime = startHour + ":" + startMinute;
        }
        if (endPeriod.equals("PM")) {
            int end = Integer.parseInt(endHour);
            end = end + 12;
            endHour = String.valueOf(end);
            stringEndTime = endHour + ":" + endMinute;
        }
        if (endPeriod.equals("AM")) {
            stringEndTime = endHour + ":" + endMinute;
        }
    }

    /**
     * Convert string to Time
     *
     * @param time
     * @return
     * @throws java.text.ParseException
     * https://stackoverflow.com/questions/6842245/converting-date-time-to-24-hour-format
     * assisted by Amanda
     */
    public java.util.Date StringToTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //HH = 1-23, hh = 1-12
        java.util.Date date = sdf.parse(time);
        return date;
    }
}
