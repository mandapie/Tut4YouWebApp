/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author Amanda
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
    
    @OneToMany(mappedBy="student", cascade=CascadeType.ALL)
    private Collection<Request> requests;
    
    @ManyToMany(mappedBy="students", cascade=CascadeType.ALL)
    private Collection<Group> groups;
    
    public Student() {
        
    }
    public Student(String email, String firstName, String lastName, String userName, String phoneNumber, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Request> getRequests() {
        return requests;
    }

    public void setRequests(Collection<Request> requests) {
        this.requests = requests;
    }
    
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
     */
    public boolean isInformationValid(String confirmPassword) {
        return (firstName != null && lastName != null
                 && email != null && password != null
                 && confirmPassword.equals(password));
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (email != null ? email.hashCode() : 0);
        return hash;
    }

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

    @Override
    public String toString() {
        return "tut4you.model.Student[ id=" + email + " first name=" + firstName + " last name=" + lastName + " username=" + userName + " phone number=" + phoneNumber + " password=" + password + " ]";
    }
}
