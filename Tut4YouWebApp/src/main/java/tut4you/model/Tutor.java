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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Tutor inherits all attributes of a User class with added attributes that
 * defines a user as a tutor. A tutor is also a student.
 *
 * @author Keith Tran <keithtran25@gmail.com>
 * @author Syed Haider <shayder426@gmail.com>
 */
@Table(name = "Tutor")
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "Tutor")
@Entity
@NamedQueries({
    @NamedQuery(name = Tutor.FIND_HOURLY_RATE_BY_EMAIL, query = "SELECT t.hourlyRate FROM Tutor t WHERE t.email = :email"),
    @NamedQuery(name = Tutor.FIND_TRANSCRIPT_PATH_BY_EMAIL, query = "SELECT t.transcriptFilePath from Tutor t WHERE t.email = :email"),
    @NamedQuery(name = Tutor.FIND_DATE_JOINED_BY_EMAIL, query = "SELECT t.dateJoinedAsTutor FROM Tutor t WHERE t.email = :email"),
    @NamedQuery(name = Tutor.FIND_TUTORS_BY_COURSE_DAY_TIME_DZIP, query = "SELECT t FROM Tutor t JOIN t.courses c JOIN t.availabilities a WHERE c.courseName = :coursename AND a.dayOfWeek = :dayofweek AND a.startTime <= :requestTime AND a.endTime >= :requestTime AND t.doNotDisturb = :doNotDisturb AND t.defaultZip = :zipCode AND t.currentZip IS NULL"),
    @NamedQuery(name = Tutor.FIND_TUTORS_BY_COURSE_DAY_TIME_CZIP, query = "SELECT t FROM Tutor t JOIN t.courses c JOIN t.availabilities a WHERE c.courseName = :coursename AND a.dayOfWeek = :dayofweek AND a.startTime <= :requestTime AND a.endTime >= :requestTime AND t.doNotDisturb = :doNotDisturb AND t.currentZip = :zipCode"),
    @NamedQuery(name = Tutor.FIND_TUTORS_BY_COURSE, query = "SELECT COUNT(t) FROM Tutor t JOIN t.courses c WHERE c.courseName = :coursename"),
    @NamedQuery(name = Tutor.FIND_TUTORS, query = "SELECT t FROM Tutor t"),
    @NamedQuery(name = Tutor.FIND_TUTOR_BY_USERNAME, query = "SELECT t FROM Tutor t WHERE t.username = :username"),
    @NamedQuery(name = Tutor.FIND_LOW_RATING_TUTORS, query = "SELECT t FROM Tutor t WHERE t.overallRating <= :overallRating"),
    @NamedQuery(name = Tutor.VERIFY_ZIPCODE, query = "SELECT t FROM Tutor t WHERE t.defaultZip = :zipcode")
})
public class Tutor extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * JPQL Query to obtain a list of tutors who taught a specific course and is
     * available using default zip code
     */
    public static final String FIND_TUTORS_BY_COURSE_DAY_TIME_DZIP = "Tutor.findTutorsByCourseDayTimeDZip";
    public static final String FIND_TUTORS_BY_COURSE_DAY_TIME_CZIP = "Tutor.findTutorsByCourseDayTimeCZip";
    public static final String FIND_HOURLY_RATE_BY_EMAIL = "Tutor.findHourlyRateByEmail";
    public static final String FIND_DATE_JOINED_BY_EMAIL = "Tutor.findDateJoinedByEmail";
    public static final String FIND_LOW_RATING_TUTORS = "Tutor.findLowRatingTutors";
    public static final String FIND_TRANSCRIPT_PATH_BY_EMAIL = "Tutor.findTranscriptPathByEmail";
    /**
     * JPQL Query to see if zipcode is in database
     */

    public static final String VERIFY_ZIPCODE = "Tutor.verifyZipcode";
    /**
     * JPQL Query to obtain a list of tutors who taught a specific course and is
     * available
     */
    public static final String FIND_TUTORS_BY_COURSE_DAY_TIME = "Tutor.findTutorsByCourseDayTime";
    /**
     * JPQL Query to obtain a list of tutors who taught a specific course
     */
    public static final String FIND_TUTORS_BY_COURSE = "Tutor.findTutorsByCourse";

    /**
     * JPQL Query to obtain a list of tutors
     */
    public static final String FIND_TUTORS = "Tutor.findTutors";

    /**
     * JPQL Query to obtain a list of tutors by username
     */
    public static final String FIND_TUTOR_BY_USERNAME = "Tutor.findTutorByUsername";

    @Temporal(TemporalType.DATE)
    private Date dateJoinedAsTutor;
    private int numOfPeopleTutored;
    private double hourlyRate;
    private boolean doNotDisturb;
    private String transcriptFilePath;
    private int overallRating;
    private String defaultZip;
    private String currentZip;
    
    /**
     * A Tutor can tutor multiple Courses and a Course can be tutored by
     * multiple Tutors.
     */
    @ManyToMany(mappedBy = "tutors", cascade = CascadeType.ALL)
    private Collection<Course> courses;
    /**
     * A tutor can set multiple Availabilities
     */

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private Collection<Availability> availabilities;

    /**
     * A tutor can receive multiple payments
     */
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private Collection<Payment> payments;

    /**
     * Many tutors can view many requests
     */
    @ManyToMany(mappedBy = "availableTutors", cascade = CascadeType.ALL)
    private Collection<Request> pendingRequests;

    /**
     * One tutor can view many ratings
     */
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private Collection<Rating> ratings;
    /**
     * One tutor can post many responses
     */
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private Collection<Responses> responses;
    /**
     * Tutor constructor
     */
    public Tutor() {
        hourlyRate = 0.00;
        overallRating = 0;
        doNotDisturb = false;
        currentZip = null;
        numOfPeopleTutored = 0;
    }

    /**
     * Copy constructor
     *
     * @param newTutor
     */
    public Tutor(User newTutor) {
        super(newTutor);
    }

    /**
     * Tutor overloaded constructor with existing attributes
     *
     * @param dateJoined
     * @param numPeopleTutored
     * @param priceRate
     * @param doNotDisturb
     * @param transcriptFileLocation
     * @param overallRating
     * @param defaultZip
     * @param currentZip
     */
    public Tutor(Date dateJoined, int numPeopleTutored, double priceRate, boolean doNotDisturb, String transcriptFileLocation, int overallRating, String defaultZip, String currentZip) {
        this.dateJoinedAsTutor = dateJoined;
        this.numOfPeopleTutored = numPeopleTutored;
        this.hourlyRate = priceRate;
        this.doNotDisturb = doNotDisturb;
        this.transcriptFilePath = transcriptFileLocation;
        this.overallRating = overallRating;
        this.defaultZip = defaultZip;
        this.currentZip = currentZip;
    }

    /**
     * Tutor overloaded constructor with inherited and existing attributes
     *
     * @param email
     * @param firstName
     * @param lastName
     * @param userName
     * @param phoneNumber
     * @param password
     * @param university
     * @param securityQuestion
     * @param securityAnswer
     * @param overallRating
     * @param dateJoined
     * @param numPeopleTutored
     * @param priceRate
     * @param doNotDisturb
     * @param defaultZip
     * @param currentZip
     * @param transcriptFileLocation
     */
    public Tutor(String email, String firstName, String lastName, String userName, String phoneNumber, String password, String university, String securityQuestion, String securityAnswer, int overallRating, Date dateJoined, int numPeopleTutored, double priceRate, boolean doNotDisturb, String defaultZip, String currentZip, String transcriptFileLocation) {
        super(email, firstName, lastName, userName, phoneNumber, password, university, securityQuestion, securityAnswer);
        this.dateJoinedAsTutor = dateJoined;
        this.numOfPeopleTutored = numPeopleTutored;
        this.hourlyRate = priceRate;
        this.doNotDisturb = doNotDisturb;
        this.overallRating = overallRating;
        this.transcriptFilePath = transcriptFileLocation;
        this.defaultZip = defaultZip;
        this.currentZip = currentZip;
    }
    /**
     * gets the current zip
     * @return current zip
     */
    public String getCurrentZip() {
        return currentZip;
    }
    /**
     * sets the current zip
     * @param currentZip 
     */
    public void setCurrentZip(String currentZip) {
        this.currentZip = currentZip;
    }
    /**
     * gets the number of people tutored
     * @return numOfPeopleTutored
     */
    public int getNumOfPeopleTutored() {
        return numOfPeopleTutored;
    }
    /**
     * sets number of people tutored
     * @param numOfPeopleTutored 
     */
    public void setNumOfPeopleTutored(int numOfPeopleTutored) {
        this.numOfPeopleTutored = numOfPeopleTutored;
    }
    /**
     * gets overall rating
     * @return overallRating
     */
    public int getOverallRating() {
        return overallRating;
    }
    /**
     * sets overall rating
     * @param overallRating 
     */
    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

