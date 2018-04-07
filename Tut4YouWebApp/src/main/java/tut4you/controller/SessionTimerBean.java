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
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import tut4you.model.Request;
import tut4you.model.SessionTimer;
import tut4you.model.Tut4YouApp;

/**
 * Binds session timer bean inputs to the EJB.
 * @author Syed Haider <shayder426@gmail.com>
 */
@Named
@RequestScoped
public class SessionTimerBean implements Serializable{
    
    private String email;
    private boolean checkEmail;
    private static final long serialVersionUID = 1L;
    private Tut4YouApp tut4youApp;
    private Request request;
    private SessionTimer sessionTimer;
  
    /**
     * This checks and sees if the email inputted (for
     * the security question) is equivalent to the
     * email in the database
     *
     * @return checkEmail true if email is the same
     */
    public boolean isCheckEmail() {
        return checkEmail;
    }

    /**
     * Sets the checkEmail attribute to true
     * or false depending on if the
     * emails are equivalent
     *
     * @param checkEmail
     */
    public void setCheckEmail(boolean checkEmail) {
        this.checkEmail = checkEmail;
    }

    /**
     * Gets the email that is inputted by the user
     *
     * @return email inputted by user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email that is inputted by the user
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Validates whether or not the emails
     * are equivalent or not
     *
     * @param email the user inputted email
     */
    public void isEmailValid(String email)
    {
        checkEmail = tut4youApp.checkEmail(email);
    }
    
    /**
     * This will set the start session time of the session
     *
     * @param r the active request of the tutoring session
     */
    public void startTutorSession(Request r)
    {
        Long startTime = System.currentTimeMillis();
        sessionTimer.setStartSessionTime(startTime);
        //sessionTimer = tut4youApp.addSessionTimer(sessionTimer);
        tut4youApp.startSessionTime(r);
        
    }

    /**
     * This ends the tutoring session and sets the request
     * to be a complete status
     * 
     * @param r the active request of the tutoring session
     */
    public void endTutorSession(Request r)
    {
        tut4youApp.setRequestToComplete(r);
    }
    
    /**
     * This will forward the tutor to the session timer page
     * to start the tutoring session
     *
     * @param r the active request 
     * @return the webpage of the session timer
     */
    public String goToSessionTimerPage(Request r)
    {
        String result;
        this.request = r;
        result = "sessionTimer";
        return result;
    }
}