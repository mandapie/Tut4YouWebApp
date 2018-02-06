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

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger("RequestBean");

    @EJB
    private Tut4YouApp tut4youApp;

    private Request request;
    private Subject subject;
    private Course course;
    private String time;
    private int numOfTutors;
    private List<Subject> subjectList = new ArrayList();
    private List<Course> courseList = new ArrayList();
    
    /**
     * RequestBean encapsulates all the functions/services involved
     * in making a request
     */
    public RequestBean() {
        request = new Request();
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
    
    /**
     * Creates a new request. If successful, get the number of tutors that tutors the course.
     * @return result to be redirected another page
     */
    public String createNewRequest() {
        String result = "failure";
        request = tut4youApp.newRequest(request);
        
        if (request != null) {
            numOfTutors = tut4youApp.getTutorsFromCourse(request.getCourse().getCourseName());
            result = "success";
            LOGGER.severe("Created new request!");
        }
        return result;
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
     * Loads all the subjects from the database.
     * @return a list of subjects
     */
    public List<Subject> getSubjectList() {
        if (subjectList.isEmpty()) {
            subjectList = tut4youApp.getSubjects();
            LOGGER.severe("Retrieved list of subjects from EJB");
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
    
    /**
     * ajax calls this method to load the courses based on the selected subject
     */
    public void changeSubject() {
        courseList = tut4youApp.getCourses(subject.getSubjectName());
        LOGGER.severe("Retrieved list of courses from EJB");
    }
}
