/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author Keith
 */
@Table(name = "Complaint")
@Entity
@NamedQueries({
    @NamedQuery(name = Complaint.FIND_UNRESOLVED_COMPLAINTS, query = "SELECT c FROM Complaint c WHERE c.isReviewed = :isReviewed"),
    @NamedQuery(name = Complaint.FIND_COMPLAINT_BY_ID, query = "SELECT c FROM Complaint c WHERE c.id = :id")
})
public class Complaint implements Serializable {
    
    /**
     * JPQL Query to find complaints that have not been reviewed yet
     */
    public static final String FIND_UNRESOLVED_COMPLAINTS = "Complaint.findUnresolvedComplaints";
    /**
     * JPQL Query to find complaint by ID
     */
    public static final String FIND_COMPLAINT_BY_ID = "Complaint.findComplaintByID";

    private static final long serialVersionUID = 1L;
    /**
     * primary key of Complaint
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    /**
     * title 
     */
    private String title;
    /**
     * details
     */
    private String details;
    /**
     * boolean to check if complaint has been reviewed
     */
    private boolean isReviewed;
    /**
     * boolean to check if reported user was a tutor during that request
     */
    private boolean isTutor;
    /**
     * many to one relationship between complaint and user(reported user)
     */
    @ManyToOne
    private User reportedUser;
    /**
     * many to one relationship between complaint and user(user submitting complaint)
     */
    @ManyToOne
    private User user;
    /**
     * many to one relationship between complaint and moderator
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
     * @param moderator 
     */
    public void setModerator(User moderator) {
        this.moderator = moderator;
    }
    /**
     * boolean get for isTutor
     * @return boolean
     */
    public boolean isIsTutor() {
        return isTutor;
    }
    /**
     * set boolean isTutor
     * @param isTutor 
     */
    public void setIsTutor(boolean isTutor) {
        this.isTutor = isTutor;
    }
    /**
     * boolean get for isReviewed
     * @return boolean
     */
    public boolean isIsReviewed() {
        return isReviewed;
    }
    /**
     * set boolean isReviewed
     * @param isReviewed 
     */
    public void setIsReviewed(boolean isReviewed) {
        this.isReviewed = isReviewed;
    }
    /**
     * get reported user
     * @return reportedUser
     */
    public User getReportedUser() {
        return reportedUser;
    }
    /**
     * set reported user
     * @param reportedUser 
     */
    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }
    /**
     * get user that is submitting complaint
     * @return user
     */
    public User getUser() {
        return user;
    }   
    /**
     * set user submitting complaint
     * @param user 
     */
    public void setUser(User user) {
        this.user = user;
    }
    /**
     * get complaint ID
     * @return ID
     */
    public Long getId() {
        return id;
    }
    /**
     * set complaint ID
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * get complaint title
     * @return title
     */
    public String getTitle() {
        return title;
    }
    /**
     * set complaint title
     * @param title 
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * get complaint details
     * @return details
     */
    public String getDetails() {
        return details;
    }
    /**
     * set complaint details
     * @param details 
     */
    public void setDetails(String details) {
        this.details = details;
    }
    /**
     * hashCode overrides method
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
        if (!(object instanceof Complaint)) {
            return false;
        }
        Complaint other = (Complaint) object;
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
        return "tut4you.model.Complaint[ id=" + id + " ]";
    }
    
}