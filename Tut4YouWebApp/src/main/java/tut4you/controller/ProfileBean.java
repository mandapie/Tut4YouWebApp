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
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import tut4you.model.*;

/**
 * ProfileBean gets the username to show the profile of a user
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 * modified by Syed Haider
 */
@Named
@ViewScoped
public class ProfileBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("ProfileBean");

    @EJB
    private Tut4YouApp tut4youapp;

    private User user;
    @ManagedProperty("#{param.username}")
    private String username;

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

    public void showUsername(String username) {
        Tutor tutor = findTutorEmail(username);
        user = tut4youapp.findUser(tutor.getEmail());
    }
    
    public Tutor findTutorEmail(String username) {
        return tut4youapp.findTutorEmail(username);
    }
}
