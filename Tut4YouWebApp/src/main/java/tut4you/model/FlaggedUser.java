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
 * @author Keith
 */
@Table(name = "FlaggedUser")
@Entity
@NamedQueries({
    @NamedQuery(name = FlaggedUser.FIND_FLAGGED_USER, query = "SELECT c FROM FlaggedUser c WHERE c.user.email = :email"),
})
public class FlaggedUser implements Serializable {

    public FlaggedUser() {
        this.count = 0;
    }
    /**
     * JPQL Query to Flagged User by User email
     */
    public static final String FIND_FLAGGED_USER = "FlaggedUser.findFlaggedUser";
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    
    private int count;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFlagged;
    @ManyToMany
    private Collection<User> moderators;

    @OneToOne
    private User user;
    
    public Collection<User> getModerators() {
        return moderators;
    }

    public void setModerators(Collection<User> moderators) {
        this.moderators = moderators;
    }
    public void addModerator(User moderator) {
        if (this.moderators == null) {
            this.moderators = new HashSet();
        }
        this.moderators.add(moderator);
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    public Date getDateFlagged() {
        return dateFlagged;
    }

    public void setDateFlagged(Date dateFlagged) {
        this.dateFlagged = dateFlagged;
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
        if (!(object instanceof FlaggedUser)) {
            return false;
        }
        FlaggedUser other = (FlaggedUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tut4you.model.FlaggedUser[ id=" + id + " ]";
    }
    
}
