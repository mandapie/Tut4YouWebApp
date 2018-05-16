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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Keith <keithtran25@gmail.com>
 */
@Table(name="ZipCodeByRadius")
@Entity
@NamedQueries({
    @NamedQuery(name = ZipCodeByRadius.FIND_ZIPCODEBYRADIUS, query = "SELECT t.zipCodeByRadius from ZipCodeByRadius t JOIN t.zipCodes r WHERE r.id = :id ")
        
})
public class ZipCodeByRadius implements Serializable {
    /**
     * JPQL Query to obtain a zipCode locations
     */
    public static final String FIND_ZIPCODEBYRADIUS= "ZipCode.FindZipCodeByRadius";
    
    //primary key of ZipCodeByRadius
    @Id
    private String zipCodeByRadius;
    /**
     * many to many relationship between ZipCodeByRadius and ZipCode
     */
    @ManyToMany
    @JoinTable(
        joinColumns={
          @JoinColumn(name="zipCodeByRadius")
        },
        inverseJoinColumns=@JoinColumn(name="zipCode_ID"))
    private Collection<ZipCode> zipCodes;
    /**
     * default constructor
     */
    public ZipCodeByRadius() {
    }
    /**
     * Overloaded constructor
     * @param zipCodeByRadius 
     */
    public ZipCodeByRadius(String zipCodeByRadius) {
        this.zipCodeByRadius = zipCodeByRadius;
    }
    /**
     * get Zip Codes of ZipCodesByRadius Object
     * @return 
     */
    public Collection<ZipCode> getZipCodes() {
        return zipCodes;
    }
    /**
     * set zip codes of ZipCodeByRadius object
     * @param zipCodes 
     */
    public void setZipCodes(Collection<ZipCode> zipCodes) {
        this.zipCodes = zipCodes;
    }
    /**
     * add zip code to ZipCodeByRadius object
     * @param zipCode 
     */
    public void addZipCode(ZipCode zipCode) {
        if (this.zipCodes == null)
            this.zipCodes = new HashSet();
        this.zipCodes.add(zipCode);
    }
    /**
     * get ZipCodeByRadius
     * @return ZipCodeByRadius
     */
    public String getZipCodeByRadius() {
        return zipCodeByRadius;
    }
    /**
     * set ZipCodeByRadius
     * @param zipCodeByRadius 
     */
    public void setZipCodeByRadius(String zipCodeByRadius) {
        this.zipCodeByRadius = zipCodeByRadius;
    }
    /**
     * Override hashCode
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (zipCodeByRadius != null ? zipCodeByRadius.hashCode() : 0);
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
        ZipCodeByRadius other = (ZipCodeByRadius) object;
        if ((this.zipCodeByRadius == null && other.zipCodeByRadius != null) || (this.zipCodeByRadius != null && !this.zipCodeByRadius.equals(other.zipCodeByRadius))) {
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
        return "tut4you.model.ZipCodeByRadius[ zipCodeByRadius: " + zipCodeByRadius ;
    }
}