
package tut4you.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import tut4you.model.*;

/**
 *
 * @author Andrew
 */
@Named
@SessionScoped
public class ForumBean implements Serializable{
    private Forum forum;
    private Question question;
    private Response response;
    private Course course;
    private Subject subject;
    private List<Subject> subjectList = new ArrayList(); //list of subjects to be loaded to the request form
    private List<Course> courseList = new ArrayList(); //list of courses based on subject to load to the request form

    public ForumBean() {
    
    }
    
    /**
     * 
     * @return 
     */
    public Forum getForum(){
        return forum;
    }
    
    /**
     * 
     * @param forum 
     */
    public void setForum(Forum forum) {
        this.forum = forum;
    }
        /**
     * Gets the subject of the request
     *
     * @return subject of the request
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the request
     *
     * @param s the subject of the request
     */
    public void setSubject(Subject s) {
        subject = s;
    }

    /**
     * Gets the course of the request
     *
     * @return course of the request
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course of the request
     *
     * @param course the course of the request
     */
    public void setCourse(Course course) {
        this.course = course;
    }
}
