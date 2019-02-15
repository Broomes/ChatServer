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
        roomName = roomName.toLowerCase();
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
        mess.setSender("Chatroom Admin");
        mess.setContent(userName + " has joined the room.");
        mess.setRoom(room);

        room.sendMessage(mess);
    }

    @OnMessage
    public void onMessage(Message message, Session session) {

        logger.info(message.toString());
        message.setRoom(rooms.get(session.getUserProperties().get("roomName")));
        rooms.get(session.getUserProperties().get("roomName")).sendMessage(message);

    }


    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {

        Message mess = new Message();
        Room room = rooms.get(session.getUserProperties().get("roomName"));

        room.leave(session);

        mess.setSender("Chatroom Admin");
        mess.setContent(session.getUserProperties().get("userName") + " has left the room.");
        mess.setRoom(room);

        room.sendMessage(mess);
    }

    @OnError
    public void onError(Throwable t) {
        logger.info("onError::" + t.getMessage());
    }
}
