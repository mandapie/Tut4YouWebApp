/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code has been developed by a group of CSULB students working on their 
 *  Computer Science senior project called Tutors4You.
 *  
 *  Tutors4You is a web application that students can utilize to findUser a tutor and
 *  ask them to meet at any location of their choosing. Students that struggle to understand 
 *  the courses they are taking would benefit from this peer to peer tutoring service.
 
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.controller;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import tut4you.model.*;

/**
 * Binds request inputs to the EJB.
 *
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Named
@ViewScoped
public class RequestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("RequestBean");
    @EJB
    private Tut4YouApp tut4youapp;
    @Inject
    private UserBean userbean;
    private Request request;
    private List<Request> pendingRequests;
    private List<Request> acceptedRequests;
    private List<Request> completedRequests;
    private List<Request> declinedRequests;
    
    /**
     * RequestBean encapsulates all the functions/services involved in making a
     * request
     */
    @PostConstruct
    public void createRequestBean() {
        request = new Request();
    }

    /**
     * Gets the Request entity
     *
     * @return the request entity
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the Request entity
     *
     * @param request the request entity
     */
    public void setRequest(Request request) {
        this.request = request;
    }
    
    public List<Request> getPendingRequests() {
        pendingRequests = tut4youapp.getActiveRequest();
        return pendingRequests;
    }

    public void setPendingRequests(List<Request> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }
    
    public List<Request> getAcceptedRequests() {
        acceptedRequests = tut4youapp.getAcceptedRequestList();
        return acceptedRequests;
    }

    public void setAcceptedRequests(List<Request> acceptedRequests) {
        this.acceptedRequests = acceptedRequests;
    }

    public List<Request> getCompletedRequests() {
        completedRequests = tut4youapp.getCompletedRequests();
        return completedRequests;
    }

    public void setCompletedRequests(List<Request> completedRequests) {
        this.completedRequests = completedRequests;
    }
    
    public List<Request> getDeclinedRequests() {
        declinedRequests = tut4youapp.getDeclinedRequest();
        return declinedRequests;
    }

    public void setDeclinedRequests(List<Request> declinedRequests) {
        this.declinedRequests = declinedRequests;
    }
    
    /**
     * Change the status of a request to canceled
     *
     * @param r
     */
    public void cancelRequest(Request r) {
        tut4youapp.cancelRequest(r);
    }

    /**
     * Change the status of a request to declined and remove the request from the list
     *
     * @param r
     */
    public void declineRequest(Request r) {
        tut4youapp.declineRequest(r);
        tut4youapp.removeRequestFromNotification(r);
    }
    
    /**
     * Sets a tutor to the request if tutor accepts
     *
     * @param r
     * @return
     */
    public String setTutorToRequest(Request r) {
        tut4youapp.setTutorToRequest(r);
        return "chat";
    }
    
    public boolean isCheckRequestStudentEmail(User user) {
        String currentUserEmail = userbean.getEmailFromSession();
        return user.getEmail().equals(currentUserEmail);
    }
}