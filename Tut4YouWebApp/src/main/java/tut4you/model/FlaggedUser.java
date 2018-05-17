/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Keith <keithtran25@gmail.com>
 */
@Table(name = "FlaggedUser")
@Entity
@NamedQueries({
    @NamedQuery(name = FlaggedUser.FIND_FLAGGED_USER, query = "SELECT c FROM FlaggedUser c WHERE c.user.email = :email"),
})
public class FlaggedUser implements Serializable {
    /**
     * Flaggeduser constructor
     */
    public FlaggedUser() {
        this.count = 0;
    }
    /**
     * JPQL Query to Flagged User by User email
     */
    public static final String FIND_FLAGGED_USER = "FlaggedUser.findFlaggedUser";
    
    private static final long serialVersionUID = 1L;
    /**
     * id is the primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    /**
     * count is the number of times user has been flagged
     */
    private int count;
    /**
     * date the user is flagged
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFlagged;
    /**
     * Many to many relationshiop between FlaggedUser and Moderator
     */
    @ManyToMany
    private Collection<User> moderators;
    /**
     * One to one relationship between FlaggedUser and User
     */
    @OneToOne
    private User user;
    /**
     * get Moderators
     * @return moderators
     */
    public Collection<User> getModerators() {
        return moderators;
    }
    /**
     * set Moderators
     * @param moderators 
     */
    public void setModerators(Collection<User> moderators) {
        this.moderators = moderators;
    }
    /**
     * add Moderators to collection if collection is not empty
     * @param moderator 
     */
    public void addModerator(User moderator) {
        if (this.moderators == null) {
            this.moderators = new HashSet();
        }
        this.moderators.add(moderator);
    }
    /**
     * get user
     * @return user
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
     * get Count
     * @return count
     */
    public int getCount() {
        return count;
    }
    /**
     * set count
     * @param count 
     */
    public void setCount(int count) {
        this.count = count;
    }
    /**
     * get date flagged
     * @return date flagged
     */
    public Date getDateFlagged() {
        return dateFlagged;
    }
    /**
     * set date flagged
     * @param dateFlagged 
     */
    public void setDateFlagged(Date dateFlagged) {
        this.dateFlagged = dateFlagged;
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
     * overrides hashCode method
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
        if (!(object instanceof FlaggedUser)) {
            return false;
        }
        FlaggedUser other = (FlaggedUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    /**
     * Overrides toString method 
     * @return string
     */
    @Override
    public String toString() {
        return "tut4you.model.FlaggedUser[ id=" + id + " ]";
    }
    
}
