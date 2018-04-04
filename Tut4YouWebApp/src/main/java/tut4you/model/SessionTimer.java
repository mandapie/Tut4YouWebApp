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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Session Timer will keep track of the length of 
 * each tutoring session
 *
 * @author Syed Haider <shayder426@gmail.com>
 */
@Entity
public class SessionTimer implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;


    @OneToOne
    @JoinColumn(name = "request", nullable = false)
    private Request request;
  

    private long startSessionTime;
    private long endSessionTime;
    private long elapsedTimeOfSession;
 

    /**
     * SessionTimer Constructor
     */
    public SessionTimer() {

    }

    /**
     * SessionTimer overloaded constructor
     *
     * @param startSessionTime starting millisecond of the session (typically 0:00)
     * @param endSessionTime ending millisecond of the session 
     * @param elapsedTimeOfSession total milliSeconds of the sessions
     */
    public SessionTimer(Long startSessionTime, Long endSessionTime, Long elapsedTimeOfSession) {
        this.startSessionTime = startSessionTime;
        this.endSessionTime = endSessionTime;
        this.elapsedTimeOfSession = elapsedTimeOfSession;
    }
    
    /**
     *  Gets the id
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id 
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the active request
     *
     * @return request the active request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the active request
     *
     * @param request the active request
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * Gets the start session time
     *
     * @return startSessionTime the start session time
     */
    public long getStartSessionTime() {
        return startSessionTime;
    }

    /**
     * Sets the start session time
     *
     * @param startSessionTime the start session time
     */
    public void setStartSessionTime(long startSessionTime) {
        this.startSessionTime = startSessionTime;
    }

    /**
     * Gets the end session time
     * 
     * @return endSessionTime the end session time
     */
    public long getEndSessionTime() {
        return endSessionTime;
    }

    /**
     * Sets the end session time
     *
     * @param endSessionTime the end session time
     */
    public void setEndSessionTime(long endSessionTime) {
        this.endSessionTime = endSessionTime;
    }

    /**
     * Gets the elapsed time of the session
     * 
     * @return elapsedTimeOfSession the total time of the session
     */
    public long getElapsedTimeOfSession() {
        return elapsedTimeOfSession;
    }

    /**
     * Sets the elapsed time of the session
     * 
     * @param elapsedTimeOfSession the total time of the session
     */
    public void setElapsedTimeOfSession(long elapsedTimeOfSession) {
        this.elapsedTimeOfSession = elapsedTimeOfSession;
    }



    /**
     * Override hashCode
     *
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Overrides the equals method
     *
     * @param object
     * @return true if object is SessionTimer, else false
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SessionTimer)) {
            return false;
        }
        SessionTimer other = (SessionTimer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


    /**
     * Override toString
     *
     * @return SessionTimer attributes
     */
    @Override
    public String toString() {
        return "tut4you.model.SessionTimer[ id=" + id + " startSessionTime=" + startSessionTime + " endSessionTime=" + endSessionTime + " elapsedTimeOfSession=" + elapsedTimeOfSession + " ]";
    }

}