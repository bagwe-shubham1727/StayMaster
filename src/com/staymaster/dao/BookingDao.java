package com.staymaster.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.staymaster.models.Booking;

public class BookingDao {

	private SessionFactory sessionFactory;

    public BookingDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public void save(Booking booking) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(booking);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Booking findById(Long bookingId) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Booking.class, bookingId);
        } finally {
            session.close();
        }
    }
    
    public List<Booking> findByUserId(Long userId) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Booking b WHERE b.user.id = :userId";
            return session.createQuery(hql, Booking.class)
                          .setParameter("userId", userId)
                          .getResultList();
        } finally {
            session.close();
        }
    }

    public void update(Booking booking) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(booking);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    


    public void delete(Booking booking) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.remove(booking);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List<Booking> findAll() {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Booking";
            Query<Booking> query = session.createQuery(hql, Booking.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
	
}