//    public Collection<Rating> getTutorRatings() {
//        return ratings;
//    }
//
//    public void setTutorRatings(Collection<Rating> ratings) {
//        
//    }
    /**
     * Overridden get ratings
     * @return ratings
     */
    @Override
    public Collection<Rating> getRatings() {
        return ratings;
    }
    /**
     * overridden set ratings
     * @param ratings 
     */
    @Override
    public void setRatings(Collection<Rating> ratings) {
        this.ratings = ratings;
    }

    /**
     * Gets the state of doNotDistrub
     *
     * @return doNotDisturb
     */
    public boolean isDoNotDisturb() {
        return doNotDisturb;
    }

    /**
     * Sets the state of doNotDistrub
     *
     * @param doNotDisturb
     */
    public void setDoNotDisturb(boolean doNotDisturb) {
        this.doNotDisturb = doNotDisturb;
    }

    /**
     * Gets the list of pending requests
     *
     * @return list of pendingRequests
     */
    public Collection<Request> getPendingRequests() {
        return pendingRequests;
    }

    /**
     * Sets the list of pending requests
     *
     * @param pendingRequests
     */
    public void setPendingRequests(Collection<Request> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    /**
     * Gets the list of availabilities
     *
     * @return availabilities
     */
    public Collection<Availability> getAvailabilities() {
        return availabilities;
    }

    /**
     * Sets the list of availabilities
     *
     * @param availabilities
     */
    public void setAvailabilities(Collection<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    /**
     * Sets the date joined by a tutor
     *
     * @param dateJoinedAsTutor
     */
    public void setDateJoinedAsTutor(Date dateJoinedAsTutor) {
        this.dateJoinedAsTutor = dateJoinedAsTutor;
    }

    /**
     * Gets the date joined by a tutor
     *
     * @return the date
     */
    public Date getDateJoinedAsTutor() {
        return dateJoinedAsTutor;
    }

    /**
     * Sets the number of student taught by a tutor
     *
     * @param numPeopleTutored
     */
    public void setNumTutored(int numPeopleTutored) {
        this.numOfPeopleTutored = numPeopleTutored;
    }

    /**
     * Gets the number of student taught by a tutor
     *
     * @return the number of student taught
     */
    public int getNumTutored() {
        return numOfPeopleTutored;
    }

    /**
     * Sets the price rate a tutor wants to be compensated per hour
     *
     * @param hourlyRate
     */
    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    /**
     * Gets the price rate a tutor wants to be compensated per hour
     *
     * @return the price rate
     */
    public double getHourlyRate() {
        return hourlyRate;
    }

    /**
     * Gets the collection of courses a tutor can teach
     *
     * @return the list of courses
     */
    public Collection<Course> getCourses() {
        return courses;
    }

    /**
     * Sets the collection of courses a tutor can teach
     *
     * @param courses the list of courses
     */
    public void setCourse(Collection<Course> courses) {
        this.courses = courses;
    }

    /**
     * Add a course to a collection of Courses if collection of courses is null,
     * create new HashSet
     *
     * @param course
     */
    public void addCourse(Course course) {
        if (this.courses == null) {
            this.courses = new HashSet();
        }
        this.courses.add(course);
    }

    /**
     * get Default Zip
     *
     * @return default zip
     */
    public String getDefaultZip() {
        return defaultZip;
    }

    /**
     * set default zip
     *
     * @param defaultZip
     */
    public void setDefaultZip(String defaultZip) {
        this.defaultZip = defaultZip;
    }
    
    /**
     * gets the list of responses
     * @return responses
     */
    public Collection<Responses> getResponses() {
        return responses;
    }

    /**
     * Sets the list of responses
     *
     * @param responses
     */
    public void setResponses(Collection<Responses> responses) {
        this.responses = responses;
    }
    
    /**
     * Adds a response to a tutor's set of responses
     * @param responses to be added to the set
     */
    public void addResponses(Responses responses){
        if (this.responses == null) {
            this.responses = new HashSet();
        }
        this.responses.add(responses);
    }
    
    /**
     * Adds a pending request to the list
     *
     * @param pr
     */
    public void addPendingRequest(Request pr) {
        if (this.pendingRequests == null) {
            this.pendingRequests = new HashSet();
        }
        this.pendingRequests.add(pr);
    }

    /**
     * Adds a pending rating to the list
     *
     * @param rating
     */
    public void addRating(Rating rating) {
        if (this.ratings == null) {
            this.ratings = new HashSet();
        }
        this.ratings.add(rating);
    }

    /**
     * removes a pending request from the list
     *
     * @param pr
     */
    public void removePendingRequest(Request pr) {
        pendingRequests.remove(pr);
    }
    
    /**
     * removes a course from the tutor
     * @param course 
     */
    public void removeCourse(Course course) {
        courses.remove(course);
    }
    /**
     * Adds an availability to a collection if availability is null, create new
     * HashSet
     *
     * @param availability
     */
    public void addAvailability(Availability availability) {
        if (this.availabilities == null) {
            this.availabilities = new HashSet();
        }
        this.availabilities.add(availability);
    }
    /**
     * gets the transcript file path
     * @return transcript file path
     */
    public String getTranscriptFilePath() {
        return transcriptFilePath;
    }
    /**
     * sets the transcript file path
     * @param transcriptFileLocation 
     */
    public void setTrancriptFileLocation(String transcriptFileLocation) {
        this.transcriptFilePath = transcriptFileLocation;
    }
    /**
     * gets a collection of payments
     * @return 
     */
    public Collection<Payment> getPayments() {
        return payments;
    }
    /**
     * sets a collection of payments
     * @param payments 
     */
    public void setPayments(Collection<Payment> payments) {
        this.payments = payments;
    }
    
     /**
     * Add a payment to a collection if payment is null, create new
     * HashSet
     *
     * @param payment
     */
    public void addPayment(Payment payment) {
        if (this.payments == null) {
            this.payments = new HashSet();
        }
        this.payments.add(payment);
    }
}
