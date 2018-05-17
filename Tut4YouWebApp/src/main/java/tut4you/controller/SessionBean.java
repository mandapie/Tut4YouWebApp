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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
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
@ViewScoped
public class SessionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private Tut4YouApp tut4youapp;
    @ManagedProperty("#{param.id}")
    private Long id; //requestId
    private Request request;
    private boolean correctAnswer;
    private Session sessionTimer;
    private String securityAnswer;
    private String securityQuestion;
    private User student;
    /**
     * This will check if the "Start Button" on the "sessionTimer.xhtml" page
     * has been clicked.
     */
    private boolean checkStartButtonState = false;
    private boolean checkEndButtonState = true;
    /**
     * post construct
     */
    @PostConstruct
    public void createSessionBean() {
        sessionTimer = new Session();
    }

    /**
     * get id
     * @return 
     */
    public Long getId() {
        return id;
    }

    /** set id
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets checkStartButtonState
     *
     * @return true if start button is clicked
     */
    public boolean isCheckStartButtonState() {
        return checkStartButtonState;
    }

    /**
     * Sets checkStartButtonState to true or false
     *
     * @param checkStartButtonState true if clicked
     */
    public void setCheckStartButtonState(boolean checkStartButtonState) {
        this.checkStartButtonState = checkStartButtonState;
    }

    /**
     * Gets checkEndButtonState
     *
     * @return true if button is clicked
     */
    public boolean isCheckEndButtonState() {
        return checkEndButtonState;
    }

    /**
     * Sets checkEndButtonState to true or false
     *
     * @param checkEndButtonState true if button is clicked
     */
    public void setCheckEndButtonState(boolean checkEndButtonState) {
        this.checkEndButtonState = checkEndButtonState;
    }

    /**
     * Gets the student of the session
     *
     * @return student - student of session
     */
    public User getStudent() {
        return student;
    }

    /**
     * Sets the student of the session
     *
     * @param student - student of session
     */
    public void setStudent(User student) {
        this.student = student;
    }

    /**
     * Gets the security answer
     *
     * @return securityAnswer
     */
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    /**
     * Sets the security answer
     *
     * @param securityAnswer
     */
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    /**
     * Gets the security question
     *
     * @return securityQuestion
     */
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * Sets the security question
     *
     * @param securityQuestion
     */
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    /**
     * Gets the request
     *
     * @return request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the request of a session
     *
     * @param request
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * Gets the session
     *
     * @return
     */
    public Session getSessionTimer() {
        return sessionTimer;
    }

    /**
     * Sets the session
     *
     * @param sessionTimer
     */
    public void setSessionTimer(Session sessionTimer) {
        this.sessionTimer = sessionTimer;
    }

    /**
     * Gets correctAnswer (true if security answer is correct)
     *
     * @return correctAnswer true if email is the same
     */
    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Sets the correctAnswer attribute to true or false depending on if the security answer is equivalent
     *
     * @param correctAnswer
     */
    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * This will set the start session time of the session
     *
     */
    public void startTutorSession() {
        sessionTimer = tut4youapp.startSessionTime(request, sessionTimer);
        checkStartButtonState = true;
        checkEndButtonState = false;
    }

    /**
     * This ends the tutoring session and sets the request to be a complete
     * status
     *
     * @return 
     */
    public String endTutorSession() {
        return tut4youapp.endSessionTime(request, sessionTimer);
    }

    /**
     *
     * This checks to see if the securityAnswer answers the security question
     */
    public void checkAnswer() {
        String email = request.getStudent().getEmail();
        correctAnswer = tut4youapp.checkAnswer(securityAnswer, email);
        if (!correctAnswer) {
            FacesMessage message = new FacesMessage("Incorrect Answer","Try again.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    /**
     * This will forward the tutor to the session timer page to start the
     * tutoring session
     *
     * @param r the active request
     * @return the webpage of the session timer
     */
    public String goToSessionTimerPage(Request r) {
        String result;
        this.request = r;
        this.student = request.getStudent();
        this.securityQuestion = request.getStudent().getSecurityQuestion();
        result = "sessionTimer";
        return result;
    }

    /**
     * This displays when a tutor will click "Start Session" on the
     * sessionTimer.xhtml page
     */
    public void showGrowlStartMessage() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful!", "You started your session!"));
    }

    /**
     * set request based on id
     * @param id 
     */
    public void showRequestId(Long id) {
        request = tut4youapp.findRequest(id);
    }
}