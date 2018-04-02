/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.messenger;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import tut4you.model.Message;

/**
 * 
 * @author Amanda
 */
public class ChatMessageEncoder implements Encoder.Text<Message> {
	@Override
	public void init(final EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public String encode(final Message chatMessage) throws EncodeException {
            return Json.createObjectBuilder()
                    .add("message", chatMessage.getMessage())
                    .add("sender", chatMessage.getSender())
                    .add("received", chatMessage.getDateSent().toString()).build()
                    .toString();
	}
}
