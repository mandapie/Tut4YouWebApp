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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import tut4you.model.*;

/**
 * Binds rating inputs to the EJB.
 *
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Named
@ViewScoped
public class RatingBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("RatingBean");

    @EJB
    private Tut4YouApp tut4youApp;

    private Rating rating;
    private Tutor tutor; //the tutor who accepts te rating
    private String studentName;
    private String username;
    private List<Tutor> tutorList = new ArrayList(); //list of available tutors
    private List<Rating> ratingList = new ArrayList(); //list of ratings
    private List<Request> requestList = new ArrayList(); //list of completed requests

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * RatingBean encapsulates all the functions/services involved in making a
     * rating
     */
    public RatingBean() {
        rating = new Rating();
    }

    public String getStudentName() {
        studentName = rating.getStudent().getFirstName() + rating.getStudent().getLastName();
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /**
     * Gets the Rating entity
     * @return the rating entity
     */
    public Rating getRating() {
        return rating;
    }

    /**
     * Sets the Rating entity
     * @param rating the rating entity
     */
    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public List<Tutor> getTutorList() {
        return tut4youApp.getTutorsList();
    }

    public void setTutorList(List<Tutor> c) {
        tutorList = c;
    }

    public String writeReviewPage(Tutor t) throws ParseException {
        String result;
        this.tutor = t;
        result = "writeReview";
        return result;
    }

    public void showUsername(String username) {
        this.tutor = findTutorEmail(username);
    }

    public Tutor findTutorEmail(String username) {
        return tut4youApp.findTutorByUsername(username);
    }

    /**
     * Convert string to Time
     * @param time
     * @return
     * @throws java.text.ParseException
     */
    public Date StringToTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = sdf.parse(time);
        return date;
    }

    public String createNewRating() throws ParseException {
        String result = "success";
        Date date = new Date();
        if(rating.getRatingValue()<=0) {
            FacesMessage message = new FacesMessage("Click 1-5 stars!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        rating.setDateRated(date);
        rating = tut4youApp.createRating(rating, tutor);
        tut4youApp.updateAverageRating(tutor.getEmail());
        return result;
    }

    /**
     * Updates the current rating of the tutor
     * @param rating
     * @return
     * @throws java.text.ParseException
     */
    public String updateRating(Rating rating) throws ParseException {
        String result = "failure";
        this.rating = rating;
        Date date = new Date();
        rating.setRatingValue(rating.getRatingValue());
        rating.setDescription(rating.getDescription());
        rating.setDateRated(date);
        tut4youApp.updateRating(rating, rating.getDescription(), rating.getRatingValue());
        tut4youApp.updateAverageRating(rating.getTutor().getEmail());
        if (rating != null) {
            result = "success";
            LOGGER.severe("Rating added");
        }
        return result;
    }

    /**
     * Delete the rating from the tutor
     * @param rating
     * @throws java.text.ParseException
     */
    public void deleteRating(Rating rating) throws ParseException {
        tut4youApp.deleteRating(rating);
        tut4youApp.updateAverageRating(rating.getTutor().getEmail());
    }
    
    public List<Request> getRequestList() {
        requestList = tut4youApp.getCompletedRequests();
        return requestList;
    }

    /**
     * Gets a list of the availabilities of the Tutor in the EJB
     * @param email
     * @return a list of subjects
     */
    public List<Rating> getRatingList(String email) {
        ratingList = tut4youApp.getRatingList(email);
        return ratingList;
    }

//    public void getAvgRating(String email) {
//        tut4youApp.getAverageRating(email);
//    }
}