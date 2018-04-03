/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Keith
 */
@Table(name="ZipCode")
@Entity
public class ZipCode implements Serializable {

    public ZipCode() {
        ZipCodesByRadius = new ArrayList();
    }

    public ZipCode(String zipCode, int maxRadius) {
        this.zipCode = zipCode;
        this.maxRadius = maxRadius;
    }

    @Id
    private Long id;
    
    @OneToMany(mappedBy="zipCode",cascade=CascadeType.ALL)
    private Collection<Request> requests;
    
    private String zipCode;

    private int maxRadius;
    
    private List<String> ZipCodesByRadius;

    public List<String> getZipCodesByRadius() {
        return ZipCodesByRadius;
    }

    public void setZipCodesByRadius(List<String> ZipCodesByRadius) {
        this.ZipCodesByRadius = ZipCodesByRadius;
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
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    
}
