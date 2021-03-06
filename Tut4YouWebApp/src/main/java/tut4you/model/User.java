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
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * User is the most basic user type. SINGLE_TABLE is used as our inheritance
 * strategy as Tutor inherits all the attributes of a User.
 *
 * @author Keith Tran <keithtran25@gmail.com>
 * @author Syed Haider <shayder426@gmail.com>
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Table(name = "Users")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "Student")

@NamedQueries({
    @NamedQuery(name = User.FIND_USER_EMAILS, query = "SELECT t.email FROM User t")
    ,
    @NamedQuery(name = User.FIND_USER_USERNAMES, query = "SELECT t.username FROM User t")
    ,
    @NamedQuery(name = User.FIND_USER_BY_EMAIL, query = "SELECT u FROM User u WHERE u.email = :email")
    ,
    @NamedQuery(name = User.FIND_USER_BY_UNAME, query = "SELECT u FROM User u WHERE u.username = :username")
})
public class User implements Serializable {

    /**
     * JPQL Query to obtain a list of users email
     */
    public static final String FIND_USER_EMAILS = "Tutor.FindUserEmails";
    /**
     * JPQL Query to obtain a list of users username
     */
    public static final String FIND_USER_USERNAMES = "Tutor.FindUserUsernames";
    /**
     * JPQL Query to obtain the user by rating
     */
    public static final String FIND_USER_BY_RATING = "User.FindUserByRating";

    /**
     *JPQL Query to obtain the user by email
     */
    public static final String FIND_USER_BY_EMAIL = "User.FindUserByEmail";

    /**
     *JPQL Query to obtain the user by username
     */
    public static final String FIND_USER_BY_UNAME = "User.FindUserByUName";

    private static final long serialVersionUID = 1L;

    @Id
    private String email;

    private String firstName;
    private String lastName;
    @Column(nullable = false, unique = true)
    private String username;
    private String phoneNumber;
    private String password;
    private String university;
    private String securityQuestion;
    private String securityAnswer;
    /**
     * A user can submit multiple Requests
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Collection<Request> requests;
    /**
     * A user can submit multiple Ratings
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Collection<Rating> ratings;
    /**
     * A user can be in multiple Groups and A group can contain multiple users
     */
    @ManyToMany(mappedBy = "students", cascade = CascadeType.ALL)
    private Collection<Group> groups;

    /**
     * One to many with relationship with question.
     * One student can ask many questions
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Collection<Question> questions;
    /**
     * one to many relationship between moderator and moderator applications
     */
    @OneToMany(mappedBy = "moderator", cascade = CascadeType.ALL)
    private Collection<ModeratorApplication> moderatorApplications;
    /**
     * one to one between user and moderator application
     */
    @OneToOne
    private ModeratorApplication moderatorApplication;
    /**
     * many to many between moderators and moderator flagging user
     */
    @ManyToMany(mappedBy = "moderators", cascade = CascadeType.ALL)
    private Collection<FlaggedUser> moderatorFlaggingUser;
    /**
     * one to one between user and flaggedUser
     */
    @OneToOne
    private FlaggedUser flaggedUser;
    /**
     * A user can send multiple payments
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Collection<Payment> payments;
    /**
     * one to many between moderator and complaint
     */
    @OneToMany(mappedBy = "moderator", cascade = CascadeType.ALL)
    private Collection<Complaint> moderatorComplaint;
    /**
     * one to many between user submitting complaint and complaint
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Complaint> userComplaint;
    /**
     * one to many between reported user and complaints
     */
    @OneToMany(mappedBy = "reportedUser", cascade = CascadeType.ALL)
    private Collection<Complaint> reportedUserComplaint;
    /**
     * get moderator application
     * @return moderator application
     */
    public ModeratorApplication getModeratorApplication() {
        return moderatorApplication;
    }
    /**
     * get moderator complaint
     * @return moderator complaint
     */
    public Collection<Complaint> getModeratorComplaint() {
        return moderatorComplaint;
    }
    /**
     * set moderator complaint
     * @param moderatorComplaint 
     */
    public void setModeratorComplaint(Collection<Complaint> moderatorComplaint) {
        this.moderatorComplaint = moderatorComplaint;
    }
    /**
     * get User Complaint
     * @return 
     */
    public Collection<Complaint> getUserComplaint() {
        return userComplaint;
    }
    /**
     * set user Complaint
     * @param userComplaint 
     */
    public void setUserComplaint(Collection<Complaint> userComplaint) {
        this.userComplaint = userComplaint;
    }   
    /**
     * get reported user complaints
     * @return collection of complaints
     */
    public Collection<Complaint> getReportedUserComplaint() {
        return reportedUserComplaint;
    }
    /**
     * set reported user complaints
     * @param reportedUserComplaint 
     */
    public void setReportedUserComplaint(Collection<Complaint> reportedUserComplaint) {
        this.reportedUserComplaint = reportedUserComplaint;
    }
    /**
     * set moderator application
     * @param moderatorApplication 
     */
    public void setModeratorApplication(ModeratorApplication moderatorApplication) {
        this.moderatorApplication = moderatorApplication;
    }

