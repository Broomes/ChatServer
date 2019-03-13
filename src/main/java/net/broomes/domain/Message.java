package net.broomes.domain;

public class Message {

    private String content;
    private String sender;
    private String received;
    private Room room;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public static enum Type{
        STARTED, JOINED, ERROR, LEFT, TEXT
    }

    @Override
    public String toString(){
        return "From: " + sender +
                "\nOn: " + received +
                "\nMessage: " + content;
    }
}
