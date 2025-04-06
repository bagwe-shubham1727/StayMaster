package com.staymaster.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.staymaster.models.User;

public class UserSessionManager {
    private static Map<String, UserSession> sessions = new HashMap<>();

    public static String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, new UserSession(user));
        return sessionId;
    }

    public static UserSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public static void endSession(String sessionId) {
        sessions.remove(sessionId);
    }
}



