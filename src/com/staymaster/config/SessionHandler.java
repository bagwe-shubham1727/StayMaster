package com.staymaster.config;

public class SessionHandler {
		private static String currentSessionId;

	public static String getCurrentSessionId() {
		    return currentSessionId;
	}

	public static void setCurrentSessionId(String sessionId) {
		    currentSessionId = sessionId;
	}

	//CALL THIS METHOD WHILE CLEARING SESSION
    public static void clearSession() {
        currentSessionId = null;
    }
    
    
}
	
