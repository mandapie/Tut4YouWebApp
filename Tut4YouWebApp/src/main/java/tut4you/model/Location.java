/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Keith
 */
@Table(name="Location")
@Entity
public class Location implements Serializable {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    
    @OneToMany(mappedBy="location", cascade={CascadeType.ALL})
    private Collection<Tutor> tutors;

    public Collection<Tutor> getTutors() {
        return tutors;
    }

    public void setTutors(Collection<Tutor> tutors) {
        this.tutors = tutors;
    }
    /**
     * adds tutor to collection of tutors
     * create new HashSet if tutors collection is null
     * @param tutor
     */
    public void addTutor(Tutor tutor) {
        if (this.tutors == null)
            this.tutors = new HashSet();
        this.tutors.add(tutor);
    }
    /**
     * Tutors default zip code location
     */
    private String defaultZip;
    /**
     * Tutors current zip code location
     */
    private String currentZip;
    /**
     * Tutors max radius
     */
    private int maxRadius;

    public Location() {
    }
    
    /**
     * Location constructor
     * @param defaultZip
     * @param currentZip
     * @param maxRadius 
     */
    public Location(String defaultZip, String currentZip, int maxRadius) {
        this.defaultZip = defaultZip;
        this.currentZip = currentZip;
        this.maxRadius = maxRadius;
    }
    /**
     * get Default Zip
     * @return default zip
     */
    public String getDefaultZip() {
        return defaultZip;
    }
    /**
     * set default zip
     * @param defaultZip 
     */
    public void SetDefaultZip(String defaultZip) {
        this.defaultZip = defaultZip;
    }
    /**
     * get current zip
     * @return currentZip
     */
    public String getCurrentZip() {
        return currentZip;
    }
    /**
     * set current zip
     * @param currentZip 
     */
    public void setCurrentZip(String currentZip) {
        this.currentZip = currentZip;
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
    
    
}
