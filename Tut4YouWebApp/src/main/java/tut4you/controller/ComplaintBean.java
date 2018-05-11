/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code has been developed by a group of CSULB students working on their 
 *  Computer Science senior project called Tutors4You.
 *  
 *  Tutors4You is a web application that students can utilize to findUser a tutor and
 *  ask them to meet at any location of their choosing. Students that struggle to understand 
 *  the courses they are taking would benefit from this peer to peer tutoring service.
 
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import tut4you.model.Complaint;
import tut4you.model.Course;

import tut4you.model.Tut4YouApp;
import tut4you.model.Tutor;
import tut4you.model.User;


/**
 * Uploads .pdf files to an Amazon bucket.
 * @author Andrew Kaichi <ahkaichi@gmail.com>
 */
@Named
//@RequestScoped
@ViewScoped
public class ComplaintBean implements Serializable {
    @EJB
    private Tut4YouApp tut4youApp;
    private User user;

    @ManagedProperty("#{param.username}")
    private String username;
    private Complaint complaint;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }
    /**
     * Creates an instance of the courseBean
     */
    @PostConstruct
    public void createComplaintBean() {
        complaint = new Complaint();
    }
    
    /**
     * Destroys an instance of the courseBean
     */
    @PreDestroy
    public void destroyComplaintBean() {
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void showUsername(String username) {
        Tutor tutor = findTutorEmail(username);
        user = tut4youApp.findUser(tutor.getEmail());
    }
    public Tutor findTutorEmail(String username)
    {
        return tut4youApp.findTutorEmail(username);
    }
    public void createNewComplaint() {
        tut4youApp.createNewComplaint(user, complaint);
    }
}