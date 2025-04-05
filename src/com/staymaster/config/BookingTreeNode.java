package com.stayease.config;

import com.stayease.models.Booking;

public class BookingTreeNode {
    private Booking booking;
    private BookingTreeNode left;
    private BookingTreeNode right;

    public BookingTreeNode(Booking booking) {
        this.booking = booking;
        this.left = null;
        this.right = null;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BookingTreeNode getLeft() {
        return left;
    }

    public void setLeft(BookingTreeNode left) {
        this.left = left;
    }

    public BookingTreeNode getRight() {
        return right;
    }

    public void setRight(BookingTreeNode right) {
        this.right = right;
    }
}
