/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.controller;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import tut4you.model.Request;
import tut4you.model.*;

/**
 * Has to do with notifications tab such as listing pending requests from students
 * and show the number of notifications.
 * @author Amanda
 */
@Named
@ViewScoped
public class NotifyBean implements Serializable{
    private static final Logger LOGGER = Logger.getLogger("NotifyBean");

    @EJB
    private Tut4YouApp tut4youApp;
    
    private Request request;
    private List<Request> pendingRequestList;
    private int numofNotif;
    
    /**
     * Gets the request
     * @return request
     */
    public Request getRequest() {
        return request;
    }
    
    /**
     * Sets the request
     * @param request 
     */
    public void setRequest(Request request) {
        this.request = request;
    }
    
    /**
     * Gets the list of pending requests
     * @return pendingRequestList
     */
    public List<Request> getPendingRequestList() {
        pendingRequestList = tut4youApp.getPendingRequestForTutor();
        numofNotif = pendingRequestList.size();
        return pendingRequestList;
    }
    
    /**
     * Sets the list of pending requests
     * @param pendingRequestList 
     */
    public void setPendingRequestList(List<Request> pendingRequestList) {
        this.pendingRequestList = pendingRequestList;
    }
    
    /**
     * Gets the state of hasNotif
     * @return true/false
     */
    public boolean isHasNotif() {
        return numofNotif != 0;
    }
    
    /**
     * Gets the number of notifications
     * @return the number of notification
     */
    public int getNumofNotif() {
        return numofNotif;
    }
    
    /**
     * Sets the number of notifications
     * @param numofNotif 
     */
    public void setNumofNotif(int numofNotif) {
        this.numofNotif = numofNotif;
    }
}
