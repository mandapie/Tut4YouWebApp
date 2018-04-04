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
 * Modified by Andrew Kaichi <ahkaichi@gmail.com>
 */
@Named
@SessionScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("UserBean");
    
    @EJB
    private Tut4YouApp tut4youapp;
    
    private User user;
    boolean doNotDisturb;
    int tabIndex;
    boolean condition;
    private String currentZip;
    
    /**
     * Creates a new instance of UserIdentity
     */
    @PostConstruct
    public void createUserBean() {
        user = null;
        condition = true;
    }
    /** 
     * Destroys a new instance of UserIdentity
     */
    @PreDestroy
    public void destroyUserBean() {
    }
    /**
     * get current zip
     * @return currentZip
     */
    public String getCurrentZip() {
        return currentZip;
    }
    /**
     * set current zip
     * @param currentZip 
     */
    public void setCurrentZip(String currentZip) {
        this.currentZip = currentZip;
    }
    /**
     * updates current zip
     */
    public void updateCurrentZip() {
        Tutor tutor = tut4youapp.updateCurrentZip(currentZip);
        if(tutor.getCurrentZip() != null) {
            condition = false;
        }
        System.out.print("Current Zip: " + currentZip);
    }
    /**
     * get boolean condition
     * @return condition
     */
    public boolean isCondition() {
        return condition;
    }
    /**
     * set boolean condition
     * @param condition 
     */
    public void setCondition(boolean condition) {
        this.condition = condition;
    }
    /**
     * Gets the Tutor object
     * @return 
     */
    public User getUser() {
        return user;
    }
    
    /**
     * Sets the User object
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }
    public void resetCurrentZip() {
        tut4youapp.resetCurrentZip();
    }
//    /**
//     * Updates a User's information
//     * @param object User or Tutor object
//     */
//    public void updateUser(Object object){
//        if (object == student){
//           this.student = (User)object;
//           tut4youapp.updateUser(student); 
//        }
//        else if (object == userTutor){
//            this.student = (User)object;
//            this.userTutor = (Tutor)object;
//            tut4youapp.updateUser(this.student);
//        }
//        
//    }
    /**
     * Updates a User's information
     * @param object User or Tutor object
     */
    public void updateUser(User user){
        tut4youapp.updateUser(user); 

        
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
     * Gets the index of the tab
     *
     * @return tabIndex
     */
    public int getTabIndex() {
        return tabIndex;
    }

    /**
     * Sets the index of the tab
     *
     * @param tabIndex
     */
    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * Called the EJG to update the state of doNotDisturb
     * @param d 
     */
    public void updateDoNotDisturb(Boolean d) {
        tut4youapp.updateDoNotDisturb(doNotDisturb);
    }

    /**
     * Determine if the user is authenticated and if so, make sure the session scope includes the User object for the authenticated user
     * @return true if the user making a request is authenticated, false otherwise.
     */
    public boolean isIsUserAuthenticated() {
        boolean isAuthenticated = true;
        if (null == this.user) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            String userName = request.getRemoteUser();
            if (userName == null) {
                isAuthenticated = false;
            } else {
                this.user = tut4youapp.find(userName);
                isAuthenticated = (this.user != null);
            }
        }
        return isAuthenticated;
    }
    
    /**
     * Determine if current authenticated user has the role of tutor
     * @return true if user has role of tutor, false otherwise.
     */
    public boolean isIsTutor() {
        boolean isTutor = false;
        if (this.isIsUserAuthenticated()) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            isTutor = request.isUserInRole("tut4youapp.tutor");
        }
        return isTutor;
    }
    /**
     * Logout the student and invalidate the session
     * @return success if student is logged out and session invalidated, failure otherwise.
     */
    /**
     * Logout the student and invalidate the session
     * @return success if student is logged out and session invalidated, failure otherwise.
     */
    public String logout() {
        String result = "failure";

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            resetCurrentZip();
            request.logout();
            user = null;
            currentZip = null;
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
