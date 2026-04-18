/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kanushan.smartcampuscw.resource;

import com.kanushan.smartcampuscw.dao.RoomDAO;
import com.kanushan.smartcampuscw.model.Room;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rooms")
public class RoomResource {

    private RoomDAO roomDAO = new RoomDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getRooms() {
        return roomDAO.getAllRooms();
    }
}