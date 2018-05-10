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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * Each tutor can receive ratings after tutoring sessions
 * @author Syed Haider <shayder426@gmail.com>
 */

@NamedQueries({
    @NamedQuery(name = Rating.FIND_RATING_BY_EMAIL, query = "SELECT r from Rating r"),
    @NamedQuery(name = Rating.FIND_RATING_BY_TUTOR, query = "SELECT r FROM Rating r JOIN r.tutor s WHERE s.email = :email"),
    @NamedQuery(name = Rating.FIND_AVG_RATING_BY_TUTOR, query = "SELECT AVG(r.ratingValue) FROM Rating r JOIN r.tutor s WHERE s.email = :email"),
    @NamedQuery(name = Rating.FIND_RATING_BY_STUDENT, query = "SELECT r FROM Rating r JOIN r.student s WHERE s.email = :email"),
    @NamedQuery(name = Request.FIND_REQUEST_BY_EMAIL, query = "SELECT r from Request r JOIN r.student s WHERE s.email = :student_email AND r.status = :status")})
@Entity
@Table(name="Rating")
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * JPQL Query to find Ratings by user email
     */
    public static final String FIND_RATING_BY_EMAIL = "Rating.findRatingByEmail";
    /**
     * JPQL Query to find ratings from available/selected tutors
     */
    public static final String FIND_RATING_BY_TUTOR = "Rating.findRatingByTutor";
    /**
     * JPQL Query to find ratings from available/selected tutors
     */
    public static final String FIND_RATING_BY_STUDENT = "Rating.findRatingByStudent";
    /**
     * JPQL Query to find average rating from available/selected tutors
     */
    public static final String FIND_AVG_RATING_BY_TUTOR = "Rating.findAvgRatingByTutor";

    /**
     * Primary key is generated uniquely
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRated;
    private int ratingValue;
    /**
     * Multiple ratings can be submitted by a student
     */
    @ManyToOne
    private User student;
    /**
     * Multiple ratings can be added/viewed by a Tutor
     */
    @ManyToOne
    private Tutor tutor;

    /**
     * Rating Constructor
     */
    public Rating() {

    }

    /**
     * Rating overloaded constructor
     *
     * @param ratingValue rating from 1 - 5 stars
     * @param description description of the tutoring session
     * @param currentTime date the rating was submitted
     */
    public Rating(int ratingValue, String description, Date currentTime) {
        this.ratingValue = ratingValue;
        this.description = description;
        this.dateRated = currentTime;
    }

    /**
     * Gets the value of the rating
     *
     * @return ratingValue (rating value of the tutor)
     */
    public int getRatingValue() {
        return ratingValue;
    }

    /**
     * Sets the value of the rating for the tutor
     *
     * @param ratingValue rating value of the tutor
     */
    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    /**
     * Gets the id of a Rating
     *
     * @return id of the Rating
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of a Rating
     *
     * @param id of the Rating
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the current date 
     *
     * @return dateRated the date the rating was done
     */
    public Date getDateRated() {
        return dateRated;
    }

    /**
     * set dateRated
     *
     * @param dateRated the date the rating was done
     */
    public void setDateRated(Date dateRated) {
        this.dateRated = dateRated;

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
     * Gets a short description of the rating by a student
     *
     * @return a short description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a short description of the rating by a student
     *
     * @param description of the problem
     */
    public void setDescription(String description) {
        this.description = description;
    }

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
     * @return true if object is Rating, else false
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rating)) {
            return false;
        }
        Rating other = (Rating) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Override toString
     *
     * @return Rating attributes
     */
    @Override
    public String toString() {
        return "tut4you.model.Rating[ id=" + id + " description=" + description + " currentTime= " + dateRated + " ]";
    }

}