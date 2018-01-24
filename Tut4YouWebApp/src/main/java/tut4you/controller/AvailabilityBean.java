
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
 *
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
    
    public AvailabilityBean(){
        availability = new Availability();
    }

//    public AvailabilityBean(Long id){
//        availability = tut4youApp.getAvailability(id);  
//    }
    
    public Availability getAvailability(){
        return availability;
    }
    public void setAvailability(Availability availability){
        this.availability = availability;
    }
    public String addAvailability(){
        String result = "failure";
        availability = tut4youApp.addAvailability(availability);
        if (availability != null){
            result = "success";
            LOGGER.severe("Availability added");
        }
        return result;
    }
    public void updateAvailability(){
        tut4youApp.updateAvailability(availability);
    }
}