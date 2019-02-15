package net.broomes.domain;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Room implements Function<String, Room> {

    private String name;
    private List<Session> sessions = new ArrayList<>();

    @Override
    public Room apply(String name) {
        return new Room(name);
    }

    public Room(String name) {
        this.name = name;
    }

    public List<String> getUserInRoom(){
        List<String> users = new ArrayList<>();
        for(Session session : sessions){
            users.add(session.getUserProperties().get("userName").toString());
        }
        return users;
    }

    public synchronized void join(Session session) {
        sessions.add(session);
    }

    public synchronized void leave(Session session) {
        sessions.remove(session);
    }

    public synchronized void sendMessage(Message message) {
        sessions.parallelStream()
                .filter(Session::isOpen)
                .forEach(session -> sendMessage(message, session));
    }


    private void sendMessage(Message message, Session session) {
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendListOfActiveRoomes(){

    }
}
