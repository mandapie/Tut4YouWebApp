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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Table;


/**
 * Availability encapsulates information of a time frame of when a Tutor is
 * available. Only a Tutor can add an availability.
 *
 * @author Andrew Kaichi <ahkaichi@gmail.com>
 * @author Keith Tran <keithtran25@gmail.com>
 * @author Syed Haider <shayder426@gmail.com>
 */
@Table(name="Availability")
@Entity
@NamedQueries({
    @NamedQuery(name = Availability.FIND_AVAILABILITY_BY_TUTOR, query = "SELECT a FROM Availability a JOIN a.tutor s WHERE s.email = :email"),
    
})
public class Availability implements Serializable {
    /**
     * JPQL Query to get all availabilities of a tutor
     */
    public static final String FIND_AVAILABILITY_BY_TUTOR = "Availability.findAvailabilityByTutor";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    private String dayOfWeek;
    @Temporal(TemporalType.TIME)
    private java.util.Date startTime;
    @Temporal(TemporalType.TIME)
    private java.util.Date endTime;
    
    /**
     * Multiple availabilities can be added by a Tutor
     */
    @ManyToOne
    private Tutor tutor;

    /**
     * Availability constructor
     */
    public Availability() {

    }

    /**
     * Availability overloaded constructor
     *
     * @param dayOfWeek
     * @param startTime
     * @param endTime
     */
    public Availability(String dayOfWeek, java.util.Date startTime, java.util.Date endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    /**
     * Gets the Id of availability
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the primary key of the availability
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the day of the week
     * @return dayOfWeek
     */
    public String getDayOfWeek() {
        return dayOfWeek;    }

    /**
     * Sets the day of the week
     * @param dayOfWeek
     */
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Gets the start time
     * @return startTime
     */
    public java.util.Date getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time
     * @param startTime
     */
    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time
     * @return endTime
     */
    public java.util.Date getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time
     * @param endTime
     */
    public void setEndTime(java.util.Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets a tutor who is going to add an availability
     * @return tutor
     */
    public Tutor getTutor() {
        return tutor;
    }

    /**
     * Sets a tutor who is going to add an availability
     * @param tutor
     */
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
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
        if (!(object instanceof Availability)) {
            return false;
        }
        Availability other = (Availability) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tut4you.entities.Availability[ id=" + id + " ]";
    }
}