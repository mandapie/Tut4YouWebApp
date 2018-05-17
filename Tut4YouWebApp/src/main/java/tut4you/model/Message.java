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
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Chat message that allows users to communicate with each other.
 * @author Amanda
 */
@Entity
@Table(name="Message")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSent;
    @OneToOne
    private User user;
    private String sender;
    private String message;
    /**
     * gets the id
     * @return id
     */
    public Long getId() {
        return id;
    }
    /**
     * sets the id
     * @param id to be set
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * gets the date sent
     * @return dateSent
     */
    public Date getDateSent() {
        return dateSent;
    }   
    /**
     * sets the date sent
     * @param dateSent 
     */
    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }
    /**
     * gets a message
     * @return message
     */
    public String getMessage() {
        return message;
    }
    /**
     * sets a message
     * @param message to be set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * gets a user
     * @return user
     */
    public User getUser() {
        return user;
    }
    /**
     * sets a user
     * @param user to be set
     */
    public void setUser(User user) {
        this.user = user;
    }
    /**
     * gets sender
     * @return sender
     */
    public String getSender() {
        return sender;
    }
    /**
     * set a sender
     * @param sender to be set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }
    /**
     * overridden hashcode
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    /**
     * overridden equals method
     * @param object
     * @return true or false
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    /**
     * overridden tostring method
     * @return 
     */
    @Override
    public String toString() {
        return "tut4you.model.Messenger[ id=" + id + " ]";
    }
}
