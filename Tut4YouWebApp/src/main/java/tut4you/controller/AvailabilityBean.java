
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
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import tut4you.model.*;
import tut4you.model.Tut4YouApp;

/**
 * Stores the availability of a tutor
 * @author Andrew Kaichi <Andrew.Kaichi@student.csulb.edu>
 */
@Named
@SessionScoped
public class AvailabilityBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger("AvailabilityBean");
    
    @EJB
    private Tut4YouApp tut4youApp;
    private Availability availability;
    
    /**
     * Creates a new instance of the Availability entity
     */
    public AvailabilityBean(){
        availability = new Availability();
    }
    
    /**
     * Gets the availability of the tutor
     * @return the availability of the tutor
     */
    public Availability getAvailability(){
        return availability;
    }
    
    /**
     * Sets the availability of the tutor (not used)
     * @param availability the availability of the tutor
     */
    public void setAvailability(Availability availability){
        this.availability = availability;
    }
    
    /**
     * Adds the availability to the tutor
     * @return result based on if the availability form was filled out properly
     */
    public String addAvailability(){
        String result = "failure";
        availability = tut4youApp.addAvailability(availability);
        if (availability != null){
            result = "success";
            LOGGER.severe("Availability added");
        }
        return result;
    }
    
    /**
     * Updates the current availability of the tutor
     */
    public void updateAvailability(){
        tut4youApp.updateAvailability(availability);
    }
}