    /**
     * User constructor
     */
    public User() {
    }

    /**
     * Copy constructor
     *
     * @param user
     */
    public User(User user) {
        this.email = user.email;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.username = user.username;
        this.phoneNumber = user.phoneNumber;
        this.password = user.password;
        this.university = user.university;
        this.securityQuestion = user.securityQuestion;
        this.securityAnswer = user.securityAnswer;
    }

    /**
     * User overloaded constructor
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
     */
    public User(String email, String firstName, String lastName, String userName, String phoneNumber, String password, String university, String securityQuestion, String securityAnswer) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = userName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.university = university;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }
    
    /**
     * gets collection of flagged users
     * @return moderatorFlaggingUser
     */
    public Collection<FlaggedUser> getModeratorFlaggingUser() {
        return moderatorFlaggingUser;
    }

    /**
     *  sets a user to a collection of flagged user
     * @param moderatorFlaggingUser
     */
    public void setModeratorFlaggingUser(Collection<FlaggedUser> moderatorFlaggingUser) {
        this.moderatorFlaggingUser = moderatorFlaggingUser;
    }

    /**
     *  sets a flagged user
     * @param moderatorFlaggingUser
     */
    public void addModeratorFlaggingUser(FlaggedUser moderatorFlaggingUser) {
        if (this.moderatorFlaggingUser == null) {
            this.moderatorFlaggingUser = new HashSet();
        }
        this.moderatorFlaggingUser.add(moderatorFlaggingUser);
    }

    /**
     *gets a collection of moderator applications
     * @return
     */
    public Collection<ModeratorApplication> getModeratorApplications() {
        return moderatorApplications;
    }

    /**
     *sets moderator application to a collection of moderator applications
     * @param moderatorApplications
     */
    public void setModeratorApplications(Collection<ModeratorApplication> moderatorApplications) {
        this.moderatorApplications = moderatorApplications;
    }

    /**
     * add moderator application to a hash set
     * @param moderatorApplication
     */
    public void addModeratorApplication(ModeratorApplication moderatorApplication) {
        if (this.moderatorApplications == null) {
            this.moderatorApplications = new HashSet();
        }
        this.moderatorApplications.add(moderatorApplication);
    }

    /**
     * get the flagged user
     *
     * @return flaggedUser
     */
    public FlaggedUser getFlaggedUser() {
        return flaggedUser;
    }

    /**
     * set the flagged user
     *
     * @param flaggedUser
     */
    public void setFlaggedUser(FlaggedUser flaggedUser) {
        this.flaggedUser = flaggedUser;
    }

    /**
     * Gets the email of a user
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of a user
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the first name of a user
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of a user
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of a student
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of a user
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the username of a user
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of a user
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the phone number of a user
     *
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of a user
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the password of a user
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of a user
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * gets the university of the user
     *
     * @return university
     */
    public String getUniversity() {
        return university;
    }

    /**
     * gets the university of the user
     *
     * @param university
     */
    public void setUniversity(String university) {
        this.university = university;
    }

    /**
     * gets the security question
     *
     * @return securityQuestion
     */
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * sets the security question
     *
     * @param securityQuestion
     */
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    /**
     * gets the security answer
     *
     * @return securityAnswer
     */
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    /**
     * gets the security answer
     *
     * @param securityAnswer
     */
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    /**
     * get a list of ratings
     *
     * @return list of ratings
     */
    public Collection<Rating> getRatings() {
        return ratings;
    }

    /**
     * sets a list of ratings
     *
     * @param ratings
     */
    public void setRatings(Collection<Rating> ratings) {
        this.ratings = ratings;
    }

    /**
     * Gets the collection requests submitted by a user
     *
     * @return collection of Requests
     */
    public Collection<Request> getRequests() {
        return requests;
    }

    /**
     * Sets the collection requests submitted by a user
     *
     * @param requests
     */
    public void setRequests(Collection<Request> requests) {
        this.requests = requests;
    }

    /**
     * gets a collection of questions
     * @return questions
     */
    public Collection<Question> getQuestion() {
        return questions;
    }
    /**
     * sets a collection of questions
     * @param questions collection to be set
     */
    public void setQuestion(Collection<Question> questions) {
        this.questions = questions;
    }
    /**
     * gets the groups that this user is a member of
     *
     * @return a collection of groups that this user belongs to
     */
    public Collection<Group> getGroups() {
        return groups;
    }

    /**
     * sets the groups that this user belongs to
     *
     * @param groups is the collection of groups that this user is a member of
     */
    public void setGroups(Collection<Group> groups) {
        this.groups = groups;
    }

    /**
     * get payments
     * @return payments
     */
    public Collection<Payment> getPayments() {
        return payments;
    }

    /**
     * set payments
     * @param payments
     */
    public void setPayments(Collection<Payment> payments) {
        this.payments = payments;
    }
      /**
     * Adds a payment  to the collection of Payments
     *
     * @param payment
     */
    public void addPayment(Payment payment) {
        if (this.payments == null) {
            this.payments = new HashSet();
        }
        this.payments.add(payment);
    }

    

    /**
     * Adds a request submitted to the collection of Requests
     *
     * @param request
     */
    public void addRequest(Request request) {
        if (this.requests == null) {
            this.requests = new HashSet();
        }
        this.requests.add(request);
    }

    /**
     * Adds a rating submitted to the collection of Rating
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
     * Add a group to the user's set of groups
     *
     * @param group to be added
     */
    public void addGroup(Group group) {
        if (this.groups == null) {
            this.groups = new HashSet();
        }
        this.groups.add(group);
    }
    
    /**
     * Adds a question to a user's set of questions
     * @param questions to be added to the set
     */
    public void addQuestion(Question questions){
        if (this.questions == null) {
            this.questions = new HashSet();
        }
        this.questions.add(questions);
    }

    /**
     * determines whether or not the information for this user is valid
     *
     * @param confirmPassword the password to be confirmed
     * @return <code>true</code> if this user has valid information;
     * <code>false</code> otherwise
     * @author Alvaro Monge <alvaro.monge@csulb.edu>
     */
    public boolean isInformationValid(String confirmPassword) {
        return (firstName != null && lastName != null
                && email != null && password != null
                && confirmPassword.equals(password));
    }

    /**
     * gets the user type from the discriminator column
     * https://stackoverflow.com/questions/15208793/getting-the-value-of-the-discriminator-column
     *
     * @return the user type
     */
    public String getDecriminatorValue() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value();
    }

    /**
     * Override hashCode
     *
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
     *
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
     *
     * @return User attributes
     */
    @Override
    public String toString() {
        return "tut4you.model.User[ id=" + email + " first name=" + firstName + " last name=" + lastName + " username=" + username + " phone number=" + phoneNumber + " ]";
    }
}
