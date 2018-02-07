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
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import tut4you.model.*;
import tut4you.exception.*;

/**
 * RegistrationBean encapsulates all the functions/services involved 
 in registering as a User or Tutor.
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 * @author Keith Tran <keithtran25@gmail.com>
 */
@Named
@RequestScoped
public class RegistrationBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger("RequestBean");
    
    @EJB
    private Tut4YouApp tut4youApp;
    private User newStudent;
    private Tutor newTutor;
    private String confirmPassword;
    private String studentType;
    private Boolean isStudent;
    
    /** Creates a new instance of Registration */
    public RegistrationBean() {
        newStudent = new User();
        newTutor = new Tutor();
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
     * Gets the tutor that has just registered
     * @return newTutor the tutor that just registered
     */
    public Tutor getNewTutor() {
        return newTutor;
    }
    
    /**
     * Sets the user to be a Tutor
     * @param newTutor the newly registered Tutor
     */
    public void setNewTutor(Tutor newTutor) {
        this.newTutor = newTutor;
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
     * Gets the studentType
     * @return the studentType is Tutor or User
     */
    public String getStudentType() {
        return studentType;
    }
    
    /**
     * Sets the studentType
     * @param studentType the studentType is tutor or student
     */
    public void setStudentType(String studentType) {
        this.studentType = studentType;
    }
    
    /**
     * Checks to see if the user is a student
     * @return true if user is a student
     */
    public Boolean getIsStudent() {
        if(isStudent.equals("Student")) {
            return true;
        }
        return false;
    }
    
    /**
     * JSF Action that uses the information submitted in the registration page
 to add user as a registered User user.
     * @return either failure, success, or register depending on successful
     * registration.
     */
    public String createStudent() {
        String result = "failure";
        if (newStudent.isInformationValid(confirmPassword)) {
            String clearText = newStudent.getPassword();
            newStudent.setPassword(tut4you.controller.HashPassword.getSHA512Digest(newStudent.getPassword()));
            LOGGER.log(Level.SEVERE, "Password: {0}\tHashed password: {1}", new Object[]{clearText, newStudent.getPassword()});
            try {
                tut4youApp.registerStudent(newStudent, "tut4youapp.student");
                result = "success";
            } catch (StudentExistsException see) {
                LOGGER.log(Level.SEVERE, null, see);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("A user with that information already exists, try again."));
                result = "register";
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, null, e);
                result = "failure";
            }
        }
        return result;
    }
    
    /**
     * JSF Action that uses the information submitted in the registration page
     * to add user as a registered Tutor user.
     * @return either failure, success, or register depending on successful 
     * registration.
     */
    public String createTutor() {
        String result = "failure";
        if (newTutor.isInformationValid(confirmPassword)) {
            String clearText = newTutor.getPassword();
            newTutor.setPassword(tut4you.controller.HashPassword.getSHA512Digest(newTutor.getPassword()));
            LOGGER.log(Level.SEVERE, "Password: {0}\tHashed password: {1}", new Object[]{clearText, newTutor.getPassword()});
            try {
                tut4youApp.registerTutor(newTutor, "tut4youapp.student", "tut4youapp.tutor");
                result = "success";
            } catch (StudentExistsException see) {
                LOGGER.log(Level.SEVERE, null, see);
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
