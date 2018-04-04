/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.controller;

import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import tut4you.model.*;

/**
 *
 * @author Amanda
 */
@Named
@ViewScoped
public class MessageBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger("MessageBean");
    
    @EJB
    Tut4YouApp tut4youapp;
    private User user;
    private Message message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    
    /**
     * Creates a new instance of MessageBean
     */
    public MessageBean() {
    }
    
    public Message sendMessage() {
        this.message.setDateSent(new Date(System.currentTimeMillis()));
        return message;
    }
}
