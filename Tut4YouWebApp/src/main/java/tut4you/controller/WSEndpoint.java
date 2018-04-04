/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import tut4you.messenger.*;
import tut4you.model.*;

/**
 * Connects the users to be in a chat.
 * source: https://netbeans.org/kb/docs/javaee/maven-websocketapi.html#createendpoint
 *         http://www.hascode.com/2013/08/creating-a-chat-application-using-java-ee-7-websockets-and-glassfish-4/
 * @author Amanda
 */
@ApplicationScoped
@DeclareRoles({"tut4youapp.student"})
@ServerEndpoint(value = "/message", encoders = ChatMessageEncoder.class, decoders = ChatMessageDecoder.class)
public class WSEndpoint {

    private static final Logger LOGGER = Logger.getLogger("WSEndpoint");

    @EJB
    Tut4YouApp tut4youApp;
    @Inject
    MessageBean messageBean;
    private Message message;
    private static Set<Session> userSessions = Collections.synchronizedSet(new HashSet<Session>());

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        LOGGER.log(Level.INFO, "Open Connection ..." + session.getId());
        synchronized(userSessions) {
            userSessions.add(session);
        }
        
    }

    @OnClose
    public void onClose(Session session) {
        LOGGER.log(Level.INFO, "Close Connection ...");
        userSessions.remove(session);
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @OnMessage
    public void onMessage(Message message, Session session) {
        try {
            for (Session s : userSessions) {
                for (Session se : s.getOpenSessions()) {
                    if (se.isOpen()) {
                        se.getBasicRemote().sendObject(message); //getBasicRemote = sends synchronusly
                    }
                }
            }
            tut4youApp.saveMessage(message);
        }
        catch (IOException | EncodeException e) {
            LOGGER.log(Level.WARNING, "onMessage failed", e);
        }
    }
}
