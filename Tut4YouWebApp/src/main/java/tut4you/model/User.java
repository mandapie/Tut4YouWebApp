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
import javax.persistence.Table;

/**
 * User is the most basic user type. SINGLE_TABLE is used as our inheritance
 strategy as Tutor inherits all the attributes of a User.
 * @author Keith Tran <keithtran25@gmail.com>
 * @author Syed Haider <shayder426@gmail.com> 
 */
@Table(name="Users")
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String email;
    
    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;
    private String password;
    
    /**
     * A user can submit multiple Requests
     */
    @OneToMany(mappedBy="student", cascade=CascadeType.ALL)
    private Collection<Request> requests;
    
    /**
     * A user can be in multiple Groups and
     * A group can contain multiple users
     */
    @ManyToMany(mappedBy="students", cascade=CascadeType.ALL)
    private Collection<Group> groups;
    
    /**
     * User constructor
     */
    public User() {
        
    }
    
    /**
     * User overloaded constructor
     * @param email
     * @param firstName
     * @param lastName
     * @param userName
     * @param phoneNumber
     * @param password 
     */
    public User(String email, String firstName, String lastName, String userName, String phoneNumber, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        // ADD UNIVERSITY ATtRIBUTE
    }
    
    /**
     * Gets the email of a user
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the email of a user
     * @param email 
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Gets the first name of a user
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Sets the first name of a user
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
     * Sets the last name of a user
     * @param lastName 
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Gets the username of a user
     * @return username
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * Sets the username of a user
     * @param userName 
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * Gets the phone number of a user
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Sets the phone number of a user
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Gets the password of a user
     * @return password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password of a user
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Gets the collection requests submitted by a user
     * @return collection of Requests
     */
    public Collection<Request> getRequests() {
        return requests;
    }
    
    /**
     * Sets the collection requests submitted by a user
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
     * gets the groups that this user is a member of
     * @return a collection of groups that this user belongs to
     */
    public Collection<Group> getGroups() {
        return groups;
    }

    /**
     * sets the groups that this user belongs to
     * @param groups is the collection of groups that this user is a member of
     */
    public void setGroups(Collection<Group> groups) {
        this.groups = groups;
    }
    
    /**
     * Add a group to the user's set of groups
     * @param group to be added
     */
    public void addGroup(Group group) {
        if (this.groups == null)
            this.groups = new HashSet();
        this.groups.add(group);
    }

    /**
     * determines whether or not the information for this user is valid
     * @param confirmPassword the password to be confirmed
     * @return <code>true</code> if this user has valid information; 
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
     * @return true if object is User, else false
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the email fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }
    
    /**
     * Override toString
     * @return User attributes
     */
    @Override
    public String toString() {
        return "tut4you.model.User[ id=" + email + " first name=" + firstName + " last name=" + lastName + " username=" + userName + " phone number=" + phoneNumber + " password=" + password + " ]";
    }
}