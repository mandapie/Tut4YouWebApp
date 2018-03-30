/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.message;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;
import tut4you.model.*;

/**
 *
 * @author Amanda
 */
@ServerEndpoint("/chat")
public class WSEndpoint {
    private static final Logger LOGGER = Logger.getLogger("WSEndpoint");
    
    @EJB
    Tut4YouApp tut4youapp;
    
    @OnOpen
    public void onOpen() {
        LOGGER.log(Level.INFO, "Open Connection ...");
    }

    @OnClose
    public void onClose() {
        LOGGER.log(Level.INFO, "Close Connection ...");
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @OnMessage
    public String onMessage(String message) {
        String echoMsg = message;
        
        return echoMsg;
    }
}
