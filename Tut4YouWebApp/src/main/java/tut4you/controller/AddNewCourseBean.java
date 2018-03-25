/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import tut4you.exception.CourseExistsException;
import tut4you.exception.UserExistsException;
import tut4you.model.*;

/**
 *
 * @author Amanda
 */
@Named
@RequestScoped
public class AddNewCourseBean implements Serializable {
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
     * @return success if the course was added successfully
     */
    public String addNewCourse() {
        String result = "failure";
        try {
            this.course.setSubject(subject);
            this.course = tut4youApp.addNewCourse(course);
            if (this.course != null) {
                result = "success";
            }
        }
        catch (CourseExistsException see) {
            FacesContext.getCurrentInstance().addMessage("addNewCourseForm:cn", new FacesMessage("This course already exists."));
            result = "addNewCourse";
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            result = "failure";
        }
        return result;
   }
}
