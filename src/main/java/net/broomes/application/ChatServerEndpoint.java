package net.broomes.application;

import net.broomes.domain.Message;
import net.broomes.domain.Room;
import net.broomes.infrastructure.MessageDecoder;
import net.broomes.infrastructure.MessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint(value = "/chat/{roomName}/{userName}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ChatServerEndpoint {


    private static Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

    private static final Map<String, Room> rooms = Collections.synchronizedMap(new HashMap<String, Room>());
    private static List<String> roomNames = new ArrayList<>();

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("roomName") String roomName,
                       @PathParam("userName") String userName )  throws IOException, EncodeException {
        session.setMaxIdleTimeout(5 * 60 * 1000);
        session.getUserProperties().putIfAbsent("roomName", roomName);
        session.getUserProperties().putIfAbsent("userName", userName);
        if(!roomNames.contains(roomName)){
            roomNames.add(roomName);
            rooms.put(roomName, new Room(roomName));
        }
        Room room = rooms.get(roomName);
        room.join(session);

        Message mess = new Message();
        mess.setSender(userName);
        mess.setContent("Welcome to the chatroom.");

        session.getBasicRemote().sendObject(mess);
    }

    @OnMessage
    public void onMessage(Message message, Session session) {

        logger.info(message.toString());
        rooms.get(session.getUserProperties().get("roomName")).sendMessage(message);

    }


    @OnClose
    public void onClose(Session session) {

        rooms.get(session.getUserProperties().get("roomName")).leave(session);
    }

    @OnError
    public void onError(Throwable t) {
        logger.info("onError::" + t.getMessage());
    }
}
