package org.example.passsystem.service;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;


public class PassService {
    private static Logger log;
    private Map<Integer, Integer> entries = new HashMap<>();

    public void Check(int roomId, boolean isEntrance, int keyId) {

        if (!isValidRoom(roomId) || !isValidKey(keyId)) {
            log.error("Invalid data. Non-existent room or key");
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        if (entries.containsKey(keyId)) {
            if (entries.get(keyId) == roomId) {
                if (isEntrance) {
                    log.error("User #" + keyId + " is already in the room");
                    throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
                } else {
                    log.info("User #" + keyId + " left the room #"+ roomId);
                    entries.remove(keyId);
                }
            } else {
                log.error("User #" + keyId + " in another room");
                throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            if (isEntrance) {
                if (keyId % roomId != 0) {
                    log.warn("Room #" + roomId + ": access denied for user #" + keyId);
                    throw new WebApplicationException(Response.Status.FORBIDDEN);
                }
                log.info("User #" + keyId + " has entered the room #"+ roomId);
                entries.put(keyId, roomId);
            } else {
                log.error("User #" + keyId + " is not in this room");
                throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private boolean isValidRoom(int roomId) {
        return roomId >= 1 && roomId <= 5;
    }

    private boolean isValidKey(int keyId) {
        return keyId >= 1 && keyId <= 100000;
    }

    public PassService(String logConfig) {
        PropertyConfigurator.configure(logConfig);
        log = Logger.getLogger(PassService.class.getName());
    }
}
