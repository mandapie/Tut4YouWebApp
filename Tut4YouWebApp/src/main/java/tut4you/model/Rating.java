/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.util.HashSet;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import org.primefaces.event.RateEvent;

@NamedQueries({
    @NamedQuery(name = Rating.FIND_RATING_BY_EMAIL, query = "SELECT r from Rating r")
    ,
    @NamedQuery(name = Rating.FIND_RATING_BY_TUTOR, query = "SELECT r FROM Rating r JOIN r.tutor s WHERE s.email = :email")
    ,
     @NamedQuery(name = Rating.FIND_AVG_RATING_BY_TUTOR, query = "SELECT AVG(r.ratingValue) FROM Rating r")
    ,
    @NamedQuery(name = Request.FIND_REQUEST_BY_EMAIL, query = "SELECT r from Request r JOIN r.student s WHERE s.email = :student_email AND r.status = :status")})
@Entity
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
    /**
     * JPQL Query to find Ratings by user email
     */
    public static final String FIND_RATING_BY_EMAIL = "Rating.findRatingByEmail";
    /**
     * JPQL Query to find ratings from available/selected tutors
     */
    public static final String FIND_RATING_BY_TUTOR = "Rating.findRatingByTutor";
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

    /**
     * Multiple ratings can be submitted by a student
     */
    @ManyToOne
    private User student;

    /**
     * Association class between rating and tutor. One tutor can receive many
     * ratings and one rating can be received by a tutor.
     *
     * @ManyToOne
     * @JoinTable(name = "Ratings_tutors", joinColumns = {
     * @JoinColumn(name = "id")}, inverseJoinColumns = @JoinColumn(name =
     * "email")) private Collection<Tutor> tutorsList;
     */
    /**
     * Multiple availabilities can be added by a Tutor
     */
    @ManyToOne
    @JoinColumn(name = "tutorName")
    private Tutor tutor;

    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date currentTime;

    private int ratingValue;

    /* private Integer rating1;
    private Integer rating2;
    private Integer rating3;
    private Integer rating4 = 3;
    private Integer ratingValue;

    /**
     * Rating Constructor
     */
    public Rating() {

    }

    /**
     * Rating overloaded constructor
     *
     * @param ratingValue
     * @param description
     * @param currentTime
     */
    public Rating(int ratingValue, String description, Date currentTime) {
        this.ratingValue = ratingValue;
        this.description = description;
        this.currentTime = currentTime;
    }

    public void onrate(RateEvent rateEvent) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rate Event", "You rated:" + ((Integer) rateEvent.getRating()).intValue());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void oncancel() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancel Event", "Rate Reset");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public int getRatingValue() {
        return ratingValue;
    }

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

    public java.util.Date getCurrentTime() {
        return currentTime;
    }

    /**
     * set currentTime
     *
     * @param currentTime
     */
    public void setCurrentTime(java.util.Date currentTime) {
        this.currentTime = currentTime;

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
        return "tut4you.model.Rating[ id=" + id + " description=" + description + " currentTime= " + currentTime + " ]";
    }

}