/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kanushan.smartcampuscw.dao;

import com.kanushan.smartcampuscw.model.Room;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    private static final List<Room> rooms = new ArrayList<>();

    static {
        rooms.add(new Room("LIB-301", "Library Quiet Study", 40));
        rooms.add(new Room("ENG-201", "Engineering Lab", 30));
    }

    public List<Room> getAllRooms() {
        return rooms;
    }

    public Room getRoomById(String roomId) {
        for (Room room : rooms) {
            if (room.getId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }
    
    public boolean deleteRoom(String roomId) {
        Room room = getRoomById(roomId);

        if (room == null) {
            return false;
        }

        rooms.remove(room);
        return true;
    }
}