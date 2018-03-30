/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.controller;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import tut4you.model.*;

/**
 *
 * @author Amanda
 */
@Named
@ViewScoped
public class ProfileBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger("ProfileBean");
    
    @EJB
    private Tut4YouApp tut4youapp;
    
    @Inject
    private UserBean userBean;
    
    private User user;
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
    
    public void showUsername() {
        String userEmail = userBean.returnEmailFromSession();
        User isSessionUser = tut4youapp.find(userEmail);
        if (user == null) {
            user = isSessionUser;
        }
        else {
            System.out.println(user);
        }
        username = user.getUserName();
    }
    
    public String goToTutorProfile(Tutor tutor) {
        user = tut4youapp.find(tutor.getEmail());
        System.out.println(user);
        username = user.getUserName();
        return "success";
    }
}
