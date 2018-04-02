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
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Tutor inherits all attributes of a User class with added attributes that
 * defines a user as a tutor. A tutor is also a student.
 * @author Keith Tran <keithtran25@gmail.com>
 * @author Syed Haider <shayder426@gmail.com>
 */
@Table(name="Tutor")
@DiscriminatorColumn(name="user_type", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue(value="Tutor")
@Entity
@NamedQueries({
    @NamedQuery(name = Tutor.FIND_TUTORS_BY_COURSE_DAY_TIME_DZIP, query = "SELECT t FROM Tutor t JOIN t.courses c JOIN t.availability a WHERE c.courseName = :coursename AND a.dayOfWeek = :dayofweek AND a.startTime <= :requestTime AND a.endTime >= :requestTime AND t.doNotDisturb = :doNotDisturb AND t.defaultZip = :zipCode AND t.currentZip IS NULL UNION ALL SELECT t FROM Tutor t JOIN t.courses c JOIN t.availability a WHERE c.courseName = :coursename AND a.dayOfWeek = :dayofweek AND a.startTime <= :requestTime AND a.endTime >= :requestTime AND t.doNotDisturb = :doNotDisturb AND t.currentZip = :zipCode"),
    @NamedQuery(name = Tutor.FIND_TUTORS_BY_COURSE_DAY_TIME_CZIP, query = "SELECT t FROM Tutor t JOIN t.courses c JOIN t.availability a WHERE c.courseName = :coursename AND a.dayOfWeek = :dayofweek AND a.startTime <= :requestTime AND a.endTime >= :requestTime AND t.doNotDisturb = :doNotDisturb AND t.currentZip = :zipCode"),
    //@NamedQuery(name = Tutor.FIND_TUTORS_BY_COURSE_DAY_TIME, query = "SELECT t FROM Tutor t JOIN t.courses c JOIN t.availability a JOIN t.location l WHERE c.courseName = :coursename AND a.dayOfWeek = :dayofweek AND a.startTime <= :requestTime AND a.endTime >= :requestTime AND t.doNotDisturb = :doNotDisturb AND l.defaultZip = :zipCode"),
    @NamedQuery(name = Tutor.FIND_TUTORS_BY_COURSE, query = "SELECT COUNT(t.email) FROM Tutor t JOIN t.courses c WHERE c.courseName = :coursename"),
})
public class Tutor extends User implements Serializable {     
    /**
     * JPQL Query to obtain a list of tutors who taught a specific course and is available using default zip code
     */
    public static final String FIND_TUTORS_BY_COURSE_DAY_TIME_DZIP = "Tutor.findTutorsByCourseDayTimeDZip";
    /**
     * JPQL Query to obtain a list of tutors who taught a specific course and is available using current zip code
     */
    public static final String FIND_TUTORS_BY_COURSE_DAY_TIME_CZIP = "Tutor.findTutorsByCourseDayTimeCZip";
    /**
     * JPQL Query to obtain a list of tutors who taught a specific course
     */
    public static final String FIND_TUTORS_BY_COURSE = "Tutor.findTutorsByCourse";

    @Temporal(TemporalType.DATE)
    private Date dateJoined;
    private int numPeopleTutored;
    private double priceRate;
    private boolean doNotDisturb;
    /**
     * Tutors default zip code location
     */
    private String defaultZip;
    /**
     * Tutors current zip code location
     */
    private String currentZip;
    /**
     * Tutors max radius
     */
    private int maxRadius;
    @ManyToOne
    //@JoinColumn(nullable=false)
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    /**
     * A Tutor can tutor multiple Courses and
     * a Course can be tutored by multiple Tutors.
     */
    @ManyToMany(mappedBy="tutors", cascade=CascadeType.ALL)
    private Collection<Course> courses;
    /**
     * A tutor can set multiple Availabilities
     */
    @OneToMany(mappedBy="tutor", cascade=CascadeType.ALL)
    private Collection<Availability> availability;
    /**
     * Many tutors can view many requests
     */
    @ManyToMany(mappedBy="availableTutors", cascade=CascadeType.ALL)
    private Collection<Request> pendingRequests;
        
    /**
     * Tutor constructor
     */
    public Tutor() {
        priceRate = 0.00;
        doNotDisturb = false;
        currentZip = null;
    }
    /**
     * Copy constructor
     * @param newTutor 
     */
    public Tutor(User newTutor) {
        super(newTutor);
    }
    /**
     * Tutor overloaded constructor with existing attributes
     * @param dateJoined
     * @param numPeopleTutored
     * @param priceRate 
     * @param doNotDisturb 
     * @param defaultZip 
     * @param currentZip 
     * @param maxRadius 
     */
    public Tutor(Date dateJoined, int numPeopleTutored, double priceRate, boolean doNotDisturb, String defaultZip, String currentZip, int maxRadius) {
        this.dateJoined = dateJoined;
        this.numPeopleTutored = numPeopleTutored;
        this.priceRate = priceRate;
        this.doNotDisturb = doNotDisturb;
        this.defaultZip = defaultZip;
        this.currentZip = currentZip;
        this.maxRadius = maxRadius;
    }
        
    /**
     * Tutor overloaded constructor with inherited and existing attributes
     * @param email
     * @param firstName
     * @param lastName
     * @param userName
     * @param phoneNumber
     * @param password
     * @param university
     * @param dateJoined
     * @param numPeopleTutored
     * @param priceRate 
     * @param doNotDisturb 
     * * @param zipCode 
     * @param defaultZip 
     * @param currentZip 
     * @param maxRadius 
     */
    public Tutor(String email, String firstName, String lastName, String userName, String phoneNumber, String password, String university, Date dateJoined, int numPeopleTutored, double priceRate, boolean doNotDisturb, String defaultZip, String currentZip, int maxRadius) {
        super(email, firstName, lastName, userName, phoneNumber, password, university);
        this.dateJoined = dateJoined;
        this.numPeopleTutored = numPeopleTutored;
        this.priceRate = priceRate;
        this.doNotDisturb = doNotDisturb;
        this.defaultZip = defaultZip;
        this.currentZip = currentZip;
        this.maxRadius = maxRadius;
    }
    
    /**
     * Gets the state of doNotDistrub
     * @return doNotDisturb
     */
    public boolean isDoNotDisturb() {
        return doNotDisturb;
    }
    
    /**
     * Sets the state of doNotDistrub
     * @param doNotDisturb 
     */
    public void setDoNotDisturb(boolean doNotDisturb) {
        this.doNotDisturb = doNotDisturb;
    }
    
    /**
     * Gets the list of pending requests
     * @return list of pendingRequests
     */
    public Collection<Request> getPendingRequests() {
        return pendingRequests;
    }
    
    /**
     * Sets the list of pending requests
     * @param pendingRequests 
     */
    public void setPendingRequests(Collection<Request> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }
    
    /**
     * Gets a collection of a Tutor's availabilities
     * @return a collection of availabilities
     */
    public Collection<Availability> getAvailability() {
        return availability;
    }

    /**
     * Sets a collection of a Tutor's availabilities
     * @param availability the availability of a tutor
     */
    public void setAvailability(Collection<Availability> availability) {
        this.availability = availability;
    }
    
    /**
     * Adds an availability to a collection
     * if availability is null, create new HashSet
     * @param availability 
     */
    public void addAvailability(Availability availability) {
        if (this.availability == null)
            this.availability = new HashSet();
        this.availability.add(availability);
    }
    
    /**
     * Sets the date joined by a tutor
     * @param dateJoined 
     */
    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }
    
    /**
     * Gets the date joined by a tutor
     * @return the date
     */
    public Date getDateJoined() {
        return dateJoined;
    }
    
    /**
     * Sets the number of student taught by a tutor
     * @param numPeopleTutored 
     */
    public void setNumTutored(int numPeopleTutored) {
        this.numPeopleTutored = numPeopleTutored;
    }
    
    /**
     * Gets the number of student taught by a tutor
     * @return the number of student taught
     */
    public int getNumTutored() {
        return numPeopleTutored;
    }
    
    /**
     * Sets the price rate a tutor wants to be compensated per hour
     * @param priceRate 
     */
    public void setPriceRate(double priceRate) {
        this.priceRate = priceRate;
    }
    
    /**
     * Gets the price rate a tutor wants to be compensated per hour
     * @return the price rate
     */
    public double getPriceRate() {
        return priceRate;
    }

    /**
     * Gets the collection of courses a tutor can teach
     * @return the list of courses
     */
    public Collection<Course> getCourses() {
        return courses;
    }
    
    /**
     * Sets the collection of courses a tutor can teach
     * @param courses the list of courses
     */
    public void setCourse(Collection<Course> courses) {
        this.courses = courses;
    }
    
    /**
     * Add a course to a collection of Courses
     * if collection of courses is null, create new HashSet
     * @param course 
     */
    public void addCourse(Course course) {
        if (this.courses == null)
            this.courses = new HashSet();
        this.courses.add(course);
    }
    /**
     * get Default Zip
     * @return default zip
     */
    public String getDefaultZip() {
        return defaultZip;
    }
    /**
     * set default zip
     * @param defaultZip 
     */
    public void setDefaultZip(String defaultZip) {
        this.defaultZip = defaultZip;
    }
    /**
     * get current zip
     * @return currentZip
     */
    public String getCurrentZip() {
        return currentZip;
    }
    /**
     * set current zip
     * @param currentZip 
     */
    public void setCurrentZip(String currentZip) {
        this.currentZip = currentZip;
    }
    /**
     * get max radius
     * @return maxRadius
     */
    public int getMaxRadius() {
        return maxRadius;
    }
    /**
     * set max radius
     * @param maxRadius 
     */
    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }
    /**
     * Adds a pending request to the list
     * @param pr 
     */
    public void addPendingRequest(Request pr) {
        if (this.pendingRequests == null)
            this.pendingRequests = new HashSet();
        this.pendingRequests.add(pr);
    }
    
    /**
     * removes a pending request from the list
     * @param pr 
     */
    public void removePendingRequest(Request pr) {
        pendingRequests.remove(pr);
    }
}