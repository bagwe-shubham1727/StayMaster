package com.staymaster.config;

import java.util.ArrayList;
import java.util.List;

import com.staymaster.models.Booking;

public class SortUtil {

    public static void mergeSort(List<Booking> bookings) {
        if (bookings.size() < 2) {
            return;
        }
        int mid = bookings.size() / 2;
        List<Booking> leftHalf = new ArrayList<>(bookings.subList(0, mid));
        List<Booking> rightHalf = new ArrayList<>(bookings.subList(mid, bookings.size()));

        mergeSort(leftHalf);
        mergeSort(rightHalf);

        merge(bookings, leftHalf, rightHalf);
    }

    private static void merge(List<Booking> bookings, List<Booking> leftHalf, List<Booking> rightHalf) {
        int leftSize = leftHalf.size();
        int rightSize = rightHalf.size();

        int i = 0, j = 0, k = 0;

        while (i < leftSize && j < rightSize) {
            if (leftHalf.get(i).getCheckInDate().compareTo(rightHalf.get(j).getCheckInDate()) <= 0) {
                bookings.set(k++, leftHalf.get(i++));
            } else {
                bookings.set(k++, rightHalf.get(j++));
            }
        }

        while (i < leftSize) {
            bookings.set(k++, leftHalf.get(i++));
        }

        while (j < rightSize) {
            bookings.set(k++, rightHalf.get(j++));
        }
    }
}

