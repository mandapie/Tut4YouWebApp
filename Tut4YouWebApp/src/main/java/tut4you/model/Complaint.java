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

    public User getModerator() {
        return moderator;
    }

    public void setModerator(User moderator) {
        this.moderator = moderator;
    }
    
    public boolean isIsReviewed() {
        return isReviewed;
    }

    public void setIsReviewed(boolean isReviewed) {
        this.isReviewed = isReviewed;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
        if (!(object instanceof Complaint)) {
            return false;
        }
        Complaint other = (Complaint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tut4you.model.Complaint[ id=" + id + " ]";
    }
    
}