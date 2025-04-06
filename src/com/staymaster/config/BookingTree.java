package com.staymaster.config;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.staymaster.dao.BookingDao;
import com.staymaster.models.Booking;
import com.staymaster.models.User;

public class BookingTree {
    private BookingTreeNode root;

    public BookingTree(SessionFactory sessionFactory) {
    	BookingDao bookingDao = new BookingDao(sessionFactory);
        List<Booking> bookings = bookingDao.findAll();
        this.root = buildBookingTree(bookings);
    }

    private BookingTreeNode buildBookingTree(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            return null;
        }
        Booking rootBooking = bookings.get(0);
        BookingTreeNode root = new BookingTreeNode(rootBooking);

        for (int i = 1; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            insertBooking(root, booking);
        }

        return root;
    }

    private void insertBooking(BookingTreeNode node, Booking booking) {
        if (booking.getCheckInDate().compareTo(node.getBooking().getCheckInDate()) < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new BookingTreeNode(booking));
            } else {
                insertBooking(node.getLeft(), booking);
            }
        } else {
            if (node.getRight() == null) {
                node.setRight(new BookingTreeNode(booking));
            } else {
                insertBooking(node.getRight(), booking);
            }
        }
    }

    public BookingTreeNode getRoot() {
        return root;
    }

    public List<Booking> searchBookingsByUser(User user) {
        List<Booking> bookings = new ArrayList<>();
        searchBookingsByUserHelper(root, user, bookings);
        return bookings;
    }

    private void searchBookingsByUserHelper(BookingTreeNode node, User user, List<Booking> bookings) {
        if (node == null) {
            return;
        }
        if (node.getBooking().getUser().getFirstName().equals(user.getFirstName())) {
            bookings.add(node.getBooking());
        }

        searchBookingsByUserHelper(node.getLeft(), user, bookings);
        searchBookingsByUserHelper(node.getRight(), user, bookings);
    }
}
