/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code has been developed by a group of CSULB students working on their 
 *  Computer Science senior project called Tutors4You.
 *  
 *  Tutors4You is a web application that students can utilize to find a tutor and
 *  ask them to meet at any location of their choosing. Students that struggle to understand 
 *  the courses they are taking would benefit from this peer to peer tutoring service.
 
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.controller;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import tut4you.model.*;
import tut4you.exception.*;

/**
 * RegistrationBean encapsulates all the functions/services involved 
 * in registering as a User or Tutor.
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 * @author Keith Tran <keithtran25@gmail.com>
 */
@Named
@ViewScoped
public class RegistrationBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger("RequestBean");
    
    @EJB
    private Tut4YouApp tut4youApp;
    
    private User newStudent;
    private Location newLocation;
    private String confirmPassword;
    private String userType;
    private String priceRate;
    private String defaultZip;
    
    /** 
     * Creates a new instance of Registration
     */
    @PostConstruct
    public void createRegistrationBean() {
        newStudent = new User();
        newLocation = new Location();
    }
    
    /** 
     * Destroy a new instance of Registration
     */
    @PreDestroy
    public void destroyRegistrationBean() {
        
    }
    
    public Location getNewLocation() {
        return newLocation;
    }
    
    public void setNewLocation(Location newLocation) {
        this.newLocation = newLocation;
    }
    /**
     * Gets the new student who just registered
     * @return the new User entity
     */
    public User getNewStudent() {
        return newStudent;
    }
    
    /**
     * Sets the user to be a User
     * @param newStudent student who has just registered
     */
    public void setNewStudent(User newStudent) {
        this.newStudent = newStudent;
    }
        
    /**
     * Gets the field of the confirm Password
     * @return the field of the confirm Password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    /**
     * Sets the value in the confirm password to check if password match
     * @param confirmPassword the password matching the typed password
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    /**
     * Gets the userType
     * @return the userType is Tutor or User
     */
    public String getUserType() {
        return userType;
    }
    
    /**
     * Sets the userType
     * @param userType the userType is tutor or student
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    public String getPriceRate() {
        return priceRate;
    }

    public void setPriceRate(String priceRate) {
        this.priceRate = priceRate;
    }
    
    public String getDefaultZip() {
        return defaultZip;
    }
    
    public void setDefaultZip(String defaultZip) {
        this.defaultZip = defaultZip;
    }
    
    /**
     * JSF Action that uses the information submitted in the registration page
     * to add user as a registered User user.
     * @return either failure, success, or register depending on successful
     * registration.
     */
    // IN PROGRESS
    public String createUser() {
        String result = "failure";
        if (newStudent.isInformationValid(confirmPassword)) {
            newStudent.setPassword(tut4you.controller.HashPassword.getSHA512Digest(newStudent.getPassword()));
            try {
                double pr = 0;
                if (priceRate != null) {
                    pr = Double.parseDouble(priceRate);
                }
                newLocation.setDefaultZip(defaultZip);
                tut4youApp.registerUser(newStudent, userType, pr, newLocation);
                result = "success";
            } catch (StudentExistsException see) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("A user with that information already exists, try again."));
                result = "register";
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, null, e);
                result = "failure";
            }
        }
        return result;
    }
}