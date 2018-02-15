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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

/**
 * Request contains subject and course name, and a short description of a
 * specific topic. Each request is automatically assigned a unique identifier.
 * @author Keith Tran <keithtran25@gmail.com>
 * Modified by Amanda Pan <daikiraidemodaisuki@gmail.com>
 * Modified by Andrew Kaichi <ahkaichi@gmail.com>
 */
@Table(name="Request")
@NamedQueries({
    @NamedQuery(name = Request.FIND_REQUEST_BY_EMAIL, query = "SELECT r from Request r where r.student.email = :email")
})


@Entity
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final String FIND_REQUEST_BY_EMAIL = "Student.findRequestByEmail";
    
    /**
     * Primary key is generated uniquely
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    
    /**
     * Multiple requests can be submitted by a student
     */
    @ManyToOne
    private Student student;
    
    /**
     * Multiple requests can be submitted under the same course
     */
    @OneToOne
    @JoinColumn(name="courseName", nullable=false)
    private Course course;
    
    private String description;
    
    @Temporal(TemporalType.TIMESTAMP)
    private DateTime timeStamp;
    
    /**
     * Tells whether a Request is pending, accepted or cancelled
     */
    public enum Status{
        PENDING,
        ACCEPTED,
        CANCELLED;
    }
    private Status status;
    /**
     * Request Constructor
     */
    public Request() {
        
    }
    
    public Request(DateTime timeStamp, Student student, String description, Status status){
        this.timeStamp = timeStamp;
        this.student = student;
        this.description = description;
        this.status = status;
    }

    /**
     * Gets the id of a Request
     * @return id of the Request
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the id of a Request
     * @param id of the Request
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the timeStamp of a Request
     * @return timeStamp of the Request
     */
    public DateTime getTimeStamp(){
        return timeStamp;
    }
    
    /**
     * Sets the timeStamp of a Request
     * @param timeStamp of the Request
     */
    public void setTimeStamp(DateTime timeStamp){
        this.timeStamp = timeStamp;
    }
    
    /**
     * Gets the status of the Request
     * @return status of the Request
     */
    public Status getStatus(){
        return status;
    }
    
    /**
     * Sets the status of the Request
     * @param status of the Request
     */
    public void setStatus(Status status){
        this.status = status;
    }
    /**
     * Gets a course from the courseList
     * @return course from the list
     */
    public Course getCourse() {
        return course;
    }
    
    /**
     * Sets a course to a Request
     * @param course selected course from the list
     */
    public void setCourse(Course course) {
        this.course = course;
    }
    
    /**
     * Gets the student who logged in to Tut4You
     * @return logged in student
     */
    public Student getStudent() {
        return student;
    }
    
    /**
     * Sets the student who logged in to Tut4You
     * @param student who logged in
     */
    public void setStudent(Student student) {
        this.student = student;
    }
    
    /**
     * Gets a short description of a topic or problem by a student
     * @return a short description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets a short description of a topic or problem by a student
     * @param description of the problem
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Override hashCode
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    /**
     * Overrides the equals method
     * @param object 
     * @return true if object is Request, else false
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Request)) {
            return false;
        }
        Request other = (Request) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    /**
     * Override toString
     * @return Request attributes
     */
    @Override
    public String toString() {
        return "tut4you.model.Request[ id=" + id + " course=" + course + " description=" + description + " ]";
    }

}