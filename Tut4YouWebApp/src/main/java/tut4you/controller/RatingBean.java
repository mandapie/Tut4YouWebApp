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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import okhttp3.OkHttpClient;
import tut4you.model.*;

/**
 * Binds rating inputs to the EJB.
 *
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Named
@ConversationScoped
public class RatingBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("RatingBean");

    @EJB
    private Tut4YouApp tut4youApp;
    private Request request;
    private Rating rating;
    private User user;
    private Tutor tutor; //the tutor who accepts te rating
    private String studentName;
    private String username;
    private List<Tutor> tutorList = new ArrayList(); //list of available tutors
    private List<Rating> ratingList = new ArrayList(); //list of ratings
    private List<Request> requestList = new ArrayList(); //list of completed requests
    private boolean isTutor;
    private @Inject
    Conversation conversation;

    /**
     * RequestBean encapsulates all the functions/services involved in making a
     * request
     */
    @PostConstruct
    public void RatingBean() {
        rating = new Rating();
        endConversation();
        initConversation();

    }

    /**
     * initialize the conversation scope
     */
    public void initConversation() {
        if (!FacesContext.getCurrentInstance().isPostback() && conversation.isTransient()) {
            conversation.begin();
        }
    }

    /**
     * End the conversation scope
     */
    public void endConversation() {

        if (!conversation.isTransient()) {
            conversation.end();
        }
    }
    public boolean isIsTutor() {
        return isTutor;
    }

    public void setIsTutor(boolean isTutor) {
        this.isTutor = isTutor;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<Tutor> getTutorList() {
        return tut4youApp.getTutorsList();
    }

    public void setTutorList(List<Tutor> c) {
        tutorList = c;
    }

    public String goToWriteReviewPage(Tutor t) throws ParseException {
        String result;
        this.tutor = t;
                System.out.println(tutor);

        result = "writeReview";
        return result;
    }
    
    public String goToSubmitComplaintPage(User user, boolean isTutor) throws ParseException {
        String result;
        this.user = user;
        this.isTutor = isTutor;

        result = "submitComplaint";
        return result;
    }

    public void showUsername(String username) {
        this.tutor = findTutorByUsername(username);
    }
    
    public Tutor findTutorByUsername(String username) {
        return tut4youApp.findTutorByUsername(username);
    }

    /**
     * Convert string to Time
     *
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
        if (rating.getRatingValue() <= 0) {
            FacesMessage message = new FacesMessage("Click 1-5 stars!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        rating = tut4youApp.createRating(rating, tutor);
        tut4youApp.updateAverageRating(tutor.getEmail());
        return result;
    }

    /**
     * Delete the rating from the tutor
     *
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
     *
     * @param email
     * @return a list of subjects
     */
    public List<Rating> getRatingList(String email) {
        ratingList = tut4youApp.getRatingList(email);
        return ratingList;
    }

}
