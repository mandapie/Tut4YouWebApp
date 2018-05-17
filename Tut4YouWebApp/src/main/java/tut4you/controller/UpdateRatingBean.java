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
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import tut4you.model.*;

/**
 * Binds rating inputs to the EJB.
 *
 * @author Syed Haider <daikiraidemodaisuki@gmail.com>
 */
@Named
@SessionScoped
public class UpdateRatingBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("RatingBean");

    @EJB
    private Tut4YouApp tut4youApp;

    private Rating rating;
    private Tutor tutor; //the tutor who accepts te rating
    private String studentName;


    /**
     * RatingBean encapsulates all the functions/services involved in making a
     * rating
     */
    public UpdateRatingBean() {
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

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    /**
     * Updates the current availability of the tutor
     *
     * @param avail
     * @return
     * @throws java.text.ParseException
     */
    public String updateRating(Rating r) throws ParseException {
        String result = "failure";
        this.rating = r;
     /*   Date date = new Date();
        rating.setRatingValue(r.getRatingValue());
        rating.setDescription(r.getDescription());
        rating.setDateRated(date);*/
        tut4youApp.updateRating(rating, rating.getDescription(), rating.getRatingValue());
        if (rating != null) {
            result = "success";
            LOGGER.severe("Rating added");
        }
        return result;
    }
    
        /**
     * Goes to the edit availability page
     *
     * @param r the availability to be edited
     * @return the webpage of edit availability
     * @throws ParseException
     */
    public String goToEditRatingPage(Rating r) throws ParseException {
        String result;
        rating = r;
        System.out.println(rating);
        result = "editRating";
        return result;
    }
}
