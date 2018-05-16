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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Keith <keithtran25@gmail.com
 */
@Table(name = "ModeratorApplication")
@Entity
@NamedQueries({
    @NamedQuery(name = ModeratorApplication.FIND_ALL_MODERATOR_APPLICATIONS, query = "SELECT s FROM ModeratorApplication s where s.applicationStatus = :applicationStatus"),
    @NamedQuery(name = ModeratorApplication.FIND_MODERATOR_APPLICATION_BY_UNAME, query = "SELECT s FROM ModeratorApplication s WHERE s.user.username = :username")
})
public class ModeratorApplication implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * JPQL query to retrieve all moderator application entities
     */
    public static final String FIND_ALL_MODERATOR_APPLICATIONS = "ModeratorApplication.findAllModeratorApplications";
    
    /**
     * JPQL query to retrieve moderator application by user username
     */
    public static final String FIND_MODERATOR_APPLICATION_BY_UNAME = "ModeratorApplication.findModeratorApplicationByUName";
    
    /**
     * Tells whether a moderation application is pending, accepted or canceled
     * http://tomee.apache.org/examples-trunk/jpa-enumerated/README.html
     */
    public enum ApplicationStatus{
        PENDING,
        ACCEPTED,
        DECLINED;
    }
    /**
     * default constructor
     */
    public ModeratorApplication() {
    }
    /**
     * overloaded constructor
     * @param resumeFilePath
     * @param reason 
     */
    public ModeratorApplication(String resumeFilePath, String reason) {
        this.resumeFilePath = resumeFilePath;
        this.reason = reason;
    }
    /**
     * primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    /**
     * resume file path
     */
    private String resumeFilePath;
    /**
     * reason for application
     */
    private String reason;
    /**
     * application status
     */
    private ApplicationStatus applicationStatus;
    /**
     * get Application Status
     * @return ApplicationStatus
     */
    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }
    /**
     * set Application Status
     * @param applicationStatus 
     */
    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
    /**
     * One to one relationship between moderator application and user
     */
    @OneToOne
    private User user;
    /**
     * many to one relationship between moderator application and moderator
     */
    @ManyToOne
    private User moderator;
    /**
     * get moderator
     * @return moderator
     */
    public User getModerator() {
        return moderator;
    }
    /**
     * set moderator
     * @param user 
     */
    public void setModerator(User user) {
        this.moderator = user;
    }
    /**
     * get User
     * @return User
     */
    public User getUser() {
        return user;
    }
    /**
     * set user
     * @param user 
     */
    public void setUser(User user) {
        this.user = user;
    }
    /**
     * get reason for application
     * @return reason
     */
    public String getReason() {
        return reason;
    }
    /**
     * set reason for application
     * @param reason 
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
    /**
     * get resume file path of application
     * @return resume file path
     */
    public String getResumeFilePath() {
        return resumeFilePath;
    }
    /**
     * set resume file path of application
     * @param resumeFilePath 
     */
    public void setResumeFilePath(String resumeFilePath) {
        this.resumeFilePath = resumeFilePath;
    }
    /**
     * get id
     * @return id
     */
    public Long getId() {
        return id;
    }
    /**
     * set id
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * overrides hashcode method
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    /**
     * overrides equals method
     * @param object
     * @return boolean
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModeratorApplication)) {
            return false;
        }
        ModeratorApplication other = (ModeratorApplication) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    /**
     * overrides toString method
     * @return string
     */
    @Override
    public String toString() {
        return "tut4you.model.ModeratorApplication[ id=" + id + " ]";
    }
    
}
