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
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import tut4you.exception.CourseExistsException;
import tut4you.model.*;
import tut4you.model.Tut4YouApp;

/**
 *
 * @author Keith
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

    public AddCourseBean() {
        course = new Course();
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

    public List<Course> getTutorCourses() {
        courseList = tut4youApp.getTutorCourses();
        return courseList;
    }

    public void setTutorCourses(List<Course> tutorCourses) {
        this.tutorCourses = tutorCourses;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public void changeSubject() {
        courseList = tut4youApp.getCourses(subject.getSubjectName());
        LOGGER.severe("Retrieved list of courses from EJB");
    }

    public String addCourse() throws CourseExistsException {
        String result = "failure";
        course = tut4youApp.addCourse(course);
        if (course != null) {
            result = "success";
            LOGGER.severe("added a course");
        }
        return result;
    }
    
    public String addNewCourse() {
       String result = "failure";
       course.setSubject(subject);
       course = tut4youApp.addNewCourse(course);
        if (course != null) {
            result = "success";
            LOGGER.severe("added a course");
        }
        return result;
   }
}
