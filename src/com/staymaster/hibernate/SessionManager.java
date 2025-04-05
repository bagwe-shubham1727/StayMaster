package com.staymaster.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionManager {
    private static final SessionFactory sessionFactory;
    private static final ThreadLocal<Session> sessionThread = new ThreadLocal<>();

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration()
                    .configure("com/staymaster/hibernate/hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session openSession() {
        Session session = sessionThread.get();
        if (session == null) {
            session = sessionFactory.openSession();
            sessionThread.set(session);
        }
        return session;
    }

    public static void closeSession() {
        Session session = sessionThread.get();
        if (session != null) {
            session.close();
            sessionThread.remove();
        }
    }

    public static void beginTransaction() {
        openSession().beginTransaction();
    }

    public static void commitTransaction() {
        openSession().getTransaction().commit();
    }

    public static void rollbackTransaction() {
        openSession().getTransaction().rollback();
    }

    public static void shutdown() {
        sessionFactory.close();
    }
}
