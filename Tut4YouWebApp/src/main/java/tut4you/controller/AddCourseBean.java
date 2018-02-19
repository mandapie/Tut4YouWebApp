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
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import tut4you.exception.CourseExistsException;
import tut4you.model.*;
import tut4you.model.Tut4YouApp;

/**
 * Adds courses to the tutor.
 * @author Keith Tran <keithtran25@gmail.com>
 * @author Syed Haider <shayder426@gmail.com>
 */
@Named
@SessionScoped
public class AddCourseBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger("AddCourseBean");
    @EJB
    private Tut4YouApp tut4youApp;
    private Course course;
    private Subject subject;
    private List<Subject> subjectList = new ArrayList();
    private List<Course> courseList = new ArrayList();
    private List<Course> tutorCourses = new ArrayList();
    
    /**
     * Creates an instance of the courseBean
     */
    public AddCourseBean() {
        course = new Course();
    }
    
    /**
     * Gets the subject
     * @return the subject
     */
    public Subject getSubject() {
        return subject;
    }
    
    /**
     * Sets the subject 
     * @param s the subject associated to course
     */
    public void setSubject(Subject s) {
        subject = s;
    }
    
    /**
     * Gets the course
     * @return the course
     */
    public Course getCourse() {
        return course;
    }
    
    /**
     * Sets the course
     * @param course the course is set
     */
    public void setCourse(Course course) {
        this.course = course;
    }
    
    /**
     * Gets a list of the subjects in the EJB
     * @return a list of subjects
     */
    public List<Subject> getSubjectList() {
        if (subjectList.isEmpty()) {
            subjectList = tut4youApp.getSubjects();
        }
        return subjectList;
    }
    
    /**
     * Sets the subjectList
     * @param s list of subjects
     */
    public void setSubjectList(List<Subject> s) {
        subjectList = s;
    }
    
    /**
     * Gets the courses of the tutor
     * @return list of courses of tutor
     */
    public List<Course> getTutorCourses() {
        courseList = tut4youApp.getTutorCourses();
        return courseList;
    }
    
    /**
     * Sets the courses of the tutor
     * @param tutorCourses
     */
    public void setTutorCourses(List<Course> tutorCourses) {
        this.tutorCourses = tutorCourses;
    }
    
    /**
     * Gets the list of courses
     * @return the list of courses
     */
    public List<Course> getCourseList() {
        return courseList;
    }
    
    /**
     * Sets the list of courses
     * @param courseList list of courses
     */
    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
    
     /**
     * Change the subject of the course
     */
    public void changeSubject() {
        courseList = tut4youApp.getCourses(subject.getSubjectName());
    }

    public String addCourse() throws CourseExistsException {
        String result = "failure";
        course = tut4youApp.addCourse(course);
        if (course != null) {
            result = "success";
        }
        return result;
    }
    
    /**
     * Adds course to the tutor's course list
     * @return success if the course was added successfully
     */
    public String addNewCourse() {
       String result = "failure";
       course.setSubject(subject);
       course = tut4youApp.addNewCourse(course);
        if (course != null) {
            result = "success";
        }
        return result;
   }
}