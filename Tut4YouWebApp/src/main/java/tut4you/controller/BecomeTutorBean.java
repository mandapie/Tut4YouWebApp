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
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import tut4you.model.*;

/**
 * UserBean checks if a user is authenticated.
 *
 * @author Alvaro Monge <alvaro.monge@csulb.edu>
 * Modified by Amanda Pan <daikiraidemodaisuki@gmail.com>
 * Modified by Andrew Kaichi <ahkaichi@gmail.com>
 */
@Named
@ViewScoped
public class BecomeTutorBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("UserBean");

    @EJB
    private Tut4YouApp tut4youapp;
    @Inject UserBean userBean;
    private String currentZip;
    private String hourlyRate;
    private ZipCode zipCode;

    private String defaultZip;
    private int maxRadius;
    private Date joinedDateAsTutor;
    /**
     * Creates a new instance of UserIdentity
     */
    @PostConstruct
    public void createUserBean() {
        zipCode = new ZipCode();
    }

    /**
     * Destroys a new instance of UserIdentity
     */
    @PreDestroy
    public void destroyUserBean() {
    }
    public ZipCode getZipCode() {
        return zipCode;
    }

    public void setZipCode(ZipCode zipCode) {
        this.zipCode = zipCode;
    }
    public String getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getDefaultZip() {
        return defaultZip;
    }

    public void setDefaultZip(String defaultZip) {
        this.defaultZip = defaultZip;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }

    public Date getJoinedDateAsTutor() {
        return joinedDateAsTutor;
    }

    public void setJoinedDateAsTutor(Date joinedDateAsTutor) {
        this.joinedDateAsTutor = joinedDateAsTutor;
    }

    /**
     * get current zip
     *
     * @return currentZip
     */
    public String getCurrentZip() {
        return currentZip;
    }
    /**
     * set current zip
     *
     * @param currentZip
     */
    public void setCurrentZip(String currentZip) {
        this.currentZip = currentZip;
    }

    /**
     * https://stackoverflow.com/questions/33098603/convert-localtime-java-8-to-date
     * gets the current date which is used for date joined attribute in tutor
     * @return date joined
     * @throws ParseException 
     */
    public Date getCurrentDate() throws ParseException {
        LocalTime d = LocalTime.now();
        Instant instant = d.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
        Date time = Date.from(instant);
        return time;
    }
    /**
     * Allows student to become a tutor as well
     * @throws ParseException 
     */
    public void becomeTutor() throws ParseException {
        double pr = 0;
        if (hourlyRate != null) {
            pr = Double.parseDouble(hourlyRate);
        }
        joinedDateAsTutor = getCurrentDate();
        tut4youapp.becomeTutor(pr, joinedDateAsTutor, defaultZip, zipCode);
    }
    public void showGrowlMessage() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful!", "Please log out and log back in to access tutor features"));
    }

}
