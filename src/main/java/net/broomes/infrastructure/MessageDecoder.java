package net.broomes.infrastructure;

import net.broomes.domain.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageDecoder implements Decoder.Text<Message> {

    private static Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

    @Override
    public Message decode(String jsonText) {
        Message message = new Message();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();

        JsonObject jsonObject = Json.createReader(new StringReader(jsonText)).readObject();
        message.setSender(jsonObject.getString("sender"));
        message.setContent(jsonObject.getString("content"));
        message.setReceived(dateFormat.format(date));
        return message;
    }

    @Override
    public boolean willDecode(String s) {
        if(s != null){return true;}
        else {
            logger.debug("willDecode method failed. Message sent to the server from the client was not a string");
            return false;
        }
    }

    @Override
    public void init(EndpointConfig config) {
        logger.info("Decoder: init method called");
    }

    @Override
    public void destroy() {
        logger.info("Decoder: destroy method called");
    }
}
