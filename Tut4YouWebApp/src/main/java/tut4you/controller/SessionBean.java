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
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import tut4you.model.Request;
import tut4you.model.Session;
import tut4you.model.Tut4YouApp;
import tut4you.model.User;

/**
 * Binds session timer bean inputs to the EJB.
 *
 * @author Syed Haider <shayder426@gmail.com>
 */
@Named
@SessionScoped
public class SessionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private Tut4YouApp tut4youApp;

    private String email;
    private boolean checkAnswer;
    private Request request;
    private Session sessionTimer;
    private String securityAnswer;
    private String securityQuestion;
    private User user;
    
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }


    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Session getSessionTimer() {
        return sessionTimer;
    }

    public void setSessionTimer(Session sessionTimer) {
        this.sessionTimer = sessionTimer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * This checks and sees if the email inputted (for the security question) is
     * equivalent to the email in the database
     *
     * @return checkAnswer true if email is the same
     */
    public boolean isCheckAnswer() {
        return checkAnswer;
    }

    /**
     * Sets the checkAnswer attribute to true or false depending on if the
     * emails are equivalent
     *
     * @param checkAnswer
     */
    public void setCheckAnswer(boolean checkAnswer) {
        this.checkAnswer = checkAnswer;
    }

    /**
     * Gets the email that is inputted by the user
     *
     * @return email inputted by user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email that is inputted by the user
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This will set the start session time of the session
     *
     * @param r the active request of the tutoring session
     */
    public void startTutorSession(Request r) {
        Date startTime = new Date();
        sessionTimer.setStartSessionTime(startTime);
        System.out.println(sessionTimer);
        tut4youApp.startSessionTime(r, sessionTimer);
    }

    public boolean checkAnswer(String answer) {

        checkAnswer = tut4youApp.checkAnswer(answer);
        return checkAnswer;
    }
    
    public void loadData(User user)
    {
        this.user = user;
    }

    /**
     * This ends the tutoring session and sets the request to be a complete
     * status
     *
     * @param r the active request of the tutoring session
     */
    public void endTutorSession(Request r) {
        tut4youApp.setRequestToComplete(r);
    }

    /**
     * This will forward the tutor to the session timer page to start the
     * tutoring session
     *
     * @param r the active request
     * @return the webpage of the session timer
     */
    public String goToSessionTimerPage() {
        String result;
        System.out.println("PLS");
        //this.request = r;
        //this.securityQuestion = request.getStudent().getSecurityQuestion();
        //this.securityAnswer = request.getStudent().getSecurityAnswer();
        result = "sessionTimer";
        return result;
    }
}
