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
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import tut4you.model.*;

/**
 *
 * @author Amanda
 */
@Named
@SessionScoped
public class ProfileBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("ProfileBean");

    @EJB
    private Tut4YouApp tut4youapp;

    @Inject
    private UserBean userBean;

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
        //String userEmail = userBean.returnEmailFromSession();
        // System.out.println(userEmail);
        UIComponent component = null;
        //User user = (User)component.getAttributes().get("user");
        System.out.println("Did this make it here");
        System.out.println("from showusername: " + username);
        String email = "amanda@gmail.com";
        user = tut4youapp.findUser(email);
        System.out.println(user);
        //username = user.getUsername();
    }

    public String goToTutorProfile(Tutor tutor) {
        user = tut4youapp.findTutor(tutor.getEmail());
        System.out.println(user);
        username = user.getUsername();
        System.out.println(username);
        return "tutorProfile";
    }
}
