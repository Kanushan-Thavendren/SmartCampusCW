/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kanushan.smartcampuscw.resource;

import com.kanushan.smartcampuscw.model.Room;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
public class RoomResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getRooms() {

        List<Room> rooms = new ArrayList<>();

        rooms.add(new Room(1, "Lab 1", "Building A", 40));
        rooms.add(new Room(2, "Lecture Hall", "Building B", 100));

        return rooms;
    }
}