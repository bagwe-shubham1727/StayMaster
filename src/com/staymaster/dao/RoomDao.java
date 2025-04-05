package com.staymaster.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.staymaster.config.RoomStatusManager;
import com.staymaster.models.Room;

public class RoomDao {

	private SessionFactory sessionFactory;
    private RoomStatusManager statusManager;


    public RoomDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    
    private void initializeRoomStatuses() {
        List<Room> rooms = findAll();
        rooms.forEach(room -> statusManager.updateRoomStatus(room.getRoomId(), room.getRoomStatus()));
    }

    public void save(Room room) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(room);
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

    public Room findById(Long roomId) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Room.class, roomId);
        } finally {
            session.close();
        }
    }

    public void update(Room room) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(room);
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

    public void delete(Room room) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.remove(room);
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

    public List<Room> findAll() {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Room";
            Query<Room> query = session.createQuery(hql, Room.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    
    public Map<Integer, String> findAllRoomsAndUpdateStatus() {
        RoomStatusManager statusManager = RoomStatusManager.getInstance(); // Singleton access
        Session session = sessionFactory.openSession();
        Map<Integer, String> roomStatusMap = new HashMap<>();
        try {
            String hql = "FROM Room";
            Query<Room> query = session.createQuery(hql, Room.class);
            List<Room> rooms = query.getResultList();
            // Update the singleton RoomStatusManager and local map
            rooms.forEach(room -> {
                statusManager.updateRoomStatus(room.getRoomId(), room.getRoomStatus());
                roomStatusMap.put(room.getRoomId(), room.getRoomStatus());
            });
        } finally {
            session.close();
        }
        return roomStatusMap;
    }

    
    
    

    public Room findByType(String type) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Room WHERE roomtype = :type";
            Query<Room> query = session.createQuery(hql, Room.class);
            query.setParameter("type", type);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }
	
	
	
}
