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
import javax.inject.Named;
import tut4you.model.Request;
import tut4you.model.*;

/**
 *
 * @author Amanda
 */
@Named
@SessionScoped
public class NotifyBean implements Serializable{
    private static final Logger LOGGER = Logger.getLogger("NotifyBean");

    @EJB
    private Tut4YouApp tut4youApp;
    private Request request;
    private List<Request> pendingRequestList;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public List<Request> getPendingRequestList() {
        pendingRequestList = tut4youApp.getPendingRequestForTutor();
        return pendingRequestList;
    }

    public void setPendingRequestList(List<Request> pendingRequestList) {
        this.pendingRequestList = pendingRequestList;
    }
}
