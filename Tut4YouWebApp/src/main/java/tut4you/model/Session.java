/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Session Timer will keep track of the length of each tutoring session
 * @author Syed Haider <shayder426@gmail.com>
 */
@Entity
@Table(name="Sessions")
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    
    @OneToOne
    private Request request;
    
    @OneToOne
    private Payment payment;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    @Temporal(TemporalType.TIMESTAMP)
    private Date startSessionTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endSessionTime;
    private double elapsedTimeOfSession;

    /**
     * Default SessionTimer constructor
     */
    public Session() {
    }

    /**
     * SessionTimer overloaded constructor
     * @param startSessionTime starting millisecond of the session (typically 0:00)
     * @param endSessionTime ending millisecond of the session 
     * @param elapsedTimeOfSession total milliSeconds of the sessions
     */
    public Session(Date startSessionTime, Date endSessionTime, double elapsedTimeOfSession) {
        this.startSessionTime = startSessionTime;
        this.endSessionTime = endSessionTime;
        this.elapsedTimeOfSession = elapsedTimeOfSession;
    }
    
    /**
     * Gets the id
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id 
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the active request
     * @return request the active request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the active request
     * @param request the active request
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * Gets the start session time
     * @return startSessionTime the start session time
     */
    public Date getStartSessionTime() {
        return startSessionTime;
    }

    /**
     * Sets the start session time
     * @param startSessionTime the start session time
     */
    public void setStartSessionTime(Date startSessionTime) {
        this.startSessionTime = startSessionTime;
    }

    /**
     * Gets the end session time
     * @return endSessionTime the end session time
     */
    public Date getEndSessionTime() {
        return endSessionTime;
    }

    /**
     * Sets the end session time
     * @param endSessionTime the end session time
     */
    public void setEndSessionTime(Date endSessionTime) {
        this.endSessionTime = endSessionTime;
    }

    /**
     * Gets the elapsed time of the session
     * @return elapsedTimeOfSession the total time of the session
     */
    public double getElapsedTimeOfSession() {
        return elapsedTimeOfSession;
    }

    /**
     * Sets the elapsed time of the session
     * @param elapsedTimeOfSession the total time of the session
     */
    public void setElapsedTimeOfSession(double elapsedTimeOfSession) {
        this.elapsedTimeOfSession = elapsedTimeOfSession;
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
        if (!(object instanceof Session)) {
            return false;
        }
        Session other = (Session) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "tut4you.model.SessionTimer[ id=" + id + " ]";
    }
    
}