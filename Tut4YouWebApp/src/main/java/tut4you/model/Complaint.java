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
import javax.persistence.OneToOne;
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
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    
    private String title;
    
    private String details;
    
    private boolean isReviewed;
    
    @ManyToOne
    private User reportedUser;
    
    @ManyToOne
    private User user;
    
    @ManyToOne 
    private User moderator;
    
    @OneToOne 
    private Request request;
    
    /**
     * get request
     * @return request
     */
    public Request getRequest() {
        return request;
    }
    /**
     * set request
     * @param request 
     */
    public void setRequest(Request request) {
        this.request = request;
    }
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
     * boolean get for isReviewed
     * @return 
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
     * hashcode overrided method
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    /**
     * overrided equals method
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
     * overrided toString method
     * @return string
     */
    @Override
    public String toString() {
        return "tut4you.model.Complaint[ id=" + id + " ]";
    }
    
}