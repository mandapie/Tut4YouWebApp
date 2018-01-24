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
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import tut4you.model.*;

/**
 *
 * @author Amanda
 */
@Named
@SessionScoped
public class UserBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger("UserBean");
    
    @EJB
    private Tut4YouApp tut4youapp;
    private Student student;
    
    /** Creates a new instance of UserIdentity */
    public UserBean() {
        student = null;
    }
    
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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
                if (isAuthenticated) {
                    LOGGER.log(Level.SEVERE, "StudentIdentiy::isStudentAuthenticated: Changed session, so now studentIdentiy object has student=authenticated student");
                }
            }
        }
        return isAuthenticated;
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
}
