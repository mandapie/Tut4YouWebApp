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
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import tut4you.model.*;

/**
 * Binds request inputs to the EJB.
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Named
@SessionScoped
public class RequestBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger("RequestBean");

    @EJB
    private Tut4YouApp tut4youApp;

    private Request request;
    private Subject subject;
    private Course course;
    private String time;
    private String stringLaterTime;
    private String stringLengthOfSession;
    private int numOfTutors; //number of tutors who teaches the course
    private List<Subject> subjectList = new ArrayList(); //list of subjects to be loaded to the request form
    private List<Course> courseList = new ArrayList(); //list of courses based on subject to load to the request form
    private List<Request> requestList = new ArrayList(); //list of pending requests
    private List<Tutor> tutorList = new ArrayList(); //list of available tutors
    private Tutor tutor; //the tutor who accepts te request
    
    /**
     * RequestBean encapsulates all the functions/services involved
     * in making a request
     */
    public RequestBean() {
        request = new Request();
    }
        
    public String getCurrentTime() throws ParseException {
        String stringCurrentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        //java.util.Date currentTime = StringToTime(stringCurrentTime);
        return stringCurrentTime;
    }
    
    /**
     * Gets current day of when the request is made
     * @return string of the current day
     */
    public String getCurrentDayOfWeek() {
        String currentDay = LocalDate.now().getDayOfWeek().name();
        return currentDay;
    }
    
    /**
     * Gets the Request entity
     * @return the request entity
     */
    public Request getRequest() {
        return request;
    }
    
    /**
     * Sets the Request entity
     * @param request the request entity
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    public Tutor getTutor() {
        return tutor;
    }
    
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
        
    public List<Request> getRequestList() {
        requestList = tut4youApp.getActiveRequest();
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }
    
    /**
     * Gets the number of tutors who fit the criteria of a request
     * @return the number of tutors available
     */
    public int getNumOfTutors() {
        return numOfTutors;
    }
    
    /**
     * Sets the number of tutors who fit the criteria of a request 
     * @param numOfTutors the tutors who are available
     */
    public void setNumOfTutors(int numOfTutors) {
        this.numOfTutors = numOfTutors;
    }
    
    /**
     *  Gets the subject of the request
     * @return subject of the request
     */
    public Subject getSubject() {
        return subject;
    }
    
    /**
     * Sets the subject of the request
     * @param s the subject of the request
     */
    public void setSubject(Subject s) {
        subject = s;
    }
    
    /**
     * Gets the course of the request 
     * @return course of the request
     */
    public Course getCourse() {
        return course;
    }
    
    /**
     * Sets the course of the request
     * @param course the course of the request
     */
    public void setCourse(Course course) {
        this.course = course;
    }
    
    /**
     * Get the time of the request 
     * @return the time of the request
     */
    public String getTime() {
        return time;
    }
    
    /**
     * Sets the time of the request
     * @param time the time of the request
     */
    public void setTime(String time) {
        this.time = time;
    }
    
    /**
     * Get the time of the request if user set for later 
     * @return the time of the request
     */
    public String getStringLaterTime() {
        return stringLaterTime;
    }
    
    /**
     * Sets the time of the request if user wants a request for later
     * @param stringLaterTime the time of the request if for later
     */
    public void setStringLaterTime(String stringLaterTime) {
        this.stringLaterTime = stringLaterTime;
    }
    
    /**
     * gets string length of session
     * @return stringLengthOfSession
     */
    public String getStringLengthOfSession() {
        return stringLengthOfSession;
    }
    
    /**
     * sets string length of session
     * @param stringLengthOfSession 
     */
    public void setStringLengthOfSession(String stringLengthOfSession) {
        this.stringLengthOfSession = stringLengthOfSession;
    }
    
    /**
     * Loads all the subjects from the database.
     * @return a list of subjects
     */
    public List<Subject> getSubjectList() {
        if (subjectList.isEmpty()) {
            subjectList = tut4youApp.getSubjects();
        }
        return subjectList;
    }
    
    /**
     * Sets the list of subjects in the request
     * @param s the list of subjects 
     */
    public void setSubjectList(List<Subject> s) {
        subjectList = s;
    }
    
    /**
     * Gets the courses in the drop-down menu of request
     * @return the list of courses in database
     */
    public List<Course> getCourseList() {
        return courseList;
    }
    
    /**
     * Sets the course in the drop-down menu of request
     * @param c the list of courses in the drop-down
     */
    public void setCourseList(List<Course> c) {
        courseList = c;
    }

    public List<Tutor> getTutorList() {
        return tutorList;
    }
    
    public void setTutorList(List<Tutor> c) {
        tutorList = c;
    }
    
    /**
     * ajax calls this method to load the courses based on the selected subject
     */
    public void changeSubject() {
        courseList = tut4youApp.getCourses(subject.getSubjectName());
    }
    
    /**
     * Convert string to Time
     * @param time
     * @return 
     * @throws java.text.ParseException
     */
    public java.util.Date StringToTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        java.util.Date date = sdf.parse(time);
        return date;
    }
    
    /**
     * converts String to int type
     * @param string
     * @return 
     */
    public int StringToInt(String string) {
        int integer = Integer.parseInt(string);
        return integer;
    }
    
    /**
     * Creates a new request. If successful, get the number of tutors that tutors the course.
     * @return result to be redirected another page
     * @throws java.text.ParseException
     */
    public String createNewRequest() throws ParseException {
        String result = "failure";
        if(time.equals("Later")) {
            request.setCurrentTime(StringToTime(getStringLaterTime()));
        }
        else {
            request.setCurrentTime(StringToTime(getCurrentTime()));
        }
        request.setDayOfWeek(getCurrentDayOfWeek());
        request.setLengthOfSession(StringToInt(stringLengthOfSession));
        request = tut4youApp.newRequest(request);
        
        if (request != null) {
            numOfTutors = tut4youApp.getNumOfTutorsFromCourse(request.getCourse().getCourseName());
            result = "success";
            tutorList = tut4youApp.getTutorsFromCourse(request.getCourse().getCourseName(), request.getDayOfWeek().toUpperCase(), request.getCurrentTime(), true);
        }
        return result;
    }
    
    /**
     * Send request to a specific tutor
     * @param t 
     */
    public void sendToTutor(Tutor t) {
        tut4youApp.addPendingRequest(t, request);
    }
    
    /**
     * Sets a tutor to the request if tutor accepts
     * @param r 
     */
    public void setTutorToRequest(Request r) {
        tut4youApp.setTutorToRequest(r);
    }
    
    /**
     * Change the status of a request
     * @param r
     */
    public void cancelRequest(Request r) {
        tut4youApp.cancelRequest(r);
    }

    /**
     * Remove the request from the notification list
     * @param r 
     */
    public void removeRequestFromTutor(Request r) {
        tut4youApp.removeRequestFromNotification(r);
    }
}