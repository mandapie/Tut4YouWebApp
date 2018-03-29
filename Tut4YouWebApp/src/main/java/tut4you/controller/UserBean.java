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
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import tut4you.model.*;

/**
 * UserBean checks if a user is authenticated.
 * @author Alvaro Monge <alvaro.monge@csulb.edu>
 * Modified by Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Named
@SessionScoped
public class UserBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger("UserBean");
    
    @EJB
    private Tut4YouApp tut4youapp;
    
    private User student;
    private Tutor userTutor;
    boolean doNotDisturb;
    
    /**
     * Creates a new instance of UserIdentity
     */
    public UserBean() {
        student = userTutor;
        userTutor = (Tutor)student;
    }
    
    /**
     * Gets the Tutor object
     * @return 
     */
    public Tutor getUserTutor() {
        return userTutor;
    }
    
    /**
     * Sets the User object
     * @param tutor 
     */
    public void setUserTutor(User tutor) {
        this.userTutor = (Tutor) tutor;
    }
    
    /**
     * Gets the User object
     * @return the student Object
     */
    public User getStudent() {
        return student;
    }
    
    /**
     * Sets the student Object
     * @param student the student 
     */
    public void setStudent(User student) {
        this.student = student;
    }
    
    /**
     * Gets the state of doNotDisturb is on or off
     * @return true/false
     */
    public boolean isDoNotDisturb() {
        return doNotDisturb;
    }
    
    /**
     * Sets the state of doNotDisturb
     * @param doNotDisturb 
     */
    public void setDoNotDisturb(boolean doNotDisturb) {
        this.doNotDisturb = doNotDisturb;
    }
    
    /**
     * Called the EJG to update the state of doNotDisturb
     * @param d 
     */
    public void updateDoNotDisturb(Boolean d) {
        tut4youapp.updateDoNotDisturb(doNotDisturb);
    }
    
    /**
     * Determine if current authenticated user has the role of tutor
     * @return true if user has role of tutor, false otherwise.
     */
    public boolean isTutor() {
        boolean isTutor = false;
        if (this.isStudentAuthenticated()) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            isTutor = request.isUserInRole("tut4youapp.tutor");
        }
        return isTutor;
    }

    /**
     * Determine if the student is authenticated and if so, make sure the session scope includes the User object for the authenticated student
     * @return true if the student making a request is authenticated, false otherwise.
     */
    public boolean isStudentAuthenticated() {
        boolean isAuthenticated = true;
        if (null == this.student) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String userName = request.getRemoteUser();
            if (userName == null) {
                isAuthenticated = false;
            } else {
                this.student = tut4youapp.find(userName);
                isAuthenticated = (this.student != null);
            }
        }
        return isAuthenticated;
    }
    
    /**
     * Logout the student and invalidate the session
     * @return success if student is logged out and session invalidated, failure otherwise.
     */
    public String logout() {
        String result = "failure";

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
            student = null;
            result = "success";
        } catch (ServletException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        return result;
    }
    /**
     * Gets a logged in username by getting the username from the session.
     * @return username
     * Source: https://dzone.com/articles/liferay-jsf-how-get-current-lo
     * Had further help by Subject2Change group.
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public String getUsernameFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String userName = request.getRemoteUser();
        return userName;
    }
}