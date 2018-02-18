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
import javax.persistence.Column;
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
 defines a user as a tutor. A tutor is also a student.
 * @author Keith Tran <keithtran25@gmail.com>
 * @author Syed Haider <shayder426@gmail.com>
 */
@Table(name="Tutor")
@DiscriminatorColumn(name="user_type", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue(value="Tutor")
@Entity
@NamedQueries({
    @NamedQuery(name = Tutor.FIND_TUTORS_BY_COURSE, query = "SELECT t FROM Tutor t JOIN t.courses c WHERE c.courseName = :coursename")
})
public class Tutor extends User implements Serializable {     
    /**
     * JPQL Query to obtain a list of tutors who taught a specific course
     */
    public static final String FIND_TUTORS_BY_COURSE = "Tutor.findTutorsByCourse";
    
    /**
     * dateJoined the date a tutor joins
     */
    @Column(nullable = true)
    @Temporal( TemporalType.DATE )
    private Date dateJoined;
    
    /**
     * numPeopleTutored the number of people a tutor tutored
     */
    @Column(nullable = true)
    private int numPeopleTutored;

    //@Column(nullable = true)
    private double priceRate;
    
    /**
     * A Tutor can tutor multiple Courses and
     * a Course can be tutored by multiple Tutors.
     */
    @ManyToMany(mappedBy="tutors", cascade=CascadeType.ALL)
    private Collection<Course> courses;
    
//    /**
//     * A Tutor can be in multiple Groups and
//     * A Group can contain multiple Tutors
//     */
//    @ManyToMany(mappedBy="tutors", cascade=CascadeType.ALL)
//    private Collection<Group> groups;
    
    /**
     * A tutor can set multiple Availabilities
     */
    @OneToMany(mappedBy="tutor", cascade=CascadeType.ALL)
    private Collection<Availability> availability;
    
    /**
     * Tutor constructor
     */
    public Tutor() {
        priceRate = 0.00;
    }
    
    /**
     * Tutor overloaded constructor with existing attributes
     * @param dateJoined
     * @param numPeopleTutored
     * @param priceRate 
     */
    public Tutor(Date dateJoined, int numPeopleTutored, double priceRate) {
        this.dateJoined = dateJoined;
        this.numPeopleTutored = numPeopleTutored;
        this.priceRate = priceRate;
        //groups = new HashSet<>();
    }
    
    /**
     * Inherits existing data from User to convert user type
     * @param user 
     */
    public Tutor(User user) {
        super.setEmail(user.getEmail());
    }
    
    /**
     * Tutor overloaded constructor with inherited and existing attributes
     * @param email
     * @param firstName
     * @param lastName
     * @param userName
     * @param phoneNumber
     * @param password
     * @param dateJoined
     * @param numPeopleTutored
     * @param priceRate 
     */
    public Tutor(String email, String firstName, String lastName, String userName, String phoneNumber, String password, Date dateJoined, int numPeopleTutored, double priceRate) {
        super(email, firstName, lastName, userName, phoneNumber, password);
        this.dateJoined = dateJoined;
        this.numPeopleTutored = numPeopleTutored;
        this.priceRate = priceRate;
        //groups = new HashSet<>();
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
    public void setTimeWorked(Date dateJoined) {
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
    public void setCourse(Collection<Course> couses) {
        this.courses = courses;
    }
    
//    /**
//     * Gets a collection of groups a tutor is in
//     * @return the collection of groups
//     */
//    @Override
//    public Collection<Group> getGroups(){
//        return groups;
//    }
//    
//    /**
//     * Add a group to the tutor's set of groups
//     * if collection of groups is null, create new HashSet
//     * @param group to be added
//     */
//    @Override
//    public void setGroups(Collection<Group> groups) {
//        this.groups = groups;
//    }
//
//    /**
//     * Add a group to the tutor's set of groups
//     * if collection of groups is null, create new HashSet
//     * @param group to be added
//     */
//    @Override
//    public void addGroup(Group group) {
//        if (this.groups == null)
//            this.groups = new HashSet();
//        this.groups.add(group);
//    }
    
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
}