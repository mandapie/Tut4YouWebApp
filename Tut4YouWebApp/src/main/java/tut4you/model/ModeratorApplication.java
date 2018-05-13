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
 * @author Keith
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
     * JPQL query to retrieve moderator application by user email
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
    
    public ModeratorApplication() {
    }
    
    public ModeratorApplication(String resumeFilePath, String reason) {
        this.resumeFilePath = resumeFilePath;
        this.reason = reason;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    
    private String resumeFilePath;
    
    private String reason;
    
    private ApplicationStatus applicationStatus;

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
    @OneToOne
    private User user;
    
    @ManyToOne
    private User moderator;

    public User getModerator() {
        return moderator;
    }

    public void setModerator(User user) {
        this.moderator = user;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResumeFilePath() {
        return resumeFilePath;
    }

    public void setResumeFilePath(String resumeFilePath) {
        this.resumeFilePath = resumeFilePath;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof ModeratorApplication)) {
            return false;
        }
        ModeratorApplication other = (ModeratorApplication) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tut4you.model.ModeratorApplication[ id=" + id + " ]";
    }
    
}
