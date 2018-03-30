/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.controller;

import java.util.Date;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import tut4you.model.*;

/**
 *
 * @author Amanda
 */
@Named
@ViewScoped
public class messageBean {
    private static final Logger LOGGER = Logger.getLogger("AddNewCourseBean");
    
    @EJB
    private Tut4YouApp tut4youApp;
    private User user;
    private String message;
    private Date timestamp;
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }    
}
