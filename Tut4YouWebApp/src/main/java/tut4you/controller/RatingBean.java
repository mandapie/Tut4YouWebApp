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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import tut4you.model.*;

/**
 * Binds rating inputs to the EJB.
 *
 * @author Syed Haider <shayder426@gmail.com>
 */
@Named
@RequestScoped
public class RatingBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("RatingBean");
    private List<Tutor> tutorList = new ArrayList(); //list of available tutors
    private List<Tutor> completedTutorList = new ArrayList(); //list of tutors that have completed sessions
    private List<Rating> ratingList = new ArrayList(); //list of ratings
    private List<Request> requestList = new ArrayList(); //list of completed requests

    @EJB
    private Tut4YouApp tut4youApp;

    private Rating rating;
    private Tutor tutor; //the tutor who accepts te rating
    private String studentName;

    /**
     * RatingBean encapsulates all the functions/services involved in making a
     * rating
     */
    public RatingBean() {
        rating = new Rating();
    }

    /**
     * Returns the full name of the student
     *
     * @return the full name of the student
     */
    public String getStudentName() {
        studentName = rating.getStudent().getFirstName() + rating.getStudent().getLastName();
        return studentName;
    }

    /**
     * Sets the studentName
     *
     * @param studentName the name of the student
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /**
     * Gets the Rating entity
     *
     * @return the rating entity
     */
    public Rating getRating() {
        return rating;
    }

    /**
     * Sets the Rating entity
     *
     * @param rating the rating entity
     */
    public void setRating(Rating rating) {
        this.rating = rating;
    }

    /**
     * Gets the tutor who is being rated
     *
     * @return
     */
    public Tutor getTutor() {
        return tutor;
    }

    /**
     * Sets the tutor who is being rated
     *
     * @param tutor
     */
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    /**
     * Gets the list of
     *
     * @return
     */
    public List<Request> getCompletedRequests() {
        return tut4youApp.getCompletedRequests();
    }

    /**
     * Sets the list of
     *
     * @param c the list of tutors being rated
     */
    public void setTutorList(List<Tutor> c) {
        tutorList = c;
    }

    /**
     * This will forward the user to the webpage to write a review on a specific
     * Tutor
     *
     * @param t tutor who is being rated
     * @return the faces-config webpage
     * @throws ParseException
     */
    public String writeReviewPage(Tutor t) throws ParseException {
        String result;
        this.tutor = t;
        result = "writeReview";
        return result;
    }

    /**
     * Creates a new rating for a tutor
     *
     * @param t the tutor being rated
     * @return user sent to home page
     * @throws ParseException
     */
    public String createNewRating(Tutor t) throws ParseException {
        String result = "success";
        System.out.println("Tutor t: " + t.toString());
        Date date = new Date();
        rating.setCurrentTime(date);
        rating = tut4youApp.newRating(rating, t);
        return result;
    }

    /**
     * Updates the rating that the student has given a tutor
     *
     * @param rating the rating that is being updated
     * @return the
     * @throws java.text.ParseException
     */
    public String updateRating(Rating rating) throws ParseException {
        String result = "success";
        this.rating = rating;
        Date date = new Date();
        rating.setRatingValue(rating.getRatingValue());
        rating.setDescription(rating.getDescription());
        rating.setCurrentTime(date);
        tut4youApp.updateRating(rating, rating.getDescription(), rating.getRatingValue());
        return result;
    }

    /**
     * Delete the rating for the tutor
     *
     * @param rating the rating to be delete
     * @throws java.text.ParseException
     */
    public void deleteRating(Rating rating) throws ParseException {
        tut4youApp.deleteRating(rating);

    }

    /**
     * Gets a list of the ratings of the Tutor
     *
     * @return a list of ratings
     */
    public List<Rating> getRatingList() {
        ratingList = tut4youApp.getRatingList();
        return ratingList;
    }

    /**
     * Gets a list of completed requests
     *
     * @return a list of requests
     */
    public List<Request> getRequestList() {
        requestList = tut4youApp.getCompletedRequests();
        return requestList;
    }

    /**
     * Gets a list of completed sessions with tutors
     *
     * @return a list of tutors
     */
    public List<Tutor> getTutorList() {
        return tut4youApp.getTutorsList();
    }

 

    /**
     * Gets the average rating of the tutor
     *
     * @return averageRating of the tutor
     */
    public int getAvgRating() {
        return (int) tut4youApp.getAverageRating();
    }
}
