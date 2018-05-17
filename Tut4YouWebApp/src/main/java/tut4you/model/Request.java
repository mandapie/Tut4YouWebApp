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
import java.util.Date;
import java.util.HashSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Request contains subject and course name, and a short description of a
 * specific topic. Each request is automatically assigned a unique identifier.
 *
 * @author Keith Tran <keithtran25@gmail.com>
 * Modified by Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Table(name = "Request")
@NamedQueries({
    @NamedQuery(name = Request.FIND_REQUESTS_BY_USER, query = "SELECT r FROM Request r JOIN r.student s WHERE s.email = :email ORDER BY CASE r.dayOfWeek WHEN 'Monday' THEN 1 WHEN 'Tuesday' THEN 2 WHEN 'Wednesday' THEN 3 WHEN 'Thursday' THEN 4 WHEN 'Friday' THEN 5 WHEN 'Saturday' THEN 6 ELSE 7 END, r.sessionTime ASC"),
    @NamedQuery(name = Request.FIND_REQUEST_BY_EMAIL, query = "SELECT r from Request r JOIN r.student s WHERE s.email = :student_email AND r.status = :status ORDER BY CASE r.dayOfWeek WHEN 'Monday' THEN 1 WHEN 'Tuesday' THEN 2 WHEN 'Wednesday' THEN 3 WHEN 'Thursday' THEN 4 WHEN 'Friday' THEN 5 WHEN 'Saturday' THEN 6 ELSE 7 END, r.sessionTime ASC"),
    @NamedQuery(name = Request.FIND_REQUEST_BY_TUTOR_EMAIL, query = "SELECT r from Request r JOIN r.tutor s WHERE s.email = :tutor_email AND r.status = :status ORDER BY CASE r.dayOfWeek WHEN 'Monday' THEN 1 WHEN 'Tuesday' THEN 2 WHEN 'Wednesday' THEN 3 WHEN 'Thursday' THEN 4 WHEN 'Friday' THEN 5 WHEN 'Saturday' THEN 6 ELSE 7 END, r.sessionTime ASC"),
    @NamedQuery(name = Request.FIND_REQUESTS_BY_TUTOR, query = "SELECT r FROM Request r JOIN r.availableTutors t WHERE t.email = :email ORDER BY CASE r.dayOfWeek WHEN 'Monday' THEN 1 WHEN 'Tuesday' THEN 2 WHEN 'Wednesday' THEN 3 WHEN 'Thursday' THEN 4 WHEN 'Friday' THEN 5 WHEN 'Saturday' THEN 6 ELSE 7 END, r.sessionTime ASC"),
    @NamedQuery(name = Request.FIND_REQUEST_BY_ID, query = "SELECT r FROM Request r WHERE r.id = :id")
})
@Entity
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Tells whether a Request is pending, accepted or canceled
     * http://tomee.apache.org/examples-trunk/jpa-enumerated/README.html
     */
    public enum Status {
        PENDING, //0
        ACCEPTED, //1
        CANCELLED, //2
        DECLINED, //3
        COMPLETED; //4
    }

    /**
     * JPQL Query to find Requests by user email
     */
    public static final String FIND_REQUEST_BY_EMAIL = "Request.findRequestByEmail";
    /**
     * JPQL Query to find requests from available/selected tutors
     */
    public static final String FIND_REQUESTS_BY_TUTOR = "Request.findRequestsByTutor";
    /**
     * JPQL Query to find requests from available/selected tutors
     */
    public static final String FIND_REQUEST_BY_TUTOR_EMAIL = "Request.findRequestsByTutorEmail";
    /**
     * JPQL Query to find requests made by a user
     */
    public static final String FIND_REQUESTS_BY_USER = "Request.findRequestsByUser";
    /**
     * JPQL Query to find request by ID
     */
    public static final String FIND_REQUEST_BY_ID = "Request.findRequestsByID";

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
    private User student;
    /**
     * ManyToOne relationship between request and zipCode
     */
    @ManyToOne
    private ZipCode zipCode;
    /**
     * only one course can be associated with one request
     */
    @OneToOne
    @JoinColumn(nullable = false)
    private Course course;
    /**
     * Association class between request and tutor. One tutor can receive many
     * pending requests and One request can be sent to many tutors.
     */
    @ManyToMany
    @JoinTable(name = "Requests_tutors",
            joinColumns = {
                @JoinColumn(name = "id")
            },
            inverseJoinColumns = @JoinColumn(name = "email"))
    private Collection<Tutor> availableTutors;
    @OneToOne
    private Tutor tutor;
    @OneToOne
    private Session session;
    private String description;
    private String dayOfWeek;
    @Temporal(TemporalType.TIME)
    private Date sessionTime;
    private Status status;
    private float lengthOfSession;

    /**
     * Request Constructor
     */
    public Request() {

    }

    /**
     * request overloaded constructor
     *
     * @param student
     * @param description
     * @param status
     * @param currentTime
     * @param lengthOfSession
     */
    public Request(User student, String description, Status status, java.util.Date currentTime, int lengthOfSession) {
        this.student = student;
        this.description = description;
        this.status = status;
        this.sessionTime = currentTime;
        this.lengthOfSession = lengthOfSession;
    }
    /**
     * get session
     * @return 
     */
    public Session getSession() {
        return session;
    }
    /**
     * set session
     * @param session 
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * get ZipCode of Request
     *
     * @return zipCode
     */
    public ZipCode getZipCode() {
        return zipCode;
    }

    /**
     * set zipCode of Request
     *
     * @param zipCode
     */
    public void setZipCode(ZipCode zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Gets the id of a Request
     *
     * @return id of the Request
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of a Request
     *
     * @param id of the Request
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * get day of week
     *
     * @return dayOfWeek
     */
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * set day of week
     *
     * @param dayOfWeek
     */
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * get sessionTime
     *
     * @return currenTime
     */
    public Date getSessionTime() {
        return sessionTime;
    }

    /**
     * set sessionTime
     *
     * @param sessionTime
     */
    public void setSessionTime(Date sessionTime) {
        this.sessionTime = sessionTime;

    }

    /**
     * Gets the status of the Request
     *
     * @return status of the Request
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the Request
     *
     * @param status of the Request
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Gets a course from the courseList
     *
     * @return course from the list
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets a course to a Request
     *
     * @param course selected course from the list
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Gets a tutor
     *
     * @return tutor
     */
    public Tutor getTutor() {
        return tutor;
    }

    /**
     * Sets a tutor
     *
     * @param tutor
     */
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    /**
     * Gets the student who logged in to Tut4You
     *
     * @return logged in student
     */
    public User getStudent() {
        return student;
    }

    /**
     * Sets the student who logged in to Tut4You
     *
     * @param student who logged in
     */
    public void setStudent(User student) {
        this.student = student;
    }

    /**
     * Gets a short description of a topic or problem by a student
     *
     * @return a short description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a short description of a topic or problem by a student
     *
     * @param description of the problem
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * gets the length of the tutoring session
     *
     * @return lengthOfSession
     */
    public float getLengthOfSession() {
        return lengthOfSession;
    }

    /**
     * sets the length of the tutoring session
     *
     * @param lengthOfSession
     */
    public void setLengthOfSession(float lengthOfSession) {
        this.lengthOfSession = lengthOfSession;
    }

    /**
     * Gets a collection of available tutors
     *
     * @return availableTutors
     */
    public Collection<Tutor> getAvailableTutors() {
        return availableTutors;
    }

    /**
     * Sets a collection of available tutors
     *
     * @param availableTutors
     */
    public void setAvailableTutors(Collection<Tutor> availableTutors) {
        this.availableTutors = availableTutors;
    }

    /**
     * Adds an available tutor to the collection of available tutors
     *
     * @param at
     */
    public void addAvailableTutor(Tutor at) {
        if (this.availableTutors == null) {
            this.availableTutors = new HashSet();
        }
        this.availableTutors.add(at);
    }

    /**
     * removes an available tutor to the collection of available tutors
     *
     * @param at
     */
    public void removeAvailableTutor(Tutor at) {
        availableTutors.remove(at);
    }
    
    public void removeAllAvailableTutor(Collection<Tutor> ats) {
        availableTutors.removeAll(ats);
    }

    /**
     * Override hashCode
     *
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
     *
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
     *
     * @return Request attributes
     */
    @Override
    public String toString() {
        return "tut4you.model.Request[ id=" + id + " course=" + course + " description=" + description + " dayOfWeek=" + dayOfWeek + " currentTime= " + sessionTime + " ]";
    }
}