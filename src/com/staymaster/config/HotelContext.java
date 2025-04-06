package com.staymaster.config;

import com.staymaster.models.Hotel;

public class HotelContext {
    private static Hotel currentHotel;

    public static Hotel getCurrentHotel() {
        return currentHotel;
    }

    public static void setCurrentHotel(Hotel hotel) {
        currentHotel = hotel;
    }
}