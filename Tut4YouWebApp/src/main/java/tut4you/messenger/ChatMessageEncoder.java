/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.messenger;

import java.text.SimpleDateFormat;
import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import tut4you.model.Message;

/**
 * Gets the attributes from the Message entity to create a JSON
 * source: http://www.hascode.com/2013/08/creating-a-chat-application-using-java-ee-7-websockets-and-glassfish-4/
 * @author Micha Kops
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
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss a");
            String time = sdf.format(chatMessage.getDateSent());
            return Json.createObjectBuilder()
                    .add("message", chatMessage.getMessage())
                    .add("sender", chatMessage.getSender())
                    .add("received", time).build()
                    .toString();
	}
}
