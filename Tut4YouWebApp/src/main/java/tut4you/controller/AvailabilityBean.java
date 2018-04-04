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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("AvailabilityBean");

    @EJB
    private Tut4YouApp tut4youApp;

    private Availability availability;
    //private String stringStartTime;
    //private String stringEndTime;

    @Temporal(TemporalType.TIME)
    private java.util.Date startTime;

    @Temporal(TemporalType.TIME)
    private java.util.Date endTime;

    private List<Availability> availabilityList = new ArrayList();

    private boolean modalFlag = false;

    /**
     * Creates a new instance of the Availability entity
     */
    public AvailabilityBean() {
        availability = new Availability();
        //stringStartTime = "";
        //stringEndTime = "";

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
     * Gets the start time
     *
     * @return startTime
     */
    public java.util.Date getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time
     *
     * @param startTime
     */
    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time
     *
     * @return endTime
     */
    public java.util.Date getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time
     *
     * @param endTime
     */
    public void setEndTime(java.util.Date endTime) {
        this.endTime = endTime;
    }

    public boolean isModalFlag() {
        return modalFlag;
    }

    public void setModalFlag(boolean modalFlag) {
        this.modalFlag = modalFlag;
    }

//    /**
//     * Sets the start time of the tutor
//     * @return stringStartTime of the tutor
//     */
//    public String getStringStartTime() {
//        return stringStartTime;
//    }
//    
//    /**
//     * Gets the start time of the tutor
//     * @param stringStartTime
//     */
//    public void setStringStartTime(String stringStartTime) {
//        this.stringStartTime = stringStartTime;
//    }
//    
//    /**
//     * Gets the end time of the tutor
//     * @return stringEndTime of the tutor
//     */
//    public String getStringEndTime() {
//        return stringEndTime;
//    }
//    
//    /**
//     * Sets the end time of the tutor
//     * @param stringEndTime
//     */
//    public void setStringEndTime(String stringEndTime) {
//        this.stringEndTime = stringEndTime;
//    }
    /**
     * Gets a list of the availabilities of the Tutor in the EJB
     *
     * @return a list of subjects
     */
    public List<Availability> getAvailabilityList() {
        availabilityList = tut4youApp.getAvailability();
        return availabilityList;
    }


    
    /**
     * Adds the availability to the tutor
     *
     * @return result based on if the availability form was filled out properly
     * @throws java.text.ParseException
     */
    public void addAvailability() throws ParseException {
        String result = "failure";
        availability.setStartTime(startTime);
        availability.setEndTime(endTime);
        availability = tut4youApp.addAvailability(availability, startTime);
        if (availability != null) {
            result = "success";
            modalFlag = true;
        }
        else
        {
            modalFlag = false;
        }
    }

    /**
     * Updates the current availability of the tutor
     *
     * @param avail
     * @return
     * @throws java.text.ParseException
     */
    public String updateAvailability(Availability avail) throws ParseException {
        String result = "failure";
        System.out.println("Start: " + avail.getStartTime());
        System.out.println("End: " + avail.getEndTime());
        this.availability = avail;
        availability.setStartTime(avail.getStartTime());
        availability.setEndTime(avail.getEndTime());
        tut4youApp.updateAvailability(availability, avail.getStartTime(), avail.getEndTime());
        if (availability != null) {
            result = "success";
        }
        return result;
    }

    /**
     * Delete the availability from the tutor
     *
     * @param avail
     * @return result based on if the availability form was filled out properly
     * @throws java.text.ParseException
     */
    public void deleteAvailability(Availability avail) throws ParseException {
        tut4youApp.deleteAvailability(availability);
    }

    /**
     * Convert string to Time
     *
     * @param time
     * @return
     * @throws java.text.ParseException
     */
    public java.util.Date StringToTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        java.util.Date date = sdf.parse(time);
        return date;
    }

    public String goToEditAvailabilityPage(Availability avail) throws ParseException {
        String result;
        this.availability = avail;
        System.out.println("Availability avail: " + avail.toString());
        result = "editAvailability";
        return result;
    }

    public String goToHomePage() {
        String result = "success";
        return result;
    }

    public String refreshPage() {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true";
    }

}
