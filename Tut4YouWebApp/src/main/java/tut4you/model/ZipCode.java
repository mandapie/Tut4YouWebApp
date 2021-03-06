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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Keith <keithtran25@gmail.com>
 */
@Table(name="ZipCode")
@Entity
@NamedQueries({
    @NamedQuery(name = ZipCode.FIND_LOCATIONS, query = "SELECT t from ZipCode t"),
    @NamedQuery(name = ZipCode.FIND_ZIP_BY_ZIP_MAXRADIUS, query = "SELECT t from ZipCode t WHERE t.currentZipCode = :zipCode AND t.maxRadius = :maxRadius"),
    
        
})
public class ZipCode implements Serializable {
    /**
     * JPQL Query to obtain a currentZipCode locations
     */
    public static final String FIND_LOCATIONS = "ZipCode.FindLocations";
    /**
     * JPQL Query to obtain currentZipCode based on currentZipCode and max radius
     */
    public static final String FIND_ZIP_BY_ZIP_MAXRADIUS = "ZipCode.FindZipByZipMaxRadius";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    /**
     * One to many relationship between ZipCode and Request
     */
    @OneToMany(mappedBy="zipCode",cascade=CascadeType.ALL)
    private Collection<Request> requests;
    /**
     * many to many relationship between ZipCode and ZipCodeByRadius
     */
    @ManyToMany(mappedBy="zipCodes")
    private Collection<ZipCodeByRadius> zipCodesByRadius;    
    //current zip code
    private String currentZipCode;
    //max radius
    private int maxRadius;
    
    /**
     * default constructor
     */
    public ZipCode() {
        
    }
    
    /**
     * overloaded constructor that takes in zipCode and maxRadius
     * @param zipCode
     * @param maxRadius 
     */
    public ZipCode(String zipCode, int maxRadius) {
        this.currentZipCode = zipCode;
        this.maxRadius = maxRadius;
    }
    
    /**
     * overloaded constructor that takes in maxRadius
     * @param maxRadius 
     */
    public ZipCode(int maxRadius) {
        this.currentZipCode = "00000";
        this.maxRadius = maxRadius;
    }
    
    /**
     * Gets the id of a ZipCode
     * @return id of the Request
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the id of a ZipCode
     * @param id of the Request
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * get the zip codes by the radius of the zip code object
     * @return zipCodesByRadius
     */
    public Collection<ZipCodeByRadius> getZipCodesByRadius() {
        return zipCodesByRadius;
    }
    /**
     * set zipCodesByRadius
     * @param zipCodesByRadius 
     */
    public void setZipCodesByRadius(Collection<ZipCodeByRadius> zipCodesByRadius) {
        this.zipCodesByRadius = zipCodesByRadius;
    }
    /**
     * add zip code by radius to the zip code object
     * @param zipCodeByRadius 
     */
    public void addZipCodeByRadius(ZipCodeByRadius zipCodeByRadius) {
        if (this.zipCodesByRadius == null)
            this.zipCodesByRadius = new HashSet();
        this.zipCodesByRadius.add(zipCodeByRadius);
    }
    /**
     * get max radius
     * @return maxRadius
     */
    public int getMaxRadius() {
        return maxRadius;
    }
    /**
     * set max radius
     * @param maxRadius 
     */
    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }
    /**
     * get current zip code
     * @return currentZipCode
     */
    public String getCurrentZipCode() {
        return currentZipCode;
    }
    /**
     * set current zip code
     * @param currentZipCode 
     */
    public void setCurrentZipCode(String currentZipCode) {
        this.currentZipCode = currentZipCode;
    }
    
    /**
     * Adds a request submitted to the collection of Requests
     * @param request 
     */
    public void addRequest(Request request) {
        if (this.requests == null)
            this.requests = new HashSet();
        this.requests.add(request);
    }
    /**
     * Gets the collection requests submitted by a user
     * @return collection of Requests
     */
    public Collection<Request> getRequests() {
        return requests;
    }
    
    /**
     * Sets the collection requests submitted by a user
     * @param requests 
     */
    public void setRequests(Collection<Request> requests) {
        this.requests = requests;
    }
    /**
     * Override hashCode
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
     * @param object 
     * @return true if object is Request, else false
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Request)) {
            return false;
        }
        ZipCode other = (ZipCode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    /**
     * override toString method
     * @return string
     */
    @Override
    public String toString() {
        return "tut4you.model.ZipCode[ id=" + id + " zipCode=" + currentZipCode + " maxRad=" + maxRadius ;
    }
}