/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import tut4you.model.*;

/**
 *
 * @author Amanda
 * modified by Syed Haider
 */
@Named
@ViewScoped
public class ProfileBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("ProfileBean");

    @EJB
    private Tut4YouApp tut4youapp;

    @Inject
    private UserBean userBean;
    
    @ManagedProperty("#{param.username}")
    private String username;
    private User user;
    private String dateJoinedAsTutor;
    private boolean userRated;

    /**
     * Creates a new instance of ProfileBean
     */
    @PostConstruct
    public void createProfileBean() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
        public boolean isUserRated() {
        return userRated;
    }
        
    public String getDateJoinedAsTutor() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        dateJoinedAsTutor = sdf.format(tut4youapp.getDateJoinedAsTutor());
        return dateJoinedAsTutor;
    }

    public void setDateJoinedAsTutor(String dateJoinedAsTutor) {
        this.dateJoinedAsTutor = dateJoinedAsTutor;
    }

    public void setUserRated(boolean userRated) {
        this.userRated = userRated;
    }
    public boolean checkIfUserRated(String email)
    {
        return tut4youapp.checkIfUserRated(email);
    }

    public void showUsername(String username) {
        Tutor tutor = findTutorEmail(username);
        user = tut4youapp.findUser(tutor.getEmail());
    }
    
    public Tutor findTutorEmail(String username)
    {
        return tut4youapp.findTutorEmail(username);
    }
}