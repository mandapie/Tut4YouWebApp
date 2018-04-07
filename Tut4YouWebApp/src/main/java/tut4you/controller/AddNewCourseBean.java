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
import javax.enterprise.context.RequestScoped;
import tut4you.model.*;

/**
 * Adds a new course to the database and tutor.
 * @author Amanda
 */
@Named
@RequestScoped
public class AddNewCourseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("AddNewCourseBean");

    @EJB
    private Tut4YouApp tut4youApp;

    private Subject subject;
    private Course course;
    private List<Subject> subjects = new ArrayList();
    private List<Course> tutorCourses = new ArrayList();

    /**
     * Creates a new instance of AddNewCourseBean
     */
    public AddNewCourseBean() {
        course = new Course();
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Subject> getSubjects() {
        if (subjects.isEmpty()) {
            subjects = tut4youApp.getSubjects();
        }
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Course> getTutorCourses() {
        return tutorCourses;
    }

    public void setTutorCourses(List<Course> tutorCourses) {
        this.tutorCourses = tutorCourses;
    }

    /**
     * Adds course to the tutor's course list
     *
     * @return success if the course was added successfully
     */
    public String addNewCourse() {
        String result = "failure";
        this.course.setSubject(subject);
        this.course = tut4youApp.addNewCourse(course);
        if (this.course != null) {
            result = "success";
        }
        return result;
    }
}
