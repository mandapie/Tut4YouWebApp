/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import tut4you.messenger.*;
import tut4you.model.*;

/**
 *
 * @author Amanda
 */
@DeclareRoles({"tut4youapp.student"})
@ApplicationScoped
@ServerEndpoint(value = "/chat", encoders = ChatMessageEncoder.class, decoders = ChatMessageDecoder.class)
public class WSEndpoint {
    private static final Logger LOGGER = Logger.getLogger("WSEndpoint");
    
    @EJB
    Tut4YouApp tut4youApp;
    @Inject
    MessageBean messageBean;
    
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    
    @OnOpen
    public void onOpen(final Session session) {
        LOGGER.log(Level.INFO, "Open Connection ...");
        session.getUserProperties();
    }

    @OnClose
    public void onClose(Session session) {
        LOGGER.log(Level.INFO, "Close Connection ...");
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @OnMessage
    public void onMessage(final Message message, Session session) {
        try {
            for (Session s : session.getOpenSessions()) {
                if (s.isOpen()) {
                    s.getBasicRemote().sendObject(message);
                }
            }
            tut4youApp.saveMessage(message);
        }
        catch (IOException | EncodeException e) {
            LOGGER.log(Level.WARNING, "onMessage failed", e);
        }
    }
}
