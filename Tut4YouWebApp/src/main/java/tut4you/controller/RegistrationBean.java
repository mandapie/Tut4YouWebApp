/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author Amanda
 */
@Named
@RequestScoped
public class RegistrationBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger("RequestBean");
    
    @EJB
    private Tut4YouApp tut4youApp;
    private Student newStudent;
    private Tutor newTutor;
    private String confirmPassword;
    private String studentType;
    private Boolean isStudent;
    
    /** Creates a new instance of Registration */
    public RegistrationBean() {
        newStudent = new Student();
        newTutor = new Tutor();
    }
    
    public Student getNewStudent() {
        return newStudent;
    }

    public void setNewStudent(Student newStudent) {
        this.newStudent = newStudent;
    }
    public Tutor getNewTutor() {
        return newTutor;
    }

    public void setNewTutor(Tutor newTutor) {
        this.newTutor = newTutor;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String getStudentType() {
        return studentType;
    }

    public void setStudentType(String studentType) {
        this.studentType = studentType;
    }
    
    public Boolean getIsStudent() {
        if(isStudent.equals("Student")) {
            return true;
        }
        return false;
    }
    
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
