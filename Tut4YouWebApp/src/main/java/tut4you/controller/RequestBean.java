/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author Amanda
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

    public RequestBean() {
        request = new Request();
    }

    public Request getRequest() {
        return request;
    }

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

    public int getNumOfTutors() {
        return numOfTutors;
    }

    public void setNumOfTutors(int numOfTutors) {
        this.numOfTutors = numOfTutors;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject s) {
        subject = s;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTime() {
        return time;
    }

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

    public void setSubjectList(List<Subject> s) {
        subjectList = s;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

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
