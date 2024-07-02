package com.mirror.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author mirror
 */

    public interface WorldClock extends Remote {
        LocalDateTime getLocalDateTime(String zoneId) throws RemoteException;
    }