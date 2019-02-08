package net.broomes.infrastructure;


import net.broomes.domain.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageEncoder implements Encoder.Text<Message> {

    private static Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

    @Override
    public String encode(Message message) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();
        message.setReceived(dateFormat.format(date));

        return Json.createObjectBuilder()
                .add("sender", message.getSender())
                .add("content", message.getContent())
                .add("time", message.getReceived())
                .build().toString();
    }

    @Override
    public void init(EndpointConfig config) {
        logger.info("Encoder: init method called");
    }

    @Override
    public void destroy() {
        logger.info("Encoder: destroy method called");
    }
}
