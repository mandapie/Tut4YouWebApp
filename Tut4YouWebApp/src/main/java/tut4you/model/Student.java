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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 * Student is the most basic user type. SINGLE_TABLE is used as our inheritance
 * strategy as Tutor inherits all the attributes of a Student.
 * @author Keith Tran <keithtran25@gmail.com>
 * @author Syed Haider <shayder426@gmail.com> 
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String email;
    
    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;
    private String password;
    
    /**
     * A student can submit multiple Requests
     */
    @OneToMany(mappedBy="student", cascade=CascadeType.ALL)
    private Collection<Request> requests;
    
    /**
     * A student can be in multiple Groups and
     * A group can contain multiple Students
     */
    @ManyToMany(mappedBy="students", cascade=CascadeType.ALL)
    private Collection<Group> groups;
    
    /**
     * Student constructor
     */
    public Student() {
        
    }
    
    /**
     * Student overloaded constructor
     * @param email
     * @param firstName
     * @param lastName
     * @param userName
     * @param phoneNumber
     * @param password 
     */
    public Student(String email, String firstName, String lastName, String userName, String phoneNumber, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        // ADD UNIVERSITY ATtRIBUTE
    }
    
    /**
     * Gets the email of a student
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the email of a student
     * @param email 
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Gets the first name of a student
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Sets the first name of a student
     * @param firstName 
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * Gets the last name of a student
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Sets the last name of a student
     * @param lastName 
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Gets the username of a student
     * @return username
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * Sets the username of a student
     * @param userName 
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * Gets the phone number of a student
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Sets the phone number of a student
     * @param phoneNumber 
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Gets the password of a student
     * @return password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password of a student
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Gets the collection requests submitted by a student
     * @return collection of Requests
     */
    public Collection<Request> getRequests() {
        return requests;
    }
    
    /**
     * Sets the collection requests submitted by a student
     * @param requests 
     */
    public void setRequests(Collection<Request> requests) {
        this.requests = requests;
    }
    
    /**
     * Adds a request submitted to the collection of Requests
     * @param request 
     */
    public void addRequest(Request request) {
        if (this.requests == null)
            this.requests = new HashSet();
        this.requests.add(request);
    }
    
    /**
     * gets the groups that this student is a member of
     * @return a collection of groups that this student belongs to
     */
    public Collection<Group> getGroups() {
        return groups;
    }

    /**
     * sets the groups that this student belongs to
     * @param groups is the collection of groups that this student is a member of
     */
    public void setGroups(Collection<Group> groups) {
        this.groups = groups;
    }
    
    /**
     * Add a group to the student's set of groups
     * @param group to be added
     */
    public void addGroup(Group group) {
        if (this.groups == null)
            this.groups = new HashSet();
        this.groups.add(group);
    }

    /**
     * determines whether or not the information for this student is valid
     * @param confirmPassword the password to be confirmed
     * @return <code>true</code> if this student has valid information; 
     *         <code>false</code> otherwise
     * @author Alvaro Monge <alvaro.monge@csulb.edu>
     */
    public boolean isInformationValid(String confirmPassword) {
        return (firstName != null && lastName != null
                 && email != null && password != null
                 && confirmPassword.equals(password));
    }
    
    /**
     * Override hashCode
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (email != null ? email.hashCode() : 0);
        return hash;
    }
    
    /**
     * Overrides the equals method
     * @param object 
     * @return true if object is Student, else false
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the email fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }
    
    /**
     * Override toString
     * @return Student attributes
     */
    @Override
    public String toString() {
        return "tut4you.model.Student[ id=" + email + " first name=" + firstName + " last name=" + lastName + " username=" + userName + " phone number=" + phoneNumber + " password=" + password + " ]";
    }
}