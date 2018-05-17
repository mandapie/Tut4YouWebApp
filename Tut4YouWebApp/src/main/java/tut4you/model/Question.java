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
package tut4you.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * A question the question a user asks when they post to the forum
 * @author Andrew Kaichi <ahkaichi@gmail.com>
 */
@Table(name = "Question")
@Entity
@NamedQueries({
    @NamedQuery(name = Question.FIND_QUESTION_BY_COURSE, query = "SELECT q FROM Question q JOIN q.course c WHERE c.courseName = :name"),
    @NamedQuery(name = Question.FIND_QUESTION_BY_TITLE, query = "SELECT q FROM Question q WHERE q.title = :title")
})
public class Question implements Serializable {
    /**
     * JPQL Query to find questions by their course name
     */
    public static final String FIND_QUESTION_BY_COURSE = "Question.findQuestionByCourse";
    /**
     * JPQL Query to find questions by their title
     */
    public static final String FIND_QUESTION_BY_TITLE = "Question.findQuestionByTitle";

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    /**
     * Many to one relationship with student.
     * Many questions can be asked by one student
     */
    @ManyToOne
    @JoinColumn(name = "student_email", nullable = false)
    private User student;
    /**
     * One to many relationship with response.
     * One question can have many responses
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private Collection<Responses> responses;
    /**
     * One to one relationship with course.
     * One question only has one course
     */
    @OneToOne
    @JoinColumn(name = "courseName", nullable = false)
    private Course course;

    /**
     * default question constructor
     */
    public Question() {
    
    }
    /**
     * question constructor
     * @param student student who asks the question
     * @param course course the question is for
     * @param title name of the question
     * @param description description of the question
     * @param responses collection of responses
     */
    public Question(User student, Course course, String title, String description, Collection<Responses> responses) {
        this.student = student;
        this.course = course;
        this.title = title;
        this.description = description;
        this.responses = responses;
    }
    /**
     * gets the id
     * @return id
     */
    public Long getId() {
        return id;
    }
    /**
     * sets the id
     * @param id to be set
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * gets the title
     * @return title
     */
    public String getTitle() {
        return title;
    }
    /**
     * sets the title
     * @param title title to be set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * gets the description
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * sets the description
     * @param description to be set
     */
    public void setDescription(String description){
        this.description = description;
    }
    /**
     * gets a collection of responses
     * @return responses
     */
    public Collection<Responses> getResponses(){
        return responses;
    }
    /**
     * sets a collection of responses
     * @param responses to be set
     */
    public void setResponse(Collection<Responses> responses){
        this.responses = responses;
    }
    /**
     * gets a course
     * @return course
     */
    public Course getCourse(){
        return course;
    }
    /**
     * sets a course
     * @param course to be set
     */
    public void setCourse(Course course){
        this.course = course;
    }
    /**
     * gets a student
     * @return student
     */
    public User getStudent(){
        return student;
    }
    /**
     * sets a student
     * @param student to be set
     */
    public void setStudent(User student){
        this.student = student;
    }
    /**
     * adds a course
     * @param course name of the course to be added
     */
    public void addCourse(String course){
        this.course.setCourseName(course);
    }
    
     /**
     * adds a response to a collection of responses
     * @param responses 
     */
    public void addResponses(Responses responses) {
        if (this.responses == null) {
            this.responses = new HashSet();
        }
        this.responses.add(responses);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tut4you.model.Question[ id=" + id + " ]";
    }
    
}
