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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    private User student;
    private SessionBean sessionBean;

    public SessionBean() {
        sessionTimer = new Session();
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

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
    public void startTutorSession() {
        System.out.println("have ya been hit");
        Date startTime = new Date();
        System.out.println(startTime);
        System.out.println(sessionTimer);
        sessionTimer.setStartSessionTime(startTime);
        // sessionTimer.setEndSessionTime(startTime);
        System.out.println(request);

        tut4youApp.startSessionTime(request, sessionTimer);
    }

    public String endTutorSession() {
        String result;
        System.out.println("End Session");
        Date endTime = new Date();
        System.out.println(endTime);
        System.out.println(sessionTimer);
        System.out.println(request);
        result = tut4youApp.setRequestToComplete(request,sessionTimer);
        return result;
    }

    public Date getDate() {
        Date date = new Date();
        return date;
    }

    public boolean checkAnswer(String answer) {
        //System.out.println("answer: "  + answer);
        String email = request.getStudent().getEmail();
        checkAnswer = tut4youApp.checkAnswer(answer, email);
        //System.out.println("checkAnswer:" + checkAnswer);
        if (!checkAnswer) {
            FacesMessage message = new FacesMessage("Answer is false. Try again.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return checkAnswer;
    }

    public void loadData(User user) {
        this.student = user;
    }

    /**
     * This ends the tutoring session and sets the request to be a complete
     * status
     *
     * @param r the active request of the tutoring session
     */
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
}
