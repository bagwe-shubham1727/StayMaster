package com.stayease.config;

import java.util.HashMap;
import java.util.Map;

public class RoomStatusManager {
    private static RoomStatusManager instance;
    private Map<Integer, String> roomStatusMap = new HashMap<>();

    private RoomStatusManager() {}

    public static RoomStatusManager getInstance() {
        if (instance == null) {
            instance = new RoomStatusManager();
        }
        return instance;
    }

    public void updateRoomStatus(int roomId, String status) {
        roomStatusMap.put(roomId, status);
    }

    public String getRoomStatus(int roomId) {
        return roomStatusMap.get(roomId);
    }

    public Map<Integer, String> getAllStatuses() {
        return roomStatusMap;
    }
}

