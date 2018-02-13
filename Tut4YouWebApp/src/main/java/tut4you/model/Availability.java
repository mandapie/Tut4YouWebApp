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
import javax.persistence.Table;

/**
 * Availability encapsulates information of a time frame of when a Tutor is
 * available. Only a Tutor can add an availability.
 * @author Andrew Kaichi <ahkaichi@gmail.com>
 * @author Keith Tran <keithtran25@gmail.com>
 */
@Table(name="Availability")
@Entity
@NamedQueries ({
    @NamedQuery(name = Availability.FIND_AVAILABILITY_BY_TUTOR, query = "SELECT a FROM Availability a JOIN a.tutor s WHERE s.email = :email")
})
public class Availability implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * JPQL Query to get all availabilities of a tutor
     */
    public static final String FIND_AVAILABILITY_BY_TUTOR = "Availability.findAvailabilityByTutor";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    
    private String dayOfWeek;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String startPeriod;
    private String endPeriod;
    
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
     * @param dayOfWeek
     * @param startHour
     * @param startMinute
     * @param startPeriod
     * @param endHour
     * @param endMinute
     * @param endPeriod 
     */
    public Availability(String dayOfWeek, int startHour, int startMinute, String startPeriod, int endHour, int endMinute, String endPeriod) {
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.startPeriod = startPeriod;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.endPeriod = endPeriod;
    }
    
    /**
     * Gets the day of the week
     * @return dayOfWeek
     */
    public String getDayOfWeek(){
        return dayOfWeek;
    }
    
    /**
     * Sets the day of the week
     * @param dayOfWeek 
     */
    public void setDayOfWeek(String dayOfWeek){
        this.dayOfWeek = dayOfWeek;
    }
    
    /**
     * Gets the start Hour
     * @return startHour
     */
    public int getStartHour() {
        return startHour;
    }
    
    /**
     * Sets the start Hour
     * @param startHour 
     */
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }
    
    /**
     * Gets the start minute
     * @return startMinute
     */
    public int getStartMinute() {
        return startMinute;
    }
    
    /**
     * Sets the start minute
     * @param startMinute 
     */
    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }
    
    /**
     * Gets the start period
     * @return startPeriod
     */
    public String getStartPeriod(){
        return startPeriod;
    }
    
    /**
     * Sets the start period
     * @param startPeriod 
     */
    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }
    
    /**
     * Gets the end hour
     * @return endHour
     */
    public int getEndHour() {
        return endHour;
    }
    
    /**
     * Sets the end hour
     * @param endHour 
     */
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
    
    /**
     * Gets the end minute
     * @return endMinute
     */
    public int getEndMinute(){
        return endMinute;
    }
    
    /**
     * Sets the end minute
     * @param endMinute 
     */
    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }
    
    /**
     * Gets the end period
     * @return endPeriod
     */
    public String getEndPeriod(){
        return endPeriod;
    }
    
    /**
     * Sets the end period
     * @param endPeriod 
     */
    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }
    
    /**
     * Gets the primary key of the availability
     * @return id
     */
    public Long getId(){
        return id;
    }
    
    /**
     * Sets the primary key of the availability
     * @param id 
     */
    public void setId(Long id){
        this.id = id;
    }
    
    /**
     * Gets a tutor who is going to add an availability
     * @return tutor
     */
    public Tutor getTutor(){
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