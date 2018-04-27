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
 * @author Keith
 */
@Table(name="ZipCode")
@Entity
@NamedQueries({
    @NamedQuery(name = ZipCode.FIND_LOCATIONS, query = "SELECT t from ZipCode t"),
    @NamedQuery(name = ZipCode.FIND_ZIP_BY_ZIP_MAXRADIUS, query = "SELECT t from ZipCode t WHERE t.zipCode = :zipCode AND t.maxRadius = :maxRadius")
})
public class ZipCode implements Serializable {
    /**
     * JPQL Query to obtain a zipCode locations
     */
    public static final String FIND_LOCATIONS = "ZipCode.FindLocations";
    /**
     * JPQL Query to obtain zipCode based on zipCode and max radius
     */
    public static final String FIND_ZIP_BY_ZIP_MAXRADIUS = "ZipCode.FindZipByZipMaxRadius";
    
    public ZipCode() {
        
    }

    public ZipCode(String zipCode, int maxRadius) {
        this.zipCode = zipCode;
        this.maxRadius = maxRadius;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    @OneToMany(mappedBy="zipCode",cascade=CascadeType.ALL)
    private Collection<Request> requests;
    @ManyToMany(mappedBy="zipCodes", cascade=CascadeType.PERSIST)
    private Collection<ZipCodeByRadius> zipCodesByRadius;    
    private String zipCode;
    private int maxRadius;
    
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

    public Collection<ZipCodeByRadius> getZipCodesByRadius() {
        return zipCodesByRadius;
    }

    public void setZipCodesByRadius(Collection<ZipCodeByRadius> zipCodesByRadius) {
        this.zipCodesByRadius = zipCodesByRadius;
    }
    
    public void addZipCodeByRadius(ZipCodeByRadius zipCodeByRadius) {
        if (this.zipCodesByRadius == null)
            this.zipCodesByRadius = new HashSet();
        this.zipCodesByRadius.add(zipCodeByRadius);
    }
    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }
    
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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
    @Override
    public String toString() {
        return "tut4you.model.ZipCode[ id=" + id + " zipCode=" + zipCode + " maxRad=" + maxRadius ;
    }
    
    
}