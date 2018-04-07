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
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
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
